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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import mx.ssp.iph.delictivo.model.ModeloEntrevistas;
import mx.ssp.iph.delictivo.viewModel.EntrevistasDelictivoViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class EntrevistasDelictivo extends Fragment {

    private EntrevistasDelictivoViewModel mViewModel;
    private Funciones funciones;
    private EditText txtFechaEntrevista,txtHoraEntrevista,txtFechaNacimientoEntrevistado,txtEntrevista;
    ImageView imgMicrofonoEntrevista,imgFirmaEntrevistado,imgFirmaDerechosVictimaDelictivo,btnGuardarEntrevista;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    Spinner spNacionalidadEntrevistado,spGeneroEntrevistado,spTipoDocumentoEntrevistado,spMunicipioEntrevistado;
    RadioGroup rgReservarDatos, rgCalidadEntrevistado, rgTrasladoPersonaEntrevistada, rgLugarTrasladoEntrevista, rgInformeDerechoVictimaDelictivo;
    RadioButton rbNoReservarDatos, rbSiReservarDatos, rbCalidadVictima, rbCalidadDenunciante, rbCalidadTestigo, rbNoTrasladoPersonaEntrevistada,
            rbSiTrasladoPersonaEntrevistada, rbLugarTrasladoEntrevistadoFiscaliaAgencia, rbLugarTrasladoEntrevistadoHospital,
            rbLugarTrasladoEntrevistadoOtraDependencia, rbNoInformeDerechoVictimaDelictivo, rbSiInformeDerechoVictimaDelictivo;
    EditText txtPrimerApellidoEntrevistado,txtSegundoApellidoEntrevistado,txtNombresEntrevistado,txtEdadEntrevistado,txtNumeroIdentificacionEntrevistado,
            txtTelefonoEntrevistado,txtCorreoEntrevistado,txtEntidadEntrevistado,txtColoniaEntrevistado,txtCalleEntrevistado,txtNumeroExteriorEntrevistado,
            txtNumeroInteriorEntrevistado,txtCodigoPostalEntrevistado,txtReferenciasdelLugarEntrevistado,txtCualLugarTrasladoEntrevista;
    TextView lblFirmadelEntrevistadoOculto,lblFirmaEntrevistaOculto;
    SharedPreferences share;
    String cargarIdPoliciaPrimerRespondiente,cargarIdHechoDelictivo,varReservarDatos,varCalidadEntrevistado,
            descNacionalidadEntrevista,descGeneroEntrevista,descTipoDocumentoEntrevista,descMunicipioEntrevista,varRutaFirmaEntrevistado,
            varRutaFirmaDerechosEntrevistado,varTrasladoCanalizacion,varLugarTraslado,cadenaPersona;
    int numberRandom,randomUrlImagen;

    public static EntrevistasDelictivo newInstance() {
        return new EntrevistasDelictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entrevistas_delictivo_fragment, container, false);
        /************************************************************************************/
        funciones = new Funciones();
        funciones.CambiarTituloSeccionesDelictivo("ANEXO E. ENTREVISTAS",getContext(),getActivity());

        //************************************** ACCIONES DE LA VISTA **************************************//
        //Botones Imagen
        cargarDatos();
        random();
        imgMicrofonoEntrevista = view.findViewById(R.id.imgMicrofonoEntrevista);
        imgFirmaEntrevistado = view.findViewById(R.id.imgFirmaEntrevistado);
        imgFirmaDerechosVictimaDelictivo = view.findViewById(R.id.imgFirmaDerechosVictimaDelictivo);
        spNacionalidadEntrevistado = view.findViewById(R.id.spNacionalidadEntrevistado);
        spGeneroEntrevistado = view.findViewById(R.id.spGeneroEntrevistado);
        spTipoDocumentoEntrevistado = view.findViewById(R.id.spTipoDocumentoEntrevistado);
        spMunicipioEntrevistado = view.findViewById(R.id.spMunicipioEntrevistado);

        rgReservarDatos = view.findViewById(R.id.rgReservarDatos);
        rgCalidadEntrevistado = view.findViewById(R.id.rgCalidadEntrevistado);
        rgTrasladoPersonaEntrevistada = view.findViewById(R.id.rgTrasladoPersonaEntrevistada);
        rgLugarTrasladoEntrevista = view.findViewById(R.id.rgLugarTrasladoEntrevista);
        rgInformeDerechoVictimaDelictivo = view.findViewById(R.id.rgInformeDerechoVictimaDelictivo);

        rbNoReservarDatos = view.findViewById(R.id.rbNoReservarDatos);
        rbSiReservarDatos = view.findViewById(R.id.rbSiReservarDatos);
        rbCalidadVictima = view.findViewById(R.id.rbCalidadVictima);
        rbCalidadDenunciante = view.findViewById(R.id.rbCalidadDenunciante);
        rbCalidadTestigo = view.findViewById(R.id.rbCalidadTestigo);
        rbNoTrasladoPersonaEntrevistada = view.findViewById(R.id.rbNoTrasladoPersonaEntrevistada);
        rbSiTrasladoPersonaEntrevistada = view.findViewById(R.id.rbSiTrasladoPersonaEntrevistada);
        rbLugarTrasladoEntrevistadoFiscaliaAgencia = view.findViewById(R.id.rbLugarTrasladoEntrevistadoFiscaliaAgencia);
        rbLugarTrasladoEntrevistadoHospital = view.findViewById(R.id.rbLugarTrasladoEntrevistadoHospital);
        rbLugarTrasladoEntrevistadoOtraDependencia = view.findViewById(R.id.rbLugarTrasladoEntrevistadoOtraDependencia);
        rbNoInformeDerechoVictimaDelictivo = view.findViewById(R.id.rbNoInformeDerechoVictimaDelictivo);
        rbSiInformeDerechoVictimaDelictivo = view.findViewById(R.id.rbSiInformeDerechoVictimaDelictivo);

        txtPrimerApellidoEntrevistado = view.findViewById(R.id.txtPrimerApellidoEntrevistado);
        txtSegundoApellidoEntrevistado = view.findViewById(R.id.txtSegundoApellidoEntrevistado);
        txtNombresEntrevistado = view.findViewById(R.id.txtNombresEntrevistado);
        txtEdadEntrevistado = view.findViewById(R.id.txtEdadEntrevistado);
        txtNumeroIdentificacionEntrevistado = view.findViewById(R.id.txtNumeroIdentificacionEntrevistado);
        txtTelefonoEntrevistado = view.findViewById(R.id.txtTelefonoEntrevistado);
        txtCorreoEntrevistado = view.findViewById(R.id.txtCorreoEntrevistado);
        txtEntidadEntrevistado = view.findViewById(R.id.txtEntidadEntrevistado);
        txtColoniaEntrevistado = view.findViewById(R.id.txtColoniaEntrevistado);
        txtCalleEntrevistado = view.findViewById(R.id.txtCalleEntrevistado);
        txtNumeroExteriorEntrevistado = view.findViewById(R.id.txtNumeroExteriorEntrevistado);
        txtNumeroInteriorEntrevistado = view.findViewById(R.id.txtNumeroInteriorEntrevistado);
        txtCodigoPostalEntrevistado = view.findViewById(R.id.txtCodigoPostalEntrevistado);
        txtReferenciasdelLugarEntrevistado = view.findViewById(R.id.txtReferenciasdelLugarEntrevistado);
        txtCualLugarTrasladoEntrevista = view.findViewById(R.id.txtCualLugarTrasladoEntrevista);
        lblFirmadelEntrevistadoOculto = view.findViewById(R.id.lblFirmadelEntrevistadoOculto);
        lblFirmaEntrevistaOculto = view.findViewById(R.id.lblFirmaEntrevistaOculto);
        btnGuardarEntrevista = view.findViewById(R.id.btnGuardarEntrevista);
        //EditText
        txtEntrevista = view.findViewById(R.id.txtEntrevista);
        txtFechaEntrevista = view.findViewById(R.id.txtFechaEntrevista);
        txtHoraEntrevista = view.findViewById(R.id.txtHoraEntrevista);
        txtFechaNacimientoEntrevistado = view.findViewById(R.id.txtFechaNacimientoEntrevistado);
        ListCombos();


        //Imagen que funciona para activar la grabación de voz
        imgMicrofonoEntrevista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMicrofonoEntrevista.setImageResource(R.drawable.ic_micro_press);
                iniciarEntradadeVoz();
            }
        });

        //***************** FECHA  **************************//
        txtFechaEntrevista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaEntrevista,getContext(),getActivity());
            }
        });

        //***************** FECHA  **************************//
        txtFechaNacimientoEntrevistado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaNacimientoEntrevistado,getContext(),getActivity());
            }
        });

        //***************** HORA **************************//
        txtHoraEntrevista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txtHoraEntrevista,getContext(),getActivity());
            }
        });

        rgReservarDatos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiInformeDerechoDetencionesDelictivo) {
                    varReservarDatos = "SI";
                } else if (checkedId == R.id.rbNoReservarDatos) {
                    varReservarDatos = "NO";
                }

            }
        });

        rgCalidadEntrevistado.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbCalidadVictima) {
                    varCalidadEntrevistado = "VICTIMA-OFENDIDO";
                } else if (checkedId == R.id.rbCalidadDenunciante) {
                    varCalidadEntrevistado = "DENUNCIANTE";
                }else if (checkedId == R.id.rbCalidadTestigo){
                    varCalidadEntrevistado = "TESTIGO";
                }
            }
        });

        rgTrasladoPersonaEntrevistada.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiTrasladoPersonaEntrevistada) {
                    varTrasladoCanalizacion = "SI";
                } else if (checkedId == R.id.rbNoTrasladoPersonaEntrevistada) {
                    varTrasladoCanalizacion = "NO";
                }

            }
        });

        rgLugarTrasladoEntrevista.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbLugarTrasladoEntrevistadoFiscaliaAgencia) {
                    varLugarTraslado = "FISCALIA/AGENCIA";
                } else if (checkedId == R.id.rbLugarTrasladoEntrevistadoHospital) {
                    varLugarTraslado = "HOSPITAL";
                }else if (checkedId == R.id.rbLugarTrasladoEntrevistadoOtraDependencia){
                    varLugarTraslado = "OTRA DEPENDENCIA";
                }
            }
        });

        rgInformeDerechoVictimaDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiInformeDerechoVictimaDelictivo) {
                    cadenaPersona = "firma_derechos_";
                    varRutaFirmaDerechosEntrevistado = "http://189.254.7.167/WebServiceIPH/FirmaEntrevista/"+cadenaPersona+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
                } else if (checkedId == R.id.rbNoInformeDerechoVictimaDelictivo) {
                    varRutaFirmaDerechosEntrevistado = "NO";
                }

            }
        });

        btnGuardarEntrevista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();

                //*****************INSERTAR LAS IMAGENES*****************************//
                if(lblFirmadelEntrevistadoOculto.getText().toString().isEmpty()){
                    cadenaPersona = "firma_entrevistado_";
                    varRutaFirmaEntrevistado = "NP";
                }else{
                    cadenaPersona = "firma_entrevistado_";
                    varRutaFirmaEntrevistado = "http://189.254.7.167/WebServiceIPH/FirmaEntrevista/"+cadenaPersona+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
                }

                insertEntrevistas();
            }
        });



        /****************************************************************************************/
        return view;
    }


public void PrimeraValidacion(){
        if(rbNoReservarDatos.isChecked() || rbSiReservarDatos.isChecked()){
            if (txtFechaEntrevista.getText().toString().length() >= 3 && txtHoraEntrevista.getText().toString().length() >= 3){
                //Demás validaciones

            }

            else{
                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFIQUE FECHA Y HORA", Toast.LENGTH_SHORT).show();
            }

        }

        else{
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFIQUE SI DESEA RESERVAR DATOS", Toast.LENGTH_SHORT).show();
        }
    }


    public void SegundaValidacion(){
        if(rbCalidadVictima.isChecked() || rbCalidadDenunciante.isChecked() || rbCalidadTestigo.isChecked()){


        }

        else{
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFIQUE EN QUE CALIDAD ESTÁ EL ENTREVISTADO", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EntrevistasDelictivoViewModel.class);
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

                    String textoActual = txtEntrevista.getText().toString();
                    txtEntrevista.setText(textoActual+" " + result.get(0));

                }
                break;
            }
        }
        imgMicrofonoEntrevista.setImageResource(R.drawable.ic_micro);
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

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertEntrevistas() {
        DataHelper dataHelper = new DataHelper(getContext());

        descGeneroEntrevista = (String) spGeneroEntrevistado.getSelectedItem();
        int idDescGeneroEntrevista = dataHelper.getIdSexo(descGeneroEntrevista);
        String idGeneroEntrevista = String.valueOf(idDescGeneroEntrevista);

        descNacionalidadEntrevista = (String) spNacionalidadEntrevistado.getSelectedItem();
        int idDescNacionalidadEntrevista = dataHelper.getIdNacionalidad(descNacionalidadEntrevista);
        String idNacionalidadEntrevista = String.valueOf(idDescNacionalidadEntrevista);

        descTipoDocumentoEntrevista = (String) spTipoDocumentoEntrevistado.getSelectedItem();
        int idDescTipoDocumentoEntrevista = dataHelper.getIdIdentificacion(descTipoDocumentoEntrevista);
        String idTipoDocumentoEntrevista = String.valueOf(idDescTipoDocumentoEntrevista);

        descMunicipioEntrevista = (String) spMunicipioEntrevistado.getSelectedItem();
        String idDescMunicipioPersonaDetenidaHD = dataHelper.getIdMunicipio(descMunicipioEntrevista);
        String idMunicipioEntrevista = String.valueOf(idDescMunicipioPersonaDetenidaHD);

        ModeloEntrevistas modeloEntrevistas = new ModeloEntrevistas
                (cargarIdHechoDelictivo, varReservarDatos, txtFechaEntrevista.getText().toString(), txtHoraEntrevista.getText().toString(),
                        txtPrimerApellidoEntrevistado.getText().toString(), txtSegundoApellidoEntrevistado.getText().toString(), txtNombresEntrevistado.getText().toString(),
                        varCalidadEntrevistado, idNacionalidadEntrevista, idGeneroEntrevista, txtFechaNacimientoEntrevistado.getText().toString(),
                        txtEdadEntrevistado.getText().toString(), idTipoDocumentoEntrevista, "NA", txtNumeroIdentificacionEntrevistado.getText().toString(),
                        txtTelefonoEntrevistado.getText().toString(), txtCorreoEntrevistado.getText().toString(), "12", idMunicipioEntrevista,
                        txtColoniaEntrevistado.getText().toString(), txtCalleEntrevistado.getText().toString(), txtNumeroExteriorEntrevistado.getText().toString(), txtNumeroInteriorEntrevistado.getText().toString(),
                        txtCodigoPostalEntrevistado.getText().toString(), txtReferenciasdelLugarEntrevistado.getText().toString(), txtEntrevista.getText().toString(), varRutaFirmaEntrevistado,
                        varTrasladoCanalizacion, varLugarTraslado, txtCualLugarTrasladoEntrevista.getText().toString(),
                        varRutaFirmaDerechosEntrevistado, cargarIdPoliciaPrimerRespondiente);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo", modeloEntrevistas.getIdHechoDelictivo())
                .add("ReservarDatos", modeloEntrevistas.getReservarDatos())
                .add("Fecha", modeloEntrevistas.getFecha())
                .add("Hora", modeloEntrevistas.getHora())
                .add("APEntrevistado", modeloEntrevistas.getAPEntrevistado())
                .add("AMEntrevistado", modeloEntrevistas.getAMEntrevistado())
                .add("NombreEntrevistado", modeloEntrevistas.getNombreEntrevistado())
                .add("Calidad", modeloEntrevistas.getCalidad())
                .add("IdNacionalidad", modeloEntrevistas.getIdNacionalidad())
                .add("IdSexo", modeloEntrevistas.getIdSexo())
                .add("FechaNacimiento", modeloEntrevistas.getFechaNacimiento())
                .add("Edad", modeloEntrevistas.getEdad())
                .add("IdIdentificacion", modeloEntrevistas.getIdIdentificacion())
                .add("IdentificacionOtro", modeloEntrevistas.getIdentificacionOtro())
                .add("NumIdentificacion", modeloEntrevistas.getNumIdentificacion())
                .add("Telefono", modeloEntrevistas.getTelefono())
                .add("Correo", modeloEntrevistas.getCorreo())
                .add("IdEntidadFederativa", modeloEntrevistas.getIdEntidadFederativa())
                .add("IdMunicipio", modeloEntrevistas.getIdMunicipio())
                .add("ColoniaLocalidad", modeloEntrevistas.getColoniaLocalidad())
                .add("CalleTramo", modeloEntrevistas.getCalleTramo())
                .add("NoExterior", modeloEntrevistas.getNoExterior())
                .add("NoInterior", modeloEntrevistas.getNoInterior())
                .add("Cp", modeloEntrevistas.getCp())
                .add("Referencia", modeloEntrevistas.getReferencia())
                .add("RelatoEntrevista", modeloEntrevistas.getRelatoEntrevista())
                .add("RutaFirmaEntrevistado", modeloEntrevistas.getRutaFirmaEntrevistado())
                .add("TrasladoCanalizacion", modeloEntrevistas.getTrasladoCanalizacion())
                .add("LugarTraslado", modeloEntrevistas.getLugarTraslado())
                .add("DescLugarTraslado", modeloEntrevistas.getDescLugarTraslado())
                .add("RutaFirmaVictimaOfendidos", modeloEntrevistas.getRutaFirmaVictimaOfendidos())
                .add("IdPoliciaPrimerRespondiente", modeloEntrevistas.getIdPoliciaPrimerRespondiente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDEntrevistas/")
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
                    EntrevistasDelictivo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                //insertLugarDetencionesDelictivo();
                                //insertImagen();

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
        ArrayList<String> listMuniEntrevista = dataHelper.getAllMunicipios();
        ArrayList<String> listNacion = dataHelper.getAllNacionalidad();
        ArrayList<String> listSexo = dataHelper.getAllSexo();
        ArrayList<String> listIdentificacion = dataHelper.getAllIdentificacion();

        if (listMuniEntrevista.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MUNICIPIOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listMuniEntrevista);
            spMunicipioEntrevistado.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON MUNICIPIOS ACTIVOS.", Toast.LENGTH_LONG).show();
        }
        if (listNacion.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE NACIONALIDAD");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listNacion);
            spNacionalidadEntrevistado.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON NACIONES ACTIVAS.", Toast.LENGTH_LONG).show();
        }
        if (listSexo.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE SEXO");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listSexo);
            spGeneroEntrevistado.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON SEXOS ACTIVOS.", Toast.LENGTH_LONG).show();
        }
        if (listIdentificacion.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE IDENTIFICACIONES");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listIdentificacion);
            spTipoDocumentoEntrevistado.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON IDENTIFICACIONES ACTIVAS.", Toast.LENGTH_LONG).show();
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