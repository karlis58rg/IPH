package mx.ssp.iph.delictivo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import mx.ssp.iph.R;
import mx.ssp.iph.delictivo.viewModel.NarrativaHechosDelictivoViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;

import static android.app.Activity.RESULT_OK;

public class NarrativaHechos_Delictivo extends Fragment {

    private NarrativaHechosDelictivoViewModel mViewModel;
    Button btnGuardarNarrativaHechosDelictivo;
    private ImageView imgMicrofonoNarrativaHechosDelictivo;
    private EditText txtNarrativaHechosDelictivo;
    private Funciones funciones;
    private static final  int REQ_CODE_SPEECH_INPUT=100;


    public static NarrativaHechos_Delictivo newInstance() {
        return new NarrativaHechos_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.narrativa_hechos__delictivo_fragment, container, false);
        /***************************************************************************************/
        btnGuardarNarrativaHechosDelictivo = view.findViewById(R.id.btnGuardarNarrativaHechosDelictivo);
        imgMicrofonoNarrativaHechosDelictivo = view.findViewById(R.id.imgMicrofonoNarrativaHechosDelictivo);
        txtNarrativaHechosDelictivo = view.findViewById(R.id.txtNarrativaHechosDelictivo);
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
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
            }
        });


        /***************************************************************************************/
        return view;
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

}