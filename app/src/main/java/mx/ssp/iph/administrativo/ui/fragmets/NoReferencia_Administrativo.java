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
import mx.ssp.iph.administrativo.viewModel.NoReferencia_Administrativo_ViewModel;

public class NoReferencia_Administrativo extends Fragment {

    private NoReferencia_Administrativo_ViewModel mViewModel;

    public static NoReferencia_Administrativo newInstance() {
        return new NoReferencia_Administrativo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.no_referencia_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NoReferencia_Administrativo_ViewModel.class);
        // TODO: Use the ViewModel
    }

}