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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.ui.fragmets.Detenciones;
import mx.ssp.iph.delictivo.model.ModeloConocimientoHecho_Delictivo;
import mx.ssp.iph.delictivo.model.ModeloDetenciones_Delictivo;
import mx.ssp.iph.delictivo.model.ModeloLugarDetenciones_Delictivo;
import mx.ssp.iph.delictivo.viewModel.DetencionesDelictivoViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
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

public class Detenciones_Delictivo extends Fragment {

    private DetencionesDelictivoViewModel mViewModel;
    Button btnGuardarPuestaDetencionesDelectivo;
    Funciones funciones;
    //RecuperarDetenidos
    ArrayList<String> ListaNombreDetenido,ListaIdDetenido;
    String[] ArrayListaIPHAdministrativo;
    ListView lvDetenidos;
    int PosicionIPHSeleccionado= -1;
    LinearLayout veinticinco,veinticincoUpdate;
    ImageView btnEditarDetenidoDelictivo,btnEliminarDetenidoDelictivo,btnCancelarDetenidoDelictivo;
    String firmaURLServer = "http://189.254.7.167/WebServiceIPH/Firma/SINFIRMA.jpg";
    TextView lblFirmadelDetenidoDelictivo,lblFirmadelDetenidoDelictivoOculto;
    ImageView imgFirmadelDetenidoDelictivoMiniatura;


    ImageView imgFirmaDerechosDelictivo,img_microfonoDescripcionDetenido;
    TextView lblFirmaOcultaDetenidoBase64Detenciones;

    EditText txtFechaDetenidoDelictivo,txthoraDetencionDelictivo,txtFechaNacimientoDetenidoDelictivo, txtPrimerApellidoDetenidoDelictivo, txtSegundoApellidoDetenidoDelictivo,
            txtNombresDetenidoDelictivo, txtApodoDetenidoDelictivo, txtEdadDetenidoDelictivo,
            txtNumeroIdentificacionDelictivo, txtColoniaDetenidoDelictivo,
            txtCalleDetenidoDelictivo, txtNumeroExteriorDetenidoDelictivo, txtNumeroInteriorDetenidoDelictivo, txtCodigoPostalDetenidoDelictivo, txtReferenciasdelLugarDetenidoDelictivo,
            txtDescripciondelDetenidoDelictivo, txtCualPadecimientoDelictivo, txtCualGrupoVulnerableDelictivo, txtCualGrupoDelictivo, txtPrimerApellidoA3Delictivo,
            txtSegundoApellidoA3Delictivo, txtNombresA3Delictivo, txtNumeroTelefonoA3Delictivo,txtCualLugarTraslado, txtColoniaDetencion, txtCalleDetencion,
            txtNumeroExteriorDetencion, txtNumeroInteriorDetencion, txtCodigoPostalDetencion, txtReferenciasdelLugarDetencion, txtObservacionesDetencion;

    CheckBox chNoAplicaAliasDetenidoDelictivo, chNoProporcionadoDelictivo, chLugarTrasladoDetencionFiscaliaAgencia, chLugarTrasladoDetencionHospital, chLugarTrasladoDetencionOtraDependencia;
    RadioGroup rgDocumentoDelictivo, rgLesionesDelictivo, rgPadecimientoDelictivo, rgGrupoVulnerableDelictivo, rgGrupoDelictivo, rgInformeDerechoDetencionesDelictivo,
            rgObjetoInspeccionDetenidoDelictivo, rgPertenenciasDetenidoDelictivo, rgLugarDetencionDelictivo,rgLugarTrasladoDelictivo;

    RadioButton rbNoDocumentoDelictivo, rbSiDocumentoDelictivo, rbNoLesionesDelictivo, rbSiLesionesDelictivo, rbPadecimientoDelictivo, rbSiPadecimientoDelictivo,
            rbNoGrupoVulnerableDelictivo, rbSiGrupoVulnerableDelictivo, rbNoGrupoDelictivo, rbSiGrupoDelictivo, rbNoInformeDerechoDetencionesDelictivo, rbSiInformeDerechoDetencionesDelictivo,
            rbSiObjetoInspeccionDetenidoDelictivo, rbNoObjetoInspeccionDetenidoDelictivo, rbSiPertenenciasDetenidoDelictivo, rbNoPertenenciasDetenidoDelictivo,
            rbSiLugarDetencionDelictivo, rbNoLugarDetencionDelictivo;

    Spinner spGeneroDetenidoDelictivo,spNacionalidadDetenidoDelictivo,spTipoDocumentoDelictivo,spMunicipioPersonaDetenidaDelictivo,spMunicipioDireccionDetencion;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    SharedPreferences share;
    String cargarIdHechoDelictivo,cargarIdPoliciaPrimerRespondiente,descGeneroHD,descNacionalidadHD,
            descTipoDocumentoHD,descMunicipioPersonaDetenidaHD,descMunicipioLugarDetenidoHD,
            varLesiones,varPadecimientos,varGrupoVulnerable,varGrupoDelictivo,varProporcionoFamiliar,
            varInformoDerechos,rutaFirma,varLugarTraslado,descPadecimiento,descGrupoVulnerable,descGrupoDelictivo,varIdentificacionDocumento,varLugarDetencionDelictivo;

    int numberRandom,randomUrlImagen;

    public static Detenciones_Delictivo newInstance() {
        return new Detenciones_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detenciones__delictivo_fragment, container, false);
        /***********************************************************************************/
        cargarDatos();
        random();
        funciones = new Funciones();
        btnGuardarPuestaDetencionesDelectivo = view.findViewById(R.id.btnGuardarPuestaDetencionesDelectivo);
        lblFirmaOcultaDetenidoBase64Detenciones = view.findViewById(R.id.lblFirmaOcultaDetenidoBase64Detenciones);
        imgFirmaDerechosDelictivo = view.findViewById(R.id.imgFirmaDerechosDelictivo);
        img_microfonoDescripcionDetenido = view.findViewById(R.id.img_microfonoDescripcionDetenido);

        txtFechaDetenidoDelictivo = view.findViewById(R.id.txtFechaDetenidoDelictivo);
        txthoraDetencionDelictivo = view.findViewById(R.id.txthoraDetencionDelictivo);
        txtFechaNacimientoDetenidoDelictivo = view.findViewById(R.id.txtFechaNacimientoDetenidoDelictivo);

        txtPrimerApellidoDetenidoDelictivo = view.findViewById(R.id.txtPrimerApellidoDetenidoDelictivo);
        txtPrimerApellidoDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtSegundoApellidoDetenidoDelictivo = view.findViewById(R.id.txtSegundoApellidoDetenidoDelictivo);
        txtSegundoApellidoDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtNombresDetenidoDelictivo = view.findViewById(R.id.txtNombresDetenidoDelictivo);
        txtNombresDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtApodoDetenidoDelictivo = view.findViewById(R.id.txtApodoDetenidoDelictivo);
        txtApodoDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtEdadDetenidoDelictivo = view.findViewById(R.id.txtEdadDetenidoDelictivo);
        txtEdadDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});

        txtNumeroIdentificacionDelictivo = view.findViewById(R.id.txtNumeroIdentificacionDelictivo);
        txtNumeroIdentificacionDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(25)});

        txtColoniaDetenidoDelictivo = view.findViewById(R.id.txtColoniaDetenidoDelictivo);
        txtColoniaDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtCalleDetenidoDelictivo = view.findViewById(R.id.txtCalleDetenidoDelictivo);
        txtCalleDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtNumeroExteriorDetenidoDelictivo = view.findViewById(R.id.txtNumeroExteriorDetenidoDelictivo);
        txtNumeroExteriorDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtNumeroInteriorDetenidoDelictivo = view.findViewById(R.id.txtNumeroInteriorDetenidoDelictivo);
        txtNumeroInteriorDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtCodigoPostalDetenidoDelictivo = view.findViewById(R.id.txtCodigoPostalDetenidoDelictivo);
        txtCodigoPostalDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});

        txtReferenciasdelLugarDetenidoDelictivo = view.findViewById(R.id.txtReferenciasdelLugarDetenidoDelictivo);
        txtReferenciasdelLugarDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});

        txtDescripciondelDetenidoDelictivo = view.findViewById(R.id.txtDescripciondelDetenidoDelictivo);
        txtDescripciondelDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});

        txtCualPadecimientoDelictivo = view.findViewById(R.id.txtCualPadecimientoDelictivo);
        txtCualPadecimientoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtCualGrupoVulnerableDelictivo = view.findViewById(R.id.txtCualGrupoVulnerableDelictivo);
        txtCualGrupoVulnerableDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtCualGrupoDelictivo = view.findViewById(R.id.txtCualGrupoDelictivo);
        txtCualGrupoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtPrimerApellidoA3Delictivo = view.findViewById(R.id.txtPrimerApellidoA3Delictivo);
        txtPrimerApellidoA3Delictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtSegundoApellidoA3Delictivo = view.findViewById(R.id.txtSegundoApellidoA3Delictivo);
        txtSegundoApellidoA3Delictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtNombresA3Delictivo = view.findViewById(R.id.txtNombresA3Delictivo);
        txtNombresA3Delictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtNumeroTelefonoA3Delictivo = view.findViewById(R.id.txtNumeroTelefonoA3Delictivo);
        txtNumeroTelefonoA3Delictivo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        txtColoniaDetencion = view.findViewById(R.id.txtColoniaDetencion);
        txtColoniaDetencion.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtCalleDetencion = view.findViewById(R.id.txtCalleDetencion);
        txtCalleDetencion.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtNumeroExteriorDetencion = view.findViewById(R.id.txtNumeroExteriorDetencion);
        txtNumeroExteriorDetencion.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtNumeroInteriorDetencion = view.findViewById(R.id.txtNumeroInteriorDetencion);
        txtNumeroInteriorDetencion.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtCodigoPostalDetencion = view.findViewById(R.id.txtCodigoPostalDetencion);
        txtCodigoPostalDetencion.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});

        txtReferenciasdelLugarDetencion = view.findViewById(R.id.txtReferenciasdelLugarDetencion);
        txtReferenciasdelLugarDetencion.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});

        txtObservacionesDetencion = view.findViewById(R.id.txtObservacionesDetencion);
        txtObservacionesDetencion.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});

        txtCualLugarTraslado = view.findViewById(R.id.txtCualLugarTraslado);

        rgDocumentoDelictivo = view.findViewById(R.id.rgDocumentoDelictivo);
        rgLesionesDelictivo = view.findViewById(R.id.rgLesionesDelictivo);
        rgPadecimientoDelictivo = view.findViewById(R.id.rgPadecimientoDelictivo);
        rgGrupoVulnerableDelictivo = view.findViewById(R.id.rgGrupoVulnerableDelictivo);
        rgGrupoDelictivo = view.findViewById(R.id.rgGrupoDelictivo);
        rgInformeDerechoDetencionesDelictivo = view.findViewById(R.id.rgInformeDerechoDetencionesDelictivo);
        rgObjetoInspeccionDetenidoDelictivo = view.findViewById(R.id.rgObjetoInspeccionDetenidoDelictivo);
        rgPertenenciasDetenidoDelictivo = view.findViewById(R.id.rgPertenenciasDetenidoDelictivo);
        rgLugarDetencionDelictivo = view.findViewById(R.id.rgLugarDetencionDelictivo);
        rbNoDocumentoDelictivo = view.findViewById(R.id.rbNoDocumentoDelictivo);
        rbSiDocumentoDelictivo = view.findViewById(R.id.rbSiDocumentoDelictivo);
        rbNoLesionesDelictivo = view.findViewById(R.id.rbNoLesionesDelictivo);
        rbSiLesionesDelictivo = view.findViewById(R.id.rbSiLesionesDelictivo);
        rbPadecimientoDelictivo = view.findViewById(R.id.rbPadecimientoDelictivo);
        rbSiPadecimientoDelictivo = view.findViewById(R.id.rbSiPadecimientoDelictivo);
        rbNoGrupoVulnerableDelictivo = view.findViewById(R.id.rbNoGrupoVulnerableDelictivo);
        rbSiGrupoVulnerableDelictivo = view.findViewById(R.id.rbSiGrupoVulnerableDelictivo);
        rbNoGrupoDelictivo = view.findViewById(R.id.rbNoGrupoDelictivo);
        rbSiGrupoDelictivo = view.findViewById(R.id.rbSiGrupoDelictivo);
        rbNoInformeDerechoDetencionesDelictivo = view.findViewById(R.id.rbNoInformeDerechoDetencionesDelictivo);
        rbSiInformeDerechoDetencionesDelictivo = view.findViewById(R.id.rbSiInformeDerechoDetencionesDelictivo);
        rbSiObjetoInspeccionDetenidoDelictivo = view.findViewById(R.id.rbSiObjetoInspeccionDetenidoDelictivo);
        rbNoObjetoInspeccionDetenidoDelictivo = view.findViewById(R.id.rbNoObjetoInspeccionDetenidoDelictivo);
        rbSiPertenenciasDetenidoDelictivo = view.findViewById(R.id.rbSiPertenenciasDetenidoDelictivo);
        rbNoPertenenciasDetenidoDelictivo = view.findViewById(R.id.rbNoPertenenciasDetenidoDelictivo);
        rbSiLugarDetencionDelictivo = view.findViewById(R.id.rbSiLugarDetencionDelictivo);
        rbNoLugarDetencionDelictivo = view.findViewById(R.id.rbNoLugarDetencionDelictivo);
        chNoAplicaAliasDetenidoDelictivo = view.findViewById(R.id.chNoAplicaAliasDetenidoDelictivo);
        chNoProporcionadoDelictivo = view.findViewById(R.id.chNoProporcionadoDelictivo);
        rgLugarTrasladoDelictivo = view.findViewById(R.id.rgLugarTrasladoDelictivo);
        spGeneroDetenidoDelictivo = view.findViewById(R.id.spGeneroDetenidoDelictivo);
        spNacionalidadDetenidoDelictivo = view.findViewById(R.id.spNacionalidadDetenidoDelictivo);
        spTipoDocumentoDelictivo = view.findViewById(R.id.spTipoDocumentoDelictivo);
        spMunicipioPersonaDetenidaDelictivo = view.findViewById(R.id.spMunicipioPersonaDetenidaDelictivo);
        spMunicipioDireccionDetencion = view.findViewById(R.id.spMunicipioDireccionDetencion);
        ListCombos();

        lvDetenidos = view.findViewById(R.id.lvDetenidos);
        veinticinco = view.findViewById(R.id.veinticinco);
        veinticincoUpdate = view.findViewById(R.id.veinticincoUpdate);
        btnEditarDetenidoDelictivo = view.findViewById(R.id.btnEditarDetenidoDelictivo);
        btnEliminarDetenidoDelictivo = view.findViewById(R.id.btnEliminarDetenidoDelictivo);
        btnCancelarDetenidoDelictivo = view.findViewById(R.id.btnCancelarDetenidoDelictivo);
        lblFirmadelDetenidoDelictivo = view.findViewById(R.id.lblFirmadelDetenidoDelictivo);
        lblFirmadelDetenidoDelictivoOculto = view.findViewById(R.id.lblFirmadelDetenidoDelictivoOculto);
        imgFirmadelDetenidoDelictivoMiniatura = view.findViewById(R.id.imgFirmadelDetenidoDelictivoMiniatura);


        funciones.CambiarTituloSeccionesDelictivo("ANEXO A. DETENCIÓN(ES)",getContext(),getActivity());

        //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
        if (funciones.ping(getContext()))
        {
            //Toast.makeText(getContext(), "cargarNoReferenciaAdministrativa()", Toast.LENGTH_LONG).show();
            cargarDetenidos();
        }

        //***************** FECHA  **************************//
        txtFechaDetenidoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaDetenidoDelictivo,getContext(),getActivity());
            }
        });

        //***************** HORA **************************//
        txthoraDetencionDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txthoraDetencionDelictivo,getContext(),getActivity());
            }
        });

        //***************** FECHA  **************************//
        txtFechaNacimientoDetenidoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaNacimientoDetenidoDelictivo,getContext(),getActivity());
            }
        });

        //***************** FIRMA **************************//
        imgFirmaDerechosDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirmaDelictivo dialog = new ContenedorFirmaDelictivo(R.id.lblFirmadelDetenidoDelictivo,R.id.lblFirmadelDetenidoDelictivoOculto,R.id.imgFirmadelDetenidoDelictivoMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });

        //***************** Imagen que funciona para activar la grabación de voz **************************//
        img_microfonoDescripcionDetenido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarEntradadeVoz();
            }
        });

        rgLesionesDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiLesionesDelictivo) {
                    varLesiones = "SI";
                } else if (checkedId == R.id.rbNoLesionesDelictivo) {
                    varLesiones = "NO";
                }

            }
        });

        rgPadecimientoDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiPadecimientoDelictivo) {
                    varPadecimientos = "SI";
                    descPadecimiento = txtCualPadecimientoDelictivo.getText().toString();
                } else if (checkedId == R.id.rbPadecimientoDelictivo) {
                    varPadecimientos = "NO";
                    descPadecimiento = "NA";
                }

            }
        });

        rgGrupoVulnerableDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiGrupoVulnerableDelictivo) {
                    varGrupoVulnerable = "SI";
                    descGrupoVulnerable = txtCualGrupoVulnerableDelictivo.getText().toString();
                } else if (checkedId == R.id.rbNoGrupoVulnerableDelictivo) {
                    varGrupoVulnerable = "NO";
                    descGrupoVulnerable = "NA";
                }

            }
        });

        rgGrupoDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiGrupoDelictivo) {
                    varGrupoDelictivo = "SI";
                    descGrupoDelictivo = txtCualGrupoDelictivo.getText().toString();
                } else if (checkedId == R.id.rbNoGrupoDelictivo) {
                    varGrupoDelictivo = "NO";
                    descGrupoDelictivo = "NA";
                }

            }
        });

        rgInformeDerechoDetencionesDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiInformeDerechoDetencionesDelictivo) {
                    varInformoDerechos = "SI";
                } else if (checkedId == R.id.rbNoInformeDerechoDetencionesDelictivo) {
                    varInformoDerechos = "NO";
                }

            }
        });
        rgLugarTrasladoDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbLugarTrasladoDetencionFiscaliaAgencia) {
                    varLugarTraslado = "FISCALIA/AGENCIA";
                } else if (checkedId == R.id.rbLugarTrasladoDetencionHospital) {
                    varLugarTraslado = "HOSPITAL";
                }else if (checkedId == R.id.rbLugarTrasladoDetencionOtraDependencia) {
                    varLugarTraslado = "OTRA DEPENDENCIA";
                }

            }
        });

        rgDocumentoDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbNoDocumentoDelictivo) {
                    varIdentificacionDocumento = "NO";
                } else if (checkedId == R.id.rbSiDocumentoDelictivo) {
                    varIdentificacionDocumento = "SI";
                }

            }
        });

        rgLugarDetencionDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbNoLugarDetencionDelictivo) {
                    varLugarDetencionDelictivo = "NO";
                } else if (checkedId == R.id.rbSiLugarDetencionDelictivo) {
                    varLugarDetencionDelictivo = "SI";
                }

            }
        });

        //***************** GUARDAR **************************//
        btnGuardarPuestaDetencionesDelectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                insertDetencionesDelictivo();
            }
        });

        lvDetenidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String Json = ArrayListaIPHAdministrativo[position];
                    JSONObject jsonjObject = new JSONObject(Json + "}");

                    PosicionIPHSeleccionado= position;
                    veinticinco.setVisibility(View.GONE);
                    veinticincoUpdate.setVisibility(View.VISIBLE);


                    //Deserealizar y colocar los valores en los campos.


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                    Log.i("DETENCIONES", e.toString());
                }

            }
        });

        btnCancelarDetenidoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCampos();
            }
        });

        btnEliminarDetenidoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnEditarDetenidoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        /**********************************************************************************/
        return view;
    }


    private void limpiarCampos(){

        txtFechaDetenidoDelictivo.setText("");
        txthoraDetencionDelictivo.setText("");
        txtPrimerApellidoDetenidoDelictivo.setText("");
        txtSegundoApellidoDetenidoDelictivo.setText("");
        txtNombresDetenidoDelictivo.setText("");
        txtApodoDetenidoDelictivo.setText("");
        chNoAplicaAliasDetenidoDelictivo.setChecked(false);
        spGeneroDetenidoDelictivo.setSelection(0);
        spNacionalidadDetenidoDelictivo.setSelection(0);

        txtFechaNacimientoDetenidoDelictivo.setText("");
        txtEdadDetenidoDelictivo.setText("");
        rgDocumentoDelictivo.clearCheck();

        spTipoDocumentoDelictivo.setSelection(0);
        txtNumeroIdentificacionDelictivo.setText("");
        spMunicipioPersonaDetenidaDelictivo.setSelection(0);
        txtColoniaDetenidoDelictivo.setText("");
        txtCalleDetenidoDelictivo.setText("");

        txtNumeroExteriorDetenidoDelictivo.setText("");
        txtNumeroInteriorDetenidoDelictivo.setText("");
        txtCodigoPostalDetenidoDelictivo.setText("");
        txtReferenciasdelLugarDetenidoDelictivo.setText("");
        txtDescripciondelDetenidoDelictivo.setText("");
        rgLesionesDelictivo.clearCheck();
        rgPadecimientoDelictivo.clearCheck();
        txtCualPadecimientoDelictivo.setText("");
        rgGrupoVulnerableDelictivo.clearCheck();
        txtCualGrupoVulnerableDelictivo.setText("");
        rgGrupoDelictivo.clearCheck();
        txtCualGrupoDelictivo.setText("");

        txtPrimerApellidoA3Delictivo.setText("");
        txtSegundoApellidoA3Delictivo.setText("");
        txtNombresA3Delictivo.setText("");
        txtNumeroTelefonoA3Delictivo.setText("");

        chNoProporcionadoDelictivo.setChecked(false);
        rgInformeDerechoDetencionesDelictivo.clearCheck();

        //Limpiar Firma
        lblFirmadelDetenidoDelictivo.setText("");
        //imgFirmadelDetenidoDelictivoMiniatura
        lblFirmadelDetenidoDelictivoOculto.setText("");

        rgObjetoInspeccionDetenidoDelictivo.clearCheck();
        rgPertenenciasDetenidoDelictivo.clearCheck();
        rgLugarDetencionDelictivo.clearCheck();

        spMunicipioDireccionDetencion.setSelection(0);
        txtColoniaDetencion.setText("");
        txtCalleDetencion.setText("");
        txtNumeroExteriorDetencion.setText("");
        txtNumeroInteriorDetencion.setText("");
        txtCodigoPostalDetencion.setText("");
        txtReferenciasdelLugarDetencion.setText("");

        //Pendiente Lugar de traslado
        txtCualLugarTraslado.setText("");
        txtObservacionesDetencion.setText("");

        //Oculta botones de actualizar y muestra botón de guardar
        veinticinco.setVisibility(View. VISIBLE);
        veinticincoUpdate.setVisibility(View.GONE);
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertDetencionesDelictivo() {
        DataHelper dataHelper = new DataHelper(getContext());
        descGeneroHD = (String) spGeneroDetenidoDelictivo.getSelectedItem();
        int idDescGeneroHD = dataHelper.getIdSexo(descGeneroHD);
        String idGenero = String.valueOf(idDescGeneroHD);

        descNacionalidadHD = (String) spNacionalidadDetenidoDelictivo.getSelectedItem();
        int idDescNacionalidadHD = dataHelper.getIdNacionalidad(descNacionalidadHD);
        String idNacionalidad = String.valueOf(idDescNacionalidadHD);

        descTipoDocumentoHD = (String) spTipoDocumentoDelictivo.getSelectedItem();
        int idDescTipoDocumentoHD = dataHelper.getIdIdentificacion(descTipoDocumentoHD);
        String idTipoDocumento = String.valueOf(idDescTipoDocumentoHD);

        descMunicipioPersonaDetenidaHD = (String) spMunicipioPersonaDetenidaDelictivo.getSelectedItem();
        String idDescMunicipioPersonaDetenidaHD = dataHelper.getIdMunicipio(descMunicipioPersonaDetenidaHD);
        String idMunicipioPersonaDetenidaHD = String.valueOf(idDescMunicipioPersonaDetenidaHD);

        if(chNoAplicaAliasDetenidoDelictivo.isChecked()){
            txtApodoDetenidoDelictivo.setText("NP");
        }

        if(chNoProporcionadoDelictivo.isChecked()){
            varProporcionoFamiliar = "NO";
            txtPrimerApellidoA3Delictivo.setText("NP");
            txtSegundoApellidoA3Delictivo.setText("NP");
            txtNombresA3Delictivo.setText("NP");
            txtNumeroTelefonoA3Delictivo.setText("NP");
        }else{
            varProporcionoFamiliar = "SI";
        }

        rutaFirma = "http://189.254.7.167/WebServiceIPH/FirmaRDDelictivo/"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";

        ModeloDetenciones_Delictivo detencionesHechoD = new ModeloDetenciones_Delictivo
                (cargarIdHechoDelictivo, ""+randomUrlImagen+"", txtFechaDetenidoDelictivo.getText().toString(), txthoraDetencionDelictivo.getText().toString(),
                        txtPrimerApellidoDetenidoDelictivo.getText().toString(), txtSegundoApellidoDetenidoDelictivo.getText().toString(), txtNombresDetenidoDelictivo.getText().toString(),
                        txtApodoDetenidoDelictivo.getText().toString(),txtDescripciondelDetenidoDelictivo.getText().toString(),
                        idNacionalidad, idGenero, txtFechaNacimientoDetenidoDelictivo.getText().toString(),txtEdadDetenidoDelictivo.getText().toString(),
                        idTipoDocumento, varIdentificacionDocumento, txtNumeroIdentificacionDelictivo.getText().toString(),
                        "12", idMunicipioPersonaDetenidaHD, txtColoniaDetenidoDelictivo.getText().toString(), txtCalleDetenidoDelictivo.getText().toString(),
                        txtNumeroExteriorDetenidoDelictivo.getText().toString(), txtNumeroInteriorDetenidoDelictivo.getText().toString(),
                        txtCodigoPostalDetenidoDelictivo.getText().toString(), txtReferenciasdelLugarDetenidoDelictivo.getText().toString(), varLesiones,
                        varPadecimientos, descPadecimiento, varGrupoVulnerable, descGrupoVulnerable,
                        varGrupoDelictivo, descGrupoDelictivo, varProporcionoFamiliar,txtPrimerApellidoA3Delictivo.getText().toString(),
                        txtSegundoApellidoA3Delictivo.getText().toString(),txtNombresA3Delictivo.getText().toString(), txtNumeroTelefonoA3Delictivo.getText().toString(),
                        varInformoDerechos, rutaFirma, varLugarDetencionDelictivo , varLugarTraslado, txtCualLugarTraslado.getText().toString(),
                        txtObservacionesDetencion.getText().toString(), cargarIdPoliciaPrimerRespondiente);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo", detencionesHechoD.getIdHechoDelictivo())
                .add("NumDetencionRND", detencionesHechoD.getNumDetencionRND())
                .add("Fecha", detencionesHechoD.getFecha())
                .add("Hora", detencionesHechoD.getHora())
                .add("APDentenido", detencionesHechoD.getAPDentenido())
                .add("AMDetenido", detencionesHechoD.getAMDetenido())
                .add("NomDetenido", detencionesHechoD.getNomDetenido())
                .add("ApodoAlias", detencionesHechoD.getApodoAlias())
                .add("DescripcionDetenido", detencionesHechoD.getDescripcionDetenido())
                .add("IdNacionalidad", detencionesHechoD.getIdNacionalidad())
                .add("IdSexo", detencionesHechoD.getIdSexo())
                .add("FechaNacimiento", detencionesHechoD.getFechaNacimiento())
                .add("Edad", detencionesHechoD.getEdad())
                .add("IdIdentificacion", detencionesHechoD.getIdIdentificacion())
                .add("IdentificacionOtro", detencionesHechoD.getIdentificacionOtro())
                .add("NumIdentificacion", detencionesHechoD.getNumIdentificacion())
                .add("IdEntidadfederativa", detencionesHechoD.getIdEntidadfederativa())
                .add("IdMunicipio", detencionesHechoD.getIdMunicipio())
                .add("ColoniaLocalidad", detencionesHechoD.getColoniaLocalidad())
                .add("CalleTramo", detencionesHechoD.getCalleTramo())
                .add("NoExterior", detencionesHechoD.getNoExterior())
                .add("NoInterior", detencionesHechoD.getNoInterior())
                .add("Cp", detencionesHechoD.getCp())
                .add("Referencia", detencionesHechoD.getReferencia())
                .add("Lesiones", detencionesHechoD.getLesiones())
                .add("Padecimientos", detencionesHechoD.getPadecimientos())
                .add("DescPadecimientos", detencionesHechoD.getDescPadecimientos())
                .add("GrupoVulnerable", detencionesHechoD.getGrupoVulnerable())
                .add("DescGrupoVulnerable", detencionesHechoD.getDescGrupoVulnerable())
                .add("GrupoDelictivo", detencionesHechoD.getGrupoDelictivo())
                .add("DescGrupoDelictivo", detencionesHechoD.getDescGrupoDelictivo())
                .add("ProporcionoFamiliar", detencionesHechoD.getProporcionoFamiliar())
                .add("APFamiliar", detencionesHechoD.getAPFamiliar())
                .add("AMFamiliar", detencionesHechoD.getAMFamiliar())
                .add("NomFamiliar", detencionesHechoD.getNomFamiliar())
                .add("TelefonoFamiliar", detencionesHechoD.getTelefonoFamiliar())
                .add("InformoDerechos", detencionesHechoD.getInformoDerechos())
                .add("RutaFirma", detencionesHechoD.getRutaFirma())
                .add("LugarDetencionIntervencion", detencionesHechoD.getLugarDetencionIntervencion())
                .add("LugarTraslado", detencionesHechoD.getIdLugarTraslado())
                .add("DescLugarTrasladoOtro", detencionesHechoD.getDescLugarTrasladoOtro())
                .add("ObservacionesDetencion", detencionesHechoD.getObservacionesDetencion())
                .add("IdPoliciaPrimerRespondiente", detencionesHechoD.getIdPoliciaPrimerRespondiente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDDetenciones/")
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
                    Detenciones_Delictivo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                //Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                //insertLugarDetencionesDelictivo();
                                insertImagen();
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

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertLugarDetencionesDelictivo() {
        DataHelper dataHelper = new DataHelper(getContext());

        descMunicipioLugarDetenidoHD = (String) spMunicipioDireccionDetencion.getSelectedItem();
        String idDescMunicipioLugarDetenidoHD = dataHelper.getIdMunicipio(descMunicipioPersonaDetenidaHD);
        String idMunicipioLugarDetenidoHD = String.valueOf(idDescMunicipioLugarDetenidoHD);

        ModeloLugarDetenciones_Delictivo modeloLugarDetenciones = new ModeloLugarDetenciones_Delictivo
                (cargarIdHechoDelictivo, "12", idMunicipioLugarDetenidoHD,
                        txtColoniaDetencion.getText().toString(), txtCalleDetencion.getText().toString(), txtNumeroExteriorDetencion.getText().toString(),
                        txtNumeroInteriorDetencion.getText().toString(), txtCodigoPostalDetencion.getText().toString(),
                        txtReferenciasdelLugarDetencion.getText().toString());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo", modeloLugarDetenciones.getIdHechoDelictivo())
                .add("IdEntidadFederativa", modeloLugarDetenciones.getIdEntidadFederativa())
                .add("IdMunicipio", modeloLugarDetenciones.getIdMunicipio())
                .add("ColoniaLocalidad", modeloLugarDetenciones.getColoniaLocalidad())
                .add("CalleTramo", modeloLugarDetenciones.getCalleTramo())
                .add("NoExterior", modeloLugarDetenciones.getNoExterior())
                .add("NoInterior", modeloLugarDetenciones.getNoInterior())
                .add("Cp", modeloLugarDetenciones.getCp())
                .add("Referencia", modeloLugarDetenciones.getReferencia())

                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDLugarDetencion/")
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
                    Detenciones_Delictivo.this.getActivity().runOnUiThread(new Runnable() {
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

    //********************************** INSERTA IMAGEN AL SERVIDOR ***********************************//
    public void insertImagen() {
        String cadena = lblFirmaOcultaDetenidoBase64Detenciones.getText().toString();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Description", cargarIdHechoDelictivo+randomUrlImagen+".jpg")
                .add("ImageData", cadena)
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/MultimediaFirmaDetencionesDerechos")
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
                if (response.isSuccessful()) {
                    final String myResponse = response.body().toString();  /********** ME REGRESA LA RESPUESTA DEL WS ****************/
                    Detenciones_Delictivo.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("EL DATO DE LA IMAGEN SE ENVIO CORRECTAMENTE");
                            //Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            insertLugarDetencionesDelictivo();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetencionesDelictivoViewModel.class);
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
                    String textoActual = txtDescripciondelDetenidoDelictivo.getText().toString();
                    txtDescripciondelDetenidoDelictivo.setText(textoActual+" " + result.get(0));
                }
                break;
            }
        }
    }

    private void ListCombos() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> listMuniPDetenida = dataHelper.getAllMunicipios();
        ArrayList<String> listNacion = dataHelper.getAllNacionalidad();
        ArrayList<String> listSexo = dataHelper.getAllSexo();
        ArrayList<String> listIdentificacion = dataHelper.getAllIdentificacion();
        if (listMuniPDetenida.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MUNICIPIOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listMuniPDetenida);
            spMunicipioPersonaDetenidaDelictivo.setAdapter(adapter);
            spMunicipioDireccionDetencion.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON MUNICIPIOS ACTIVOS.", Toast.LENGTH_LONG).show();
        }
        if (listNacion.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE NACIONALIDAD");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listNacion);
            spNacionalidadDetenidoDelictivo.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON NACIONES ACTIVAS.", Toast.LENGTH_LONG).show();
        }
        if (listSexo.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE SEXO");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listSexo);
            spGeneroDetenidoDelictivo.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON SEXOS ACTIVOS.", Toast.LENGTH_LONG).show();
        }
        if (listIdentificacion.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE IDENTIFICACIONES");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, listIdentificacion);
            spTipoDocumentoDelictivo.setAdapter(adapter);
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
    private void cargarDetenidos() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDDetenciones?folioInterno="+cargarIdHechoDelictivo)
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
                                    ArrayListaIPHAdministrativo = ArrayIPHAdministrativo;

                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordeDetenido=0;
                                    while(contadordeDetenido < ArrayIPHAdministrativo.length){
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayIPHAdministrativo[contadordeDetenido] + "}");

                                            //ListaDetenidos.add(jsonjObject);

                                            // ListaIdentificadorDetenido.add(Integer.toString(jsonjObject.getInt("IdDetenido")));

                                            ListaIdDetenido.add(jsonjObject.getString("IdDetenido"));
                                            ListaNombreDetenido.add(((jsonjObject.getString("APDentenido")).equals("null")?"":jsonjObject.getString("APDentenido")) +
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
                                Detenciones_Delictivo.MyAdapter adapter = new Detenciones_Delictivo.MyAdapter(getContext(), ListaIdDetenido, ListaNombreDetenido);
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
            lblNumDetencion.setText("PERSONA DETENIDA:");
            lblNombreCompleto.setText(ListaNombreDetenido.get(position));

            return row;
        }
    }
}