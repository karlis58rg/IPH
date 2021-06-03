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
import java.util.IllegalFormatCodePointException;
import java.util.Random;


import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModeloNoReferencia_Administrativo;
import mx.ssp.iph.administrativo.viewModel.NoReferencia_Administrativo_ViewModel;
import mx.ssp.iph.principal.ui.fragments.PrincipalAdministrativo;
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
    String guardarNoReferencia,IDFALTAADMIN,GETUSUARIO;


    String codigoVerifi,randomCodigoVerifi,respuestaJson,descripcionMunicipio,descripcionInstitucion,guardarIdFaltaAdmin;
    int numberRandom;
    private int idEntidadFederativa;
    private int idMunicipio;
    private String municipio;
    int idAutoridadAdmin; String autoridadAdmin;
    int idCargo; String cargo;
    int idConocimiento; String conocimiento;
    int idFiscaliaAutoridad; String fiscaliaAutoridad;
    int idInstitucion;String institucion;
    int idLugarTraslado; String lugarTraslado; String descripcion;
    int idNacionalida; String nacionalida; String desNacionalidad;
    int idSexo;String sexo;
    String idUnidad; String unidad; String idMarca; int idSubMarca; int modelo; String descripcionU; int idInstitucionU;

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
        ListAutoridadAdmin();
        ListCargo();
        ListConocimientoInfraccion();
        ListFiscaliaAutoridad();
        ListInstitucion();
        ListLugarTraslado();
        ListMunicipios();
        ListNacionalidad();
        ListSexo();
        ListUnidad();

/*
        getAutoridadAdmin();
        getCargo();
        getConocimientoInfraccion();
        getFiscaliaAutoridad();
        getInstitucion();
        getLugarTraslado();
        getMunicipios();
        getNacionalidad();
        getSexo();
        getUnidad();
*/
        //***************** Cargar Datos si es que existen  **************************//
        CargarDatos();

        txtEstadoReferenciaAdministrativo.setEnabled(false);
        txtFolioInternoAdministrativo.setText(IDFALTAADMIN);
        txtFolioInternoAdministrativo.setEnabled(false);
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

    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            Toast.makeText(getContext(),""+spinner.getItemAtPosition(i).toString() + " - " + spinner.getItemIdAtPosition(i),Toast.LENGTH_SHORT).show();



            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    private void ConsultavalorSpinerconId(){

    }

    //private method of your class
    private int getIndexbyId(Spinner spinner, int idSpiner){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemIdAtPosition(i) == idSpiner)
            {
                return i;
            }
        }
        return 0;
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertNoReferenciaAdministrativa() {
        DataHelper dataHelper = new DataHelper(getContext());
        descripcionMunicipio = (String) spMunicipioReferenciaAdministrativo.getSelectedItem();
        int idDescMunicipio = dataHelper.getIdMunicipio(descripcionMunicipio);
        String idMunicipio = String.valueOf(idDescMunicipio);

        descripcionInstitucion = (String) spInstitucionReferenciaAdministrativo.getSelectedItem();
        int idDescItritucion = dataHelper.getIdInstitucion(descripcionInstitucion);
        String idInstitucion = String.valueOf(idDescItritucion);

        ModeloNoReferencia_Administrativo modeloNoReferencia = new ModeloNoReferencia_Administrativo
                (       txtNoReferenciaAdministrativo.getText().toString(),
                        txtGobiernoReferenciaAdministrativo.getText().toString(),
                        txtFechaEntregaReferenciaAdministrativo.getText().toString(),
                        txtHoraEntregaReferenciaAdministrativo.getText().toString());

        guardarNoReferencia = modeloNoReferencia.getNumReferencia();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin", IDFALTAADMIN)
                .add("NumReferencia", modeloNoReferencia.getNumReferencia())
                .add("IdEntidadFederativa","12")
                .add("IdMunicipio", idMunicipio)
                .add("IdInstitucion", idInstitucion)
                .add("Gobierno", modeloNoReferencia.getIdGobierno())
                .add("Fecha", modeloNoReferencia.getFecha())
                .add("Hora", modeloNoReferencia.getHora())
                .add("Usuario", GETUSUARIO)
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
                                guardarNoReferencia();
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


    private void guardarFolios() {
        share = getContext().getSharedPreferences("main", getContext().MODE_PRIVATE);
        editor = share.edit();
        editor.putString("IDFALTAADMIN", guardarIdFaltaAdmin );
        editor.commit();
    }
    private void guardarNoReferencia() {
        share = getContext().getSharedPreferences("main", getContext().MODE_PRIVATE);
        editor = share.edit();
        editor.putString("NOREFERENCIA", guardarNoReferencia);
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
        GETUSUARIO = share.getString("Usuario", "");

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

        //spInstitucionReferenciaAdministrativo.setSelection(getIndexbyId(spInstitucionReferenciaAdministrativo, 3));
    }

    /**************** SPINNER **************************************/
    private void ListAutoridadAdmin() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllAutoridadAdmin();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE AUTORIDAD ADMIN");
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            //spMunicipioReferenciaAdministrativo.setAdapter(adapter);
        }
    }
    private void ListCargo() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllCargos();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE CARGOS");
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            //spMunicipioReferenciaAdministrativo.setAdapter(adapter);
        }
    }
    private void ListConocimientoInfraccion() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllConocimientoInfraccion();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE CONOCIMIENTO INFRACCION");
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            //spMunicipioReferenciaAdministrativo.setAdapter(adapter);
        }
    }
    private void ListFiscaliaAutoridad() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllFiscaliaAutoridad();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE FISCALIA AUTORIDAD");
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            //spMunicipioReferenciaAdministrativo.setAdapter(adapter);
        }
    }
    private void ListLugarTraslado() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllLugarTraslado();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE LUGAR TRASLADO");
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            //spMunicipioReferenciaAdministrativo.setAdapter(adapter);
        }
    }
    private void ListMunicipios() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllMunicipios();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MUNICIPIOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spMunicipioReferenciaAdministrativo.setAdapter(adapter);
        }
    }
    private void ListNacionalidad() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllNacionalidad();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE NACIONALIDAD");
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            //spMunicipioReferenciaAdministrativo.setAdapter(adapter);
        }
    }
    private void ListSexo() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllSexo();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE SEXO");
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            //spMunicipioReferenciaAdministrativo.setAdapter(adapter);
        }
    }
    private void ListUnidad() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllUnidad();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE UNIDAD");
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            //spMunicipioReferenciaAdministrativo.setAdapter(adapter);
        }
    }



    /********************************** CARGA DE TABLAS POR WS ***************************************/
    public void getAutoridadAdmin() {
        DataHelper dataHelper = new DataHelper(getContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatAutoridadAdmin")
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
                                                idAutoridadAdmin = (ja.getJSONObject(i).getInt("IdAutoridadAdmin"));
                                                autoridadAdmin = (ja.getJSONObject(i).getString("AutoridadAdmin"));
                                                dataHelper.insertCatAutoridadAdmin(idAutoridadAdmin,autoridadAdmin);
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
    public void getCargo() {
        DataHelper dataHelper = new DataHelper(getContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatCargo")
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
                                                idCargo = (ja.getJSONObject(i).getInt("IdCargo"));
                                                cargo = (ja.getJSONObject(i).getString("Cargo"));
                                                dataHelper.insertCatCargo(idCargo,cargo);
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
    public void getConocimientoInfraccion() {
        DataHelper dataHelper = new DataHelper(getContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatConocimientoInfraccion")
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
                                                idConocimiento = (ja.getJSONObject(i).getInt("IdConocimiento"));
                                                conocimiento = (ja.getJSONObject(i).getString("Conocimiento"));
                                                dataHelper.insertCatConocimientoInfraccion(idConocimiento,conocimiento);
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
    public void getFiscaliaAutoridad() {
        DataHelper dataHelper = new DataHelper(getContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatFiscaliaAutoridad")
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
                                                idFiscaliaAutoridad = (ja.getJSONObject(i).getInt("IdFiscaliaAutoridad"));
                                                fiscaliaAutoridad = (ja.getJSONObject(i).getString("FiscaliaAutoridad"));
                                                dataHelper.insertCatFiscaliaAutoridad(idFiscaliaAutoridad,fiscaliaAutoridad);
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
    public void getInstitucion() {
        DataHelper dataHelper = new DataHelper(getContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatInstitucion")
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
                                                idInstitucion = (ja.getJSONObject(i).getInt("IdInstitucion"));
                                                institucion = (ja.getJSONObject(i).getString("Institucion"));
                                                dataHelper.insertCatInstitucion(idInstitucion,institucion);
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
    public void getLugarTraslado() {
        DataHelper dataHelper = new DataHelper(getContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatLugarTraslado")
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
                                                idLugarTraslado = (ja.getJSONObject(i).getInt("IdLugarTraslado"));
                                                lugarTraslado = (ja.getJSONObject(i).getString("LugarTraslado"));
                                                descripcion = (ja.getJSONObject(i).getString("Descripcion"));
                                                dataHelper.insertCatLugarTraslado(idLugarTraslado,lugarTraslado,descripcion);
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
    public void getNacionalidad() {
        DataHelper dataHelper = new DataHelper(getContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatNacionalidad")
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
                                                idNacionalida = (ja.getJSONObject(i).getInt("IdNacionalida"));
                                                nacionalida = (ja.getJSONObject(i).getString("Nacionalida"));
                                                desNacionalidad = (ja.getJSONObject(i).getString("DesNacionalidad"));
                                                dataHelper.insertCatNacionalidad(idNacionalida,nacionalida,desNacionalidad);
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
    public void getSexo() {
        DataHelper dataHelper = new DataHelper(getContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatSexo")
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
                                                idSexo = (ja.getJSONObject(i).getInt("IdSexo"));
                                                sexo = (ja.getJSONObject(i).getString("Sexo"));
                                                dataHelper.insertCatSexo(idSexo,sexo);
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
    public void getUnidad() {
        DataHelper dataHelper = new DataHelper(getContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatUnidad")
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
                                                idUnidad = (ja.getJSONObject(i).getString("IdUnidad"));
                                                unidad = (ja.getJSONObject(i).getString("Unidad"));
                                                idMarca = (ja.getJSONObject(i).getString("IdMarca"));
                                                idSubMarca = (ja.getJSONObject(i).getInt("IdSubMarca"));
                                                modelo = (ja.getJSONObject(i).getInt("Modelo"));
                                                descripcionU = (ja.getJSONObject(i).getString("Descripcion"));
                                                idInstitucionU = (ja.getJSONObject(i).getInt("IdInstitucion"));
                                                dataHelper.insertCatUnidad(idUnidad,unidad,idMarca,idSubMarca,modelo,descripcionU,idInstitucionU);
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
}