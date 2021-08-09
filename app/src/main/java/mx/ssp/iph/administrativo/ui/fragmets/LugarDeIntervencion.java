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
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModelLugarIntervencion_Administrativo;
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

import static android.content.Context.MODE_PRIVATE;

public class LugarDeIntervencion extends Fragment {

    private LugarDeIntervencionViewModel mViewModel;
    EditText txtCalleUbicacionGeograficaAdministrativo,txtNumeroExteriorUbicacionGeograficaAdministrativo,txtNumeroInteriorUbicacionGeograficaAdministrativo,txtCodigoPostalUbicacionGeograficaAdministrativo,
            txtReferenciasdelLugarUbicacionGeograficaAdministrativo,txtLatitudUbicacionGeograficaAdministrativo,txtLongitudUbicacionGeograficaAdministrativo,
            txtEntidadUbicacionGeograficaAdministrativo,txtColoniaUbicacionGeograficaAdministrativo;
    ImageView btnGuardarLugarIntervencionAdministrativo;
    SharedPreferences share;
    SharedPreferences.Editor editor;

    String cargarIdFaltaAdmin,descripcionMunicipio;
    private Funciones funciones;
    ImageView imgMap;
    final private int REQUEST_CODE_ASK_PERMISSION = 111;
    Spinner spMunicipioUbicacionGeograficaAdministrativo;
    ViewGroup sextoLinear;


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
        txtCalleUbicacionGeograficaAdministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(500)});
        txtNumeroExteriorUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtNumeroExteriorUbicacionGeograficaAdministrativo);
        txtNumeroExteriorUbicacionGeograficaAdministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});
        txtNumeroInteriorUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtNumeroInteriorUbicacionGeograficaAdministrativo);
        txtNumeroInteriorUbicacionGeograficaAdministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});
        txtCodigoPostalUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtCodigoPostalUbicacionGeograficaAdministrativo);
        txtCodigoPostalUbicacionGeograficaAdministrativo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});

        txtReferenciasdelLugarUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtReferenciasdelLugarUbicacionGeograficaAdministrativo);
        txtReferenciasdelLugarUbicacionGeograficaAdministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(500)});
        txtLatitudUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtLatitudUbicacionGeograficaAdministrativo);
        txtLatitudUbicacionGeograficaAdministrativo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        txtLongitudUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtLongitudUbicacionGeograficaAdministrativo);
        txtEntidadUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtEntidadUbicacionGeograficaAdministrativo);
        txtColoniaUbicacionGeograficaAdministrativo  = root.findViewById(R.id.txtColoniaUbicacionGeograficaAdministrativo);
        txtColoniaUbicacionGeograficaAdministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});
        spMunicipioUbicacionGeograficaAdministrativo = root.findViewById(R.id.spMunicipioUbicacionGeograficaAdministrativo);
        txtLongitudUbicacionGeograficaAdministrativo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        imgMap = (ImageView)root.findViewById(R.id.imgMap);

        sextoLinear = root.findViewById(R.id.sextoLinear);


        btnGuardarLugarIntervencionAdministrativo = root.findViewById(R.id.btnGuardarLugarIntervencionAdministrativo);
        funciones= new Funciones();

        txtEntidadUbicacionGeograficaAdministrativo.setEnabled(false);

        //Cambia el título de acuerdo a la sección seleccionada
        funciones.CambiarTituloSecciones("SECCIÓN 3: LUGAR DE LA INTERVENSIÓN",getContext(),getActivity());


        ListCombos();
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
                    CargarBANDERAMAPA("ADMINISTRATIVO");
                    ContenedorMaps dialog = new ContenedorMaps();
                    dialog.show( getActivity().getSupportFragmentManager(),"Maps");
                }
            }
        });


        //***************** Cargar Datos si es que existen  **************************//
        CargarDatos();

        btnGuardarLugarIntervencionAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtLatitudUbicacionGeograficaAdministrativo.getText().length() >= 8 && txtLongitudUbicacionGeograficaAdministrativo.getText().length() >= 8){
                    Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS ", Toast.LENGTH_LONG).show();
                    insertLugarIntervencion();
                } else if(txtColoniaUbicacionGeograficaAdministrativo.getText().length() >= 3 && txtCalleUbicacionGeograficaAdministrativo.getText().length() >= 3 && txtReferenciasdelLugarUbicacionGeograficaAdministrativo.getText().length() >= 3){
                    Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS ", Toast.LENGTH_LONG).show();
                    insertLugarIntervencion();
                }  else{
                        Toast.makeText(getContext(), "INGRESA LAS COORDENADAS COMPLETAS O LA DIRECCIÓN COMPLETA PARA GUARDAR", Toast.LENGTH_LONG).show();
                        sextoLinear.requestFocus();
                    }
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
        DataHelper dataHelper = new DataHelper(getContext());
        descripcionMunicipio = (String) spMunicipioUbicacionGeograficaAdministrativo.getSelectedItem();
        String idDescMunicipio = dataHelper.getIdMunicipio(descripcionMunicipio);
        String idMunicipio = String.valueOf(idDescMunicipio);

        if(idMunicipio.length() == 1){
            idMunicipio = "00"+idMunicipio;
        }else if(idMunicipio.length() == 2){
            idMunicipio = "0"+idMunicipio;
        }

        ModelLugarIntervencion_Administrativo modeloIntervencion= new ModelLugarIntervencion_Administrativo
                (cargarIdFaltaAdmin,"12",
                        idMunicipio,txtColoniaUbicacionGeograficaAdministrativo.getText().toString(),
                        txtCalleUbicacionGeograficaAdministrativo.getText().toString(),
                        txtNumeroExteriorUbicacionGeograficaAdministrativo.getText().toString(),
                        txtNumeroInteriorUbicacionGeograficaAdministrativo.getText().toString(),
                        txtCodigoPostalUbicacionGeograficaAdministrativo.getText().toString(),
                        txtReferenciasdelLugarUbicacionGeograficaAdministrativo.getText().toString(),
                        txtLatitudUbicacionGeograficaAdministrativo.getText().toString(),
                        txtLongitudUbicacionGeograficaAdministrativo.getText().toString());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin",cargarIdFaltaAdmin)
                .add("IdEntidadFederativa",modeloIntervencion.getIdEntidadFederativa())
                .add("IdMunicipio", modeloIntervencion.getIdMunicipio())
                .add("IdColoniaLocalidad", modeloIntervencion.getIdColoniaLocalidad())
                .add("CalleTramo", modeloIntervencion.getCalleTramo())
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
                                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, VERIFIQUE SU INFORMACIÓN", Toast.LENGTH_SHORT).show();
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

                                        txtColoniaUbicacionGeograficaAdministrativo.setText((jsonjObject.getString("IdColoniaLocalidad")).equals("null")?"":jsonjObject.getString("IdColoniaLocalidad"));
                                        //Spiner
                                        spMunicipioUbicacionGeograficaAdministrativo.setSelection(funciones.getIndexSpiner(spMunicipioUbicacionGeograficaAdministrativo, jsonjObject.getString("IdMunicipio")));

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
                        Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, VERIFIQUE SU INFORMACIÓN", Toast.LENGTH_SHORT).show();
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

    private void ListCombos() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> municipios = dataHelper.getAllMunicipios();
        if (municipios.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MUNICIPIOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, municipios);
            spMunicipioUbicacionGeograficaAdministrativo.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON MUNICIPIOS ACTIVOS", Toast.LENGTH_LONG).show();
        }
    }

    //Coloca una bandera en sharedpref para indicar si el mapa se está abriendo de administrativo Y NO DELICTIVO
    public void CargarBANDERAMAPA(String Bandera){
        share = getActivity().getSharedPreferences("main", MODE_PRIVATE);
        editor = share.edit();
        editor.putString("BANDERAMAPA", Bandera );
        editor.commit();
    }

}