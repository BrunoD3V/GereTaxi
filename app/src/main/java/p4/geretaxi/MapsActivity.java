package p4.geretaxi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.RoadsApi;
import com.google.maps.android.SphericalUtil;
import com.google.maps.model.GeocodingResult;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;



public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    private GeoApiContext mContext;

    private static final int PAGE_SIZE_LIMIT = 100;

    private static final int PAGINATION_OVERLAP = 5;

    private static final double LATPTAXI = 41.484205;
    private static final double LNGPTAXI = -7.183378;

    private ProgressBar mProgressBar;

    private LocationManager lManager;
    private LocationListener lListener = null;

    //ServicoContratado servicoContratado;

    private EditText editTextProcesso;


    Button buttonIniciaServico;
    Button buttonTerminaServico;
    Button buttonMostraDistancia;
    Button buttonMostraServico;

    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/xml?";
    private static final String GOOGLE_API_KEY = "AIzaSyASiyTdSrR5R0M0A_DudilgE3CuSWCUIlQ";


    Handler myHandler;

    List<LatLng> routes;
    List<LatLng> mCapturedLocations;
    List<SnappedPoint> mSnappedPoints;
    Double distance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mContext = new GeoApiContext().setApiKey(getString(R.string.google_maps_web_services_key));

        editTextProcesso = (EditText) findViewById(R.id.editTextProcesso);

        buttonIniciaServico = (Button) findViewById(R.id.buttonIniciaServico);
        buttonTerminaServico = (Button) findViewById(R.id.buttonTerminaServico);
        buttonMostraDistancia = (Button) findViewById(R.id.buttonMostraDistancia);
        buttonMostraServico = (Button) findViewById(R.id.buttonMostraServico);
        buttonTerminaServico.setEnabled(false);


    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

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
                if(res==null){
                    Toast.makeText(getApplicationContext(),"erro na direction Api", Toast.LENGTH_SHORT).show();
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
    AsyncTask<Void, Void, List<SnappedPoint>> mTaskSnapToRoads =
            new AsyncTask<Void, Void, List<SnappedPoint>>() {
                @Override
                protected void onPreExecute() {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setIndeterminate(true);
                }

                @Override
                protected List<SnappedPoint> doInBackground(Void... params) {
                    try {
                        return snapToRoads(mContext);
                    } catch (final Exception ex) {
                        toastException(ex);
                        ex.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(List<SnappedPoint> snappedPoints) {

                    mSnappedPoints = snappedPoints;
                    mProgressBar.setVisibility(View.INVISIBLE);

                    com.google.android.gms.maps.model.LatLng[] mapPoints =
                            new com.google.android.gms.maps.model.LatLng[mSnappedPoints.size()];
                    int i = 0;
                    LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                    for (SnappedPoint point : mSnappedPoints) {
                        mapPoints[i] = new com.google.android.gms.maps.model.LatLng(point.location.lat,
                                point.location.lng);
                        bounds.include(mapPoints[i]);
                        i += 1;
                    }

                    mMap.addPolyline(new PolylineOptions().add(mapPoints).color(Color.BLUE));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 0));
                }

            };


    public void onClickIniciaServico(View v){

        if (isEmpty(editTextProcesso)){
            Toast toast = Toast.makeText(getApplicationContext(), "Tem que inserir o processo", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String processo = editTextProcesso.getText().toString();



        boolean result=inicializarDados(processo);
        if (result == true) {
            Toast toast = Toast.makeText(getApplicationContext(), "Serviço Inicializado",Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Erro na inicialização", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        initGPS(processo);

    }


    public void onClickTerminaServico(View v){

        if(lListener != null){
            lManager.removeUpdates(lListener);
        }
        if (isEmpty(editTextProcesso)){
            Toast toast = Toast.makeText(getApplicationContext(), "Tem que inserir o processo", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        mostraServico(editTextProcesso.getText().toString());
        buttonMostraDistancia.setEnabled(true);
    }

    public void onClickDistancia(View V) {
        Double distance = getDistance();
        Toast toast = Toast.makeText(getApplicationContext(),distance.toString(), Toast.LENGTH_SHORT);
        toast.show();
        buttonMostraDistancia.setEnabled(false);
    }

    public void onClickbuttonMostraServico(View v){
        if (isEmpty(editTextProcesso)){
            Toast toast = Toast.makeText(getApplicationContext(), "Tem que inserir o processo", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        mostraServico(editTextProcesso.getText().toString());
        buttonMostraDistancia.setEnabled(true);
    }

    private void mostraServico(String processo) {

        try {
            XMLHandler parser = new XMLHandler();
            mCapturedLocations = parser.loadGpxData(Xml.newPullParser(), processo);


            LatLng ptaxis = new LatLng(LATPTAXI, LNGPTAXI);
            String origin = ptaxis.toString();
            String destination = mCapturedLocations.get(0).toString();
            System.out.println(origin);
            String termino = mCapturedLocations.get(mCapturedLocations.size()-1).toString();

            execute(origin, destination);

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
            execute(termino, origin);
            myHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            mCapturedLocations= ListUtils.union(routes, mCapturedLocations);
                    }
                }
            };

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            toastException(e);
        }

        boolean hasNet = isNetworkAvailable();
        if(hasNet){
            mTaskSnapToRoads.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Active a conexão à internet", Toast.LENGTH_SHORT).show();
        }
        buttonMostraServico.setEnabled(false);

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

    private Double getDistance() {

        List<LatLng> latLngs;
        latLngs = getVisitedPlaces();
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

    public String getDate()
    {
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String data = sdf.format(date);
        return data;
    }

    public String getTime()
    {
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("k:mm");
        String hora = sdf.format(date);
        return hora;
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

    private GeocodingResult geocodeSnappedPoint(GeoApiContext context, SnappedPoint point) throws Exception {
        GeocodingResult[] results = GeocodingApi.newRequest(context)
                .place(point.placeId)
                .await();

        if (results.length > 0) {
            return results[0];
        }
        return null;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private boolean inicializarDados(String processo) {
        XMLHandler eraser = new XMLHandler();
        Boolean result = eraser.eraser(processo);
        mMap.clear();
        return result;
    }

    public void initGPS(final String processo) {
        final XMLHandler writer = new XMLHandler();
        lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(lManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            buttonTerminaServico.setEnabled(true);
            buttonIniciaServico.setEnabled(false);
            Toast.makeText(getApplicationContext(),"O GPS está a registar.", Toast.LENGTH_LONG).show();
        }
        else
        {
            displayPromptForEnablingGPS(MapsActivity.this);
        }

        lListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                Boolean result = writer.writeGPSCoordinates(location, processo);
                Toast toast = Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_SHORT );
                toast.show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //displayPromptForEnablingGPS(MainActivity.this);
            }
        };


        try {

            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 10, lListener);

        } catch (SecurityException se) {
        }
    }

    public void displayPromptForEnablingGPS(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("O Sensor de GPS está desativado. Deseja ativar?")
                .setCancelable(false)
                .setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // Sent user to GPS settings screen
                                final ComponentName toLaunch = new ComponentName(
                                        "com.android.settings",
                                        "com.android.settings.SecuritySettings");
                                final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                intent.setComponent(toLaunch);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                dialog.dismiss();
                                dialog.cancel();
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                            }
                        })
                .setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isEmpty(EditText editText){
        return editText.getText().toString().trim().length() == 0;
    }

    /** Helper for toasting exception messages on the UI thread. */
    private void toastException(final Exception ex) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
