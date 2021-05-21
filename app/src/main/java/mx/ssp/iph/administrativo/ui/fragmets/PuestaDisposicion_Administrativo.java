package mx.ssp.iph.administrativo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import android.widget.TextView;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.model.ModeloNoReferencia_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloPuestaDisposicion_Administrativo;
import mx.ssp.iph.administrativo.viewModel.PuestaDisposicionAdministrativoViewModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.Funciones;

public class PuestaDisposicion_Administrativo extends Fragment {

    private PuestaDisposicionAdministrativoViewModel mViewModel;
    private ImageView imgFirmaAutoridadAdministrativo;
    EditText txtFechaPuestaDisposicionAdministrativo,txthoraPuestaDisposicionAdministrativo,txtNoExpedienteAdmministrativo,txtPrimerApellidoAdministrativo,txtSegundoApellidoAdministrativo,txtNombresAdministrativo,
            txtInstitucionAdscripcionAdministrativo,txtGradoCargoAdministrativo,txtUnidadDeArriboAdministrativo,txtFiscaliaAutoridadAdministrativo,txtAdscripcionAdministrativo,txtCargoAdministrativo;
    TextView lblFirmaAutoridadRealizadaAdministrativo;
    Button btnGuardarPuestaDisposicioAdministrativo;
    SharedPreferences share;
    String cargarIdFaltaAdmin,cargarNumReferencia,cargarNumFolio;
    Funciones funciones;

    public static PuestaDisposicion_Administrativo newInstance() {
        return new PuestaDisposicion_Administrativo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.puesta_disposicion_administrativo_fragment, container, false);
        funciones = new Funciones();



        //************************************** ACCIONES DE LA VISTA **************************************//
        cargarFolios();
        txtFechaPuestaDisposicionAdministrativo = root.findViewById(R.id.txtFechaPuestaDisposicionAdministrativo);
        txthoraPuestaDisposicionAdministrativo = root.findViewById(R.id.txthoraPuestaDisposicionAdministrativo);
        txtNoExpedienteAdmministrativo = root.findViewById(R.id.txtNoExpedienteAdmministrativo);
        txtPrimerApellidoAdministrativo = root.findViewById(R.id.txtPrimerApellidoAdministrativo);
        txtSegundoApellidoAdministrativo = root.findViewById(R.id.txtSegundoApellidoAdministrativo);
        txtNombresAdministrativo = root.findViewById(R.id.txtNombresAdministrativo);
        txtInstitucionAdscripcionAdministrativo = root.findViewById(R.id.txtInstitucionAdscripcionAdministrativo);
        txtGradoCargoAdministrativo = root.findViewById(R.id.txtGradoCargoAdministrativo);
        txtUnidadDeArriboAdministrativo = root.findViewById(R.id.txtUnidadDeArriboAdministrativo);
        txtFiscaliaAutoridadAdministrativo = root.findViewById(R.id.txtFiscaliaAutoridadAdministrativo);
        txtAdscripcionAdministrativo = root.findViewById(R.id.txtAdscripcionAdministrativo);
        txtCargoAdministrativo = root.findViewById(R.id.txtCargoAdministrativo);
        lblFirmaAutoridadRealizadaAdministrativo  = root.findViewById(R.id.lblFirmaAutoridadRealizadaAdministrativo);
        btnGuardarPuestaDisposicioAdministrativo = root.findViewById(R.id.btnGuardarPuestaDisposicioAdministrativo);

        imgFirmaAutoridadAdministrativo = (ImageView) root.findViewById(R.id.imgFirmaAutoridadAdministrativo);
        txtFechaPuestaDisposicionAdministrativo = (EditText)root.findViewById(R.id.txtFechaPuestaDisposicionAdministrativo);
        txthoraPuestaDisposicionAdministrativo = (EditText)root.findViewById(R.id.txthoraPuestaDisposicionAdministrativo);

        txtFechaPuestaDisposicionAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaPuestaDisposicionAdministrativo,getContext(),getActivity());
            }
        });
        txthoraPuestaDisposicionAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txthoraPuestaDisposicionAdministrativo,getContext(),getActivity());
            }
        });
        imgFirmaAutoridadAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirma dialog = new ContenedorFirma(R.id.lblFirmaAutoridadRealizadaAdministrativo,R.id.lblFirmaOcultaAutoridadBase64);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });
        btnGuardarPuestaDisposicioAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS ", Toast.LENGTH_LONG).show();
                insertPuestaDisposicion();
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
        ModeloPuestaDisposicion_Administrativo puestaDisposicion = new ModeloPuestaDisposicion_Administrativo
                (cargarIdFaltaAdmin,cargarNumReferencia,cargarNumFolio,
                        txtFechaPuestaDisposicionAdministrativo.getText().toString(),
                        txthoraPuestaDisposicionAdministrativo.getText().toString(),
                        txtNoExpedienteAdmministrativo.getText().toString());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin", puestaDisposicion.getIdFaltaAdmin())
                .add("NumReferencia", puestaDisposicion.getNumReferencia())
                .add("NumFolio", puestaDisposicion.getNumFolio())
                .add("Fecha", puestaDisposicion.getFecha())
                .add("Hora", puestaDisposicion.getHora())
                .add("NumExpediente", puestaDisposicion.getNumExpediente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/FaltaAdministrativa/")
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
                                txtFechaPuestaDisposicionAdministrativo.setText("");
                                txthoraPuestaDisposicionAdministrativo.setText("");
                                txtNoExpedienteAdmministrativo.setText("");
                                txtPrimerApellidoAdministrativo.setText("");
                                txtSegundoApellidoAdministrativo.setText("");
                                txtNombresAdministrativo.setText("");
                                txtInstitucionAdscripcionAdministrativo.setText("");
                                txtGradoCargoAdministrativo.setText("");
                                txtUnidadDeArriboAdministrativo.setText("");
                                txtFiscaliaAutoridadAdministrativo.setText("");
                                txtAdscripcionAdministrativo.setText("");
                                txtCargoAdministrativo.setText("");

                            }else{
                                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("HERE", resp);
                        }
                    });
                }
            }
        });
    }

    public void cargarFolios(){
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdFaltaAdmin = share.getString("IDFALTAADMIN", "");
        cargarNumReferencia = share.getString("NOREFERENCIA", "");
        cargarNumFolio = share.getString("NUMFOLIO", "");
        System.out.println(cargarIdFaltaAdmin+cargarNumReferencia+cargarNumFolio);
    }

}