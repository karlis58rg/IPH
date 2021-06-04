package mx.ssp.iph.administrativo.ui.fragmets;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.model.ModelLugarIntervencion_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloNoReferencia_Administrativo;
import mx.ssp.iph.administrativo.viewModel.LugarDeIntervencionViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.ContenedorMaps;
import mx.ssp.iph.utilidades.ui.Funciones;
import mx.ssp.iph.utilidades.ui.MapsFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LugarDeIntervencion extends Fragment {

    private LugarDeIntervencionViewModel mViewModel;
    EditText txtCalleUbicacionGeograficaAdministrativo,txtNumeroExteriorUbicacionGeograficaAdministrativo,txtNumeroInteriorUbicacionGeograficaAdministrativo,txtCodigoPostalUbicacionGeograficaAdministrativo,
            txtReferenciasdelLugarUbicacionGeograficaAdministrativo,txtLatitudUbicacionGeograficaAdministrativo,txtLongitudUbicacionGeograficaAdministrativo;
    Button btnGuardarPuestaDisposicioAdministrativo;
    SharedPreferences share;
    String cargarIdFaltaAdmin;
    private Funciones funciones;
    ImageView imgMap;
    final private int REQUEST_CODE_ASK_PERMISSION = 111;

    public static LugarDeIntervencion newInstance() {
        return new LugarDeIntervencion();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.lugar_de_intervencion_fragment, container, false);

        //************************************** ACCIONES DE LA VISTA **************************************//
        cargarFolios();
        txtCalleUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtCalleUbicacionGeograficaAdministrativo);
        txtNumeroExteriorUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtNumeroExteriorUbicacionGeograficaAdministrativo);
        txtNumeroInteriorUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtNumeroInteriorUbicacionGeograficaAdministrativo);
        txtCodigoPostalUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtCodigoPostalUbicacionGeograficaAdministrativo);
        txtReferenciasdelLugarUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtReferenciasdelLugarUbicacionGeograficaAdministrativo);
        txtLatitudUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtLatitudUbicacionGeograficaAdministrativo);
        txtLongitudUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtLongitudUbicacionGeograficaAdministrativo);
        imgMap = (ImageView)root.findViewById(R.id.imgMap);


        btnGuardarPuestaDisposicioAdministrativo = root.findViewById(R.id.btnGuardarPuestaDisposicioAdministrativo);
        funciones= new Funciones();


        //***************** Botón abrir maps  **************************//
        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permisoubicacion = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (permisoubicacion != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSION);
                        Toast.makeText(getContext(),"POR FAVOR ACTIVA LOS PERMISOS DE UBICACIÓN",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    ContenedorMaps dialog = new ContenedorMaps();
                    dialog.show( getActivity().getSupportFragmentManager(),"Maps");
                }
            }
        });


        //***************** Cargar Datos si es que existen  **************************//
        CargarDatos();

        btnGuardarPuestaDisposicioAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS ", Toast.LENGTH_LONG).show();
                insertLugarIntervencion();
            }
        });

        //***************************************************************************//
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LugarDeIntervencionViewModel.class);
        // TODO: Use the ViewModel
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertLugarIntervencion() {
        ModelLugarIntervencion_Administrativo modeloIntervencion= new ModelLugarIntervencion_Administrativo
                (txtCalleUbicacionGeograficaAdministrativo.getText().toString(),
                        txtNumeroExteriorUbicacionGeograficaAdministrativo.getText().toString(),
                        txtNumeroInteriorUbicacionGeograficaAdministrativo.getText().toString(),
                        txtCodigoPostalUbicacionGeograficaAdministrativo.getText().toString(),
                        txtReferenciasdelLugarUbicacionGeograficaAdministrativo.getText().toString(),
                        txtLatitudUbicacionGeograficaAdministrativo.getText().toString(),
                        txtLongitudUbicacionGeograficaAdministrativo.getText().toString());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin",cargarIdFaltaAdmin)
                .add("CalleTramo", modeloIntervencion.getCalleTramo())

                .add("IdLugar", "1")
                .add("IdEntidadFederativa", "1")
                .add("IdMunicipio", "1")



                .add("NoExterior", modeloIntervencion.getNoExterior())
                .add("NoInterior", modeloIntervencion.getNoInterior())
                .add("Cp", modeloIntervencion.getCp())
                .add("Referencia", modeloIntervencion.getReferencia())
                .add("Latitud", modeloIntervencion.getLatitud())
                .add("Longitud", modeloIntervencion.getLongitud())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/LugarIntervencionAdministrativa/")
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
                    LugarDeIntervencion.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                /*
                                txtCalleUbicacionGeograficaAdministrativo.setText("");
                                txtNumeroExteriorUbicacionGeograficaAdministrativo.setText("");
                                txtNumeroInteriorUbicacionGeograficaAdministrativo.setText("");
                                txtCodigoPostalUbicacionGeograficaAdministrativo.setText("");
                                txtReferenciasdelLugarUbicacionGeograficaAdministrativo.setText("");
                                txtLatitudUbicacionGeograficaAdministrativo.setText("");
                                txtLongitudUbicacionGeograficaAdministrativo.setText("");
                                 */
                            }else{
                                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("HERE", resp);
                        }
                    });
                }
            }
        });
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarLugarIntervencion() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/LugarIntervencionAdministrativa?folioInterno="+cargarIdFaltaAdmin)
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

                                        txtCalleUbicacionGeograficaAdministrativo.setText((jsonjObject.getString("CalleTramo")).equals("null")?"":jsonjObject.getString("CalleTramo"));
                                        txtNumeroExteriorUbicacionGeograficaAdministrativo.setText((jsonjObject.getString("NoExterior")).equals("null")?"":jsonjObject.getString("NoExterior"));
                                        txtNumeroInteriorUbicacionGeograficaAdministrativo.setText((jsonjObject.getString("NoInterior")).equals("null")?"":jsonjObject.getString("NoInterior"));
                                        txtCodigoPostalUbicacionGeograficaAdministrativo.setText((jsonjObject.getString("Cp")).equals("null")?"":jsonjObject.getString("Cp"));
                                        txtReferenciasdelLugarUbicacionGeograficaAdministrativo.setText((jsonjObject.getString("Referencia")).equals("null")?"":jsonjObject.getString("Referencia"));
                                        txtLatitudUbicacionGeograficaAdministrativo.setText((jsonjObject.getString("Latitud")).equals("null")?"":jsonjObject.getString("Latitud"));
                                        txtLongitudUbicacionGeograficaAdministrativo.setText((jsonjObject.getString("Longitud")).equals("null")?"":jsonjObject.getString("Longitud"));

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
                cargarLugarIntervencion();
            }
        }

    }

}