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

import mx.ssp.iph.administrativo.viewModel.DetencionesViewModel;
import mx.ssp.iph.R;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.Funciones;

import static android.app.Activity.RESULT_OK;

public class Detenciones extends Fragment  {

    private DetencionesViewModel mViewModel;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    private TextView txtDescripciondelDetenido;
    private ImageView img_microfonoDescripcionDetenido,imgFirmaAutoridadAdministrativo;
    private EditText txtFechaDetenido,txthoraDetencion,txtFechaNacimientoDetenido;
    private Funciones funciones;

    public static Detenciones newInstance() {
        return new Detenciones();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.detenciones_fragment, container, false);
        funciones = new Funciones();
        txtDescripciondelDetenido = (TextView)view.findViewById(R.id.txtDescripciondelDetenido);
        img_microfonoDescripcionDetenido = (ImageView) view.findViewById(R.id.img_microfonoDescripcionDetenido);
        txtFechaDetenido = (EditText)view.findViewById(R.id.txtFechaDetenido);
        txthoraDetencion = (EditText)view.findViewById(R.id.txthoraDetencion);
        txtFechaNacimientoDetenido = (EditText) view.findViewById(R.id.txtFechaNacimientoDetenido);

        //Fecha
        txtFechaNacimientoDetenido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaNacimientoDetenido,getContext(),getActivity());
            }
        });

        //Fecha
        txtFechaDetenido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"CLick",Toast.LENGTH_SHORT).show();
                funciones.calendar(R.id.txtFechaDetenido,getContext(),getActivity());
            }
        });

        //Hora
        txthoraDetencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txthoraDetencion,getContext(),getActivity());
            }
        });


        //Firma del Detenido
        imgFirmaAutoridadAdministrativo = (ImageView) view.findViewById(R.id.imgFirmaAutoridadAdministrativo);

        //Imagen que funciona para activar la grabación de voz
        img_microfonoDescripcionDetenido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarEntradadeVoz();
            }
        });

        //Imagen que funciona para activar la firma
        imgFirmaAutoridadAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirma dialog = new ContenedorFirma(R.id.lblFirmadelDetenido,R.id.lblFirmaOcultaDetenidoBase64);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetencionesViewModel.class);
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
                    String textoActual = txtDescripciondelDetenido.getText().toString();
                    txtDescripciondelDetenido.setText(textoActual+" " + result.get(0));
                }
                break;
            }
        }
    }

}