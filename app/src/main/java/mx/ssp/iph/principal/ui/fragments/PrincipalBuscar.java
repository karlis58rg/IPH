package mx.ssp.iph.principal.ui.fragments;

import androidx.annotation.ColorInt;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import mx.ssp.iph.administrativo.ui.activitys.Iph_Administrativo_Up;
import mx.ssp.iph.delictivo.ui.activitys.Iph_Delictivo_Up;
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

    private ArrayList<String> ListaIdIPH,ListaNumReferenciaDelictivo,ListaTipoIPH, ListaUsuarioIPH;
    private ListView lvIPHBuscados;
    String FechaInicial,FechaFinal,FolioInterno;
    ProgressDialog progressDialog;


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
        lvIPHBuscados= root.findViewById(R.id.lvIPHBuscados);

        cargarUsuario();

        //Evento al pulsar sobre un elemento de la lista
        lvIPHBuscados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (ListaIdIPH.get(position).equals("SIN INFORMACION"))
                {
                    Toast.makeText(getContext(), "EL REGISTRO NO CONTIENE INFORMACIÓN", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (ListaTipoIPH.get(position).equals("Administrativo"))
                    {
                        Log.i("buscar", "Seleccionó IPH Administrativo");
                        guardarIPHAdministrativo(ListaIdIPH.get(position),ListaNumReferenciaDelictivo.get(position));
                    }else {
                        Log.i("buscar", "Seleccionó IPH Delictivo");
                        guardarIPHDelictivo(ListaIdIPH.get(position),ListaNumReferenciaDelictivo.get(position));

                    }
                }



                //Recupera el no. folio interno y lo guarda como preferencia y cambia de Actividad
                //guardarFolioInterno(ListaIdFaltaAdmin.get(position),ListaNumReferencia.get(position));
            }
        });

        btnBuscarIPH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rbBuscarFolioInterno.isChecked()) {
                    if (txtBuscarFolioInterno.getText().length() < 4) {
                        Toast.makeText(getContext(), "Escribe un Número de Folio Interno o los últimos 4 dígitos", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog = ProgressDialog.show(getActivity(), "", "Buscando, por favor espere. . . ", true);
                        FolioInterno = txtBuscarFolioInterno.getText().toString();
                        FechaInicial = "";
                        FechaFinal = "";
                       BuscarIPH();
                    }
                }

                if (rbBuscarFecha.isChecked()) {
                    if (txtFechaFinal.getText().toString().equals("AAAA/MM/DD") || txtFechaFinal.getText().toString().equals("") || txtFechaInicio.getText().toString().equals("AAAA/MM/DD") || txtFechaInicio.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Seleciona una fecha inicial y final", Toast.LENGTH_SHORT).show();
                    } else {

                        progressDialog = ProgressDialog.show(getActivity(), "", "Buscando, por favor espere. . . ", true);
                        FolioInterno = "";
                        FechaInicial = txtFechaInicio.getText().toString();
                        FechaFinal = txtFechaFinal.getText().toString();
                        BuscarIPH();
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
    private void BuscarIPH() {
        String asd = "http://189.254.7.167/WebServiceIPH/api/HDHechoDelictivo?folioId="+FolioInterno+"&fechaInicial="+FechaInicial+"&fechaFinal="+FechaFinal+"&userId="+Usuario;
        Log.i("buscar", asd);


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout (10, TimeUnit.SECONDS) // Tiempo de espera de conexión agotado
                .writeTimeout (10, TimeUnit.SECONDS) // Tiempo de espera de escritura de socket
                .readTimeout (30, TimeUnit.SECONDS) // Tiempo de espera de lectura de socket
                .build();

        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDHechoDelictivo?folioId="+FolioInterno+"&fechaInicial="+FechaInicial+"&fechaFinal="+FechaFinal+"&userId="+Usuario)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), e.toString()+"ERROR AL CONSULTAR IPH, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                ListaIdIPH = new ArrayList<String>();
                                ListaNumReferenciaDelictivo = new ArrayList<String>();
                                ListaTipoIPH = new ArrayList<String>();
                                ListaUsuarioIPH = new ArrayList<String>();

                                //CONVERTIR ARREGLO DE JSON A OBJET JSON
                                String ArregloJson = resp.replace("[", "");
                                ArregloJson = ArregloJson.replace("]", "");

                                if(ArregloJson.equals(""))
                                {
                                    Toast.makeText(getContext(), "NO EXISTEN INFORMES.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    //SEPARAR CADA IPH EN UN ARREGLO
                                    String[] ArrayIPHAdministrativo = ArregloJson.split(Pattern.quote("},"));

                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordeIPH=0;
                                    while(contadordeIPH < ArrayIPHAdministrativo.length){
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayIPHAdministrativo[contadordeIPH] + "}");

                                            ListaIdIPH.add(jsonjObject.getString("IdFaltaHecho"));
                                            ListaNumReferenciaDelictivo.add(jsonjObject.getString("NumReferencia"));
                                            ListaTipoIPH.add(jsonjObject.getString("Tipo"));
                                            ListaUsuarioIPH.add(jsonjObject.getString("IdPoliciaPrimerRespondiente"));


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "BUSCAR: ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordeIPH++;
                                    }
                                }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                PrincipalBuscar.MyAdapter adapter = new PrincipalBuscar.MyAdapter(getContext(), ListaIdIPH, ListaNumReferenciaDelictivo,ListaTipoIPH,ListaUsuarioIPH);
                                lvIPHBuscados.setAdapter(adapter);
                                funciones.ajustaAlturaListView(lvIPHBuscados,250);

                                progressDialog.dismiss();

                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL SOLICITAR INFORMACION IPH BUSCADOS, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


    //***************** ADAPTADOR PARA LLENAR LISTA DE IPH ADMINISTRATIVO **************************//
    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        private ArrayList<String> ListaIdIPH,ListaNumReferenciaDelictivo,ListaTipoIPH, ListaUsuarioIPH;


        MyAdapter (Context c, ArrayList<String> ListaIdIPH, ArrayList<String> ListaNumReferenciaDelictivo, ArrayList<String> ListaTipoIPH,ArrayList<String> ListaUsuarioIPH) {
            super(c, R.layout.row_iph, ListaIdIPH);
            this.context = c;
            this.ListaIdIPH = ListaIdIPH;
            this.ListaNumReferenciaDelictivo = ListaNumReferenciaDelictivo;
            this.ListaTipoIPH = ListaTipoIPH;
            this.ListaUsuarioIPH = ListaUsuarioIPH;

        }

        @SuppressLint("ResourceAsColor")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_iph, parent, false);

            TextView lblPrincipalFolioInternoAdministrativo = row.findViewById(R.id.lblPrincipalFolioInternoAdministrativo);
            TextView lblPrincipalNoRegrenciaAdministrativo = row.findViewById(R.id.lblPrincipalNoRegrenciaAdministrativo);
            TextView lblTipoIPH = row.findViewById(R.id.lblTipoIPH);
            TextView lblUsuario = row.findViewById(R.id.lblUsuario);

            // Asigna los valores
            lblPrincipalFolioInternoAdministrativo.setText(ListaIdIPH.get(position));
            lblPrincipalNoRegrenciaAdministrativo.setText(ListaNumReferenciaDelictivo.get(position));
            lblTipoIPH.setText(ListaTipoIPH.get(position));
            lblUsuario.setText(ListaUsuarioIPH.get(position));


            return row;
        }
    }

    //***************** Obtiene el Usuario de las preferencias **************************//
    public void cargarUsuario(){
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        Usuario = share.getString("Usuario", "");
    }

    //***************** GUARDA EL FOLIO INTERNO COMO REFERENCIA ADMINISTRATIVO**************************//
    private void guardarIPHAdministrativo(String FolioInterno, String guardarNoReferencia) {
        share = getContext().getSharedPreferences("main", getContext().MODE_PRIVATE);
        editor = share.edit();
        editor.putString("IDFALTAADMIN", FolioInterno );
        editor.putString("NOREFERENCIA", guardarNoReferencia);
        editor.commit();

        //Cambia de Actividad
        Intent intent = new Intent(getActivity(), Iph_Administrativo_Up.class);
        startActivity(intent);
    }

    //***************** GUARDA EL FOLIO INTERNO COMO REFERENCIA DELICTIVO **************************//
    private void guardarIPHDelictivo(String FolioInterno, String guardarNoReferencia) {
        share = getContext().getSharedPreferences("main", getContext().MODE_PRIVATE);
        editor = share.edit();
        editor.putString("IDHECHODELICTIVO", FolioInterno );
        editor.putString("NOREFERENCIA", guardarNoReferencia);
        editor.commit();

        //Cambia de Actividad
        Intent intent = new Intent(getActivity(), Iph_Delictivo_Up.class);
        startActivity(intent);
    }

}