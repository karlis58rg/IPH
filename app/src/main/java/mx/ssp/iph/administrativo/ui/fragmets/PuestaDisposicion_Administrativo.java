package mx.ssp.iph.administrativo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.lang.Object;
import java.util.Date;
import java.util.Random;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModeloPuestaDisposicion_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloRecibeDisposicion_Administrativo;
import mx.ssp.iph.administrativo.viewModel.PuestaDisposicionAdministrativoViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorFirmaPDisposicion;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PuestaDisposicion_Administrativo extends Fragment {

    private PuestaDisposicionAdministrativoViewModel mViewModel;
    private ImageView imgFirmaAutoridadAdministrativo;
    Integer aux1, aux2;
    CheckBox chDetencionesAnexoAAdministrativo,chDetencionesAnexoBAdministrativo,chSinAnexosAdministrativo,chNoAplicaUnidadDeArriboAdministrativo;
    RadioGroup rgPrimerRespondienteAdministrativo;
    EditText txtFolioInternoAdministrativo,txtFolioSistemaAdministrativo,txtNoReferenciaAdministrativo,
            txtFechaPuestaDisposicionAdministrativo,txthoraPuestaDisposicionAdministrativo,txtNoExpedienteAdmministrativo,txtFiscaliaAutoridadAdministrativo;
    TextView lblFirmaAutoridadRealizadaAdministrativo,lblFirmaOcultaAutoridadBase64;
    ImageView btnGuardarPuestaDisposicioAdministrativo,imgFirmaAutoridadAdministrativoMiniatura;
    SharedPreferences share;
    String cargarIdFaltaAdmin,cargarUsuario,noReferencia,noExpediente,firmaURLServer = "http://189.254.7.167/WebServiceIPH/Firma/SINFIRMA.jpg",
            varAnexoA = "NO",varNoDetenidos = "000",varAnexoB = "NO",varNoVehiculos = "000",varSinAnexos = "NO",
            varNoAplicaUnidad,descUnidad,descAutoridad,descCargo,
            edo = "",inst = "",gob = "",mpio = "",fecha = "",
            dia = "01",mes = "01",anio = "2021",tiempo = "",hora = "",minutos = "",respuestaJson;
    int numberRandom,randomUrlImagen;
    Funciones funciones;
    Spinner txtCargoAdministrativo,txtUnidadDeArriboAdministrativo,
            spDetencionesAnexoAAdministrativo,spDetencionesAnexoBAdministrativo,txtAdscripcionAdministrativo;
    ViewGroup linearAnexos, lyAnexosADetencionAdministrativo, lyDetencionesAnexoAAdministrativo, lyAnexosEDescripcionVehiculosAdministrativo, lyDetencionesAnexoBAdministrativo, lySinAnexosAdministrativo, lySinEntregaAnexosAdministrativo;
    public static PuestaDisposicion_Administrativo newInstance() {
        return new PuestaDisposicion_Administrativo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.puesta_disposicion_administrativo_fragment, container, false);

        //************************************** ACCIONES DE LA VISTA **************************************//
        funciones = new Funciones();
        cargarFolios();
        random();
        txtFechaPuestaDisposicionAdministrativo = root.findViewById(R.id.txtFechaPuestaDisposicionAdministrativo);
        txthoraPuestaDisposicionAdministrativo = root.findViewById(R.id.txthoraPuestaDisposicionAdministrativo);
        txtNoExpedienteAdmministrativo = root.findViewById(R.id.txtNoExpedienteAdmministrativo);
        txtNoExpedienteAdmministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(25)});

        txtFolioInternoAdministrativo = root.findViewById(R.id.txtFolioInternoAdministrativo);
        txtFolioSistemaAdministrativo = root.findViewById(R.id.txtFolioSistemaAdministrativo);
        txtFolioSistemaAdministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(21)});
        txtNoReferenciaAdministrativo = root.findViewById(R.id.txtNoReferenciaAdministrativo);
        txtNoReferenciaAdministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(41)});

        txtUnidadDeArriboAdministrativo = root.findViewById(R.id.txtUnidadDeArriboAdministrativo);
        txtFiscaliaAutoridadAdministrativo = root.findViewById(R.id.txtFiscaliaAutoridadAdministrativo);
        txtAdscripcionAdministrativo = root.findViewById(R.id.txtAdscripcionAdministrativo);
        txtCargoAdministrativo = root.findViewById(R.id.txtCargoAdministrativo);
        spDetencionesAnexoAAdministrativo = root.findViewById(R.id.spDetencionesAnexoAAdministrativo);
        spDetencionesAnexoBAdministrativo = root.findViewById(R.id.spDetencionesAnexoBAdministrativo);
        lblFirmaAutoridadRealizadaAdministrativo  = root.findViewById(R.id.lblFirmaAutoridadRealizadaAdministrativo);
        btnGuardarPuestaDisposicioAdministrativo = root.findViewById(R.id.btnGuardarPuestaDisposicioAdministrativo);

        chDetencionesAnexoAAdministrativo = root.findViewById(R.id.chDetencionesAnexoAAdministrativo);
        chDetencionesAnexoBAdministrativo = root.findViewById(R.id.chDetencionesAnexoBAdministrativo);
        chSinAnexosAdministrativo = root.findViewById(R.id.chSinAnexosAdministrativo);
        chNoAplicaUnidadDeArriboAdministrativo = root.findViewById(R.id.chNoAplicaUnidadDeArriboAdministrativo);

        //rgPrimerRespondienteAdministrativo = root.findViewById(R.id.rgPrimerRespondienteAdministrativo);

        imgFirmaAutoridadAdministrativo = (ImageView) root.findViewById(R.id.imgFirmaAutoridadAdministrativo);
        imgFirmaAutoridadAdministrativoMiniatura = root.findViewById(R.id.imgFirmaAutoridadAdministrativoMiniatura);

        lblFirmaOcultaAutoridadBase64 = root.findViewById(R.id.lblFirmaOcultaAutoridadBase64);

        linearAnexos = root.findViewById(R.id.linearAnexos);
        lyAnexosADetencionAdministrativo = root.findViewById(R.id.lyAnexosADetencionAdministrativo);
        lyDetencionesAnexoAAdministrativo = root.findViewById(R.id.lyDetencionesAnexoAAdministrativo);
        lyAnexosEDescripcionVehiculosAdministrativo = root.findViewById(R.id.lyAnexosEDescripcionVehiculosAdministrativo);
        lyDetencionesAnexoBAdministrativo = root.findViewById(R.id.lyDetencionesAnexoBAdministrativo);
        lySinAnexosAdministrativo = root.findViewById(R.id.lySinAnexosAdministrativo);
        lySinEntregaAnexosAdministrativo = root.findViewById(R.id.lySinEntregaAnexosAdministrativo);

        txtFolioInternoAdministrativo.setText(cargarIdFaltaAdmin);
        txtFolioInternoAdministrativo.setEnabled(false);
        txtNoReferenciaAdministrativo.setEnabled(false);
        ListCombos();
        getNumReferencia();
        //Cambia el título de acuerdo a la sección seleccionada
        funciones.CambiarTituloSecciones("SECCIÓN 1: PUESTA A DISPOSICIÓN",getContext(),getActivity());

        //HABILITAR - DESHABILITAR EDITTEXT ANEXO A
        chDetencionesAnexoAAdministrativo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {

                if(chselect == true){
                    spDetencionesAnexoAAdministrativo.setVisibility(buttonView.VISIBLE);
                    spDetencionesAnexoAAdministrativo.setSelection(funciones.getIndexSpiner(spDetencionesAnexoAAdministrativo, "--Selecciona--"));

                } else if(chselect == false) {
                    spDetencionesAnexoAAdministrativo.setVisibility(buttonView.INVISIBLE);
                }
            }
        });

        //HABILITAR - DESHABILITAR EDITTEXT ANEXO B
        chDetencionesAnexoBAdministrativo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {

                if(chselect == true){
                    spDetencionesAnexoBAdministrativo.setVisibility(buttonView.VISIBLE);
                    spDetencionesAnexoBAdministrativo.setSelection(funciones.getIndexSpiner(spDetencionesAnexoBAdministrativo, "--Selecciona--"));
                } else if(chselect == false) {
                    spDetencionesAnexoBAdministrativo.setVisibility(buttonView.INVISIBLE);
                }
            }
        });

        //HABILITAR - DESHABILITAR CHECKBOX ANEXOS
        chSinAnexosAdministrativo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {

                if(chselect == true){
                    chDetencionesAnexoAAdministrativo.setEnabled(false);
                    spDetencionesAnexoAAdministrativo.setEnabled(false);
                    chDetencionesAnexoBAdministrativo.setEnabled(false);
                    spDetencionesAnexoBAdministrativo.setEnabled(false);
                    chDetencionesAnexoAAdministrativo.setChecked(false);
                    chDetencionesAnexoBAdministrativo.setChecked(false);
                    varNoDetenidos = "000";
                    varNoVehiculos = "000";
                } else if(chselect == false) {
                    chDetencionesAnexoAAdministrativo.setEnabled(true);
                    spDetencionesAnexoAAdministrativo.setEnabled(true);
                    chDetencionesAnexoBAdministrativo.setEnabled(true);
                    spDetencionesAnexoBAdministrativo.setEnabled(true);
                }
            }
        });

        //DESHABILITAR SPPINER UNIDAD DE ARRIBO
        chNoAplicaUnidadDeArriboAdministrativo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {

                if(chselect == true){
                    txtUnidadDeArriboAdministrativo.setEnabled(false);
                    varNoAplicaUnidad = "NA";
                } else if(chselect == false) {
                    txtUnidadDeArriboAdministrativo.setEnabled(true);
                }
            }
        });
        //***************** Cargar Datos si es que existen  **************************//
        CargarDatos();

        imgFirmaAutoridadAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirmaPDisposicion dialog = new ContenedorFirmaPDisposicion(R.id.lblFirmaAutoridadRealizadaAdministrativo,R.id.lblFirmaOcultaAutoridadBase64,R.id.imgFirmaAutoridadAdministrativoMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });

        txtFechaPuestaDisposicionAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaPuestaDisposicionAdministrativo,getContext(),getActivity());
            }
        });
        txthoraPuestaDisposicionAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txthoraPuestaDisposicionAdministrativo,getContext(),getActivity());
            }
        });


        /*
        rgPrimerRespondienteAdministrativo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiPrimerRespondienteAdministrativo) {
                    varRBPrimerRespondiente = "SI";
                } else if (checkedId == R.id.rbNoPrimerRespondienteAdministrativo) {
                    varRBPrimerRespondiente = "NO";
                }

            }
        });*/


        //VALOR DE LISTA ANEXO A
        spDetencionesAnexoAAdministrativo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                Object item = parent.getItemIdAtPosition(pos);

                int i = Integer.parseInt(item.toString()) + 1;

                aux1 = i;

                //Toast.makeText(getContext(), "" + i, Toast.LENGTH_LONG).show();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //VALOR DE LISTA ANEXO B
        spDetencionesAnexoBAdministrativo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                Object item = parent.getItemIdAtPosition(pos);

                int j = Integer.parseInt(item.toString()) + 1;

                aux2 = j;

                //Toast.makeText(getContext(), "" + j, Toast.LENGTH_LONG).show();

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        btnGuardarPuestaDisposicioAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(lblFirmaOcultaAutoridadBase64.getText().toString().isEmpty()){
                        Toast.makeText(getContext(), "LO SENTIMOS, SU FIRMA ES NECESARIA PARA PODER CONTINUAR", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS ", Toast.LENGTH_LONG).show();
                        updatePuestaDisposicion();
                    }

                if(chDetencionesAnexoAAdministrativo.isChecked()){
                    if(aux1 != 1){

                        if(chDetencionesAnexoBAdministrativo.isChecked()){
                            if(aux2 != 1){

                                // GUARDA DATO
                                Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_LONG).show();
                                updatePuestaDisposicion();

                            } else {

                                Toast.makeText(getContext(), "SELECCIONA CUANTOS ANEXOS “B” SE ENTREGAN", Toast.LENGTH_LONG).show();
                                linearAnexos.requestFocus();
                                lyDetencionesAnexoBAdministrativo.requestFocus();

                            }

                        } else{

                            // GUARDA DATO
                            Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_LONG).show();
                            updatePuestaDisposicion();

                            }

                    } else {
                        Toast.makeText(getContext(), "SELECCIONA CUANTOS ANEXOS “A” SE ENTREGAN", Toast.LENGTH_LONG).show();
                        linearAnexos.requestFocus();
                        lyDetencionesAnexoAAdministrativo.requestFocus();
                    }

                } else if (chDetencionesAnexoBAdministrativo.isChecked()){
                    if(aux2 != 1){

                        // GUARDA DATO
                        Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_LONG).show();
                        updatePuestaDisposicion();

                    } else {

                        Toast.makeText(getContext(), "SELECCIONA CUANTOS ANEXOS “B” SE ENTREGAN", Toast.LENGTH_LONG).show();
                        linearAnexos.requestFocus();
                        lyDetencionesAnexoBAdministrativo.requestFocus();

                    }


                } else if(chSinAnexosAdministrativo.isChecked()){

                    // GUARDA DATO
                    Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_LONG).show();
                    updatePuestaDisposicion();

                } else {
                    Toast.makeText(getContext(), "PARA GUARDAR ESPECIFICA SI HAY ANEXOS Y EN SU CASO CUANTOS", Toast.LENGTH_LONG).show();
                    linearAnexos.requestFocus();
                    lyAnexosADetencionAdministrativo.requestFocus();
                    lyAnexosEDescripcionVehiculosAdministrativo.requestFocus();
                    lySinAnexosAdministrativo.requestFocus();
                    lySinEntregaAnexosAdministrativo.requestFocus();

                }


                //insertImagen();

            }
        });

        //****************************************************************************//
        return root;
    }

    public void getFirmaFromURL(){
        Picasso.get()
                .load(firmaURLServer)
                .into(imgFirmaAutoridadAdministrativoMiniatura);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PuestaDisposicionAdministrativoViewModel.class);
        // TODO: Use the ViewModel
    }
    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void updatePuestaDisposicion() {
        DataHelper dataHelper = new DataHelper(getContext());
        descUnidad = (String) txtUnidadDeArriboAdministrativo.getSelectedItem();

        if(chDetencionesAnexoAAdministrativo.isChecked()){
            varAnexoA = "SI";
            varNoDetenidos = (String) spDetencionesAnexoAAdministrativo.getSelectedItem();
        }else{
            varAnexoA = "NO";
            varNoDetenidos = "000";
        }
        if(chDetencionesAnexoBAdministrativo.isChecked()){
            varAnexoB = "SI";
            varNoVehiculos = (String) spDetencionesAnexoBAdministrativo.getSelectedItem();
        }else{

            varAnexoB = "NO";
            varNoVehiculos = "000";
        }
        if(chSinAnexosAdministrativo.isChecked()){
            varSinAnexos = "NA";
        }
        if(chNoAplicaUnidadDeArriboAdministrativo.isChecked()){
            txtUnidadDeArriboAdministrativo.setEnabled(false);
            varNoAplicaUnidad = "NA";
        }
        if(txtNoExpedienteAdmministrativo.getText().toString().isEmpty()){
            noExpediente = "SN";
        }else {
            noExpediente = txtNoExpedienteAdmministrativo.getText().toString();
        }

        ModeloPuestaDisposicion_Administrativo puestaDisposicion = new ModeloPuestaDisposicion_Administrativo
                (cargarIdFaltaAdmin,noReferencia,
                        txtFechaPuestaDisposicionAdministrativo.getText().toString(),
                        txthoraPuestaDisposicionAdministrativo.getText().toString(),
                        noExpediente);
        Log.i("fecha",puestaDisposicion.getFecha());
        Log.i("hora",puestaDisposicion.getHora());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin", puestaDisposicion.getIdFaltaAdmin())
                .add("NumReferencia", puestaDisposicion.getNumReferencia())
                .add("Fecha", puestaDisposicion.getFecha())
                .add("Hora", puestaDisposicion.getHora())
                .add("NumExpediente", puestaDisposicion.getNumExpediente())
                .add("Detenciones", varAnexoA)
                .add("NumDetenciones", varNoDetenidos)
                .add("Vehiculos", varAnexoB)
                .add("NumVehiculos", varNoVehiculos)
                .add("SinAnexos", varSinAnexos)
                .add("IdPoliciaPrimerRespondiente", cargarUsuario)
                .add("IdUnidad", descUnidad)
                .build();
        System.out.println(puestaDisposicion.getIdFaltaAdmin()+puestaDisposicion.getNumReferencia()+puestaDisposicion.getFecha()+puestaDisposicion.getHora()+
                puestaDisposicion.getNumExpediente()+varAnexoA+varNoDetenidos+varAnexoB+varNoVehiculos+varSinAnexos+cargarUsuario+descUnidad);
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/FaltaAdministrativa/")
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
                    PuestaDisposicion_Administrativo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO DE DISPOSICION SE ENVIO CORRECTAMENTE");
                                updateRecibeDisposicion();
                                //Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
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

    private void updateRecibeDisposicion() {
        DataHelper dataHelper = new DataHelper(getContext());
        descAutoridad = (String) txtAdscripcionAdministrativo.getSelectedItem();
        int idAdscripcion = dataHelper.getIdAutoridadAdmin(descAutoridad);
        String adscripcion = String.valueOf(idAdscripcion);

        descCargo = (String) txtCargoAdministrativo.getSelectedItem();
        int idCargo = dataHelper.getIdCargo(descCargo);
        String cargo = String.valueOf(idCargo);

        String urlImagen = "http://189.254.7.167/WebServiceIPH/Firma/"+cargarIdFaltaAdmin+randomUrlImagen+".jpg";

        ModeloRecibeDisposicion_Administrativo recibePuestaDisposicion = new ModeloRecibeDisposicion_Administrativo
                (cargarIdFaltaAdmin, adscripcion,cargo,txtFiscaliaAutoridadAdministrativo.getText().toString(),urlImagen);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin", recibePuestaDisposicion.getIdFaltaAdmin())
                .add("IdFiscaliaAutoridad", recibePuestaDisposicion.getIdFiscaliaAutoridad())
                .add("IdCargo", recibePuestaDisposicion.getIdCargo())
                .add("NomRecibePuestaDisp", recibePuestaDisposicion.getNomRecibePuestaDisp())
                .add("UrlFirma", recibePuestaDisposicion.getUrlFirma())

                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/RecibeDisposicionAdministrativa/")
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
                    PuestaDisposicion_Administrativo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO DE LA RECEPCION DE DISPOSICION SE ENVIO CORRECTAMENTE");
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
        ModeloRecibeDisposicion_Administrativo recibeDisposicion =
                new ModeloRecibeDisposicion_Administrativo(txtFiscaliaAutoridadAdministrativo.getText().toString());
        String cadena = lblFirmaOcultaAutoridadBase64.getText().toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Description", cargarIdFaltaAdmin+randomUrlImagen+".jpg")
                .add("ImageData", cadena)
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/MultimediaFirma")
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
                    PuestaDisposicion_Administrativo.this.getActivity().runOnUiThread(new Runnable() {
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

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarPuestaDisposicion() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/FaltaAdministrativa?folioInterno="+cargarIdFaltaAdmin)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR SECCIÓN 1, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                    //Sin Información. Todos los campos vacíos.
                                }
                                else{
                                    //Deserializa el json y colcoa el dato correspondiente en cada campo si existe
                                    try {
                                        JSONObject jsonjObject = new JSONObject(ArregloJson);
                                        txtNoReferenciaAdministrativo.setText((jsonjObject.getString("NumReferencia")).equals("null")?"":jsonjObject.getString("NumReferencia"));
                                        String[] Fecha = (jsonjObject.getString("Fecha").replace("-","/")).split("T");
                                        txtFechaPuestaDisposicionAdministrativo.setText((jsonjObject.getString("Fecha")).equals("null")?"":Fecha[0]);
                                        txthoraPuestaDisposicionAdministrativo.setText((jsonjObject.getString("Hora")).equals("null")?"":jsonjObject.getString("Hora"));
                                        txtNoExpedienteAdmministrativo.setText((jsonjObject.getString("NumExpediente")).equals("null")?"":jsonjObject.getString("NumExpediente"));


                                        if(jsonjObject.getString("SinAnexos").equals("NO")){
                                            //Anexos Checks
                                                if(jsonjObject.getString("Detenciones").equals("SI")){
                                                    chDetencionesAnexoAAdministrativo.setChecked(true);
                                                    spDetencionesAnexoAAdministrativo.setSelection(funciones.getIndexSpiner(spDetencionesAnexoAAdministrativo, jsonjObject.getString("NumDetenciones")));
                                                }

                                                if(jsonjObject.getString("Vehiculos").equals("SI")){
                                                    chDetencionesAnexoBAdministrativo.setChecked(true);
                                                    spDetencionesAnexoBAdministrativo.setSelection(funciones.getIndexSpiner(spDetencionesAnexoBAdministrativo, jsonjObject.getString("NumVehiculos")));
                                                }
                                        }
                                        else{
                                            chDetencionesAnexoAAdministrativo.setEnabled(false);
                                            chDetencionesAnexoBAdministrativo.setEnabled(false);
                                        }
                                        //Coloca Unidad. si es Falso la unidad no aplica. De lo contrario Coloca el valor
                                        if (jsonjObject.getString("Unidad").equals("false"))
                                        {
                                            chNoAplicaUnidadDeArriboAdministrativo.setChecked(true);
                                        }else
                                        {
                                            chNoAplicaUnidadDeArriboAdministrativo.setChecked(false);
                                            txtUnidadDeArriboAdministrativo.setSelection(funciones.getIndexSpiner(txtUnidadDeArriboAdministrativo, jsonjObject.getString("Unidad")));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        //Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON1. LLENE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                                    }
                                }
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

    private void cargarPuestaDisposicionFiscalia() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/RecibeDisposicionAdministrativa?folioInterno="+cargarIdFaltaAdmin)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR SECCIÓN 1 FISCALÍA, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                    //Sin Información. Todos los campos vacíos.

                                }
                                else{
                                    //Deserializa el json y colcoa el dato correspondiente en cada campo si existe
                                    try {
                                        JSONObject jsonjObject = new JSONObject(ArregloJson);

                                        //txtPrimerApellidoAdministrativo.setText((jsonjObject.getString("APRecibePuestaDisp")).equals("null")?"":jsonjObject.getString("APRecibePuestaDisp"));
                                        //txtSegundoApellidoAdministrativo.setText((jsonjObject.getString("AMRecibePuestaDisp")).equals("null")?"":jsonjObject.getString("AMRecibePuestaDisp"));
                                        //txtNombresAdministrativo.setText((jsonjObject.getString("NomRecibePuestaDisp")).equals("null")?"":jsonjObject.getString("NomRecibePuestaDisp"));
                                        txtFiscaliaAutoridadAdministrativo.setText((jsonjObject.getString("NomRecibePuestaDisp")).equals("null")?"":jsonjObject.getString("NomRecibePuestaDisp"));
                                        lblFirmaAutoridadRealizadaAdministrativo.setText((jsonjObject.getString("UrlFirma")).equals("null")?"":"FIRMA CORRECTA");
                                        firmaURLServer = (jsonjObject.getString("UrlFirma").equals("null")?firmaURLServer:jsonjObject.getString("UrlFirma"));
                                        getFirmaFromURL();
                                        //Llenar spiners
                                        txtAdscripcionAdministrativo.setSelection(funciones.getIndexSpiner(txtAdscripcionAdministrativo, jsonjObject.getString("IdFiscaliaAutoridad")));
                                        txtCargoAdministrativo.setSelection(funciones.getIndexSpiner(txtCargoAdministrativo, jsonjObject.getString("IdCargo")));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON2. LLENE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                                    }
                                }
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
    //***************** GET A LA BD MEDIANTE EL WS **************************//
    private void getNumReferencia() {
        //*************** FECHA **********************//
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        fecha = dateFormat.format(date);
        String [] partsFecha = fecha.split("/");
        anio = partsFecha[0];
        mes = partsFecha[1];
        dia = partsFecha[2];
        //*************** HORA **********************//
        Date time = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        tiempo = timeFormat.format(time);
        String[] partsTiempo = tiempo.split(":");
        hora = partsTiempo[0];
        minutos = partsTiempo[1];
        /******************************************************/
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/FaltaAdministrativa?usuario="+cargarUsuario)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getActivity(),"ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    PuestaDisposicion_Administrativo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "FALSE";
                                if(myResponse.equals(respuestaJson)){
                                    Toast.makeText(getActivity(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                }else{
                                    JSONObject jObj = null;
                                    jObj = new JSONObject(""+myResponse+"");
                                    edo = jObj.getString("IdEntidadFederativa");
                                    inst = jObj.getString("IdInstitucion");
                                    gob = jObj.getString("IdGobierno");
                                    mpio = jObj.getString("IdMunicipio");
                                    noReferencia = edo+inst+gob+mpio+dia+mes+anio+hora+minutos;
                                    txtNoReferenciaAdministrativo.setText(edo+"|"+inst+"|"+gob+"|"+mpio+"|"+dia+"|"+mes+"|"+anio+"|"+hora+"|"+minutos);
                                    Log.i("HERE", ""+jObj);
                                }

                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }

                    });
                }
            }

        });
    }
    public void cargarFolios(){
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdFaltaAdmin = share.getString("IDFALTAADMIN", "");
        //cargarNumReferencia = share.getString("NOREFERENCIA", "");
        cargarUsuario = share.getString("Usuario", "");
        Log.i("id",cargarIdFaltaAdmin);
        //Log.i("refe",cargarNumReferencia);
        Log.i("user",cargarUsuario);
    }
    /*****************************************************************************/
    private void ListCombos() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> unidad = dataHelper.getAllUnidad();
        ArrayList<String> cargos = dataHelper.getAllCargos();
        ArrayList<String> autoridad = dataHelper.getAllAutoridadAdmin();
        if (unidad.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE UNIDAD");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, unidad);
            txtUnidadDeArriboAdministrativo.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON UNIDADES ACTIVAS.", Toast.LENGTH_LONG).show();
        }
        if (cargos.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE CARGOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, cargos);
            txtCargoAdministrativo.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON CARGOS ACTIVOS.", Toast.LENGTH_LONG).show();
        }
        if (autoridad.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE AUTORIDAD");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, autoridad);
            txtAdscripcionAdministrativo.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON ADSCRIPCIONES.", Toast.LENGTH_LONG).show();
        }
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
                cargarPuestaDisposicion();
                cargarPuestaDisposicionFiscalia();
            }
        }

    }

    public void random(){
        Random random = new Random();
        numberRandom = random.nextInt(9000)*99;
        randomUrlImagen = numberRandom;
    }


}