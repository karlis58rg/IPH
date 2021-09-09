package mx.ssp.iph.delictivo.ui.fragmets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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
import mx.ssp.iph.delictivo.model.ModeloEntrevistas;
import mx.ssp.iph.delictivo.viewModel.EntrevistasDelictivoViewModel;
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

public class EntrevistasDelictivo extends Fragment {

    private EntrevistasDelictivoViewModel mViewModel;
    private Funciones funciones;
    private EditText txtFechaEntrevista,txtHoraEntrevista,txtFechaNacimientoEntrevistado,txtEntrevista;
    ImageView imgMicrofonoEntrevista,imgFirmaEntrevistado,imgFirmaDerechosVictimaDelictivo,btnGuardarEntrevista;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    Spinner spNacionalidadEntrevistado,spGeneroEntrevistado,spTipoDocumentoEntrevistado,spMunicipioEntrevistado;
    RadioGroup rgReservarDatos, rgCalidadEntrevistado, rgTrasladoPersonaEntrevistada, rgLugarTrasladoEntrevista, rgInformeDerechoVictimaDelictivo;
    RadioButton rbNoReservarDatos, rbSiReservarDatos, rbCalidadVictima, rbCalidadDenunciante, rbCalidadTestigo, rbNoTrasladoPersonaEntrevistada,
            rbSiTrasladoPersonaEntrevistada, rbLugarTrasladoEntrevistadoFiscaliaAgencia, rbLugarTrasladoEntrevistadoHospital,
            rbLugarTrasladoEntrevistadoOtraDependencia, rbNoInformeDerechoVictimaDelictivo, rbSiInformeDerechoVictimaDelictivo;
    EditText txtPrimerApellidoEntrevistado,txtSegundoApellidoEntrevistado,txtNombresEntrevistado,txtEdadEntrevistado,txtNumeroIdentificacionEntrevistado,
            txtTelefonoEntrevistado,txtCorreoEntrevistado,txtEntidadEntrevistado,txtColoniaEntrevistado,txtCalleEntrevistado,txtNumeroExteriorEntrevistado,
            txtNumeroInteriorEntrevistado,txtCodigoPostalEntrevistado,txtReferenciasdelLugarEntrevistado,txtCualLugarTrasladoEntrevista;
    TextView lblFirmadelEntrevistadoOculto,lblFirmaEntrevistaOculto;
    SharedPreferences share;
    String cargarIdPoliciaPrimerRespondiente,cargarIdHechoDelictivo,varReservarDatos,varCalidadEntrevistado,
            descNacionalidadEntrevista,descGeneroEntrevista,descTipoDocumentoEntrevista,descMunicipioEntrevista,varRutaFirmaEntrevistado,
            varRutaFirmaDerechosEntrevistado,varTrasladoCanalizacion,varLugarTraslado,cadenaPersona,cadenaImagenFirmaEntrevistas;
    LinearLayout quintoTresLinear, cuartoLinear, LinearEntrevistado, LinearCalidadEntrevistado, lyTelEntrevistado,
            lyCorreoEntrevistado, LinearEntrevista, lyFirma, lyRGTrasladoTes, Linear13, lyLecturaDerechos, lyFirmaDerechosVictima, Linear4, Linear8;
    int numberRandom,randomUrlImagen;

    private ListView lvEntrevistas;
    private ArrayList<String> ListaIdEntrevistas,ListaDatosEntrevistas;
    private String[] ArrayListaIPHDelictivoEntrevistas;

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
        cargarDatos();
        random();
        imgMicrofonoEntrevista = view.findViewById(R.id.imgMicrofonoEntrevista);
        imgFirmaEntrevistado = view.findViewById(R.id.imgFirmaEntrevistado);
        imgFirmaDerechosVictimaDelictivo = view.findViewById(R.id.imgFirmaDerechosVictimaDelictivo);
        spNacionalidadEntrevistado = view.findViewById(R.id.spNacionalidadEntrevistado);
        spGeneroEntrevistado = view.findViewById(R.id.spGeneroEntrevistado);
        spTipoDocumentoEntrevistado = view.findViewById(R.id.spTipoDocumentoEntrevistado);
        spMunicipioEntrevistado = view.findViewById(R.id.spMunicipioEntrevistado);

        rgReservarDatos = view.findViewById(R.id.rgReservarDatos);
        rgCalidadEntrevistado = view.findViewById(R.id.rgCalidadEntrevistado);
        rgTrasladoPersonaEntrevistada = view.findViewById(R.id.rgTrasladoPersonaEntrevistada);
        rgLugarTrasladoEntrevista = view.findViewById(R.id.rgLugarTrasladoEntrevista);
        rgInformeDerechoVictimaDelictivo = view.findViewById(R.id.rgInformeDerechoVictimaDelictivo);

        rbNoReservarDatos = view.findViewById(R.id.rbNoReservarDatos);
        rbSiReservarDatos = view.findViewById(R.id.rbSiReservarDatos);
        rbCalidadVictima = view.findViewById(R.id.rbCalidadVictima);
        rbCalidadDenunciante = view.findViewById(R.id.rbCalidadDenunciante);
        rbCalidadTestigo = view.findViewById(R.id.rbCalidadTestigo);
        rbNoTrasladoPersonaEntrevistada = view.findViewById(R.id.rbNoTrasladoPersonaEntrevistada);
        rbSiTrasladoPersonaEntrevistada = view.findViewById(R.id.rbSiTrasladoPersonaEntrevistada);
        rbLugarTrasladoEntrevistadoFiscaliaAgencia = view.findViewById(R.id.rbLugarTrasladoEntrevistadoFiscaliaAgencia);
        rbLugarTrasladoEntrevistadoHospital = view.findViewById(R.id.rbLugarTrasladoEntrevistadoHospital);
        rbLugarTrasladoEntrevistadoOtraDependencia = view.findViewById(R.id.rbLugarTrasladoEntrevistadoOtraDependencia);
        rbNoInformeDerechoVictimaDelictivo = view.findViewById(R.id.rbNoInformeDerechoVictimaDelictivo);
        rbSiInformeDerechoVictimaDelictivo = view.findViewById(R.id.rbSiInformeDerechoVictimaDelictivo);

        txtPrimerApellidoEntrevistado = view.findViewById(R.id.txtPrimerApellidoEntrevistado);
        txtSegundoApellidoEntrevistado = view.findViewById(R.id.txtSegundoApellidoEntrevistado);
        txtNombresEntrevistado = view.findViewById(R.id.txtNombresEntrevistado);
        txtEdadEntrevistado = view.findViewById(R.id.txtEdadEntrevistado);
        txtNumeroIdentificacionEntrevistado = view.findViewById(R.id.txtNumeroIdentificacionEntrevistado);
        txtTelefonoEntrevistado = view.findViewById(R.id.txtTelefonoEntrevistado);
        txtCorreoEntrevistado = view.findViewById(R.id.txtCorreoEntrevistado);
        txtEntidadEntrevistado = view.findViewById(R.id.txtEntidadEntrevistado);
        txtColoniaEntrevistado = view.findViewById(R.id.txtColoniaEntrevistado);
        txtCalleEntrevistado = view.findViewById(R.id.txtCalleEntrevistado);
        txtNumeroExteriorEntrevistado = view.findViewById(R.id.txtNumeroExteriorEntrevistado);
        txtNumeroInteriorEntrevistado = view.findViewById(R.id.txtNumeroInteriorEntrevistado);
        txtCodigoPostalEntrevistado = view.findViewById(R.id.txtCodigoPostalEntrevistado);
        txtReferenciasdelLugarEntrevistado = view.findViewById(R.id.txtReferenciasdelLugarEntrevistado);
        txtCualLugarTrasladoEntrevista = view.findViewById(R.id.txtCualLugarTrasladoEntrevista);
        lblFirmadelEntrevistadoOculto = view.findViewById(R.id.lblFirmadelEntrevistadoOculto);
        lblFirmaEntrevistaOculto = view.findViewById(R.id.lblFirmaEntrevistaOculto);
        btnGuardarEntrevista = view.findViewById(R.id.btnGuardarEntrevista);
        //EditText
        txtEntrevista = view.findViewById(R.id.txtEntrevista);
        txtFechaEntrevista = view.findViewById(R.id.txtFechaEntrevista);
        txtHoraEntrevista = view.findViewById(R.id.txtHoraEntrevista);
        txtFechaNacimientoEntrevistado = view.findViewById(R.id.txtFechaNacimientoEntrevistado);

        quintoTresLinear = view.findViewById(R.id.quintoTresLinear);
        cuartoLinear = view.findViewById(R.id.cuartoLinear);
        LinearEntrevistado = view.findViewById(R.id.LinearEntrevistado);
        LinearCalidadEntrevistado = view.findViewById(R.id.LinearCalidadEntrevistado);
        lyTelEntrevistado = view.findViewById(R.id.lyTelEntrevistado);
        lyCorreoEntrevistado = view.findViewById(R.id.lyCorreoEntrevistado);
        LinearEntrevista = view.findViewById(R.id.LinearEntrevista);
        lyFirma = view.findViewById(R.id.lyFirma);
        lyRGTrasladoTes = view.findViewById(R.id.lyRGTrasladoTes);
        Linear13 = view.findViewById(R.id.Linear13);
        lyLecturaDerechos = view.findViewById(R.id.lyLecturaDerechos);
        lyFirmaDerechosVictima = view.findViewById(R.id.lyFirmaDerechosVictima);
        Linear4 = view.findViewById(R.id.Linear4);
        Linear8 = view.findViewById(R.id.Linear8);

        lvEntrevistas= view.findViewById(R.id.lvEntrevistas);
        ListCombos();

        rgLugarTrasladoEntrevista.setEnabled(false);
        rbLugarTrasladoEntrevistadoFiscaliaAgencia.setEnabled(false);
        rbLugarTrasladoEntrevistadoHospital.setEnabled(false);
        rbLugarTrasladoEntrevistadoOtraDependencia.setEnabled(false);

        //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
        if (funciones.ping(getContext())){
            //Toast.makeText(getContext(), "cargarNoReferenciaAdministrativa()", Toast.LENGTH_LONG).show();
            cargarEntrevistas();
        }


        //Clic a la lista
        lvEntrevistas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext()).
                                setMessage("¿DESEA ELIMINAR LA ENTREVISTA "+ ListaIdEntrevistas.get(position) + "?" ).
                                setPositiveButton( "ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //CONSUME WEB SERVICE PARA ELIMINAR DB
                                        EliminarEntrevista(ListaIdEntrevistas.get(position));
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

        rgReservarDatos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiReservarDatos) {
                    varReservarDatos = "SI";
                } else if (checkedId == R.id.rbNoReservarDatos) {
                    varReservarDatos = "NO";
                }

            }
        });

        rgCalidadEntrevistado.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbCalidadVictima) {
                    varCalidadEntrevistado = "VICTIMA-OFENDIDO";
                } else if (checkedId == R.id.rbCalidadDenunciante) {
                    varCalidadEntrevistado = "DENUNCIANTE";
                }else if (checkedId == R.id.rbCalidadTestigo){
                    varCalidadEntrevistado = "TESTIGO";
                }
            }
        });

        rgTrasladoPersonaEntrevistada.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiTrasladoPersonaEntrevistada) {
                    varTrasladoCanalizacion = "SI";
                    rgLugarTrasladoEntrevista.setEnabled(true);

                    rbLugarTrasladoEntrevistadoFiscaliaAgencia.setEnabled(true);
                    rbLugarTrasladoEntrevistadoHospital.setEnabled(true);
                    rbLugarTrasladoEntrevistadoOtraDependencia.setEnabled(true);

                } else if (checkedId == R.id.rbNoTrasladoPersonaEntrevistada) {
                    varTrasladoCanalizacion = "NO";
                    rgLugarTrasladoEntrevista.setEnabled(false);

                    rbLugarTrasladoEntrevistadoFiscaliaAgencia.setChecked(false);
                    rbLugarTrasladoEntrevistadoHospital.setChecked(false);
                    rbLugarTrasladoEntrevistadoOtraDependencia.setChecked(false);

                    rbLugarTrasladoEntrevistadoFiscaliaAgencia.setEnabled(false);
                    rbLugarTrasladoEntrevistadoHospital.setEnabled(false);
                    rbLugarTrasladoEntrevistadoOtraDependencia.setEnabled(false);

                    txtCualLugarTrasladoEntrevista.setEnabled(false);
                    txtCualLugarTrasladoEntrevista.setText("");
                }

            }
        });

        rgLugarTrasladoEntrevista.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbLugarTrasladoEntrevistadoFiscaliaAgencia) {
                    varLugarTraslado = "FISCALIA/AGENCIA";
                    txtCualLugarTrasladoEntrevista.setText("");
                    txtCualLugarTrasladoEntrevista.setEnabled(false);
                } else if (checkedId == R.id.rbLugarTrasladoEntrevistadoHospital) {
                    varLugarTraslado = "HOSPITAL";
                    txtCualLugarTrasladoEntrevista.setText("");
                    txtCualLugarTrasladoEntrevista.setEnabled(false);
                }else if (checkedId == R.id.rbLugarTrasladoEntrevistadoOtraDependencia){
                    varLugarTraslado = "OTRA DEPENDENCIA";
                    txtCualLugarTrasladoEntrevista.setEnabled(true);
                }
            }
        });

        rgInformeDerechoVictimaDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiInformeDerechoVictimaDelictivo) {
                    cadenaPersona = "firma_derechos_";
                    varRutaFirmaDerechosEntrevistado = "http://189.254.7.167/WebServiceIPH/FirmaEntrevista/"+cadenaPersona+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
                } else if (checkedId == R.id.rbNoInformeDerechoVictimaDelictivo) {
                    varRutaFirmaDerechosEntrevistado = "NO";
                }
            }
        });

        imgFirmaEntrevistado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContenedorFirmaDelictivo dialog = new ContenedorFirmaDelictivo(R.id.lblFirmadelEntrevistado,R.id.lblFirmadelEntrevistadoOculto,R.id.imgFirmadelEntrevistadoMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });

        imgFirmaDerechosVictimaDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContenedorFirmaDelictivo dialog = new ContenedorFirmaDelictivo(R.id.lblFirmaVictimaEntrevistaDelictivo,R.id.lblFirmaEntrevistaOculto,R.id.imgFirmaEntrevistaDelictivoMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });

        btnGuardarEntrevista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


/*                if(varRutaFirmaDerechosEntrevistado.equals("NO")){
                }else{
                    cadenaImagenFirmaEntrevistas = lblFirmaEntrevistaOculto.getText().toString();
                    insertImagenFirmaEntrevistas();
                }

                if(lblFirmadelEntrevistadoOculto.getText().toString().isEmpty()){
                    cadenaPersona = "firma_entrevistado_";
                    varRutaFirmaEntrevistado = "NP";
                }else{
                    cadenaPersona = "firma_entrevistado_";
                    varRutaFirmaEntrevistado = "http://189.254.7.167/WebServiceIPH/FirmaEntrevista/"+cadenaPersona+cargarIdHechoDelictivo+randomUrlImagen+".jpg";
                    cadenaImagenFirmaEntrevistas = lblFirmadelEntrevistadoOculto.getText().toString();
                    insertImagenFirmaEntrevistas();
                }*/

                //insertEntrevistas();
                PrimeraValidacion();
            }
        });


        /****************************************************************************************/
        return view;
    }


    public void PrimeraValidacion(){
            if(rbNoReservarDatos.isChecked() || rbSiReservarDatos.isChecked()){
                if (txtFechaEntrevista.getText().toString().length() >= 3 && txtHoraEntrevista.getText().toString().length() >= 3){
                    if(txtPrimerApellidoEntrevistado.getText().toString().length() >= 3){
                        if(txtNombresEntrevistado.getText().toString().length() >= 3){
                            SegundaValidacion();

                        }

                        else{
                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA NOMBRE DEL ENTREVISTADO", Toast.LENGTH_SHORT).show();
                            txtNombresEntrevistado.requestFocus();
                            LinearEntrevistado.requestFocus();
                            txtNombresEntrevistado.requestFocus();
                        }

                    }

                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA PRIMER APELLIDO DEL ENTREVISTADO", Toast.LENGTH_SHORT).show();
                        txtPrimerApellidoEntrevistado.requestFocus();
                        LinearEntrevistado.requestFocus();
                        txtPrimerApellidoEntrevistado.requestFocus();
                    }

                }

                else{
                    Toast.makeText(getActivity().getApplicationContext(), "ESPECIFIQUE FECHA Y HORA DE LA ENTREVISTA", Toast.LENGTH_SHORT).show();
                    cuartoLinear.requestFocus();
                }

            }

            else{
                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFIQUE SI DESEA RESERVAR DATOS", Toast.LENGTH_SHORT).show();
                quintoTresLinear.requestFocus();
            }
        }

    public void SegundaValidacion(){
        if(rbCalidadVictima.isChecked() || rbCalidadDenunciante.isChecked() || rbCalidadTestigo.isChecked()){
            if(txtFechaNacimientoEntrevistado.getText().toString().length() >= 3){
                if(txtEdadEntrevistado.getText().toString().length() >= 3){
                    if(txtTelefonoEntrevistado.getText().toString().length() == 10 || txtCorreoEntrevistado.getText().toString().length() >= 8){
                        if(txtColoniaEntrevistado.getText().toString().length() >= 3){
                            if(txtCalleEntrevistado.getText().toString().length() >= 3){
                                if(txtReferenciasdelLugarEntrevistado.getText().toString().length() >= 3){
                                    if(txtEntrevista.getText().toString().length() >= 3){
                                        if(lblFirmadelEntrevistadoOculto.getText().toString().isEmpty()){
                                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA FIRMA DEL ENTREVISTADO", Toast.LENGTH_SHORT).show();
                                            lyFirma.requestFocus();
                                        }
                                        else{
                                            TerceraValidacion();
                                        }

                                    }

                                    else{
                                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA ENTREVISTA REALIZADA", Toast.LENGTH_SHORT).show();
                                        LinearEntrevista.requestFocus();
                                    }

                                }

                                else{
                                    Toast.makeText(getActivity().getApplicationContext(), "INGRESA UNA REFERENCIA DEL DOMICILIO DEL ENTREVISTADO", Toast.LENGTH_SHORT).show();
                                    txtReferenciasdelLugarEntrevistado.requestFocus();
                                }

                            }

                            else{
                                Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA CALLE DEL DOMICILIO DEL ENTREVISTADO", Toast.LENGTH_SHORT).show();
                                txtCalleEntrevistado.requestFocus();
                            }

                        }

                        else{
                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA COLONIA DEL DOMICILIO DEL ENTREVISTADO", Toast.LENGTH_SHORT).show();
                            txtColoniaEntrevistado.requestFocus();
                        }

                    }

                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA AL MENOS UN CONTACTO DEL ENTREVISTADO", Toast.LENGTH_SHORT).show();
                        Linear8.requestFocus();
                    }


                }

                else{
                    Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA EDAD DEL ENTREVISTADO", Toast.LENGTH_SHORT).show();
                    txtEdadEntrevistado.requestFocus();
                }


            }

            else{
                Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA FECHA DE NACIMIENTO DEL ENTREVISTADO", Toast.LENGTH_SHORT).show();
                Linear4.requestFocus();
            }


        }

        else{
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFIQUE EN QUE CALIDAD ESTÁ EL ENTREVISTADO", Toast.LENGTH_SHORT).show();
            LinearCalidadEntrevistado.requestFocus();
        }
    }

    public void TerceraValidacion(){
        if(rbSiTrasladoPersonaEntrevistada.isChecked()){
            if(rbLugarTrasladoEntrevistadoFiscaliaAgencia.isChecked() || rbLugarTrasladoEntrevistadoHospital.isChecked()){
                CuartaValidacion();
            }

            else if(rbLugarTrasladoEntrevistadoOtraDependencia.isChecked()){
                if(txtCualLugarTrasladoEntrevista.getText().toString().length() >= 3){
                    CuartaValidacion();
                }

                else{
                    Toast.makeText(getActivity().getApplicationContext(), "INGRESA A QUÉ OTRO LUGAR SE TRASLADÓ A LA PERSONA ENTREVISTADA", Toast.LENGTH_SHORT).show();
                    txtCualLugarTrasladoEntrevista.requestFocus();
                    Linear13.requestFocus();
                    txtCualLugarTrasladoEntrevista.requestFocus();
                }

            }

            else{
                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA A QUÉ LUGAR SE TRASLADÓ A LA PERSONA ENTREVISTADA", Toast.LENGTH_SHORT).show();
                Linear13.requestFocus();
            }

        }

        else if(rbNoTrasladoPersonaEntrevistada.isChecked()){
            CuartaValidacion();
        }

        else{
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI SE TRASLADÓ O CANALIZÓ A LA PERSONA ENTREVISTADA", Toast.LENGTH_SHORT).show();
            lyRGTrasladoTes.requestFocus();
        }
    }

    public void CuartaValidacion(){
        if(rbNoInformeDerechoVictimaDelictivo.isChecked() || rbSiInformeDerechoVictimaDelictivo.isChecked()){
            if(lblFirmaEntrevistaOculto.getText().toString().isEmpty()){
                Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA FIRMA DE LA VICTIMA U OFENDIDO", Toast.LENGTH_SHORT).show();
                lyFirmaDerechosVictima.requestFocus();

            }

            else{
                //INSERTAR ENTREVISTA
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                //insertEntrevistas();
            }


        }

        else{
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI SE LE INFORMÓ SUS DERECHOS A LA PERSONA ENTREVISTADA", Toast.LENGTH_SHORT).show();
            lyLecturaDerechos.requestFocus();
        }
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

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertEntrevistas() {
        DataHelper dataHelper = new DataHelper(getContext());

        descGeneroEntrevista = (String) spGeneroEntrevistado.getSelectedItem();
        int idDescGeneroEntrevista = dataHelper.getIdSexo(descGeneroEntrevista);
        String idGeneroEntrevista = String.valueOf(idDescGeneroEntrevista);

        descNacionalidadEntrevista = (String) spNacionalidadEntrevistado.getSelectedItem();
        int idDescNacionalidadEntrevista = dataHelper.getIdNacionalidad(descNacionalidadEntrevista);
        String idNacionalidadEntrevista = String.valueOf(idDescNacionalidadEntrevista);

        descTipoDocumentoEntrevista = (String) spTipoDocumentoEntrevistado.getSelectedItem();
        int idDescTipoDocumentoEntrevista = dataHelper.getIdIdentificacion(descTipoDocumentoEntrevista);
        String idTipoDocumentoEntrevista = String.valueOf(idDescTipoDocumentoEntrevista);

        descMunicipioEntrevista = (String) spMunicipioEntrevistado.getSelectedItem();
        String idDescMunicipioPersonaDetenidaHD = dataHelper.getIdMunicipio(descMunicipioEntrevista);
        String idMunicipioEntrevista = String.valueOf(idDescMunicipioPersonaDetenidaHD);

        ModeloEntrevistas modeloEntrevistas = new ModeloEntrevistas
                (cargarIdHechoDelictivo, varReservarDatos, txtFechaEntrevista.getText().toString(), txtHoraEntrevista.getText().toString(),
                        txtPrimerApellidoEntrevistado.getText().toString(), txtSegundoApellidoEntrevistado.getText().toString(), txtNombresEntrevistado.getText().toString(),
                        varCalidadEntrevistado, idNacionalidadEntrevista, idGeneroEntrevista, txtFechaNacimientoEntrevistado.getText().toString(),
                        txtEdadEntrevistado.getText().toString(), idTipoDocumentoEntrevista, "NA", txtNumeroIdentificacionEntrevistado.getText().toString(),
                        txtTelefonoEntrevistado.getText().toString(), txtCorreoEntrevistado.getText().toString(), "12", idMunicipioEntrevista,
                        txtColoniaEntrevistado.getText().toString(), txtCalleEntrevistado.getText().toString(), txtNumeroExteriorEntrevistado.getText().toString(), txtNumeroInteriorEntrevistado.getText().toString(),
                        txtCodigoPostalEntrevistado.getText().toString(), txtReferenciasdelLugarEntrevistado.getText().toString(), txtEntrevista.getText().toString(), varRutaFirmaEntrevistado,
                        varTrasladoCanalizacion, varLugarTraslado, txtCualLugarTrasladoEntrevista.getText().toString(),
                        varRutaFirmaDerechosEntrevistado, cargarIdPoliciaPrimerRespondiente);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo", modeloEntrevistas.getIdHechoDelictivo())
                .add("ReservarDatos", modeloEntrevistas.getReservarDatos())
                .add("Fecha", modeloEntrevistas.getFecha())
                .add("Hora", modeloEntrevistas.getHora())
                .add("APEntrevistado", modeloEntrevistas.getAPEntrevistado())
                .add("AMEntrevistado", modeloEntrevistas.getAMEntrevistado())
                .add("NombreEntrevistado", modeloEntrevistas.getNombreEntrevistado())
                .add("Calidad", modeloEntrevistas.getCalidad())
                .add("IdNacionalidad", modeloEntrevistas.getIdNacionalidad())
                .add("IdSexo", modeloEntrevistas.getIdSexo())
                .add("FechaNacimiento", modeloEntrevistas.getFechaNacimiento())
                .add("Edad", modeloEntrevistas.getEdad())
                .add("IdIdentificacion", modeloEntrevistas.getIdIdentificacion())
                .add("IdentificacionOtro", modeloEntrevistas.getIdentificacionOtro())
                .add("NumIdentificacion", modeloEntrevistas.getNumIdentificacion())
                .add("Telefono", modeloEntrevistas.getTelefono())
                .add("Correo", modeloEntrevistas.getCorreo())
                .add("IdEntidadFederativa", modeloEntrevistas.getIdEntidadFederativa())
                .add("IdMunicipio", modeloEntrevistas.getIdMunicipio())
                .add("ColoniaLocalidad", modeloEntrevistas.getColoniaLocalidad())
                .add("CalleTramo", modeloEntrevistas.getCalleTramo())
                .add("NoExterior", modeloEntrevistas.getNoExterior())
                .add("NoInterior", modeloEntrevistas.getNoInterior())
                .add("Cp", modeloEntrevistas.getCp())
                .add("Referencia", modeloEntrevistas.getReferencia())
                .add("RelatoEntrevista", modeloEntrevistas.getRelatoEntrevista())
                .add("RutaFirmaEntrevistado", modeloEntrevistas.getRutaFirmaEntrevistado())
                .add("TrasladoCanalizacion", modeloEntrevistas.getTrasladoCanalizacion())
                .add("LugarTraslado", modeloEntrevistas.getLugarTraslado())
                .add("DescLugarTraslado", modeloEntrevistas.getDescLugarTraslado())
                .add("RutaFirmaVictimaOfendidos", modeloEntrevistas.getRutaFirmaVictimaOfendidos())
                .add("IdPoliciaPrimerRespondiente", modeloEntrevistas.getIdPoliciaPrimerRespondiente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDEntrevistas/")
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
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    EntrevistasDelictivo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                cargarEntrevistas();

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

    public void insertImagenFirmaEntrevistas() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Description", cadenaPersona+cargarIdHechoDelictivo+randomUrlImagen+".jpg")
                .add("ImageData", cadenaImagenFirmaEntrevistas)
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/MultimediaEntrevistas")
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
                    EntrevistasDelictivo.this.getActivity().runOnUiThread(new Runnable() {
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

    private void ListCombos() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> listMuniEntrevista = dataHelper.getAllMunicipios();
        ArrayList<String> listNacion = dataHelper.getAllNacionalidad();
        ArrayList<String> listSexo = dataHelper.getAllSexo();
        ArrayList<String> listIdentificacion = dataHelper.getAllIdentificacion();

        if (listMuniEntrevista.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MUNICIPIOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listMuniEntrevista);
            spMunicipioEntrevistado.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON MUNICIPIOS ACTIVOS.", Toast.LENGTH_LONG).show();
        }
        if (listNacion.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE NACIONALIDAD");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listNacion);
            spNacionalidadEntrevistado.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON NACIONES ACTIVAS.", Toast.LENGTH_LONG).show();
        }
        if (listSexo.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE SEXO");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listSexo);
            spGeneroEntrevistado.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON SEXOS ACTIVOS.", Toast.LENGTH_LONG).show();
        }
        if (listIdentificacion.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE IDENTIFICACIONES");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listIdentificacion);
            spTipoDocumentoEntrevistado.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON IDENTIFICACIONES ACTIVAS.", Toast.LENGTH_LONG).show();
        }
    }

    public void cargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdPoliciaPrimerRespondiente = share.getString("Usuario", "SIN INFORMACION");
        cargarIdHechoDelictivo = share.getString("IDHECHODELICTIVO", "SIN INFORMACION");
    }

    public void random(){
        Random random = new Random();
        numberRandom = random.nextInt(9000)*99;
        randomUrlImagen = numberRandom;
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void cargarEntrevistas() {
        Log.i("ARMAS", "INICIA CARGAR OBJETOS");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDEntrevistas?folioInternoEntrevista="+cargarIdHechoDelictivo)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR ENTREVISTAS, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                ListaIdEntrevistas = new ArrayList<String>();
                                ListaDatosEntrevistas = new ArrayList<String>();
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
                                    ArrayListaIPHDelictivoEntrevistas = ArrayIPHDelictivo;

                                    Log.i("ARMAS", "ArrayListaDelictivo:"+ArrayIPHDelictivo[0]);

                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordeDetenido=0;
                                    while(contadordeDetenido < ArrayListaIPHDelictivoEntrevistas.length){
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayListaIPHDelictivoEntrevistas[contadordeDetenido] + "}");

                                            ListaIdEntrevistas.add(jsonjObject.getString("IdPersonaEntrvistada"));
                                            ListaDatosEntrevistas.add(
                                                    " NOMBRE: "+
                                                            ((jsonjObject.getString("NombreEntrevistado")).equals("null")?" - ":jsonjObject.getString("NombreEntrevistado") + jsonjObject.getString("APEntrevistado")) +
                                                            " CALIDAD: "+
                                                            " "+((jsonjObject.getString("Calidad")).equals("null")?" - ":jsonjObject.getString("Calidad")) +
                                                            " TELÉFONO:  "+
                                                            " "+ ((jsonjObject.getString("Telefono")).equals("null")?" - ":jsonjObject.getString("Telefono"))
                                            );

                                        } catch (JSONException e) {
                                            Log.i("ARMAS", "catch:" + e.toString());

                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON ENTREVISTAS", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordeDetenido++;
                                    }
                                }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                EntrevistasDelictivo.MyAdapter adapter2 = new EntrevistasDelictivo.MyAdapter(getContext(), ListaDatosEntrevistas,ListaDatosEntrevistas,"ENTREVISTA");
                                lvEntrevistas.setAdapter(adapter2);
                                funciones.ajustaAlturaListView(lvEntrevistas,180);


                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL CONSULTAR ENTREVISTAS, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
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
            Log.i("ENTREVISTAS", "ADAPTER");

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


    //***************** eliminar entrevista**************************//
    private void EliminarEntrevista(String IdEntrevista) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDEntrevistas?folioInternoEntrevista="+cargarIdHechoDelictivo+"&idEntrevista="+IdEntrevista)
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ELIMINAR ENTREVISTA, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                    cargarEntrevistas();
                                }
                                else{
                                    Toast.makeText(getContext(), "PROBLEMA AL ELIMINAR", Toast.LENGTH_SHORT).show();
                                }
                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL ELIMINAR ENTREVISTA, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

}