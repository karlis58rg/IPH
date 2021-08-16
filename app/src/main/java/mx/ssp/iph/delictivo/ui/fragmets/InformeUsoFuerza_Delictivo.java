package mx.ssp.iph.delictivo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import mx.ssp.iph.R;
import mx.ssp.iph.delictivo.model.ModeloUsoFuerza_Delictivo;
import mx.ssp.iph.delictivo.viewModel.InformeUsoFuerzaDelictivoViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class InformeUsoFuerza_Delictivo extends Fragment {

    private InformeUsoFuerzaDelictivoViewModel mViewModel;
    Button btnGuardarUsoFuerza;
    RadioGroup rgReduccionFisicaDelictivo,rgArmasIncapacitantesDelictivo,rgArmasFuegoDelictivo,rgAsistenciaMedicaUsoFuerza;
    Spinner spAutoridadesLesionadosUsoFuerza,spAutoridadesFallecidasUsoFuerza,spPersonasLesionadasUsoFuerza,spPersonasFallecidasUsoFuerza;
    EditText txtDescripciondelUsoFuerza,txtDescripcionAsistenciaMedicaUsoFuerza;
    String cargarIdPoliciaPrimerRespondiente,cargarIdHechoDelictivo,varLesionadosUsoFuerza = "",varReduccionFisicaDelictivo = "",varArmasIncapacitantesDelictivo = "",
            varArmasFuegoDelictivo = "",varAsistenciaMedicaUsoFuerza = "",varNuevoElementoUsoFuerza = "",
            varNoElementosAutoridadesLesionados = "000",varNoElementosAutoridadesFallecidas = "000",
            varNoElementosPersonasLesionadas = "000",varNoElementosPersonasFallecidas = "000",
            varLesionadosAgentes,varLesionadosPersona,varFallecidoAgente,varFallecidoPersona;
    SharedPreferences share;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    private ImageView img_microfonoDescripcionUsoFuerza,img_microfonoDescripcionAsistenciaMedicaUsoFuerza;
    private int banderaPostPut=0;
    private RadioButton rbNoReduccionFisicaDelictivo,rbSiReduccionFisicaDelictivo,rbNoArmasIncapacitantesDelictivo,rbSiArmasIncapacitantesDelictivo,rbNoArmasFuegoDelictivo,rbSiArmasFuegoDelictivo;
    private  RadioButton rbSiAsistenciaMedicaUsoFuerza,rbNoAsistenciaMedicaUsoFuerza,rbSiNuevoElementoUsoFuerza,rbNoNuevoElementoUsoFuerza;
    ViewGroup quinceavoLinearFuerza, OpcionesUnoDelic, lyOpcionesDosDelic, catorceavoLinear, principalLinear1, catorceavoLinear1;

    private Funciones funciones;

    public static InformeUsoFuerza_Delictivo newInstance() {
        return new InformeUsoFuerza_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.informe_uso_fuerza__delictivo_fragment, container, false);
        /*******************************************************************/
        cargarDatos();
        rgReduccionFisicaDelictivo = view.findViewById(R.id.rgReduccionFisicaDelictivo);
        rgArmasIncapacitantesDelictivo = view.findViewById(R.id.rgArmasIncapacitantesDelictivo);
        rgArmasFuegoDelictivo = view.findViewById(R.id.rgArmasFuegoDelictivo);
        rgAsistenciaMedicaUsoFuerza = view.findViewById(R.id.rgAsistenciaMedicaUsoFuerza);
        spAutoridadesLesionadosUsoFuerza = view.findViewById(R.id.spAutoridadesLesionadosUsoFuerza);
        spAutoridadesFallecidasUsoFuerza = view.findViewById(R.id.spAutoridadesFallecidasUsoFuerza);
        spPersonasLesionadasUsoFuerza = view.findViewById(R.id.spPersonasLesionadasUsoFuerza);
        spPersonasFallecidasUsoFuerza = view.findViewById(R.id.spPersonasFallecidasUsoFuerza);
        txtDescripciondelUsoFuerza = view.findViewById(R.id.txtDescripciondelUsoFuerza);
        txtDescripcionAsistenciaMedicaUsoFuerza = view.findViewById(R.id.txtDescripcionAsistenciaMedicaUsoFuerza);
        btnGuardarUsoFuerza = view.findViewById(R.id.btnGuardarUsoFuerza);

        rbNoReduccionFisicaDelictivo = view.findViewById(R.id.rbNoReduccionFisicaDelictivo);
        rbSiReduccionFisicaDelictivo = view.findViewById(R.id.rbSiReduccionFisicaDelictivo);
        rbNoArmasIncapacitantesDelictivo = view.findViewById(R.id.rbNoArmasIncapacitantesDelictivo);
        rbSiArmasIncapacitantesDelictivo = view.findViewById(R.id.rbSiArmasIncapacitantesDelictivo);
        rbNoArmasFuegoDelictivo = view.findViewById(R.id.rbNoArmasFuegoDelictivo);
        rbSiArmasFuegoDelictivo = view.findViewById(R.id.rbSiArmasFuegoDelictivo);
        rbSiAsistenciaMedicaUsoFuerza = view.findViewById(R.id.rbSiAsistenciaMedicaUsoFuerza);
        rbNoAsistenciaMedicaUsoFuerza = view.findViewById(R.id.rbNoAsistenciaMedicaUsoFuerza);

        quinceavoLinearFuerza = view.findViewById(R.id.quinceavoLinearFuerza);
        OpcionesUnoDelic = view.findViewById(R.id.OpcionesUnoDelic);
        lyOpcionesDosDelic = view.findViewById(R.id.lyOpcionesDosDelic);
        catorceavoLinear = view.findViewById(R.id.catorceavoLinear);
        principalLinear1 = view.findViewById(R.id.principalLinear1);
        catorceavoLinear1 = view.findViewById(R.id.catorceavoLinear1);

        img_microfonoDescripcionUsoFuerza = view.findViewById(R.id.img_microfonoDescripcionUsoFuerza);
        img_microfonoDescripcionAsistenciaMedicaUsoFuerza = view.findViewById(R.id.img_microfonoDescripcionAsistenciaMedicaUsoFuerza);

        funciones = new Funciones();
        funciones.CambiarTituloSeccionesDelictivo("ANEXO B. INFORME DEL USO DE LA FUERZA",getContext(),getActivity());

        //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
        if (funciones.ping(getContext()))
        {
            cargarUsoFuerzaDelictivo();
        }

        rgReduccionFisicaDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiReduccionFisicaDelictivo) {
                    varReduccionFisicaDelictivo = "SI";
                } else if (checkedId == R.id.rbNoReduccionFisicaDelictivo) {
                    varReduccionFisicaDelictivo = "NO";
                }

            }
        });
        rgArmasIncapacitantesDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiArmasIncapacitantesDelictivo) {
                    varArmasIncapacitantesDelictivo = "SI";
                } else if (checkedId == R.id.rbNoArmasIncapacitantesDelictivo) {
                    varArmasIncapacitantesDelictivo = "NO";
                }

            }
        });
        rgArmasFuegoDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiArmasFuegoDelictivo) {
                    varArmasFuegoDelictivo = "SI";
                } else if (checkedId == R.id.rbNoArmasFuegoDelictivo) {
                    varArmasFuegoDelictivo = "NO";
                }

            }
        });
        rgAsistenciaMedicaUsoFuerza.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiAsistenciaMedicaUsoFuerza) {
                    varAsistenciaMedicaUsoFuerza = "SI";
                } else if (checkedId == R.id.rbNoAsistenciaMedicaUsoFuerza) {
                    varAsistenciaMedicaUsoFuerza = "NO";
                }

            }
        });

        btnGuardarUsoFuerza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (banderaPostPut == 0)
                {
                    PrimeraValidacion();
                }
                else    {
                    PrimeraValidacionUPD();
                }

            }
        });


        //Imagen que funciona para activar la grabación de voz
        img_microfonoDescripcionUsoFuerza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_microfonoDescripcionUsoFuerza.setImageResource(R.drawable.ic_micro_press);
                img_microfonoDescripcionUsoFuerza.setTag(R.drawable.ic_micro_press);

                iniciarEntradadeVoz();
            }
        });


        //Imagen que funciona para activar la grabación de voz
        img_microfonoDescripcionAsistenciaMedicaUsoFuerza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_microfonoDescripcionAsistenciaMedicaUsoFuerza.setImageResource(R.drawable.ic_micro_press);
                img_microfonoDescripcionAsistenciaMedicaUsoFuerza.setTag(R.drawable.ic_micro_press);

                iniciarEntradadeVoz();
            }
        });



        /********************************************************************/
        return view;
    }

    //VALIDACIONES INSERTAR
    public void PrimeraValidacion(){

        if(rbNoReduccionFisicaDelictivo.isChecked() || rbSiReduccionFisicaDelictivo.isChecked()){
            if(rbNoArmasIncapacitantesDelictivo.isChecked() || rbSiArmasIncapacitantesDelictivo.isChecked()){
                if(rbNoArmasFuegoDelictivo.isChecked() || rbSiArmasFuegoDelictivo.isChecked()){
                    if (txtDescripciondelUsoFuerza.getText().toString().length() >= 3){

                        //Segunda Valicacion
                        SegundaValidacion();

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "DESCRIBE LAS CONDUCTAS QUE MOTIVARON EL USO DE LA FUERZA", Toast.LENGTH_SHORT).show();
                        catorceavoLinear.requestFocus();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI SE UTILIZARON ARMAS DE FUEGO O FUERZA LETAL", Toast.LENGTH_SHORT).show();
                    OpcionesUnoDelic.requestFocus();
                    lyOpcionesDosDelic.requestFocus();
                    quinceavoLinearFuerza.requestFocus();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI SE UTILIZARON ARMAS INCAPACITANTES MENOS LETALES", Toast.LENGTH_SHORT).show();
                OpcionesUnoDelic.requestFocus();
                lyOpcionesDosDelic.requestFocus();
                quinceavoLinearFuerza.requestFocus();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI HUBO REDUCCIÓN FÍSICA DE MOVIMIENTOS", Toast.LENGTH_SHORT).show();
            OpcionesUnoDelic.requestFocus();
            lyOpcionesDosDelic.requestFocus();
            quinceavoLinearFuerza.requestFocus();
        }





    }
    public void SegundaValidacion(){
        if(rbSiAsistenciaMedicaUsoFuerza.isChecked()){
            if(txtDescripcionAsistenciaMedicaUsoFuerza.getText().toString().length() >= 3){
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                insertUsoFuerza();

            } else {
                Toast.makeText(getActivity().getApplicationContext(), "EXPLIQUE LA ASISTENCIA MÉDICA QUE SOLICITÓ O BRINDÓ", Toast.LENGTH_SHORT).show();
                catorceavoLinear1.requestFocus();
            }
        } else if (rbNoAsistenciaMedicaUsoFuerza.isChecked()){
            Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
            insertUsoFuerza();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI BRINDÓ O SOLICITÓ ASISTENCIA MÉDICA", Toast.LENGTH_SHORT).show();
            principalLinear1.requestFocus();
        }
    }


    //VALIDACIONES ACTUALIZAR
    public void PrimeraValidacionUPD(){

        if(rbNoReduccionFisicaDelictivo.isChecked() || rbSiReduccionFisicaDelictivo.isChecked()){
            if(rbNoArmasIncapacitantesDelictivo.isChecked() || rbSiArmasIncapacitantesDelictivo.isChecked()){
                if(rbNoArmasFuegoDelictivo.isChecked() || rbSiArmasFuegoDelictivo.isChecked()){
                    if (txtDescripciondelUsoFuerza.getText().toString().length() >= 3){

                        //Segunda Valicacion
                        SegundaValidacionUPD();

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "DESCRIBE LAS CONDUCTAS QUE MOTIVARON EL USO DE LA FUERZA", Toast.LENGTH_SHORT).show();
                        catorceavoLinear.requestFocus();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI SE UTILIZARON ARMAS DE FUEGO O FUERZA LETAL", Toast.LENGTH_SHORT).show();
                    OpcionesUnoDelic.requestFocus();
                    lyOpcionesDosDelic.requestFocus();
                    quinceavoLinearFuerza.requestFocus();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI SE UTILIZARON ARMAS INCAPACITANTES MENOS LETALES", Toast.LENGTH_SHORT).show();
                OpcionesUnoDelic.requestFocus();
                lyOpcionesDosDelic.requestFocus();
                quinceavoLinearFuerza.requestFocus();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI HUBO REDUCCIÓN FÍSICA DE MOVIMIENTOS", Toast.LENGTH_SHORT).show();
            OpcionesUnoDelic.requestFocus();
            lyOpcionesDosDelic.requestFocus();
            quinceavoLinearFuerza.requestFocus();
        }





    }
    public void SegundaValidacionUPD(){
        if(rbSiAsistenciaMedicaUsoFuerza.isChecked()){
            if(txtDescripcionAsistenciaMedicaUsoFuerza.getText().toString().length() >= 3){
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                updateUsoFuerza();

            } else {
                Toast.makeText(getActivity().getApplicationContext(), "EXPLIQUE LA ASISTENCIA MÉDICA QUE SOLICITÓ O BRINDÓ", Toast.LENGTH_SHORT).show();
                catorceavoLinear1.requestFocus();
            }
        } else if (rbNoAsistenciaMedicaUsoFuerza.isChecked()){
            Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
            updateUsoFuerza();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI BRINDÓ O SOLICITÓ ASISTENCIA MÉDICA", Toast.LENGTH_SHORT).show();
            principalLinear1.requestFocus();
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
        mViewModel = new ViewModelProvider(this).get(InformeUsoFuerzaDelictivoViewModel.class);
        // TODO: Use the ViewModel
    }


    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void updateUsoFuerza() {
        Log.i("FUERZA", "Inicia método updateUsoFuerza");

        varNoElementosAutoridadesLesionados = (String) spAutoridadesLesionadosUsoFuerza.getSelectedItem();
        if(varNoElementosAutoridadesLesionados.equals("--Selecciona--")){
            varLesionadosAgentes = "NO";
            varNoElementosAutoridadesLesionados = "000";
        }else{
            varLesionadosAgentes = "SI";
        }
        varNoElementosAutoridadesFallecidas = (String) spAutoridadesFallecidasUsoFuerza.getSelectedItem();
        if(varNoElementosAutoridadesFallecidas.equals("--Selecciona--")){
            varFallecidoAgente = "NO";
            varNoElementosAutoridadesFallecidas = "000";
        }else{
            varFallecidoAgente = "SI";
        }
        varNoElementosPersonasLesionadas = (String) spPersonasLesionadasUsoFuerza.getSelectedItem();
        if(varNoElementosPersonasLesionadas.equals("--Selecciona--")){
            varLesionadosPersona = "NO";
            varNoElementosPersonasLesionadas = "000";
        }else{
            varLesionadosPersona = "SI";
        }
        varNoElementosPersonasFallecidas = (String) spPersonasFallecidasUsoFuerza.getSelectedItem();
        if(varNoElementosPersonasFallecidas.equals("--Selecciona--")){
            varFallecidoPersona = "NO";
            varNoElementosPersonasFallecidas = "000";
        }else{
            varFallecidoPersona = "SI";
        }

        ModeloUsoFuerza_Delictivo modeloUsoFuerza = new ModeloUsoFuerza_Delictivo
                (cargarIdHechoDelictivo, varLesionadosAgentes, varNoElementosAutoridadesLesionados, varLesionadosPersona,
                        varNoElementosPersonasLesionadas, varFallecidoAgente, varNoElementosAutoridadesFallecidas, varFallecidoPersona,
                        varNoElementosPersonasFallecidas,varReduccionFisicaDelictivo, varArmasIncapacitantesDelictivo, varArmasFuegoDelictivo,
                        txtDescripciondelUsoFuerza.getText().toString(), varAsistenciaMedicaUsoFuerza, txtDescripcionAsistenciaMedicaUsoFuerza.getText().toString(),
                        cargarIdPoliciaPrimerRespondiente);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo",modeloUsoFuerza.getIdHechoDelictivo())
                .add("LesionadosAutoridad",modeloUsoFuerza.getLesionadosAutoridad())
                .add("NumLesionadosAutoridad", modeloUsoFuerza.getNumLesionadosAutoridad())
                .add("LesionadosPersona", modeloUsoFuerza.getLesionadosPersona())
                .add("NumLesionadosPersona", modeloUsoFuerza.getNumLesionadosPersona())
                .add("FallecidosAutoridad", modeloUsoFuerza.getFallecidosAutoridad())
                .add("NumFallecidosAutoridad", modeloUsoFuerza.getNumFallecidosAutoridad())
                .add("FallecidosPersona", modeloUsoFuerza.getFallecidosPersona())
                .add("NumFallecidosPersona", modeloUsoFuerza.getNumLesionadosPersona())
                .add("ReduccionMovimientos", modeloUsoFuerza.getReduccionMovimientos())
                .add("ArmasIncapacitantes", modeloUsoFuerza.getArmasIncapacitantes())
                .add("ArmasLetal", modeloUsoFuerza.getArmasLetal())
                .add("NarrativaUsoFuerza", modeloUsoFuerza.getNarrativaUsoFuerza())
                .add("AsistenciaMedica", modeloUsoFuerza.getAsistenciaMedica())
                .add("NarrativaAsistenciaMedica", modeloUsoFuerza.getNarrativaAsistenciaMedica())
                .add("IdPoliciaPrimerRespondiente", modeloUsoFuerza.getIdPoliciaPrimerRespondiente())
                .build();

        Log.i("FUERZA", "Body:"+body.toString());

        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDUsoFuerza/")
                .put(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("FUERZA", "Falla"+e.toString());

                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("FUERZA", "Respuesta");

                if (response.isSuccessful()) {
                    Log.i("FUERZA", "Respuesta is successful");

                    final String myResponse = response.body().string();
                    InformeUsoFuerza_Delictivo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("FUERZA", "Run");
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
    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertUsoFuerza() {
        Log.i("FUERZA", "Inicia método updateUsoFuerza");

        varNoElementosAutoridadesLesionados = (String) spAutoridadesLesionadosUsoFuerza.getSelectedItem();
        if(varNoElementosAutoridadesLesionados.equals("--Selecciona--")){
            varLesionadosAgentes = "NO";
            varNoElementosAutoridadesLesionados = "000";
        }else{
            varLesionadosAgentes = "SI";
        }
        varNoElementosAutoridadesFallecidas = (String) spAutoridadesFallecidasUsoFuerza.getSelectedItem();
        if(varNoElementosAutoridadesFallecidas.equals("--Selecciona--")){
            varFallecidoAgente = "NO";
            varNoElementosAutoridadesFallecidas = "000";
        }else{
            varFallecidoAgente = "SI";
        }
        varNoElementosPersonasLesionadas = (String) spPersonasLesionadasUsoFuerza.getSelectedItem();
        if(varNoElementosPersonasLesionadas.equals("--Selecciona--")){
            varLesionadosPersona = "NO";
            varNoElementosPersonasLesionadas = "000";
        }else{
            varLesionadosPersona = "SI";
        }
        varNoElementosPersonasFallecidas = (String) spPersonasFallecidasUsoFuerza.getSelectedItem();
        if(varNoElementosPersonasFallecidas.equals("--Selecciona--")){
            varFallecidoPersona = "NO";
            varNoElementosPersonasFallecidas = "000";
        }else{
            varFallecidoPersona = "SI";
        }

        ModeloUsoFuerza_Delictivo modeloUsoFuerza = new ModeloUsoFuerza_Delictivo
                (cargarIdHechoDelictivo, varLesionadosAgentes, varNoElementosAutoridadesLesionados, varLesionadosPersona,
                        varNoElementosPersonasLesionadas, varFallecidoAgente, varNoElementosAutoridadesFallecidas, varFallecidoPersona,
                        varNoElementosPersonasFallecidas,varReduccionFisicaDelictivo, varArmasIncapacitantesDelictivo, varArmasFuegoDelictivo,
                        txtDescripciondelUsoFuerza.getText().toString(), varAsistenciaMedicaUsoFuerza, txtDescripcionAsistenciaMedicaUsoFuerza.getText().toString(),
                        cargarIdPoliciaPrimerRespondiente);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo",modeloUsoFuerza.getIdHechoDelictivo())
                .add("LesionadosAutoridad",modeloUsoFuerza.getLesionadosAutoridad())
                .add("NumLesionadosAutoridad", modeloUsoFuerza.getNumLesionadosAutoridad())
                .add("LesionadosPersona", modeloUsoFuerza.getLesionadosPersona())
                .add("NumLesionadosPersona", modeloUsoFuerza.getNumLesionadosPersona())
                .add("FallecidosAutoridad", modeloUsoFuerza.getFallecidosAutoridad())
                .add("NumFallecidosAutoridad", modeloUsoFuerza.getNumFallecidosAutoridad())
                .add("FallecidosPersona", modeloUsoFuerza.getFallecidosPersona())
                .add("NumFallecidosPersona", modeloUsoFuerza.getNumLesionadosPersona())
                .add("ReduccionMovimientos", modeloUsoFuerza.getReduccionMovimientos())
                .add("ArmasIncapacitantes", modeloUsoFuerza.getArmasIncapacitantes())
                .add("ArmasLetal", modeloUsoFuerza.getArmasLetal())
                .add("NarrativaUsoFuerza", modeloUsoFuerza.getNarrativaUsoFuerza())
                .add("AsistenciaMedica", modeloUsoFuerza.getAsistenciaMedica())
                .add("NarrativaAsistenciaMedica", modeloUsoFuerza.getNarrativaAsistenciaMedica())
                .add("IdPoliciaPrimerRespondiente", modeloUsoFuerza.getIdPoliciaPrimerRespondiente())
                .build();

        Log.i("FUERZA", "Body:"+body.toString());

        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDUsoFuerza/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("FUERZA", "Falla"+e.toString());

                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("FUERZA", "Respuesta");

                if (response.isSuccessful()) {
                    Log.i("FUERZA", "Respuesta is successful");

                    final String myResponse = response.body().string();
                    InformeUsoFuerza_Delictivo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("FUERZA", "Run");
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


    public void cargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdPoliciaPrimerRespondiente = share.getString("Usuario", "SIN INFORMACION");
        cargarIdHechoDelictivo = share.getString("IDHECHODELICTIVO", "SIN INFORMACION");
    }

    //Almacena la Respuesta de la lectura de voz y la coloca en el Cuadro de Texto Correspondiente
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Integer resource = (Integer) img_microfonoDescripcionUsoFuerza.getTag();

                    if (resource == R.drawable.ic_micro_press) {
                        String textoActual = txtDescripciondelUsoFuerza.getText().toString();
                        txtDescripciondelUsoFuerza.setText(textoActual + " " + result.get(0));
                    } else {
                        String textoActual = txtDescripcionAsistenciaMedicaUsoFuerza.getText().toString();
                        txtDescripcionAsistenciaMedicaUsoFuerza.setText(textoActual + " " + result.get(0));
                    }

                }
                break;
            }
        }

        img_microfonoDescripcionUsoFuerza.setImageResource(R.drawable.ic_micro);
        img_microfonoDescripcionUsoFuerza.setTag(R.drawable.ic_micro);

        img_microfonoDescripcionAsistenciaMedicaUsoFuerza.setImageResource(R.drawable.ic_micro);
        img_microfonoDescripcionAsistenciaMedicaUsoFuerza.setTag(R.drawable.ic_micro);
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarUsoFuerzaDelictivo() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDUsoFuerza?folioInterno="+cargarIdHechoDelictivo)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR DATOS ANEXO B, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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


                                        if((jsonjObject.getString("LesionadosAutoridad").equals("SI"))){
                                            spAutoridadesLesionadosUsoFuerza.setSelection(funciones.getIndexSpiner(spAutoridadesLesionadosUsoFuerza, jsonjObject.getString("NumLesionadosAutoridad")));
                                        }

                                        if((jsonjObject.getString("LesionadosPersona").equals("SI"))){
                                            spPersonasLesionadasUsoFuerza.setSelection(funciones.getIndexSpiner(spPersonasLesionadasUsoFuerza, jsonjObject.getString("NumLesionadosPersona")));
                                        }

                                        if((jsonjObject.getString("FallecidosAutoridad").equals("SI"))){
                                            spAutoridadesFallecidasUsoFuerza.setSelection(funciones.getIndexSpiner(spAutoridadesFallecidasUsoFuerza, jsonjObject.getString("NumFallecidosAutoridad")));
                                        }

                                        if((jsonjObject.getString("FallecidosPersona").equals("SI"))){
                                            spPersonasFallecidasUsoFuerza.setSelection(funciones.getIndexSpiner(spPersonasFallecidasUsoFuerza, jsonjObject.getString("NumFallecidosPersona")));
                                        }

                                        if((jsonjObject.getString("ReduccionMovimientos").equals("SI"))){
                                            rbSiReduccionFisicaDelictivo.setChecked(true);
                                        }else if((jsonjObject.getString("ReduccionMovimientos").equals("NO")))
                                        {
                                            rbNoReduccionFisicaDelictivo.setChecked(true);
                                        }

                                        if((jsonjObject.getString("ArmasIncapacitantes").equals("SI"))){
                                            rbSiArmasIncapacitantesDelictivo.setChecked(true);
                                        }else if((jsonjObject.getString("ArmasIncapacitantes").equals("NO")))
                                        {
                                            rbNoArmasIncapacitantesDelictivo.setChecked(true);
                                        }

                                        if((jsonjObject.getString("ArmasLetal").equals("SI"))){
                                            rbSiArmasFuegoDelictivo.setChecked(true);
                                        }else if((jsonjObject.getString("ArmasLetal").equals("NO")))
                                        {
                                            rbNoArmasFuegoDelictivo.setChecked(true);
                                        }

                                        txtDescripciondelUsoFuerza.setText((jsonjObject.getString("NarrativaUsoFuerza")).equals("null")?"":jsonjObject.getString("NarrativaUsoFuerza"));

                                        if((jsonjObject.getString("AsistenciaMedica").equals("SI"))){
                                            rbSiAsistenciaMedicaUsoFuerza.setChecked(true);
                                            txtDescripcionAsistenciaMedicaUsoFuerza.setText((jsonjObject.getString("NarrativaAsistenciaMedica")).equals("null")?"":jsonjObject.getString("NarrativaAsistenciaMedica"));
                                        }else if((jsonjObject.getString("AsistenciaMedica").equals("NO")))
                                        {
                                            rbNoAsistenciaMedicaUsoFuerza.setChecked(true);
                                        }

                                        if((jsonjObject.getString("AsistenciaMedica").equals("SI"))){
                                            rbSiAsistenciaMedicaUsoFuerza.setChecked(true);
                                            txtDescripcionAsistenciaMedicaUsoFuerza.setText((jsonjObject.getString("NarrativaAsistenciaMedica")).equals("null")?"":jsonjObject.getString("NarrativaAsistenciaMedica"));
                                        }else if((jsonjObject.getString("AsistenciaMedica").equals("NO")))
                                        {
                                            rbNoAsistenciaMedicaUsoFuerza.setChecked(true);
                                        }

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
                        Toast.makeText(getContext(), "ERROR AL SOLICITAR INFORMACIÓN ANEXO B, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

}
