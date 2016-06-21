package p4.geretaxi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by belchior on 21/06/2016.
 */
public class EscolherServicoDialogFragment extends DialogFragment {



    public EscolherServicoDialogFragment(){

    }




    public Dialog onCreateDialog(Bundle savedInstanceSate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.escolhe_tipo_servico).setItems(R.array.servico_tipo_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



               switch (i) {
                    case 0:
                        System.out.println(i);
                        Intent intent = new Intent(getActivity(),AssistenciaEmViagemActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        System.out.println(i);
                        intent = new Intent(getActivity(), AcidenteDeTabalhoActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        System.out.println(i);
                        intent = new Intent(getActivity(),MapsActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        System.out.println(i);
                        break;
                }
            }
        });
        return builder.create();
    }

    public static EscolherServicoDialogFragment newInstance() {
        EscolherServicoDialogFragment f = new EscolherServicoDialogFragment();
        return f;
    }


}
