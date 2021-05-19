package mx.ssp.iph.principal.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import mx.ssp.iph.administrativo.ui.activitys.Iph_Administrativo_Up;
import mx.ssp.iph.principal.viewmodel.PrincipalAdministrativoViewModel;
import mx.ssp.iph.R;

public class PrincipalAdministrativo extends Fragment {

    private PrincipalAdministrativoViewModel mViewModel;

    public static PrincipalAdministrativo newInstance() {
        return new PrincipalAdministrativo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.principal_administrativo_fragment, container, false);
        final FloatingActionButton fabNuevoIPHDelictivo = view.findViewById(R.id.fabNuevoIPHDelictivo);

        fabNuevoIPHDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Iph_Administrativo_Up.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PrincipalAdministrativoViewModel.class);
        // TODO: Use the ViewModel
    }

}