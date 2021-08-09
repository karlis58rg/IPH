package mx.ssp.iph.delictivo.ui.fragmets;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.delictivo.model.ModeloConocimientoHecho_Delictivo;
import mx.ssp.iph.delictivo.model.ModeloPrimerRespondiente_Delictivo;
import mx.ssp.iph.delictivo.viewModel.ConocimientoHechoViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConocimientoHecho extends Fragment {

    private ConocimientoHechoViewModel mViewModel;
    Button btnGuardarConocimientoHechoDelictivo;
    Spinner spConocimientoHechoDelictivo;
    EditText txt911FolioConocimientoHechoDelictivo,txtFechaConocimientoHechoDelictivo,
            txtHoraConocimientoHechoDelictivo,txtFechaArriboLugarHD,txtHoraArriboLugarHD;
    String descConocimientoHD,cargarIdPoliciaPrimerRespondiente,cargarIdHechoDelictivo;
    SharedPreferences share;
    private Funciones funciones;

    public static ConocimientoHecho newInstance() {
        return new ConocimientoHecho();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conocimiento_hecho_fragment, container, false);
        /************************************************************************************/
        cargarDatos();
        spConocimientoHechoDelictivo = view.findViewById(R.id.spConocimientoHechoDelictivo);
        txt911FolioConocimientoHechoDelictivo = view.findViewById(R.id.txt911FolioConocimientoHechoDelictivo);
        txtFechaConocimientoHechoDelictivo = view.findViewById(R.id.txtFechaConocimientoHechoDelictivo);
        txtHoraConocimientoHechoDelictivo = view.findViewById(R.id.txtHoraConocimientoHechoDelictivo);
        txtFechaArriboLugarHD = view.findViewById(R.id.txtFechaArriboLugarHD);
        txtHoraArriboLugarHD = view.findViewById(R.id.txtHoraArriboLugarHD);
        btnGuardarConocimientoHechoDelictivo = view.findViewById(R.id.btnGuardarConocimientoHechoDelictivo);
        funciones = new Funciones();
        funciones.CambiarTituloSeccionesDelictivo("SECCIÓN 3. CONOCIMIENTO DEL HECHO",getContext(),getActivity());
        ListConocimientoHecho();

        //***************** FECHA  **************************//
        txtFechaConocimientoHechoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaConocimientoHechoDelictivo,getContext(),getActivity());
            }
        });

        //***************** HORA **************************//
        txtHoraConocimientoHechoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txtHoraConocimientoHechoDelictivo,getContext(),getActivity());
            }
        });

        //***************** FECHA  **************************//
        txtFechaArriboLugarHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaArriboLugarHD,getContext(),getActivity());
            }
        });

        //***************** HORA **************************//
        txtHoraArriboLugarHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txtHoraArriboLugarHD,getContext(),getActivity());
            }
        });

        //***************** GUARDAR **************************//
        btnGuardarConocimientoHechoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                updateConocimientoHecho();
            }
        });


        /****************************************************************************************/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConocimientoHechoViewModel.class);
        // TODO: Use the ViewModel
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void updateConocimientoHecho() {
        DataHelper dataHelper = new DataHelper(getContext());
        descConocimientoHD = (String) spConocimientoHechoDelictivo.getSelectedItem();
        int idDescConocimiento = dataHelper.getIdConocimientoInfraccion(descConocimientoHD);
        String idConocimiento = String.valueOf(idDescConocimiento);

        ModeloConocimientoHecho_Delictivo conocimientoHechoD = new ModeloConocimientoHecho_Delictivo
                (cargarIdHechoDelictivo,idConocimiento,txt911FolioConocimientoHechoDelictivo.getText().toString(), txtFechaConocimientoHechoDelictivo.getText().toString(),
                        txtHoraConocimientoHechoDelictivo.getText().toString(),txtFechaArriboLugarHD.getText().toString(),txtHoraArriboLugarHD.getText().toString());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo", conocimientoHechoD.getIdHechoDelictivo())
                .add("IdConocimiento", conocimientoHechoD.getIdConocimiento())
                .add("Telefono911", conocimientoHechoD.getTelefono911())
                .add("FechaConocimiento", conocimientoHechoD.getFechaConocimiento())
                .add("HoraConocimiento", conocimientoHechoD.getHoraConocimiento())
                .add("FechaArribo", conocimientoHechoD.getFechaArribo())
                .add("HoraArribo", conocimientoHechoD.getHoraArribo())
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
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    ConocimientoHecho.this.getActivity().runOnUiThread(new Runnable() {
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

    public void cargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdPoliciaPrimerRespondiente = share.getString("Usuario", "SIN INFORMACION");
        cargarIdHechoDelictivo = share.getString("IDHECHODELICTIVO", "SIN INFORMACION");
    }

    private void ListConocimientoHecho() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllConocimientoInfraccion();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE CONOCIMIENTO INFRACCION");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spConocimientoHechoDelictivo.setAdapter(adapter);
        }
    }

}