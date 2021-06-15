package mx.ssp.iph.delictivo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.ssp.iph.R;
import mx.ssp.iph.delictivo.viewModel.LugarDeIntervencionDelictivoViewModel;

public class LugarDeIntervencion_Delictivo extends Fragment {

    private LugarDeIntervencionDelictivoViewModel mViewModel;

    public static LugarDeIntervencion_Delictivo newInstance() {
        return new LugarDeIntervencion_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lugar_de_intervencion__delictivo_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LugarDeIntervencionDelictivoViewModel.class);
        // TODO: Use the ViewModel
    }

}