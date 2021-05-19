package mx.ssp.iph.principal.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.ssp.iph.principal.viewmodel.MenuComoFuncionaViewModel;
import mx.ssp.iph.R;

public class MenuComoFunciona extends Fragment {

    private MenuComoFuncionaViewModel mViewModel;

    public static MenuComoFunciona newInstance() {
        return new MenuComoFunciona();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_como_funciona_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MenuComoFuncionaViewModel.class);
        // TODO: Use the ViewModel
    }

}