package p4.geretaxi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DialogCorrigeDadosS extends DialogFragment implements View.OnClickListener {

    Button yes, no;
    EditText editTextCorrige;
    TextView txt_dia;
    CommunicatorCorrige communicatorCorrige;
    int mNum;


    public static DialogCorrigeDadosS newInstance(int num) {
        DialogCorrigeDadosS f = new DialogCorrigeDadosS();
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
        View view = inflater.inflate(R.layout.corrige_dados_s_dialog, null);
        setCancelable(false);
        yes = (Button) view.findViewById(R.id.btn_yes);
        no = (Button) view.findViewById(R.id.btn_no);
        editTextCorrige = (EditText) view.findViewById(R.id.editTextCorrige);
        TextView txt_dia = (TextView) view.findViewById(R.id.txt_dia);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

           switch (view.getId()) {
               case R.id.btn_yes:
                   if (!Helper.isEmpty(editTextCorrige)) {
                       if (mNum<6) {
                           communicatorCorrige.onDialogMessage(editTextCorrige.getText().toString(), mNum);
                       } else if (mNum == 6){
                           if (Helper.intergerTryParse(editTextCorrige.getText().toString())){
                               int i = Integer.parseInt(editTextCorrige.getText().toString());
                               if (i < 1 || i > 8){
                                   Toast.makeText(getActivity(), Constants.N_PASSAGEIROS_VALIDO, Toast.LENGTH_SHORT).show();
                                   this.dismiss();
                               } else {
                                   communicatorCorrige.onDialogMessage(editTextCorrige.getText().toString(), mNum);
                               }
                           }else {
                               Toast.makeText(getActivity(), Constants.N_VALIDO, Toast.LENGTH_SHORT).show();
                           }

                       } else {

                           if (!Helper.doubleTryParse(editTextCorrige.getText().toString())) {
                               Toast.makeText(getActivity(), Constants.N_VALIDO, Toast.LENGTH_SHORT).show();
                               this.dismiss();
                           } else {
                               if (Double.parseDouble(editTextCorrige.getText().toString()) <= 0) {
                                   Toast.makeText(getActivity(), Constants.N_POSITIVO, Toast.LENGTH_SHORT).show();
                                   this.dismiss();
                               }else {
                                   communicatorCorrige.onDialogMessage(editTextCorrige.getText().toString(), mNum);
                               }

                           }


                       }
                   }
                   this.dismiss();
                   break;
               case R.id.btn_no:
                   this.dismiss();
                   break;
               default:
                   break;
           }




    }

    interface CommunicatorCorrige {
        public void onDialogMessage(String dados, int num);
    }
}
