package p4.geretaxi.ClassesHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class GPSHandler {

    private LocationManager lManager;
    private LocationListener lListener = null;

    private static Context context;

    public GPSHandler(Context c) {
        context = c;
    }

    public void initGPS(final String processo, final Activity activity) {
        final XMLHandler writer = new XMLHandler();
        lManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(lManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {

            Toast.makeText(context,"O GPS está a registar.", Toast.LENGTH_LONG).show();
        }
        else
        {
            displayPromptForEnablingGPS(activity);
        }

        lListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                writer.writeGPSCoordinates(location, processo);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        try {

            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 300, lListener);

        } catch (SecurityException se) {
        }
    }

    public void listenerClose() {
        if(lListener != null){
            lManager.removeUpdates(lListener);
        }
    }

    public void displayPromptForEnablingGPS(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("O Sensor de GPS está desativado. Deseja ativar?")
                .setCancelable(false)
                .setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                final ComponentName toLaunch = new ComponentName(
                                        "com.android.settings",
                                        "com.android.settings.SecuritySettings");
                                final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                intent.setComponent(toLaunch);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                dialog.dismiss();
                                activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
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
}
