package mx.ssp.iph.principal.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.ui.activitys.Iph_Administrativo_Up;
import mx.ssp.iph.delictivo.ui.activitys.Iph_Delictivo_Up;
import mx.ssp.iph.utilidades.ui.Funciones;


public class MenuPrincipal extends Fragment {

    LinearLayout lyBtn1,lyBtn2,lyBtn3,lyBtn4;
    private Fragment PrincipalEmergencias,PrincipalBuscar;
    private Funciones funciones;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.menu_principal,container,false);
        lyBtn1 = root.findViewById(R.id.lyBtn1);
        lyBtn2 = root.findViewById(R.id.lyBtn2);
        lyBtn3 = root.findViewById(R.id.lyBtn3);
        lyBtn4 = root.findViewById(R.id.lyBtn4);
        funciones = new Funciones();

        PrincipalEmergencias = new PrincipalEmergencias();
        PrincipalBuscar = new PrincipalBuscar();


        lyBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            funciones.Procesando(getActivity(),"","Procesando, Por favor espera...");
                Intent intent = new Intent(getActivity(), Iph_Delictivo_Up.class);
                startActivity(intent);
            }
        });

        lyBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                funciones.Procesando(getActivity(),"","Procesando, Por favor espera...");
                Intent intent = new Intent(getActivity(), Iph_Administrativo_Up.class);
                startActivity(intent);
            }
        });

        lyBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(PrincipalBuscar);
            }
        });
        lyBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(PrincipalEmergencias);
            }
        });


        return root;
    }

    //Intercambia los Fragmentos
    private void addFragment(Fragment fragment) {
       getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                //.addToBackStack(null) //Se quita la pila de fragments. Botón atrás
                .commit();
    }
}
