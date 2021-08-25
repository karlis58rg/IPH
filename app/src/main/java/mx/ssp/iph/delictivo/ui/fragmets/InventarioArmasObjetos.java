package mx.ssp.iph.delictivo.ui.fragmets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import mx.ssp.iph.administrativo.model.ModeloDescripcionVehiculos_Administrativo;
import mx.ssp.iph.administrativo.ui.fragmets.DescripcionVehiculo;
import mx.ssp.iph.delictivo.model.ModeloArmasFuego_Delictivo;
import mx.ssp.iph.delictivo.viewModel.InventarioArmasObjetosViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class InventarioArmasObjetos extends Fragment {

    private InventarioArmasObjetosViewModel mViewModel;
    private Funciones funciones;
    ImageView btnAgregarArma,imgMicrofonoObservacionesArma,imgFirmaEntrevistado,imgFirmaTestigo1Arma,imgFirmaTestigo2Arma;
    EditText txtLugarEncontroArma,txtCalibreArmaDelictivo, txtMatriculaArmaDelictivo,txtNoSerieArmaDelictivo,txtDestinoArma,txtObservacionesArma,
            txtPrimerApellidoPropietarioArma,txtSegundoApellidoPropietarioArma,txtNombresPropietarioArma,
            txtPrimerApellidoTestigo1Arma,txtSegundoApellidoTestigo1Arma,txtNombresTestigo1Arma,
            txtPrimerApellidoTestigo2Arma,txtSegundoApellidoTestigo2Arma,txtNombresTestigo2Arma;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    RadioGroup rgAportacionInspeccionArmaFuego,rgTipoArma;
    Spinner spColorArmaDelictivo;
    String descripcionColor,cargarIdPoliciaPrimerRespondiente,cargarIdHechoDelictivo,varAportacion,varInspeccion,varTipoArma,varRutaImagen,varRutaImagenT1,varRutaImagenT2;
    SharedPreferences share;
    int numberRandom,randomUrlImagen;

    public static InventarioArmasObjetos newInstance() {
        return new InventarioArmasObjetos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventario_armas_objetos, container, false);
        funciones= new Funciones();
        funciones.CambiarTituloSeccionesDelictivo("ANEXO D. INVENTARIO DE ARMAS Y OBJETOS",getContext(),getActivity());

        //************************************** ACCIONES DE LA VISTA **************************************//
        //************************************** ARMAS DE FUEGO *******************************************//
        cargarDatos();
        random();
        imgMicrofonoObservacionesArma = view.findViewById(R.id.imgMicrofonoObservacionesArma);
        imgFirmaEntrevistado = view.findViewById(R.id.imgFirmaEntrevistado);
        imgFirmaTestigo1Arma = view.findViewById(R.id.imgFirmaTestigo1Arma);
        imgFirmaTestigo2Arma = view.findViewById(R.id.imgFirmaTestigo2Arma);
        txtLugarEncontroArma = view.findViewById(R.id.txtLugarEncontroArma);
        txtCalibreArmaDelictivo = view.findViewById(R.id.txtCalibreArmaDelictivo);
        txtMatriculaArmaDelictivo = view.findViewById(R.id.txtMatriculaArmaDelictivo);
        txtNoSerieArmaDelictivo = view.findViewById(R.id.txtNoSerieArmaDelictivo);
        txtObservacionesArma = view.findViewById(R.id.txtObservacionesArma);
        txtDestinoArma = view.findViewById(R.id.txtDestinoArma);
        txtPrimerApellidoPropietarioArma = view.findViewById(R.id.txtPrimerApellidoPropietarioArma);
        txtSegundoApellidoPropietarioArma = view.findViewById(R.id.txtSegundoApellidoPropietarioArma);
        txtNombresPropietarioArma = view.findViewById(R.id.txtNombresPropietarioArma);
        txtPrimerApellidoTestigo1Arma = view.findViewById(R.id.txtPrimerApellidoTestigo1Arma);
        txtSegundoApellidoTestigo1Arma = view.findViewById(R.id.txtSegundoApellidoTestigo1Arma);
        txtNombresTestigo1Arma = view.findViewById(R.id.txtNombresTestigo1Arma);
        txtPrimerApellidoTestigo2Arma = view.findViewById(R.id.txtPrimerApellidoTestigo2Arma);
        txtSegundoApellidoTestigo2Arma = view.findViewById(R.id.txtSegundoApellidoTestigo2Arma);
        txtNombresTestigo2Arma = view.findViewById(R.id.txtNombresTestigo2Arma);
        rgAportacionInspeccionArmaFuego = view.findViewById(R.id.rgAportacionInspeccionArmaFuego);
        rgTipoArma = view.findViewById(R.id.rgTipoArma);
        spColorArmaDelictivo = view.findViewById(R.id.spColorArmaDelictivo);
        btnAgregarArma = view.findViewById(R.id.btnAgregarArma);
        ListCombos();
        //**************************************************************************************************//

        //************************************** ACCIONES DE LA VISTA **************************************//
        //************************************** OBJETOS *******************************************//

        //**************************************************************************************************//
        //Imagen que funciona para activar la grabación de voz
        imgMicrofonoObservacionesArma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMicrofonoObservacionesArma.setImageResource(R.drawable.ic_micro_press);
                iniciarEntradadeVoz();
            }
        });
        rgAportacionInspeccionArmaFuego.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rbAportacion) {
                    varAportacion = "SI";
                } else{
                    varAportacion = "NO";
                }
                if (checkedId == R.id.rbInspeccionLugar) {
                    varInspeccion = "INSPECCION LUGAR";
                }else if(checkedId == R.id.rbInspeccionPersona){
                    varInspeccion = "INSPECCION PERSONA";
                }else if(checkedId == R.id.rbInspeccionVehiculo){
                    varInspeccion = "INSPECCION VEHICULO";
                }
            }
        });

        rgTipoArma.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rbArmaCorta) {
                    varTipoArma = "ARMA CORTA";
                }else if(checkedId == R.id.rbArmaLarga){
                    varTipoArma = "ARMA LARGA";
                }
            }
        });

        btnAgregarArma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                insertArmasFuego();
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
        mViewModel = new ViewModelProvider(this).get(InventarioArmasObjetosViewModel.class);
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
                    String textoActual = txtObservacionesArma.getText().toString();
                    txtObservacionesArma.setText(textoActual+" " + result.get(0));
                }
                break;
            }

        }
        imgMicrofonoObservacionesArma.setImageResource(R.drawable.ic_micro);
    }

    /*****************************************************************************************************/
    /************************************* ARMAS *********************************************************/
    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertArmasFuego() {
        descripcionColor = (String) spColorArmaDelictivo.getSelectedItem();

        if(txtNombresPropietarioArma.getText().toString().equals("NA")){
            varRutaImagen = "NA";
        }else{
            varRutaImagen = "http://189.254.7.167/WebServiceIPH/FirmaRDDelictivo/"+"propietario_"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
        }
        if(txtNombresTestigo1Arma.getText().toString().equals("NA")){
            varRutaImagenT1 = "NA";
        }else{
            varRutaImagenT1 = "http://189.254.7.167/WebServiceIPH/FirmaRDDelictivo/"+"testigo1_"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
        }
        if(txtNombresTestigo2Arma.getText().toString().equals("NA")){
            varRutaImagenT2 = "NA";

        }else{
            varRutaImagenT2 = "http://189.254.7.167/WebServiceIPH/FirmaRDDelictivo/"+"testigo2_"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
        }

        ModeloArmasFuego_Delictivo modeloArmasFuego = new ModeloArmasFuego_Delictivo
                (cargarIdHechoDelictivo, varAportacion, varInspeccion, txtLugarEncontroArma.getText().toString(), varTipoArma,
                        txtCalibreArmaDelictivo.getText().toString(), descripcionColor, txtMatriculaArmaDelictivo.getText().toString(),
                        txtNoSerieArmaDelictivo.getText().toString(), txtObservacionesArma.getText().toString(), txtDestinoArma.getText().toString(),
                        txtPrimerApellidoPropietarioArma.getText().toString(), txtSegundoApellidoPropietarioArma.getText().toString(),
                        txtNombresPropietarioArma.getText().toString(),varRutaImagen, txtPrimerApellidoTestigo1Arma.getText().toString(),
                        txtSegundoApellidoTestigo1Arma.getText().toString(), txtNombresTestigo1Arma.getText().toString(),
                        varRutaImagenT1, txtPrimerApellidoTestigo2Arma.getText().toString(), txtSegundoApellidoTestigo2Arma.getText().toString(),
                        txtNombresTestigo2Arma.getText().toString(),varRutaImagenT2, cargarIdPoliciaPrimerRespondiente);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo",modeloArmasFuego.getIdHechoDelictivo())
                .add("Aportacion",modeloArmasFuego.getAportacion())
                .add("Inspeccion",modeloArmasFuego.getInspeccion())
                .add("LugarEncontro",modeloArmasFuego.getLugarEncontro())
                .add("TipoArma",modeloArmasFuego.getTipoArma())
                .add("Calibre",modeloArmasFuego.getCalibre())
                .add("Color",modeloArmasFuego.getColor())
                .add("Matricula",modeloArmasFuego.getMatricula())
                .add("NumSerie",modeloArmasFuego.getNumSerie())
                .add("Observaciones",modeloArmasFuego.getObservaciones())
                .add("Destino",modeloArmasFuego.getDestino())
                .add("APEncontro",modeloArmasFuego.getAPEncontro())
                .add("AMEncontro",modeloArmasFuego.getAMEncontro())
                .add("NombreEncontro",modeloArmasFuego.getNombreEncontro())
                .add("RutaFirmaEncontro",modeloArmasFuego.getRutaFirmaEncontro())
                .add("APTestigoUno",modeloArmasFuego.getAPTestigoUno())
                .add("AMTestigoUno",modeloArmasFuego.getAMTestigoUno())
                .add("NombreTestigoUno",modeloArmasFuego.getNombreTestigoUno())
                .add("RutaFirmaTestigoUno",modeloArmasFuego.getRutaFirmaTestigoUno())
                .add("APTestigoDos",modeloArmasFuego.getAPTestigoDos())
                .add("AMTestigoDos",modeloArmasFuego.getAMTestigoDos())
                .add("NombreTestigoDos",modeloArmasFuego.getNombreTestigoDos())
                .add("RutaFirmaTestigoDos",modeloArmasFuego.getRutaFirmaTestigoDos())
                .add("IdPoliciaPrimerRespondiente",modeloArmasFuego.getIdPoliciaPrimerRespondiente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDArmasFuego/")
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
                    InventarioArmasObjetos.this.getActivity().runOnUiThread(new Runnable() {
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


    /*****************************************************************************************************/
    /************************************ OBJETOS *******************************************************/



    //*****************************************************************************************************//
    public void random(){
        Random random = new Random();
        numberRandom = random.nextInt(9000)*99;
        randomUrlImagen = numberRandom;
    }
    private void ListCombos() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllColores();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE COLORES");
            ArrayAdapter<String> adapterColores = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spColorArmaDelictivo.setAdapter(adapterColores);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON COLORES ACTIVOS", Toast.LENGTH_LONG).show();
        }
    }
    public void cargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdPoliciaPrimerRespondiente = share.getString("Usuario", "SIN INFORMACION");
        cargarIdHechoDelictivo = share.getString("IDHECHODELICTIVO", "SIN INFORMACION");
    }

}