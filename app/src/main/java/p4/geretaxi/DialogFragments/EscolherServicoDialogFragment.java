package p4.geretaxi.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import p4.geretaxi.Activities.IniciaServicoActivity;
import p4.geretaxi.ClassesDados.Servico;
import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.R;

public class EscolherServicoDialogFragment extends DialogFragment {

    public EscolherServicoDialogFragment(){

    }

    public Dialog onCreateDialog(Bundle savedInstanceSate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.escolhe_tipo_servico).setItems(R.array.servico_tipo_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Servico servico = new Servico();
                switch (i) {
                    case 0:
                        servico.setTipo(Constants.VIAGEM);
                        setIntent(servico);
                        break;
                    case 1:
                        servico.setTipo(Constants.ACIDENTE);
                        setIntent(servico);
                        break;
                    case 2:
                        servico.setTipo(Constants.PARTICULAR);
                        setIntent(servico);
                        break;
                    default:
                        break;
                }
            }
        });
        return builder.create();
    }

    private void setIntent(Servico servico){
        Intent intent = new Intent(getActivity(),IniciaServicoActivity.class);
        intent.putExtra(Constants.INTENT_SERVICO, servico);
        startActivity(intent);
    }

    public static EscolherServicoDialogFragment newInstance() {
        EscolherServicoDialogFragment f = new EscolherServicoDialogFragment();
        return f;
    }
}
