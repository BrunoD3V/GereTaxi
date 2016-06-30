package p4.geretaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        XMLHandler xmlHandler = new XMLHandler();
        if(Helper.isEmpty(edtNome) || Helper.isEmpty(edtMorada) || Helper.isEmpty(edtNif) ){
            Toast.makeText(getApplicationContext(), "Deverá preencher todos os campos antes de enviar.", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            cliente.setIdMotorista(sharedPreference.getValueInt(this, Constants.ID_MOTORISTA));
            cliente.setNome(edtNome.getText().toString());
            cliente.setMorada(edtMorada.getText().toString());
            cliente.setNif(Integer.parseInt(edtNif.getText().toString()));
            cliente.setTipo(tipoCliente);
            if(!Helper.isEmpty(edtCodigoPostal))
                cliente.setCodigoPostal(edtCodigoPostal.getText().toString());
            if(!Helper.isEmpty(edtContacto))
                cliente.setContacto(Integer.parseInt(edtContacto.getText().toString()));
            if(!Helper.isEmpty(edtEmail) && Helper.isValidEmail(edtEmail.getText().toString()))
                cliente.setEmail(edtEmail.getText().toString());
            else{
                Toast.makeText(getApplicationContext(),"Verifique o email que tentou introduzir.", Toast.LENGTH_LONG).show();
                return;
            }


        }
        if(!xmlHandler.writeClientes(cliente)){
            Toast.makeText(getApplicationContext(), "Cliente já existe localmente", Toast.LENGTH_SHORT).show();
        }

        if(Helper.isNetworkAvailable(getApplicationContext())){
            boolean res = gereBD.inserirCliente(cliente);
            if (!res){
                Toast.makeText(getApplicationContext(), "Não foi possivel inserir o Cliente.", Toast.LENGTH_SHORT).show();
                if(!xmlHandler.writenovoCliente(cliente)) {
                    Toast.makeText(getApplicationContext(), "O cliente já existe localmente", Toast.LENGTH_SHORT).show();
                }
                return;
            }else{
                Toast.makeText(getApplicationContext(), "Cliente Inserido com Sucesso!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Cliente inserido localmente.",Toast.LENGTH_LONG).show();
            if(!xmlHandler.writenovoCliente(cliente) || !xmlHandler.writeClientes(cliente))
                Toast.makeText(getApplicationContext(),"Erro na gravação local do cliente", Toast.LENGTH_SHORT).show();

        }
        Intent intent = new Intent(this, ConsultarClientesActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.logout_id:
                SharedPreference sharedPreference = new SharedPreference();
                sharedPreference.save(getApplicationContext(), " ", Constants.EMAIL);
                sharedPreference.save(getApplicationContext(), " ", Constants.PASS);
                sharedPreference.save(getApplicationContext(), -1, Constants.ID_MOTORISTA);
                sharedPreference.save(getApplicationContext(), Constants.FALSE, Constants.SESSION);

                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;

            case R.id.settings_id:                 Intent in = new Intent(this, CoordenadasActivity.class);                 startActivity(in);

                break;

            case R.id.inicio_id:
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
