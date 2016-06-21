package p4.geretaxi;

import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Xml;

import com.google.maps.model.LatLng;

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
import java.util.List;

public class ServicoHandler {


    private static final double LATPTAXI = 41.484205;
    private static final double LNGPTAXI = -7.183378;
    private static Context context;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/xml?";
    private static final String GOOGLE_API_KEY = "AIzaSyCmOQIe5TjVlSpu5tQJFdHP4amgpo8gJ1M";

    List<LatLng> mCapturedLocations;
    List<LatLng> routes;

    Handler myHandler;

    public ServicoHandler(Context c) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        context = c;
    }



    /* private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            XMLHandler parser = new XMLHandler();
            try {
                if(res.isEmpty()){
                    Toast.makeText(context,"erro na direction Api", Toast.LENGTH_SHORT).show();
                    return;
                }
                routes=parser.parseDirections(Xml.newPullParser(),res);
                System.out.println(routes.get(0).toString());
                System.out.println(routes.get(routes.size()-1).toString());
                myHandler.sendEmptyMessage(0);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(String origin, String destination) throws UnsupportedEncodingException {

        new DownloadRawData().execute(createUrl(origin, destination));
    }

    private String createUrl(String origin, String destination) throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");

        return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&key=" + GOOGLE_API_KEY;
    }
*/
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
            return parser.parseDirections(Xml.newPullParser(), buffer.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return routes;
    }

    public List<LatLng> mostraServico(String processo) {
        try {
            XMLHandler parser = new XMLHandler();
            mCapturedLocations = parser.loadGpxData(Xml.newPullParser(), processo);


            LatLng ptaxis = new LatLng(LATPTAXI, LNGPTAXI);
            String origin = ptaxis.toString();
            String destination = mCapturedLocations.get(0).toString();
            System.out.println(origin);
            String termino = mCapturedLocations.get(mCapturedLocations.size()-1).toString();

            routes = getDirections(origin, destination);

            /*myHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:*/

                            mCapturedLocations= ListUtils.union(routes,mCapturedLocations);

                    /*}
                }
            };*/
            routes = null;
            routes = getDirections(termino, origin);
           /* myHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:*/
                            mCapturedLocations= ListUtils.union(mCapturedLocations, routes);

/*
                    }
                }
            };*/
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

     return mCapturedLocations;
    }

}
