package p4.geretaxi.DialogFragments;

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
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import p4.geretaxi.ClassesDados.Cliente;
import p4.geretaxi.ClassesHelper.Helper;
import p4.geretaxi.ClassesHelper.MyApplication;
import p4.geretaxi.ClassesHelper.SharedPreference;
import p4.geretaxi.ClassesHelper.XMLHandler;
import p4.geretaxi.WebServiceClass.GereBD;
import p4.geretaxi.R;

public class DialogSpinnerFragment extends DialogFragment implements View.OnClickListener {

    Button yes, no;
    Spinner spinner;
    CommunicatorCorrige communicatorCorrige;
    int mNum;
    XMLHandler handler;
    GereBD gereBD;
    ArrayList<String> clientesSpinner = new ArrayList<>();
    private String nomeCliente;

    public static DialogSpinnerFragment newInstance(int num) {
        DialogSpinnerFragment f = new DialogSpinnerFragment();
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

        if(Helper.isNetworkAvailable(MyApplication.getAppContext())){//vai buscar os clientes à base de dados
            clientes = gereBD.listarClientes(SharedPreference.getIdMotoristaSharedPreferences(MyApplication.getAppContext()));
            for (Cliente c: clientes) {
                clientesSpinner.add(c.getNome());
            }
        }else{
            clientes = handler.parseClientes(Xml.newPullParser());//vai buscar os clientes armazenados localmente
            for (Cliente c: clientes) {
                clientesSpinner.add(c.getNome());
            }
        }
        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyApplication.getAppContext(),android.R.layout.simple_spinner_item, clientesSpinner);
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
        switch (view.getId()) {
            case R.id.btn_yes:
                communicatorCorrige.onDialogMessage(nomeCliente, mNum);
                this.dismiss();
                break;
            case R.id.btn_no:
                this.dismiss();
                break;
            default:
                break;
        }

    }

    public interface CommunicatorCorrige {
        public void onDialogMessage(String dados, int num);
    }
}
