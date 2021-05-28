package mx.ssp.iph.administrativo.ui.fragmets;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModeloNoReferencia_Administrativo;
import mx.ssp.iph.administrativo.viewModel.NoReferencia_Administrativo_ViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NoReferencia_Administrativo extends Fragment {

    private NoReferencia_Administrativo_ViewModel mViewModel;
    private EditText txtHoraEntregaReferenciaAdministrativo,txtFechaEntregaReferenciaAdministrativo;
    Funciones funciones;

    EditText txtFolioInternoAdministrativo,txtFolioSistemaAdministrativo,txtNoReferenciaAdministrativo,txtEstadoReferenciaAdministrativo,txtGobiernoReferenciaAdministrativo;
    Spinner spInstitucionReferenciaAdministrativo,spMunicipioReferenciaAdministrativo;
    Button btnGuardarReferenciaAdministrativo;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    String guardarIdFaltaAdmin,IDFALTAADMIN;


    String codigoVerifi,randomCodigoVerifi,respuestaJson,descripcionMunicipio;
    int numberRandom;
    private int idEntidadFederativa;
    private int idMunicipio;
    private String municipio;

    public static NoReferencia_Administrativo newInstance() {
        return new NoReferencia_Administrativo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.no_referencia_administrativo_fragment, container, false);

        //************************************** ACCIONES DE LA VISTA **************************************//
        txtFolioInternoAdministrativo = root.findViewById(R.id.txtFolioInternoAdministrativo);
        txtFolioSistemaAdministrativo = root.findViewById(R.id.txtFolioSistemaAdministrativo);
        txtNoReferenciaAdministrativo = root.findViewById(R.id.txtNoReferenciaAdministrativo);
        txtEstadoReferenciaAdministrativo = root.findViewById(R.id.txtEstadoReferenciaAdministrativo);
        txtGobiernoReferenciaAdministrativo = root.findViewById(R.id.txtGobiernoReferenciaAdministrativo);
        txtFechaEntregaReferenciaAdministrativo = root.findViewById(R.id.txtFechaEntregaReferenciaAdministrativo);
        txtHoraEntregaReferenciaAdministrativo = root.findViewById(R.id.txtHoraEntregaReferenciaAdministrativo);

        spInstitucionReferenciaAdministrativo = root.findViewById(R.id.spInstitucionReferenciaAdministrativo);
        spMunicipioReferenciaAdministrativo = root.findViewById(R.id.spMunicipioReferenciaAdministrativo);

        btnGuardarReferenciaAdministrativo = root.findViewById(R.id.btnGuardarReferenciaAdministrativo);
        funciones = new Funciones();
        txtHoraEntregaReferenciaAdministrativo = (EditText) root.findViewById(R.id.txtHoraEntregaReferenciaAdministrativo);
        ListInstitucion();
        ListMunicipios();
        //getMunicipios();

        //***************** Cargar Datos si es que existen  **************************//
        CargarDatos();


        //***************** FECHA  **************************//
        txtFechaEntregaReferenciaAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaEntregaReferenciaAdministrativo,getContext(),getActivity());
            }
        });

        //***************** HORA **************************//
        txtHoraEntregaReferenciaAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txtHoraEntregaReferenciaAdministrativo,getContext(),getActivity());
            }
        });

        //***************** Boton Guardar Datos  **************************//
        btnGuardarReferenciaAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS ", Toast.LENGTH_LONG).show();
                insertNoReferenciaAdministrativa();
            }
        });
        //****************************************************************************//
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NoReferencia_Administrativo_ViewModel.class);
        // TODO: Use the ViewModel
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertNoReferenciaAdministrativa() {
        DataHelper dataHelper = new DataHelper(getContext());
        descripcionMunicipio = (String) spMunicipioReferenciaAdministrativo.getSelectedItem();
        int idDescMunicipio = dataHelper.getIdMunicipio(descripcionMunicipio);
        String idMunicipio = String.valueOf(idDescMunicipio);

        ModeloNoReferencia_Administrativo modeloNoReferencia = new ModeloNoReferencia_Administrativo
                (txtFolioInternoAdministrativo.getText().toString(),
                        txtNoReferenciaAdministrativo.getText().toString(),
                        txtFechaEntregaReferenciaAdministrativo.getText().toString(),
                        txtHoraEntregaReferenciaAdministrativo.getText().toString());

        guardarIdFaltaAdmin = modeloNoReferencia.getIdFaltaAdmin();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin", modeloNoReferencia.getIdFaltaAdmin())
                .add("NumReferencia", modeloNoReferencia.getNumReferencia())
                .add("IdEntidadFederativa","12")
                .add("IdMunicipio", idMunicipio)
                .add("IdInstitucion", "3")
                .add("Fecha", modeloNoReferencia.getFecha())
                .add("Hora", modeloNoReferencia.getHora())
                .add("Usuario", "IPH001")
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/NoReferenciaAdministrativa/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 500){
                    Toast.makeText(getContext(), "EXISTIÓ UN ERROR EN SU CONEXIÓN A INTERNET, INTÉNTELO NUEVAMENTE", Toast.LENGTH_SHORT).show();
                }else if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    NoReferencia_Administrativo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                //guardarFolios();
                                txtFolioInternoAdministrativo.setText("");
                                txtFolioSistemaAdministrativo.setText("");
                                txtNoReferenciaAdministrativo.setText("");
                                txtEstadoReferenciaAdministrativo.setText("");
                                txtGobiernoReferenciaAdministrativo.setText("");
                                txtFechaEntregaReferenciaAdministrativo.setText("");
                                txtHoraEntregaReferenciaAdministrativo.setText("");
                            }else{
                                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("HERE", resp);
                        }
                    });
                }
            }
        });
    }

    public void getMunicipios() {
        DataHelper dataHelper = new DataHelper(getContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatMunicipios")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getActivity(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    NoReferencia_Administrativo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getActivity(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idEntidadFederativa = (ja.getJSONObject(i).getInt("IdEntidadFederativa"));
                                                idMunicipio = (ja.getJSONObject(i).getInt("IdMunicipio"));
                                                municipio = (ja.getJSONObject(i).getString("Municipio"));
                                                dataHelper.insertCatMunicipios(idEntidadFederativa,idMunicipio,municipio);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }

    private void guardarFolios() {
        share = getContext().getSharedPreferences("main", getContext().MODE_PRIVATE);
        editor = share.edit();
        editor.putString("IDFALTAADMIN", guardarIdFaltaAdmin );
        editor.commit();
    }

    public void Random() {
        Random random = new Random();
        numberRandom = random.nextInt(9000)*99;
        codigoVerifi = String.valueOf(numberRandom);
        randomCodigoVerifi = codigoVerifi;
    }
    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarNoReferenciaAdministrativa() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/NoReferenciaAdministrativa?folioInterno="+IDFALTAADMIN)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR SECCIÓN NÚMERO DE REFERENCIA, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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

                                //CONVERTIR ARREGLO DE JSON A OBJET JSON
                                String ArregloJson = resp.replace("[", "");
                                ArregloJson = ArregloJson.replace("]", "");

                                if(ArregloJson.equals(""))
                                {
                                    //Sin Información. Todos los campos vacíos. Solo se llena
                                    txtFolioInternoAdministrativo.setText(IDFALTAADMIN);
                                    txtEstadoReferenciaAdministrativo.setText("GUERRERO");

                                }
                                else{
                                    //Deserializa el json y colcoa el dato correspondiente en cada campo si existe
                                    try {
                                        JSONObject jsonjObject = new JSONObject(ArregloJson);

                                        txtFolioInternoAdministrativo.setText(jsonjObject.getString("IdFaltaAdmin"));
                                        txtFolioSistemaAdministrativo.setText((jsonjObject.getString("NumSistema")).equals("null")?"":jsonjObject.getString("NumSistema"));
                                        txtNoReferenciaAdministrativo.setText((jsonjObject.getString("NumReferencia")).equals("null")?"":jsonjObject.getString("NumReferencia"));
                                        txtEstadoReferenciaAdministrativo.setText("GUERRERO");

                                        String[] Fecha = (jsonjObject.getString("Fecha").replace("-","/")).split("T");
                                        txtFechaEntregaReferenciaAdministrativo.setText((jsonjObject.getString("Fecha")).equals("null")?"":Fecha[0]);
                                        txtHoraEntregaReferenciaAdministrativo.setText((jsonjObject.getString("Hora")).equals("null")?"":jsonjObject.getString("Hora"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON. LLENE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL SOLICITAR INFORMACION NO DE REFERENCIA, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    /********* SPINNER INSTITUCION ****************/
    private void ListInstitucion() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllInstitucion();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE INSTITUCIONES");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spInstitucionReferenciaAdministrativo.setAdapter(adapter);
        } else {
            dataHelper.insertCatInstitucion(1, "GUARDIA NACIONAL");
            dataHelper.insertCatInstitucion(2, "POLICIA FEDERAL MINISTERIAL");
            dataHelper.insertCatInstitucion(3, "POLICIA MINISTERIAL");
            dataHelper.insertCatInstitucion(4, "POLICIA MANDO UNICO");
            dataHelper.insertCatInstitucion(5, "POLICIA ESTATAL");
            dataHelper.insertCatInstitucion(6, "POLICIA MUNICIPAL");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spInstitucionReferenciaAdministrativo.setAdapter(adapter);
        }
    }

    //***************** SE RECUPERA EL FOLIO INTERNO **************************//
    private void CargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        IDFALTAADMIN= share.getString("IDFALTAADMIN", "");

        if (IDFALTAADMIN.equals(""))
        {
            Toast.makeText(getContext(), "EL FOLIO INTERNO NO EXISTE. POR FAVOR REINCICIE LA APP CREE UN NUEVO INFORME.", Toast.LENGTH_LONG).show();
        }
        else
        {
            //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
            if (funciones.ping(getContext()))
            {
                //Toast.makeText(getContext(), "cargarNoReferenciaAdministrativa()", Toast.LENGTH_LONG).show();
                cargarNoReferenciaAdministrativa();
            }
        }

    }


    /**************** SPINNER MUNICIPIOS **************************************/
    private void ListMunicipios() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllMunicipios();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MUNICIPIOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spMunicipioReferenciaAdministrativo.setAdapter(adapter);
        }
    }
}