package mx.ssp.iph.utilidades.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import mx.ssp.iph.R;

public class ContenedorMaps extends DialogFragment {
    private Fragment fragment;
    private Button btnCerrarFragmentMapa;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.contenedor_maps_fragment,container,false);

        btnCerrarFragmentMapa = root.findViewById(R.id.btnCerrarFragmentMapa);
        btnCerrarFragmentMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return root;
    }

    //Remplaza el fragmento
    private void addFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContenedorMapa, fragment)
                //.addToBackStack(null)//Esta línea Evita pila de fragments
                .commit();
    }
}
