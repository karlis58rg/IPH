package mx.ssp.iph.delictivo.ui.fragmets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModeloDescripcionVehiculos_Administrativo;
import mx.ssp.iph.administrativo.ui.fragmets.DescripcionVehiculo;
import mx.ssp.iph.delictivo.model.ModeloArmasFuego_Delictivo;
import mx.ssp.iph.delictivo.model.ModeloObjetos_Delictivo;
import mx.ssp.iph.delictivo.viewModel.InventarioArmasObjetosViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorFirmaDelictivo;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class InventarioArmasObjetos extends Fragment {

    private InventarioArmasObjetosViewModel mViewModel;
    private Funciones funciones;
    ImageView btnAgregarArma,imgMicrofonoObservacionesArma,imgFirmaEntrevistadoAO,imgFirmaTestigo1Arma,imgFirmaTestigo2Arma,
            imgMicrofonoObservacionesObjetos,imgFirmaPropietarioObjetos,imgFirmaTestigo1Objeto,imgFirmaTestigo2Objeto,btnAgregarObjeto;
    EditText txtLugarEncontroArma,txtCalibreArmaDelictivo, txtMatriculaArmaDelictivo,txtNoSerieArmaDelictivo,txtDestinoArma,txtObservacionesArma,
            txtPrimerApellidoPropietarioArma,txtSegundoApellidoPropietarioArma,txtNombresPropietarioArma,
            txtPrimerApellidoTestigo1Arma,txtSegundoApellidoTestigo1Arma,txtNombresTestigo1Arma,
            txtPrimerApellidoTestigo2Arma,txtSegundoApellidoTestigo2Arma,txtNombresTestigo2Arma,
            txtOtroObjeto,txtLugarEncontroObjetos,txtObservacionesObjetos,txtDestinoObjetos,
            txtPrimerApellidoPropietarioObjetos,txtSegundoApellidoPropietarioObjetos,txtNombresPropietarioObjetos,
            txtPrimerApellidoTestigo1Objeto,txtSegundoApellidoTestigo1Objeto,txtNombresTestigo1Objeto,
            txtPrimerApellidoTestigo2Objeto,txtSegundoApellidoTestigo2Objeto,txtNombresTestigo2Objeto;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    RadioGroup rgAportacionInspeccionArmaFuego,rgTipoArma,
            rgTipoObjeto,rgAportacionInspeccionObjetos;
    Spinner spColorArmaDelictivo;
    String descripcionColor,cargarIdPoliciaPrimerRespondiente,cargarIdHechoDelictivo,varAportacion,varInspeccion,varTipoArma,varRutaImagen,varRutaImagenT1,varRutaImagenT2,
            varTipoObjeto,varAportacionObjeto,varInspeccionObjeto,varRutaFirmaObjeto,varRutaImagenT1Objeto,varRutaImagenT2Objeto,cadenaImagenFirmaArmas,cadenaPersona;
    SharedPreferences share;
    int numberRandom,randomUrlImagen;
    TextView lblFirmadelEntrevistadoOcultoAO,lblFirmaTestigo1ArmaOculto,lblFirmaTestigo2ArmaOculto,
            lblFirmadelPropietarioObjetosOculto,lblFirmaTestigo1ObjetoOculto,lblFirmaTestigo2ObjetoOculto;

    ListView lvObjeto;
    ArrayList<String> ListaIdObjetos,ListaDatosObjetos;
    String[] ArrayListaIPHDelictivo;

    ListView lvArmas;
    ArrayList<String> ListaIdArmas,ListaDatosArmas;
    String[] ArrayListaIPHDelictivoArmas;


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
        //************************************** ARMAS DE FUEGO *******************************************//
        cargarDatos();
        random();
        imgMicrofonoObservacionesArma = view.findViewById(R.id.imgMicrofonoObservacionesArma);
        imgFirmaEntrevistadoAO = view.findViewById(R.id.imgFirmaEntrevistadoAO);
        imgFirmaTestigo1Arma = view.findViewById(R.id.imgFirmaTestigo1Arma);
        imgFirmaTestigo2Arma = view.findViewById(R.id.imgFirmaTestigo2Arma);
        lblFirmadelEntrevistadoOcultoAO = view.findViewById(R.id.lblFirmadelEntrevistadoOcultoAO);
        lblFirmaTestigo1ArmaOculto = view.findViewById(R.id.lblFirmaTestigo1ArmaOculto);
        lblFirmaTestigo2ArmaOculto = view.findViewById(R.id.lblFirmaTestigo2ArmaOculto);
        txtLugarEncontroArma = view.findViewById(R.id.txtLugarEncontroArma);
        txtCalibreArmaDelictivo = view.findViewById(R.id.txtCalibreArmaDelictivo);
        txtMatriculaArmaDelictivo = view.findViewById(R.id.txtMatriculaArmaDelictivo);
        txtNoSerieArmaDelictivo = view.findViewById(R.id.txtNoSerieArmaDelictivo);
        txtObservacionesArma = view.findViewById(R.id.txtObservacionesArma);
        txtDestinoArma = view.findViewById(R.id.txtDestinoArma);
        txtPrimerApellidoPropietarioArma = view.findViewById(R.id.txtPrimerApellidoPropietarioArma);
        txtSegundoApellidoPropietarioArma = view.findViewById(R.id.txtSegundoApellidoPropietarioArma);
        txtNombresPropietarioArma = view.findViewById(R.id.txtNombresPropietarioArma);
        txtPrimerApellidoTestigo1Arma = view.findViewById(R.id.txtPrimerApellidoTestigo1Arma);
        txtSegundoApellidoTestigo1Arma = view.findViewById(R.id.txtSegundoApellidoTestigo1Arma);
        txtNombresTestigo1Arma = view.findViewById(R.id.txtNombresTestigo1Arma);
        txtPrimerApellidoTestigo2Arma = view.findViewById(R.id.txtPrimerApellidoTestigo2Arma);
        txtSegundoApellidoTestigo2Arma = view.findViewById(R.id.txtSegundoApellidoTestigo2Arma);
        txtNombresTestigo2Arma = view.findViewById(R.id.txtNombresTestigo2Arma);
        rgAportacionInspeccionArmaFuego = view.findViewById(R.id.rgAportacionInspeccionArmaFuego);
        rgTipoArma = view.findViewById(R.id.rgTipoArma);
        spColorArmaDelictivo = view.findViewById(R.id.spColorArmaDelictivo);
        btnAgregarArma = view.findViewById(R.id.btnAgregarArma);
        ListCombos();
        //**************************************************************************************************//
        //************************************** ACCIONES DE LA VISTA **************************************//
        //************************************** OBJETOS *******************************************//
        txtOtroObjeto = view.findViewById(R.id.txtOtroObjeto);
        txtLugarEncontroObjetos = view.findViewById(R.id.txtLugarEncontroObjetos);
        txtObservacionesObjetos = view.findViewById(R.id.txtObservacionesObjetos);
        txtDestinoObjetos = view.findViewById(R.id.txtDestinoObjetos);
        txtPrimerApellidoPropietarioObjetos = view.findViewById(R.id.txtPrimerApellidoPropietarioObjetos);
        txtSegundoApellidoPropietarioObjetos = view.findViewById(R.id.txtSegundoApellidoPropietarioObjetos);
        txtNombresPropietarioObjetos = view.findViewById(R.id.txtNombresPropietarioObjetos);
        txtPrimerApellidoTestigo1Objeto = view.findViewById(R.id.txtPrimerApellidoTestigo1Objeto);
        txtSegundoApellidoTestigo1Objeto = view.findViewById(R.id.txtSegundoApellidoTestigo1Objeto);
        txtNombresTestigo1Objeto = view.findViewById(R.id.txtNombresTestigo1Objeto);
        txtPrimerApellidoTestigo2Objeto = view.findViewById(R.id.txtPrimerApellidoTestigo2Objeto);
        txtSegundoApellidoTestigo2Objeto = view.findViewById(R.id.txtSegundoApellidoTestigo2Objeto);
        txtNombresTestigo2Objeto = view.findViewById(R.id.txtNombresTestigo2Objeto);
        rgTipoObjeto = view.findViewById(R.id.rgTipoObjeto);
        rgAportacionInspeccionObjetos = view.findViewById(R.id.rgAportacionInspeccionObjetos);
        imgMicrofonoObservacionesObjetos = view.findViewById(R.id.imgMicrofonoObservacionesObjetos);
        imgFirmaPropietarioObjetos = view.findViewById(R.id.imgFirmaPropietarioObjetos);
        imgFirmaTestigo1Objeto = view.findViewById(R.id.imgFirmaTestigo1Objeto);
        imgFirmaTestigo2Objeto = view.findViewById(R.id.imgFirmaTestigo2Objeto);
        lblFirmadelPropietarioObjetosOculto = view.findViewById(R.id.lblFirmadelPropietarioObjetosOculto);
        lblFirmaTestigo1ObjetoOculto = view.findViewById(R.id.lblFirmaTestigo1ObjetoOculto);
        lblFirmaTestigo2ObjetoOculto = view.findViewById(R.id.lblFirmaTestigo2ObjetoOculto);
        btnAgregarObjeto = view.findViewById(R.id.btnAgregarObjeto);

        lvObjeto = view.findViewById(R.id.lvObjeto);
        lvArmas  = view.findViewById(R.id.lvArmas);

        //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
        if (funciones.ping(getContext()))
        {
            //Toast.makeText(getContext(), "cargarNoReferenciaAdministrativa()", Toast.LENGTH_LONG).show();
            cargarArmas();
            cargarObjetos();
        }

        //**************************************************************************************************//
        //Clic a la lista
        lvObjeto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext()).
                                setMessage("¿DESEA ELIMINAR EL OBJETO "+ ListaIdObjetos.get(position) + "?" ).
                                setPositiveButton( "ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //CONSUME WEB SERVICE PARA ELIMINAR DB
                                        EliminarObjetos(ListaIdObjetos.get(position));
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        dialog.dismiss();
                                    }
                                });

                builder.create().show();
            }
        });

        //Clic a la lista
        lvArmas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext()).
                                setMessage("¿DESEA ELIMINAR EL ARMA "+ ListaIdArmas.get(position) + "?" ).
                                setPositiveButton( "ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //CONSUME WEB SERVICE PARA ELIMINAR DB
                                        EliminarArmas(ListaIdArmas.get(position));
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                        dialog.dismiss();
                                    }
                                });

                builder.create().show();
            }
        });

        //Imagen que funciona para activar la grabación de voz
        imgMicrofonoObservacionesArma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMicrofonoObservacionesArma.setImageResource(R.drawable.ic_micro_press);
                imgMicrofonoObservacionesArma.setTag(R.drawable.ic_micro_press);
                iniciarEntradadeVoz();
            }
        });
        imgMicrofonoObservacionesObjetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgMicrofonoObservacionesObjetos.setImageResource(R.drawable.ic_micro_press);
                imgMicrofonoObservacionesObjetos.setTag(R.drawable.ic_micro_press);
                iniciarEntradadeVoz();
            }
        });

        rgAportacionInspeccionArmaFuego.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rbAportacion) {
                    varAportacion = "SI";
                } else{
                    varAportacion = "NO";
                }
                if (checkedId == R.id.rbInspeccionLugar) {
                    varInspeccion = "INSPECCION LUGAR";
                }else if(checkedId == R.id.rbInspeccionPersona){
                    varInspeccion = "INSPECCION PERSONA";
                }else if(checkedId == R.id.rbInspeccionVehiculo){
                    varInspeccion = "INSPECCION VEHICULO";
                }
            }
        });

        rgTipoArma.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rbArmaCorta) {
                    varTipoArma = "ARMA CORTA";
                }else if(checkedId == R.id.rbArmaLarga){
                    varTipoArma = "ARMA LARGA";
                }
            }
        });

        btnAgregarArma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();

                if(lblFirmadelEntrevistadoOcultoAO.getText().toString().isEmpty()){
                }else{
                    cadenaPersona = "propietario_";
                    cadenaImagenFirmaArmas = lblFirmadelEntrevistadoOcultoAO.getText().toString();
                    insertImagenFirmaArmas();
                }

                if(lblFirmaTestigo1ArmaOculto.getText().toString().isEmpty()){
                }else{
                    cadenaPersona = "testigo1_";
                    cadenaImagenFirmaArmas = lblFirmaTestigo1ArmaOculto.getText().toString();
                    insertImagenFirmaArmas();
                }

                if(lblFirmaTestigo2ArmaOculto.getText().toString().isEmpty()){
                }else{
                    cadenaPersona = "testigo2_";
                    cadenaImagenFirmaArmas = lblFirmaTestigo2ArmaOculto.getText().toString();
                    insertImagenFirmaArmas();
                }
                    insertArmasFuego();
            }
        });
        rgAportacionInspeccionObjetos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rbAportacionObjetos) {
                    varAportacionObjeto = "SI";
                } else{
                    varAportacionObjeto = "NO";
                }
                if (checkedId == R.id.rbInspeccionLugarObjetos) {
                    varInspeccionObjeto = "INSPECCION LUGAR";
                }else if(checkedId == R.id.rbInspeccionPersonaObjetos){
                    varInspeccionObjeto = "INSPECCION PERSONA";
                }else if(checkedId == R.id.rbInspeccionVehiculoObjetos){
                    varInspeccionObjeto = "INSPECCION VEHICULO";
                }
            }
        });

        rgTipoObjeto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rbNarcotico) {
                    varTipoObjeto = "NARCOTICO";
                }else if(checkedId == R.id.rbHidrocarburo){
                    varTipoObjeto = "HIDROCARBURO";
                }else if(checkedId == R.id.rbNumerario){
                    varTipoObjeto = "NUMERARIO";
                }else{
                    varTipoObjeto = "NA";
                }
            }
        });

        btnAgregarObjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                insertObjetos();
            }
        });

        //***************** FIRMAS **************************//
        imgFirmaEntrevistadoAO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirmaDelictivo dialog = new ContenedorFirmaDelictivo(R.id.lblFirmadelEntrevistadoAO,R.id.lblFirmadelEntrevistadoOcultoAO,R.id.imgFirmadelEntrevistadoMiniaturaAO);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });
        //***************** FIRMA **************************//
        imgFirmaTestigo1Arma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirmaDelictivo dialog = new ContenedorFirmaDelictivo(R.id.lblStatusFirmaTestigo1Arma,R.id.lblFirmaTestigo1ArmaOculto,R.id.imgFirmaTestigo1ArmaMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });
        //***************** FIRMA **************************//
        imgFirmaTestigo2Arma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirmaDelictivo dialog = new ContenedorFirmaDelictivo(R.id.lblStatusFirmaTestigo2Arma,R.id.lblFirmaTestigo2ArmaOculto,R.id.imgFirmaTestigo2ArmaMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });
        //***************** FIRMA **************************//
        imgFirmaPropietarioObjetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirmaDelictivo dialog = new ContenedorFirmaDelictivo(R.id.lblFirmaPropietarioObjetos,R.id.lblFirmadelPropietarioObjetosOculto,R.id.imgFirmadelPropietarioObjetosMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });
        //***************** FIRMA **************************//
        imgFirmaTestigo1Objeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirmaDelictivo dialog = new ContenedorFirmaDelictivo(R.id.lblStatusFirmaTestigo1Objeto,R.id.lblFirmaTestigo1ObjetoOculto,R.id.imgFirmaTestigo1ObjetoMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });
        //***************** FIRMA **************************//
        imgFirmaTestigo2Objeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirmaDelictivo dialog = new ContenedorFirmaDelictivo(R.id.lblStatusFirmaTestigo2Objeto,R.id.lblFirmaTestigo2ObjetoOculto,R.id.imgFirmaTestigo2ObjetoMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
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
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Integer resource2 = (Integer) imgMicrofonoObservacionesArma.getTag();

                    if (resource2 == R.drawable.ic_micro_press ) {
                        String textoActual = txtObservacionesArma.getText().toString();
                        txtObservacionesArma.setText(textoActual + " " + result.get(0));
                    } else {
                        String textoActual = txtObservacionesObjetos.getText().toString();
                        txtObservacionesObjetos.setText(textoActual + " " + result.get(0));
                    }

                }
                break;
            }
        }
        imgMicrofonoObservacionesArma.setImageResource(R.drawable.ic_micro);
        imgMicrofonoObservacionesArma.setTag(R.drawable.ic_micro);

        imgMicrofonoObservacionesObjetos.setImageResource(R.drawable.ic_micro);
        imgMicrofonoObservacionesObjetos.setTag(R.drawable.ic_micro);
    }
    private void insertArmasFuego() {
        descripcionColor = (String) spColorArmaDelictivo.getSelectedItem();

        if(txtNombresPropietarioArma.getText().toString().equals("NA")){
            varRutaImagen = "NA";
        }else{
            varRutaImagen = "http://189.254.7.167/WebServiceIPH/FirmaArmas/"+"propietario_"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
        }
        if(txtNombresTestigo1Arma.getText().toString().equals("NA")){
            varRutaImagenT1 = "NA";
        }else{
            varRutaImagenT1 = "http://189.254.7.167/WebServiceIPH/FirmaArmas/"+"testigo1_"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
        }
        if(txtNombresTestigo2Arma.getText().toString().equals("NA")){
            varRutaImagenT2 = "NA";

        }else{
            varRutaImagenT2 = "http://189.254.7.167/WebServiceIPH/FirmaArmas/"+"testigo2_"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
        }

        ModeloArmasFuego_Delictivo modeloArmasFuego = new ModeloArmasFuego_Delictivo
                (cargarIdHechoDelictivo, varAportacion, varInspeccion, txtLugarEncontroArma.getText().toString(), varTipoArma,
                        txtCalibreArmaDelictivo.getText().toString(), descripcionColor, txtMatriculaArmaDelictivo.getText().toString(),
                        txtNoSerieArmaDelictivo.getText().toString(), txtObservacionesArma.getText().toString(), txtDestinoArma.getText().toString(),
                        txtPrimerApellidoPropietarioArma.getText().toString(), txtSegundoApellidoPropietarioArma.getText().toString(),
                        txtNombresPropietarioArma.getText().toString(),varRutaImagen, txtPrimerApellidoTestigo1Arma.getText().toString(),
                        txtSegundoApellidoTestigo1Arma.getText().toString(), txtNombresTestigo1Arma.getText().toString(),
                        varRutaImagenT1, txtPrimerApellidoTestigo2Arma.getText().toString(), txtSegundoApellidoTestigo2Arma.getText().toString(),
                        txtNombresTestigo2Arma.getText().toString(),varRutaImagenT2, cargarIdPoliciaPrimerRespondiente);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo",modeloArmasFuego.getIdHechoDelictivo())
                .add("Aportacion",modeloArmasFuego.getAportacion())
                .add("Inspeccion",modeloArmasFuego.getInspeccion())
                .add("LugarEncontro",modeloArmasFuego.getLugarEncontro())
                .add("TipoArma",modeloArmasFuego.getTipoArma())
                .add("Calibre",modeloArmasFuego.getCalibre())
                .add("Color",modeloArmasFuego.getColor())
                .add("Matricula",modeloArmasFuego.getMatricula())
                .add("NumSerie",modeloArmasFuego.getNumSerie())
                .add("Observaciones",modeloArmasFuego.getObservaciones())
                .add("Destino",modeloArmasFuego.getDestino())
                .add("APEncontro",modeloArmasFuego.getAPEncontro())
                .add("AMEncontro",modeloArmasFuego.getAMEncontro())
                .add("NombreEncontro",modeloArmasFuego.getNombreEncontro())
                .add("RutaFirmaEncontro",modeloArmasFuego.getRutaFirmaEncontro())
                .add("APTestigoUno",modeloArmasFuego.getAPTestigoUno())
                .add("AMTestigoUno",modeloArmasFuego.getAMTestigoUno())
                .add("NombreTestigoUno",modeloArmasFuego.getNombreTestigoUno())
                .add("RutaFirmaTestigoUno",modeloArmasFuego.getRutaFirmaTestigoUno())
                .add("APTestigoDos",modeloArmasFuego.getAPTestigoDos())
                .add("AMTestigoDos",modeloArmasFuego.getAMTestigoDos())
                .add("NombreTestigoDos",modeloArmasFuego.getNombreTestigoDos())
                .add("RutaFirmaTestigoDos",modeloArmasFuego.getRutaFirmaTestigoDos())
                .add("IdPoliciaPrimerRespondiente",modeloArmasFuego.getIdPoliciaPrimerRespondiente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDArmasFuego/")
                .post(body)
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
                if(response.code() == 500){
                    Toast.makeText(getContext(), "EXISTIÓ UN ERROR EN SU CONEXIÓN A INTERNET, INTÉNTELO NUEVAMENTE", Toast.LENGTH_SHORT).show();
                }else if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    InventarioArmasObjetos.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                cargarArmas();
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

    private void insertObjetos() {
        if(txtNombresPropietarioObjetos.getText().toString().equals("NA")){
            varRutaFirmaObjeto = "NA";
        }else{
            varRutaFirmaObjeto = "http://189.254.7.167/WebServiceIPH/FirmaObjetos/"+"propietario_objeto_"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
        }
        if(txtNombresTestigo1Objeto.getText().toString().equals("NA")){
            varRutaImagenT1Objeto = "NA";
        }else{
            varRutaImagenT1Objeto = "http://189.254.7.167/WebServiceIPH/FirmaObjetos/"+"testigo1_objeto_"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
        }
        if(txtNombresTestigo2Objeto.getText().toString().equals("NA")){
            varRutaImagenT2Objeto = "NA";

        }else{
            varRutaImagenT2Objeto = "http://189.254.7.167/WebServiceIPH/FirmaObjetos/"+"testigo2_objeto_"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
        }

        ModeloObjetos_Delictivo modeloObjetos = new ModeloObjetos_Delictivo
                (cargarIdHechoDelictivo, varTipoObjeto, txtOtroObjeto.getText().toString(), varAportacionObjeto, varInspeccionObjeto, txtLugarEncontroObjetos.getText().toString(),
                        txtObservacionesObjetos.getText().toString(), txtDestinoObjetos.getText().toString(),
                        txtPrimerApellidoPropietarioObjetos.getText().toString(), txtSegundoApellidoPropietarioObjetos.getText().toString(), txtNombresPropietarioObjetos.getText().toString(),
                        varRutaFirmaObjeto, txtPrimerApellidoTestigo1Objeto.getText().toString(), txtSegundoApellidoTestigo1Objeto.getText().toString(), txtNombresTestigo1Objeto.getText().toString(),
                        varRutaImagenT1Objeto, txtPrimerApellidoTestigo2Objeto.getText().toString(), txtSegundoApellidoTestigo2Objeto.getText().toString(), txtNombresTestigo2Objeto.getText().toString(),
                        varRutaImagenT2Objeto, cargarIdPoliciaPrimerRespondiente);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo",modeloObjetos.getIdHechoDelictivo())
                .add("Objeto",modeloObjetos.getObjeto())
                .add("ObjetoOtro",modeloObjetos.getObjetoOtro())
                .add("Aportacion",modeloObjetos.getAportacion())
                .add("Inspeccion",modeloObjetos.getInspeccion())
                .add("LugarEncontro",modeloObjetos.getLugarEncontro())
                .add("DescObjeto",modeloObjetos.getDescObjeto())
                .add("Destino",modeloObjetos.getDestino())
                .add("APEncontro",modeloObjetos.getAPEncontro())
                .add("AMEncontro",modeloObjetos.getAMEncontro())
                .add("NombreEncontro",modeloObjetos.getNombreEncontro())
                .add("RutaFirmaEncontro",modeloObjetos.getRutaFirmaEncontro())
                .add("APTestigoUno",modeloObjetos.getAPTestigoUno())
                .add("AMTestigoUno",modeloObjetos.getAMTestigoUno())
                .add("NombreTestigoUno",modeloObjetos.getNombreTestigoUno())
                .add("RutaFirmaTestigoUno",modeloObjetos.getRutaFirmaTestigoUno())
                .add("APTestigoDos",modeloObjetos.getAPTestigoDos())
                .add("AMTestigoDos",modeloObjetos.getAMTestigoDos())
                .add("NombreTestigoDos",modeloObjetos.getNombreTestigoDos())
                .add("RutaFirmaTestigoDos",modeloObjetos.getRutaFirmaTestigoDos())
                .add("IdPoliciaPrimerRespondiente",modeloObjetos.getIdPoliciaPrimerRespondiente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDObjetos/")
                .post(body)
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
                if(response.code() == 500){
                    Toast.makeText(getContext(), "EXISTIÓ UN ERROR EN SU CONEXIÓN A INTERNET, INTÉNTELO NUEVAMENTE", Toast.LENGTH_SHORT).show();
                }else if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    InventarioArmasObjetos.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                cargarObjetos();
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

    public void insertImagenFirmaArmas() {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Description", cadenaPersona+cargarIdHechoDelictivo+randomUrlImagen+".jpg")
                .add("ImageData", cadenaImagenFirmaArmas)
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/MultimediaArmas")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, FAVOR DE VERIFICAR SU CONEXCIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 500){
                    Toast.makeText(getContext(), "EXISTIÓ UN ERROR EN SU CONEXIÓN A INTERNET, INTÉNTELO NUEVAMENTE", Toast.LENGTH_SHORT).show();
                }else if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    InventarioArmasObjetos.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "SU ARCHIVO MULTIMEDIA SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
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

    public void random(){
        Random random = new Random();
        numberRandom = random.nextInt(9000)*99;
        randomUrlImagen = numberRandom;
    }
    private void ListCombos() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllColores();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE COLORES");
            ArrayAdapter<String> adapterColores = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spColorArmaDelictivo.setAdapter(adapterColores);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON COLORES ACTIVOS", Toast.LENGTH_LONG).show();
        }
    }
    public void cargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdPoliciaPrimerRespondiente = share.getString("Usuario", "SIN INFORMACION");
        cargarIdHechoDelictivo = share.getString("IDHECHODELICTIVO", "SIN INFORMACION");
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarObjetos() {
        Log.i("OBJETOS", "INICIA CARGAR OBJETOS");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDObjetos?folioInternoObjetos="+cargarIdHechoDelictivo)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR OBJETOS, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
                Log.i("OBJETOS", "onFailure");

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
                                ListaIdObjetos = new ArrayList<String>();
                                ListaDatosObjetos = new ArrayList<String>();
                                Log.i("OBJETOS", "RESP:"+resp);

                                //***************** RESPUESTA DEL WEBSERVICE **************************//

                                //CONVERTIR ARREGLO DE JSON A OBJET JSON
                                String ArregloJson = resp.replace("[", "");
                                ArregloJson = ArregloJson.replace("]", "");

                                if(ArregloJson.equals(""))
                                {
                                    //Sin Información. Todos los campos vacíos.
                                    Log.i("OBJETOS", "SIN INFORMACIÓN");

                                }
                                else{
                                    Log.i("OBJETOS", "CON INFORMACIÓN:");

                                    //SEPARAR CADA detenido EN UN ARREGLO
                                    String[] ArrayIPHDelictivo = ArregloJson.split(Pattern.quote("},"));
                                    ArrayListaIPHDelictivo = ArrayIPHDelictivo;

                                    Log.i("OBJETOS", "ArrayListaDelictivo:"+ArrayIPHDelictivo[0]);

                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordeDetenido=0;
                                    while(contadordeDetenido < ArrayListaIPHDelictivo.length){
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayListaIPHDelictivo[contadordeDetenido] + "}");

                                            ListaIdObjetos.add(jsonjObject.getString("IdObjeto"));
                                            ListaDatosObjetos.add(
                                                    " OBJETO: "+
                                                            ((jsonjObject.getString("Objeto")).equals("null")?" - ":jsonjObject.getString("Objeto")) +
                                                            " DESCRIPCIÓN: "+
                                                            " "+((jsonjObject.getString("DescObjeto")).equals("null")?" - ":jsonjObject.getString("DescObjeto")) +
                                                            " DESTINO:  "+
                                                            " "+ ((jsonjObject.getString("Destino")).equals("null")?" - ":jsonjObject.getString("Destino"))
                                            );

                                        } catch (JSONException e) {
                                            Log.i("OBJETOS", "catch:" + e.toString());

                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordeDetenido++;
                                    }
                                }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                InventarioArmasObjetos.MyAdapter adapter = new InventarioArmasObjetos.MyAdapter(getContext(), ListaDatosObjetos,ListaDatosObjetos,"OBJETO");
                                lvObjeto.setAdapter(adapter);
                                funciones.ajustaAlturaListView(lvObjeto,290);


                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL CONSULTAR ANEXO C, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarArmas() {
        Log.i("ARMAS", "INICIA CARGAR OBJETOS");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDArmasFuego?folioInternoArmas="+cargarIdHechoDelictivo)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR OBJETOS, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
                Log.i("ARMAS", "onFailure");

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
                                ListaIdArmas = new ArrayList<String>();
                                ListaDatosArmas = new ArrayList<String>();
                                Log.i("ARMAS", "RESP:"+resp);

                                //***************** RESPUESTA DEL WEBSERVICE **************************//

                                //CONVERTIR ARREGLO DE JSON A OBJET JSON
                                String ArregloJson = resp.replace("[", "");
                                ArregloJson = ArregloJson.replace("]", "");

                                if(ArregloJson.equals(""))
                                {
                                    //Sin Información. Todos los campos vacíos.
                                    Log.i("ARMAS", "SIN INFORMACIÓN");

                                }
                                else{
                                    Log.i("ARMAS", "CON INFORMACIÓN:");

                                    //SEPARAR CADA detenido EN UN ARREGLO
                                    String[] ArrayIPHDelictivo = ArregloJson.split(Pattern.quote("},"));
                                    ArrayListaIPHDelictivoArmas = ArrayIPHDelictivo;

                                    Log.i("ARMAS", "ArrayListaDelictivo:"+ArrayIPHDelictivo[0]);

                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordeDetenido=0;
                                    while(contadordeDetenido < ArrayListaIPHDelictivoArmas.length){
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayListaIPHDelictivoArmas[contadordeDetenido] + "}");

                                            ListaIdArmas.add(jsonjObject.getString("IdArma"));
                                            ListaDatosArmas.add(
                                                    " TIPO ARMA: "+
                                                            ((jsonjObject.getString("TipoArma")).equals("null")?" - ":jsonjObject.getString("TipoArma")) +
                                                            " MATRICULA: "+
                                                            " "+((jsonjObject.getString("Matricula")).equals("null")?" - ":jsonjObject.getString("Matricula")) +
                                                            " DESTINO:  "+
                                                            " "+ ((jsonjObject.getString("Destino")).equals("null")?" - ":jsonjObject.getString("Destino"))
                                            );

                                        } catch (JSONException e) {
                                            Log.i("ARMAS", "catch:" + e.toString());

                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON ARMAS", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordeDetenido++;
                                    }
                                }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                InventarioArmasObjetos.MyAdapter adapter2 = new InventarioArmasObjetos.MyAdapter(getContext(), ListaDatosArmas,ListaDatosArmas,"ARMA");
                                lvArmas.setAdapter(adapter2);
                                funciones.ajustaAlturaListView(lvArmas,290);


                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL CONSULTAR ANEXO C, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    //***************** ADAPTADOR PARA LLENAR LISTA DE IPH ADMINISTRATIVO **************************//
    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> ListaIdVehiculo,ListaDatosVehiculo;
        String titulo;
        MyAdapter (Context c, ArrayList<String> ListaIdVehiculo, ArrayList<String> ListaDatosVehiculo, String titulo) {
            super(c, R.layout.row_armas_objetos, ListaIdVehiculo);
            this.context = c;
            this.ListaIdVehiculo = ListaIdVehiculo;
            this.ListaDatosVehiculo = ListaDatosVehiculo;
            this.titulo = titulo;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Log.i("OBJETOS", "ADAPTER");

            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_armas_objetos, parent, false);

            TextView lblNumeroVehiculo = row.findViewById(R.id.lblNumeroVehiculo);
            TextView lblDatosVehiculo = row.findViewById(R.id.lblDatosVehiculo);

            // Asigna los valores
            lblNumeroVehiculo.setText(titulo+":");
            lblDatosVehiculo.setText(ListaDatosVehiculo.get(position));

            return row;
        }
    }

    //***************** eliminar armas**************************//
    private void EliminarArmas(String IdArma) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDArmasFuego?folioInternoArma="+cargarIdHechoDelictivo+"&idArma="+IdArma)
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ELIMINAR ARMA, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                if(resp.equals("true"))
                                {
                                    //***************** MENSAJE MÁS ACTUALIZAR LISTA (Recargando el Fragmento xoxo) **************************//
                                    Toast.makeText(getContext(), "SE ELIMINÓ CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                    cargarArmas();
                                }
                                else{
                                    Toast.makeText(getContext(), "PROBLEMA AL ELIMINAR", Toast.LENGTH_SHORT).show();
                                }
                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL ELIMINAR ARMAS, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    //***************** Eliminar objetps**************************//
    private void EliminarObjetos(String IdObjeto) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDObjetos?folioInternoObjeto="+cargarIdHechoDelictivo+"&idObjeto="+IdObjeto)
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ELIMINAR OBJETO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                if(resp.equals("true"))
                                {
                                    //***************** MENSAJE MÁS ACTUALIZAR LISTA (Recargando el Fragmento xoxo) **************************//
                                    Toast.makeText(getContext(), "SE ELIMINÓ CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                    cargarObjetos();
                                }
                                else{
                                    Toast.makeText(getContext(), "PROBLEMA AL ELIMINAR", Toast.LENGTH_SHORT).show();
                                }
                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL ELIMINAR OBJETOS, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


}