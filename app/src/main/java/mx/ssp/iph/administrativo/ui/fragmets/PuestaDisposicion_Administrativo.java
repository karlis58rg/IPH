package mx.ssp.iph.administrativo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.lang.Object;


import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModeloNoReferencia_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloPuestaDisposicion_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloRecibeDisposicion_Administrativo;
import mx.ssp.iph.administrativo.viewModel.PuestaDisposicionAdministrativoViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
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
    CheckBox chDetencionesAnexoAAdministrativo,chDetencionesAnexoBAdministrativo,chSinAnexosAdministrativo,chNoAplicaUnidadDeArriboAdministrativo;
    RadioGroup rgPrimerRespondienteAdministrativo;
    EditText txtFechaPuestaDisposicionAdministrativo,txthoraPuestaDisposicionAdministrativo,txtNoExpedienteAdmministrativo,txtFiscaliaAutoridadAdministrativo;
    TextView lblFirmaAutoridadRealizadaAdministrativo;
    Button btnGuardarPuestaDisposicioAdministrativo;
    SharedPreferences share;
    String cargarIdFaltaAdmin,cargarNumReferencia,cargarUsuario,
            varAnexoA = "NO",varNoDetenidos = "000",varAnexoB = "NO",varNoVehiculos = "000",varSinAnexos = "NO",
            varNoAplicaUnidad,descUnidad,descAutoridad,descCargo;
    Funciones funciones;
    Spinner txtCargoAdministrativo,txtUnidadDeArriboAdministrativo,
            spDetencionesAnexoAAdministrativo,spDetencionesAnexoBAdministrativo,txtAdscripcionAdministrativo;
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
        txtFechaPuestaDisposicionAdministrativo = root.findViewById(R.id.txtFechaPuestaDisposicionAdministrativo);
        txthoraPuestaDisposicionAdministrativo = root.findViewById(R.id.txthoraPuestaDisposicionAdministrativo);
        txtNoExpedienteAdmministrativo = root.findViewById(R.id.txtNoExpedienteAdmministrativo);
        txtNoExpedienteAdmministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(25)});

        //txtPrimerApellidoAdministrativo = root.findViewById(R.id.txtPrimerApellidoAdministrativo);
        //txtPrimerApellidoAdministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});
        //txtSegundoApellidoAdministrativo = root.findViewById(R.id.txtSegundoApellidoAdministrativo);
        //txtSegundoApellidoAdministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});
        //txtNombresAdministrativo = root.findViewById(R.id.txtNombresAdministrativo);
        //txtNombresAdministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});
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
        imgFirmaAutoridadAdministrativo = (ImageView) root.findViewById(R.id.imgFirmaAutoridadAdministrativo);

        ListCombos();




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
                ContenedorFirma dialog = new ContenedorFirma(R.id.lblFirmaAutoridadRealizadaAdministrativo,R.id.lblFirmaOcultaAutoridadBase64,R.id.imgFirmaAutoridadAdministrativoMiniatura);
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
        });

         */

        btnGuardarPuestaDisposicioAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS ", Toast.LENGTH_LONG).show();
                insertPuestaDisposicion();
                //insertImagen();
            }
        });

        //****************************************************************************//
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PuestaDisposicionAdministrativoViewModel.class);
        // TODO: Use the ViewModel
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertPuestaDisposicion() {
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

        ModeloPuestaDisposicion_Administrativo puestaDisposicion = new ModeloPuestaDisposicion_Administrativo
                (cargarIdFaltaAdmin,cargarNumReferencia,
                        txtFechaPuestaDisposicionAdministrativo.getText().toString(),
                        txthoraPuestaDisposicionAdministrativo.getText().toString(),
                        txtNoExpedienteAdmministrativo.getText().toString());
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
                                insertRecibeDisposicion();
                                /*System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                txtFechaPuestaDisposicionAdministrativo.setText("");
                                txthoraPuestaDisposicionAdministrativo.setText("");
                                txtNoExpedienteAdmministrativo.setText("");
                                txtPrimerApellidoAdministrativo.setText("");
                                txtSegundoApellidoAdministrativo.setText("");
                                txtNombresAdministrativo.setText("");
                                txtFiscaliaAutoridadAdministrativo.setText("");
                                */

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

    private void insertRecibeDisposicion() {
        DataHelper dataHelper = new DataHelper(getContext());
        descAutoridad = (String) txtAdscripcionAdministrativo.getSelectedItem();
        int idAdscripcion = dataHelper.getIdAutoridadAdmin(descAutoridad);
        String adscripcion = String.valueOf(idAdscripcion);

        descCargo = (String) txtCargoAdministrativo.getSelectedItem();
        int idCargo = dataHelper.getIdCargo(descCargo);
        String cargo = String.valueOf(idCargo);

        String urlImagen = "http://189.254.7.167/WebServiceIPH/Firma/"+cargarIdFaltaAdmin+".jpg";

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
                    PuestaDisposicion_Administrativo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                /*
                                txtFechaPuestaDisposicionAdministrativo.setText("");
                                txthoraPuestaDisposicionAdministrativo.setText("");
                                txtNoExpedienteAdmministrativo.setText("");
                                txtPrimerApellidoAdministrativo.setText("");
                                txtSegundoApellidoAdministrativo.setText("");
                                txtNombresAdministrativo.setText("");
                                txtFiscaliaAutoridadAdministrativo.setText("");
                                txtAdscripcionAdministrativo.setText("");
                                */

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
        String cadena = lblFirmaAutoridadRealizadaAdministrativo.getText().toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Description", recibeDisposicion.getNomRecibePuestaDisp()+".jpg")
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
                            Toast.makeText(getContext(), "REGISTRO ENVIADO CON EXITO", Toast.LENGTH_SHORT).show();
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

    public void cargarFolios(){
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdFaltaAdmin = share.getString("IDFALTAADMIN", "");
        cargarNumReferencia = share.getString("NOREFERENCIA", "");
        cargarUsuario = share.getString("Usuario", "");
        Log.i("id",cargarIdFaltaAdmin);
        Log.i("refe",cargarNumReferencia);
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

}