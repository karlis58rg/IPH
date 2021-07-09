package mx.ssp.iph.delictivo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import mx.ssp.iph.R;
import mx.ssp.iph.delictivo.viewModel.DetencionesDelictivoViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.Funciones;

import static android.app.Activity.RESULT_OK;

public class Detenciones_Delictivo extends Fragment {

    private DetencionesDelictivoViewModel mViewModel;
    Button btnGuardarPuestaDetencionesDelectivo;
    private Funciones funciones;
    private ImageView imgFirmaAutoridadAdministrativo,img_microfonoDescripcionDetenido;
    private TextView txtFechaDetenidoDelictivo,txthoraDetencionDelictivo,txtFechaNacimientoDetenido, txtPrimerApellidoDetenidoDelictivo, txtSegundoApellidoDetenidoDelictivo, txtNombresDetenidoDelictivo, txtApodoDetenidoDelictivo, txtNacionalidadEspecifiqueDetenido, txtEdadDetenidoDelictivo, txtCualGpoVulnerableDelictivo, txtEspecifiqueTipoDocumentoDelictivo, txtNumeroIdentificacionDelictivo, txtColoniaDetenido, txtCalleDetenido, txtNumeroExteriorDetenido, txtNumeroInteriorDetenido, txtCodigoPostalDetenido, txtReferenciasdelLugarDetenido, txtDescripciondelDetenido, txtCualPadecimiento, txtCualGrupoVulnerable, txtCualGrupoDelictivo, txtPrimerApellidoA3Delictivo, txtSegundoApellidoA3Delictivo, txtNombresA3Delictivo, txtNumeroTelefonoA3Delictivo, txtColoniaDetencion, txtCalleDetencion, txtNumeroExteriorDetencion, txtNumeroInteriorDetencion, txtCodigoPostalDetencion, txtReferenciasdelLugarDetencion, txtObservacionesDetencion, txtDetencionPrimerApellido, txtDetencionSegundoApellido, txtDetencionNombres, txtDetencionPrimerApellidoDos, txtDetencionSegundoApellidoDos, txtDetencionNombresDos;
    private CheckBox chNoAplicaAliasDetenidoDelictivo, chNoProporcionadoDelictivo, chLugarTrasladoDetencionFiscaliaAgencia, chLugarTrasladoDetencionHospital, chLugarTrasladoDetencionOtraDependencia;
    private RadioGroup rgDocumentoDelictivo, rgLesiones, rgPadecimiento, rgGrupoVulnerable, rgGrupoDelictivo, rgInformeDerechoDetencionesDelictivo, rgObjetoInspeccionDetenidoDelictivo, rgPertenenciasDetenidoDelictivo, rgLugarDetencionDelictivo;
    private RadioButton rbNoDocumentoDelictivo, rbSiDocumentoDelictivo, rbNoLesiones, rbSiLesiones, rbPadecimiento, rbSiPadecimiento, rbNoGrupoVulnerable, rbSiGrupoVulnerable, rbNoGrupoDelictivo, rbSiGrupoDelictivo, rbNoInformeDerechoDetencionesDelictivo, rbSiInformeDerechoDetencionesDelictivo, rbSiObjetoInspeccionDetenidoDelictivo, rbNoObjetoInspeccionDetenidoDelictivo, rbSiPertenenciasDetenidoDelictivo, rbNoPertenenciasDetenidoDelictivo, rbSiLugarDetencionDelictivo, rbNoLugarDetencionDelictivo;
    private static final  int REQ_CODE_SPEECH_INPUT=100;


    public static Detenciones_Delictivo newInstance() {
        return new Detenciones_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detenciones__delictivo_fragment, container, false);
        /***********************************************************************************/
        funciones = new Funciones();
        btnGuardarPuestaDetencionesDelectivo = view.findViewById(R.id.btnGuardarPuestaDetencionesDelectivo);
        imgFirmaAutoridadAdministrativo = view.findViewById(R.id.imgFirmaAutoridadAdministrativo);
        img_microfonoDescripcionDetenido = view.findViewById(R.id.img_microfonoDescripcionDetenido);

        txtFechaDetenidoDelictivo = view.findViewById(R.id.txtFechaDetenidoDelictivo);
        txthoraDetencionDelictivo = view.findViewById(R.id.txthoraDetencionDelictivo);
        txtFechaNacimientoDetenido = view.findViewById(R.id.txtFechaNacimientoDetenido);

        txtPrimerApellidoDetenidoDelictivo = view.findViewById(R.id.txtPrimerApellidoDetenidoDelictivo);
        txtPrimerApellidoDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtSegundoApellidoDetenidoDelictivo = view.findViewById(R.id.txtSegundoApellidoDetenidoDelictivo);
        txtSegundoApellidoDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtNombresDetenidoDelictivo = view.findViewById(R.id.txtNombresDetenidoDelictivo);
        txtNombresDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtApodoDetenidoDelictivo = view.findViewById(R.id.txtApodoDetenidoDelictivo);
        txtApodoDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtNacionalidadEspecifiqueDetenido = view.findViewById(R.id.txtNacionalidadEspecifiqueDetenido);
        txtNacionalidadEspecifiqueDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});

        txtEdadDetenidoDelictivo = view.findViewById(R.id.txtEdadDetenidoDelictivo);
        txtEdadDetenidoDelictivo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});

        txtCualGpoVulnerableDelictivo = view.findViewById(R.id.txtCualGpoVulnerableDelictivo);
        txtCualGpoVulnerableDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtEspecifiqueTipoDocumentoDelictivo = view.findViewById(R.id.txtEspecifiqueTipoDocumentoDelictivo);
        txtEspecifiqueTipoDocumentoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtNumeroIdentificacionDelictivo = view.findViewById(R.id.txtNumeroIdentificacionDelictivo);
        txtNumeroIdentificacionDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(25)});

        txtColoniaDetenido = view.findViewById(R.id.txtColoniaDetenido);
        txtColoniaDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtCalleDetenido = view.findViewById(R.id.txtCalleDetenido);
        txtCalleDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtNumeroExteriorDetenido = view.findViewById(R.id.txtNumeroExteriorDetenido);
        txtNumeroExteriorDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtNumeroInteriorDetenido = view.findViewById(R.id.txtNumeroInteriorDetenido);
        txtNumeroInteriorDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtCodigoPostalDetenido = view.findViewById(R.id.txtCodigoPostalDetenido);
        txtCodigoPostalDetenido.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});

        txtReferenciasdelLugarDetenido = view.findViewById(R.id.txtReferenciasdelLugarDetenido);
        txtReferenciasdelLugarDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});

        txtDescripciondelDetenido = view.findViewById(R.id.txtDescripciondelDetenido);
        txtDescripciondelDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});

        txtCualPadecimiento = view.findViewById(R.id.txtCualPadecimiento);
        txtCualPadecimiento.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtCualGrupoVulnerable = view.findViewById(R.id.txtCualGrupoVulnerable);
        txtCualGrupoVulnerable.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtCualGrupoDelictivo = view.findViewById(R.id.txtCualGrupoDelictivo);
        txtCualGrupoDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});

        txtPrimerApellidoA3Delictivo = view.findViewById(R.id.txtPrimerApellidoA3Delictivo);
        txtPrimerApellidoA3Delictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtSegundoApellidoA3Delictivo = view.findViewById(R.id.txtSegundoApellidoA3Delictivo);
        txtSegundoApellidoA3Delictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtNombresA3Delictivo = view.findViewById(R.id.txtNombresA3Delictivo);
        txtNombresA3Delictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtNumeroTelefonoA3Delictivo = view.findViewById(R.id.txtNumeroTelefonoA3Delictivo);
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

        txtDetencionPrimerApellido = view.findViewById(R.id.txtDetencionPrimerApellido);
        txtDetencionPrimerApellido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtDetencionSegundoApellido = view.findViewById(R.id.txtDetencionSegundoApellido);
        txtDetencionSegundoApellido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtDetencionNombres = view.findViewById(R.id.txtDetencionNombres);
        txtDetencionNombres.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtDetencionPrimerApellidoDos = view.findViewById(R.id.txtDetencionPrimerApellidoDos);
        txtDetencionPrimerApellidoDos.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtDetencionSegundoApellidoDos = view.findViewById(R.id.txtDetencionSegundoApellidoDos);
        txtDetencionSegundoApellidoDos.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtDetencionNombresDos = view.findViewById(R.id.txtDetencionNombresDos);
        txtDetencionNombresDos.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        rgDocumentoDelictivo = view.findViewById(R.id.rgDocumentoDelictivo);
        rgLesiones = view.findViewById(R.id.rgLesiones);
        rgPadecimiento = view.findViewById(R.id.rgPadecimiento);
        rgGrupoVulnerable = view.findViewById(R.id.rgGrupoVulnerable);
        rgGrupoDelictivo = view.findViewById(R.id.rgGrupoDelictivo);
        rgInformeDerechoDetencionesDelictivo = view.findViewById(R.id.rgInformeDerechoDetencionesDelictivo);
        rgObjetoInspeccionDetenidoDelictivo = view.findViewById(R.id.rgObjetoInspeccionDetenidoDelictivo);
        rgPertenenciasDetenidoDelictivo = view.findViewById(R.id.rgPertenenciasDetenidoDelictivo);
        rgLugarDetencionDelictivo = view.findViewById(R.id.rgLugarDetencionDelictivo);
        rbNoDocumentoDelictivo = view.findViewById(R.id.rbNoDocumentoDelictivo);
        rbSiDocumentoDelictivo = view.findViewById(R.id.rbSiDocumentoDelictivo);
        rbNoLesiones = view.findViewById(R.id.rbNoLesiones);
        rbSiLesiones = view.findViewById(R.id.rbSiLesiones);
        rbPadecimiento = view.findViewById(R.id.rbPadecimiento);
        rbSiPadecimiento = view.findViewById(R.id.rbSiPadecimiento);
        rbNoGrupoVulnerable = view.findViewById(R.id.rbNoGrupoVulnerable);
        rbSiGrupoVulnerable = view.findViewById(R.id.rbSiGrupoVulnerable);
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




        funciones.CambiarTituloSeccionesDelictivo("ANEXO A. DETENCIÓN(ES)",getContext(),getActivity());


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
        txtFechaNacimientoDetenido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaNacimientoDetenido,getContext(),getActivity());
            }
        });

        //***************** FIRMA **************************//
        imgFirmaAutoridadAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirma dialog = new ContenedorFirma(R.id.lblFirmadelDetenido,R.id.lblFirmadelDetenidoOculto);
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

        //***************** GUARDAR **************************//
        btnGuardarPuestaDetencionesDelectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
            }
        });


        /**********************************************************************************/
        return view;
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
                    String textoActual = txtDescripciondelDetenido.getText().toString();
                    txtDescripciondelDetenido.setText(textoActual+" " + result.get(0));
                }
                break;
            }
        }
    }
}