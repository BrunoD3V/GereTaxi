package p4.geretaxi;

import android.content.Context;
import android.os.StrictMode;
import android.util.Xml;
import android.widget.Toast;

import com.google.maps.GeoApiContext;
import com.google.maps.RoadsApi;
import com.google.maps.android.SphericalUtil;
import com.google.maps.model.LatLng;
import com.google.maps.model.SnappedPoint;

import org.apache.commons.collections4.ListUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

public class ServicoHandler {

    private static final double LATPTAXI = 41.484205;
    private static final double LNGPTAXI = -7.183378;
    private static Context context;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/xml?";
    private static final String GOOGLE_API_KEY = "AIzaSyCmOQIe5TjVlSpu5tQJFdHP4amgpo8gJ1M";


    private static final int PAGE_SIZE_LIMIT = 100;

    private static final int PAGINATION_OVERLAP = 5;

    Boolean portagens = false;

    double distance;

    List<LatLng> mCapturedLocations;
    List<SnappedPoint> mSnappedPoints;
    List<LatLng> routes;

    public ServicoHandler(Context c) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        context = c;
    }


    public ServicoHandler() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

    }


    public double getDistance() {
        distance = distance/1000.0;
        distance = Math.round(distance*10.0)/10.0;
        return distance;
    }

    public Boolean getPortagens() {
        return portagens;
    }

    private List<LatLng> getDirections(String origin, String destination) {
        try {
            String urlOrigin = URLEncoder.encode(origin, "utf-8");
            String urlDestination = URLEncoder.encode(destination, "utf-8");
            String link = DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&key=" + GOOGLE_API_KEY;
            URL url = new URL(link);
            InputStream is = url.openConnection().getInputStream();
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            XMLHandler parser = new XMLHandler();
            portagens =parser.getPortagem(Xml.newPullParser(), buffer.toString());
            Toast.makeText(context, portagens.toString(),Toast.LENGTH_SHORT).show();
            distance = parser.parseDistance(buffer.toString());
            return parser.parseDirections(Xml.newPullParser(), buffer.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LatLng> mergeCapture(String processo) {
        try {
            distance = 0.0;
            XMLHandler parser = new XMLHandler();
            mCapturedLocations = parser.loadGpxData(Xml.newPullParser(), processo);
            double dis = getDistance(mCapturedLocations);

            LatLng ptaxis = new LatLng(LATPTAXI, LNGPTAXI);
            String origin = ptaxis.toString();
            String destination = mCapturedLocations.get(0).toString();
            String termino = mCapturedLocations.get(mCapturedLocations.size()-1).toString();

            routes = getDirections(origin, destination);
            dis += distance;
            mCapturedLocations= ListUtils.union(routes,mCapturedLocations);

            routes = null;
            routes = getDirections(termino, origin);
            dis += distance;
            distance = dis;
            System.out.println(dis);
            mCapturedLocations= ListUtils.union(mCapturedLocations, routes);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

     return mCapturedLocations;
    }

    public List<LatLng> getRoute(List<LatLng> capturedLocations, GeoApiContext context) throws Exception {

        mCapturedLocations = capturedLocations;
        mSnappedPoints = snapToRoads(context);
        mCapturedLocations = getVisitedPlaces();
        return mCapturedLocations;
    }

    public Double getDistance( List<LatLng> latLngs) {

        double distance = 0.0;


        for(int i = 0;i < latLngs.size()-1; i++) {
            com.google.android.gms.maps.model.LatLng to =
                    new com.google.android.gms.maps.model.LatLng(latLngs.get(i+1).lat, latLngs.get(i+1).lng);


            com.google.android.gms.maps.model.LatLng from =
                    new com.google.android.gms.maps.model.LatLng(latLngs.get(i).lat, latLngs.get(i).lng);

            distance += SphericalUtil.computeDistanceBetween(from, to);
        }
        distance = distance/1000.0;
        distance = Math.round(distance*10.0)/10.0;
        return distance;
    }

    private List<LatLng> getVisitedPlaces() {

        List<LatLng>latLngs = new ArrayList<>();

        for (SnappedPoint point : mSnappedPoints) {
            if (!latLngs.contains(point.placeId)) {
                latLngs.add(point.location);
            }
        }
        return  latLngs;
    }

    private List<SnappedPoint> snapToRoads(GeoApiContext context) throws Exception {

        List<SnappedPoint> snappedPoints = new ArrayList<>();

        int offset = 0;
        while (offset < mCapturedLocations.size()) {
            // Calculate which points to include in this request. We can't exceed the APIs
            // maximum and we want to ensure some overlap so the API can infer a good location for
            // the first few points in each request.
            if (offset > 0) {
                offset -= PAGINATION_OVERLAP;   // Rewind to include some previous points
            }
            int lowerBound = offset;
            int upperBound = Math.min(offset + PAGE_SIZE_LIMIT, mCapturedLocations.size());

            // Grab the data we need for this page.
            LatLng[] page = mCapturedLocations
                    .subList(lowerBound, upperBound)
                    .toArray(new LatLng[upperBound - lowerBound]);

            // Perform the request. Because we have interpolate=true, we will get extra data points
            // between our originally requested path. To ensure we can concatenate these points, we
            // only start adding once we've hit the first new point (i.e. skip the overlap).
            SnappedPoint[] points = RoadsApi.snapToRoads(context, true, page).await();
            boolean passedOverlap = false;
            for (SnappedPoint point : points) {
                if (offset == 0 || point.originalIndex >= PAGINATION_OVERLAP) {
                    passedOverlap = true;
                }
                if (passedOverlap) {
                    snappedPoints.add(point);
                }
            }

            offset = upperBound;
        }

        return snappedPoints;
    }
}
