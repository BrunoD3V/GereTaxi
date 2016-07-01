package p4.geretaxi.ClassesHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.KSoapClass.GereBD;

public class Helper {

    public static boolean isEmpty(EditText editText){
        return editText.getText().toString().trim().length() == 0;
    }

    public static String getDate()
    {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String data = sdf.format(date);
        return data;
    }

    public static String getTime()
    {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("k:mm");
        String hora = sdf.format(date);
        return hora;
    }

    public static String getExpirationDate(){

        String date = getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
            c.add(Calendar.DATE, 30);// adiciona 30 dias à data actual

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(c.getTime());
    }

    public static boolean isExpired(String expDate) {

        GregorianCalendar now = new GregorianCalendar();


        return now.after(expDate); //verifica se a data é anterior à data actual
    }

    public static boolean doubleTryParse(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean integerTryParse(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void displayPromptEnableWifi(final Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setTitle("Definições de conexão.");
        alertDialogBuilder
                .setMessage("Deverá conectar-se a uma rede para concluir o serviço.")
                .setCancelable(false)
                .setPositiveButton("Ativar Wi-Fi",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        context.startActivity(i);
                    }
                }).setNegativeButton("Ativar 3G", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int id) {
                            Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            context.startActivity(i);
                        }
                })
                .setNeutralButton("Não",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    public static boolean attemptLogin() {
        boolean result = false;

        SharedPreference sharedPreference = new SharedPreference();

        GereBD bd = new GereBD();
        String email = sharedPreference.getValueString(MyApplication.getAppContext(), Constants.EMAIL);
        String pass = sharedPreference.getValueString(MyApplication.getAppContext(), Constants.PASS);
        if(Helper.isNetworkAvailable(MyApplication.getAppContext())){
            int res = bd.checkLogin(email, pass.trim());
            switch (res) {
                case -2:
                    if (!isExpired(sharedPreference.getValueString(MyApplication.getAppContext(),Constants.VALIDADE))) {
                        sharedPreference.save(MyApplication.getAppContext(), Constants.TRUE, Constants.SESSION);
                        result = true;
                    }
                    break;
                case 1:
                    sharedPreference.save(MyApplication.getAppContext(), Constants.TRUE, Constants.SESSION);
                    sharedPreference.save(MyApplication.getAppContext(), getExpirationDate(), Constants.VALIDADE);
                    result = true;
                    break;
                default:
                    sharedPreference.save(MyApplication.getAppContext(), Constants.FALSE, Constants.SESSION);
            }

        }else {
            if (!isExpired(sharedPreference.getValueString(MyApplication.getAppContext(),Constants.VALIDADE))){
                sharedPreference.save(MyApplication.getAppContext(), Constants.TRUE, Constants.SESSION);
                result = true;
            } else {
                sharedPreference.save(MyApplication.getAppContext(), Constants.FALSE, Constants.SESSION);
            }
        }

        return result;
    }



    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
