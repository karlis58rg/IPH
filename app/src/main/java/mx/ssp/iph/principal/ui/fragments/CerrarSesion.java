package mx.ssp.iph.principal.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import mx.ssp.iph.Login;
import mx.ssp.iph.R;
import mx.ssp.iph.principal.ui.activitys.Principal;

import static android.content.Context.MODE_PRIVATE;

public class CerrarSesion extends Fragment {

    SharedPreferences share;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cerrar_session,container,false);


        //Guarda el usaurio con espacio en blanco
        guardarUsuario();

        //Redirecciona al Login
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);


        return view;
    }

    private void guardarUsuario() {
        share = getActivity().getSharedPreferences("main", MODE_PRIVATE);
        editor = share.edit();
        editor.putString("Usuario", "" );
        editor.commit();
    }
}
