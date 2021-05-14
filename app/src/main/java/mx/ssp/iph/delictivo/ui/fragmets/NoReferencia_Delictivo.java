package mx.ssp.iph.delictivo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.ssp.iph.delictivo.viewModel.NoReferencia_Delictivo_ViewModel;
import mx.ssp.iph.R;

public class NoReferencia_Delictivo extends Fragment {

    private NoReferencia_Delictivo_ViewModel mViewModel;

    public static NoReferencia_Delictivo newInstance() {
        return new NoReferencia_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.no_referencia_delictivo_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NoReferencia_Delictivo_ViewModel.class);
        // TODO: Use the ViewModel
    }

}