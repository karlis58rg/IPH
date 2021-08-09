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
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.ui.fragmets.NarrativaHechos;
import mx.ssp.iph.delictivo.model.ModeloConocimientoHecho_Delictivo;
import mx.ssp.iph.delictivo.viewModel.RegistroArmasObjetosDelictivoViewModel;
import mx.ssp.iph.delictivo.viewModel.NarrativaHechosDelictivoViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class NarrativaHechos_Delictivo extends Fragment {

    private NarrativaHechosDelictivoViewModel mViewModel;
    Button btnGuardarNarrativaHechosDelictivo;
    private ImageView imgMicrofonoNarrativaHechosDelictivo;
    private EditText txtNarrativaHechosDelictivo;
    private Funciones funciones;
    private ViewGroup tercerLinear;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    String descNarrativa,cargarIdPoliciaPrimerRespondiente,cargarIdHechoDelictivo;
    SharedPreferences share;


    public static NarrativaHechos_Delictivo newInstance() {
        return new NarrativaHechos_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.narrativa_hechos__delictivo_fragment, container, false);
        /***************************************************************************************/
        cargarDatos();
        btnGuardarNarrativaHechosDelictivo = view.findViewById(R.id.btnGuardarNarrativaHechosDelictivo);
        imgMicrofonoNarrativaHechosDelictivo = view.findViewById(R.id.imgMicrofonoNarrativaHechosDelictivo);
        txtNarrativaHechosDelictivo = view.findViewById(R.id.txtNarrativaHechosDelictivo);
        tercerLinear = view.findViewById(R.id.tercerLinear);
        txtNarrativaHechosDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});
        funciones = new Funciones();

        funciones.CambiarTituloSeccionesDelictivo("SECCIÓN 5. NARRATIVA DE LOS HECHOS",getContext(),getActivity());

        //Imagen que funciona para activar la grabación de voz
        imgMicrofonoNarrativaHechosDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarEntradadeVoz();
            }
        });


        btnGuardarNarrativaHechosDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtNarrativaHechosDelictivo.getText().length() < 3){
                    Toast.makeText(getActivity().getApplicationContext(),"INGRESA LA DESCRIPCIÓN DE LOS HECHOS",Toast.LENGTH_SHORT).show();
                    tercerLinear.requestFocus();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                    updateNarrativaHechoDelictivo();
                }
            }
        });


        /***************************************************************************************/
        return view;
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void updateNarrativaHechoDelictivo() {
        descNarrativa = txtNarrativaHechosDelictivo.getText().toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo", cargarIdHechoDelictivo)
                .add("NarrativaHechos", descNarrativa)
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
                    NarrativaHechos_Delictivo.this.getActivity().runOnUiThread(new Runnable() {
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NarrativaHechosDelictivoViewModel.class);
        // TODO: Use the ViewModel
    }

    //Método que inicia el intent para de grabar la voz
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

    //Almacena la Respuesta de la lectura de voz y la coloca en el Cuadro de Texto Correspondiente
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if (resultCode== RESULT_OK && null != data)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String textoActual = txtNarrativaHechosDelictivo.getText().toString();
                    txtNarrativaHechosDelictivo.setText(textoActual+" " + result.get(0));
                }
                break;
            }
        }
    }

    public void cargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdPoliciaPrimerRespondiente = share.getString("Usuario", "SIN INFORMACION");
        cargarIdHechoDelictivo = share.getString("IDHECHODELICTIVO", "SIN INFORMACION");
    }
}