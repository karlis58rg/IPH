package mx.ssp.iph.utilidades.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import mx.ssp.iph.R;
import mx.ssp.iph.principal.ui.activitys.Principal;
import mx.ssp.iph.principal.ui.fragments.MenuPrincipal;

public class DialogFragmentSalir extends DialogFragment {

    private Button btnMenu,btnSalir,btnCancelar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_fragment_salir,container,false);
        btnMenu = root.findViewById(R.id.btnMenu);
        btnSalir = root.findViewById(R.id.btnSalir);
        btnCancelar = root.findViewById(R.id.btnCancelar);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Principal.class);
                startActivity(intent);            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragmentSalir.super.dismiss();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragmentSalir.super.dismiss();

                Intent intent = new Intent(getActivity(),ExitActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }
}
