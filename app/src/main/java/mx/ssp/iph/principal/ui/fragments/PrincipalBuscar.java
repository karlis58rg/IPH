package mx.ssp.iph.principal.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import mx.ssp.iph.principal.viewmodel.PrincipalBuscarViewModel;
import mx.ssp.iph.R;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrincipalBuscar extends Fragment {

    private PrincipalBuscarViewModel mViewModel;
    private Funciones funciones;
    private EditText txtFechaInicio,txtFechaFinal,txtBuscarFolioInterno;
    private RadioButton rbBuscarFolioInterno,rbBuscarFecha;
    private RadioGroup rgFiltrosBuscar;
    private Button btnBuscarIPH;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    private String Usuario = "";


    public static PrincipalBuscar newInstance() {
        return new PrincipalBuscar();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.principal_buscar_fragment, container, false);
        funciones = new Funciones();
        txtFechaInicio = root.findViewById(R.id.txtFechaInicio);
        txtFechaFinal = root.findViewById(R.id.txtFechaFinal);
        txtBuscarFolioInterno = root.findViewById(R.id.txtBuscarFolioInterno);
        rbBuscarFolioInterno = root.findViewById(R.id.rbBuscarFolioInterno);
        rbBuscarFecha = root.findViewById(R.id.rbBuscarFecha);
        rgFiltrosBuscar = root.findViewById(R.id.rgFiltrosBuscar);
        btnBuscarIPH = root.findViewById(R.id.btnBuscarIPH);

        cargarUsuario();

        btnBuscarIPH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rbBuscarFolioInterno.isChecked()) {
                    if (txtBuscarFolioInterno.getText().length() < 4) {
                        Toast.makeText(getContext(), "Escribe un Número de Folio Interno o los últimos 4 dígitos", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Buscando Informes", Toast.LENGTH_SHORT).show();
                       // BuscarIPH();
                    }
                }

                if (rbBuscarFecha.isChecked()) {
                    if (txtFechaFinal.getText().toString().equals("AAAA/MM/DD") || txtFechaFinal.getText().toString().equals("") || txtFechaInicio.getText().toString().equals("AAAA/MM/DD") || txtFechaInicio.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Seleciona una fecha inicial y final", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Buscando Informes", Toast.LENGTH_SHORT).show();
                       // BuscarIPH();
                    }
                }
            }
        });
        //***************** FECHA  **************************//
        rgFiltrosBuscar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if(rbBuscarFecha.isChecked())
                {
                    txtFechaFinal.setEnabled(true);
                    txtFechaInicio.setEnabled(true);

                    txtBuscarFolioInterno.setText("");
                    txtBuscarFolioInterno.setEnabled(false);

                }
                else
                {
                    txtBuscarFolioInterno.setEnabled(true);

                    txtFechaFinal.setText("AAAA/MM/DD");
                    txtFechaInicio.setText("AAAA/MM/DD");
                    txtFechaFinal.setEnabled(false);
                    txtFechaInicio.setEnabled(false);

                }
            }
        });


        //***************** FECHA  **************************//
        txtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaInicio,getContext(),getActivity());
            }
        });
        //***************** FECHA  **************************//
        txtFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaFinal,getContext(),getActivity());
            }
        });



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PrincipalBuscarViewModel.class);
        // TODO: Use the ViewModel
    }


    //***************** CONSULTA BD TODOS LOS IPH ADMINISTRATIVOS PENDIENTES **************************//

    //***************** Obtiene el Usuario de las preferencias **************************//
    public void cargarUsuario(){
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        Usuario = share.getString("Usuario", "");
    }

}