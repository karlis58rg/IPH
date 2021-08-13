package mx.ssp.iph.delictivo.ui.fragmets;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
    RadioButton rbLugarTrasladoDetencionFiscaliaAgencia,rbLugarTrasladoDetencionHospital,rbLugarTrasladoDetencionOtraDependencia;


    ImageView imgFirmaDerechosDelictivo,img_microfonoDescripcionDetenido;
    TextView lblFirmaOcultaDetenidoBase64Detenciones, lblCualLugarTraslado;

    EditText txtFechaDetenidoDelictivo,txthoraDetencionDelictivo,txtFechaNacimientoDetenidoDelictivo, txtPrimerApellidoDetenidoDelictivo, txtSegundoApellidoDetenidoDelictivo,
            txtNombresDetenidoDelictivo, txtApodoDetenidoDelictivo, txtEdadDetenidoDelictivo,
            txtNumeroIdentificacionDelictivo, txtColoniaDetenidoDelictivo,
            txtCalleDetenidoDelictivo, txtNumeroExteriorDetenidoDelictivo, txtNumeroInteriorDetenidoDelictivo, txtCodigoPostalDetenidoDelictivo, txtReferenciasdelLugarDetenidoDelictivo,
            txtDescripciondelDetenidoDelictivo, txtCualPadecimientoDelictivo, txtCualGrupoVulnerableDelictivo, txtCualGrupoDelictivo, txtPrimerApellidoA3Delictivo,
            txtSegundoApellidoA3Delictivo, txtNombresA3Delictivo, txtNumeroTelefonoA3Delictivo,txtCualLugarTraslado, txtColoniaDetencion, txtCalleDetencion,
            txtNumeroExteriorDetencion, txtNumeroInteriorDetencion, txtCodigoPostalDetencion, txtReferenciasdelLugarDetencion, txtObservacionesDetencion;

    CheckBox chNoAplicaAliasDetenidoDelictivo, chNoProporcionadoDelictivo;
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

    ViewGroup segundoLinear, cuartoLinear, quintoUnoLinear,
            quintoTresLinear, catorceavoLinear, quinceavoLinear,
            dieciseisLinear, diecisietelinear, diecisietelinear2, lyLecturaDerechos,
            septimounoLinear, septimodosLinear, septimotresLinear, treintaicuatroLinear, treintaicincoLinear,
            lyLesiones, treintaidosLinear, treintaLinear, lyColoniaDetencionDelictivo, veintinueveLinear,
            septimoLinear, lyGrupoDelictivoDelictivo, lyPrimerApFamDet, lyNomFamDet, lyTelFamDet,
            lyCalleTramoDet, lyReferenciaLugarDet, treintaicuatrounoLinear;

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
        rbLugarTrasladoDetencionFiscaliaAgencia  = view.findViewById(R.id.rbLugarTrasladoDetencionFiscaliaAgencia);
        rbLugarTrasladoDetencionHospital = view.findViewById(R.id.rbLugarTrasladoDetencionHospital);
        rbLugarTrasladoDetencionOtraDependencia = view.findViewById(R.id.rbLugarTrasladoDetencionOtraDependencia);
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
        lblCualLugarTraslado= view.findViewById(R.id.lblCualLugarTraslado);

        rbLugarTrasladoDetencionFiscaliaAgencia = view.findViewById(R.id.rbLugarTrasladoDetencionFiscaliaAgencia);
        rbLugarTrasladoDetencionHospital = view.findViewById(R.id.rbLugarTrasladoDetencionHospital);
        rbLugarTrasladoDetencionOtraDependencia = view.findViewById(R.id.rbLugarTrasladoDetencionOtraDependencia);

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


        //RADIOGRUPO DOCUMENTACIÓN
        rgDocumentoDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiDocumentoDelictivo) {
                    varIdentificacionDocumento = "SI";
                    spTipoDocumentoDelictivo.setVisibility(view.VISIBLE);
                    txtNumeroIdentificacionDelictivo.setEnabled(true);
                } else if (checkedId == R.id.rbNoDocumentoDelictivo) {
                    varIdentificacionDocumento = "NO";
                    spTipoDocumentoDelictivo.setVisibility(view.INVISIBLE);
                    txtNumeroIdentificacionDelictivo.setEnabled(false);
                    txtNumeroIdentificacionDelictivo.setText("");
                }

            }
        });


        //RADIOGRUPO LESIONES
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


        //RADIOGRUPO PADECIMIENTO
        rgPadecimientoDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiPadecimientoDelictivo) {
                    varPadecimientos = "SI";
                    descPadecimiento = txtCualPadecimientoDelictivo.getText().toString();
                    txtCualPadecimientoDelictivo.setVisibility(view.VISIBLE);
                    txtCualPadecimientoDelictivo.setEnabled(true);
                } else if (checkedId == R.id.rbPadecimientoDelictivo) {
                    varPadecimientos = "NO";
                    descPadecimiento = "NA";

                    txtCualPadecimientoDelictivo.setVisibility(view.INVISIBLE);
                    txtCualPadecimientoDelictivo.setEnabled(false);
                    txtCualPadecimientoDelictivo.setText("");
                }

            }
        });


        //RADIOGRUPO GRUPO VULNERABLE
        rgGrupoVulnerableDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiGrupoVulnerableDelictivo) {
                    varGrupoVulnerable = "SI";
                    descGrupoVulnerable = txtCualGrupoVulnerableDelictivo.getText().toString();
                    txtCualGrupoVulnerableDelictivo.setVisibility(view.VISIBLE);
                    txtCualGrupoVulnerableDelictivo.setEnabled(true);
                } else if (checkedId == R.id.rbNoGrupoVulnerableDelictivo) {
                    varGrupoVulnerable = "NO";
                    descGrupoVulnerable = "NA";
                    txtCualGrupoVulnerableDelictivo.setVisibility(view.INVISIBLE);
                    txtCualGrupoVulnerableDelictivo.setEnabled(false);
                    txtCualGrupoVulnerableDelictivo.setText("");
                }

            }
        });


        //RADIOGRUPO GRUPO DELICTIVO
        rgGrupoDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiGrupoDelictivo) {
                    varGrupoDelictivo = "SI";
                    descGrupoDelictivo = txtCualGrupoDelictivo.getText().toString();
                    txtCualGrupoDelictivo.setVisibility(view.VISIBLE);
                    txtCualGrupoDelictivo.setEnabled(true);
                } else if (checkedId == R.id.rbNoGrupoDelictivo) {
                    varGrupoDelictivo = "NO";
                    descGrupoDelictivo = "NA";
                    txtCualGrupoDelictivo.setVisibility(view.INVISIBLE);
                    txtCualGrupoDelictivo.setEnabled(false);
                    txtCualGrupoDelictivo.setText("");
                }

            }
        });


        //RADIOGRUPO INFORME DE DERECHOS
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


        //RADIOGRUPO LUGAR DE TRASLADO
        rgLugarTrasladoDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbLugarTrasladoDetencionFiscaliaAgencia) {
                    varLugarTraslado = "FISCALIA/AGENCIA";
                    txtCualLugarTraslado.setVisibility(view.INVISIBLE);
                    txtCualLugarTraslado.setEnabled(false);
                    txtCualLugarTraslado.setText("");
                    lblCualLugarTraslado.setVisibility(view.INVISIBLE);
                } else if (checkedId == R.id.rbLugarTrasladoDetencionHospital) {
                    varLugarTraslado = "HOSPITAL";
                    txtCualLugarTraslado.setVisibility(view.INVISIBLE);
                    txtCualLugarTraslado.setEnabled(false);
                    txtCualLugarTraslado.setText("");
                    lblCualLugarTraslado.setVisibility(view.INVISIBLE);
                }else if (checkedId == R.id.rbLugarTrasladoDetencionOtraDependencia) {
                    varLugarTraslado = "OTRA DEPENDENCIA";
                    txtCualLugarTraslado.setVisibility(view.VISIBLE);
                    txtCualLugarTraslado.setEnabled(true);
                    lblCualLugarTraslado.setVisibility(view.VISIBLE);
                    txtCualLugarTraslado.setText("");
                }

            }
        });




        //******* HABILITAR DESHABILITAR CAMPO DE TEXTO APODO *******//
        chNoAplicaAliasDetenidoDelictivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {

                if(chselect == true){
                    txtApodoDetenidoDelictivo.setEnabled(false);
                    //varNoAlias = "NA";
                    txtApodoDetenidoDelictivo.setText("");
                } else if(chselect == false) {
                    txtApodoDetenidoDelictivo.setEnabled(true);
                    //varNoAlias = "";
                    txtApodoDetenidoDelictivo.setText("");
                }

            }
        });

        //******* HABILITAR DESHABILITAR CAMPO DE TEXTO TELEFONO FAMILIAR *******//
        chNoProporcionadoDelictivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {
                if(chselect == true){
                    txtNumeroTelefonoA3Delictivo.setEnabled(false);
                    //varNoAlias = "NA";
                    txtNumeroTelefonoA3Delictivo.setText("");
                } else if(chselect == false) {
                    txtNumeroTelefonoA3Delictivo.setEnabled(true);
                    //varNoAlias = "";
                    txtNumeroTelefonoA3Delictivo.setText("");
                }
            }
        });


        //***************** GUARDAR **************************//
        btnGuardarPuestaDetencionesDelectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrimeraValidacion();
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
                    txtFechaDetenidoDelictivo.setText(((jsonjObject.getString("Fecha")).equals("null")?"":jsonjObject.getString("Fecha")).replace("-","/").substring(0,10));
                    txthoraDetencionDelictivo.setText(((jsonjObject.getString("Hora")).equals("null")?"":jsonjObject.getString("Hora")));

                    txtPrimerApellidoDetenidoDelictivo.setText(((jsonjObject.getString("APDentenido")).equals("null")?"":jsonjObject.getString("APDentenido")));
                    txtSegundoApellidoDetenidoDelictivo.setText(((jsonjObject.getString("AMDetenido")).equals("null")?"":jsonjObject.getString("AMDetenido")));
                    txtNombresDetenidoDelictivo.setText(((jsonjObject.getString("NomDetenido")).equals("null")?"":jsonjObject.getString("NomDetenido")));

                   if ((jsonjObject.getString("ApodoAlias")).equals("NP"))
                    {
                        chNoAplicaAliasDetenidoDelictivo.setChecked(true);
                        txtApodoDetenidoDelictivo.setText("");
                    }else
                    {
                        txtApodoDetenidoDelictivo.setText(((jsonjObject.getString("ApodoAlias")).equals("null")?"":jsonjObject.getString("ApodoAlias")));
                    }

                    spGeneroDetenidoDelictivo.setSelection(funciones.getIndexSpiner(spGeneroDetenidoDelictivo, jsonjObject.getString("IdSexo")));
                    spNacionalidadDetenidoDelictivo.setSelection(funciones.getIndexSpiner(spNacionalidadDetenidoDelictivo, jsonjObject.getString("IdNacionalidad")));

                    txtFechaNacimientoDetenidoDelictivo.setText(((jsonjObject.getString("FechaNacimiento")).equals("null")?"":jsonjObject.getString("FechaNacimiento")).replace("-","/").substring(0,10));
                    txtEdadDetenidoDelictivo.setText(((jsonjObject.getString("Edad")).equals("null")?"":jsonjObject.getString("Edad")));

                    if ((jsonjObject.getString("IdentificacionOtro")).equals("SI"))
                    {
                        rbSiDocumentoDelictivo.setChecked(true);

                    }else if ((jsonjObject.getString("IdentificacionOtro")).equals("NO"))
                    {
                        rbNoDocumentoDelictivo.setChecked(true);
                    }

                    spTipoDocumentoDelictivo.setSelection(funciones.getIndexSpiner(spTipoDocumentoDelictivo, jsonjObject.getString("IdIdentificacion")));
                    txtNumeroIdentificacionDelictivo.setText(((jsonjObject.getString("NumIdentificacion")).equals("null")?"":jsonjObject.getString("NumIdentificacion")));

                    spMunicipioPersonaDetenidaDelictivo.setSelection(funciones.getIndexSpiner(spTipoDocumentoDelictivo, jsonjObject.getString("IdMunicipio")));
                    txtColoniaDetenidoDelictivo.setText(((jsonjObject.getString("ColoniaLocalidad")).equals("null")?"":jsonjObject.getString("ColoniaLocalidad")));
                    txtCalleDetenidoDelictivo.setText(((jsonjObject.getString("CalleTramo")).equals("null")?"":jsonjObject.getString("CalleTramo")));
                    txtNumeroExteriorDetenidoDelictivo.setText(((jsonjObject.getString("NoExterior")).equals("null")?"":jsonjObject.getString("NoExterior")));
                    txtNumeroInteriorDetenidoDelictivo.setText(((jsonjObject.getString("NoInterior")).equals("null")?"":jsonjObject.getString("NoInterior")));
                    txtCodigoPostalDetenidoDelictivo.setText(((jsonjObject.getString("Cp")).equals("null")?"":jsonjObject.getString("Cp")));
                    txtReferenciasdelLugarDetenidoDelictivo.setText(((jsonjObject.getString("Referencia")).equals("null")?"":jsonjObject.getString("Referencia")));
                    txtDescripciondelDetenidoDelictivo.setText(((jsonjObject.getString("DescripcionDetenido")).equals("null")?"":jsonjObject.getString("DescripcionDetenido")));

                    //============= Lesiones
                    if ((jsonjObject.getString("Lesiones")).equals("SI"))
                    {
                        rbSiLesionesDelictivo.setChecked(true);

                    }else if ((jsonjObject.getString("Lesiones")).equals("NO"))
                    {
                        rbNoLesionesDelictivo.setChecked(true);
                    }

                    //============= Padecimientos
                    if ((jsonjObject.getString("Padecimientos")).equals("SI"))
                    {
                        rbPadecimientoDelictivo.setChecked(true);
                        txtCualPadecimientoDelictivo.setText(((jsonjObject.getString("DescPadecimientos")).equals("null")?"":jsonjObject.getString("DescPadecimientos")));
                    }else if ((jsonjObject.getString("Padecimientos")).equals("NO"))
                    {
                        rbPadecimientoDelictivo.setChecked(true);
                    }

                    //============= GrupoVulnerable
                    if ((jsonjObject.getString("GrupoVulnerable")).equals("SI"))
                    {
                        rbSiGrupoVulnerableDelictivo.setChecked(true);
                        txtCualGrupoVulnerableDelictivo.setText(((jsonjObject.getString("DescGrupoVulnerable")).equals("null")?"":jsonjObject.getString("DescGrupoVulnerable")));
                    }else if ((jsonjObject.getString("GrupoVulnerable")).equals("NO"))
                    {
                        rbNoGrupoVulnerableDelictivo.setChecked(true);
                    }

                    //============= Grupo Delictivo
                    if ((jsonjObject.getString("GrupoDelictivo")).equals("SI"))
                    {
                        rbSiGrupoDelictivo.setChecked(true);
                        txtCualGrupoDelictivo.setText(((jsonjObject.getString("DescGrupoDelictivo")).equals("null")?"":jsonjObject.getString("DescGrupoDelictivo")));
                    }else if ((jsonjObject.getString("GrupoDelictivo")).equals("NO"))
                    {
                        rbNoGrupoDelictivo.setChecked(true);
                    }

                    //============= ProporcionoFamiliar
                    if ((jsonjObject.getString("ProporcionoFamiliar")).equals("SI"))
                    {
                        rbSiGrupoDelictivo.setChecked(false);
                    }else if ((jsonjObject.getString("ProporcionoFamiliar")).equals("NO"))
                    {
                        chNoProporcionadoDelictivo.setChecked(true);
                        txtPrimerApellidoA3Delictivo.setText(((jsonjObject.getString("APFamiliar")).equals("null")?"":jsonjObject.getString("APFamiliar")));
                        txtSegundoApellidoA3Delictivo.setText(((jsonjObject.getString("AMFamiliar")).equals("null")?"":jsonjObject.getString("AMFamiliar")));
                        txtNombresA3Delictivo.setText(((jsonjObject.getString("NomFamiliar")).equals("null")?"":jsonjObject.getString("NomFamiliar")));
                        txtNumeroTelefonoA3Delictivo.setText(((jsonjObject.getString("TelefonoFamiliar")).equals("null")?"":jsonjObject.getString("TelefonoFamiliar")));
                    }

                    //============= InformoDerechos
                    if ((jsonjObject.getString("InformoDerechos")).equals("SI"))
                    {
                        rbSiInformeDerechoDetencionesDelictivo.setChecked(true);
                    }else if ((jsonjObject.getString("InformoDerechos")).equals("NO"))
                    {
                        rbNoInformeDerechoDetencionesDelictivo.setChecked(true);
                    }

                    //Firma
                    lblFirmadelDetenidoDelictivo.setText((jsonjObject.getString("RutaFirma")).equals("null")?"":"FIRMA CORRECTA");
                    firmaURLServer = (jsonjObject.getString("RutaFirma").equals("null")?"http://189.254.7.167/WebServiceIPH/Firma/SINFIRMA.jpg":jsonjObject.getString("RutaFirma"));
                    lblFirmadelDetenidoDelictivoOculto.setText(firmaURLServer);
                    getFirmaFromURL();

                    //Pendiente Pertencencias Anexo  y pertenecncias personals

        segundoLinear = view.findViewById(R.id.segundoLinear);
        cuartoLinear = view.findViewById(R.id.cuartoLinear);
        quintoUnoLinear = view.findViewById(R.id.quintoUnoLinear);
        quintoTresLinear = view.findViewById(R.id.quintoTresLinear);
        catorceavoLinear = view.findViewById(R.id.catorceavoLinear);
        quinceavoLinear = view.findViewById(R.id.quinceavoLinear);
        dieciseisLinear = view.findViewById(R.id.dieciseisLinear);
        diecisietelinear = view.findViewById(R.id.diecisietelinear);
        diecisietelinear2 = view.findViewById(R.id.diecisietelinear2);
        lyLecturaDerechos = view.findViewById(R.id.lyLecturaDerechos);
        septimounoLinear = view.findViewById(R.id.septimounoLinear);
        septimodosLinear = view.findViewById(R.id.septimodosLinear);
        septimotresLinear = view.findViewById(R.id.septimotresLinear);
        treintaicuatroLinear = view.findViewById(R.id.treintaicuatroLinear);
        treintaicincoLinear = view.findViewById(R.id.treintaicincoLinear);
        lyLesiones = view.findViewById(R.id.lyLesiones);
        treintaidosLinear = view.findViewById(R.id.treintaidosLinear);
        treintaLinear = view.findViewById(R.id.treintaLinear);
        lyColoniaDetencionDelictivo = view.findViewById(R.id.lyColoniaDetencionDelictivo);
        veintinueveLinear = view.findViewById(R.id.veintinueveLinear);
        septimoLinear = view.findViewById(R.id.septimoLinear);
        lyGrupoDelictivoDelictivo = view.findViewById(R.id.lyGrupoDelictivoDelictivo);
        lyPrimerApFamDet = view.findViewById(R.id.lyPrimerApFamDet);
        lyNomFamDet = view.findViewById(R.id.lyNomFamDet);
        lyTelFamDet = view.findViewById(R.id.lyTelFamDet);
        lyCalleTramoDet = view.findViewById(R.id.lyCalleTramoDet);
        lyReferenciaLugarDet = view.findViewById(R.id.lyReferenciaLugarDet);
        treintaicuatrounoLinear = view.findViewById(R.id.treintaicuatrounoLinear);


                    //=============LugarDetencionIntervencion
                    if ((jsonjObject.getString("LugarDetencionIntervencion")).equals("SI"))
                    {
                        //No hace nada. Se oculta el formulario con la validación de josué
                    }else if ((jsonjObject.getString("LugarDetencionIntervencion")).equals("NO"))
                    {
                        spMunicipioDireccionDetencion.setSelection(funciones.getIndexSpiner(spMunicipioDireccionDetencion, jsonjObject.getString("IdMunicipioLD")));
                        txtColoniaDetencion.setText(((jsonjObject.getString("ColoniaLocalidadLD")).equals("null")?"":jsonjObject.getString("ColoniaLocalidadLD")));
                        txtCalleDetencion.setText(((jsonjObject.getString("CalleTramoLD")).equals("null")?"":jsonjObject.getString("CalleTramoLD")));
                        txtNumeroExteriorDetencion.setText(((jsonjObject.getString("NoExteriorLD")).equals("null")?"":jsonjObject.getString("NoExteriorLD")));
                        txtNumeroInteriorDetencion.setText(((jsonjObject.getString("NoInteriorLD")).equals("null")?"":jsonjObject.getString("NoInteriorLD")));
                        txtCodigoPostalDetencion.setText(((jsonjObject.getString("CpLD")).equals("null")?"":jsonjObject.getString("CpLD")));
                        txtReferenciasdelLugarDetencion.setText(((jsonjObject.getString("ReferenciaLD")).equals("null")?"":jsonjObject.getString("ReferenciaLD")));
                    }

                    //============= rgLugarTrasladoDelictivo
                    if ((jsonjObject.getString("IdLugarTraslado")).equals("FISCALIA"))
                    {
                        rbLugarTrasladoDetencionFiscaliaAgencia.setChecked(true);
                    }else if ((jsonjObject.getString("IdLugarTraslado")).equals("HOSPITAL"))
                    {
                        rbLugarTrasladoDetencionHospital.setChecked(true);

                    }else if ((jsonjObject.getString("IdLugarTraslado")).equals("OTRA DEPENDENCIA"))
                    {
                        rbLugarTrasladoDetencionOtraDependencia.setChecked(true);
                        txtCualLugarTraslado.setText(((jsonjObject.getString("DescLugarTrasladoOtro")).equals("null")?"":jsonjObject.getString("DescLugarTrasladoOtro")));
                    }

                    txtObservacionesDetencion .setText(((jsonjObject.getString("ObservacionesDetencion")).equals("null")?"":jsonjObject.getString("ObservacionesDetencion")));


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

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext()).
                                setMessage("¿DESEA ELIMINAR EL DETENIDO "+ ListaIdDetenido.get(PosicionIPHSeleccionado) + "?" ).
                                setPositiveButton( "ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //CONSUME WEB SERVICE PARA ELIMINAR DB
                                        String IdDetenido = ListaIdDetenido.get(PosicionIPHSeleccionado);
                                        EliminarDEtenido(IdDetenido);
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

        btnEditarDetenidoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        /**********************************************************************************/
        return view;
    }

    //Validaciones

    public void PrimeraValidacion(){
        if (txtFechaDetenidoDelictivo.getText().toString().length() >= 3 && txthoraDetencionDelictivo.getText().toString().length() >= 3) {
            if (txtPrimerApellidoDetenidoDelictivo.getText().toString().length() >= 3) {
                if (txtNombresDetenidoDelictivo.getText().toString().length() >= 3) {
                    if (chNoAplicaAliasDetenidoDelictivo.isChecked()) {
                        SegundaValidacion();
                    } else if (txtApodoDetenidoDelictivo.getText().toString().length() >= 3) {
                        SegundaValidacion();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI TIENE ALGÚN APODO", Toast.LENGTH_SHORT).show();
                        quintoUnoLinear.requestFocus();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "INGRESA AL MENOS UN NOMBRE DEL DETENIDO", Toast.LENGTH_SHORT).show();
                    cuartoLinear.requestFocus();
                    txtNombresDetenidoDelictivo.requestFocus();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "INGRESA AL MENOS EL PRIMER APELLIDO DEL DETENIDO", Toast.LENGTH_SHORT).show();
                cuartoLinear.requestFocus();
                txtPrimerApellidoDetenidoDelictivo.requestFocus();
            }

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "NO SE PUEDE ALMACENAR INFORMACIÓN DE DETENCIÓN SIN FECHA Y HORA DE REGISTRO", Toast.LENGTH_SHORT).show();
            segundoLinear.requestFocus();
        }

    }

    public void SegundaValidacion() {
        if (rbSiDocumentoDelictivo.isChecked()) {
            if(txtDescripciondelDetenidoDelictivo.getText().toString().length() >= 3){
                //Tercera Validacion
                TerceraValidacion();
            }
        } else if (rbNoDocumentoDelictivo.isChecked()){
            if(txtDescripciondelDetenidoDelictivo.getText().toString().length() >= 3){
                //Tercera Validacion
                TerceraValidacion();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI SE IDENTIFICÓ CON ALGÚN DOCUMENTO", Toast.LENGTH_SHORT).show();
            quintoTresLinear.requestFocus();
        }


    }

    public void TerceraValidacion(){
        if(rbNoLesionesDelictivo.isChecked() || rbSiLesionesDelictivo.isChecked()) {
            if(rbPadecimientoDelictivo.isChecked()){
                if(rbNoGrupoVulnerableDelictivo.isChecked()){
                    if(rbNoGrupoDelictivo.isChecked()){
                        CuartaValidacion();
                    } else if (rbSiGrupoDelictivo.isChecked()){
                        if(txtCualGrupoDelictivo.getText().toString().length() >= 3){
                            CuartaValidacion();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO DELICTIVO AL QUE PERTENECE EL DETENIDO", Toast.LENGTH_SHORT).show();
                            txtCualGrupoDelictivo.requestFocus();
                            lyGrupoDelictivoDelictivo.requestFocus();
                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI PERTENECE A ALGÚN GRUPO DELICTIVO", Toast.LENGTH_SHORT).show();
                        diecisietelinear2.requestFocus();
                    }
                } else if(rbSiGrupoVulnerableDelictivo.isChecked()){
                    if(txtCualGrupoVulnerableDelictivo.getText().toString().length() >= 3){
                        if(rbNoGrupoDelictivo.isChecked()){
                            CuartaValidacion();
                        } else if (rbSiGrupoDelictivo.isChecked()){
                            if(txtCualGrupoDelictivo.getText().toString().length() >= 3){
                                CuartaValidacion();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO DELICTIVO AL QUE PERTENECE EL DETENIDO", Toast.LENGTH_SHORT).show();
                                txtCualGrupoDelictivo.requestFocus();
                                lyGrupoDelictivoDelictivo.requestFocus();
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI PERTENECE A ALGÚN GRUPO DELICTIVO", Toast.LENGTH_SHORT).show();
                            diecisietelinear2.requestFocus();
                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO VULNERABLE AL QUE PERTENECE", Toast.LENGTH_SHORT).show();
                        diecisietelinear.requestFocus();
                        txtCualGrupoVulnerableDelictivo.requestFocus();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI PERTENECE A ALGÚN GRUPO VULNERABLE", Toast.LENGTH_SHORT).show();
                    diecisietelinear.requestFocus();
                }
            } else if(rbSiPadecimientoDelictivo.isChecked()){
                if(txtCualPadecimientoDelictivo.getText().toString().length() >= 3){
                    if(rbNoGrupoVulnerableDelictivo.isChecked()){
                        if(rbNoGrupoDelictivo.isChecked()){
                            CuartaValidacion();
                        } else if (rbSiGrupoDelictivo.isChecked()){
                            if(txtCualGrupoDelictivo.getText().toString().length() >= 3){
                                CuartaValidacion();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO DELICTIVO AL QUE PERTENECE EL DETENIDO", Toast.LENGTH_SHORT).show();
                                txtCualGrupoDelictivo.requestFocus();
                                lyGrupoDelictivoDelictivo.requestFocus();
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI PERTENECE A ALGÚN GRUPO DELICTIVO", Toast.LENGTH_SHORT).show();
                            diecisietelinear2.requestFocus();
                        }
                    } else if(rbSiGrupoVulnerableDelictivo.isChecked()){
                        if(txtCualGrupoVulnerableDelictivo.getText().toString().length() >= 3){
                            if(rbNoGrupoDelictivo.isChecked()){
                                CuartaValidacion();
                            } else if (rbSiGrupoDelictivo.isChecked()){
                                if(txtCualGrupoDelictivo.getText().toString().length() >= 3){
                                    CuartaValidacion();
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO DELICTIVO AL QUE PERTENECE EL DETENIDO", Toast.LENGTH_SHORT).show();
                                    txtCualGrupoDelictivo.requestFocus();
                                    lyGrupoDelictivoDelictivo.requestFocus();
                                }
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI PERTENECE A ALGÚN GRUPO DELICTIVO", Toast.LENGTH_SHORT).show();
                                diecisietelinear2.requestFocus();
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO VULNERABLE AL QUE PERTENECE", Toast.LENGTH_SHORT).show();
                            diecisietelinear.requestFocus();
                            txtCualGrupoVulnerableDelictivo.requestFocus();
                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI PERTENECE A ALGÚN GRUPO VULNERABLE", Toast.LENGTH_SHORT).show();
                        diecisietelinear.requestFocus();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL PADECIMIENTO DEL DETENIDO", Toast.LENGTH_SHORT).show();
                    dieciseisLinear.requestFocus();
                    txtCualPadecimientoDelictivo.requestFocus();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI TIENE ALGÚN PADECIMIENTO", Toast.LENGTH_SHORT).show();
                dieciseisLinear.requestFocus();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI PRESENTA LESIONES", Toast.LENGTH_SHORT).show();
            lyLesiones.requestFocus();
            quinceavoLinear.requestFocus();
        }
    }

    public void CuartaValidacion(){
        if(txtPrimerApellidoA3Delictivo.getText().toString().length() >= 3){
            if(txtNombresA3Delictivo.getText().toString().length() >= 3){
                if(chNoProporcionadoDelictivo.isChecked()){
                    //VALIDACION INFORME DERECHOS
                    if(rbNoInformeDerechoDetencionesDelictivo.isChecked()){
                        if(rbSiObjetoInspeccionDetenidoDelictivo.isChecked()){
                            //HABILITAR ANEXO D
                            if(rbSiPertenenciasDetenidoDelictivo.isChecked()){
                                //LLENAR PERTENENCIAS
                                QuintaValidacion();
                            } else if (rbNoPertenenciasDetenidoDelictivo.isChecked()){
                                QuintaValidacion();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI SE RECOLECTÓ ALGUNA PERTENENCIA DE LA PERSONA DETENIDA", Toast.LENGTH_SHORT).show();
                                septimodosLinear.requestFocus();
                            }

                        } else if(rbNoObjetoInspeccionDetenidoDelictivo.isChecked()){

                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI ENCONTRÓ ALGÚN OBJETO RELACIONADO CON LOS HECHOS", Toast.LENGTH_SHORT).show();
                            septimounoLinear.requestFocus();
                        }

                    } else if (rbSiInformeDerechoDetencionesDelictivo.isChecked()){
                        //IF VALIDACION FIRMA REALIZADA


                        if(rbSiObjetoInspeccionDetenidoDelictivo.isChecked()){
                            //HABILITAR ANEXO D
                            if(rbSiPertenenciasDetenidoDelictivo.isChecked()){
                                //LLENAR PERTENENCIAS
                                QuintaValidacion();
                            } else if (rbNoPertenenciasDetenidoDelictivo.isChecked()){
                                QuintaValidacion();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI SE RECOLECTÓ ALGUNA PERTENENCIA DE LA PERSONA DETENIDA", Toast.LENGTH_SHORT).show();
                                septimodosLinear.requestFocus();
                            }

                        } else if(rbNoObjetoInspeccionDetenidoDelictivo.isChecked()){

                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI ENCONTRÓ ALGÚN OBJETO RELACIONADO CON LOS HECHOS", Toast.LENGTH_SHORT).show();
                            septimounoLinear.requestFocus();
                        }

                        //CIERRE IF VALIDACION FIRMA REALIZADA
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI LE INFORMÓ SUS DERECHOS AL DETENIDO", Toast.LENGTH_SHORT).show();
                        septimoLinear.requestFocus();
                    }

                } else{
                    if(txtNumeroTelefonoA3Delictivo.getText().toString().length() == 10){
                        //VALIDACION INFORME DERECHOS
                        if(rbNoInformeDerechoDetencionesDelictivo.isChecked()){
                            if(rbSiObjetoInspeccionDetenidoDelictivo.isChecked()){
                                //HABILITAR ANEXO D
                                if(rbSiPertenenciasDetenidoDelictivo.isChecked()){
                                    //LLENAR PERTENENCIAS
                                    QuintaValidacion();
                                } else if (rbNoPertenenciasDetenidoDelictivo.isChecked()){
                                    QuintaValidacion();
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI SE RECOLECTÓ ALGUNA PERTENENCIA DE LA PERSONA DETENIDA", Toast.LENGTH_SHORT).show();
                                    septimodosLinear.requestFocus();
                                }

                            } else if(rbNoObjetoInspeccionDetenidoDelictivo.isChecked()){

                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI ENCONTRÓ ALGÚN OBJETO RELACIONADO CON LOS HECHOS", Toast.LENGTH_SHORT).show();
                                septimounoLinear.requestFocus();
                            }

                        } else if (rbSiInformeDerechoDetencionesDelictivo.isChecked()){
                            //IF VALIDACION FIRMA REALIZADA


                            if(rbSiObjetoInspeccionDetenidoDelictivo.isChecked()){
                                //HABILITAR ANEXO D
                                if(rbSiPertenenciasDetenidoDelictivo.isChecked()){
                                    //LLENAR PERTENENCIAS
                                    QuintaValidacion();
                                } else if (rbNoPertenenciasDetenidoDelictivo.isChecked()){
                                    QuintaValidacion();
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI SE RECOLECTÓ ALGUNA PERTENENCIA DE LA PERSONA DETENIDA", Toast.LENGTH_SHORT).show();
                                    septimodosLinear.requestFocus();
                                }

                            } else if(rbNoObjetoInspeccionDetenidoDelictivo.isChecked()){

                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI ENCONTRÓ ALGÚN OBJETO RELACIONADO CON LOS HECHOS", Toast.LENGTH_SHORT).show();
                                septimounoLinear.requestFocus();
                            }

                            //CIERRE IF VALIDACION FIRMA REALIZADA
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI LE INFORMÓ SUS DERECHOS AL DETENIDO", Toast.LENGTH_SHORT).show();
                            septimoLinear.requestFocus();
                        }

                    } else{
                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA CORRECTAMENTE EL NÚMERO TELEFÓNICO", Toast.LENGTH_SHORT).show();
                        lyTelFamDet.requestFocus();
                        txtNumeroTelefonoA3Delictivo.requestFocus();
                    }
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL NOMBRE DEL FAMILIAR DEL DETENIDO", Toast.LENGTH_SHORT).show();
                lyNomFamDet.requestFocus();
                txtNombresA3Delictivo.requestFocus();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL PRIMER APELLIDO DEL FAMILIAR DEL DETENIDO", Toast.LENGTH_SHORT).show();
            lyPrimerApFamDet.requestFocus();
            txtPrimerApellidoA3Delictivo.requestFocus();
        }


    }

    public void QuintaValidacion(){
        if(rbSiLugarDetencionDelictivo.isChecked()){
            //VALIDACION DIRECCION DETENCIÓN
            if(txtColoniaDetencion.getText().toString().length() >= 3){
                if(txtCalleDetencion.getText().toString().length() >= 3){
                    if(txtReferenciasdelLugarDetencion.getText().toString().length() >= 3){
                        SextaValidacion();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA AL MENOS UNA REFERENCIA DEL LUGAR DE LA DETENCIÓN", Toast.LENGTH_SHORT).show();
                        txtReferenciasdelLugarDetencion.requestFocus();
                        lyReferenciaLugarDet.requestFocus();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA CALLE O TRAMO CARRETERO DEL LUGAR DE LA INTERVENCIÓN", Toast.LENGTH_SHORT).show();
                    txtCalleDetencion.requestFocus();
                    lyCalleTramoDet.requestFocus();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA COLONIA O LOCALIDAD DEL LUGAR DE LA INTERVENCIÓN", Toast.LENGTH_SHORT).show();
                txtColoniaDetencion.requestFocus();
                lyColoniaDetencionDelictivo.requestFocus();
            }

        } else if(rbNoLugarDetencionDelictivo.isChecked()){
            //VALIDACION DIRECCION DETENCIÓN
            if(txtColoniaDetencion.getText().toString().length() >= 3){
                if(txtCalleDetencion.getText().toString().length() >= 3){
                    if(txtReferenciasdelLugarDetencion.getText().toString().length() >= 3){
                        SextaValidacion();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA AL MENOS UNA REFERENCIA DEL LUGAR DE LA DETENCIÓN", Toast.LENGTH_SHORT).show();
                        txtReferenciasdelLugarDetencion.requestFocus();
                        lyReferenciaLugarDet.requestFocus();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA CALLE O TRAMO CARRETERO DEL LUGAR DE LA INTERVENCIÓN", Toast.LENGTH_SHORT).show();
                    txtCalleDetencion.requestFocus();
                    lyCalleTramoDet.requestFocus();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA COLONIA O LOCALIDAD DEL LUGAR DE LA INTERVENCIÓN", Toast.LENGTH_SHORT).show();
                txtColoniaDetencion.requestFocus();
                lyColoniaDetencionDelictivo.requestFocus();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI EL LUGAR DE LA DETENCIÓN ES EL MISMO QUE EL DE LA INTERVENCIÓN", Toast.LENGTH_SHORT).show();
            septimotresLinear.requestFocus();
        }

    }

    public void SextaValidacion(){
        if(rbLugarTrasladoDetencionFiscaliaAgencia.isChecked()){
            if(txtObservacionesDetencion.getText().toString().length() >= 3){
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                insertDetencionesDelictivo();
            } else{
                Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA DESCRIPCIÓN DE LA RUTA DE TRASLADO DEL DETENIDO", Toast.LENGTH_SHORT).show();
                treintaicincoLinear.requestFocus();
            }

        } else if(rbLugarTrasladoDetencionHospital.isChecked()){
            if(txtObservacionesDetencion.getText().toString().length() >= 3){
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                insertDetencionesDelictivo();
            } else{
                Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA DESCRIPCIÓN DE LA RUTA DE TRASLADO DEL DETENIDO", Toast.LENGTH_SHORT).show();
                treintaicincoLinear.requestFocus();
            }

        } else if(rbLugarTrasladoDetencionOtraDependencia.isChecked()){
            if(txtCualLugarTraslado.getText().toString().length() >= 3){
                if(txtObservacionesDetencion.getText().toString().length() >= 3){
                    Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
                    insertDetencionesDelictivo();
                } else{
                    Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA DESCRIPCIÓN DE LA RUTA DE TRASLADO DEL DETENIDO", Toast.LENGTH_SHORT).show();
                    treintaicincoLinear.requestFocus();
                }

            } else {
                Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA DEPENDENCIA A LA QUE FUE TRASLADADO EL DETENIDO", Toast.LENGTH_SHORT).show();
                treintaicuatroLinear.requestFocus();
                txtCualLugarTraslado.requestFocus();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA A QUE DEPENDENCIA FUE TRASLADADO EL DETENIDO", Toast.LENGTH_SHORT).show();
            treintaicuatroLinear.requestFocus();
            treintaicuatrounoLinear.requestFocus();
        }
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

        rgLugarTrasladoDelictivo.clearCheck();
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

    public void getFirmaFromURL(){
        Picasso.get()
                .load(firmaURLServer)
                .into(imgFirmadelDetenidoDelictivoMiniatura);
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void EliminarDEtenido(String IdDetenido) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDDetenciones?folioInterno="+cargarIdHechoDelictivo+"&idInspeccionVehiculo="+IdDetenido)
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ELIMINAR DETENIDO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                    limpiarCampos();
                                    addFragment(new DescripcionVehiculoDelictivo());
                                }
                                else{
                                    Toast.makeText(getContext(), "PROBLEMA AL ELIMINAR", Toast.LENGTH_SHORT).show();
                                }
                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL ELIMINAR DETENIDO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    //Intercambia los Fragmentos
    private void addFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContenedorDelictivo, fragment)
                //.addToBackStack(null) //Se quita la pila de fragments. Botón atrás
                .commit();
    }
}