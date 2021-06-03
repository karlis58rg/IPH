package mx.ssp.iph.principal.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import mx.ssp.iph.R;
import mx.ssp.iph.principal.viewmodel.SumarViewModel;
import mx.ssp.iph.utilidades.Sumar;

public class ViewModelSumarActivity  extends Fragment {

    private TextView tvSumar, tvSumarViewModel;
    private Button btSumar;

    private int numero;
    private SumarViewModel sumarViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_view_model_sumar,container,false);

        sumarViewModel = ViewModelProviders.of(this).get(SumarViewModel.class);

        tvSumar = root.findViewById(R.id.videModelSumarActivityTvSumar);
        tvSumarViewModel = root.findViewById(R.id.videModelSumarActivityTvSumarViewModel);

        tvSumar.setText(" "+ numero);
        tvSumarViewModel.setText(" "+ sumarViewModel.getResultado());


        btSumar = root.findViewById(R.id.viewModelSumarActivityBtSumar);
        btSumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numero = Sumar.sumar(numero);
                tvSumar.setText(" "+ numero);
                sumarViewModel.setResultado(Sumar.sumar(sumarViewModel.getResultado()));
                tvSumarViewModel.setText(" "+ sumarViewModel.getResultado());
            }
        });

        return root;
    }

    private void configView(){

    }
}
