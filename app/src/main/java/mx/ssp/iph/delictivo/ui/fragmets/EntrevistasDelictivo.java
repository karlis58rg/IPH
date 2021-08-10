package mx.ssp.iph.delictivo.ui.fragmets;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Locale;

import mx.ssp.iph.R;
import mx.ssp.iph.delictivo.viewModel.ConocimientoHechoViewModel;
import mx.ssp.iph.delictivo.viewModel.EntrevistasDelictivoViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;

import static android.app.Activity.RESULT_OK;

public class EntrevistasDelictivo extends Fragment {

    private EntrevistasDelictivoViewModel mViewModel;

    private Funciones funciones;
    private EditText txtFechaEntrevista,txtHoraEntrevista,txtFechaNacimientoEntrevistado,txtEntrevista;
    ImageView imgMicrofonoEntrevista;
    private static final  int REQ_CODE_SPEECH_INPUT=100;


    public static EntrevistasDelictivo newInstance() {
        return new EntrevistasDelictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entrevistas_delictivo_fragment, container, false);
        /************************************************************************************/
        funciones = new Funciones();
        funciones.CambiarTituloSeccionesDelictivo("ANEXO E. ENTREVISTAS",getContext(),getActivity());

        //************************************** ACCIONES DE LA VISTA **************************************//
        //Botones Imagen
        imgMicrofonoEntrevista = view.findViewById(R.id.imgMicrofonoEntrevista);

        //EditText
        txtEntrevista = view.findViewById(R.id.txtEntrevista);
        txtFechaEntrevista = view.findViewById(R.id.txtFechaEntrevista);
        txtHoraEntrevista = view.findViewById(R.id.txtHoraEntrevista);
        txtFechaNacimientoEntrevistado = view.findViewById(R.id.txtFechaNacimientoEntrevistado);


        //Imagen que funciona para activar la grabación de voz
        imgMicrofonoEntrevista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMicrofonoEntrevista.setImageResource(R.drawable.ic_micro_press);
                iniciarEntradadeVoz();
            }
        });

        //***************** FECHA  **************************//
        txtFechaEntrevista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaEntrevista,getContext(),getActivity());
            }
        });

        //***************** FECHA  **************************//
        txtFechaNacimientoEntrevistado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaNacimientoEntrevistado,getContext(),getActivity());
            }
        });

        //***************** HORA **************************//
        txtHoraEntrevista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txtHoraEntrevista,getContext(),getActivity());
            }
        });


        /****************************************************************************************/
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EntrevistasDelictivoViewModel.class);
        // TODO: Use the ViewModel
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

                    String textoActual = txtEntrevista.getText().toString();
                    txtEntrevista.setText(textoActual+" " + result.get(0));

                }
                break;
            }
        }
        imgMicrofonoEntrevista.setImageResource(R.drawable.ic_micro);
    }

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

}