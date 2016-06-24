package p4.geretaxi;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by belchior on 22/06/2016.
 */
public class DialogCustoPortagemFragment extends DialogFragment implements View.OnClickListener {

    Button yes, no;
    EditText editTextPortagem;
    Communicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_portagem_dialog_fragment, null);
        setCancelable(false);
        yes = (Button) view.findViewById(R.id.btn_yes);
        no = (Button) view.findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        editTextPortagem = (EditText) view.findViewById(R.id.editTextCusto);


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                communicator.onDialogMessage(editTextPortagem.getText().toString());
                this.dismiss();
                break;
            case R.id.btn_no:
                communicator.onDialogMessage("0");
                this.dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
    interface Communicator {
        public void onDialogMessage(String portagem);
    }


}
