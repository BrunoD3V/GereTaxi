package p4.geretaxi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Xml;
import android.widget.Toast;

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
        context = c;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                System.out.println("Está no background da async task directions");
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

    public List<LatLng> mostraServico(String processo) {
        try {
            XMLHandler parser = new XMLHandler();
            mCapturedLocations = parser.loadGpxData(Xml.newPullParser(), processo);


            LatLng ptaxis = new LatLng(LATPTAXI, LNGPTAXI);
            String origin = ptaxis.toString();
            String destination = mCapturedLocations.get(0).toString();
            System.out.println(origin);
            String termino = mCapturedLocations.get(mCapturedLocations.size()-1).toString();
            if (isNetworkAvailable()) {
                execute(origin, destination);
            } else {
                displayPromptEnableWifi();
                return mostraServico(processo);
            }

            myHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            mCapturedLocations= ListUtils.union(routes,mCapturedLocations);

                    }
                }
            };
            routes = null;
            execute(origin, termino);
            myHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            mCapturedLocations= ListUtils.union(mCapturedLocations, routes);
                    }
                }
            };
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

     return mCapturedLocations;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void displayPromptEnableWifi() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        final WifiManager wifiMan;
        wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        // set title
        alertDialogBuilder.setTitle("Wifi Settings");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to enable WIFI ?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //enable wifi
                        wifiMan.setWifiEnabled(true);
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //disable wifi
                        wifiMan.setWifiEnabled(false);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}