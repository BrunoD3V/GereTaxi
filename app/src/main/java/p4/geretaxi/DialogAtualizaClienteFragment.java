package p4.geretaxi;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by belchior on 29/06/2016.
 */
public class DialogAtualizaClienteFragment extends DialogFragment implements View.OnClickListener{

    Button yes, no;
    EditText editTextCorrige;
    CommunicatorCliente communicatorCliente;
    int mNum;

    public static DialogAtualizaClienteFragment newInstance(int num) {
        DialogAtualizaClienteFragment f = new DialogAtualizaClienteFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicatorCliente = (CommunicatorCliente) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.corrige_dados_s_dialog, null);
        setCancelable(false);
        yes = (Button) view.findViewById(R.id.btn_yes);
        no = (Button) view.findViewById(R.id.btn_no);
        editTextCorrige = (EditText) view.findViewById(R.id.editTextCorrige);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        return view;


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                if(!Helper.isEmpty(editTextCorrige)){
                    String dados = editTextCorrige.getText().toString();
                    if (mNum < 5){
                        if(mNum==3 && !Helper.isValidEmail(dados)){
                                Toast.makeText(MyApplication.getAppContext(),"Introduza um email válido.", Toast.LENGTH_LONG).show();
                        }
                        communicatorCliente.onDialogMessage(dados,mNum);
                    } else {
                        if (Helper.integerTryParse(dados)) {
                            communicatorCliente.onDialogMessage(dados, mNum);
                        } else {
                            Toast.makeText(getActivity(), Constants.N_VALIDO, Toast.LENGTH_SHORT).show();
                            this.dismiss();
                        }
                    }
                }

                this.dismiss();
                break;
            case R.id.btn_no:
                this.dismiss();
                break;
        }
    }
    interface CommunicatorCliente {
        public void onDialogMessage(String dados, int num);
    }
}
