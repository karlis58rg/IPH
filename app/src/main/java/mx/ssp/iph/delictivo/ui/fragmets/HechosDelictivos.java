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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.CheckBox;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mx.ssp.iph.Login;
import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModeloNoReferencia_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloUsuarios_Administrativo;
import mx.ssp.iph.administrativo.ui.fragmets.NoReferencia_Administrativo;
import mx.ssp.iph.delictivo.model.ModeloHechoDelictivo;
import mx.ssp.iph.delictivo.viewModel.HechosDelictivosViewModel;
import mx.ssp.iph.principal.ui.activitys.Principal;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.Funciones;

public class HechosDelictivos extends Fragment {

    private HechosDelictivosViewModel mViewModel;

    Button btnGuardarHechoDelictivo;
    EditText txtFolioInternoDelictivo,txtFolioSistemaDelictivo,txtNoReferenciaDelictivo,txtNoExpedienteAdmministrativo,txtFechaEntregaReferenciaDelictivo,txtHoraEntregaReferenciaDelictivo,
            txtFiscaliaAutoridadDelictivo;
    CheckBox chDetencionesAnexoADelictivo,chAnexosDInventarioArmasDelictivo,chUsoFuerzaAnexoBDelictivo,chEntrevistasAnexoEDelictivo,chAnexosCInspeccionVehiculoDelictivo,chAnexosFEntregaRecepcionDelictivo,
            chSinAnexosDelictivo;
    Spinner spDetencionesAnexoADelictivo,spAnexosDInventarioArmasDelictivo,spUsoFuerzaAnexoBDelictivo,spEntrevistasAnexoEDelictivo,spAnexosCInspeccionVehiculoDelictivo,spAnexosFEntregaRecepcionDelictivo,
            spAdscripcionDelictivo,spCargoDelictivo;
    RadioGroup rgAnexosDocumentacionDelictivo;
    TextView lblFirmaAutoridadRealizadaDelictivo,lblFirmaOcultaAutoridadBase64HechosDelictivos;
    ImageView imgFirmaAutoridadDelictivo;
    String edo = "",inst = "",gob = "",mpio = "",fecha = "",dia = "01",mes = "01",anio = "2021",tiempo = "",hora = "",minutos = "",
            descAdscripcion = "",descCargo = "",cargarIdPoliciaPrimerRespondiente = "",cargarIdHechoDelictivo = "",respuestaJson = "";
    String anexoDetenciones = "NO", numAnexoDetenciones = "000", anexoUsoFuerza = "NO",  numAnexoUsoFuerza = "000",  anexoVehiculos = "NO",
     numAnexoVehiculo = "000",  anexoArmasObjetos = "NO",  numAnexoArmasObjetos = "000", anexoEntrevista = "NO",  numAnexoEntrevista = "000", anexoLugarIntervencion = "NO",
     numAnexoLugarIntervencion = "000",  anexoNoSeEntregan = "SI";
    SharedPreferences share;

    private Funciones funciones;

    public static HechosDelictivos newInstance() {
        return new HechosDelictivos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.hechos_delictivos_fragment, container, false);

        /*********************************************************************************************************/
        cargarDatos();

        btnGuardarHechoDelictivo = view.findViewById(R.id.btnGuardarHechoDelictivo);
        txtFechaEntregaReferenciaDelictivo = view.findViewById(R.id.txtFechaEntregaReferenciaDelictivo);
        txtHoraEntregaReferenciaDelictivo = view.findViewById(R.id.txtHoraEntregaReferenciaDelictivo);
        imgFirmaAutoridadDelictivo = view.findViewById(R.id.imgFirmaAutoridadDelictivo);
        funciones = new Funciones();

        funciones.CambiarTituloSeccionesDelictivo("SECCIÓN 1. PUESTA A DISPOSICIÓN",getContext(),getActivity());


        //***************** FECHA  **************************//
        txtFechaEntregaReferenciaDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaEntregaReferenciaDelictivo,getContext(),getActivity());
            }
        });

        //***************** HORA **************************//
        txtHoraEntregaReferenciaDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txtHoraEntregaReferenciaDelictivo,getContext(),getActivity());
            }
        });

        //***************** FIRMA **************************//
        imgFirmaAutoridadDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirma dialog = new ContenedorFirma(R.id.lblFirmaAutoridadRealizadaDelictivo,R.id.lblFirmaAutoridadRealizadaDelictivoOculto);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });

        //***************** GUARDAR **************************//
        txtFolioInternoDelictivo = view.findViewById(R.id.txtFolioInternoDelictivo);
        txtFolioSistemaDelictivo = view.findViewById(R.id.txtFolioSistemaDelictivo);
        txtNoReferenciaDelictivo = view.findViewById(R.id.txtNoReferenciaDelictivo);
        txtNoExpedienteAdmministrativo = view.findViewById(R.id.txtNoExpedienteAdmministrativo);
        txtFechaEntregaReferenciaDelictivo = view.findViewById(R.id.txtFechaEntregaReferenciaDelictivo);
        txtHoraEntregaReferenciaDelictivo = view.findViewById(R.id.txtHoraEntregaReferenciaDelictivo);
        txtFiscaliaAutoridadDelictivo = view.findViewById(R.id.txtFiscaliaAutoridadDelictivo);

        chDetencionesAnexoADelictivo = view.findViewById(R.id.chDetencionesAnexoADelictivo);
        chAnexosDInventarioArmasDelictivo = view.findViewById(R.id.chAnexosDInventarioArmasDelictivo);
        chUsoFuerzaAnexoBDelictivo = view.findViewById(R.id.chUsoFuerzaAnexoBDelictivo);
        chEntrevistasAnexoEDelictivo = view.findViewById(R.id.chEntrevistasAnexoEDelictivo);
        chAnexosCInspeccionVehiculoDelictivo = view.findViewById(R.id.chAnexosCInspeccionVehiculoDelictivo);
        chAnexosFEntregaRecepcionDelictivo = view.findViewById(R.id.chAnexosFEntregaRecepcionDelictivo);
        chSinAnexosDelictivo = view.findViewById(R.id.chSinAnexosDelictivo);

        spDetencionesAnexoADelictivo = view.findViewById(R.id.spDetencionesAnexoADelictivo);
        spAnexosDInventarioArmasDelictivo = view.findViewById(R.id.spAnexosDInventarioArmasDelictivo);
        spUsoFuerzaAnexoBDelictivo = view.findViewById(R.id.spUsoFuerzaAnexoBDelictivo);
        spEntrevistasAnexoEDelictivo = view.findViewById(R.id.spEntrevistasAnexoEDelictivo);
        spAnexosCInspeccionVehiculoDelictivo = view.findViewById(R.id.spAnexosCInspeccionVehiculoDelictivo);
        spAnexosFEntregaRecepcionDelictivo = view.findViewById(R.id.spAnexosFEntregaRecepcionDelictivo);
        spAdscripcionDelictivo = view.findViewById(R.id.spAdscripcionDelictivo);
        spCargoDelictivo = view.findViewById(R.id.spCargoDelictivo);

        rgAnexosDocumentacionDelictivo = view.findViewById(R.id.rgAnexosDocumentacionDelictivo);
        lblFirmaAutoridadRealizadaDelictivo = view.findViewById(R.id.lblFirmaAutoridadRealizadaDelictivo);
        lblFirmaOcultaAutoridadBase64HechosDelictivos = view.findViewById(R.id.lblFirmaOcultaAutoridadBase64HechosDelictivos);
        imgFirmaAutoridadDelictivo = view.findViewById(R.id.imgFirmaAutoridadDelictivo);

        ListCombos();
        getNumReferencia();
        txtFolioInternoDelictivo.setText(cargarIdHechoDelictivo);
        txtNoReferenciaDelictivo.setEnabled(false);
        txtFolioInternoDelictivo.setEnabled(false);


        btnGuardarHechoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
            }
        });




        /******************************************************************************/
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HechosDelictivosViewModel.class);
        // TODO: Use the ViewModel
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
                .url("http://189.254.7.167/WebServiceIPH/api/HDHechoDelictivo?usuario="+cargarIdPoliciaPrimerRespondiente)
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
                    HechosDelictivos.this.getActivity().runOnUiThread(new Runnable() {
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
                                    txtNoReferenciaDelictivo.setText(edo+"|"+inst+"|"+gob+"|"+mpio+"|"+dia+"|"+mes+"|"+anio+"|"+hora+"|"+minutos);
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

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertHechoDelictivo() {

       /* if(chDetencionesAnexoAAdministrativo.isChecked()){
            varAnexoA = "SI";
            varNoDetenidos = (String) spDetencionesAnexoAAdministrativo.getSelectedItem();
        }else{
            varAnexoA = "NO";
            varNoDetenidos = "000";
        }

        chDetencionesAnexoADelictivo,
                chAnexosDInventarioArmasDelictivo,
                chUsoFuerzaAnexoBDelictivo,
                chEntrevistasAnexoEDelictivo,
                chAnexosCInspeccionVehiculoDelictivo,
                chAnexosFEntregaRecepcionDelictivo,
                chSinAnexosDelictivo;*/







        ModeloHechoDelictivo modeloHechoDelictivo = new ModeloHechoDelictivo(
                cargarIdHechoDelictivo, txtNoReferenciaDelictivo.getText().toString(), txtFechaEntregaReferenciaDelictivo.getText().toString(),
                txtHoraEntregaReferenciaDelictivo.getText().toString(),
                txtNoExpedienteAdmministrativo.getText().toString(), anexoDetenciones,
                numAnexoDetenciones, anexoUsoFuerza,  numAnexoUsoFuerza,  anexoVehiculos,
                 numAnexoVehiculo, anexoArmasObjetos, numAnexoArmasObjetos,
                 anexoEntrevista, numAnexoEntrevista, anexoLugarIntervencion,
                 numAnexoLugarIntervencion, anexoNoSeEntregan,
                cargarIdPoliciaPrimerRespondiente);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo", modeloHechoDelictivo.getIdHechoDelictivo())
                .add("NumReferencia", modeloHechoDelictivo.getNumReferencia())
                .add("Fecha",modeloHechoDelictivo.getFecha())
                .add("Hora", modeloHechoDelictivo.getHora())
                .add("NumExpediente", modeloHechoDelictivo.getNumExpediente())
                .add("AnexoDetenciones", modeloHechoDelictivo.getAnexoDetenciones())
                .add("NumAnexoDetenciones", modeloHechoDelictivo.getNumAnexoDetenciones())
                .add("AnexoUsoFuerza", modeloHechoDelictivo.getAnexoUsoFuerza())
                .add("NumAnexoUsoFuerza", modeloHechoDelictivo.getNumAnexoUsoFuerza())
                .add("AnexoVehiculos",modeloHechoDelictivo.getAnexoVehiculos())
                .add("NumAnexoVehiculo", modeloHechoDelictivo.getNumAnexoVehiculo())
                .add("AnexoArmasObjetos", modeloHechoDelictivo.getAnexoArmasObjetos())
                .add("NumAnexoArmasObjetos", modeloHechoDelictivo.getNumAnexoArmasObjetos())
                .add("AnexoEntrevista", modeloHechoDelictivo.getAnexoEntrevista())
                .add("NumAnexoEntrevista", modeloHechoDelictivo.getNumAnexoEntrevista())
                .add("AnexoLugarIntervencion", modeloHechoDelictivo.getAnexoLugarIntervencion())
                .add("NumAnexoLugarIntervencion", modeloHechoDelictivo.getNumAnexoLugarIntervencion())
                .add("AnexoNoSeEntregan",modeloHechoDelictivo.getAnexoNoSeEntregan())
                .add("IdPoliciaPrimerRespondiente", modeloHechoDelictivo.getIdPoliciaPrimerRespondiente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDHechoDelictivo/")
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
                if(response.code() == 500){
                    Toast.makeText(getContext(), "EXISTIÓ UN ERROR EN SU CONEXIÓN A INTERNET, INTÉNTELO NUEVAMENTE", Toast.LENGTH_SHORT).show();
                }else if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    HechosDelictivos.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                //guardarFolios();
                                //guardarNoReferencia();
                                 /*
                                txtFolioInternoAdministrativo.setText("");
                                txtFolioSistemaAdministrativo.setText("");
                                txtNoReferenciaAdministrativo.setText("");
                                txtEstadoReferenciaAdministrativo.setText("");
                                txtGobiernoReferenciaAdministrativo.setText("");
                                txtFechaEntregaReferenciaAdministrativo.setText("");
                                txtHoraEntregaReferenciaAdministrativo.setText("");

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

    private void ListCombos() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> cargos = dataHelper.getAllCargos();
        ArrayList<String> autoridad = dataHelper.getAllAutoridadAdmin();
        if (cargos.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE CARGOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, cargos);
            spCargoDelictivo.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON CARGOS ACTIVOS.", Toast.LENGTH_LONG).show();
        }
        if (autoridad.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE AUTORIDAD");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, autoridad);
            spAdscripcionDelictivo.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON ADSCRIPCIONES.", Toast.LENGTH_LONG).show();
        }

    }

    public void cargarDatos(){
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdPoliciaPrimerRespondiente = share.getString("Usuario", "SIN INFORMACION");
        cargarIdHechoDelictivo = share.getString("IDHECHODELICTIVO", "SIN INFORMACION");
    }



}