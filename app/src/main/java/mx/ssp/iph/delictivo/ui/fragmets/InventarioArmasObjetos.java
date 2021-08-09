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
import mx.ssp.iph.delictivo.viewModel.InventarioArmasObjetosViewModel;

public class InventarioArmasObjetos extends Fragment {

    private InventarioArmasObjetosViewModel mViewModel;

    public static InventarioArmasObjetos newInstance() {
        return new InventarioArmasObjetos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventario_armas_objetos, container, false);

        /***************************************************************************************/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InventarioArmasObjetosViewModel.class);
        // TODO: Use the ViewModel
    }


}