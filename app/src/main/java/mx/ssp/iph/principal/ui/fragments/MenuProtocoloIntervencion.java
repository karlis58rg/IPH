package mx.ssp.iph.principal.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.ssp.iph.principal.viewmodel.MenuProtocoloIntervencionViewModel;
import mx.ssp.iph.R;

public class MenuProtocoloIntervencion extends Fragment {

    private MenuProtocoloIntervencionViewModel mViewModel;

    public static MenuProtocoloIntervencion newInstance() {
        return new MenuProtocoloIntervencion();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_protocolo_intervencion_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MenuProtocoloIntervencionViewModel.class);
        // TODO: Use the ViewModel
    }

}