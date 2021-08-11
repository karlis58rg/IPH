package mx.ssp.iph.delictivo.ui.fragmets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModeloDescripcionVehiculos_Administrativo;
import mx.ssp.iph.administrativo.ui.fragmets.DescripcionVehiculo;
import mx.ssp.iph.administrativo.viewModel.DescripcionVehiculoViewModel;
import mx.ssp.iph.delictivo.viewModel.DescripcionVehiculoDelictivoViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL;

public class DescripcionVehiculoDelictivo extends Fragment {

    private DescripcionVehiculoDelictivoViewModel mViewModel;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    private TextView txtObservacionesdelVehiculoDelictivo;
    private ImageView imgMicrofonoObservacionesdelVehiculoDelictivo;
    private EditText txthoraRetencionDelictivo,txtFechaRetencionDelictivo;
    private Funciones funciones;
    RadioGroup rgTipoVehiculoDelictivo,rgProcedenciaVehiculoDelictivo,rgUsoVehiculoDelictivo,rgObjetos,rgSituacionVehiculoDelictivo;
    RadioButton rbTerrestreDelictivo,rbAcuaticoDelictivo,rbAereoDelictivo, rbNacionalDelictivo,rbExtranjeroDelictivo,rbParticularDelictivo,rbTransportePublicoDelictivo,rbCargaDelictivo,rbObjetosNODelictivo,rbObjetosSIDelictivo,rbConReporteRoboDelictivo,rbSinReporteRoboDelictivo,rbNoSePuedeSaberReporteRoboDelictivo;
    Spinner spMarcaVehiculoDelictivo,spSubmarcaVehiculoDelictivo,txtModeloVehiculoDelictivo,txtColorVehiculoDelictivo;
    TextView txtPlacaVehiculoDelictivo,txtSerieVehiculoDelictivo,txtDestinoVehiculoDelictivo;
    Button btnGuardarVehiculoDelictivo;
    String varProcedencia,varUso,varTipoVehiculo;
    String cargarIdPoliciaPrimerRespondiente,cargarIdHechoDelictivo;
    String[] ArrayListaIPHDelictivo;
    int PosicionIPHSeleccionado= -1;


    //Actualizar
    LinearLayout LinearBotonesActualizarVehiculo,LinearAgregarVehiculo;
    ImageView btnEliminarVehiculoDelictivo,btnCancelarVehiculoDelictivo,btnEditarVehiculoDelictivo;




    SharedPreferences share;
    String cargarUsuario,idMarcaDelictivo,idSubMarcaDelictivo,descripcionMarcaDelictivo,descripcionSubMarcaDelictivo,descripcionColorDelictivo,descripcionAnioDelictivo;
    private ListView lvVehiculosDelictivo;
    ArrayList<String> ListaIdVehiculoDelictivo,ListaDatosVehiculoDelictivo;

    public static DescripcionVehiculoDelictivo newInstance() {
        return new DescripcionVehiculoDelictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.descripcion_vehiculo_delictivo_fragment, container, false);
        cargarDatos();

        funciones = new Funciones();

        LinearBotonesActualizarVehiculo = (LinearLayout ) root.findViewById(R.id.LinearBotonesActualizarVehiculo);
        LinearAgregarVehiculo = (LinearLayout ) root.findViewById(R.id.LinearAgregarVehiculo);

        btnEliminarVehiculoDelictivo = (ImageView) root.findViewById(R.id.btnEliminarVehiculoDelictivo);
        btnCancelarVehiculoDelictivo = (ImageView)root.findViewById(R.id.btnCancelarVehiculoDelictivo);
        btnEditarVehiculoDelictivo = (ImageView)root.findViewById(R.id.btnEditarVehiculoDelictivo);

        txtObservacionesdelVehiculoDelictivo = (TextView)root.findViewById(R.id.txtObservacionesdelVehiculoDelictivo);
        txtObservacionesdelVehiculoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});
        imgMicrofonoObservacionesdelVehiculoDelictivo = (ImageView) root.findViewById(R.id.imgMicrofonoObservacionesdelVehiculoDelictivo);
        txthoraRetencionDelictivo = (EditText)root.findViewById(R.id.txthoraRetencionDelictivo);
        txtFechaRetencionDelictivo = (EditText)root.findViewById(R.id.txtFechaRetencionDelictivo);
        lvVehiculosDelictivo = (ListView) root.findViewById(R.id.lvVehiculosDelictivo);

        txtModeloVehiculoDelictivo = root.findViewById(R.id.txtModeloVehiculoDelictivo);
        txtColorVehiculoDelictivo = root.findViewById(R.id.txtColorVehiculoDelictivo);
        txtPlacaVehiculoDelictivo = root.findViewById(R.id.txtPlacaVehiculoDelictivo);
        txtPlacaVehiculoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(10)});
        txtSerieVehiculoDelictivo = root.findViewById(R.id.txtSerieVehiculoDelictivo);
        txtSerieVehiculoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});
        rgTipoVehiculoDelictivo = root.findViewById(R.id.rgTipoVehiculoDelictivo);
        rgProcedenciaVehiculoDelictivo = root.findViewById(R.id.rgProcedenciaVehiculoDelictivo);
        rgSituacionVehiculoDelictivo = root.findViewById(R.id.rgSituacionVehiculoDelictivo);
        rgUsoVehiculoDelictivo = root.findViewById(R.id.rgUsoVehiculoDelictivo);
        rgObjetos = root.findViewById(R.id.rgObjetos);
        spMarcaVehiculoDelictivo = root.findViewById(R.id.spMarcaVehiculoDelictivo);
        spSubmarcaVehiculoDelictivo = root.findViewById(R.id.spSubmarcaVehiculoDelictivo);
        txtDestinoVehiculoDelictivo = root.findViewById(R.id.txtDestinoVehiculoDelictivo);
        txtDestinoVehiculoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});
        btnGuardarVehiculoDelictivo = root.findViewById(R.id.btnGuardarVehiculoDelictivo);

        rbTerrestreDelictivo = root.findViewById(R.id.rbTerrestreDelictivo);
        rbAcuaticoDelictivo = root.findViewById(R.id.rbAcuaticoDelictivo);
        rbAereoDelictivo = root.findViewById(R.id.rbAereoDelictivo);
        rbNacionalDelictivo = root.findViewById(R.id.rbNacionalDelictivo);
        rbExtranjeroDelictivo = root.findViewById(R.id.rbExtranjeroDelictivo);
        rbParticularDelictivo = root.findViewById(R.id.rbParticularDelictivo);
        rbTransportePublicoDelictivo = root.findViewById(R.id.rbTransportePublicoDelictivo);
        rbCargaDelictivo = root.findViewById(R.id.rbCargaDelictivo);
        rbObjetosNODelictivo = root.findViewById(R.id.rbObjetosNODelictivo);
        rbObjetosSIDelictivo = root.findViewById(R.id.rbObjetosSIDelictivo);
        rbConReporteRoboDelictivo = root.findViewById(R.id.rbConReporteRoboDelictivo);
        rbSinReporteRoboDelictivo = root.findViewById(R.id.rbSinReporteRoboDelictivo);
        rbNoSePuedeSaberReporteRoboDelictivo = root.findViewById(R.id.rbNoSePuedeSaberReporteRoboDelictivo);



        //Cambia el título de acuerdo a la sección seleccionada
        funciones.CambiarTituloSeccionesDelictivo("ANEXO C. INSPECCIÓN DE VEHÍCULO",getContext(),getActivity());



        //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
        if (funciones.ping(getContext()))
        {
            //Toast.makeText(getContext(), "cargarNoReferenciaAdministrativa()", Toast.LENGTH_LONG).show();
            cargarVehiculos();
        }

        //***************** Cargar Datos si es que existen  **************************//
        //CargarDatos();
        ListCombos();

        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> marca = dataHelper.getAllMarcaVehiculos();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, marca);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        spMarcaVehiculoDelictivo.setAdapter(adapter);

        spMarcaVehiculoDelictivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(), "CLICK VEHICULOS", Toast.LENGTH_LONG).show();
                descripcionMarcaDelictivo = (String) spMarcaVehiculoDelictivo.getSelectedItem();
                String idmarca = dataHelper.getIdMarcaVehiculo(descripcionMarcaDelictivo);

                ArrayList<String> submarcaVehiculos = dataHelper.getValueByIdMarca(idmarca);
                ArrayAdapter<String> adapterSubMarca = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, submarcaVehiculos);
                adapterSubMarca.setDropDownViewResource(R.layout.spinner_layout);
                spSubmarcaVehiculoDelictivo.setAdapter(adapterSubMarca);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Imagen que funciona para activar la grabación de voz
        imgMicrofonoObservacionesdelVehiculoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarEntradadeVoz();
            }
        });

        //FEcha
        txtFechaRetencionDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaRetencion,getContext(),getActivity());
            }
        });

        //HORA
        txthoraRetencionDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txthoraRetencion,getContext(),getActivity());
            }
        });

        rgTipoVehiculoDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            }
        });
        rgProcedenciaVehiculoDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbNacional) {
                    varProcedencia = "NACIONAL";
                } else if (checkedId == R.id.rbExtranjero) {
                    varProcedencia = "EXTRANJERO";
                }
            }
        });

        rgUsoVehiculoDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbParticular) {
                    varUso = "PARTICULAR";
                } else if (checkedId == R.id.rbTransportePublico) {
                    varUso = "TRANSPORTE PUBLICO";
                }else if(checkedId == R.id.rbCarga){
                    varUso = "CARGA";
                }
            }
        });



        btnGuardarVehiculoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtFechaRetencionDelictivo.getText().toString().isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"INGRESA LA FECHA DE RETENCIÓN DEL VEHÍCULO",Toast.LENGTH_SHORT).show();
                }else if(txthoraRetencionDelictivo.getText().toString().isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"INGRESA LA HORA DE RETENCIÓN DEL VEHÍCULO",Toast.LENGTH_SHORT).show();
                }else if(txtObservacionesdelVehiculoDelictivo.getText().length() < 3){
                    Toast.makeText(getActivity().getApplicationContext(),"AGREGAR EN OBSERVACIONES AL MENOS 3 CARACTERES",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                    //insertDescripcionVehiculos();
                }
            }
        });

        /**************************************************************************************/
        /*********************************************************************************************************/
        //Clic a la lista
        lvVehiculosDelictivo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PosicionIPHSeleccionado = position;
                //Oculto el botón de Editar y Coloco 2 botones.
                LinearBotonesActualizarVehiculo.setVisibility(View.VISIBLE);
                LinearAgregarVehiculo.setVisibility(View.GONE);


            //Sube la información al formulario
                try {
                    String Json = ArrayListaIPHDelictivo[position];
                    JSONObject jsonjObject = new JSONObject(Json + "}");

                    //Deserealizar y colocar los valores en los campos.
                    txtFechaRetencionDelictivo.setText(((jsonjObject.getString("Fecha")).equals("null")?"":jsonjObject.getString("Fecha")).replace("-","/").substring(0,10));
                    txthoraRetencionDelictivo.setText(((jsonjObject.getString("Hora")).equals("null")?"":jsonjObject.getString("Hora")));

                    ///////rgTipoVehiculoDelictivo
                    if ((jsonjObject.getString("Tipo")).equals("TERRESTRE"))
                    {
                        rbTerrestreDelictivo.setChecked(true);
                    }else if((jsonjObject.getString("Tipo")).equals("ACUATICO"))
                    {
                        rbAcuaticoDelictivo.setChecked(true);
                    }
                    else if((jsonjObject.getString("Tipo")).equals("AEREO"))
                    {
                        rbAereoDelictivo.setChecked(true);
                    }

                    ///////rgProcedenciaVehiculoDelictivo
                    if ((jsonjObject.getString("Procedencia")).equals("NACIONAL"))
                    {
                        rbNacionalDelictivo.setChecked(true);
                    }else if((jsonjObject.getString("Procedencia")).equals("EXTRANJERO"))
                    {
                        rbExtranjeroDelictivo.setChecked(true);
                    }

                    ///////Situacion
                    if ((jsonjObject.getString("Situacion")).equals("ROBADO"))
                    {
                        rbConReporteRoboDelictivo.setChecked(true);
                    }else if((jsonjObject.getString("Situacion")).equals("NORMAL"))
                    {
                        rbSinReporteRoboDelictivo.setChecked(true);
                    }
                    else if((jsonjObject.getString("Situacion")).equals("DESCONOCIDO"))
                    {
                        rbNoSePuedeSaberReporteRoboDelictivo.setChecked(true);
                    }


                    spMarcaVehiculoDelictivo.setSelection(funciones.getIndexSpiner(spMarcaVehiculoDelictivo, jsonjObject.getString("IdMarca")));
                    spSubmarcaVehiculoDelictivo.setSelection(funciones.getIndexSpiner(spSubmarcaVehiculoDelictivo, jsonjObject.getString("IdSubMarca")));
                    txtModeloVehiculoDelictivo.setSelection(funciones.getIndexSpiner(txtModeloVehiculoDelictivo, jsonjObject.getString("Modelo")));
                    txtColorVehiculoDelictivo.setSelection(funciones.getIndexSpiner(txtColorVehiculoDelictivo, jsonjObject.getString("Color")));

                    txtPlacaVehiculoDelictivo.setText(((jsonjObject.getString("Placa")).equals("null")?"":jsonjObject.getString("Placa")));
                    txtSerieVehiculoDelictivo.setText(((jsonjObject.getString("NoSerie")).equals("null")?"":jsonjObject.getString("NoSerie")));
                    txtDestinoVehiculoDelictivo.setText(((jsonjObject.getString("Destino")).equals("null")?"":jsonjObject.getString("Destino")));
                    txtObservacionesdelVehiculoDelictivo.setText(((jsonjObject.getString("Observaciones")).equals("null")?"":jsonjObject.getString("Observaciones")));

                    if ((jsonjObject.getString("ObjetosRelacionados")).equals("SI"))
                    {
                        rbObjetosSIDelictivo.setChecked(true);
                    }else
                        {rbObjetosNODelictivo.setChecked(true);}


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                    Log.i("VEHICULOS", e.toString());
                }


            }
        });
        //btnEliminarVehiculoDelictivo
        btnEliminarVehiculoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext()).
                                setMessage("¿DESEA ELIMINAR EL VEHÍCULO "+ ListaIdVehiculoDelictivo.get(PosicionIPHSeleccionado) + "" ).
                                setPositiveButton( "ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //CONSUME WEB SERVICE PARA ELIMINAR DB
                                        String IdDetenido = ListaIdVehiculoDelictivo.get(PosicionIPHSeleccionado);
                                        //EliminarVehiculo(IdDetenido);
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        dialog.dismiss();
                                    }
                                });

                builder.create().show();
            }
        });

        //Cancelar
        btnCancelarVehiculoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LimpiarCampos();
            }
        });



        return root;
    }

    private void LimpiarCampos(){
        txtFechaRetencionDelictivo.setText("");
        txthoraRetencionDelictivo.setText("");

        rgTipoVehiculoDelictivo.clearCheck();
        rgProcedenciaVehiculoDelictivo.clearCheck();
        rgUsoVehiculoDelictivo.clearCheck();

        spMarcaVehiculoDelictivo.setSelection(0);
        spSubmarcaVehiculoDelictivo.setSelection(0);
        txtModeloVehiculoDelictivo.setSelection(0);
        txtColorVehiculoDelictivo.setSelection(0);

        txtPlacaVehiculoDelictivo.setText("");
        txtSerieVehiculoDelictivo.setText("");
        txtDestinoVehiculoDelictivo.setText("");
        txtObservacionesdelVehiculoDelictivo.setText("");

        rgObjetos.clearCheck();
        rgSituacionVehiculoDelictivo.clearCheck();

        //Oculto el botón de Editar y Coloco 2 botones.
        LinearBotonesActualizarVehiculo.setVisibility(View.GONE);
        LinearAgregarVehiculo.setVisibility(View.VISIBLE);
        PosicionIPHSeleccionado=-1;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DescripcionVehiculoDelictivoViewModel.class);
        // TODO: Use the ViewModel
    }

    //Método que inicia el intent para de grabar la voz
    private void iniciarEntradadeVoz() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"COMIENZA A HABLAR AHORA");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
        catch (Exception e){
            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    //Almacena la Respuesta de la lectura de voz y la coloca en el Cuadro de Texto Correspondiente
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if (resultCode== RESULT_OK && null != data)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String textoActual = txtObservacionesdelVehiculoDelictivo.getText().toString();
                    txtObservacionesdelVehiculoDelictivo.setText(textoActual+" " + result.get(0));
                }
                break;
            }
        }
    }



    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//


    //***************** SE RECUPERA EL FOLIO INTERNO **************************//
    private void CargarDatos() {
            //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
            if (funciones.ping(getContext()))
            {
                //Toast.makeText(getContext(), "cargarNoReferenciaAdministrativa()", Toast.LENGTH_LONG).show();
                //cargarDetenidos();
            }
    }



    public void cargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdPoliciaPrimerRespondiente = share.getString("Usuario", "SIN INFORMACION");
        cargarIdHechoDelictivo = share.getString("IDHECHODELICTIVO", "SIN INFORMACION");
    }


    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//

    private void ListCombos() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllColores();
        ArrayList<String> listM = dataHelper.getAllModelos();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE COLORES");
            ArrayAdapter<String> adapterColores = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            txtColorVehiculoDelictivo.setAdapter(adapterColores);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON COLORES ACTIVOS", Toast.LENGTH_LONG).show();
        }
        if (listM.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MODELOS");
            ArrayAdapter<String> adapterModelo = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listM);
            txtModeloVehiculoDelictivo.setAdapter(adapterModelo);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON MODELOS ACTIVOS", Toast.LENGTH_LONG).show();
        }


    }
    //Intercambia los Fragmentos
    private void addFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContenedorJusticiacivica, fragment)
                //.addToBackStack(null) //Se quita la pila de fragments. Botón atrás
                .commit();
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarVehiculos() {
        Log.i("VEHICULOS", "INICIA CARGAR VEHÍCULOS");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDInspeccionVehiculo?folioInterno="+cargarIdHechoDelictivo)
                .build();
        Log.i("VEHICULOS", "url:"+"http://189.254.7.167/WebServiceIPH/api/HDInspeccionVehiculo?folioInterno="+cargarIdHechoDelictivo);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR ANEXO C, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
                Log.i("VEHICULOS", "onFailure");

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
                                ListaIdVehiculoDelictivo = new ArrayList<String>();
                                ListaDatosVehiculoDelictivo = new ArrayList<String>();
                                Log.i("VEHICULOS", "RESP:"+resp);

                                //***************** RESPUESTA DEL WEBSERVICE **************************//

                                //CONVERTIR ARREGLO DE JSON A OBJET JSON
                                String ArregloJson = resp.replace("[", "");
                                ArregloJson = ArregloJson.replace("]", "");

                                if(ArregloJson.equals(""))
                                {
                                    //Sin Información. Todos los campos vacíos.
                                    Log.i("VEHICULOS", "SIN INFORMACIÓN");

                                }
                                else{
                                    Log.i("VEHICULOS", "CON INFORMACIÓN:");

                                    //SEPARAR CADA detenido EN UN ARREGLO
                                    String[] ArrayIPHDelictivo = ArregloJson.split(Pattern.quote("},"));
                                    ArrayListaIPHDelictivo = ArrayIPHDelictivo;

                                    Log.i("VEHICULOS", "ArrayListaDelictivo:"+ArrayIPHDelictivo[0]);

                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordeDetenido=0;
                                    while(contadordeDetenido < ArrayListaIPHDelictivo.length){
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayListaIPHDelictivo[contadordeDetenido] + "}");

                                            ListaIdVehiculoDelictivo.add(jsonjObject.getString("IdVehiculo"));
                                            ListaDatosVehiculoDelictivo.add(
                                                    " PLACA: "+
                                                            ((jsonjObject.getString("Placa")).equals("null")?" - ":jsonjObject.getString("Placa")) +
                                                            " Número de Serie: "+
                                                            " "+((jsonjObject.getString("NoSerie")).equals("null")?" - ":jsonjObject.getString("NoSerie")) +
                                                            " DESTINO:  "+
                                                            " "+ ((jsonjObject.getString("Destino")).equals("null")?" - ":jsonjObject.getString("Destino"))
                                            );

                                        } catch (JSONException e) {
                                            Log.i("VEHICULOS", "catch:" + e.toString());

                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordeDetenido++;
                                    }
                                }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                Log.i("VEHICULOS", "ANTES DE ADAPTER:");
                                DescripcionVehiculoDelictivo.MyAdapter adapter = new DescripcionVehiculoDelictivo.MyAdapter(getContext(), ListaDatosVehiculoDelictivo,ListaDatosVehiculoDelictivo);
                                lvVehiculosDelictivo.setAdapter(adapter);
                                Log.i("VEHICULOS", "DESPUÉS DE ADAPTER:");

                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL CONSULTAR ANEXO C, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


    //***************** ADAPTADOR PARA LLENAR LISTA DE IPH ADMINISTRATIVO **************************//
    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> ListaIdVehiculo,ListaDatosVehiculo;

        MyAdapter (Context c, ArrayList<String> ListaIdVehiculo, ArrayList<String> ListaDatosVehiculo) {
            super(c, R.layout.row_iph, ListaIdVehiculo);
            this.context = c;
            this.ListaIdVehiculo = ListaIdVehiculo;
            this.ListaDatosVehiculo = ListaDatosVehiculo;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Log.i("VEHICULOS", "ADAPTER");

            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_vehiculos, parent, false);

            TextView lblNumeroVehiculo = row.findViewById(R.id.lblNumeroVehiculo);
            TextView lblDatosVehiculo = row.findViewById(R.id.lblDatosVehiculo);

            // Asigna los valores
            lblNumeroVehiculo.setText("VEHÍCULO:");
            lblDatosVehiculo.setText(ListaDatosVehiculo.get(position));

            return row;
        }
    }

}