package mx.ssp.iph.principal.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import mx.ssp.iph.administrativo.ui.activitys.Iph_Administrativo_Up;
import mx.ssp.iph.delictivo.ui.activitys.Iph_Delictivo;
import mx.ssp.iph.delictivo.ui.activitys.Iph_Delictivo_Up;
import mx.ssp.iph.principal.viewmodel.PrincipalAdministrativoViewModel;
import mx.ssp.iph.principal.viewmodel.PrincipalDelictivoViewModel;
import mx.ssp.iph.R;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PrincipalDelictivo extends Fragment {

    private PrincipalDelictivoViewModel mViewModel;
    private ArrayList<String> ListaIdDelictivo,ListaNumReferenciaDelictivo;
    private ArrayList<Integer> ListaColorEstatusDelictivo;
    private ListView lvPrincipalFolioInternoDelictivo;
    private Funciones funciones;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    private String Usuario = "";
    private String codigoVerifi,randomCodigoVerifi,randomReferencia;
    int numberRandom;
    int actualzarinformacionDelictivo = 0;

    public static PrincipalDelictivo newInstance() {
        return new PrincipalDelictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.principal_delictivo_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(PrincipalDelictivoViewModel.class);
        final FloatingActionButton fabNuevoIPHDelictivo = root.findViewById(R.id.fabNuevoIPHDelictivo);
        lvPrincipalFolioInternoDelictivo = root.findViewById(R.id.lvPrincipalFolioInternoDelictivo);
        funciones = new Funciones();
        cargarUsuario();

        //Actualiza solo al inicio o cuando se requiere recargar el fragmento
        if (actualzarinformacionDelictivo == 0)
        {
            //Comprobamos acceso a intenet y ejecutamos la consulta al webservice
            if (funciones.ping(getContext())){
                SelectIPHDelictivo();
            }
        }


        //Evento al pulsar sobre un elemento de la lista
        lvPrincipalFolioInternoDelictivo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Recupera el no. folio interno y lo guarda como preferencia y cambia de Actividad
                guardarFolioInterno(ListaIdDelictivo.get(position));
            }
        });

        //***************** BOTÓN + **************************//
        fabNuevoIPHDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abre Dialog de por favor espere
                funciones.mensajeAlertDialog("GENERANDO NO. DE FOLIO...","Aceptar","Procesando",getContext());

                //Genera número de folio aleatorio
                GenerarNumerodeReferencia();

                //Enviamos el número de Refrerencia generado temporalmente. Se envía cuando el WS es correcto
                guardarFolioInterno(randomCodigoVerifi);
                //Consume el webservice
               // =================================
                // GeneraIPHDelictivo();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PrincipalDelictivoViewModel.class);
        // TODO: Use the ViewModel
    }

    //***************** CONSULTA BD TODOS LOS IPH ADMINISTRATIVOS PENDIENTES **************************//
    private void SelectIPHDelictivo() {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout (10, TimeUnit.SECONDS) // Tiempo de espera de conexión agotado
                .writeTimeout (10, TimeUnit.SECONDS) // Tiempo de espera de escritura de socket
                .readTimeout (30, TimeUnit.SECONDS) // Tiempo de espera de lectura de socket
                .build();

        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/NoReferenciaAdministrativa?usuario="+Usuario)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), e.toString()+"ERROR AL CONSULTAR FOLIOS IPH ADMINISTRATIVO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                ListaIdDelictivo = new ArrayList<String>();
                                ListaNumReferenciaDelictivo = new ArrayList<String>();
                                ListaColorEstatusDelictivo = new ArrayList<Integer>();

                                //CONVERTIR ARREGLO DE JSON A OBJET JSON
                                String ArregloJson = resp.replace("[", "");
                                ArregloJson = ArregloJson.replace("]", "");

                                if(ArregloJson.equals(""))
                                {
                                    Toast.makeText(getContext(), "NO EXISTEN IPH DELICTIVOS PENDIENTES", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    //SEPARAR CADA IPH EN UN ARREGLO
                                    String[] ArrayIPHAdministrativo = ArregloJson.split(Pattern.quote("},"));

                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordeIPH=0;
                                    while(contadordeIPH < ArrayIPHAdministrativo.length){
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayIPHAdministrativo[contadordeIPH] + "}");

                                            ListaIdDelictivo.add(jsonjObject.getString("IdFaltaAdmin"));
                                            ListaNumReferenciaDelictivo.add(jsonjObject.getString("NumReferencia"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordeIPH++;
                                    }
                                }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                PrincipalDelictivo.MyAdapter adapter = new PrincipalDelictivo.MyAdapter(getContext(), ListaIdDelictivo, ListaNumReferenciaDelictivo,ListaColorEstatusDelictivo);
                                lvPrincipalFolioInternoDelictivo.setAdapter(adapter);

                                actualzarinformacionDelictivo++;
                                Log.i("SelectIPHDelictivo", "Actualiza Información");

                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL SOLICITAR INFORMACION IPH DELICTIVO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    //***************** ADAPTADOR PARA LLENAR LISTA DE IPH ADMINISTRATIVO **************************//
    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> ListaIdDelictivo;
        ArrayList<String> ListaNumReferenciaDelictivo;
        ArrayList<Integer> ListaColorEstatusDelictivo;

        MyAdapter (Context c, ArrayList<String> ListaIdDelictivo, ArrayList<String> ListaNumReferenciaDelictivo, ArrayList<Integer> ListaColorEstatusDelictivo) {
            super(c, R.layout.row_iph, ListaIdDelictivo);
            this.context = c;
            this.ListaIdDelictivo = ListaIdDelictivo;
            this.ListaNumReferenciaDelictivo = ListaNumReferenciaDelictivo;
            this.ListaColorEstatusDelictivo = ListaColorEstatusDelictivo;
        }

        @SuppressLint("ResourceAsColor")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_iph, parent, false);

            TextView lblPrincipalFolioInternoAdministrativo = row.findViewById(R.id.lblPrincipalFolioInternoAdministrativo);
            TextView lblPrincipalNoRegrenciaAdministrativo = row.findViewById(R.id.lblPrincipalNoRegrenciaAdministrativo);
            LinearLayout lyIndicadorStatusIPHEstatusAdministrativo = row.findViewById(R.id.lyIndicadorStatusIPHEstatusAdministrativo);

            // Asigna los valores
            lblPrincipalFolioInternoAdministrativo.setText(ListaIdDelictivo.get(position));
            lblPrincipalNoRegrenciaAdministrativo.setText(ListaNumReferenciaDelictivo.get(position));
            //lyIndicadorStatusIPHEstatusAdministrativo.setBackgroundColor(R.color.status_completo_por_entregar_background);

            return row;
        }
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

    //***************** Obtiene el Usuario de las preferencias **************************//
    public void cargarUsuario(){
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        Usuario = share.getString("Usuario", "");
    }

    //***************** AGREGA UN NUEVO IPH A LA BASE MEDIANTE EL WEB SERVICE **************************//
    private void GeneraIPHDelictivo() {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin",randomCodigoVerifi)
                .add("NumReferencia",randomReferencia)
                .add("Usuario",Usuario)
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
                                    guardarFolioInterno(randomCodigoVerifi);
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
}