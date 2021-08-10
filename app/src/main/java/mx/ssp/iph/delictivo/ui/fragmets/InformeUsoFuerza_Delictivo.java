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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.delictivo.model.ModeloLugarIntervencion_Delictivo;
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

public class InformeUsoFuerza_Delictivo extends Fragment {

    private InformeUsoFuerzaDelictivoViewModel mViewModel;
    Button btnGuardarUsoFuerza;
    RadioGroup rgLesionadosUsoFuerza,rgReduccionFisicaDelictivo,rgArmasIncapacitantesDelictivo,rgArmasFuegoDelictivo,rgAsistenciaMedicaUsoFuerza,rgNuevoElementoUsoFuerza;
    Spinner spAutoridadesLesionadosUsoFuerza,spAutoridadesFallecidasUsoFuerza,spPersonasLesionadasUsoFuerza,spPersonasFallecidasUsoFuerza;
    EditText txtDescripciondelUsoFuerza,txtDescripcionAsistenciaMedicaUsoFuerza;
    String cargarIdPoliciaPrimerRespondiente,cargarIdHechoDelictivo,varLesionadosUsoFuerza = "",varReduccionFisicaDelictivo = "",varArmasIncapacitantesDelictivo = "",
            varArmasFuegoDelictivo = "",varAsistenciaMedicaUsoFuerza = "",varNuevoElementoUsoFuerza = "",
            varNoElementosAutoridadesLesionados = "000",varNoElementosAutoridadesFallecidas = "000",
            varNoElementosPersonasLesionadas = "000",varNoElementosPersonasFallecidas = "000",
            varLesionadosAgentes,varLesionadosPersona,varFallecidoAgente,varFallecidoPersona;
    SharedPreferences share;

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
        rgLesionadosUsoFuerza = view.findViewById(R.id.rgLesionadosUsoFuerza);
        rgReduccionFisicaDelictivo = view.findViewById(R.id.rgReduccionFisicaDelictivo);
        rgArmasIncapacitantesDelictivo = view.findViewById(R.id.rgArmasIncapacitantesDelictivo);
        rgArmasFuegoDelictivo = view.findViewById(R.id.rgArmasFuegoDelictivo);
        rgAsistenciaMedicaUsoFuerza = view.findViewById(R.id.rgAsistenciaMedicaUsoFuerza);
        rgNuevoElementoUsoFuerza = view.findViewById(R.id.rgNuevoElementoUsoFuerza);
        spAutoridadesLesionadosUsoFuerza = view.findViewById(R.id.spAutoridadesLesionadosUsoFuerza);
        spAutoridadesFallecidasUsoFuerza = view.findViewById(R.id.spAutoridadesFallecidasUsoFuerza);
        spPersonasLesionadasUsoFuerza = view.findViewById(R.id.spPersonasLesionadasUsoFuerza);
        spPersonasFallecidasUsoFuerza = view.findViewById(R.id.spPersonasFallecidasUsoFuerza);
        txtDescripciondelUsoFuerza = view.findViewById(R.id.txtDescripciondelUsoFuerza);
        txtDescripcionAsistenciaMedicaUsoFuerza = view.findViewById(R.id.txtDescripcionAsistenciaMedicaUsoFuerza);
        btnGuardarUsoFuerza = view.findViewById(R.id.button);

        funciones = new Funciones();

        funciones.CambiarTituloSeccionesDelictivo("ANEXO B. INFORME DEL USO DE LA FUERZA",getContext(),getActivity());

        rgLesionadosUsoFuerza.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiLesionadosUsoFuerza) {
                    varLesionadosUsoFuerza = "SI";
                } else if (checkedId == R.id.rbNoLesionadosUsoFuerza) {
                    varLesionadosUsoFuerza = "NO";
                }

            }
        });
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
        rgNuevoElementoUsoFuerza.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiNuevoElementoUsoFuerza) {
                    varNuevoElementoUsoFuerza = "SI";
                } else if (checkedId == R.id.rbNoNuevoElementoUsoFuerza) {
                    varNuevoElementoUsoFuerza = "NO";
                }

            }
        });

        btnGuardarUsoFuerza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUsoFuerza();
            }
        });

        /********************************************************************/
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InformeUsoFuerzaDelictivoViewModel.class);
        // TODO: Use the ViewModel
    }


    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void updateUsoFuerza() {

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
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDUsoFuerza/")
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
                    InformeUsoFuerza_Delictivo.this.getActivity().runOnUiThread(new Runnable() {
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

}