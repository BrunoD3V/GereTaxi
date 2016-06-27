package p4.geretaxi;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class InserirCoordenadasPTaxiAFragment extends DialogFragment{

    Button btnGravarCoordenadas;
    Button btnObterLocalizacao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insere_coordenadas_ptaxi_a, null);
        setCancelable(false);
        btnGravarCoordenadas = (Button) view.findViewById(R.id.btnGravarCoordenadas);
        btnObterLocalizacao = (Button) view.findViewById(R.id.btnObterLocalizacao);
        btnGravarCoordenadas.setOnClickListener(this);
        btnObterLocalizacao.setOnClickListener(this);
        
        return view;
    }

    public static InserirCoordenadasPTaxiAFragment newInstance() {
        InserirCoordenadasPTaxiAFragment i = new InserirCoordenadasPTaxiAFragment();
        return i;
    }

}
