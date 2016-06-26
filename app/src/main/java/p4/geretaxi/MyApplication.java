package p4.geretaxi;

import android.app.Application;
import android.content.Context;

/**
 * Created by belchior on 24/06/2016.
 */
public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}