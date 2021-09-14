package mx.ssp.iph.delictivo.ui.fragmets;

import android.content.Context;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.google.gson.internal.$Gson$Preconditions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.Random;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.delictivo.model.ModeloArmasFuego_Delictivo;
import mx.ssp.iph.delictivo.model.ModeloRecepcionLugarIntervencion;
import mx.ssp.iph.delictivo.viewModel.EntregaRecepcion_DelictivoViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorFirmaDelictivo;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;


public class EntregaRecepcion_Delictivo extends Fragment {

    private EntregaRecepcion_DelictivoViewModel mViewModel;
    private Funciones funciones;
    private ImageView imgMicrofonoAccionesRealizadas,imgMicrofonoObservacionesLugarIntervencion,imgFirmaPersonaRecepciondelLugar;
    EditText txtAccionesRealizadas,txtObservacionesLugarIntervencion,txtCualAutoridad,txtmotivoIngreso,
            txtPrimerApellidoPersonaRecepciondelLugar,txtSegundoApellidoPersonaRecepciondelLugar,txtNombresPersonaRecepciondelLugar,
            txtFechaEntregaRecepcionLugardelaIntervencion,txtHoraEntregaRecepcionLugardelaIntervencion,
            txtPrimerApellidoPersonal,txtSegundoApellidoPersonal,txtNombresPersonal;
    TextView lblFirmaOcultoRecepcionIntervencion;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    String cargarIdHechoDelictivo,cargarIdPoliciaPrimerRespondiente;
    RadioGroup rgServiciosEspecializados,rgIngreso;
    RadioButton rbNoServiciosEspecializados, rbSiServiciosEspecializados, rgNoIngreso, rgSiIngreso;
    Spinner spAdscripcionPersonaRecepciondelLugar,spCargoPersonaRecepciondelLugar,
            spCargoIntervencion,spInstitucionIntervencion;
    Button btnGuardarEntregaRecepcion;
    SharedPreferences share;
    String varServiciosEspecializados,varIngreso,descAutoridad,descCargo,rutaFirmaRecibe,cadena;
    LinearLayout Linear2, Linear3, Linear5, lyFirmaPersonaRecepciondelLugar, Linear11ObservacionesLugarIntervencion;
    int numberRandom,randomUrlImagen;

    private ImageView btnAgregarPersona;
    private ListView lvPersonas;

    ArrayList<String> ListaJsonPertenencias;
    ArrayList<String> ListPertenencia,ListDescripcionPertenencia,ListDestino,ListaIdPertenencias;


    public static EntregaRecepcion_Delictivo newInstance() {
        return new EntregaRecepcion_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entrega_recepcion_fragment, container, false);
        /***************************************************************************************/
        funciones = new Funciones();
        funciones.CambiarTituloSeccionesDelictivo("ANEXO F. ENTREGA-RECPCIÓN DEL LUGAR DE LA INTERVENCIÓN",getContext(),getActivity());

        cargarDatos();
        //************************************** ACCIONES DE LA VISTA **************************************//
        //Botones Imagen
        imgMicrofonoAccionesRealizadas = view.findViewById(R.id.imgMicrofonoAccionesRealizadas);
        imgMicrofonoObservacionesLugarIntervencion = view.findViewById(R.id.imgMicrofonoObservacionesLugarIntervencion);

        //EditText
        txtAccionesRealizadas = view.findViewById(R.id.txtAccionesRealizadas);
        txtAccionesRealizadas.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});
        txtObservacionesLugarIntervencion = view.findViewById(R.id.txtObservacionesLugarIntervencion);
        txtObservacionesLugarIntervencion.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});

        txtCualAutoridad = view.findViewById(R.id.txtCualAutoridad);
        txtmotivoIngreso = view.findViewById(R.id.txtmotivoIngreso);
        txtPrimerApellidoPersonaRecepciondelLugar = view.findViewById(R.id.txtPrimerApellidoPersonaRecepciondelLugar);
        txtSegundoApellidoPersonaRecepciondelLugar = view.findViewById(R.id.txtSegundoApellidoPersonaRecepciondelLugar);
        txtNombresPersonaRecepciondelLugar = view.findViewById(R.id.txtNombresPersonaRecepciondelLugar);
        txtFechaEntregaRecepcionLugardelaIntervencion = view.findViewById(R.id.txtFechaEntregaRecepcionLugardelaIntervencion);
        txtHoraEntregaRecepcionLugardelaIntervencion = view.findViewById(R.id.txtHoraEntregaRecepcionLugardelaIntervencion);
        rgServiciosEspecializados = view.findViewById(R.id.rgServiciosEspecializados);
        rgIngreso = view.findViewById(R.id.rgIngreso);
        rbNoServiciosEspecializados = view.findViewById(R.id.rbNoServiciosEspecializados);
        rbSiServiciosEspecializados = view.findViewById(R.id.rbSiServiciosEspecializados);
        rgNoIngreso = view.findViewById(R.id.rgNoIngreso);
        rgSiIngreso = view.findViewById(R.id.rgSiIngreso);
        spAdscripcionPersonaRecepciondelLugar = view.findViewById(R.id.spAdscripcionPersonaRecepciondelLugar);
        spCargoPersonaRecepciondelLugar = view.findViewById(R.id.spCargoPersonaRecepciondelLugar);
        btnGuardarEntregaRecepcion = view.findViewById(R.id.btnGuardarEntregaRecepcion);
        imgFirmaPersonaRecepciondelLugar = view.findViewById(R.id.imgFirmaPersonaRecepciondelLugar);
        lblFirmaOcultoRecepcionIntervencion = view.findViewById(R.id.lblFirmaOcultoRecepcionIntervencion);

        txtPrimerApellidoPersonal = view.findViewById(R.id.txtPrimerApellidoPersonal);
        txtSegundoApellidoPersonal = view.findViewById(R.id.txtSegundoApellidoPersonal);
        txtNombresPersonal = view.findViewById(R.id.txtNombresPersonal);
        spCargoIntervencion = view.findViewById(R.id.spCargoIntervencion);
        spInstitucionIntervencion = view.findViewById(R.id.spInstitucionIntervencion);
        Linear2 = view.findViewById(R.id.Linear2);
        Linear3 = view.findViewById(R.id.Linear3);
        Linear5 = view.findViewById(R.id.Linear5);
        lyFirmaPersonaRecepciondelLugar = view.findViewById(R.id.lyFirmaPersonaRecepciondelLugar);
        Linear11ObservacionesLugarIntervencion = view.findViewById(R.id.Linear11ObservacionesLugarIntervencion);
        ListCombos();

        //Personas
        btnAgregarPersona = view.findViewById(R.id.btnAgregarPersona);
        lvPersonas = view.findViewById(R.id.lvPersonas);



        //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
        if (funciones.ping(getContext()))
        {
            //Toast.makeText(getContext(), "cargarNoReferenciaAdministrativa()", Toast.LENGTH_LONG).show();
            //cargarEntregaRecepcion();
            CargarRecepcionIngreso();
           Log.i("peronas","ping");
        }


        //Imagen que funciona para activar la grabación de voz
        imgMicrofonoAccionesRealizadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMicrofonoAccionesRealizadas.setImageResource(R.drawable.ic_micro_press);
                imgMicrofonoAccionesRealizadas.setTag(R.drawable.ic_micro_press);

                iniciarEntradadeVoz();
            }
        });

        imgMicrofonoObservacionesLugarIntervencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMicrofonoObservacionesLugarIntervencion.setImageResource(R.drawable.ic_micro_press);
                imgMicrofonoObservacionesLugarIntervencion.setTag(R.drawable.ic_micro_press);

                iniciarEntradadeVoz();
            }
        });

        rgServiciosEspecializados.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rbNoServiciosEspecializados) {
                    varServiciosEspecializados = "NO";
                }else if(checkedId == R.id.rbSiServiciosEspecializados){
                    varServiciosEspecializados = "SI";
                }
            }
        });

        rgIngreso.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rgNoIngreso) {
                    varIngreso = "NO";
                }else if(checkedId == R.id.rgSiIngreso){
                    varIngreso = "SI";
                }
            }
        });

        imgFirmaPersonaRecepciondelLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirmaDelictivo dialog = new ContenedorFirmaDelictivo(R.id.lblStatusPersonaRecepciondelLugar,R.id.lblFirmaOcultoRecepcionIntervencion,R.id.imgFirmaPersonaRecepciondelLugarMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });

        txtFechaEntregaRecepcionLugardelaIntervencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaEntregaRecepcionLugardelaIntervencion,getContext(),getActivity());
            }
        });

        txtHoraEntregaRecepcionLugardelaIntervencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txtHoraEntregaRecepcionLugardelaIntervencion,getContext(),getActivity());
            }
        });

        btnGuardarEntregaRecepcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                if(lblFirmaOcultoRecepcionIntervencion.getText().toString().isEmpty()){
                    rutaFirmaRecibe = "NA";
                }else{
                    rutaFirmaRecibe = "http://189.254.7.167/WebServiceIPH/FirmaRecepcionLugarIntervencion/"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
                    insertImagen();
                }
                PrimeraValidacion();

            }
        });

     lvPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext()).
                                setMessage("¿DESEA ELIMINAR "+ ListPertenencia.get(position) + "?" ).
                                setPositiveButton( "ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        EliminarPersonal(ListaIdPertenencias.get(position));

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

     btnAgregarPersona.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if (true) {
                 ValidacionIngresaPersona();
             }
         }
     });

        /***************************************************************************************/
        return view;
    }

    private void PrimeraValidacion(){
        if(txtAccionesRealizadas.getText().toString().length() >= 3){
            if(rbNoServiciosEspecializados.isChecked()){

                if(rgNoIngreso.isChecked()){
                    //Segunda Validacion
                    SegundaValidacion();

                }

                else if(rgSiIngreso.isChecked()){
                    if(txtmotivoIngreso.getText().toString().length() >= 3){
                        //Segunda Validacion
                        SegundaValidacion();

                    }
                }

                else{
                    Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI INGRESÓ OTRA PERSONA AL LUGAR", Toast.LENGTH_SHORT).show();
                }

            }
            else if(rbSiServiciosEspecializados.isChecked()){
                if(txtCualAutoridad.getText().toString().length() >= 3){

                    if(rgNoIngreso.isChecked()){
                        //Segunda Validacion
                        SegundaValidacion();

                    }

                    else if(rgSiIngreso.isChecked()){
                        if(txtmotivoIngreso.getText().toString().length() >= 3){
                            //Segunda Validacion
                            SegundaValidacion();

                        }
                    }

                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI INGRESÓ OTRA PERSONA AL LUGAR", Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    Toast.makeText(getActivity().getApplicationContext(), "INGRESA A QUÉ AUTORIDAD O SERVICIOS ESPECIALIZADOS SOLICITÓ APOYO", Toast.LENGTH_SHORT).show();
                }

            }

            else{
                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI SOLICITÓ APOYO DE ALGUNA AUTORIDAD O SERVICIOS ESPECIALIZADOS", Toast.LENGTH_SHORT).show();
            }
        }

        else{
            Toast.makeText(getActivity().getApplicationContext(), "EXPLIQUE LAS ACCIONES REALIZADAS", Toast.LENGTH_SHORT).show();
        }
    }

    private void SegundaValidacion(){
        if(txtPrimerApellidoPersonaRecepciondelLugar.getText().toString().length() >= 3){
            if(txtNombresPersonaRecepciondelLugar.getText().toString().length() >= 3){
                if(lblFirmaOcultoRecepcionIntervencion.getText().toString().isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA FIRMA DE LA PERSONA QUE RECEPCIONA EL LUGAR DE LA INTERVENCIÓN", Toast.LENGTH_SHORT).show();
                }

                else{
                    if(txtObservacionesLugarIntervencion.getText().toString().length() >= 3){
                        if(txtFechaEntregaRecepcionLugardelaIntervencion.getText().toString().length() >= 3 ||  txtHoraEntregaRecepcionLugardelaIntervencion.getText().toString().length() >= 3){
                            //Inserta Datos
                            Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                            insertRecepcionLugarIntervencion();
                        }

                        else{
                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA FECHA Y HORA DE LA ENTREGA - RECEPCIÓN DEL LUGAR DE LA INTERVENCIÓN", Toast.LENGTH_SHORT).show();
                        }

                    }

                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA SI HAY OBSERVACIONES DE LA PRESERVACIÓN DEL LUGAR", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            else{
                Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL NOMBRE DE LA PERSONA QUE RECEPCIONA EL LUGAR DE LA INTERVENCIÓN", Toast.LENGTH_SHORT).show();
            }
        }

        else{
            Toast.makeText(getActivity().getApplicationContext(), "INGRESA AL MENOS EL PRIMER APELLIDO DE LA PERSONA QUE RECEPCIONA EL LUGAR DE LA INTERVENCIÓN", Toast.LENGTH_SHORT).show();
        }
    }

    private void ValidacionIngresaPersona(){
        if(txtPrimerApellidoPersonal.getText().toString().length() >= 3){
            if(txtNombresPersonal.getText().toString().length() >= 3){
                insertPertenenciasDetenido();
            }

            else{
                Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL NOMBRE DE LA PERSONA QUE INGRESO AL LUGAR DE LA INTERVENCIÓN", Toast.LENGTH_SHORT).show();
            }
        }

        else{
            Toast.makeText(getActivity().getApplicationContext(), "INGRESA AL MENOS EL PRIMER APELLIDO DE LA PERSONA QUE INGRESO AL LUGAR DE LA INTERVENCIÓN", Toast.LENGTH_SHORT).show();
        }
    }


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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EntregaRecepcion_DelictivoViewModel.class);
        // TODO: Use the ViewModel
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
                    Integer resource = (Integer) imgMicrofonoAccionesRealizadas.getTag();

                    if (resource == R.drawable.ic_micro_press)
                    {
                        String textoActual = txtAccionesRealizadas.getText().toString();
                        txtAccionesRealizadas.setText(textoActual+" " + result.get(0));
                    }
                    else
                    {
                        String textoActual = txtObservacionesLugarIntervencion.getText().toString();
                        txtObservacionesLugarIntervencion.setText(textoActual+" " + result.get(0));
                    }

                }
                break;
            }
        }

        imgMicrofonoAccionesRealizadas.setImageResource(R.drawable.ic_micro);
        imgMicrofonoAccionesRealizadas.setTag(R.drawable.ic_micro);

        imgMicrofonoObservacionesLugarIntervencion.setImageResource(R.drawable.ic_micro);
        imgMicrofonoObservacionesLugarIntervencion.setTag(R.drawable.ic_micro);
    }

    private void insertRecepcionLugarIntervencion() {

        DataHelper dataHelper = new DataHelper(getContext());
        descAutoridad = (String) spAdscripcionPersonaRecepciondelLugar.getSelectedItem();
        int idAdscripcion = dataHelper.getIdAutoridadAdmin(descAutoridad);
        String adscripcion = String.valueOf(idAdscripcion);

        descCargo = (String) spCargoPersonaRecepciondelLugar.getSelectedItem();
        int idCargo = dataHelper.getIdCargo(descCargo);
        String cargo = String.valueOf(idCargo);

        ModeloRecepcionLugarIntervencion modeloRecepcionLugarIntervencion = new ModeloRecepcionLugarIntervencion
                (cargarIdHechoDelictivo,txtAccionesRealizadas.getText().toString(),
                        varServiciosEspecializados, txtCualAutoridad.getText().toString(),cargarIdPoliciaPrimerRespondiente,
                        txtPrimerApellidoPersonaRecepciondelLugar.getText().toString(), txtSegundoApellidoPersonaRecepciondelLugar.getText().toString(),
                        txtNombresPersonaRecepciondelLugar.getText().toString(), cargo, adscripcion,
                        rutaFirmaRecibe,txtObservacionesLugarIntervencion.getText().toString(),
                        txtFechaEntregaRecepcionLugardelaIntervencion.getText().toString(),txtHoraEntregaRecepcionLugardelaIntervencion.getText().toString());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo",modeloRecepcionLugarIntervencion.getIdHechoDelictivo())
                .add("DescLugarIntervencion",modeloRecepcionLugarIntervencion.getDescLugarIntervencion())
                .add("ApoyoServiciosEspecializados",modeloRecepcionLugarIntervencion.getApoyoServiciosEspecializados())
                .add("ServicioEspecializado",modeloRecepcionLugarIntervencion.getServicioEspecializado())
                .add("IdPoliciaPrimerRespondiente",modeloRecepcionLugarIntervencion.getIdPoliciaPrimerRespondiente())
                .add("APRecibeIntervencion",modeloRecepcionLugarIntervencion.getAPRecibeIntervencion())
                .add("AMRecibeIntervencion",modeloRecepcionLugarIntervencion.getAMRecibeIntervencion())
                .add("NombreReciveIntervencion",modeloRecepcionLugarIntervencion.getNombreReciveIntervencion())
                .add("IdCargoRecibe",modeloRecepcionLugarIntervencion.getIdCargoRecibe())
                .add("IdAdscripcionRecibe",modeloRecepcionLugarIntervencion.getIdAdscripcionRecibe())
                .add("RutaFirmaRecibe",modeloRecepcionLugarIntervencion.getRutaFirmaRecibe())
                .add("Observaciones",modeloRecepcionLugarIntervencion.getObservaciones())
                .add("Fecha",modeloRecepcionLugarIntervencion.getFecha())
                .add("Hora",modeloRecepcionLugarIntervencion.getHora())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDRecepcionLugarIntervencion/")
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
                    EntregaRecepcion_Delictivo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
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
    public void insertImagen() {
        cadena = lblFirmaOcultoRecepcionIntervencion.getText().toString();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Description", cargarIdHechoDelictivo+randomUrlImagen+".jpg")
                .add("ImageData", cadena)
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/MultimediaRecepcionLugarIntervencion")
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
                if(response.code() == 500){
                    Toast.makeText(getContext(), "EXISTIÓ UN ERROR EN SU CONEXIÓN A INTERNET, INTÉNTELO NUEVAMENTE", Toast.LENGTH_SHORT).show();
                }else if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    EntregaRecepcion_Delictivo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
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

    private void ListCombos() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> cargos = dataHelper.getAllCargos();
        ArrayList<String> autoridad = dataHelper.getAllAutoridadAdmin();
        if (cargos.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE CARGOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, cargos);
            spCargoPersonaRecepciondelLugar.setAdapter(adapter);
            spCargoIntervencion.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON CARGOS ACTIVOS.", Toast.LENGTH_LONG).show();
        }
        if (autoridad.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE AUTORIDAD");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, autoridad);
            spAdscripcionPersonaRecepciondelLugar.setAdapter(adapter);
            spInstitucionIntervencion.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON ADSCRIPCIONES.", Toast.LENGTH_LONG).show();
        }
    }

    public void random(){
        Random random = new Random();
        numberRandom = random.nextInt(9000)*99;
        randomUrlImagen = numberRandom;
    }

    public void cargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdPoliciaPrimerRespondiente = share.getString("Usuario", "SIN INFORMACION");
        cargarIdHechoDelictivo = share.getString("IDHECHODELICTIVO", "SIN INFORMACION");
    }


    //***************** agregar personal **************************//
    private void CargarRecepcionIngreso() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDTempoRecepcionIngresos?folioInternoIngreso="+cargarIdHechoDelictivo)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR PERSONAS ANEXO F, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                Log.i("peronas","run");

                                String resp = myResponse;
                                ListaJsonPertenencias = new ArrayList<String>();
                                ListPertenencia = new ArrayList<String>();
                                ListDescripcionPertenencia = new ArrayList<String>();
                                ListDestino = new ArrayList<String>();
                                ListaIdPertenencias = new ArrayList<String>();


                                //***************** RESPUESTA DEL WEBSERVICE **************************//

                                //CONVERTIR ARREGLO DE JSON A OBJET JSON
                                String ArregloJson = resp.replace("[", "");
                                ArregloJson = ArregloJson.replace("]", "");

                                if(ArregloJson.equals(""))
                                {
                                    //Sin Información. Todos los campos vacíos.
                                    Log.i("peronas","vacio");
                                    Log.i("peronas",ArregloJson);


                                }
                                else{
                                    Log.i("peronas","json");

                                    //SEPARAR CADA detenido EN UN ARREGLO
                                    String[] ArrayPertenenciasReal = ArregloJson.split(Pattern.quote("},"));
                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordePertenencias=0;
                                    while(contadordePertenencias < ArrayPertenenciasReal.length){
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayPertenenciasReal[contadordePertenencias] + "}");
                                            Log.i("peronas",Integer.toString(contadordePertenencias));

                                            ListaIdPertenencias.add(jsonjObject.getString("IdIngreso"));
                                            ListPertenencia.add(jsonjObject.getString("APIngreso")+" " +jsonjObject.getString("AMIngreso") + " " + jsonjObject.getString("NombresIngreso"));
                                            ListDescripcionPertenencia.add(jsonjObject.getString("MotivoIngreso"));
                                            ListDestino.add(jsonjObject.getString("CargoIngreso"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordePertenencias++;
                                    }
                                }

                                //*************************
                                EntregaRecepcion_Delictivo.MyAdapterRecepcionIngreso adapter = new EntregaRecepcion_Delictivo.MyAdapterRecepcionIngreso(getContext(), ListaIdPertenencias, ListPertenencia);
                                lvPersonas.setAdapter(adapter);
                                funciones.ajustaAlturaListView(lvPersonas,80);
                            }
                        });
                    }

                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL SOLICITAR LISTA ANEXO F, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    //***************** ADAPTADOR PARA LLENAR LISTA DE personal que ingresó **************************//
    class MyAdapterRecepcionIngreso extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> ListaTablaPertenencias,ListaPertenencias;

        MyAdapterRecepcionIngreso (Context c, ArrayList<String> ListaTablaPertenencias, ArrayList<String> ListaPertenencias) {
            super(c, R.layout.row_pertenencias, ListaTablaPertenencias);
            this.context = c;
            this.ListaTablaPertenencias = ListaTablaPertenencias;
            this.ListaPertenencias = ListaPertenencias;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_pertenencias, parent, false);
            Log.i("peronas","adapter");
            TextView lblPertenencia = row.findViewById(R.id.lblPertenencia);
            TextView lblDescripcion = row.findViewById(R.id.lblDescripcion);
            TextView lblDestino = row.findViewById(R.id.lblDestino);


            // Asigna los valores
            lblPertenencia.setText(ListPertenencia.get(position));
            lblDescripcion.setText(ListDescripcionPertenencia.get(position));
            lblDestino.setText(ListDestino.get(position));

            return row;
        }
    }

    //********************************** INSERTAR PERSONAL QUE INGRESÓ AL SERVIDOR ***********************************//
    public void insertPertenenciasDetenido() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo", cargarIdHechoDelictivo)
                .add("Ingreso", "SI")
                .add("MotivoIngreso", txtmotivoIngreso.getText().toString())
                .add("APIngreso", txtPrimerApellidoPersonal.getText().toString())
                .add("AMIngreso", txtSegundoApellidoPersonal.getText().toString())
                .add("NombresIngreso", txtNombresPersonal.getText().toString())
                .add("CargoIngreso", Integer.toString(spCargoIntervencion.getSelectedItemPosition()))
                .add("InstitucionIngreso", Integer.toString(spInstitucionIntervencion.getSelectedItemPosition()))

                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDTempoRecepcionIngresos")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL AGREGAR PERSONAL QUE INGRESÓ, FAVOR DE VERIFICAR SU CONEXCIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();  /********** ME REGRESA LA RESPUESTA DEL WS ****************/
                    Log.i("INSERTAR-PERSONAL", myResponse);

                    EntregaRecepcion_Delictivo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String resp = myResponse;

                                if(resp.length() == 4)
                                {
                                    Toast.makeText(getContext(), "PERTENENCIA AGREGADA CORRECTAMENTE", Toast.LENGTH_LONG).show();
                                    txtmotivoIngreso.setText("");
                                    txtPrimerApellidoPersonal.setText("");
                                    txtSegundoApellidoPersonal.setText("");
                                    txtNombresPersonal.setText("");
                                    spCargoIntervencion.setSelection(0);
                                    spInstitucionIntervencion.setSelection(0);
                                    CargarRecepcionIngreso();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "VUELVA A INTENTARLO", Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (Exception e)
                            {

                            }

                        }
                    });
                }
            }
        });
    }

    //***************** EliminarPertenencia Temporales**************************//
    private void EliminarPersonal(String IdPertenencia) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDTempoRecepcionIngresos?tempoFolioInterno="+cargarIdHechoDelictivo+"&tempoIdIngreso="+IdPertenencia)
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ELIMINAR, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                    CargarRecepcionIngreso();
                                }
                                else{
                                    Toast.makeText(getContext(), "PROBLEMA AL ELIMINAR", Toast.LENGTH_SHORT).show();
                                }
                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL ELIMINAR PERSONAL, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

}