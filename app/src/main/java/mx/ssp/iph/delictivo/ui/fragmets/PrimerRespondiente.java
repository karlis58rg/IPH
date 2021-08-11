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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModeloPuestaDisposicion_Administrativo;
import mx.ssp.iph.administrativo.ui.fragmets.PuestaDisposicion_Administrativo;
import mx.ssp.iph.delictivo.model.ModeloPrimerRespondiente_Delictivo;
import mx.ssp.iph.delictivo.viewModel.PrimerRespondienteViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PrimerRespondiente extends Fragment {

    private PrimerRespondienteViewModel mViewModel;
    Button btnGuardarPrimerRespondiente;
    Spinner spUnidadDeArriboDelictivo,spArriboElementosLugarIntervencion;
    CheckBox chNoAplicaUnidadDeArriboDelictivo,chSiArriboElementosLugarIntervencion,chNoArriboElementosLugarIntervencion;
    String descUnidad,varIdUnidad,varArriboOtroElemento,varNoElementos,cargarIdHechoDelictivo,cargarIdPoliciaPrimerRespondiente;
    SharedPreferences share;
    private Funciones funciones;

    public static PrimerRespondiente newInstance() {
        return new PrimerRespondiente();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.primer_respondiente_fragment, container, false);
        /************************************************************************************/
        cargarDatos();
        funciones =  new Funciones();
        funciones.CambiarTituloSeccionesDelictivo("SECCIÓN 2. PRIMER RESPONDIENTE",getContext(),getActivity());

        spUnidadDeArriboDelictivo = view.findViewById(R.id.spUnidadDeArriboDelictivo);
        spArriboElementosLugarIntervencion = view.findViewById(R.id.spArriboElementosLugarIntervencion);
        chNoAplicaUnidadDeArriboDelictivo = view.findViewById(R.id.chNoAplicaUnidadDeArriboDelictivo);
        chSiArriboElementosLugarIntervencion = view.findViewById(R.id.chSiArriboElementosLugarIntervencion);
        chNoArriboElementosLugarIntervencion = view.findViewById(R.id.chNoArriboElementosLugarIntervencion);
        btnGuardarPrimerRespondiente = view.findViewById(R.id.btnGuardarPrimerRespondiente);
        ListCombos();

        //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
        if (funciones.ping(getContext()))
        {
            cargarPrimerespondiente();
        }


        chNoAplicaUnidadDeArriboDelictivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {
                if(chselect == true){
                    spUnidadDeArriboDelictivo.setEnabled(false);
                }else{
                    spUnidadDeArriboDelictivo.setEnabled(true);
                }
            }
        });
        chSiArriboElementosLugarIntervencion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {
                if(chselect == true){
                    chNoArriboElementosLugarIntervencion.setEnabled(false);
                }else{
                    chNoArriboElementosLugarIntervencion.setEnabled(true);
                }

            }
        });
        chNoArriboElementosLugarIntervencion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {
                if(chselect == true){
                    chSiArriboElementosLugarIntervencion.setEnabled(false);
                    spArriboElementosLugarIntervencion.setEnabled(true);
                }else{
                    spArriboElementosLugarIntervencion.setVisibility(View.VISIBLE);
                    spArriboElementosLugarIntervencion.setSelection(funciones.getIndexSpiner(spArriboElementosLugarIntervencion, "--Selecciona--"));
                }
            }
        });

        btnGuardarPrimerRespondiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                updatePrimerRespondiente();
            }
        });


        /*********************************************************************************/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PrimerRespondienteViewModel.class);
        // TODO: Use the ViewModel
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void updatePrimerRespondiente() {
        descUnidad = (String) spUnidadDeArriboDelictivo.getSelectedItem();
        varIdUnidad = descUnidad;

        if(chNoAplicaUnidadDeArriboDelictivo.isChecked()){
            varIdUnidad= "NA";
        }

        if(chSiArriboElementosLugarIntervencion.isChecked()){
            varArriboOtroElemento = "SI";
            varNoElementos = (String) spArriboElementosLugarIntervencion.getSelectedItem();
        }

        if(chNoArriboElementosLugarIntervencion.isChecked()){
            varArriboOtroElemento = "NO";
            varNoElementos = "000";
        }

        ModeloPrimerRespondiente_Delictivo primerRespondiente = new ModeloPrimerRespondiente_Delictivo
                (cargarIdHechoDelictivo,varIdUnidad,
                        varArriboOtroElemento,varNoElementos);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo", primerRespondiente.getIdHechoDelictivo())
                .add("IdUnidad", primerRespondiente.getIdUnidad())
                .add("MasElementos", primerRespondiente.getMasElementos())
                .add("NumElementos", primerRespondiente.getNumElementos())
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
                    PrimerRespondiente.this.getActivity().runOnUiThread(new Runnable() {
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

    private void ListCombos() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> unidad = dataHelper.getAllUnidad();
        if (unidad.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE UNIDAD");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, unidad);
            spUnidadDeArriboDelictivo.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON UNIDADES ACTIVAS.", Toast.LENGTH_LONG).show();
        }
    }

    public void cargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdPoliciaPrimerRespondiente = share.getString("Usuario", "SIN INFORMACION");
        cargarIdHechoDelictivo = share.getString("IDHECHODELICTIVO", "SIN INFORMACION");
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarPrimerespondiente() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDHechoDelictivo?folioInterno="+cargarIdHechoDelictivo)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR DATOS SECCIÓN 2, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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

                                }
                                else{
                                    //Deserializa el json y colcoa el dato correspondiente en cada campo si existe
                                    try {
                                        JSONObject jsonjObject = new JSONObject(ArregloJson);

                                        if(jsonjObject.getString("IdUnidad").equals("null"))
                                        {
                                        }
                                        else
                                        {
                                            if(jsonjObject.getString("IdUnidad").equals("NO")){
                                                chNoAplicaUnidadDeArriboDelictivo.setChecked(true);
                                            }
                                            else {
                                                spUnidadDeArriboDelictivo.setSelection(funciones.getIndexSpiner(spUnidadDeArriboDelictivo, jsonjObject.getString("IdUnidad")));
                                            }
                                        }

                                        if(jsonjObject.getString("MasElementos").equals("null"))
                                        {
                                        }
                                        else {
                                            if(jsonjObject.getString("MasElementos").equals("SI"))
                                            {
                                                chSiArriboElementosLugarIntervencion.setChecked(true);
                                                spArriboElementosLugarIntervencion.setSelection(funciones.getIndexSpiner(spArriboElementosLugarIntervencion, jsonjObject.getString("NumElementos")));
                                            }
                                            else
                                            {
                                                chNoArriboElementosLugarIntervencion.setChecked(true);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON. LLENE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL SOLICITAR INFORMACIÓN SECCIÓN 2, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

}