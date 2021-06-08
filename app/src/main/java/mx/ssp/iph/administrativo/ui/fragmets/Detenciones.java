package mx.ssp.iph.administrativo.ui.fragmets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import mx.ssp.iph.administrativo.model.ModeloDetenciones_Administrativo;
import mx.ssp.iph.administrativo.viewModel.DetencionesViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class Detenciones extends Fragment  {

    private DetencionesViewModel mViewModel;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    private TextView txtDescripciondelDetenido;
    ImageView img_microfonoDescripcionDetenido,imgFirmaAutoridadAdministrativo;
    EditText txtFechaDetenido,txthoraDetencion,txtFechaNacimientoDetenido,txtPrimerApellidoDetenido,txtSegundoApellidoDetenido,txtNombresDetenido,txtApodoDetenido,txtEntidadDetenido,
            txtColoniaDetenido,txtCalleDetenido,txtNumeroExteriorDetenido,txtNumeroInteriorDetenido,txtCodigoPostalDetenido,txtReferenciasdelLugarDetenido,txtCualGrupoVulnerable,txtCualPadecimiento, txtNacionalidadEspecifiqueDetenido;
    CheckBox chNoAplicaAliasDetenido;
    Spinner spGeneroDetenido,txtNacionalidadDetenido,txtMunicipioDetenido,spLugarTrasladoPersonaDetenida;
    RadioGroup rgLesiones,rgPadecimiento,rgGrupoVulnerable;
    Button btnGuardarPuestaDisposicioAdministrativo;
    private Funciones funciones;
    SharedPreferences share;
    private ListView lvDetenidos;
    ArrayList<String> ListaIdDetenido,ListaNombreDetenido;
    ArrayList<JSONObject> ListaDetenidos;


    String cargarIdFaltaAdmin,cargarUsuario,descripcionLugarTraslado,descripcionMunicipio,descripcionNacionalidad,descripcionSexo,
            varLesiones = "NO",varPadecimiento = "NO",varGrupoVulnerable = "NO",varNoAlias;

    String [] NacionalidadItems = {"Mexicano","Otra"};

    public static Detenciones newInstance() {
        return new Detenciones();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        View view  = inflater.inflate(R.layout.detenciones_fragment, container, false);
        /********************************************************************************************/
        funciones = new Funciones();
        lvDetenidos = (ListView) view.findViewById(R.id.lvDetenidos);

        txtDescripciondelDetenido = (TextView)view.findViewById(R.id.txtDescripciondelDetenido);
        txtDescripciondelDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});

        img_microfonoDescripcionDetenido = (ImageView) view.findViewById(R.id.img_microfonoDescripcionDetenido);
        txtFechaDetenido = (EditText)view.findViewById(R.id.txtFechaDetenido);
        txthoraDetencion = (EditText)view.findViewById(R.id.txthoraDetencion);
        txtFechaNacimientoDetenido = (EditText) view.findViewById(R.id.txtFechaNacimientoDetenido);

        txtPrimerApellidoDetenido = view.findViewById(R.id.txtPrimerApellidoDetenido);
        txtPrimerApellidoDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});


        txtSegundoApellidoDetenido = view.findViewById(R.id.txtSegundoApellidoDetenido);
        txtSegundoApellidoDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtNombresDetenido = view.findViewById(R.id.txtNombresDetenido);
        txtNombresDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtApodoDetenido = view.findViewById(R.id.txtApodoDetenido);
        txtApodoDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtEntidadDetenido = view.findViewById(R.id.txtEntidadDetenido);

        txtColoniaDetenido = view.findViewById(R.id.txtColoniaDetenido);
        txtColoniaDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtCalleDetenido = view.findViewById(R.id.txtCalleDetenido);
        txtCalleDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtNumeroExteriorDetenido = view.findViewById(R.id.txtNumeroExteriorDetenido);
        txtNumeroExteriorDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});
        txtNumeroInteriorDetenido = view.findViewById(R.id.txtNumeroInteriorDetenido);
        txtNumeroInteriorDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});
        txtCodigoPostalDetenido = view.findViewById(R.id.txtCodigoPostalDetenido);
        txtCodigoPostalDetenido.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        txtReferenciasdelLugarDetenido = view.findViewById(R.id.txtReferenciasdelLugarDetenido);
        txtReferenciasdelLugarDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});
        txtCualGrupoVulnerable = view.findViewById(R.id.txtCualGrupoVulnerable);
        txtCualGrupoVulnerable.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});
        txtCualPadecimiento = view.findViewById(R.id.txtCualPadecimiento);
        txtCualPadecimiento.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});
        chNoAplicaAliasDetenido = view.findViewById(R.id.chNoAplicaAliasDetenido);
        spGeneroDetenido = view.findViewById(R.id.spGeneroDetenido);
        txtNacionalidadDetenido = view.findViewById(R.id.txtNacionalidadDetenido);


        txtMunicipioDetenido = view.findViewById(R.id.txtMunicipioDetenido);
        spLugarTrasladoPersonaDetenida = view.findViewById(R.id.spLugarTrasladoPersonaDetenida);
        rgLesiones = view.findViewById(R.id.rgLesiones);
        rgPadecimiento = view.findViewById(R.id.rgPadecimiento);
        rgGrupoVulnerable = view.findViewById(R.id.rgGrupoVulnerable);
        btnGuardarPuestaDisposicioAdministrativo = view.findViewById(R.id.btnGuardarPuestaDisposicioAdministrativo);
        ListLugarTraslado();
        ListMunicipios();
        ListNacionalidad();
        ListSexo();

        txtCualPadecimiento.setEnabled(false);
        txtCualGrupoVulnerable.setEnabled(false);
        txtEntidadDetenido.setEnabled(false);

        cargarFolios();
        //***************** Cargar Datos si es que existen  **************************//
        CargarDatos();


        //Fecha
        txtFechaNacimientoDetenido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaNacimientoDetenido,getContext(),getActivity());
            }
        });

        //Fecha
        txtFechaDetenido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"CLick",Toast.LENGTH_SHORT).show();
                funciones.calendar(R.id.txtFechaDetenido,getContext(),getActivity());
            }
        });

        //Hora
        txthoraDetencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txthoraDetencion,getContext(),getActivity());
            }
        });


        //HABILITAR - DESHABILITAR EDITTEXT ALIAS O APODO
        chNoAplicaAliasDetenido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {

                if(chselect == true){
                    txtApodoDetenido.setEnabled(false);
                    varNoAlias = "NA";
                } else if(chselect == false) {
                    txtApodoDetenido.setEnabled(true);
                    varNoAlias = "";
                }

            }
        });


        //Firma del Detenido
        imgFirmaAutoridadAdministrativo = (ImageView) view.findViewById(R.id.imgFirmaAutoridadAdministrativo);

        //Imagen que funciona para activar la grabación de voz
        img_microfonoDescripcionDetenido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarEntradadeVoz();
            }
        });

        txtDescripciondelDetenido.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    return true;
                }
                return false;
            }
        });

        //Imagen que funciona para activar la firma
        imgFirmaAutoridadAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirma dialog = new ContenedorFirma(R.id.lblFirmadelDetenido,R.id.lblFirmaOcultaDetenidoBase64);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });

        btnGuardarPuestaDisposicioAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtFechaDetenido.getText().toString().isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"INGRESA LA FECHA DE LA DETENCIÓN",Toast.LENGTH_SHORT).show();
                }else if(txthoraDetencion.getText().toString().isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"INGRESA LA HORA DE LA DETENCIÓN",Toast.LENGTH_SHORT).show();
                }else if(txtDescripciondelDetenido.getText().toString().isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"INGRESA DESCRIPCIÓN DEL DETENIDO",Toast.LENGTH_SHORT).show();
                }else if(txtDescripciondelDetenido.getText().length() < 3){
                    Toast.makeText(getActivity().getApplicationContext(),"AGREGAR EN DESCRIPCIÓN DEL DETENIDO AL MENOS 3 CARACTERES",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                    insertDetenciones();

                /*if (validarEditText(txtApodoDetenido)){
                    Toast.makeText(getActivity().getApplicationContext(),"DATOS INSERTADOS",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS ", Toast.LENGTH_LONG).show();
                    insertDetenciones();
                }

                 */


                }
            }
        });

        rgLesiones.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiLesiones) {
                    varLesiones = "SI";
                } else if (checkedId == R.id.rbNoLesiones) {
                    varLesiones = "NO";
                }

            }
        });
        rgPadecimiento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiPadecimiento) {
                    txtCualPadecimiento.setEnabled(true);
                    varPadecimiento = "SI";
                    txtCualPadecimiento.setText("");
                } else if (checkedId == R.id.rbPadecimiento) {
                    varPadecimiento = "NO";
                    txtCualPadecimiento.setText("NA");
                    txtCualPadecimiento.setEnabled(false);
                }

            }
        });
        rgGrupoVulnerable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiGrupoVulnerable) {
                    txtCualGrupoVulnerable.setEnabled(true);
                    varGrupoVulnerable = "SI";
                    txtCualGrupoVulnerable.setText("");
                } else if (checkedId == R.id.rbNoGrupoVulnerable) {
                    varGrupoVulnerable = "NO";
                    txtCualGrupoVulnerable.setText("NA");
                    txtCualGrupoVulnerable.setEnabled(false);
                }

            }
        });
        /*********************************************************************************************************/

        return view;
    }



    //VALIDAR CAMPOS VACIOS O MENOR A TRES CARACTERES
    public boolean validarEditText(EditText editText){
        if(editText.getText().toString().isEmpty()){
            Toast.makeText(getActivity().getApplicationContext(),"EL CAMPO" + editText.getId() + "ES OBLIGATORIO",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(editText.getText().length() < 3) {
            Toast.makeText(getActivity().getApplicationContext(),"EL CAMPO" + editText + "NO DEBE SER MENOR A TRES CARACTERES",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetencionesViewModel.class);
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
                    String textoActual = txtDescripciondelDetenido.getText().toString();
                    txtDescripciondelDetenido.setText(textoActual+" " + result.get(0));
                }
                break;
            }
        }
    }



    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarDetenidos() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/DetencionesAdministrativa?folioInterno="+cargarIdFaltaAdmin)
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
                                ListaIdDetenido = new ArrayList<String>();
                                ListaNombreDetenido = new ArrayList<String>();

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

                                            //ListaDetenidos.add(jsonjObject);
                                            ListaIdDetenido.add(jsonjObject.getString("NumDetencion"));
                                            ListaNombreDetenido.add(((jsonjObject.getString("APDetenido")).equals("null")?"":jsonjObject.getString("APDetenido")) +
                                                    " "+((jsonjObject.getString("AMDetenido")).equals("null")?"":jsonjObject.getString("AMDetenido")) +
                                                    " "+ ((jsonjObject.getString("NomDetenido")).equals("null")?"":jsonjObject.getString("NomDetenido")) +
                                                    " (" +((jsonjObject.getString("ApodoAlias")).equals("null")?"":jsonjObject.getString("ApodoAlias"))+" )" );

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordeDetenido++;
                                    }
                                }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                Detenciones.MyAdapter adapter = new Detenciones.MyAdapter(getContext(), ListaIdDetenido, ListaNombreDetenido);
                                lvDetenidos.setAdapter(adapter);
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
            Toast.makeText(getContext(), "EL FOLIO INTERNO NO EXISTE. POR FAVOR REINCICIE LA APP CREE UN NUEVO INFORME.", Toast.LENGTH_LONG).show();
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
        ArrayList<String> ListaIdDetenido,ListaNombreDetenido;

        MyAdapter (Context c, ArrayList<String> ListaIdDetenido, ArrayList<String> ListaNombreDetenido) {
            super(c, R.layout.row_iph, ListaIdDetenido);
            this.context = c;
            this.ListaIdDetenido = ListaIdDetenido;
            this.ListaNombreDetenido = ListaNombreDetenido;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_detenidos, parent, false);

            TextView lblNumDetencion = row.findViewById(R.id.lblNumDetencion);
            TextView lblNombreCompleto = row.findViewById(R.id.lblNombreCompleto);

            // Asigna los valores
            lblNumDetencion.setText("DETENIDO NÚMERO "+ListaIdDetenido.get(position));
            lblNombreCompleto.setText(ListaNombreDetenido.get(position));

            return row;
        }
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertDetenciones() {
        DataHelper dataHelper = new DataHelper(getContext());

        descripcionLugarTraslado = (String) spLugarTrasladoPersonaDetenida.getSelectedItem();
        int idDesLugarTraslado = dataHelper.getIdLugarTraslado(descripcionLugarTraslado);
        String idLugarTraslado = String.valueOf(idDesLugarTraslado);

        descripcionMunicipio = (String) txtMunicipioDetenido.getSelectedItem();
        int idDescMunicipio = dataHelper.getIdMunicipio(descripcionMunicipio);
        String idMunicipio = String.valueOf(idDescMunicipio);

        descripcionNacionalidad = (String) txtNacionalidadDetenido.getSelectedItem();
        int idDescNacionalidad = dataHelper.getIdNacionalidad(descripcionNacionalidad);
        String idNacionalidad = String.valueOf(idDescNacionalidad);

        descripcionSexo = (String) spGeneroDetenido.getSelectedItem();
        int idDescSexo = dataHelper.getIdSexo(descripcionSexo);
        String idSexo = String.valueOf(idDescSexo);

        if(chNoAplicaAliasDetenido.isChecked()){
            txtApodoDetenido.setText("NA");
        }

        if(txtNumeroExteriorDetenido.getText().toString().isEmpty()){
            txtNumeroExteriorDetenido.setText("SN");
        }
        if(txtNumeroInteriorDetenido.getText().toString().isEmpty()){
            txtNumeroInteriorDetenido.setText("NA");
        }


        ModeloDetenciones_Administrativo modeloDetenciones = new ModeloDetenciones_Administrativo
                (cargarIdFaltaAdmin, "1",
                        txtFechaDetenido.getText().toString(), txthoraDetencion.getText().toString(),
                        txtPrimerApellidoDetenido.getText().toString(), txtSegundoApellidoDetenido.getText().toString(),
                        txtNombresDetenido.getText().toString(),txtApodoDetenido.getText().toString(), txtDescripciondelDetenido.getText().toString(),
                        idNacionalidad, idSexo, txtFechaNacimientoDetenido.getText().toString(),
                        "SIN INFORMACION","12",idMunicipio,
                        txtColoniaDetenido.getText().toString(), txtCalleDetenido.getText().toString(),
                        txtNumeroExteriorDetenido.getText().toString(), txtNumeroInteriorDetenido.getText().toString(),
                        txtCodigoPostalDetenido.getText().toString(), txtReferenciasdelLugarDetenido.getText().toString(),
                        varLesiones, varPadecimiento, txtCualPadecimiento.getText().toString(),
                        varGrupoVulnerable, txtCualGrupoVulnerable.getText().toString(), idLugarTraslado, cargarUsuario);


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin", modeloDetenciones.getIdFaltaAdmin())
                .add("NumDetencion", modeloDetenciones.getNumDetencion())
                .add("Fecha",modeloDetenciones.getFecha())
                .add("Hora", modeloDetenciones.getHora())
                .add("APDetenido", modeloDetenciones.getAPDetenido())
                .add("AMDetenido", modeloDetenciones.getAMDetenido())
                .add("NomDetenido", modeloDetenciones.getNomDetenido())
                .add("ApodoAlias", modeloDetenciones.getApodoAlias())
                .add("DescripcionDetenido", modeloDetenciones.getDescripcionDetenido())
                .add("IdNacionalidad", modeloDetenciones.getIdNacionalidad())
                .add("IdSexo", modeloDetenciones.getIdSexo())
                .add("FechaNacimiento",modeloDetenciones.getFechaNacimiento())
                .add("UrlFirmaDetenido", modeloDetenciones.getUrlFirmaDetenido())
                .add("IdEntidadFederativa", modeloDetenciones.getIdEntidadFederativa())
                .add("IdMunicipio", modeloDetenciones.getIdMunicipio())
                .add("ColoniaLocalidad", modeloDetenciones.getColoniaLocalidad())
                .add("CalleTramo", modeloDetenciones.getCalleTramo())
                .add("NoExterior", modeloDetenciones.getNoExterior())
                .add("NoInterior", modeloDetenciones.getNoInterior())
                .add("Cp", modeloDetenciones.getCp())
                .add("Referencia",modeloDetenciones.getReferencia())
                .add("Lesiones", modeloDetenciones.getLesiones())
                .add("Padecimientos", modeloDetenciones.getPadecimientos())
                .add("DescPadecimientos", modeloDetenciones.getDescPadecimientos())
                .add("GrupoVulnerable", modeloDetenciones.getGrupoVulnerable())
                .add("DescGrupoVulnerable", modeloDetenciones.getDescGrupoVulnerable())
                .add("IdLugarTraslado", modeloDetenciones.getIdLugarTraslado())
                .add("IdPoliciaPrimerRespondiente", modeloDetenciones.getIdPoliciaPrimerRespondiente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/DetencionesAdministrativa/")
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
                    Detenciones.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                //guardarFolios();
                                txtFechaDetenido.setText("");
                                txthoraDetencion.setText("");
                                txtFechaNacimientoDetenido.setText("");
                                txtPrimerApellidoDetenido.setText("");
                                txtSegundoApellidoDetenido.setText("");
                                txtNombresDetenido.setText("");
                                txtApodoDetenido.setText("");
                                txtColoniaDetenido.setText("");
                                txtCalleDetenido.setText("");
                                txtNumeroExteriorDetenido.setText("");
                                txtNumeroInteriorDetenido.setText("");
                                txtCodigoPostalDetenido.setText("");
                                txtReferenciasdelLugarDetenido.setText("");
                                txtCualGrupoVulnerable.setText("");
                                txtCualPadecimiento.setText("");
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

    /***************************** SPINNER *************************************************************/
    private void ListLugarTraslado() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllLugarTraslado();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE LUGAR TRASLADO");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spLugarTrasladoPersonaDetenida.setAdapter(adapter);
        }
    }
    private void ListMunicipios() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllMunicipios();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MUNICIPIOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            txtMunicipioDetenido.setAdapter(adapter);
        }
    }
    private void ListNacionalidad() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllNacionalidad();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE NACIONALIDAD");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            txtNacionalidadDetenido.setAdapter(adapter);
        }
    }
    private void ListSexo() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllSexo();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE SEXO");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spGeneroDetenido.setAdapter(adapter);
        }
    }

    public void cargarFolios(){
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdFaltaAdmin = share.getString("IDFALTAADMIN", "");
        cargarUsuario = share.getString("Usuario", "");
    }

}