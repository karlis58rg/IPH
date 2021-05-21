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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.model.ModelLugarIntervencion_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloNoReferencia_Administrativo;
import mx.ssp.iph.administrativo.viewModel.LugarDeIntervencionViewModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LugarDeIntervencion extends Fragment {

    private LugarDeIntervencionViewModel mViewModel;
    EditText txtCalleUbicacionGeograficaAdministrativo,txtNumeroExteriorUbicacionGeograficaAdministrativo,txtNumeroInteriorUbicacionGeograficaAdministrativo,txtCodigoPostalUbicacionGeograficaAdministrativo,
            txtReferenciasdelLugarUbicacionGeograficaAdministrativo,txtLatitudUbicacionGeograficaAdministrativo,txtLongitudUbicacionGeograficaAdministrativo;
    Button btnGuardarPuestaDisposicioAdministrativo;
    SharedPreferences share;
    String cargarIdFaltaAdmin;

    public static LugarDeIntervencion newInstance() {
        return new LugarDeIntervencion();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.lugar_de_intervencion_fragment, container, false);
        //************************************** ACCIONES DE LA VISTA **************************************//
        cargarFolios();
        txtCalleUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtCalleUbicacionGeograficaAdministrativo);
        txtNumeroExteriorUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtNumeroExteriorUbicacionGeograficaAdministrativo);
        txtNumeroInteriorUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtNumeroInteriorUbicacionGeograficaAdministrativo);
        txtCodigoPostalUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtCodigoPostalUbicacionGeograficaAdministrativo);
        txtReferenciasdelLugarUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtReferenciasdelLugarUbicacionGeograficaAdministrativo);
        txtLatitudUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtLatitudUbicacionGeograficaAdministrativo);
        txtLongitudUbicacionGeograficaAdministrativo = root.findViewById(R.id.txtLongitudUbicacionGeograficaAdministrativo);

        btnGuardarPuestaDisposicioAdministrativo = root.findViewById(R.id.btnGuardarPuestaDisposicioAdministrativo);

        btnGuardarPuestaDisposicioAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS ", Toast.LENGTH_LONG).show();
                insertLugarIntervencion();
            }
        });

        //***************************************************************************//
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LugarDeIntervencionViewModel.class);
        // TODO: Use the ViewModel
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertLugarIntervencion() {
        ModelLugarIntervencion_Administrativo modeloIntervencion= new ModelLugarIntervencion_Administrativo
                (txtCalleUbicacionGeograficaAdministrativo.getText().toString(),
                        txtNumeroExteriorUbicacionGeograficaAdministrativo.getText().toString(),
                        txtNumeroInteriorUbicacionGeograficaAdministrativo.getText().toString(),
                        txtCodigoPostalUbicacionGeograficaAdministrativo.getText().toString(),
                        txtReferenciasdelLugarUbicacionGeograficaAdministrativo.getText().toString(),
                        txtLatitudUbicacionGeograficaAdministrativo.getText().toString(),
                        txtLongitudUbicacionGeograficaAdministrativo.getText().toString());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin",cargarIdFaltaAdmin)
                .add("CalleTramo", modeloIntervencion.getCalleTramo())
                .add("NoExterior", modeloIntervencion.getNoExterior())
                .add("NoInterior", modeloIntervencion.getNoInterior())
                .add("Cp", modeloIntervencion.getCp())
                .add("Referencia", modeloIntervencion.getReferencia())
                .add("Latitud", modeloIntervencion.getLatitud())
                .add("Longitud", modeloIntervencion.getLongitud())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/LugarIntervencionAdministrativa/")
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
                    LugarDeIntervencion.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                txtCalleUbicacionGeograficaAdministrativo.setText("");
                                txtNumeroExteriorUbicacionGeograficaAdministrativo.setText("");
                                txtNumeroInteriorUbicacionGeograficaAdministrativo.setText("");
                                txtCodigoPostalUbicacionGeograficaAdministrativo.setText("");
                                txtReferenciasdelLugarUbicacionGeograficaAdministrativo.setText("");
                                txtLatitudUbicacionGeograficaAdministrativo.setText("");
                                txtLongitudUbicacionGeograficaAdministrativo.setText("");
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
    }

}