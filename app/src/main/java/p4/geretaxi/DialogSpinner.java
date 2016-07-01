package p4.geretaxi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DialogSpinner extends DialogFragment implements View.OnClickListener {

    Button yes, no;
    Spinner spinner;
    CommunicatorCorrige communicatorCorrige;
    int mNum;
    XMLHandler handler;
    GereBD gereBD;
    ArrayList<String> clientesSpinner = new ArrayList<>();
    private String nomeCliente;

    public static DialogSpinner newInstance(int num) {
        DialogSpinner f = new DialogSpinner();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicatorCorrige =(CommunicatorCorrige) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_spinner, null);
        setCancelable(false);
        yes = (Button) view.findViewById(R.id.btn_yes);
        no = (Button) view.findViewById(R.id.btn_no);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        handler = new XMLHandler();
        gereBD = new GereBD();
        List<Cliente> clientes;

        if(Helper.isNetworkAvailable(MyApplication.getAppContext())){
            clientes = gereBD.listarClientes(SharedPreference.getIdMotoristaSharedPreferences(MyApplication.getAppContext()));
            for (Cliente c: clientes) {
                clientesSpinner.add(c.getNome());
            }
        }else{
            clientes = handler.parseClientes(Xml.newPullParser());
            for (Cliente c: clientes) {
                clientesSpinner.add(c.getNome());
            }
        }
        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MyApplication.getAppContext(),android.R.layout.simple_spinner_item, clientesSpinner);
        System.out.println("Spinner"+clientes.toString());
        System.out.println("SpinnerContent" + clientesSpinner.toString());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                nomeCliente= item.toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View view) {


    }

    interface CommunicatorCorrige {
        public void onDialogMessage(String dados, int num);
    }
}
