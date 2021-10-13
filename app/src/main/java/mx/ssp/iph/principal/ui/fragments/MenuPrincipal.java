package mx.ssp.iph.principal.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.ui.activitys.Iph_Administrativo_Up;
import mx.ssp.iph.delictivo.ui.activitys.Iph_Delictivo_Up;
import mx.ssp.iph.delictivo.ui.fragmets.HechosDelictivos;
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
    String noReferencia,edo = "",inst = "",gob = "",mpio = "",fecha = "",dia = "01",mes = "01",anio = "2021",tiempo = "",hora = "",minutos = "",
            cargarIdPoliciaPrimerRespondiente = "",cargarIdHechoDelictivo = "",respuestaJson = "",descAutoridad,descCargo;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.menu_principal,container,false);
        lyBtn1 = root.findViewById(R.id.lyBtn1);
        lyBtn2 = root.findViewById(R.id.lyBtn2);
        lyBtn3 = root.findViewById(R.id.lyBtn3);
        lyBtn4 = root.findViewById(R.id.lyBtn4);
        funciones = new Funciones();
        cargarDatos();

        PrincipalEmergencias = new PrincipalEmergencias();
        PrincipalBuscar = new PrincipalBuscar();



        lyBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            funciones.Procesando(getActivity(),"","GENERANDO NO. DE FOLIO... \n PROCESANDO, POR FAVOR ESPERA...");
                //Genera número de folio aleatorio
                getNumReferencia();

            }
        });

        lyBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abre Dialog de por favor espere
                funciones.Procesando(getActivity(),"","GENERANDO NO. DE FOLIO... \n PROCESANDO, POR FAVOR ESPERA...");

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

    //***************** GUARDA EL FOLIO INTERNO Administrativo **************************//
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

    //***************** GUARDA EL FOLIO INTERNO Delictivo **************************//
    private void guardarFolioInternoyReferenciaDelictivo(String FolioInterno) {
        Log.i("zz", "guardarFolioInternoyReferenciaDelictivo");

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
                .add("NumReferencia",randomReferencia)
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
        Log.i("ME", "GeneraIPHDelictivo");
        Log.i("ME", "folio:"+randomCodigoVerifi);
        Log.i("ME", "numero:"+noReferencia);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        fecha = dateFormat.format(date);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo",randomCodigoVerifi)
                .add("NumReferencia",noReferencia)
                .add("Fecha", fecha)
                .add("IdPoliciaPrimerRespondiente", cargarIdPoliciaPrimerRespondiente)
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
                                Log.i("ME", "resp:"+resp);

                                if(resp.equals("true")) {
                                    //Enviamos el número de Refrerencia generado
                                    guardarFolioInternoyReferenciaDelictivo(randomCodigoVerifi);
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "no es true", Toast.LENGTH_SHORT).show();
                                    Log.i("ME", "resp:"+resp);

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

    private void getNumReferencia() {
        //*************** FECHA **********************//
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        fecha = dateFormat.format(date);
        String [] partsFecha = fecha.split("/");
        anio = partsFecha[0];
        mes = partsFecha[1];
        dia = partsFecha[2];
        //*************** HORA **********************//
        Date time = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        tiempo = timeFormat.format(time);
        String[] partsTiempo = tiempo.split(":");
        hora = partsTiempo[0];
        minutos = partsTiempo[1];
        /******************************************************/
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDHechoDelictivo?usuario="+cargarIdPoliciaPrimerRespondiente)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getActivity(),"ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                     getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "FALSE";
                                if(myResponse.equals(respuestaJson)){
                                    Toast.makeText(getActivity(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                }else{
                                    JSONObject jObj = null;
                                    jObj = new JSONObject(""+myResponse+"");
                                    edo = jObj.getString("IdEntidadFederativa");
                                    inst = jObj.getString("IdInstitucion");
                                    gob = jObj.getString("IdGobierno");
                                    mpio = jObj.getString("IdMunicipio");
                                    noReferencia = edo+inst+gob+mpio+dia+mes+anio+hora+minutos;
                                    Log.i("REFERENCIA", ""+noReferencia);

                                    GenerarNumerodeReferencia();
                                    // =================================
                                    GeneraIPHDelictivo();
                                    Log.i("AR", ""+jObj);
                                }

                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }

                    });
                }
            }

        });
    }

    public void cargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdPoliciaPrimerRespondiente = share.getString("Usuario", "SIN INFORMACION");
    }

}
