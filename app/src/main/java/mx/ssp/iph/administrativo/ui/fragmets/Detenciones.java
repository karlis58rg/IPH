package mx.ssp.iph.administrativo.ui.fragmets;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import mx.ssp.iph.administrativo.viewModel.DetencionesViewModel;
import mx.ssp.iph.R;
import mx.ssp.iph.principal.ui.fragments.PrincipalAdministrativo;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class Detenciones extends Fragment  {

    private DetencionesViewModel mViewModel;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    private TextView txtDescripciondelDetenido;
    private ImageView img_microfonoDescripcionDetenido,imgFirmaAutoridadAdministrativo;
    private EditText txtFechaDetenido,txthoraDetencion,txtFechaNacimientoDetenido;
    private Funciones funciones;
    SharedPreferences share;
    String cargarIdFaltaAdmin,cargarNumReferencia;
    private ListView lvDetenidos;
    ArrayList<String> ListaIdDetenido,ListaNombreDetenido;


    public static Detenciones newInstance() {
        return new Detenciones();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root  = inflater.inflate(R.layout.detenciones_fragment, container, false);
        funciones = new Funciones();
        txtDescripciondelDetenido = (TextView)root.findViewById(R.id.txtDescripciondelDetenido);
        img_microfonoDescripcionDetenido = (ImageView) root.findViewById(R.id.img_microfonoDescripcionDetenido);
        txtFechaDetenido = (EditText)root.findViewById(R.id.txtFechaDetenido);
        txthoraDetencion = (EditText)root.findViewById(R.id.txthoraDetencion);
        txtFechaNacimientoDetenido = (EditText) root.findViewById(R.id.txtFechaNacimientoDetenido);
        lvDetenidos = (ListView) root.findViewById(R.id.lvDetenidos);




        cargarFolios();
        //***************** Cargar Datos si es que existen  **************************//
        CargarDatos();
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
        imgFirmaAutoridadAdministrativo = (ImageView) root.findViewById(R.id.imgFirmaAutoridadAdministrativo);

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

        return root;
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

    public void cargarFolios(){
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdFaltaAdmin = share.getString("IDFALTAADMIN", "");
        cargarNumReferencia = share.getString("NOREFERENCIA", "");
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarDetenidos() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/DetencionesAdministrativa?folioInterno="+cargarIdFaltaAdmin)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR DETENIDOS ANEXO A, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                ListaIdDetenido = new ArrayList<String>();
                                ListaNombreDetenido = new ArrayList<String>();

                                //***************** RESPUESTA DEL WEBSERVICE **************************//

                                //CONVERTIR ARREGLO DE JSON A OBJET JSON
                                String ArregloJson = resp.replace("[", "");
                                ArregloJson = ArregloJson.replace("]", "");

                                if(ArregloJson.equals(""))
                                {
                                    //Sin Información. Todos los campos vacíos.

                                }
                                else{
                                    //SEPARAR CADA detenido EN UN ARREGLO
                                    String[] ArrayIPHAdministrativo = ArregloJson.split(Pattern.quote("},"));

                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordeDetenido=0;
                                    while(contadordeDetenido < ArrayIPHAdministrativo.length){
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayIPHAdministrativo[contadordeDetenido] + "}");

                                            ListaIdDetenido.add(jsonjObject.getString("NumDetencion"));
                                            ListaNombreDetenido.add(((jsonjObject.getString("APDetenido")).equals("null")?"":jsonjObject.getString("APDetenido")) +
                                                    " "+((jsonjObject.getString("AMDetenido")).equals("null")?"":jsonjObject.getString("AMDetenido")) +
                                                    " "+ ((jsonjObject.getString("NomDetenido")).equals("null")?"":jsonjObject.getString("NomDetenido")) +
                                                    " (" +((jsonjObject.getString("ApodoAlias")).equals("null")?"":jsonjObject.getString("ApodoAlias"))+" )" );

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordeDetenido++;
                                    }
                                }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                Detenciones.MyAdapter adapter = new Detenciones.MyAdapter(getContext(), ListaIdDetenido, ListaNombreDetenido);
                                lvDetenidos.setAdapter(adapter);
                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL SOLICITAR INFORMACION NO DE REFERENCIA, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    //***************** SE RECUPERA EL FOLIO INTERNO **************************//
    private void CargarDatos() {

        if (cargarIdFaltaAdmin.equals(""))
        {
            Toast.makeText(getContext(), "EL FOLIO INTERNO NO EXISTE. POR FAVOR REINCICIE LA APP CREE UN NUEVO INFORME.", Toast.LENGTH_LONG).show();
        }
        else
        {
            //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
            if (funciones.ping(getContext()))
            {
                //Toast.makeText(getContext(), "cargarNoReferenciaAdministrativa()", Toast.LENGTH_LONG).show();
                cargarDetenidos();
            }
        }

    }

    //***************** ADAPTADOR PARA LLENAR LISTA DE IPH ADMINISTRATIVO **************************//
    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> ListaIdDetenido,ListaNombreDetenido;

        MyAdapter (Context c, ArrayList<String> ListaIdDetenido, ArrayList<String> ListaNombreDetenido) {
            super(c, R.layout.row_iph, ListaIdDetenido);
            this.context = c;
            this.ListaIdDetenido = ListaIdDetenido;
            this.ListaNombreDetenido = ListaNombreDetenido;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_detenidos, parent, false);

            TextView lblNumDetencion = row.findViewById(R.id.lblNumDetencion);
            TextView lblNombreCompleto = row.findViewById(R.id.lblNombreCompleto);

            // Asigna los valores
            lblNumDetencion.setText("DETENIDO NÚMERO "+ListaIdDetenido.get(position));
            lblNombreCompleto.setText(ListaNombreDetenido.get(position));

            return row;
        }
    }

}