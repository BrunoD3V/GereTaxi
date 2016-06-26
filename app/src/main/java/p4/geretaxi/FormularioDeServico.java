package p4.geretaxi;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormularioDeServico extends AppCompatActivity{

    Servico servico = new Servico();
    NumberPicker numberPicker = null;
    TextView txtData;
    TextView txtHoraDeInicio;
    private String data;
    private String horaDeInicio;
    private String tipoServico;

    //EDIT TEXTS FORMULARIO
    EditText edtProcesso;
    EditText edtCliente;
    EditText edtOrigem;
    EditText edtDestino;
    EditText edtCustoPortagens;
    EditText edtDistanciaPercorrida;
    EditText edtHorasDeEspera;
    EditText edtTrajeto;
    Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_de_servico);

        edtCliente =(EditText) findViewById(R.id.edtCliente);
        edtCustoPortagens =(EditText) findViewById(R.id.edtCustoPortagens);
        edtDestino =(EditText) findViewById(R.id.edtDestino);
        edtOrigem =(EditText) findViewById(R.id.edtOrigem);
        edtProcesso =(EditText) findViewById(R.id.edtProcesso);
        edtDistanciaPercorrida =(EditText) findViewById(R.id.edtDistancia);
        edtHorasDeEspera =(EditText) findViewById(R.id.edtHorasDeEspera);
        edtTrajeto =(EditText) findViewById(R.id.edtTrajeto);
        txtData = (TextView) findViewById(R.id.txtDataDisplay);
        txtHoraDeInicio = (TextView) findViewById(R.id.txtHoraDisplay);
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(7);
        numberPicker.setWrapSelectorWheel(true);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.servico_tipo_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                tipoServico= item.toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setAdapter(adapter);
    }

    public void onClickTimePicker(View v){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                horaDeInicio = hourOfDay + ":" + minute;
                txtHoraDeInicio.setText(horaDeInicio);
            }
        };
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnTimeSetListener(onTimeSetListener);
        timePickerFragment.show(getFragmentManager(), "TimePicker");
    }


    public void onClickDatePicker(View v){
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year,monthOfYear,dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
                data = sdf2.format(cal.getTime());
                String formatedData = sdf.format(cal.getTime());
                txtData.setText(formatedData);
            }
        };
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(onDateSetListener);
        datePickerFragment.show(getFragmentManager(), "DatePicker");
    }

    public void onClickInserirServico(View v){

        servico.setProcesso(edtProcesso.getText().toString());
        servico.setNomeCliente(edtCliente.getText().toString());
        servico.setNumPassageiros(numberPicker.getValue());
        servico.setData(data);
        servico.setHoraDeInicio(horaDeInicio);
        servico.setOrigem(edtOrigem.getText().toString());
        servico.setDestino(edtDestino.getText().toString());
        servico.setCustoPortagens(Double.parseDouble(edtCustoPortagens.getText().toString()));
        servico.setDistancia(Double.parseDouble(edtDistanciaPercorrida.getText().toString()));
        servico.setHorasDeEspera(Double.parseDouble(edtHorasDeEspera.getText().toString()));
        servico.setTrajeto(edtTrajeto.getText().toString());
        servico.setTipo(tipoServico);
        Log.d("onClickInserirServico: ", servico.toString());
    }
}
