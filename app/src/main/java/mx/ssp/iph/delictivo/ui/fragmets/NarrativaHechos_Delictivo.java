package mx.ssp.iph.delictivo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import mx.ssp.iph.R;
import mx.ssp.iph.delictivo.viewModel.NarrativaHechosDelictivoViewModel;

public class NarrativaHechos_Delictivo extends Fragment {

    private NarrativaHechosDelictivoViewModel mViewModel;
    Button btnGuardarNarrativaHechosDelictivo;

    public static NarrativaHechos_Delictivo newInstance() {
        return new NarrativaHechos_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.narrativa_hechos__delictivo_fragment, container, false);
        /***************************************************************************************/
        btnGuardarNarrativaHechosDelictivo = view.findViewById(R.id.btnGuardarNarrativaHechosDelictivo);

        btnGuardarNarrativaHechosDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
            }
        });


        /***************************************************************************************/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NarrativaHechosDelictivoViewModel.class);
        // TODO: Use the ViewModel
    }

}