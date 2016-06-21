package p4.geretaxi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;

import java.text.SimpleDateFormat;

/**
 * Created by belchior on 21/06/2016.
 */
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


}
