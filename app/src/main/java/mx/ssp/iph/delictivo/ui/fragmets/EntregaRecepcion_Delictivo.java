package mx.ssp.iph.delictivo.ui.fragmets;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import mx.ssp.iph.R;
import mx.ssp.iph.delictivo.viewModel.EntregaRecepcion_DelictivoViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;


public class EntregaRecepcion_Delictivo extends Fragment {

    private EntregaRecepcion_DelictivoViewModel mViewModel;
    private Funciones funciones;


    public static EntregaRecepcion_Delictivo newInstance() {
        return new EntregaRecepcion_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entrega_recepcion_fragment, container, false);
        /***************************************************************************************/
        funciones = new Funciones();
        funciones.CambiarTituloSeccionesDelictivo("ANEXO F. ENTREGA-RECPCIÓN DEL LUGAR DE LA INTERVENCIÓN",getContext(),getActivity());


        /***************************************************************************************/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EntregaRecepcion_DelictivoViewModel.class);
        // TODO: Use the ViewModel
    }
}