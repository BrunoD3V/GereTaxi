package p4.geretaxi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
            c.add(Calendar.DATE, 30);
            System.out.println("BIBA");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(c.getTime());
    }

    public static boolean isExpired(String expDate) {

        GregorianCalendar now = new GregorianCalendar();


        return now.after(expDate);
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

    public  static boolean inicializarDados(String processo) {
        XMLHandler eraser = new XMLHandler();
        Boolean result = eraser.eraser(processo);
        return result;
    }

    public static boolean isNetworkAvailable(Context context) { //TODO verificação se tem acesso à internet
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

    public enum AppStart {
        FIRST_TIME, FIRST_TIME_VERSION, NORMAL;
    }

    /**
     * The app version code (not the version name!) that was used on the last
     * start of the app.
     */
    private static final String LAST_APP_VERSION = "last_app_version";

    /**
     * Finds out started for the first time (ever or in the current version).<br/>
     * <br/>
     * Note: This method is <b>not idempotent</b> only the first call will
     * determine the proper result. Any subsequent calls will only return
     * {@link AppStart#NORMAL} until the app is started again. So you might want
     * to consider caching the result!
     *
     * @return the type of app start
     */
    public AppStart checkAppStart() {
        PackageInfo pInfo;
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(MyApplication.getAppContext());
        AppStart appStart = AppStart.NORMAL;
        try {

            pInfo = MyApplication.getAppContext().getPackageManager().getPackageInfo(MyApplication.getAppContext().getApplicationContext().getPackageName(), 0);
            int lastVersionCode = sharedPreferences
                    .getInt(LAST_APP_VERSION, -1);

            int currentVersionCode = pInfo.versionCode;
            appStart = checkAppStart(currentVersionCode, lastVersionCode);
            // Update version in preferences
            sharedPreferences.edit()
                    .putInt(LAST_APP_VERSION, currentVersionCode).commit();
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(Constants.LOG,
                    "Unable to determine current app version from pacakge manager. Defenisvely assuming normal app start.");
        }
        return appStart;
    }

    public AppStart checkAppStart(int currentVersionCode, int lastVersionCode) {
        if (lastVersionCode == -1) {
            return AppStart.FIRST_TIME;
        } else if (lastVersionCode < currentVersionCode) {
            return AppStart.FIRST_TIME_VERSION;
        } else if (lastVersionCode > currentVersionCode) {
            Log.w(Constants.LOG, "Current version code (" + currentVersionCode
                    + ") is less then the one recognized on last startup ("
                    + lastVersionCode
                    + "). Defenisvely assuming normal app start.");
            return AppStart.NORMAL;
        } else {
            return AppStart.NORMAL;
        }
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
            /*if (res == 1) {
                sharedPreference.save(MyApplication.getAppContext(), Constants.TRUE, Constants.SESSION);
                sharedPreference.save(MyApplication.getAppContext(), getExpirationDate(), Constants.VALIDADE);
                result = true;
            } else {
                sharedPreference.save(MyApplication.getAppContext(), Constants.FALSE, Constants.SESSION);
            }*/
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

    public static boolean isSessionEnabled(){
        SharedPreference sharedPreference = new SharedPreference();
        String res = sharedPreference.getValueString(MyApplication.getAppContext(), Constants.SESSION);
        return res.equalsIgnoreCase(Constants.TRUE);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
