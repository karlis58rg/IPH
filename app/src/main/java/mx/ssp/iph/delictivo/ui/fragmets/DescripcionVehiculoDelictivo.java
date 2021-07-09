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

public class DescripcionVehiculoDelictivo extends Fragment {

    private DescripcionVehiculoDelictivoViewModel mViewModel;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    private TextView txtObservacionesdelVehiculoDelictivo;
    private ImageView imgMicrofonoObservacionesdelVehiculoDelictivo;
    private EditText txthoraRetencionDelictivo,txtFechaRetencionDelictivo;
    private Funciones funciones;
    RadioGroup rgTipoVehiculoDelictivo,rgProcedenciaVehiculoDelictivo,rgUsoVehiculoDelictivo;
    RadioButton rbTerrestreDelictivo,rbAcuaticoDelictivo,rbAereoDelictivo;
    Spinner spMarcaVehiculoDelictivo,spSubmarcaVehiculoDelictivo,txtModeloVehiculoDelictivo,txtColorVehiculoDelictivo;
    TextView txtPlacaVehiculoDelictivo,txtSerieVehiculoDelictivo,txtDestinoVehiculoDelictivo;
    Button btnGuardarVehiculoDelictivo;
    String varProcedencia,varUso,varTipoVehiculo;


    SharedPreferences share;
    String cargarIdFaltaAdmin ,cargarUsuario,idMarcaDelictivo,idSubMarcaDelictivo,descripcionMarcaDelictivo,descripcionSubMarcaDelictivo,descripcionColorDelictivo,descripcionAnioDelictivo;
    private ListView lvVehiculosDelictivo;
    ArrayList<String> ListaIdVehiculoDelictivo,ListaDatosVehiculoDelictivo;

    public static DescripcionVehiculoDelictivo newInstance() {
        return new DescripcionVehiculoDelictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.descripcion_vehiculo_delictivo_fragment, container, false);
        funciones = new Funciones();
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
        rgUsoVehiculoDelictivo = root.findViewById(R.id.rgUsoVehiculoDelictivo);
        spMarcaVehiculoDelictivo = root.findViewById(R.id.spMarcaVehiculoDelictivo);
        spSubmarcaVehiculoDelictivo = root.findViewById(R.id.spSubmarcaVehiculoDelictivo);
        txtDestinoVehiculoDelictivo = root.findViewById(R.id.txtDestinoVehiculoDelictivo);
        txtDestinoVehiculoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});
        btnGuardarVehiculoDelictivo = root.findViewById(R.id.btnGuardarVehiculoDelictivo);


        //Cambia el título de acuerdo a la sección seleccionada
        funciones.CambiarTituloSeccionesDelictivo("ANEXO C. INSPECCIÓN DE VEHÍCULO",getContext(),getActivity());

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


                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext()).
                                setMessage("¿DESEA ELIMINAR EL VEHÍCULO "+ ListaIdVehiculoDelictivo.get(position) + "" ).
                                setPositiveButton( "ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //CONSUME WEB SERVICE PARA ELIMINAR DB
                                        String IdDetenido = ListaIdVehiculoDelictivo.get(position);
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
        return root;
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
                //cargarDetenidos();
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

}