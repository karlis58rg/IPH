package mx.ssp.iph.delictivo.ui.fragmets;

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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModelLugarIntervencion_Administrativo;
import mx.ssp.iph.administrativo.ui.fragmets.LugarDeIntervencion;
import mx.ssp.iph.delictivo.model.ModeloLugarIntervencion_Delictivo;
import mx.ssp.iph.delictivo.viewModel.LugarDeIntervencionDelictivoViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorCroquis;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.ContenedorMaps;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class LugarDeIntervencion_Delictivo extends Fragment {

    private LugarDeIntervencionDelictivoViewModel mViewModel;
    Button btnGuardarLugarIntervencionDelictivo;
    private ImageView imgMapDelictivo,imgCroquisDelictivo;
    private ImageView imgMapDelictivoMiniatura;
    final private int REQUEST_CODE_ASK_PERMISSION = 111;
    private Funciones funciones;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    TextView lblCroquisDelictivoOculto,lblCroquisDelictivo;
    EditText txtEntidadUbicacionGeograficaDelictivo,txtColoniaUbicacionGeograficaDelictivo,txtCalleUbicacionGeograficaDelictivo,txtNumeroExteriorUbicacionGeograficaDelictivo,
            txtNumeroInteriorUbicacionGeograficaDelictivo,txtCodigoPostalUbicacionGeograficaDelictivo,txtReferenciasdelLugarUbicacionGeograficaDelictivo,
            txtLatitudUbicacionGeograficaDelictivo,txtLongitudUbicacionGeograficaDelictivo,txtEspecificarRiesgoLugarIntervencion;
    Spinner spMunicipioUbicacionGeograficaDelictivo;
    RadioGroup rgInspeccionLugarIntervencion,rgObjetosRelacionadosLugarIntervencion,rgPreservoLugarIntervencion,rgPriorizacionLugarIntervencion,rgRiesgoPresentadoLugarIntervencion;
    String descripcionMunicipio,cargarIdPoliciaPrimerRespondiente,cargarIdHechoDelictivo,rutaCroquis,
            varInspeccionLugarIntervencion,varObjetosRelacionadosLugarIntervencion,
            varPreservoLugarIntervencion,varPriorizacionLugarIntervencion,
            varRiesgoSocialesLugarIntervencion;
    int numberRandom,randomUrlImagen;
    RadioButton rbSiInspeccionLugarIntervencion,rbNoInspeccionLugarIntervencion,rbNoObjetosRelacionadosLugarIntervencion,rbSiObjetosRelacionadosLugarIntervencion,rbNoPreservoLugarIntervencion;
    RadioButton rbNoPriorizacionLugarIntervencion,rbSiPriorizacionLugarIntervencion,rbRiesgoSocialesLugarIntervencion,rbRiesgoNaturalesLugarIntervencion,rbSiPreservoLugarIntervencion;
    String firmaURLServer = "http://189.254.7.167/WebServiceIPH/Firma/SINFIRMA.jpg";


    ViewGroup segundoLinear, principalLinear, lyEspecifiqueDelictivoLI, quinceavoLinear, quinceavoLinearDos, quinceavoLinearTres, quinceavoLinearCuatro, quinceavoLinearCinco, lyTipoRiesgoLI;

    public static LugarDeIntervencion_Delictivo newInstance() {
        return new LugarDeIntervencion_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lugar_de_intervencion_delictivo_fragment, container, false);
        /*******************************************************************************/
        cargarDatos();
        random();
        txtEntidadUbicacionGeograficaDelictivo = view.findViewById(R.id.txtEntidadUbicacionGeograficaDelictivo);
        txtColoniaUbicacionGeograficaDelictivo = view.findViewById(R.id.txtColoniaUbicacionGeograficaDelictivo);
        txtCalleUbicacionGeograficaDelictivo = view.findViewById(R.id.txtCalleUbicacionGeograficaDelictivo);
        txtNumeroExteriorUbicacionGeograficaDelictivo = view.findViewById(R.id.txtNumeroExteriorUbicacionGeograficaDelictivo);
        txtNumeroInteriorUbicacionGeograficaDelictivo = view.findViewById(R.id.txtNumeroInteriorUbicacionGeograficaDelictivo);
        txtCodigoPostalUbicacionGeograficaDelictivo = view.findViewById(R.id.txtCodigoPostalUbicacionGeograficaDelictivo);
        txtReferenciasdelLugarUbicacionGeograficaDelictivo = view.findViewById(R.id.txtReferenciasdelLugarUbicacionGeograficaDelictivo);
        txtLatitudUbicacionGeograficaDelictivo = view.findViewById(R.id.txtLatitudUbicacionGeograficaDelictivo);
        txtLongitudUbicacionGeograficaDelictivo = view.findViewById(R.id.txtLongitudUbicacionGeograficaDelictivo);
        txtEspecificarRiesgoLugarIntervencion = view.findViewById(R.id.txtEspecificarRiesgoLugarIntervencion);
        spMunicipioUbicacionGeograficaDelictivo = view.findViewById(R.id.spMunicipioUbicacionGeograficaDelictivo);
        rgInspeccionLugarIntervencion = view.findViewById(R.id.rgInspeccionLugarIntervencion);
        rgObjetosRelacionadosLugarIntervencion = view.findViewById(R.id.rgObjetosRelacionadosLugarIntervencion);
        rgPreservoLugarIntervencion = view.findViewById(R.id.rgPreservoLugarIntervencion);
        rgPriorizacionLugarIntervencion = view.findViewById(R.id.rgPriorizacionLugarIntervencion);
        rgRiesgoPresentadoLugarIntervencion = view.findViewById(R.id.rgRiesgoPresentadoLugarIntervencion);
        btnGuardarLugarIntervencionDelictivo = view.findViewById(R.id.btnGuardarLugarIntervencionDelictivo);
        imgMapDelictivo = view.findViewById(R.id.imgMapDelictivo);
        imgCroquisDelictivo = view.findViewById(R.id.imgCroquisDelictivo);

        lblCroquisDelictivo = view.findViewById(R.id.lblCroquisDelictivo);
        lblCroquisDelictivoOculto = view.findViewById(R.id.lblCroquisDelictivoOculto);
        imgMapDelictivoMiniatura = view.findViewById(R.id.imgMapDelictivoMiniatura);

        rbSiInspeccionLugarIntervencion = view.findViewById(R.id.rbSiInspeccionLugarIntervencion);
        rbNoInspeccionLugarIntervencion = view.findViewById(R.id.rbNoInspeccionLugarIntervencion);
        rbNoObjetosRelacionadosLugarIntervencion = view.findViewById(R.id.rbNoObjetosRelacionadosLugarIntervencion);
        rbSiObjetosRelacionadosLugarIntervencion = view.findViewById(R.id.rbSiObjetosRelacionadosLugarIntervencion);
        rbNoPreservoLugarIntervencion = view.findViewById(R.id.rbNoPreservoLugarIntervencion);
        rbNoPriorizacionLugarIntervencion = view.findViewById(R.id.rbNoPriorizacionLugarIntervencion);
        rbSiPriorizacionLugarIntervencion = view.findViewById(R.id.rbSiPriorizacionLugarIntervencion);
        rbRiesgoSocialesLugarIntervencion = view.findViewById(R.id.rbRiesgoSocialesLugarIntervencion);
        rbRiesgoNaturalesLugarIntervencion = view.findViewById(R.id.rbRiesgoNaturalesLugarIntervencion);
        rbSiPreservoLugarIntervencion = view.findViewById(R.id.rbSiPreservoLugarIntervencion);
        principalLinear = view.findViewById(R.id.principalLinear);
        lyEspecifiqueDelictivoLI = view.findViewById(R.id.lyEspecifiqueDelictivoLI);
        quinceavoLinear = view.findViewById(R.id.quinceavoLinear);
        quinceavoLinearDos = view.findViewById(R.id.quinceavoLinearDos);
        quinceavoLinearTres = view.findViewById(R.id.quinceavoLinearTres);
        quinceavoLinearCuatro = view.findViewById(R.id.quinceavoLinearCuatro);
        quinceavoLinearCinco = view.findViewById(R.id.quinceavoLinearCinco);
        lyTipoRiesgoLI = view.findViewById(R.id.lyTipoRiesgoLI);

        segundoLinear = view.findViewById(R.id.segundoLinear);

        funciones = new Funciones();
        funciones.CambiarTituloSeccionesDelictivo("SECCIÓN 4. LUGAR DE LA INTERVENCIÓN",getContext(),getActivity());

        ListCombos();

        //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
        if (funciones.ping(getContext()))
        {
            cargarLugardelaInternvecion();
        }


        //***************** BOTÓN ABRIR FRAGMENT MAPS  **************************//
        imgMapDelictivo.setOnClickListener(new View.OnClickListener() {
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
                    CargarBANDERAMAPA("DELICTIVO");
                    ContenedorMaps dialog = new ContenedorMaps();
                    dialog.show( getActivity().getSupportFragmentManager(),"Maps");
                }
            }
        });

        //***************** Croquis **************************//
        imgCroquisDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorCroquis dialog = new ContenedorCroquis(R.id.lblCroquisDelictivo,R.id.lblCroquisDelictivoOculto,R.id.imgMapDelictivoMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });

        btnGuardarLugarIntervencionDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtLatitudUbicacionGeograficaDelictivo.getText().length() >= 8 && txtLongitudUbicacionGeograficaDelictivo.getText().length() >= 8){
                    SegundaValidacion();
                } else if(txtColoniaUbicacionGeograficaDelictivo.getText().length() >= 3 && txtCalleUbicacionGeograficaDelictivo.getText().length() >= 3 && txtReferenciasdelLugarUbicacionGeograficaDelictivo.getText().length() >= 3){
                    SegundaValidacion();
                }  else{
                    Toast.makeText(getContext(), "INGRESA LAS COORDENADAS COMPLETAS O LA DIRECCIÓN COMPLETA PARA GUARDAR", Toast.LENGTH_LONG).show();
                    segundoLinear.requestFocus();
                    principalLinear.requestFocus();
                }




            }
        });

        rgInspeccionLugarIntervencion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiInspeccionLugarIntervencion) {
                    varInspeccionLugarIntervencion = "SI";
                } else if (checkedId == R.id.rbNoInspeccionLugarIntervencion) {
                    varInspeccionLugarIntervencion = "NO";
                }

            }
        });
        rgObjetosRelacionadosLugarIntervencion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiObjetosRelacionadosLugarIntervencion) {
                    varObjetosRelacionadosLugarIntervencion = "SI";
                } else if (checkedId == R.id.rbNoObjetosRelacionadosLugarIntervencion) {
                    varObjetosRelacionadosLugarIntervencion = "NO";
                }

            }
        });
        rgPreservoLugarIntervencion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiPreservoLugarIntervencion) {
                    varPreservoLugarIntervencion = "SI";
                } else if (checkedId == R.id.rbNoPreservoLugarIntervencion) {
                    varPreservoLugarIntervencion = "NO";
                }

            }
        });
        rgPriorizacionLugarIntervencion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiPriorizacionLugarIntervencion) {
                    varPriorizacionLugarIntervencion = "SI";
                } else if (checkedId == R.id.rbNoPriorizacionLugarIntervencion) {
                    varPriorizacionLugarIntervencion = "NO";
                }

            }
        });
        rgRiesgoPresentadoLugarIntervencion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbRiesgoSocialesLugarIntervencion) {
                    varRiesgoSocialesLugarIntervencion = "SOCIALES";
                } else if (checkedId == R.id.rbRiesgoNaturalesLugarIntervencion) {
                    varRiesgoSocialesLugarIntervencion = "NATURALES";
                }

            }
        });


        /********************************************************************************/
        return view;
    }


    public void SegundaValidacion(){
        if (rbNoInspeccionLugarIntervencion.isChecked() || rbSiInspeccionLugarIntervencion.isChecked()){
            if (rbNoObjetosRelacionadosLugarIntervencion.isChecked() || rbSiObjetosRelacionadosLugarIntervencion.isChecked()){
                    if (rbNoPreservoLugarIntervencion.isChecked() || rbSiPreservoLugarIntervencion.isChecked()){
                        if (rbNoPriorizacionLugarIntervencion.isChecked() || rbSiPriorizacionLugarIntervencion.isChecked()){
                            if (rbRiesgoSocialesLugarIntervencion.isChecked() || rbRiesgoNaturalesLugarIntervencion.isChecked()){
                                if(txtEspecificarRiesgoLugarIntervencion.getText().length() >= 3){
                                    Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                                    updateLugarIntervencionHD();
                                } else {
                                    Toast.makeText(getContext(), "INGRESA LA ESPECIFICACIÓN DE TIPO DE RIESGO", Toast.LENGTH_LONG).show();
                                    txtEspecificarRiesgoLugarIntervencion.requestFocus();
                                    lyEspecifiqueDelictivoLI.requestFocus();
                                    quinceavoLinearCinco.requestFocus();
                                }
                            } else {
                                Toast.makeText(getContext(), "ESPECIFICA SI HUBO UN TIPO DE RIESGO", Toast.LENGTH_LONG).show();
                                quinceavoLinearCinco.requestFocus();
                                lyEspecifiqueDelictivoLI.requestFocus();
                            }
                        } else {
                            Toast.makeText(getContext(), "ESPECIFICA SI LLEVÓ A CABO LA PRIORIZACIÓN EN EL LUGAR DE LA INTERVENCIÓN", Toast.LENGTH_LONG).show();
                            quinceavoLinearCuatro.requestFocus();
                        }
                    } else {
                        Toast.makeText(getContext(), "ESPECIFICA SI PRESERVÓ EL LUGAR DE LA INTERVENCIÓN", Toast.LENGTH_LONG).show();
                        quinceavoLinearTres.requestFocus();
                    }
            } else {
                Toast.makeText(getContext(), "ESPECIFICA SI ENCONTRARON OBJETOS RELACIONADOS CON LOS HECHOS", Toast.LENGTH_LONG).show();
                quinceavoLinearDos.requestFocus();
            }
        } else {
            Toast.makeText(getContext(), "ESPECIFICA SI REALIZÓ INSPECCIÓN DEL LUGAR", Toast.LENGTH_LONG).show();
            quinceavoLinear.requestFocus();
        }

    }


    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void updateLugarIntervencionHD() {
        DataHelper dataHelper = new DataHelper(getContext());
        descripcionMunicipio = (String) spMunicipioUbicacionGeograficaDelictivo.getSelectedItem();
        String idDescMunicipio = dataHelper.getIdMunicipio(descripcionMunicipio);
        String idMunicipio = String.valueOf(idDescMunicipio);

        if(idMunicipio.length() == 1){
            idMunicipio = "00"+idMunicipio;
        }else if(idMunicipio.length() == 2){
            idMunicipio = "0"+idMunicipio;
        }
        rutaCroquis = "http://189.254.7.167/WebServiceIPH/RutaCroquis/"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";

        ModeloLugarIntervencion_Delictivo modeloIntervencion = new ModeloLugarIntervencion_Delictivo
                (cargarIdHechoDelictivo,"12",
                        idMunicipio,txtColoniaUbicacionGeograficaDelictivo.getText().toString(),
                        txtCalleUbicacionGeograficaDelictivo.getText().toString(),
                        txtNumeroExteriorUbicacionGeograficaDelictivo.getText().toString(),
                        txtNumeroInteriorUbicacionGeograficaDelictivo.getText().toString(),
                        txtCodigoPostalUbicacionGeograficaDelictivo.getText().toString(),
                        txtReferenciasdelLugarUbicacionGeograficaDelictivo.getText().toString(),
                        txtLatitudUbicacionGeograficaDelictivo.getText().toString(),
                        txtLongitudUbicacionGeograficaDelictivo.getText().toString(),
                        rutaCroquis,varInspeccionLugarIntervencion,varObjetosRelacionadosLugarIntervencion,
                        varPreservoLugarIntervencion,varPriorizacionLugarIntervencion,varRiesgoSocialesLugarIntervencion,
                        txtEspecificarRiesgoLugarIntervencion.getText().toString());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo",modeloIntervencion.getIdHechoDelictivo())
                .add("IdEntidadFederativa",modeloIntervencion.getIdEntidadFederativa())
                .add("IdMunicipio", modeloIntervencion.getIdMunicipio())
                .add("ColoniaLocalidad", modeloIntervencion.getColoniaLocalidad())
                .add("CalleTramo", modeloIntervencion.getCalleTramo())
                .add("NoExterior", modeloIntervencion.getNoExterior())
                .add("NoInterior", modeloIntervencion.getNoInterior())
                .add("Cp", modeloIntervencion.getCp())
                .add("Referencia", modeloIntervencion.getReferencia())
                .add("Latitud", modeloIntervencion.getLatitud())
                .add("Longitud", modeloIntervencion.getLongitud())
                .add("RutaCroquis", modeloIntervencion.getRutaCroquis())
                .add("RealizoInspeccion", modeloIntervencion.getRealizoInspeccion())
                .add("AnexoObjetosRelacionados", modeloIntervencion.getAnexoObjetosRelacionados())
                .add("PreservoLugar", modeloIntervencion.getPreservoLugar())
                .add("PriorizacionIntervencion", modeloIntervencion.getPriorizacionIntervencion())
                .add("TipoRiesgoPresentado", modeloIntervencion.getTipoRiesgoPresentado())
                .add("DesTipoRiesgoPresentado", modeloIntervencion.getDesTipoRiesgoPresentado())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDLugarIntervencion/")
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
                    LugarDeIntervencion_Delictivo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                //Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                insertImagen();
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

    //********************************** INSERTA IMAGEN AL SERVIDOR ***********************************//
    public void insertImagen() {
        String cadena = lblCroquisDelictivoOculto.getText().toString();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Description", cargarIdHechoDelictivo+randomUrlImagen+".jpg")
                .add("ImageData", cadena)
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/MultimediaCroquis")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, FAVOR DE VERIFICAR SU CONEXCIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().toString();  /********** ME REGRESA LA RESPUESTA DEL WS ****************/
                    LugarDeIntervencion_Delictivo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("EL DATO DE LA IMAGEN SE ENVIO CORRECTAMENTE");
                            Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LugarDeIntervencionDelictivoViewModel.class);
        // TODO: Use the ViewModel
    }

    //Coloca una bandera en sharedpref para indicar si el mapa se está abriendo de delictivo y no administrativo
    public void CargarBANDERAMAPA(String Bandera){
        share = getActivity().getSharedPreferences("main", MODE_PRIVATE);
        editor = share.edit();
        editor.putString("BANDERAMAPA", Bandera );
        editor.commit();
    }

    private void ListCombos() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> municipios = dataHelper.getAllMunicipios();
        if (municipios.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MUNICIPIOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, municipios);
            spMunicipioUbicacionGeograficaDelictivo.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON MUNICIPIOS ACTIVOS", Toast.LENGTH_LONG).show();
        }
    }
    public void cargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdPoliciaPrimerRespondiente = share.getString("Usuario", "SIN INFORMACION");
        cargarIdHechoDelictivo = share.getString("IDHECHODELICTIVO", "SIN INFORMACION");
    }
    public void random(){
        Random random = new Random();
        numberRandom = random.nextInt(9000)*99;
        randomUrlImagen = numberRandom;
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarLugardelaInternvecion() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDLugarIntervencion?folioInterno="+cargarIdHechoDelictivo)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR DATOS SECCIÓN 4, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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

                                }
                                else{
                                    //Deserializa el json y colcoa el dato correspondiente en cada campo si existe
                                    try {
                                        JSONObject jsonjObject = new JSONObject(ArregloJson);

                                        spMunicipioUbicacionGeograficaDelictivo.setSelection(funciones.getIndexSpiner(spMunicipioUbicacionGeograficaDelictivo, jsonjObject.getString("IdMunicipio")));
                                        txtColoniaUbicacionGeograficaDelictivo.setText((jsonjObject.getString("ColoniaLocalidad")).equals("null")?"":jsonjObject.getString("ColoniaLocalidad"));
                                        txtCalleUbicacionGeograficaDelictivo.setText((jsonjObject.getString("CalleTramo")).equals("null")?"":jsonjObject.getString("CalleTramo"));
                                        txtNumeroExteriorUbicacionGeograficaDelictivo.setText((jsonjObject.getString("NoExterior")).equals("null")?"":jsonjObject.getString("NoExterior"));
                                        txtNumeroInteriorUbicacionGeograficaDelictivo.setText((jsonjObject.getString("NoInterior")).equals("null")?"":jsonjObject.getString("NoInterior"));
                                        txtCodigoPostalUbicacionGeograficaDelictivo.setText((jsonjObject.getString("Cp")).equals("null")?"":jsonjObject.getString("Cp"));
                                        txtReferenciasdelLugarUbicacionGeograficaDelictivo.setText((jsonjObject.getString("Referencia")).equals("null")?"":jsonjObject.getString("Referencia"));

                                        txtLatitudUbicacionGeograficaDelictivo.setText((jsonjObject.getString("Latitud")).equals("null")?"":jsonjObject.getString("Latitud"));
                                        txtLongitudUbicacionGeograficaDelictivo.setText((jsonjObject.getString("Longitud")).equals("null")?"":jsonjObject.getString("Longitud"));

                                        //Firma
                                        lblCroquisDelictivo.setText((jsonjObject.getString("RutaCroquis")).equals("null")?"":"CROQUIS CORRECTO");
                                        firmaURLServer = (jsonjObject.getString("RutaCroquis").equals("null")?"http://189.254.7.167/WebServiceIPH/Firma/SINFIRMA.jpg":jsonjObject.getString("RutaCroquis"));
                                        lblCroquisDelictivoOculto.setText(firmaURLServer);
                                        getFirmaFromURL();


                                        //========
                                        if((jsonjObject.getString("RealizoInspeccion")).equals("null"))
                                        {
                                        }else{
                                            if((jsonjObject.getString("RealizoInspeccion")).equals("SI"))
                                            {
                                                rbSiInspeccionLugarIntervencion.setChecked(true);
                                            }
                                            else
                                            {
                                                rbNoInspeccionLugarIntervencion.setChecked(true);
                                            }
                                        }

                                        //========
                                        if((jsonjObject.getString("AnexoObjetosRelacionados")).equals("null"))
                                        {
                                        }else{
                                            if((jsonjObject.getString("AnexoObjetosRelacionados")).equals("SI"))
                                            {
                                                rbSiObjetosRelacionadosLugarIntervencion.setChecked(true);
                                            }
                                            else
                                            {
                                                rbNoObjetosRelacionadosLugarIntervencion.setChecked(true);
                                            }
                                        }
                                        //========
                                        if((jsonjObject.getString("PreservoLugar")).equals("null"))
                                        {
                                        }else{
                                            if((jsonjObject.getString("PreservoLugar")).equals("SI"))
                                            {
                                                rbSiPreservoLugarIntervencion.setChecked(true);
                                            }
                                            else
                                            {
                                                rbNoPreservoLugarIntervencion.setChecked(true);
                                            }
                                        }
                                        //========
                                        if((jsonjObject.getString("PriorizacionIntervencion")).equals("null"))
                                        {
                                        }else{
                                            if((jsonjObject.getString("PriorizacionIntervencion")).equals("SI"))
                                            {
                                                rbSiPriorizacionLugarIntervencion.setChecked(true);
                                            }
                                            else
                                            {
                                                rbNoPriorizacionLugarIntervencion.setChecked(true);
                                            }
                                        }

                                        //========
                                        if((jsonjObject.getString("TipoRiesgoPresentado")).equals("null"))
                                        {
                                        }else{
                                            if((jsonjObject.getString("TipoRiesgoPresentado")).equals("SOCIALES"))
                                            {
                                                rbRiesgoSocialesLugarIntervencion.setChecked(true);
                                            }
                                            else
                                            {
                                                rbRiesgoNaturalesLugarIntervencion.setChecked(true);
                                            }
                                        }

                                        txtEspecificarRiesgoLugarIntervencion.setText((jsonjObject.getString("DesTipoRiesgoPresentado")).equals("null")?"":jsonjObject.getString("DesTipoRiesgoPresentado"));


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
                        Toast.makeText(getContext(), "ERROR AL SOLICITAR INFORMACIÓN SECCIÓN 4, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public void getFirmaFromURL(){
        Picasso.get()
                .load(firmaURLServer)
                .into(imgMapDelictivoMiniatura);
    }

}