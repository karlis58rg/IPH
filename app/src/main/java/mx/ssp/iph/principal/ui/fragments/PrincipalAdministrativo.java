package mx.ssp.iph.principal.ui.fragments;

import androidx.lifecycle.ViewModel;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import mx.ssp.iph.administrativo.model.ModeloNoReferencia_Administrativo;
import mx.ssp.iph.administrativo.ui.activitys.Iph_Administrativo_Up;
import mx.ssp.iph.administrativo.ui.fragmets.NoReferencia_Administrativo;
import mx.ssp.iph.principal.viewmodel.PrincipalAdministrativoViewModel;
import mx.ssp.iph.R;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PrincipalAdministrativo extends Fragment {

    //***************** BENY ROMÁN GONZÁLEZ RUIZ. HASAT AQUÍ DEJA DE PRESIONAR CTRL + Z **************************//

    private ArrayList<String> ListaIdFaltaAdmin,ListaNumReferencia;
    private ArrayList<Integer> ListaColorEstatus;
    private PrincipalAdministrativoViewModel mViewModel;
    private ListView lvPrincipalFolioInternoAdministrativo;
    private Funciones funciones;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    private String Usuario = "";
    private String codigoVerifi,randomCodigoVerifi,randomReferencia;
    int numberRandom;
    int actualzarinformacion = 0;


    public static PrincipalAdministrativo newInstance() {
        return new PrincipalAdministrativo();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.principal_administrativo_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(PrincipalAdministrativoViewModel.class);
        final FloatingActionButton fabNuevoIPHAdministrativo = view.findViewById(R.id.fabNuevoIPHAdministrativo);
        lvPrincipalFolioInternoAdministrativo = view.findViewById(R.id.lvPrincipalFolioInternoAdministrativo);
        funciones = new Funciones();
        cargarUsuario();

        //Actualiza solo al inicio o cuando se requiere recargar el fragmento
        if (actualzarinformacion == 0)
        {
            //Comprobamos acceso a intenet y ejecutamos la consulta al webservice
            if (funciones.ping(getContext())){
                SelectIPHAdministrativo();
            }
        }


        //Evento al pulsar sobre un elemento de la lista
        lvPrincipalFolioInternoAdministrativo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Recupera el no. folio interno y lo guarda como preferencia y cambia de Actividad
                guardarFolioInterno(ListaIdFaltaAdmin.get(position));
        }
        });

        //***************** BOTÓN + **************************//
        fabNuevoIPHAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abre Dialog de por favor espere
                funciones.mensajeAlertDialog("GENERANDO NO. DE FOLIO...","Aceptar","Procesando",getContext());

                //Genera número de folio aleatorio
                 GenerarNumerodeReferencia();

                //Enviamos el número de Refrerencia generado
                //guardarFolioInterno(randomCodigoVerifi);


                 //Consume el webservice
                 GeneraIPHAdministrativo();

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }


    //***************** CONSULTA BD TODOS LOS IPH ADMINISTRATIVOS PENDIENTES **************************//
    private void SelectIPHAdministrativo() {

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
                                ListaIdFaltaAdmin = new ArrayList<String>();
                                ListaNumReferencia = new ArrayList<String>();
                                ListaColorEstatus = new ArrayList<Integer>();

                                //CONVERTIR ARREGLO DE JSON A OBJET JSON
                                String ArregloJson = resp.replace("[", "");
                                ArregloJson = ArregloJson.replace("]", "");

                               if(ArregloJson.equals(""))
                               {
                                   Toast.makeText(getContext(), "NO EXISTEN IPH JUSTICIA CÍVICA PENDIENTES", Toast.LENGTH_SHORT).show();
                               }
                               else{
                                   //SEPARAR CADA IPH EN UN ARREGLO
                                   String[] ArrayIPHAdministrativo = ArregloJson.split(Pattern.quote("},"));

                                   //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                   int contadordeIPH=0;
                                   while(contadordeIPH < ArrayIPHAdministrativo.length){
                                       try {
                                           JSONObject jsonjObject = new JSONObject(ArrayIPHAdministrativo[contadordeIPH] + "}");

                                           ListaIdFaltaAdmin.add(jsonjObject.getString("IdFaltaAdmin"));
                                           ListaNumReferencia.add(jsonjObject.getString("NumReferencia"));

                                       } catch (JSONException e) {
                                           e.printStackTrace();
                                           Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                                       }
                                       contadordeIPH++;
                                   }
                               }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                MyAdapter adapter = new MyAdapter(getContext(), ListaIdFaltaAdmin, ListaNumReferencia,ListaColorEstatus);
                                lvPrincipalFolioInternoAdministrativo.setAdapter(adapter);

                                actualzarinformacion++;
                                Log.i("SelectIPHAdministrativo", "Actualiza Información");

                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL SOLICITAR INFORMACION IPH ADMINISTRATIVO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    //***************** ADAPTADOR PARA LLENAR LISTA DE IPH ADMINISTRATIVO **************************//
    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        ArrayList<String> ListaIdFaltaAdmin;
        ArrayList<String> ListaNumReferencia;
        ArrayList<Integer> ListaColorEstatus;

        MyAdapter (Context c, ArrayList<String> ListaIdFaltaAdmin, ArrayList<String> ListaNumReferencia, ArrayList<Integer> ListaColorEstatus) {
            super(c, R.layout.row_iph, ListaIdFaltaAdmin);
            this.context = c;
            this.ListaIdFaltaAdmin = ListaIdFaltaAdmin;
            this.ListaNumReferencia = ListaNumReferencia;
            this.ListaColorEstatus = ListaColorEstatus;
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
              lblPrincipalFolioInternoAdministrativo.setText(ListaIdFaltaAdmin.get(position));
              lblPrincipalNoRegrenciaAdministrativo.setText(ListaNumReferencia.get(position));
            //lyIndicadorStatusIPHEstatusAdministrativo.setBackgroundColor(R.color.status_completo_por_entregar_background);

            return row;
        }
    }

    //***************** GUARDA EL FOLIO INTERNO COMO REFERENCIA **************************//
    private void guardarFolioInterno(String FolioInterno) {
        share = getContext().getSharedPreferences("main", getContext().MODE_PRIVATE);
        editor = share.edit();
        editor.putString("IDFALTAADMIN", FolioInterno );
        editor.commit();

        //Cambia de Actividad
        Intent intent = new Intent(getActivity(),Iph_Administrativo_Up.class);
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
    private void GeneraIPHAdministrativo() {

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