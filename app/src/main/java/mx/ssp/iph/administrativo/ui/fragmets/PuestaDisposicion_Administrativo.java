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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.model.ModeloNoReferencia_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloPuestaDisposicion_Administrativo;
import mx.ssp.iph.administrativo.viewModel.PuestaDisposicionAdministrativoViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PuestaDisposicion_Administrativo extends Fragment {

    private PuestaDisposicionAdministrativoViewModel mViewModel;
    private ImageView imgFirmaAutoridadAdministrativo;
    EditText txtFechaPuestaDisposicionAdministrativo,txthoraPuestaDisposicionAdministrativo,txtNoExpedienteAdmministrativo,txtPrimerApellidoAdministrativo,txtSegundoApellidoAdministrativo,txtNombresAdministrativo,
    txtFiscaliaAutoridadAdministrativo,txtAdscripcionAdministrativo;
    TextView lblFirmaAutoridadRealizadaAdministrativo;
    Button btnGuardarPuestaDisposicioAdministrativo;
    SharedPreferences share;
    String cargarIdFaltaAdmin,cargarNumReferencia,cargarNumFolio;
    Funciones funciones;
    Spinner txtInstitucionAdscripcionAdministrativo,txtGradoCargoAdministrativo,txtCargoAdministrativo,txtUnidadDeArriboAdministrativo;
    public static PuestaDisposicion_Administrativo newInstance() {
        return new PuestaDisposicion_Administrativo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.puesta_disposicion_administrativo_fragment, container, false);

        //************************************** ACCIONES DE LA VISTA **************************************//
        funciones = new Funciones();
        cargarFolios();
        txtFechaPuestaDisposicionAdministrativo = root.findViewById(R.id.txtFechaPuestaDisposicionAdministrativo);
        txthoraPuestaDisposicionAdministrativo = root.findViewById(R.id.txthoraPuestaDisposicionAdministrativo);
        txtNoExpedienteAdmministrativo = root.findViewById(R.id.txtNoExpedienteAdmministrativo);
        txtPrimerApellidoAdministrativo = root.findViewById(R.id.txtPrimerApellidoAdministrativo);
        txtSegundoApellidoAdministrativo = root.findViewById(R.id.txtSegundoApellidoAdministrativo);
        txtNombresAdministrativo = root.findViewById(R.id.txtNombresAdministrativo);
        txtInstitucionAdscripcionAdministrativo = root.findViewById(R.id.txtInstitucionAdscripcionAdministrativo);
        txtGradoCargoAdministrativo = root.findViewById(R.id.txtGradoCargoAdministrativo);
        txtUnidadDeArriboAdministrativo = root.findViewById(R.id.txtUnidadDeArriboAdministrativo);
        txtFiscaliaAutoridadAdministrativo = root.findViewById(R.id.txtFiscaliaAutoridadAdministrativo);
        txtAdscripcionAdministrativo = root.findViewById(R.id.txtAdscripcionAdministrativo);
        txtCargoAdministrativo = root.findViewById(R.id.txtCargoAdministrativo);
        lblFirmaAutoridadRealizadaAdministrativo  = root.findViewById(R.id.lblFirmaAutoridadRealizadaAdministrativo);
        btnGuardarPuestaDisposicioAdministrativo = root.findViewById(R.id.btnGuardarPuestaDisposicioAdministrativo);

        imgFirmaAutoridadAdministrativo = (ImageView) root.findViewById(R.id.imgFirmaAutoridadAdministrativo);
        imgFirmaAutoridadAdministrativo = (ImageView) root.findViewById(R.id.imgFirmaAutoridadAdministrativo);

        //***************** Cargar Datos si es que existen  **************************//
        CargarDatos();

        imgFirmaAutoridadAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirma dialog = new ContenedorFirma(R.id.lblFirmaAutoridadRealizadaAdministrativo,R.id.lblFirmaOcultaAutoridadBase64);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });

        txtFechaPuestaDisposicionAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaPuestaDisposicionAdministrativo,getContext(),getActivity());
            }
        });
        txthoraPuestaDisposicionAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txthoraPuestaDisposicionAdministrativo,getContext(),getActivity());
            }
        });
        imgFirmaAutoridadAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirma dialog = new ContenedorFirma(R.id.lblFirmaAutoridadRealizadaAdministrativo,R.id.lblFirmaOcultaAutoridadBase64);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });
        btnGuardarPuestaDisposicioAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS ", Toast.LENGTH_LONG).show();
                insertPuestaDisposicion();
            }
        });


        //****************************************************************************//
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PuestaDisposicionAdministrativoViewModel.class);
        // TODO: Use the ViewModel
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertPuestaDisposicion() {
        ModeloPuestaDisposicion_Administrativo puestaDisposicion = new ModeloPuestaDisposicion_Administrativo
                (cargarIdFaltaAdmin,cargarNumReferencia,cargarNumFolio,
                        txtFechaPuestaDisposicionAdministrativo.getText().toString(),
                        txthoraPuestaDisposicionAdministrativo.getText().toString(),
                        txtNoExpedienteAdmministrativo.getText().toString());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin", puestaDisposicion.getIdFaltaAdmin())
                .add("NumReferencia", puestaDisposicion.getNumReferencia())
                .add("NumFolio", puestaDisposicion.getNumFolio())
                .add("Fecha", puestaDisposicion.getFecha())
                .add("Hora", puestaDisposicion.getHora())
                .add("NumExpediente", puestaDisposicion.getNumExpediente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/FaltaAdministrativa/")
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
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    PuestaDisposicion_Administrativo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                txtFechaPuestaDisposicionAdministrativo.setText("");
                                txthoraPuestaDisposicionAdministrativo.setText("");
                                txtNoExpedienteAdmministrativo.setText("");
                                txtPrimerApellidoAdministrativo.setText("");
                                txtSegundoApellidoAdministrativo.setText("");
                                txtNombresAdministrativo.setText("");
                                txtFiscaliaAutoridadAdministrativo.setText("");
                                txtAdscripcionAdministrativo.setText("");

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

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarPuestaDisposicion() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/FaltaAdministrativa?folioInterno="+cargarIdFaltaAdmin)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR SECCIÓN 1, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                    //Sin Información. Todos los campos vacíos.
                                }
                                else{
                                    //Deserializa el json y colcoa el dato correspondiente en cada campo si existe
                                    try {
                                        JSONObject jsonjObject = new JSONObject(ArregloJson);

                                        String[] Fecha = (jsonjObject.getString("Fecha").replace("-","/")).split("T");
                                        txtFechaPuestaDisposicionAdministrativo.setText((jsonjObject.getString("Fecha")).equals("null")?"":Fecha[0]);
                                        txthoraPuestaDisposicionAdministrativo.setText((jsonjObject.getString("Hora")).equals("null")?"":jsonjObject.getString("Hora"));
                                        txtNoExpedienteAdmministrativo.setText((jsonjObject.getString("NumExpediente")).equals("null")?"":jsonjObject.getString("NumExpediente"));

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

    private void cargarPuestaDisposicionFiscalia() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/RecibeDisposicionAdministrativa?folioInterno="+cargarIdFaltaAdmin)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR SECCIÓN 1, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                    //Sin Información. Todos los campos vacíos.

                                }
                                else{
                                    //Deserializa el json y colcoa el dato correspondiente en cada campo si existe
                                    try {
                                        JSONObject jsonjObject = new JSONObject(ArregloJson);

                                        txtPrimerApellidoAdministrativo.setText((jsonjObject.getString("APRecibePuestaDisp")).equals("null")?"":jsonjObject.getString("APRecibePuestaDisp"));
                                        txtSegundoApellidoAdministrativo.setText((jsonjObject.getString("AMRecibePuestaDisp")).equals("null")?"":jsonjObject.getString("AMRecibePuestaDisp"));
                                        txtNombresAdministrativo.setText((jsonjObject.getString("NomRecibePuestaDisp")).equals("null")?"":jsonjObject.getString("NomRecibePuestaDisp"));
                                        lblFirmaAutoridadRealizadaAdministrativo.setText((jsonjObject.getString("UrlFirma")).equals("null")?"":"FIRMA CORRECTA");



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

    public void cargarFolios(){
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdFaltaAdmin = share.getString("IDFALTAADMIN", "");
        cargarNumReferencia = share.getString("NOREFERENCIA", "");
        cargarNumFolio = share.getString("NUMFOLIO", "");
        System.out.println(cargarIdFaltaAdmin+cargarNumReferencia+cargarNumFolio);
    }

    //***************** SE RECUPERA EL FOLIO INTERNO **************************//
    private void CargarDatos() {
        if (cargarIdFaltaAdmin.equals(""))
        {
            Toast.makeText(getContext(), "EL FOLIO INTERNO NO EXISTE. POR FAVOR REINCICIE LA APP CREE UN NUEVO INFORME.", Toast.LENGTH_LONG).show();
        }
        else
        {
            //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
            if (funciones.ping(getContext()))
            {
                cargarPuestaDisposicion();
                cargarPuestaDisposicionFiscalia();
            }
        }

    }

}