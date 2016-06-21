package p4.geretaxi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.EditText;

import java.text.SimpleDateFormat;

public class Helper {

    public boolean isEmpty(EditText editText){
        return editText.getText().toString().trim().length() == 0;
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

    public boolean inicializarDados(String processo) {
        XMLHandler eraser = new XMLHandler();
        Boolean result = eraser.eraser(processo);
        return result;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void displayPromptEnableWifi(final Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // set title
        alertDialogBuilder.setTitle("Definições de Wifi");

        // set dialog message
        alertDialogBuilder
                .setMessage("Pretende ligar a rede WiFi??")
                .setCancelable(false)
                .setPositiveButton("Sim",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        context.startActivity(i);
                    }
                })
                .setNegativeButton("Não",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
