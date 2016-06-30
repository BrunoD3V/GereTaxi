package p4.geretaxi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;

import com.google.maps.model.LatLng;

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

import javax.xml.xpath.XPathExpressionException;

public class TesteActivity extends AppCompatActivity {

    List<LatLng> mCapturedLocations;

    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/xml?";
    private static final String GOOGLE_API_KEY = "AIzaSyCmOQIe5TjVlSpu5tQJFdHP4amgpo8gJ1M";
    private static final int PAGE_SIZE_LIMIT = 100;
    private static final int PAGINATION_OVERLAP = 5;
    boolean portagens;
    double distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreference sharedPreference = new SharedPreference();
                String latPTaxi = sharedPreference.getValueString(MyApplication.getAppContext(),Constants.LAT);
                String lonPTaxi = sharedPreference.getValueString(MyApplication.getAppContext(), Constants.LON);

                System.out.println("LAT: " + latPTaxi);

                // LatLng ptaxis = new com.google.maps.model.LatLng(latPTaxi, lonPTaxi);
                String origin = latPTaxi.trim() + "," + lonPTaxi.trim();
                System.out.println("COOrdenadas "+ origin);
                getDirections(origin.trim(),"41.485538,-7.181021");
            }
        }).start();

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
            System.out.println("BUFFER " + buffer.toString()  );
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
}
