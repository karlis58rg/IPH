package mx.ssp.iph.administrativo.ui.fragmets;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.util.Log;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModeloDescripcionVehiculos_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloDetenciones_Administrativo;
import mx.ssp.iph.administrativo.viewModel.DescripcionVehiculoViewModel;
import mx.ssp.iph.R;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class DescripcionVehiculo extends Fragment {

    private DescripcionVehiculoViewModel mViewModel;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    private TextView txtObservacionesdelVehiculo;
    private ImageView imgMicrofonoObservacionesdelVehiculo;
    private EditText txthoraRetencion,txtFechaRetencion;
    private Funciones funciones;
    private RadioGroup rgTipoVehiculoAdministrativo, rgProcedenciaVehiculoAdministrativo, rgUsoVehiculoAdministrativo;
    private RadioButton rbTerrestre, rbOtro, rbNacional, rbExtranjero, rbParticular, rbTransportePublico, rbCarga;
    Spinner spMarcaVehiculo,spSubmarcaVehiculo,txtModeloVehiculo,txtColorVehiculo;
    TextView txtOtroVehiculo,txtPlacaVehiculo,txtSerieVehiculo,txtDestinoVehiculo;
    ImageView btnGuardarVehiculo;
    ViewGroup segundoLinear, tipoVehiculoAdministrativo, otroTipoVehiculoAdministrativo, procedenciaVehiculoAdministrativo, usoVehiculoAdministrativo, novenoLinear, destinoVehiculoAdministrativo;


    SharedPreferences share;
    String cargarIdFaltaAdmin,cargarUsuario,varTipoVehiculo,varTipoOtro,varProcedencia,varUso,idMarca,idSubMarca,descripcionMarca,descripcionSubMarca,descripcionColor,descripcionAnio;
    private ListView lvVehiculos;
    ArrayList<String> ListaIdVehiculo,ListaDatosVehiculo;

    public static DescripcionVehiculo newInstance() {
        return new DescripcionVehiculo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.descripcion_vehiculo_fragment, container, false);
        funciones = new Funciones();
        txtObservacionesdelVehiculo = (TextView)root.findViewById(R.id.txtObservacionesdelVehiculo);
        txtObservacionesdelVehiculo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});
        imgMicrofonoObservacionesdelVehiculo = (ImageView) root.findViewById(R.id.imgMicrofonoObservacionesdelVehiculo);
        txthoraRetencion = (EditText)root.findViewById(R.id.txthoraRetencion);
        txtFechaRetencion = (EditText)root.findViewById(R.id.txtFechaRetencion);
        lvVehiculos = (ListView) root.findViewById(R.id.lvVehiculos);

        txtOtroVehiculo = root.findViewById(R.id.txtOtroVehiculo);
        txtOtroVehiculo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(15)});
        txtModeloVehiculo = root.findViewById(R.id.txtModeloVehiculo);
        txtColorVehiculo = root.findViewById(R.id.txtColorVehiculo);
        txtPlacaVehiculo = root.findViewById(R.id.txtPlacaVehiculo);
        txtPlacaVehiculo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(10)});
        txtSerieVehiculo = root.findViewById(R.id.txtSerieVehiculo);
        txtSerieVehiculo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        rgTipoVehiculoAdministrativo = root.findViewById(R.id.rgTipoVehiculoAdministrativo);
        rgProcedenciaVehiculoAdministrativo = root.findViewById(R.id.rgProcedenciaVehiculoAdministrativo);
        rgUsoVehiculoAdministrativo = root.findViewById(R.id.rgUsoVehiculoAdministrativo);
        rbTerrestre = root.findViewById(R.id.rbTerrestre);
        rbOtro = root.findViewById(R.id.rbOtro);
        rbNacional = root.findViewById(R.id.rbNacional);
        rbExtranjero = root.findViewById(R.id.rbExtranjero);
        rbParticular = root.findViewById(R.id.rbParticular);
        rbTransportePublico = root.findViewById(R.id.rbTransportePublico);
        rbCarga = root.findViewById(R.id.rbCarga);

        spMarcaVehiculo = root.findViewById(R.id.spMarcaVehiculo);
        spSubmarcaVehiculo = root.findViewById(R.id.spSubmarcaVehiculo);



        txtDestinoVehiculo = root.findViewById(R.id.txtDestinoVehiculo);
        txtDestinoVehiculo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});
        btnGuardarVehiculo = root.findViewById(R.id.btnGuardarVehiculo);

        segundoLinear = root.findViewById(R.id.segundoLinear);
        tipoVehiculoAdministrativo = root.findViewById(R.id.tipoVehiculoAdministrativo);
        otroTipoVehiculoAdministrativo = root.findViewById(R.id.otroTipoVehiculoAdministrativo);
        procedenciaVehiculoAdministrativo = root.findViewById(R.id.procedenciaVehiculoAdministrativo);
        usoVehiculoAdministrativo = root.findViewById(R.id.usoVehiculoAdministrativo);
        novenoLinear = root.findViewById(R.id.novenoLinear);
        destinoVehiculoAdministrativo = root.findViewById(R.id.destinoVehiculoAdministrativo);

        //Cambia el título de acuerdo a la sección seleccionada
        funciones.CambiarTituloSecciones("ANEXO B: DESCRIPCIÓN DEL VEHÍCULO",getContext(),getActivity());

        cargarFolios();
        //***************** Cargar Datos si es que existen  **************************//
        CargarDatos();
        ListCombos();

        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> marca = dataHelper.getAllMarcaVehiculos();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, marca);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        spMarcaVehiculo.setAdapter(adapter);


        //HABILITAR - DESHABILITAR EDITTEXT Tipo de vehiculo
        rgTipoVehiculoAdministrativo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.rbTerrestre:
                        txtOtroVehiculo.setEnabled(false);
                        txtOtroVehiculo.setInputType(InputType.TYPE_NULL);
                        txtOtroVehiculo.setFocusableInTouchMode(false);
                        break;
                    case R.id.rbOtro:
                        txtOtroVehiculo.setEnabled(true);
                        txtOtroVehiculo.setFocusableInTouchMode(true);
                        break;
                }
            }
        });


        spMarcaVehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(), "CLICK VEHICULOS", Toast.LENGTH_LONG).show();
                descripcionMarca = (String) spMarcaVehiculo.getSelectedItem();
                String idmarca = dataHelper.getIdMarcaVehiculo(descripcionMarca);

                ArrayList<String> submarcaVehiculos = dataHelper.getValueByIdMarca(idmarca);
                ArrayAdapter<String> adapterSubMarca = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, submarcaVehiculos);
                adapterSubMarca.setDropDownViewResource(R.layout.spinner_layout);
                spSubmarcaVehiculo.setAdapter(adapterSubMarca);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        txtOtroVehiculo.setEnabled(false);
        //Imagen que funciona para activar la grabación de voz
        imgMicrofonoObservacionesdelVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgMicrofonoObservacionesdelVehiculo.setImageResource(R.drawable.ic_micro_press);
                iniciarEntradadeVoz();
            }
        });

        //FEcha
        txtFechaRetencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaRetencion,getContext(),getActivity());
            }
        });

        //FEcha
        txthoraRetencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txthoraRetencion,getContext(),getActivity());
            }
        });

        rgTipoVehiculoAdministrativo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbTerrestre) {
                    txtOtroVehiculo.setEnabled(false);
                    varTipoVehiculo = "TERRESTRE";
                    varTipoOtro = "NA";
                } else if (checkedId == R.id.rbOtro) {
                    txtOtroVehiculo.setEnabled(true);
                    varTipoVehiculo = "OTRO";
                    varTipoOtro = txtOtroVehiculo.getText().toString();
                }
            }
        });
        rgProcedenciaVehiculoAdministrativo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbNacional) {
                    varProcedencia = "NACIONAL";
                } else if (checkedId == R.id.rbExtranjero) {
                    varProcedencia = "EXTRANJERO";
                }
            }
        });

        rgUsoVehiculoAdministrativo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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



        btnGuardarVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtFechaRetencion.getText().toString().length() >= 3 && txthoraRetencion.getText().toString().length() >= 3){
                    if(rbTerrestre.isChecked() || rbOtro.isChecked()){
                        if(rbNacional.isChecked() || rbExtranjero.isChecked()){
                            if(rbParticular.isChecked() || rbTransportePublico.isChecked() || rbCarga.isChecked()){
                                if(txtDestinoVehiculo.getText().toString().length() >= 3){
                                    if(txtObservacionesdelVehiculo.getText().toString().length() >= 3){
                                        Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                                        insertDescripcionVehiculos();
                                    }else{
                                        Toast.makeText(getActivity().getApplicationContext(),"INGRESA LAS OBSERVACIONES DEL VEHÍCULO ANTES DE CONTINUAR",Toast.LENGTH_SHORT).show();
                                        novenoLinear.requestFocus();
                                    }

                                }else{
                                    Toast.makeText(getActivity().getApplicationContext(),"INGRESA EL DESTINO DEL VEHÍCULO ANTES DE CONTINUAR",Toast.LENGTH_SHORT).show();
                                    destinoVehiculoAdministrativo.requestFocus();
                                }

                            }else{
                                Toast.makeText(getActivity().getApplicationContext(),"SELECCIONA EL USO DEL VEHÍCULO ANTES DE CONTINUAR",Toast.LENGTH_SHORT).show();
                                usoVehiculoAdministrativo.requestFocus();
                            }

                        }else{
                            Toast.makeText(getActivity().getApplicationContext(),"SELECCIONA PROCEDENCIA DEL VEHÍCULO ANTES DE CONTINUAR",Toast.LENGTH_SHORT).show();
                            procedenciaVehiculoAdministrativo.requestFocus();
                        }

                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),"SELECCIONA EL TIPO DE VEHÍCULO ANTES DE CONTINUAR",Toast.LENGTH_SHORT).show();
                        tipoVehiculoAdministrativo.requestFocus();
                    }

                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"NO SE PUEDE ALMACENAR INFORMACIÓN DE VEHÍCULO SIN FECHA Y HORA DE REGISTRO",Toast.LENGTH_SHORT).show();
                    segundoLinear.requestFocus();
                }
            }
        });

        /**************************************************************************************/
        /*********************************************************************************************************/
        //Clic a la lista
        lvVehiculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext()).
                                setMessage("¿DESEA ELIMINAR EL VEHÍCULO "+ ListaIdVehiculo.get(position) + "" ).
                                setPositiveButton( "ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //CONSUME WEB SERVICE PARA ELIMINAR DB
                                        String IdDetenido = ListaIdVehiculo.get(position);
                                        EliminarVehiculo(IdDetenido);
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
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DescripcionVehiculoViewModel.class);
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
                    String textoActual = txtObservacionesdelVehiculo.getText().toString();
                    txtObservacionesdelVehiculo.setText(textoActual+" " + result.get(0));
                }
                break;
            }
        }
        imgMicrofonoObservacionesdelVehiculo.setImageResource(R.drawable.ic_micro);
    }

    public void cargarFolios(){
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdFaltaAdmin = share.getString("IDFALTAADMIN", "");
        cargarUsuario = share.getString("Usuario", "");
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarDetenidos() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/VehiculosInvolucradosAdministrativa?folioInterno="+cargarIdFaltaAdmin)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR DETENIDOS ANEXO A, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                ListaIdVehiculo = new ArrayList<String>();
                                ListaDatosVehiculo = new ArrayList<String>();

                                //***************** RESPUESTA DEL WEBSERVICE **************************//

                                //CONVERTIR ARREGLO DE JSON A OBJET JSON
                                String ArregloJson = resp.replace("[", "");
                                ArregloJson = ArregloJson.replace("]", "");

                                if(ArregloJson.equals(""))
                                {
                                    //Sin Información. Todos los campos vacíos.

                                }
                                else{
                                    //SEPARAR CADA detenido EN UN ARREGLO
                                    String[] ArrayIPHAdministrativo = ArregloJson.split(Pattern.quote("},"));

                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordeDetenido=0;
                                    while(contadordeDetenido < ArrayIPHAdministrativo.length){
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayIPHAdministrativo[contadordeDetenido] + "}");

                                            ListaIdVehiculo.add(jsonjObject.getString("IdVehiculo"));
                                            ListaDatosVehiculo.add(
                                                    "PLACA: "+
                                                    ((jsonjObject.getString("Placa")).equals("null")?" - ":jsonjObject.getString("Placa")) +
                                                    "Número de Serie: "+
                                                    " "+((jsonjObject.getString("NoSerie")).equals("null")?" - ":jsonjObject.getString("NoSerie")) +
                                                    "DESTINO:  "+
                                                    " "+ ((jsonjObject.getString("Destino")).equals("null")?" - ":jsonjObject.getString("Destino"))
                                                     );

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordeDetenido++;
                                    }
                                }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                DescripcionVehiculo.MyAdapter adapter = new DescripcionVehiculo.MyAdapter(getContext(), ListaIdVehiculo, ListaDatosVehiculo);
                                lvVehiculos.setAdapter(adapter);
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

    //***************** SE RECUPERA EL FOLIO INTERNO **************************//
    private void CargarDatos() {

        if (cargarIdFaltaAdmin.equals(""))
        {
            Toast.makeText(getContext(), "EL FOLIO INTERNO NO EXISTE. POR FAVOR REINCICIE LA APP O CREE UN NUEVO INFORME.", Toast.LENGTH_LONG).show();
        }
        else
        {
            //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
            if (funciones.ping(getContext()))
            {
                //Toast.makeText(getContext(), "cargarNoReferenciaAdministrativa()", Toast.LENGTH_LONG).show();
                cargarDetenidos();
            }
        }

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


    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertDescripcionVehiculos() {
        DataHelper dataHelper = new DataHelper(getContext());

        descripcionMarca = (String) spMarcaVehiculo.getSelectedItem();
        String idDesMarca = dataHelper.getIdMarcaVehiculo(descripcionMarca);
        String idMarca = idDesMarca;

        descripcionSubMarca = (String) spSubmarcaVehiculo.getSelectedItem();
        String  idDescSubMarca = dataHelper.getIdSubMarcaVehiculos(descripcionSubMarca);
        String idSubMarca = idDescSubMarca;

        descripcionColor = (String) txtColorVehiculo.getSelectedItem();

        descripcionAnio = (String) txtModeloVehiculo.getSelectedItem();

        ModeloDescripcionVehiculos_Administrativo modeloDescVehiculos = new ModeloDescripcionVehiculos_Administrativo
                (cargarIdFaltaAdmin, txtFechaRetencion.getText().toString(), txthoraRetencion.getText().toString(), varTipoVehiculo,
                        txtOtroVehiculo.getText().toString(), varProcedencia,idMarca,
                        idSubMarca,descripcionAnio, descripcionColor, varUso,
                        txtPlacaVehiculo.getText().toString(), txtSerieVehiculo.getText().toString(), txtObservacionesdelVehiculo.getText().toString(),
                        txtDestinoVehiculo.getText().toString(), cargarUsuario);


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin", modeloDescVehiculos.getIdFaltaAdmin())
                .add("Fecha",modeloDescVehiculos.getFecha())
                .add("Hora", modeloDescVehiculos.getHora())
                .add("Tipo", modeloDescVehiculos.getTipo())
                .add("TipoOtro", modeloDescVehiculos.getTipoOtro())
                .add("Procedencia", modeloDescVehiculos.getProcedencia())
                .add("IdMarca", modeloDescVehiculos.getIdMarca())
                .add("IdSubMarca", modeloDescVehiculos.getIdSubMarca())
                .add("Modelo", modeloDescVehiculos.getModelo())
                .add("Color", modeloDescVehiculos.getColor())
                .add("Uso",modeloDescVehiculos.getUso())
                .add("Placa", modeloDescVehiculos.getPlaca())
                .add("NoSerie", modeloDescVehiculos.getNoSerie())
                .add("Observaciones", modeloDescVehiculos.getObservaciones())
                .add("Destino", modeloDescVehiculos.getDestino())
                .add("IdPoliciaPrimerRespondiente", modeloDescVehiculos.getIdPoliciaPrimerRespondiente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/VehiculosInvolucradosAdministrativa/")
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
                    DescripcionVehiculo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                txtOtroVehiculo.setText("");
                                txtPlacaVehiculo.setText("");
                                txtSerieVehiculo.setText("");
                                txtDestinoVehiculo.setText("");
                                txtObservacionesdelVehiculo.setText("");
                                addFragment(new DescripcionVehiculo());

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
    private void EliminarVehiculo(String IdVehiculo) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/VehiculosInvolucradosAdministrativa?folioInterno="+cargarIdFaltaAdmin+"&idVehiculo="+IdVehiculo)
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ELIMINAR Vehículo, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                if(resp.equals("true"))
                                {
                                    //***************** MENSAJE MÁS ACTUALIZAR LISTA (Recargando el Fragmento xoxo) **************************//
                                    Toast.makeText(getContext(), "SE ELIMINÓ CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                    addFragment(new DescripcionVehiculo());
                                }
                                else{
                                    Toast.makeText(getContext(), "PROBLEMA AL ELIMINAR", Toast.LENGTH_SHORT).show();
                                }
                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL ELIMINAR VEHÍCULO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void ListCombos() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllColores();
        ArrayList<String> listM = dataHelper.getAllModelos();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE COLORES");
            ArrayAdapter<String> adapterColores = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            txtColorVehiculo.setAdapter(adapterColores);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON COLORES ACTIVOS", Toast.LENGTH_LONG).show();
        }
        if (listM.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MODELOS");
            ArrayAdapter<String> adapterModelo = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listM);
            txtModeloVehiculo.setAdapter(adapterModelo);
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

}