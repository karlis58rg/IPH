package mx.ssp.iph.delictivo.ui.fragmets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import mx.ssp.iph.R;
import mx.ssp.iph.delictivo.viewModel.EntregaRecepcion_DelictivoViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;


public class EntregaRecepcion_Delictivo extends Fragment {

    private EntregaRecepcion_DelictivoViewModel mViewModel;
    private Funciones funciones;
    private ImageView imgMicrofonoAccionesRealizadas,imgMicrofonoObservacionesLugarIntervencion;
    private EditText txtAccionesRealizadas,txtObservacionesLugarIntervencion;
    private static final  int REQ_CODE_SPEECH_INPUT=100;

    private ImageView btnAgregarPersona;
    private ListView lvPersonas;

    ArrayList<String> ListaJsonPertenencias;
    ArrayList<String> ListPertenencia,ListDescripcionPertenencia,ListDestino,ListaTablaPertenencias,ListaIdPertenencias;
    SharedPreferences share;


    public static EntregaRecepcion_Delictivo newInstance() {
        return new EntregaRecepcion_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entrega_recepcion_fragment, container, false);
        /***************************************************************************************/
        funciones = new Funciones();
        funciones.CambiarTituloSeccionesDelictivo("ANEXO F. ENTREGA-RECPCIÓN DEL LUGAR DE LA INTERVENCIÓN",getContext(),getActivity());

        //************************************** ACCIONES DE LA VISTA **************************************//
        //Botones Imagen
        imgMicrofonoAccionesRealizadas = view.findViewById(R.id.imgMicrofonoAccionesRealizadas);
        imgMicrofonoObservacionesLugarIntervencion = view.findViewById(R.id.imgMicrofonoObservacionesLugarIntervencion);

        //EditText
        txtAccionesRealizadas = view.findViewById(R.id.txtAccionesRealizadas);
        txtAccionesRealizadas.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});
        txtObservacionesLugarIntervencion = view.findViewById(R.id.txtObservacionesLugarIntervencion);
        txtObservacionesLugarIntervencion.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});

        //Personas
        btnAgregarPersona = view.findViewById(R.id.btnAgregarPersona);
        lvPersonas = view.findViewById(R.id.lvPersonas);


        //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
        if (funciones.ping(getContext()))
        {
            //Toast.makeText(getContext(), "cargarNoReferenciaAdministrativa()", Toast.LENGTH_LONG).show();
            //cargarEntregaRecepcion();
        }


        //Imagen que funciona para activar la grabación de voz
        imgMicrofonoAccionesRealizadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMicrofonoAccionesRealizadas.setImageResource(R.drawable.ic_micro_press);
                imgMicrofonoAccionesRealizadas.setTag(R.drawable.ic_micro_press);

                iniciarEntradadeVoz();
            }
        });

        imgMicrofonoObservacionesLugarIntervencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMicrofonoObservacionesLugarIntervencion.setImageResource(R.drawable.ic_micro_press);
                imgMicrofonoObservacionesLugarIntervencion.setTag(R.drawable.ic_micro_press);

                iniciarEntradadeVoz();
            }
        });

     lvPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        mViewModel = new ViewModelProvider(this).get(EntregaRecepcion_DelictivoViewModel.class);
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
                    Integer resource = (Integer) imgMicrofonoAccionesRealizadas.getTag();

                    if (resource == R.drawable.ic_micro_press)
                    {
                        String textoActual = txtAccionesRealizadas.getText().toString();
                        txtAccionesRealizadas.setText(textoActual+" " + result.get(0));
                    }
                    else
                    {
                        String textoActual = txtObservacionesLugarIntervencion.getText().toString();
                        txtObservacionesLugarIntervencion.setText(textoActual+" " + result.get(0));
                    }

                }
                break;
            }
        }

        imgMicrofonoAccionesRealizadas.setImageResource(R.drawable.ic_micro);
        imgMicrofonoAccionesRealizadas.setTag(R.drawable.ic_micro);

        imgMicrofonoObservacionesLugarIntervencion.setImageResource(R.drawable.ic_micro);
        imgMicrofonoObservacionesLugarIntervencion.setTag(R.drawable.ic_micro);
    }
}