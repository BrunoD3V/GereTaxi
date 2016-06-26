package p4.geretaxi;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class FormularioDeServico extends AppCompatActivity{

    Servico servico;
    NumberPicker numberPicker = null;
    TextView txtData;
    TextView txtHoraDeInicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_de_servico);

        txtData = (TextView) findViewById(R.id.txtDataDisplay);
        txtHoraDeInicio = (TextView) findViewById(R.id.txtHoraDisplay);
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(7);
        numberPicker.setWrapSelectorWheel(true);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.servico_tipo_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onClickTimePicker(View v){
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getFragmentManager(), "NumPicker");
    }


    public void onClickDatePicker(View v){
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String data = dayOfMonth + "-" + monthOfYear + "-" + year;
                txtData.setText(data);
            }
        };
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(onDateSetListener);
        datePickerFragment.show(getFragmentManager(), "DatePicker");
    }

    public void onClickInserirServico(View v){

        servico.setNumPassageiros(numberPicker.getValue());
    }




}
