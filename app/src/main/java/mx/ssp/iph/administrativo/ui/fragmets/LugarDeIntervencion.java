package mx.ssp.iph.administrativo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.viewModel.LugarDeIntervencionViewModel;

public class LugarDeIntervencion extends Fragment {

    private LugarDeIntervencionViewModel mViewModel;

    public static LugarDeIntervencion newInstance() {
        return new LugarDeIntervencion();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lugar_de_intervencion_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LugarDeIntervencionViewModel.class);
        // TODO: Use the ViewModel
    }

}