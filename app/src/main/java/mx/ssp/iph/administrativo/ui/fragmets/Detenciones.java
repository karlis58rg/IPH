package mx.ssp.iph.administrativo.ui.fragmets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import mx.ssp.iph.administrativo.model.ModeloDetenciones_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloRecibeDisposicion_Administrativo;
import mx.ssp.iph.administrativo.viewModel.DetencionesViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class Detenciones extends Fragment  {

    private DetencionesViewModel mViewModel;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    private TextView txtDescripciondelDetenido,lblFirmaOcultaDetenidoBase64;
    Integer aux1;
    ImageView img_microfonoDescripcionDetenido,imgFirmaDetencionesAutoridadAdministrativo;
    EditText txtFechaDetenido,txthoraDetencion,txtFechaNacimientoDetenido,txtPrimerApellidoDetenido,txtSegundoApellidoDetenido,txtNombresDetenido,txtApodoDetenido,txtEntidadDetenido,
            txtColoniaDetenido,txtCalleDetenido,txtNumeroExteriorDetenido,txtNumeroInteriorDetenido,txtCodigoPostalDetenido,txtReferenciasdelLugarDetenido,txtCualGrupoVulnerable,txtCualPadecimiento, txtNacionalidadEspecifiqueDetenido;
    CheckBox chNoAplicaAliasDetenido;
    Spinner spGeneroDetenido,txtNacionalidadDetenido,txtMunicipioDetenido,spLugarTrasladoPersonaDetenida;
    RadioGroup rgLesiones, rgPadecimiento, rgGrupoVulnerable;
    RadioButton rbNoLesiones, rbSiLesiones, rbPadecimiento, rbSiPadecimiento, rbNoGrupoVulnerable, rbSiGrupoVulnerable;
    ImageView btnGuardarDetencionesAdministrativo,btnEditarDetencionesAdministrativo,btnCancelarDetencionesAdministrativo,btnEliminarDetencionesAdministrativo,imgFirmadelDetenidoMiniatura;
    private Funciones funciones;
    SharedPreferences share;
    private ListView lvDetenidos_Administrativo;
    ArrayList<String> ListaNumDetenido,ListaNombreDetenido,ListaIdDetenido;
    ArrayList<JSONObject> ListaDetenidos;
    String firmaURLServer = "http://189.254.7.167/WebServiceIPH/Firma/SINFIRMA.jpg";
    int numberRandom,randomUrlImagen;
    private ViewGroup linearApodoDetenido, cuartoLinear, segundoLinear, catorceavoLinear, quinceavoLinear, dieciseisLinear, diecisietelinear, especificaNacionalidad;
    //Variable para almacenar el jsn detenido y deserealizarlo al darle clic a la lista
    String[] ArrayListaIPHAdministrativo;
    LinearLayout veinticincoBtnAgregar,veinticincoBtnEditar;
    int PosicionIPHSeleccionado= 0;
    TextView lblFirmadelDetenido;


    String cargarIdFaltaAdmin,cargarUsuario,descripcionLugarTraslado,descripcionMunicipio,descripcionNacionalidad,descripcionSexo,
            varLesiones = "NO",varPadecimiento = "NO",varGrupoVulnerable = "NO",varNoAlias;

    public static Detenciones newInstance() {
        return new Detenciones();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.detenciones_fragment, container, false);
        /********************************************************************************************/
        random();
        funciones = new Funciones();

        veinticincoBtnAgregar = (LinearLayout) view.findViewById(R.id.veinticincoBtnAgregar);
        veinticincoBtnEditar = (LinearLayout) view.findViewById(R.id.veinticincoBtnEditar);

        lvDetenidos_Administrativo = (ListView) view.findViewById(R.id.lvDetenidos_Administrativo);

        txtDescripciondelDetenido = (TextView)view.findViewById(R.id.txtDescripciondelDetenido);
        txtDescripciondelDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(8000)});

        img_microfonoDescripcionDetenido = (ImageView) view.findViewById(R.id.img_microfonoDescripcionDetenido);
        txtFechaDetenido = (EditText)view.findViewById(R.id.txtFechaDetenido);
        txthoraDetencion = (EditText)view.findViewById(R.id.txthoraDetencion);
        txtFechaNacimientoDetenido = (EditText) view.findViewById(R.id.txtFechaNacimientoDetenido);

        txtPrimerApellidoDetenido = view.findViewById(R.id.txtPrimerApellidoDetenido);
        txtPrimerApellidoDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});


        txtSegundoApellidoDetenido = view.findViewById(R.id.txtSegundoApellidoDetenido);
        txtSegundoApellidoDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtNombresDetenido = view.findViewById(R.id.txtNombresDetenido);
        txtNombresDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtApodoDetenido = view.findViewById(R.id.txtApodoDetenido);
        txtApodoDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        txtEntidadDetenido = view.findViewById(R.id.txtEntidadDetenido);

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
        txtCualGrupoVulnerable = view.findViewById(R.id.txtCualGrupoVulnerable);
        txtCualGrupoVulnerable.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});
        txtCualPadecimiento = view.findViewById(R.id.txtCualPadecimiento);
        txtCualPadecimiento.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(250)});
        txtNacionalidadEspecifiqueDetenido = view.findViewById(R.id.txtNacionalidadEspecifiqueDetenido);
        txtNacionalidadEspecifiqueDetenido.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(40)});
        chNoAplicaAliasDetenido = view.findViewById(R.id.chNoAplicaAliasDetenido);
        spGeneroDetenido = view.findViewById(R.id.spGeneroDetenido);
        txtNacionalidadDetenido = view.findViewById(R.id.txtNacionalidadDetenido);


        txtMunicipioDetenido = view.findViewById(R.id.txtMunicipioDetenido);
        spLugarTrasladoPersonaDetenida = view.findViewById(R.id.spLugarTrasladoPersonaDetenida);
        rgLesiones = view.findViewById(R.id.rgLesiones);
        rgPadecimiento = view.findViewById(R.id.rgPadecimiento);
        rgGrupoVulnerable = view.findViewById(R.id.rgGrupoVulnerable);
        rbNoLesiones = view.findViewById(R.id.rbNoLesiones);
        rbSiLesiones = view.findViewById(R.id.rbSiLesiones);
        rbPadecimiento = view.findViewById(R.id.rbPadecimiento);
        rbSiPadecimiento = view.findViewById(R.id.rbSiPadecimiento);
        rbNoGrupoVulnerable = view.findViewById(R.id.rbNoGrupoVulnerable);
        rbSiGrupoVulnerable = view.findViewById(R.id.rbSiGrupoVulnerable);

        linearApodoDetenido = view.findViewById(R.id.linearApodoDetenido);
        cuartoLinear = view.findViewById(R.id.cuartoLinear);
        segundoLinear = view.findViewById(R.id.segundoLinear);
        catorceavoLinear  = view.findViewById(R.id.catorceavoLinear);
        quinceavoLinear = view.findViewById(R.id.quinceavoLinear);
        dieciseisLinear = view.findViewById(R.id.dieciseisLinear);
        diecisietelinear  = view.findViewById(R.id.diecisietelinear);
        especificaNacionalidad  = view.findViewById(R.id.especificaNacionalidad);

        imgFirmadelDetenidoMiniatura = view.findViewById(R.id.imgFirmadelDetenidoMiniatura);
        lblFirmaOcultaDetenidoBase64 = view.findViewById(R.id.lblFirmaOcultaDetenidoBase64);

        btnGuardarDetencionesAdministrativo = view.findViewById(R.id.btnGuardarDetencionesAdministrativo);
        btnEditarDetencionesAdministrativo = view.findViewById(R.id.btnEditarDetencionesAdministrativo);
        btnCancelarDetencionesAdministrativo = view.findViewById(R.id.btnCancelarDetencionesAdministrativo);
        btnEliminarDetencionesAdministrativo  = view.findViewById(R.id.btnEliminarDetencionesAdministrativo);

        lblFirmadelDetenido = view.findViewById(R.id.lblFirmadelDetenido);
        ListLugarTraslado();
        ListMunicipios();
        ListNacionalidad();
        ListSexo();

        txtCualPadecimiento.setEnabled(false);
        txtCualGrupoVulnerable.setEnabled(false);
        txtEntidadDetenido.setEnabled(false);

        //Cambia el título de acuerdo a la sección seleccionada
        funciones.CambiarTituloSecciones("ANEXO A: DETENCIÓN (ES)",getContext(),getActivity());

        txtNacionalidadDetenido.setSelection(funciones.getIndexSpiner(txtNacionalidadDetenido, "MEXICANA"));

        //SLEECCIONA "OTRA NACIONALIDAD" Y HABILITA CAMPO DE TEXTO
        txtNacionalidadDetenido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                Object item = parent.getItemIdAtPosition(pos);

                int i = Integer.parseInt(item.toString()) + 1;

                aux1 = i;

                if(i == 34){
                    txtNacionalidadEspecifiqueDetenido.setEnabled(true);
                    txtNacionalidadEspecifiqueDetenido.setText("");
                }else{
                    txtNacionalidadEspecifiqueDetenido.setEnabled(false);
                    txtNacionalidadEspecifiqueDetenido.setText("");
                }



                //Toast.makeText(getContext(), "" + i, Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



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
                //Toast.makeText(getContext(),"CLick",Toast.LENGTH_SHORT).show();
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


        //HABILITAR - DESHABILITAR EDITTEXT ALIAS O APODO
        chNoAplicaAliasDetenido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {

                if(chselect == true){
                    txtApodoDetenido.setEnabled(false);
                    varNoAlias = "NA";
                    txtApodoDetenido.setText("");
                } else if(chselect == false) {
                    txtApodoDetenido.setEnabled(true);
                    varNoAlias = "";
                    txtApodoDetenido.setText("");
                }

            }
        });

        //Firma del Detenido
        imgFirmaDetencionesAutoridadAdministrativo = (ImageView) view.findViewById(R.id.imgFirmaDetencionesAutoridadAdministrativo);

        //Imagen que funciona para activar la grabación de voz
        img_microfonoDescripcionDetenido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_microfonoDescripcionDetenido.setImageResource(R.drawable.ic_micro_press);
                iniciarEntradadeVoz();
            }
        });

        txtDescripciondelDetenido.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    return true;
                }
                return false;
            }
        });

        //Imagen que funciona para activar la firma
        imgFirmaDetencionesAutoridadAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirma dialog = new ContenedorFirma(R.id.lblFirmadelDetenido,R.id.lblFirmaOcultaDetenidoBase64,R.id.imgFirmadelDetenidoMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });

        btnGuardarDetencionesAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtFechaDetenido.getText().toString().length() >= 3 && txthoraDetencion.getText().toString().length() > 3){
                    if(txtPrimerApellidoDetenido.getText().toString().length() >= 3) {
                        if (txtNombresDetenido.getText().toString().length() >= 3) {
                            if (txtApodoDetenido.getText().toString().length() >= 3 || chNoAplicaAliasDetenido.isChecked()){
                                if(aux1 == 34){
                                    if(txtNacionalidadEspecifiqueDetenido.getText().toString().length() >= 3){

                                        if (txtDescripciondelDetenido.getText().toString().length() >= 3) {
                                            if (rbNoLesiones.isChecked() || rbSiLesiones.isChecked()) {
                                                if (rbPadecimiento.isChecked()) {

                                                    if (rbNoGrupoVulnerable.isChecked()) {
                                                        insertDetenciones();
                                                    } else if (rbSiGrupoVulnerable.isChecked()) {
                                                        if (txtCualGrupoVulnerable.getText().toString().length() >= 3) {
                                                            insertDetenciones();
                                                        } else {
                                                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO VULNERABLE AL QUE PERTENECE", Toast.LENGTH_SHORT).show();
                                                            diecisietelinear.requestFocus();
                                                            txtCualGrupoVulnerable.requestFocus();
                                                        }
                                                    } else {
                                                        Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI PERTENECE A ALGÚN GRUPO VULNERABLE", Toast.LENGTH_SHORT).show();
                                                        diecisietelinear.requestFocus();
                                                    }

                                                } else if (rbSiPadecimiento.isChecked()) {
                                                    if (txtCualPadecimiento.getText().toString().length() >= 3) {

                                                        if (rbNoGrupoVulnerable.isChecked()) {
                                                            insertDetenciones();
                                                        } else if (rbSiGrupoVulnerable.isChecked()) {
                                                            if (txtCualGrupoVulnerable.getText().toString().length() >= 3) {
                                                                insertDetenciones();
                                                            } else {
                                                                Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO VULNERABLE AL QUE PERTENECE EL DETENIDO", Toast.LENGTH_SHORT).show();
                                                                diecisietelinear.requestFocus();
                                                                txtCualGrupoVulnerable.requestFocus();
                                                            }
                                                        } else {
                                                            Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI PERTENECE A ALGÚN GRUPO VULNERABLE", Toast.LENGTH_SHORT).show();
                                                            diecisietelinear.requestFocus();
                                                        }


                                                    } else {
                                                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL PADECIMIENTO DEL DETENIDO", Toast.LENGTH_SHORT).show();
                                                        dieciseisLinear.requestFocus();
                                                        txtCualPadecimiento.requestFocus();
                                                    }
                                                } else {
                                                    Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI TIENE ALGÚN PADECIMIENTO", Toast.LENGTH_SHORT).show();
                                                    dieciseisLinear.requestFocus();
                                                }

                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI PRESENTA LESIONES VISIBLES", Toast.LENGTH_SHORT).show();
                                                quinceavoLinear.requestFocus();
                                            }

                                        } else {
                                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA ALGUNA DESCRIPCIÓN DEL DETENIDO", Toast.LENGTH_SHORT).show();
                                            catorceavoLinear.requestFocus();
                                        }

                                    } else{
                                        Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA LA NACIONALIDAD", Toast.LENGTH_SHORT).show();
                                        especificaNacionalidad.requestFocus();
                                        txtNacionalidadEspecifiqueDetenido.requestFocus();
                                        }


                                } else{

                                        if (txtDescripciondelDetenido.getText().toString().length() >= 3) {
                                            if (rbNoLesiones.isChecked() || rbSiLesiones.isChecked()) {
                                                if (rbPadecimiento.isChecked()) {

                                                    if (rbNoGrupoVulnerable.isChecked()) {
                                                        insertDetenciones();
                                                    } else if (rbSiGrupoVulnerable.isChecked()) {
                                                        if (txtCualGrupoVulnerable.getText().toString().length() >= 3) {
                                                            insertDetenciones();
                                                        } else {
                                                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO VULNERABLE AL QUE PERTENECE", Toast.LENGTH_SHORT).show();
                                                            diecisietelinear.requestFocus();
                                                            txtCualGrupoVulnerable.requestFocus();
                                                        }
                                                    } else {
                                                        Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI PERTENECE A ALGÚN GRUPO VULNERABLE", Toast.LENGTH_SHORT).show();
                                                        diecisietelinear.requestFocus();
                                                    }

                                                } else if (rbSiPadecimiento.isChecked()) {
                                                    if (txtCualPadecimiento.getText().toString().length() >= 3) {

                                                        if (rbNoGrupoVulnerable.isChecked()) {
                                                            insertDetenciones();
                                                        } else if (rbSiGrupoVulnerable.isChecked()) {
                                                            if (txtCualGrupoVulnerable.getText().toString().length() >= 3) {
                                                                insertDetenciones();
                                                            } else {
                                                                Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO VULNERABLE AL QUE PERTENECE EL DETENIDO", Toast.LENGTH_SHORT).show();
                                                                diecisietelinear.requestFocus();
                                                                txtCualGrupoVulnerable.requestFocus();
                                                            }
                                                        } else {
                                                            Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI PERTENECE A ALGÚN GRUPO VULNERABLE", Toast.LENGTH_SHORT).show();
                                                            diecisietelinear.requestFocus();
                                                        }


                                                    } else {
                                                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL PADECIMIENTO DEL DETENIDO", Toast.LENGTH_SHORT).show();
                                                        dieciseisLinear.requestFocus();
                                                        txtCualPadecimiento.requestFocus();
                                                    }
                                                } else {
                                                    Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI TIENE ALGÚN PADECIMIENTO", Toast.LENGTH_SHORT).show();
                                                    dieciseisLinear.requestFocus();
                                                }

                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI PRESENTA LESIONES VISIBLES", Toast.LENGTH_SHORT).show();
                                                quinceavoLinear.requestFocus();
                                            }

                                        } else {
                                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA ALGUNA DESCRIPCIÓN DEL DETENIDO", Toast.LENGTH_SHORT).show();
                                            catorceavoLinear.requestFocus();
                                        }

                                    }

                            } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI TIENE ALGÚN APODO", Toast.LENGTH_SHORT).show();
                                    linearApodoDetenido.requestFocus();
                                    }

                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA AL MENOS UN NOMBRE DEL DETENIDO", Toast.LENGTH_SHORT).show();
                            cuartoLinear.requestFocus();
                            txtNombresDetenido.requestFocus();
                            }

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA AL MENOS EL PRIMER APELLIDO DEL DETENIDO", Toast.LENGTH_SHORT).show();
                        cuartoLinear.requestFocus();
                        txtPrimerApellidoDetenido.requestFocus();
                        }

                } else{
                    Toast.makeText(getActivity().getApplicationContext(),"NO SE PUEDE ALMACENAR INFORMACIÓN DE DETENCIÓN SIN FECHA Y HORA DE REGISTRO",Toast.LENGTH_SHORT).show();
                    segundoLinear.requestFocus();
                    }
            }
        });

        btnEditarDetencionesAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtFechaDetenido.getText().toString().length() >= 3 && txthoraDetencion.getText().toString().length() > 3){
                    if(txtPrimerApellidoDetenido.getText().toString().length() >= 3) {
                        if (txtNombresDetenido.getText().toString().length() >= 3) {
                            if (txtApodoDetenido.getText().toString().length() >= 3 || chNoAplicaAliasDetenido.isChecked()){
                                if(aux1 == 34){
                                    if(txtNacionalidadEspecifiqueDetenido.getText().toString().length() >= 3){

                                        if (txtDescripciondelDetenido.getText().toString().length() >= 3) {
                                            if (rbNoLesiones.isChecked() || rbSiLesiones.isChecked()) {
                                                if (rbPadecimiento.isChecked()) {

                                                    if (rbNoGrupoVulnerable.isChecked()) {
                                                        insertDetenciones();
                                                    } else if (rbSiGrupoVulnerable.isChecked()) {
                                                        if (txtCualGrupoVulnerable.getText().toString().length() >= 3) {
                                                            insertDetenciones();
                                                        } else {
                                                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO VULNERABLE AL QUE PERTENECE", Toast.LENGTH_SHORT).show();
                                                            diecisietelinear.requestFocus();
                                                            txtCualGrupoVulnerable.requestFocus();
                                                        }
                                                    } else {
                                                        Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI PERTENECE A ALGÚN GRUPO VULNERABLE", Toast.LENGTH_SHORT).show();
                                                        diecisietelinear.requestFocus();
                                                    }

                                                } else if (rbSiPadecimiento.isChecked()) {
                                                    if (txtCualPadecimiento.getText().toString().length() >= 3) {

                                                        if (rbNoGrupoVulnerable.isChecked()) {
                                                            insertDetenciones();
                                                        } else if (rbSiGrupoVulnerable.isChecked()) {
                                                            if (txtCualGrupoVulnerable.getText().toString().length() >= 3) {
                                                                insertDetenciones();
                                                            } else {
                                                                Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO VULNERABLE AL QUE PERTENECE EL DETENIDO", Toast.LENGTH_SHORT).show();
                                                                diecisietelinear.requestFocus();
                                                                txtCualGrupoVulnerable.requestFocus();
                                                            }
                                                        } else {
                                                            Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI PERTENECE A ALGÚN GRUPO VULNERABLE", Toast.LENGTH_SHORT).show();
                                                            diecisietelinear.requestFocus();
                                                        }


                                                    } else {
                                                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL PADECIMIENTO DEL DETENIDO", Toast.LENGTH_SHORT).show();
                                                        dieciseisLinear.requestFocus();
                                                        txtCualPadecimiento.requestFocus();
                                                    }
                                                } else {
                                                    Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI TIENE ALGÚN PADECIMIENTO", Toast.LENGTH_SHORT).show();
                                                    dieciseisLinear.requestFocus();
                                                }

                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI PRESENTA LESIONES VISIBLES", Toast.LENGTH_SHORT).show();
                                                quinceavoLinear.requestFocus();
                                            }

                                        } else {
                                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA ALGUNA DESCRIPCIÓN DEL DETENIDO", Toast.LENGTH_SHORT).show();
                                            catorceavoLinear.requestFocus();
                                        }

                                    } else{
                                        Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA LA NACIONALIDAD", Toast.LENGTH_SHORT).show();
                                        especificaNacionalidad.requestFocus();
                                        txtNacionalidadEspecifiqueDetenido.requestFocus();
                                    }


                                } else{

                                    if (txtDescripciondelDetenido.getText().toString().length() >= 3) {
                                        if (rbNoLesiones.isChecked() || rbSiLesiones.isChecked()) {
                                            if (rbPadecimiento.isChecked()) {

                                                if (rbNoGrupoVulnerable.isChecked()) {
                                                    insertDetenciones();
                                                } else if (rbSiGrupoVulnerable.isChecked()) {
                                                    if (txtCualGrupoVulnerable.getText().toString().length() >= 3) {
                                                        insertDetenciones();
                                                    } else {
                                                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO VULNERABLE AL QUE PERTENECE", Toast.LENGTH_SHORT).show();
                                                        diecisietelinear.requestFocus();
                                                        txtCualGrupoVulnerable.requestFocus();
                                                    }
                                                } else {
                                                    Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI PERTENECE A ALGÚN GRUPO VULNERABLE", Toast.LENGTH_SHORT).show();
                                                    diecisietelinear.requestFocus();
                                                }

                                            } else if (rbSiPadecimiento.isChecked()) {
                                                if (txtCualPadecimiento.getText().toString().length() >= 3) {

                                                    if (rbNoGrupoVulnerable.isChecked()) {
                                                        insertDetenciones();
                                                    } else if (rbSiGrupoVulnerable.isChecked()) {
                                                        if (txtCualGrupoVulnerable.getText().toString().length() >= 3) {
                                                            insertDetenciones();
                                                        } else {
                                                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL GRUPO VULNERABLE AL QUE PERTENECE EL DETENIDO", Toast.LENGTH_SHORT).show();
                                                            diecisietelinear.requestFocus();
                                                            txtCualGrupoVulnerable.requestFocus();
                                                        }
                                                    } else {
                                                        Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI PERTENECE A ALGÚN GRUPO VULNERABLE", Toast.LENGTH_SHORT).show();
                                                        diecisietelinear.requestFocus();
                                                    }


                                                } else {
                                                    Toast.makeText(getActivity().getApplicationContext(), "INGRESA EL PADECIMIENTO DEL DETENIDO", Toast.LENGTH_SHORT).show();
                                                    dieciseisLinear.requestFocus();
                                                    txtCualPadecimiento.requestFocus();
                                                }
                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI TIENE ALGÚN PADECIMIENTO", Toast.LENGTH_SHORT).show();
                                                dieciseisLinear.requestFocus();
                                            }

                                        } else {
                                            Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI PRESENTA LESIONES VISIBLES", Toast.LENGTH_SHORT).show();
                                            quinceavoLinear.requestFocus();
                                        }

                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA ALGUNA DESCRIPCIÓN DEL DETENIDO", Toast.LENGTH_SHORT).show();
                                        catorceavoLinear.requestFocus();
                                    }

                                }

                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA SI TIENE ALGÚN APODO", Toast.LENGTH_SHORT).show();
                                linearApodoDetenido.requestFocus();
                            }

                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "INGRESA AL MENOS UN NOMBRE DEL DETENIDO", Toast.LENGTH_SHORT).show();
                            cuartoLinear.requestFocus();
                            txtNombresDetenido.requestFocus();
                        }

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "INGRESA AL MENOS EL PRIMER APELLIDO DEL DETENIDO", Toast.LENGTH_SHORT).show();
                        cuartoLinear.requestFocus();
                        txtPrimerApellidoDetenido.requestFocus();
                    }

                } else{
                    Toast.makeText(getActivity().getApplicationContext(),"NO SE PUEDE ALMACENAR INFORMACIÓN DE DETENCIÓN SIN FECHA Y HORA DE REGISTRO",Toast.LENGTH_SHORT).show();
                    segundoLinear.requestFocus();
                }
            }
        });

        btnEliminarDetencionesAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Alert para eliminar el detenido

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext()).
                                setMessage("¿DESEA ELIMINAR EL DETENIDO "+ ListaNombreDetenido.get(PosicionIPHSeleccionado) + "?" ).
                                setPositiveButton( "ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //CONSUME WEB SERVICE PARA ELIMINAR DB
                                        String IdDetenido = ListaIdDetenido.get(PosicionIPHSeleccionado);
                                        EliminarDetenido(IdDetenido);
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

        btnCancelarDetencionesAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Limpia los campos
                txtFechaDetenido.setText("");
                txthoraDetencion.setText("");
                txtFechaNacimientoDetenido.setText("");
                txtPrimerApellidoDetenido.setText("");
                txtSegundoApellidoDetenido.setText("");
                txtNombresDetenido.setText("");
                txtApodoDetenido.setText("");
                txtColoniaDetenido.setText("");
                txtCalleDetenido.setText("");
                txtNumeroExteriorDetenido.setText("");
                txtNumeroInteriorDetenido.setText("");
                txtCodigoPostalDetenido.setText("");
                txtReferenciasdelLugarDetenido.setText("");
                txtCualGrupoVulnerable.setText("");
                txtCualPadecimiento.setText("");
                txtDescripciondelDetenido.setText("");

                chNoAplicaAliasDetenido.setChecked(false);
                rbNoLesiones.setChecked(true);
                rbNoGrupoVulnerable.setChecked(true);
                rbPadecimiento.setChecked(true);
                txtCualPadecimiento.setText("");
                txtCualGrupoVulnerable.setText("");

                //Firma
                lblFirmaOcultaDetenidoBase64.setText("");
                lblFirmadelDetenido.setText("");


                txtNacionalidadDetenido.setSelection(funciones.getIndexSpiner(txtNacionalidadDetenido, ("Mexicano")));
                spLugarTrasladoPersonaDetenida.setSelection(funciones.getIndexSpiner(spLugarTrasladoPersonaDetenida, ("FISCALIA GENERAL DEL ESTADO")));
                spGeneroDetenido.setSelection(funciones.getIndexSpiner(spGeneroDetenido, ("MASCULINO")));


                //addFragment(new Detenciones());

                //Oculto el botón de Editar y Coloco 2 botones.
                veinticincoBtnEditar.setVisibility(View.GONE);
                veinticincoBtnAgregar.setVisibility(View.VISIBLE);

            }
        });

        rgLesiones.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiLesiones) {
                    varLesiones = "SI";
                } else if (checkedId == R.id.rbNoLesiones) {
                    varLesiones = "NO";
                }

            }
        });

        rgPadecimiento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiPadecimiento) {
                    txtCualPadecimiento.setEnabled(true);
                    varPadecimiento = "SI";
                    txtCualPadecimiento.setText("");
                } else if (checkedId == R.id.rbPadecimiento) {
                    varPadecimiento = "NO";
                    txtCualPadecimiento.setText("NA");
                    txtCualPadecimiento.setEnabled(false);
                }

            }
        });
        rgGrupoVulnerable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiGrupoVulnerable) {
                    txtCualGrupoVulnerable.setEnabled(true);
                    varGrupoVulnerable = "SI";
                    txtCualGrupoVulnerable.setText("");
                } else if (checkedId == R.id.rbNoGrupoVulnerable) {
                    varGrupoVulnerable = "NO";
                    txtCualGrupoVulnerable.setText("NA");
                    txtCualGrupoVulnerable.setEnabled(false);
                }

            }
        });
        /*********************************************************************************************************/
        //Clic a la lista
        lvDetenidos_Administrativo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


//f
                try {
                    String Json = ArrayListaIPHAdministrativo[position];
                    JSONObject jsonjObject = new JSONObject(Json + "}");

                    PosicionIPHSeleccionado= position;

                    //funciones.mensajeAlertDialog(Fecha,"Aceptar","Alerta",getContext());
                    //Deserealizar y colocar los valores en los campos.

                    txtFechaDetenido.setText(((jsonjObject.getString("Fecha")).equals("null")?"":jsonjObject.getString("Fecha")).replace("-","/").substring(0,10));
                    txthoraDetencion.setText(((jsonjObject.getString("Hora")).equals("null")?"":jsonjObject.getString("Hora")));
                    txtPrimerApellidoDetenido.setText((jsonjObject.getString("APDetenido")).equals("null")?"":jsonjObject.getString("APDetenido"));
                    txtSegundoApellidoDetenido.setText((jsonjObject.getString("AMDetenido")).equals("null")?"":jsonjObject.getString("AMDetenido"));
                    txtNombresDetenido.setText((jsonjObject.getString("NomDetenido")).equals("null")?"":jsonjObject.getString("NomDetenido"));

                    txtApodoDetenido.setText((jsonjObject.getString("ApodoAlias")).equals("null")?"":jsonjObject.getString("ApodoAlias"));
                    if(txtApodoDetenido.getText().toString().equals("")){chNoAplicaAliasDetenido.setChecked(true);}

                    spGeneroDetenido.setSelection(funciones.getIndexSpiner(spGeneroDetenido, (jsonjObject.getString("Sexo"))));
                    //Revisar cuando es otra
                    txtNacionalidadDetenido.setSelection(funciones.getIndexSpiner(txtNacionalidadDetenido, (jsonjObject.getString("Nacionalidad"))));
                    if (jsonjObject.getString("Nacionalidad").equals("Otra")){txtNacionalidadEspecifiqueDetenido.setText(jsonjObject.getString("Nacionalidad"));}

                    txtFechaNacimientoDetenido.setText(((jsonjObject.getString("FechaNacimiento")).equals("null")?"":jsonjObject.getString("FechaNacimiento")).replace("-","/").substring(0,10));

                    //Revisar Firma

                    //DIRECCIÓN
                    txtEntidadDetenido.setText("GUERRERO");
                    txtMunicipioDetenido.setSelection(funciones.getIndexSpiner(txtMunicipioDetenido, (jsonjObject.getString("Municipio"))));
                    txtColoniaDetenido.setText(((jsonjObject.getString("ColoniaLocalidad")).equals("null")?"":jsonjObject.getString("ColoniaLocalidad")));
                    txtCalleDetenido.setText(((jsonjObject.getString("CalleTramo")).equals("null")?"":jsonjObject.getString("CalleTramo")));
                    txtNumeroExteriorDetenido.setText(((jsonjObject.getString("NoExterior")).equals("null")?"":jsonjObject.getString("NoExterior")));
                    txtNumeroInteriorDetenido.setText(((jsonjObject.getString("NoInterior")).equals("null")?"":jsonjObject.getString("NoInterior")));
                    txtCodigoPostalDetenido.setText(((jsonjObject.getString("Cp")).equals("null")?"":jsonjObject.getString("Cp")));
                    txtReferenciasdelLugarDetenido.setText(((jsonjObject.getString("Referencia")).equals("null")?"":jsonjObject.getString("Referencia")));
                    txtDescripciondelDetenido.setText(((jsonjObject.getString("DescripcionDetenido")).equals("null")?"":jsonjObject.getString("DescripcionDetenido")));

                    //Preguntas
                    if ((jsonjObject.getString("Lesiones")).equals("SI")){rbSiLesiones.setChecked(true);}
                    else{rbNoLesiones.setChecked(true);}

                    if ((jsonjObject.getString("Padecimientos")).equals("SI")){rbSiPadecimiento.setChecked(true); txtCualPadecimiento.setText(((jsonjObject.getString("DescPadecimientos")).equals("null")?"":jsonjObject.getString("DescPadecimientos")));}
                    else{rbPadecimiento.setChecked(true);}

                    if ((jsonjObject.getString("GrupoVulnerable")).equals("SI")){rbSiGrupoVulnerable.setChecked(true); txtCualGrupoVulnerable.setText(((jsonjObject.getString("DescGrupoVulnerable")).equals("null")?"":jsonjObject.getString("DescGrupoVulnerable")));}
                    else{rbNoGrupoVulnerable.setChecked(true);}

                    firmaURLServer=((jsonjObject.getString("UrlFirmaDetenido")).equals("SIN INFORMACION")?"http://189.254.7.167/WebServiceIPH/Firma/SINFIRMA.jpg":jsonjObject.getString("UrlFirmaDetenido"));
                    getFirmaFromURL();

                    //Autoridad
                    spLugarTrasladoPersonaDetenida.setSelection(funciones.getIndexSpiner(spLugarTrasladoPersonaDetenida, (jsonjObject.getString("LugarTraslado"))));

                    //Oculto el botón de Guardar y Coloco 2 botones.
                    veinticincoBtnEditar.setVisibility(View.VISIBLE);
                    veinticincoBtnAgregar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                    Log.i("DETENCIONES", e.toString());
                }




            }
        });

        return view;
    }

    public void getFirmaFromURL(){
        Picasso.get()
                .load(firmaURLServer)
                .into(imgFirmadelDetenidoMiniatura);
    }

    //VALIDAR CAMPOS VACIOS O MENOR A TRES CARACTERES
    public boolean validarEditText(EditText editText){
        if(editText.getText().toString().isEmpty()){
            Toast.makeText(getActivity().getApplicationContext(),"EL CAMPO" + editText.getId() + "ES OBLIGATORIO",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(editText.getText().length() < 3) {
            Toast.makeText(getActivity().getApplicationContext(),"EL CAMPO" + editText + "NO DEBE SER MENOR A TRES CARACTERES",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
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
        img_microfonoDescripcionDetenido.setImageResource(R.drawable.ic_micro);
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
                                    ArrayListaIPHAdministrativo = ArrayIPHAdministrativo;

                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordeDetenido=0;
                                    while(contadordeDetenido < ArrayIPHAdministrativo.length){
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayIPHAdministrativo[contadordeDetenido] + "}");

                                            //ListaDetenidos.add(jsonjObject);

                                           // ListaIdentificadorDetenido.add(Integer.toString(jsonjObject.getInt("IdDetenido")));

                                            ListaIdDetenido.add(jsonjObject.getString("IdDetenido"));
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
                                lvDetenidos_Administrativo.setAdapter(adapter);
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
            lblNumDetencion.setText("PERSONA DETENIDA:");
            lblNombreCompleto.setText(ListaNombreDetenido.get(position));

            return row;
        }
    }

    //********************************** INSERTA IMAGEN AL SERVIDOR ***********************************//
    public void insertImagen() {
        String cadena = lblFirmaOcultaDetenidoBase64.getText().toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Description", cargarIdFaltaAdmin+randomUrlImagen+".jpg")
                .add("ImageData", cadena)
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/MultimediaFirmaDetenidos/")
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
                    Detenciones.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("EL DATO DE LA IMAGEN SE ENVIO CORRECTAMENTE");
                            Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//
    private void insertDetenciones() {
        DataHelper dataHelper = new DataHelper(getContext());

        descripcionLugarTraslado = (String) spLugarTrasladoPersonaDetenida.getSelectedItem();
        int idDesLugarTraslado = dataHelper.getIdLugarTraslado(descripcionLugarTraslado);
        String idLugarTraslado = String.valueOf(idDesLugarTraslado);

        descripcionMunicipio = (String) txtMunicipioDetenido.getSelectedItem();
        String idDescMunicipio = dataHelper.getIdMunicipio(descripcionMunicipio);
        String idMunicipio = String.valueOf(idDescMunicipio);

        if(idMunicipio.length() == 1){
            idMunicipio = "00"+idMunicipio;
        }else if(idMunicipio.length() == 2){
            idMunicipio = "0"+idMunicipio;
        }

        descripcionNacionalidad = (String) txtNacionalidadDetenido.getSelectedItem();
        int idDescNacionalidad = dataHelper.getIdNacionalidad(descripcionNacionalidad);
        String idNacionalidad = String.valueOf(idDescNacionalidad);

        descripcionSexo = (String) spGeneroDetenido.getSelectedItem();
        int idDescSexo = dataHelper.getIdSexo(descripcionSexo);
        String idSexo = String.valueOf(idDescSexo);

        if(chNoAplicaAliasDetenido.isChecked()){
            txtApodoDetenido.setText("NA");
        }

        if(txtNumeroExteriorDetenido.getText().toString().isEmpty()){
            txtNumeroExteriorDetenido.setText("SN");
        }
        if(txtNumeroInteriorDetenido.getText().toString().isEmpty()){
            txtNumeroInteriorDetenido.setText("NA");
        }

        String urlImagen = "http://189.254.7.167/WebServiceIPH/FirmaDetenidos/"+cargarIdFaltaAdmin+randomUrlImagen+".jpg";

        ModeloDetenciones_Administrativo modeloDetenciones = new ModeloDetenciones_Administrativo
                (cargarIdFaltaAdmin, "1",
                        txtFechaDetenido.getText().toString(), txthoraDetencion.getText().toString(),
                        txtPrimerApellidoDetenido.getText().toString(), txtSegundoApellidoDetenido.getText().toString(),
                        txtNombresDetenido.getText().toString(),txtApodoDetenido.getText().toString(), txtDescripciondelDetenido.getText().toString(),
                        idNacionalidad, idSexo, txtFechaNacimientoDetenido.getText().toString(), urlImagen,"12",idMunicipio,
                        txtColoniaDetenido.getText().toString(), txtCalleDetenido.getText().toString(),
                        txtNumeroExteriorDetenido.getText().toString(), txtNumeroInteriorDetenido.getText().toString(),
                        txtCodigoPostalDetenido.getText().toString(), txtReferenciasdelLugarDetenido.getText().toString(),
                        varLesiones, varPadecimiento, txtCualPadecimiento.getText().toString(),
                        varGrupoVulnerable, txtCualGrupoVulnerable.getText().toString(), idLugarTraslado, cargarUsuario);


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin", modeloDetenciones.getIdFaltaAdmin())
                .add("NumDetencion", modeloDetenciones.getNumDetencion())
                .add("Fecha",modeloDetenciones.getFecha())
                .add("Hora", modeloDetenciones.getHora())
                .add("APDetenido", modeloDetenciones.getAPDetenido())
                .add("AMDetenido", modeloDetenciones.getAMDetenido())
                .add("NomDetenido", modeloDetenciones.getNomDetenido())
                .add("ApodoAlias", modeloDetenciones.getApodoAlias())
                .add("DescripcionDetenido", modeloDetenciones.getDescripcionDetenido())
                .add("IdNacionalidad", modeloDetenciones.getIdNacionalidad())
                .add("IdSexo", modeloDetenciones.getIdSexo())
                .add("FechaNacimiento",modeloDetenciones.getFechaNacimiento())
                .add("UrlFirmaDetenido", modeloDetenciones.getUrlFirmaDetenido())
                .add("IdEntidadFederativa", modeloDetenciones.getIdEntidadFederativa())
                .add("IdMunicipio", modeloDetenciones.getIdMunicipio())
                .add("ColoniaLocalidad", modeloDetenciones.getColoniaLocalidad())
                .add("CalleTramo", modeloDetenciones.getCalleTramo())
                .add("NoExterior", modeloDetenciones.getNoExterior())
                .add("NoInterior", modeloDetenciones.getNoInterior())
                .add("Cp", modeloDetenciones.getCp())
                .add("Referencia",modeloDetenciones.getReferencia())
                .add("Lesiones", modeloDetenciones.getLesiones())
                .add("Padecimientos", modeloDetenciones.getPadecimientos())
                .add("DescPadecimientos", modeloDetenciones.getDescPadecimientos())
                .add("GrupoVulnerable", modeloDetenciones.getGrupoVulnerable())
                .add("DescGrupoVulnerable", modeloDetenciones.getDescGrupoVulnerable())
                .add("IdLugarTraslado", modeloDetenciones.getIdLugarTraslado())
                .add("IdPoliciaPrimerRespondiente", modeloDetenciones.getIdPoliciaPrimerRespondiente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/DetencionesAdministrativa/")
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
                    Detenciones.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                insertImagen();
                                //Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                //guardarFolios();
                                txtFechaDetenido.setText("");
                                txthoraDetencion.setText("");
                                txtFechaNacimientoDetenido.setText("");
                                txtPrimerApellidoDetenido.setText("");
                                txtSegundoApellidoDetenido.setText("");
                                txtNombresDetenido.setText("");
                                txtApodoDetenido.setText("");
                                txtColoniaDetenido.setText("");
                                txtCalleDetenido.setText("");
                                txtNumeroExteriorDetenido.setText("");
                                txtNumeroInteriorDetenido.setText("");
                                txtCodigoPostalDetenido.setText("");
                                txtReferenciasdelLugarDetenido.setText("");
                                txtCualGrupoVulnerable.setText("");
                                txtCualPadecimiento.setText("");
                                addFragment(new Detenciones());

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

    //***************** Actualiza A LA BD MEDIANTE EL WS **************************//
    private void updateDetenciones() {
        DataHelper dataHelper = new DataHelper(getContext());

        descripcionLugarTraslado = (String) spLugarTrasladoPersonaDetenida.getSelectedItem();
        int idDesLugarTraslado = dataHelper.getIdLugarTraslado(descripcionLugarTraslado);
        String idLugarTraslado = String.valueOf(idDesLugarTraslado);

        descripcionMunicipio = (String) txtMunicipioDetenido.getSelectedItem();
        String idDescMunicipio = dataHelper.getIdMunicipio(descripcionMunicipio);
        String idMunicipio = String.valueOf(idDescMunicipio);

        if(idMunicipio.length() == 1){
            idMunicipio = "00"+idMunicipio;
        }else if(idMunicipio.length() == 2){
            idMunicipio = "0"+idMunicipio;
        }

        descripcionNacionalidad = (String) txtNacionalidadDetenido.getSelectedItem();
        int idDescNacionalidad = dataHelper.getIdNacionalidad(descripcionNacionalidad);
        String idNacionalidad = String.valueOf(idDescNacionalidad);

        descripcionSexo = (String) spGeneroDetenido.getSelectedItem();
        int idDescSexo = dataHelper.getIdSexo(descripcionSexo);
        String idSexo = String.valueOf(idDescSexo);

        if(chNoAplicaAliasDetenido.isChecked()){
            txtApodoDetenido.setText("NA");
        }

        if(txtNumeroExteriorDetenido.getText().toString().isEmpty()){
            txtNumeroExteriorDetenido.setText("SN");
        }
        if(txtNumeroInteriorDetenido.getText().toString().isEmpty()){
            txtNumeroInteriorDetenido.setText("NA");
        }

        String urlImagen = "http://189.254.7.167/WebServiceIPH/FirmaDetenidos/"+cargarIdFaltaAdmin+randomUrlImagen+".jpg";

        ModeloDetenciones_Administrativo modeloDetenciones = new ModeloDetenciones_Administrativo
                (cargarIdFaltaAdmin, "1",
                        txtFechaDetenido.getText().toString(), txthoraDetencion.getText().toString(),
                        txtPrimerApellidoDetenido.getText().toString(), txtSegundoApellidoDetenido.getText().toString(),
                        txtNombresDetenido.getText().toString(),txtApodoDetenido.getText().toString(), txtDescripciondelDetenido.getText().toString(),
                        idNacionalidad, idSexo, txtFechaNacimientoDetenido.getText().toString(), urlImagen,"12",idMunicipio,
                        txtColoniaDetenido.getText().toString(), txtCalleDetenido.getText().toString(),
                        txtNumeroExteriorDetenido.getText().toString(), txtNumeroInteriorDetenido.getText().toString(),
                        txtCodigoPostalDetenido.getText().toString(), txtReferenciasdelLugarDetenido.getText().toString(),
                        varLesiones, varPadecimiento, txtCualPadecimiento.getText().toString(),
                        varGrupoVulnerable, txtCualGrupoVulnerable.getText().toString(), idLugarTraslado, cargarUsuario);


        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdFaltaAdmin", modeloDetenciones.getIdFaltaAdmin())
                .add("NumDetencion", modeloDetenciones.getNumDetencion())
                .add("Fecha",modeloDetenciones.getFecha())
                .add("Hora", modeloDetenciones.getHora())
                .add("APDetenido", modeloDetenciones.getAPDetenido())
                .add("AMDetenido", modeloDetenciones.getAMDetenido())
                .add("NomDetenido", modeloDetenciones.getNomDetenido())
                .add("ApodoAlias", modeloDetenciones.getApodoAlias())
                .add("DescripcionDetenido", modeloDetenciones.getDescripcionDetenido())
                .add("IdNacionalidad", modeloDetenciones.getIdNacionalidad())
                .add("IdSexo", modeloDetenciones.getIdSexo())
                .add("FechaNacimiento",modeloDetenciones.getFechaNacimiento())
                .add("UrlFirmaDetenido", modeloDetenciones.getUrlFirmaDetenido())
                .add("IdEntidadFederativa", modeloDetenciones.getIdEntidadFederativa())
                .add("IdMunicipio", modeloDetenciones.getIdMunicipio())
                .add("ColoniaLocalidad", modeloDetenciones.getColoniaLocalidad())
                .add("CalleTramo", modeloDetenciones.getCalleTramo())
                .add("NoExterior", modeloDetenciones.getNoExterior())
                .add("NoInterior", modeloDetenciones.getNoInterior())
                .add("Cp", modeloDetenciones.getCp())
                .add("Referencia",modeloDetenciones.getReferencia())
                .add("Lesiones", modeloDetenciones.getLesiones())
                .add("Padecimientos", modeloDetenciones.getPadecimientos())
                .add("DescPadecimientos", modeloDetenciones.getDescPadecimientos())
                .add("GrupoVulnerable", modeloDetenciones.getGrupoVulnerable())
                .add("DescGrupoVulnerable", modeloDetenciones.getDescGrupoVulnerable())
                .add("IdLugarTraslado", modeloDetenciones.getIdLugarTraslado())
                .add("IdPoliciaPrimerRespondiente", modeloDetenciones.getIdPoliciaPrimerRespondiente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/DetencionesAdministrativa/")
                .put(body)
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
                    Detenciones.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                insertImagen();
                                //Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                //guardarFolios();
                                txtFechaDetenido.setText("");
                                txthoraDetencion.setText("");
                                txtFechaNacimientoDetenido.setText("");
                                txtPrimerApellidoDetenido.setText("");
                                txtSegundoApellidoDetenido.setText("");
                                txtNombresDetenido.setText("");
                                txtApodoDetenido.setText("");
                                txtColoniaDetenido.setText("");
                                txtCalleDetenido.setText("");
                                txtNumeroExteriorDetenido.setText("");
                                txtNumeroInteriorDetenido.setText("");
                                txtCodigoPostalDetenido.setText("");
                                txtReferenciasdelLugarDetenido.setText("");
                                txtCualGrupoVulnerable.setText("");
                                txtCualPadecimiento.setText("");
                                addFragment(new Detenciones());

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

    /***************************** SPINNER *************************************************************/
    private void ListLugarTraslado() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllLugarTraslado();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE LUGAR TRASLADO");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spLugarTrasladoPersonaDetenida.setAdapter(adapter);
        }
    }
    private void ListMunicipios() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllMunicipios();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MUNICIPIOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            txtMunicipioDetenido.setAdapter(adapter);
        }
    }
    private void ListNacionalidad() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllNacionalidad();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE NACIONALIDAD");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            txtNacionalidadDetenido.setAdapter(adapter);
        }
    }
    private void ListSexo() {
        DataHelper dataHelper = new DataHelper(getContext());
        ArrayList<String> list = dataHelper.getAllSexo();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE SEXO");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, list);
            spGeneroDetenido.setAdapter(adapter);
        }
    }

    public void cargarFolios(){
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarIdFaltaAdmin = share.getString("IDFALTAADMIN", "");
        cargarUsuario = share.getString("Usuario", "");
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    private void EliminarDetenido(String IdDetenido) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/DetencionesAdministrativa?folioInterno="+cargarIdFaltaAdmin+"&idDetenido="+IdDetenido)
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
                                    addFragment(new Detenciones());
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
                .replace(R.id.fragmentContenedorJusticiacivica, fragment)
                //.addToBackStack(null) //Se quita la pila de fragments. Botón atrás
                .commit();
    }

    public void random(){
        Random random = new Random();
        numberRandom = random.nextInt(9000)*99;
        randomUrlImagen = numberRandom;
    }

}