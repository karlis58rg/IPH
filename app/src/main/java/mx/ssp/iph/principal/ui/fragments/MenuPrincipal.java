package mx.ssp.iph.principal.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.ui.activitys.Iph_Administrativo_Up;
import mx.ssp.iph.delictivo.ui.activitys.Iph_Delictivo_Up;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MenuPrincipal extends Fragment {

    LinearLayout lyBtn1,lyBtn2,lyBtn3,lyBtn4;
    private Fragment PrincipalEmergencias,PrincipalBuscar;
    private Funciones funciones;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    private String codigoVerifi,randomCodigoVerifi,randomReferencia;
    int numberRandom;


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
            funciones.Procesando(getActivity(),"","GENERANDO NO. DE FOLIO... Procesando, Por favor espera...");
                //Genera número de folio aleatorio
                GenerarNumerodeReferencia();

                //Enviamos el número de Refrerencia generado temporalmente. Se envía cuando el WS es correcto
                guardarFolioInterno(randomCodigoVerifi);
                //Consume el webservice
                // =================================
                GeneraIPHDelictivo();
            }
        });

        lyBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abre Dialog de por favor espere
                funciones.Procesando(getActivity(),"","GENERANDO NO. DE FOLIO... Procesando, Por favor espera...");

                //Genera número de folio aleatorio
                GenerarNumerodeReferencia();

                //Enviamos el número de Refrerencia generado
                guardarFolioInterno(randomCodigoVerifi,randomReferencia);
                //guardarFolioInterno("202118965497");

                //Consume el webservice
                // Beny
                GeneraIPHAdministrativo();


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

    //***************** GENERA UN NÚMERO DE REFERENCIA ALEATORIO **************************//
    public void GenerarNumerodeReferencia() {
        //Fecha actual desglosada:
        Calendar fecha = Calendar.getInstance();
        int año = fecha.get(Calendar.YEAR);

        Random random = new Random();
        numberRandom = random.nextInt(9000)*99;
        codigoVerifi = String.valueOf(numberRandom);
        randomCodigoVerifi = Integer.toString(año) + codigoVerifi;

        randomReferencia = Integer.toString(año);
    }

    //***************** GUARDA EL FOLIO INTERNO COMO REFERENCIA **************************//
    private void guardarFolioInterno(String FolioInterno, String guardarNoReferencia) {
        share = getContext().getSharedPreferences("main", getContext().MODE_PRIVATE);
        editor = share.edit();
        editor.putString("IDFALTAADMIN", FolioInterno );
        editor.putString("NOREFERENCIA", guardarNoReferencia);
        editor.commit();

        //Cambia de Actividad
        Intent intent = new Intent(getActivity(),Iph_Administrativo_Up.class);
        startActivity(intent);
    }

    //***************** GUARDA EL FOLIO INTERNO COMO REFERENCIA **************************//
    private void guardarFolioInterno(String FolioInterno) {
        share = getContext().getSharedPreferences("main", getContext().MODE_PRIVATE);
        editor = share.edit();
        editor.putString("IDHECHODELICTIVO", FolioInterno );
        editor.commit();

        //Cambia de Actividad
        Intent intent = new Intent(getActivity(), Iph_Delictivo_Up.class);
        startActivity(intent);
    }

    //***************** AGREGA UN NUEVO IPH A LA BASE MEDIANTE EL WEB SERVICE **************************//
    private void GeneraIPHAdministrativo() {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin",randomCodigoVerifi)
                .build();

        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/IPH")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL GENERAR NÚMERO DE FOLIO INTERNO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String resp = myResponse;

                                //***************** RESPUESTA DEL WEBSERVICE **************************//

                                if(resp.equals("true")) {
                                    //Enviamos el número de Refrerencia generado
                                    guardarFolioInterno(randomCodigoVerifi,randomReferencia);
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "NO FUE POSIBLE GENERAR EL NÚMERO DE FOLIO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                                }
                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    //***************** AGREGA UN NUEVO IPH A LA BASE MEDIANTE EL WEB SERVICE **************************//
    private void GeneraIPHDelictivo() {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo",randomCodigoVerifi)
                .build();

        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/IPHDelictivo")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL GENERAR NÚMERO DE FOLIO INTERNO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String resp = myResponse;

                                //***************** RESPUESTA DEL WEBSERVICE **************************//

                                if(resp.equals("true")) {
                                    //Enviamos el número de Refrerencia generado
                                    guardarFolioInterno(randomCodigoVerifi);
                                }
                                else
                                {
                                    Toast.makeText(getContext(), " ", Toast.LENGTH_SHORT).show();
                                }
                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}
