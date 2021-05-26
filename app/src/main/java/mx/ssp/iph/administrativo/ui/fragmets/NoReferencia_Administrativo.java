package mx.ssp.iph.administrativo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

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
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModeloNoReferencia_Administrativo;
import mx.ssp.iph.administrativo.viewModel.NoReferencia_Administrativo_ViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NoReferencia_Administrativo extends Fragment {

    private NoReferencia_Administrativo_ViewModel mViewModel;
    private EditText txtHoraEntregaReferenciaAdministrativo,txtFechaEntregaReferenciaAdministrativo;
    Funciones funciones;

    EditText txtFolioInternoAdministrativo,txtFolioSistemaAdministrativo,txtNoReferenciaAdministrativo,txtEstadoReferenciaAdministrativo,txtGobiernoReferenciaAdministrativo;
    Spinner spInstitucionReferenciaAdministrativo,spMunicipioReferenciaAdministrativo;
    Button btnGuardarReferenciaAdministrativo;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    String guardarIdFaltaAdmin,codigoVerifi,randomCodigoVerifi;
    int numberRandom;


    public static NoReferencia_Administrativo newInstance() {
        return new NoReferencia_Administrativo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.no_referencia_administrativo_fragment, container, false);

        //************************************** ACCIONES DE LA VISTA **************************************//
        txtFolioInternoAdministrativo = root.findViewById(R.id.txtFolioInternoAdministrativo);
        txtFolioSistemaAdministrativo = root.findViewById(R.id.txtFolioSistemaAdministrativo);
        txtNoReferenciaAdministrativo = root.findViewById(R.id.txtNoReferenciaAdministrativo);
        txtEstadoReferenciaAdministrativo = root.findViewById(R.id.txtEstadoReferenciaAdministrativo);
        txtGobiernoReferenciaAdministrativo = root.findViewById(R.id.txtGobiernoReferenciaAdministrativo);
        txtFechaEntregaReferenciaAdministrativo = root.findViewById(R.id.txtFechaEntregaReferenciaAdministrativo);
        txtHoraEntregaReferenciaAdministrativo = root.findViewById(R.id.txtHoraEntregaReferenciaAdministrativo);

        spInstitucionReferenciaAdministrativo = root.findViewById(R.id.spInstitucionReferenciaAdministrativo);
        spMunicipioReferenciaAdministrativo = root.findViewById(R.id.spMunicipioReferenciaAdministrativo);

        btnGuardarReferenciaAdministrativo = root.findViewById(R.id.btnGuardarReferenciaAdministrativo);
        funciones = new Funciones();
        txtHoraEntregaReferenciaAdministrativo = (EditText) root.findViewById(R.id.txtHoraEntregaReferenciaAdministrativo);
        ListInstitucion();

        //FEcha
        txtFechaEntregaReferenciaAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaEntregaReferenciaAdministrativo,getContext(),getActivity());
            }
        });

        //Hora
        txtHoraEntregaReferenciaAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txtHoraEntregaReferenciaAdministrativo,getContext(),getActivity());
            }
        });

        btnGuardarReferenciaAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS ", Toast.LENGTH_LONG).show();
                insertNoReferenciaAdministrativa();
            }
        });
        //****************************************************************************//
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NoReferencia_Administrativo_ViewModel.class);
        // TODO: Use the ViewModel
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertNoReferenciaAdministrativa() {
        ModeloNoReferencia_Administrativo modeloNoReferencia = new ModeloNoReferencia_Administrativo
                (txtFolioInternoAdministrativo.getText().toString(),
                        txtNoReferenciaAdministrativo.getText().toString(),
                        txtFechaEntregaReferenciaAdministrativo.getText().toString(),
                        txtHoraEntregaReferenciaAdministrativo.getText().toString());

        guardarIdFaltaAdmin = modeloNoReferencia.getIdFaltaAdmin();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin", modeloNoReferencia.getIdFaltaAdmin())
                .add("NumReferencia", modeloNoReferencia.getIdGobierno())
                .add("Fecha", modeloNoReferencia.getFecha())
                .add("Hora", modeloNoReferencia.getHora())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/NoReferenciaAdministrativa/")
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
                    NoReferencia_Administrativo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                guardarFolios();
                                txtFolioInternoAdministrativo.setText("");
                                txtFolioSistemaAdministrativo.setText("");
                                txtNoReferenciaAdministrativo.setText("");
                                txtEstadoReferenciaAdministrativo.setText("");
                                txtGobiernoReferenciaAdministrativo.setText("");
                                txtFechaEntregaReferenciaAdministrativo.setText("");
                                txtHoraEntregaReferenciaAdministrativo.setText("");
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

    private void guardarFolios() {
        share = getContext().getSharedPreferences("main", getContext().MODE_PRIVATE);
        editor = share.edit();
        editor.putString("IDFALTAADMIN", guardarIdFaltaAdmin );
        editor.commit();
    }

    public void Random()
    {
        Random random = new Random();
        numberRandom = random.nextInt(9000)*99;
        codigoVerifi = String.valueOf(numberRandom);
        randomCodigoVerifi = codigoVerifi;
    }

    private void ListInstitucion() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllInstitucion();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spInstitucionReferenciaAdministrativo.setAdapter(adapter);
        } else {
            dataHelper.insertCatInstitucion(1, "GUARDIA NACIONAL");
            dataHelper.insertCatInstitucion(2, "POLICIA FEDERAL MINISTERIAL");
            dataHelper.insertCatInstitucion(3, "POLICIA MINISTERIAL");
            dataHelper.insertCatInstitucion(4, "POLICIA MANDO UNICO");
            dataHelper.insertCatInstitucion(5, "POLICIA ESTATAL");
            dataHelper.insertCatInstitucion(6, "POLICIA MUNICIPAL");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spInstitucionReferenciaAdministrativo.setAdapter(adapter);
        }
    }
}