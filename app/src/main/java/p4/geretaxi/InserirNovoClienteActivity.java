package p4.geretaxi;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class InserirNovoClienteActivity extends AppCompatActivity {

    EditText edtNome;
    EditText edtMorada;
    EditText edtCodigoPostal;
    EditText edtNif;
    EditText edtContacto;
    EditText edtEmail;
    GereBD gereBD;
    Helper helper;
    private Cliente cliente;
    private SharedPreference sharedPreference;
    private Spinner spinner;
    private String tipoCliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir_novo_cliente);

        edtNome = (EditText)findViewById(R.id.edtNomeCliente);
        edtMorada= (EditText)findViewById(R.id.edtMorada);
        edtCodigoPostal= (EditText)findViewById(R.id.edtCodigoPostal);
        edtNif= (EditText)findViewById(R.id.edtNif);
        edtContacto= (EditText)findViewById(R.id.edtContacto);
        edtEmail= (EditText)findViewById(R.id.edtEmail);
        gereBD = new GereBD();
        helper = new Helper();
        cliente = new Cliente();
        sharedPreference= new SharedPreference();
        spinner = (Spinner) findViewById(R.id.spinnerTipoCliente);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_cliente_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                tipoCliente = item.toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);
    }

    public void onClickInserirNovoCliente(View v){

        if(helper.isNetworkAvailable(getApplicationContext())){
            if(helper.isEmpty(edtNome) || helper.isEmpty(edtMorada) || helper.isEmpty(edtCodigoPostal) || helper.isEmpty(edtNif) || helper.isEmpty(edtContacto) || helper.isEmpty(edtEmail)){
                Toast.makeText(getApplicationContext(), "Deverá preencher todos os campos antes de enviar.", Toast.LENGTH_LONG).show();
                return;
            }else{
                cliente.setIdMotorista(sharedPreference.getValueInt(this, Constants.ID_MOTORISTA));
                cliente.setNome(edtNome.getText().toString());
                cliente.setMorada(edtMorada.getText().toString());
                cliente.setCodigoPostal(edtCodigoPostal.getText().toString());
                cliente.setNif(Integer.parseInt(edtNif.getText().toString()));
                cliente.setContacto(Integer.parseInt(edtContacto.getText().toString()));
                cliente.setEmail(edtEmail.getText().toString());
                cliente.setTipo(tipoCliente);
            }
            boolean res = gereBD.inserirCliente(cliente);
            if (!res){
                Toast.makeText(getApplicationContext(), "Não foi possivel inserir o Cliente.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Cliente Inserido com Sucesso!", Toast.LENGTH_SHORT).show();
                //TODO: ENCAMINHAR PARA LISTAGEM DE CLIENTES
            }
        }else{
            XMLHandler xmlHandler = new XMLHandler();
            Toast.makeText(getApplicationContext(), "Cliente inserido localmente.",Toast.LENGTH_LONG).show();
            //TODO: ESCREVER CLIENTE LOCALMENTE
        }
    }
}
