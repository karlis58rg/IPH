package mx.ssp.iph.delictivo.ui.fragmets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import mx.ssp.iph.R;
import mx.ssp.iph.delictivo.viewModel.ConocimientoHechoViewModel;
import mx.ssp.iph.delictivo.viewModel.EntrevistasDelictivoViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;

public class EntrevistasDelictivo extends Fragment {

    private EntrevistasDelictivoViewModel mViewModel;

    private Funciones funciones;

    public static EntrevistasDelictivo newInstance() {
        return new EntrevistasDelictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entrevistas_delictivo_fragment, container, false);
        /************************************************************************************/
        funciones = new Funciones();


        /****************************************************************************************/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EntrevistasDelictivoViewModel.class);
        // TODO: Use the ViewModel
    }

}