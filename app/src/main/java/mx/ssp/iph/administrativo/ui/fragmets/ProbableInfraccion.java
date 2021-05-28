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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.model.ModeloProbableInfraccion_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloPuestaDisposicion_Administrativo;
import mx.ssp.iph.administrativo.viewModel.ProbableInfraccionViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProbableInfraccion extends Fragment {

    private ProbableInfraccionViewModel mViewModel;
    EditText txtOtroProbableInfraccionAdministrativo,txt911FolioProbableInfraccionAdministrativo;
    Button btnGuardarProbableInfraccionAdministrativo;
    SharedPreferences share;
    String cargarIdFaltaAdmin,cargarNumReferencia;
    Funciones funciones;

    public static ProbableInfraccion newInstance() {
        return new ProbableInfraccion();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.probable_infraccion_fragment, container, false);
        //************************************** ACCIONES DE LA VISTA **************************************//
        cargarFolios();
        txtOtroProbableInfraccionAdministrativo = root.findViewById(R.id.txtOtroProbableInfraccionAdministrativo);
        txt911FolioProbableInfraccionAdministrativo = root.findViewById(R.id.txt911FolioProbableInfraccionAdministrativo);
        btnGuardarProbableInfraccionAdministrativo = root.findViewById(R.id.btnGuardarProbableInfraccionAdministrativo);

        funciones= new Funciones();
        //***************** Cargar Datos si es que existen  **************************//
        CargarDatos();

        btnGuardarProbableInfraccionAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS ", Toast.LENGTH_LONG).show();
                updateProbableInfraccion();
            }
        });

        //***************************************************************************//


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProbableInfraccionViewModel.class);
        // TODO: Use the ViewModel
    }

    private void updateProbableInfraccion(){
        ModeloProbableInfraccion_Administrativo probableInfraccion = new ModeloProbableInfraccion_Administrativo
                (  txt911FolioProbableInfraccionAdministrativo.getText().toString(),txtOtroProbableInfraccionAdministrativo.getText().toString());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin", cargarIdFaltaAdmin)
                .add("NumReferencia", cargarNumReferencia)
                .add("Telefono911", probableInfraccion.getTelefono911())
                .add("Otro", probableInfraccion.getOtro())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/FaltaAdministrativa/")
                .put(body)
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
                    ProbableInfraccion.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                txtOtroProbableInfraccionAdministrativo.setText("");
                                txt911FolioProbableInfraccionAdministrativo.setText("");
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
    private void cargarProbableInfraccion() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/FaltaAdministrativa?folioInterno=="+cargarIdFaltaAdmin)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR SECCIÓN 3, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                }
                                else{
                                    //Deserializa el json y colcoa el dato correspondiente en cada campo si existe
                                    try {
                                        JSONObject jsonjObject = new JSONObject(ArregloJson);

                                        txtOtroProbableInfraccionAdministrativo.setText((jsonjObject.getString("Telefono911")).equals("null")?"":jsonjObject.getString("Telefono911"));
                                        txt911FolioProbableInfraccionAdministrativo.setText((jsonjObject.getString("Otro")).equals("null")?"":jsonjObject.getString("Otro"));

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
    }

    //***************** SE RECUPERA EL FOLIO INTERNO **************************//
    private void CargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        if (cargarIdFaltaAdmin.equals(""))
        {
            Toast.makeText(getContext(), "EL FOLIO INTERNO NO EXISTE. POR FAVOR REINCICIE LA APP CREE UN NUEVO INFORME.", Toast.LENGTH_LONG).show();
        }
        else
        {
            //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
            if (funciones.ping(getContext()))
            {
                //Toast.makeText(getContext(), "cargarNoReferenciaAdministrativa()", Toast.LENGTH_LONG).show();
                cargarProbableInfraccion();
            }
        }

    }

}