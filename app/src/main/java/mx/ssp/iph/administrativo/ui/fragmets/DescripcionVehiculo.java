package mx.ssp.iph.administrativo.ui.fragmets;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import mx.ssp.iph.administrativo.viewModel.DescripcionVehiculoViewModel;
import mx.ssp.iph.R;
import mx.ssp.iph.utilidades.ui.Funciones;

import static android.app.Activity.RESULT_OK;

public class DescripcionVehiculo extends Fragment {

    private DescripcionVehiculoViewModel mViewModel;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    private TextView txtObservacionesdelVehiculo;
    private ImageView imgMicrofonoObservacionesdelVehiculo;
    private EditText txthoraRetencion,txtFechaRetencion;
    private Funciones funciones;

    public static DescripcionVehiculo newInstance() {
        return new DescripcionVehiculo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.descripcion_vehiculo_fragment, container, false);
        funciones = new Funciones();
        txtObservacionesdelVehiculo = (TextView)root.findViewById(R.id.txtObservacionesdelVehiculo);
        imgMicrofonoObservacionesdelVehiculo = (ImageView) root.findViewById(R.id.imgMicrofonoObservacionesdelVehiculo);
        txthoraRetencion = (EditText)root.findViewById(R.id.txthoraRetencion);
        txtFechaRetencion = (EditText)root.findViewById(R.id.txtFechaRetencion);


        //Imagen que funciona para activar la grabación de voz
        imgMicrofonoObservacionesdelVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarEntradadeVoz();
            }
        });

        //FEcha
        txthoraRetencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txthoraRetencion,getContext(),getActivity());
            }
        });

        //FEcha
        txthoraRetencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txthoraRetencion,getContext(),getActivity());
            }
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DescripcionVehiculoViewModel.class);
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
                    String textoActual = txtObservacionesdelVehiculo.getText().toString();
                    txtObservacionesdelVehiculo.setText(textoActual+" " + result.get(0));
                }
                break;
            }
        }
    }

}