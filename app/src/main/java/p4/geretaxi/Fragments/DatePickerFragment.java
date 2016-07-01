package p4.geretaxi.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment{

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    public DatePickerFragment() {
    }

    //LISTENER PARA A DATA
    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerFragment = new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);

        datePickerFragment.setTitle("Data do Serviço");

        return datePickerFragment;
    }


}
