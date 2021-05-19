package mx.ssp.iph.principal.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.ssp.iph.principal.viewmodel.PrincipalDelictivoViewModel;
import mx.ssp.iph.R;

public class PrincipalDelictivo extends Fragment {

    private PrincipalDelictivoViewModel mViewModel;

    public static PrincipalDelictivo newInstance() {
        return new PrincipalDelictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.principal_delictivo_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PrincipalDelictivoViewModel.class);
        // TODO: Use the ViewModel
    }

}