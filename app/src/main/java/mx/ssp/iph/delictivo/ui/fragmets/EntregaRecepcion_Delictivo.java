package mx.ssp.iph.delictivo.ui.fragmets;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
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
    RadioGroup rgServiciosEspecializados,rgIngreso;
    Spinner spAdscripcionPersonaRecepciondelLugar,spCargoPersonaRecepciondelLugar,
            spCargoIntervencion,spInstitucionIntervencion;
    Button btnGuardarEntregaRecepcion;
    SharedPreferences share;
    String cargarIdPoliciaPrimerRespondiente,cargarIdHechoDelictivo,
            varServiciosEspecializados,varIngreso,descAutoridad,descCargo,rutaFirmaRecibe,cadena;
    int numberRandom,randomUrlImagen;


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

        //************************************** ACCIONES DE LA VISTA **************************************//
        cargarDatos();
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
        ListCombos();

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
                insertRecepcionLugarIntervencion();

            }
        });

        /***************************************************************************************/
        return view;
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
            spCargoIntervencion.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON ADSCRIPCIONES.", Toast.LENGTH_LONG).show();
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

}