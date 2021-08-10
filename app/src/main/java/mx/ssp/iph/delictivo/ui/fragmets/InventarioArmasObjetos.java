package mx.ssp.iph.delictivo.ui.fragmets;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import mx.ssp.iph.delictivo.viewModel.InventarioArmasObjetosViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;

import static android.app.Activity.RESULT_OK;

public class InventarioArmasObjetos extends Fragment {

    private InventarioArmasObjetosViewModel mViewModel;
    private Funciones funciones;
    private ImageView imgMicrofonoObservacionesArma,imgFirmaEntrevistado,imgFirmaTestigo1Arma,imgFirmaTestigo2Arma;
    private EditText txtObservacionesArma;
    private static final  int REQ_CODE_SPEECH_INPUT=100;


    public static InventarioArmasObjetos newInstance() {
        return new InventarioArmasObjetos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventario_armas_objetos, container, false);
        funciones= new Funciones();
        funciones.CambiarTituloSeccionesDelictivo("ANEXO D. INVENTARIO DE ARMAS Y OBJETOS",getContext(),getActivity());

        //************************************** ACCIONES DE LA VISTA **************************************//
        //Botones Imagen
        imgMicrofonoObservacionesArma = view.findViewById(R.id.imgMicrofonoObservacionesArma);
        imgFirmaEntrevistado = view.findViewById(R.id.imgFirmaEntrevistado);
        imgFirmaTestigo1Arma = view.findViewById(R.id.imgFirmaTestigo1Arma);
        imgFirmaTestigo2Arma = view.findViewById(R.id.imgFirmaTestigo2Arma);

        //EditText
        txtObservacionesArma= view.findViewById(R.id.txtObservacionesArma);



        //Imagen que funciona para activar la grabación de voz
        imgMicrofonoObservacionesArma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMicrofonoObservacionesArma.setImageResource(R.drawable.ic_micro_press);
                iniciarEntradadeVoz();
            }
        });




        /***************************************************************************************/
        return view;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InventarioArmasObjetosViewModel.class);
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
                    String textoActual = txtObservacionesArma.getText().toString();
                    txtObservacionesArma.setText(textoActual+" " + result.get(0));
                }
                break;
            }

        }
        imgMicrofonoObservacionesArma.setImageResource(R.drawable.ic_micro);
    }


}