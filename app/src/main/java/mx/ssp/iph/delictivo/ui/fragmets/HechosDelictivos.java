                                                                                                                                                                                                                            package mx.ssp.iph.delictivo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import mx.ssp.iph.Login;
import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.model.ModeloNoReferencia_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloRecibeDisposicion_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloUsuarios_Administrativo;
import mx.ssp.iph.administrativo.ui.fragmets.NoReferencia_Administrativo;
import mx.ssp.iph.administrativo.ui.fragmets.PuestaDisposicion_Administrativo;
import mx.ssp.iph.delictivo.model.ModeloAnexosMultimedia_Delictivo;
import mx.ssp.iph.delictivo.model.ModeloHechoDelictivo;
import mx.ssp.iph.delictivo.model.ModeloRecibeDisposicion_Delictivo;
import mx.ssp.iph.delictivo.viewModel.HechosDelictivosViewModel;
import mx.ssp.iph.principal.ui.activitys.Principal;
import mx.ssp.iph.utilidades.GridViewAdapter;
import mx.ssp.iph.utilidades.ui.ContenedorFirmaDelictivo;
import mx.ssp.iph.utilidades.ui.ContenedorFirmaPDisposicionDelictivo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.Funciones;
import retrofit2.http.GET;

import static android.app.Activity.RESULT_OK;

public class HechosDelictivos extends Fragment {

    private HechosDelictivosViewModel mViewModel;
    Button btnGuardarHechoDelictivo;
    EditText txtFolioInternoDelictivo,txtFolioSistemaDelictivo,txtNoReferenciaDelictivo,txtNoExpedienteAdmministrativo,txtFechaEntregaReferenciaDelictivo,txtHoraEntregaReferenciaDelictivo,
            txtFiscaliaAutoridadDelictivo;
    CheckBox chDetencionesAnexoADelictivo,chAnexosDInventarioArmasDelictivo,chUsoFuerzaAnexoBDelictivo,chEntrevistasAnexoEDelictivo,chAnexosCInspeccionVehiculoDelictivo,chAnexosFEntregaRecepcionDelictivo,
            chSinAnexosDelictivo;
    Spinner spDetencionesAnexoADelictivo,spAnexosDInventarioArmasDelictivo,spUsoFuerzaAnexoBDelictivo,spEntrevistasAnexoEDelictivo,spAnexosCInspeccionVehiculoDelictivo,spAnexosFEntregaRecepcionDelictivo,
            spAdscripcionDelictivo,spCargoDelictivo;
    RadioGroup rgAnexoMultimediaDelictivo;
    TextView lblFirmaAutoridadRealizadaDelictivo,lblFirmaOcultaAutoridadBase64HechosDelictivos;
    ImageView imgFirmaAutoridadDelictivo,imgFirmaAutoridadRealizadaDelictivoMiniatura;
    String noReferencia,edo = "",inst = "",gob = "",mpio = "",fecha = "",dia = "01",mes = "01",anio = "2021",tiempo = "",hora = "",minutos = "",
            cargarIdPoliciaPrimerRespondiente = "",cargarIdHechoDelictivo = "",respuestaJson = "",descAutoridad,descCargo;
    String anexoDetenciones = "NO", numAnexoDetenciones = "000", anexoUsoFuerza = "NO",  numAnexoUsoFuerza = "000",  anexoVehiculos = "NO",
     numAnexoVehiculo = "000",  anexoArmasObjetos = "NO",  numAnexoArmasObjetos = "000", anexoEntrevista = "NO",  numAnexoEntrevista = "000", anexoLugarIntervencion = "NO",
     numAnexoLugarIntervencion = "000",  anexoNoSeEntregan = "SI",varAnexoMultimedia,rutaAnexoMultimedia;
    SharedPreferences share;
    int numberRandom,randomUrlImagen;
    String firmaURLServer = "http://189.254.7.167/WebServiceIPH/Firma/SINFIRMA.jpg";
    private Target target;
    Button btnBase64;
    LinearLayout lyCargarFotografias,lyEnviarFotografias;


    ViewGroup lyAnexosUno, quintoLinear1, quintoLinear2, lyDetencionesAnexoADelictivo, lyInventarioArmasDelictivo,
            lyUsoFuerzaAnexoBDelictivo, lyEntrevistasDelictivo, lyInspeccionVehiculoDelictivo, lyEntregaRecepcionDelictivo, lyFiscaliaAutoridadDelictivo;

    Integer aux1, aux2, aux3, aux4, aux5, aux6;

    //Para subir imagnes
    List<Uri> listaImagenes = new ArrayList<>();
    List<Integer> identificador = new ArrayList<>();
    List<String> listaBase64Imagenes = new ArrayList<>();

    int PICK_IMAGE = 100;
    Uri imageUri;
    GridView gvImagenes;
    GridViewAdapter baseAdapter;
    Button btnSubirImagenes;
    //////////////



    private Funciones funciones;

    public static HechosDelictivos newInstance() {
        return new HechosDelictivos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.hechos_delictivos_fragment, container, false);

        /*********************************************************************************************************/
        cargarDatos();
        random();

        btnGuardarHechoDelictivo = view.findViewById(R.id.btnGuardarHechoDelictivo);
        txtFechaEntregaReferenciaDelictivo = view.findViewById(R.id.txtFechaEntregaReferenciaDelictivo);
        txtHoraEntregaReferenciaDelictivo = view.findViewById(R.id.txtHoraEntregaReferenciaDelictivo);
        funciones = new Funciones();
        txtFolioInternoDelictivo = view.findViewById(R.id.txtFolioInternoDelictivo);
        txtFolioSistemaDelictivo = view.findViewById(R.id.txtFolioSistemaDelictivo);
        txtNoReferenciaDelictivo = view.findViewById(R.id.txtNoReferenciaDelictivo);
        txtNoReferenciaDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(25)});
        txtNoExpedienteAdmministrativo = view.findViewById(R.id.txtNoExpedienteAdmministrativo);
        txtNoExpedienteAdmministrativo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(25)});
        txtFechaEntregaReferenciaDelictivo = view.findViewById(R.id.txtFechaEntregaReferenciaDelictivo);
        txtHoraEntregaReferenciaDelictivo = view.findViewById(R.id.txtHoraEntregaReferenciaDelictivo);
        txtFiscaliaAutoridadDelictivo = view.findViewById(R.id.txtFiscaliaAutoridadDelictivo);
        txtFiscaliaAutoridadDelictivo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(50)});

        chDetencionesAnexoADelictivo = view.findViewById(R.id.chDetencionesAnexoADelictivo);
        chAnexosDInventarioArmasDelictivo = view.findViewById(R.id.chAnexosDInventarioArmasDelictivo);
        chUsoFuerzaAnexoBDelictivo = view.findViewById(R.id.chUsoFuerzaAnexoBDelictivo);
        chEntrevistasAnexoEDelictivo = view.findViewById(R.id.chEntrevistasAnexoEDelictivo);
        chAnexosCInspeccionVehiculoDelictivo = view.findViewById(R.id.chAnexosCInspeccionVehiculoDelictivo);
        chAnexosFEntregaRecepcionDelictivo = view.findViewById(R.id.chAnexosFEntregaRecepcionDelictivo);
        chSinAnexosDelictivo = view.findViewById(R.id.chSinAnexosDelictivo);

        spDetencionesAnexoADelictivo = view.findViewById(R.id.spDetencionesAnexoADelictivo);
        spAnexosDInventarioArmasDelictivo = view.findViewById(R.id.spAnexosDInventarioArmasDelictivo);
        spUsoFuerzaAnexoBDelictivo = view.findViewById(R.id.spUsoFuerzaAnexoBDelictivo);
        spEntrevistasAnexoEDelictivo = view.findViewById(R.id.spEntrevistasAnexoEDelictivo);
        spAnexosCInspeccionVehiculoDelictivo = view.findViewById(R.id.spAnexosCInspeccionVehiculoDelictivo);
        spAnexosFEntregaRecepcionDelictivo = view.findViewById(R.id.spAnexosFEntregaRecepcionDelictivo);
        spAdscripcionDelictivo = view.findViewById(R.id.spAdscripcionDelictivo);
        spCargoDelictivo = view.findViewById(R.id.spCargoDelictivo);
        rgAnexoMultimediaDelictivo = view.findViewById(R.id.rgAnexoMultimediaDelictivo);

        lblFirmaAutoridadRealizadaDelictivo = view.findViewById(R.id.lblFirmaAutoridadRealizadaDelictivo);
        imgFirmaAutoridadRealizadaDelictivoMiniatura = view.findViewById(R.id.imgFirmaAutoridadRealizadaDelictivoMiniatura);
        imgFirmaAutoridadDelictivo = view.findViewById(R.id.imgFirmaAutoridadDelictivo);
        lblFirmaOcultaAutoridadBase64HechosDelictivos = view.findViewById(R.id.lblFirmaOcultaAutoridadBase64HechosDelictivos);

        lyAnexosUno = view.findViewById(R.id.lyAnexosUno);
        quintoLinear1 = view.findViewById(R.id.quintoLinear1);
        quintoLinear2 = view.findViewById(R.id.quintoLinear2);
        lyDetencionesAnexoADelictivo = view.findViewById(R.id.lyDetencionesAnexoADelictivo);
        lyInventarioArmasDelictivo = view.findViewById(R.id.lyInventarioArmasDelictivo);
        lyUsoFuerzaAnexoBDelictivo = view.findViewById(R.id.lyUsoFuerzaAnexoBDelictivo);
        lyEntrevistasDelictivo = view.findViewById(R.id.lyEntrevistasDelictivo);
        lyInspeccionVehiculoDelictivo = view.findViewById(R.id.lyInspeccionVehiculoDelictivo);
        lyEntregaRecepcionDelictivo = view.findViewById(R.id.lyEntregaRecepcionDelictivo);
        lyFiscaliaAutoridadDelictivo = view.findViewById(R.id.lyFiscaliaAutoridadDelictivo);
        btnBase64 = view.findViewById(R.id.btnBase64);
        btnSubirImagenes  = view.findViewById(R.id.btnSubirImagenes);
        gvImagenes = view.findViewById(R.id.gvImagenes);

        lyCargarFotografias = view.findViewById(R.id.lyCargarFotografias);
        lyEnviarFotografias = view.findViewById(R.id.lyEnviarFotografias);

        ListCombos();
        getNumReferencia();
        txtFolioInternoDelictivo.setText(cargarIdHechoDelictivo);
        txtNoReferenciaDelictivo.setEnabled(false);
        txtFolioInternoDelictivo.setEnabled(false);

        spDetencionesAnexoADelictivo.setEnabled(false);
        spUsoFuerzaAnexoBDelictivo.setEnabled(false);
        spAnexosCInspeccionVehiculoDelictivo.setEnabled(false);
        spAnexosDInventarioArmasDelictivo.setEnabled(false);
        spEntrevistasAnexoEDelictivo.setEnabled(false);
        spAnexosFEntregaRecepcionDelictivo.setEnabled(false);

        funciones.CambiarTituloSeccionesDelictivo("SECCIÓN 1. PUESTA A DISPOSICIÓN",getContext(),getActivity());

        //***************** Trae los Datos del Folio de la primera Sección  **************************//
        //Consulta si hay conexión a internet y realiza la peticion al ws de consulta de los datos.
        if (funciones.ping(getContext()))
        {
            cargarHechoDelictivo();
        }

            //Boton subir imagenes
        btnSubirImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("INTENT","PRESS");

                Intent intent = new Intent();
                intent.setType("image/*");
                //intent.setType("application/pdf");

                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Log.d("INTENT","ACTION");

                startActivityForResult(Intent.createChooser(intent, "SELECCIONA LAS IMAGENES"), 111);
               // startActivityForResult(intent, 111);
                Log.d("INTENT","START");

            }
        });

        btnBase64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaBase64Imagenes.clear();

                for(int i = 0 ; i < listaImagenes.size() ; i++) {
                    try {

                        InputStream is = getActivity().getContentResolver().openInputStream(listaImagenes.get(i));
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        String cadena = convertirUriToBase64(bitmap);

                        //enviarImagenes("nomIma"+i, cadena);
                        String nombreImagen = Integer.toString(i);
                        //Log.i("BASE64",cadena);


                        insertAnexosMultimedia(cadena,nombreImagen);
                        bitmap.recycle();

                    } catch (IOException e) {
                        Log.i("CATCH",e.toString());

                    }

                }

            }
        });



        //***************** FECHA  **************************//
        txtFechaEntregaReferenciaDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaEntregaReferenciaDelictivo,getContext(),getActivity());
            }
        });

        //***************** HORA **************************//
        txtHoraEntregaReferenciaDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txtHoraEntregaReferenciaDelictivo,getContext(),getActivity());
            }
        });

        //***************** FIRMA **************************//
        imgFirmaAutoridadDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirmaDelictivo dialog = new ContenedorFirmaDelictivo(R.id.lblFirmaAutoridadRealizadaDelictivo,R.id.lblFirmaOcultaAutoridadBase64HechosDelictivos,R.id.imgFirmaAutoridadRealizadaDelictivoMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });


        //VALOR DE LISTA ANEXO A
        spDetencionesAnexoADelictivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                Object item = parent.getItemIdAtPosition(pos);

                int i = Integer.parseInt(item.toString()) + 1;

                aux1 = i;
                //Toast.makeText(getContext(), "" + i, Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //VALOR DE LISTA ANEXO B
        spUsoFuerzaAnexoBDelictivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                Object item = parent.getItemIdAtPosition(pos);

                int i = Integer.parseInt(item.toString()) + 1;

                aux2 = i;
                //Toast.makeText(getContext(), "" + i, Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //VALOR DE LISTA ANEXO C
        spAnexosCInspeccionVehiculoDelictivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                Object item = parent.getItemIdAtPosition(pos);

                int i = Integer.parseInt(item.toString()) + 1;

                aux3 = i;
                //Toast.makeText(getContext(), "" + i, Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //VALOR DE LISTA ANEXO D
        spAnexosDInventarioArmasDelictivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                Object item = parent.getItemIdAtPosition(pos);

                int i = Integer.parseInt(item.toString()) + 1;

                aux4 = i;
                //Toast.makeText(getContext(), "" + i, Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //VALOR DE LISTA ANEXO E
        spEntrevistasAnexoEDelictivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                Object item = parent.getItemIdAtPosition(pos);

                int i = Integer.parseInt(item.toString()) + 1;

                aux5 = i;
                //Toast.makeText(getContext(), "" + i, Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //VALOR DE LISTA ANEXO F
        spAnexosFEntregaRecepcionDelictivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        Object item = parent.getItemIdAtPosition(pos);

                        int i = Integer.parseInt(item.toString()) + 1;

                        aux6 = i;
                        //Toast.makeText(getContext(), "" + i, Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


        chDetencionesAnexoADelictivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {
                if(chselect == true){
                    spDetencionesAnexoADelictivo.setEnabled(true);
                } else if(chselect == false) {
                    spDetencionesAnexoADelictivo.setEnabled(false);
                    spDetencionesAnexoADelictivo.setSelection(0);
                }
            }
        });

        chUsoFuerzaAnexoBDelictivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {
                if(chselect == true){
                    spUsoFuerzaAnexoBDelictivo.setEnabled(true);
                } else if(chselect == false) {
                    spUsoFuerzaAnexoBDelictivo.setEnabled(false);
                    spUsoFuerzaAnexoBDelictivo.setSelection(0);
                }
            }
        });

        chAnexosCInspeccionVehiculoDelictivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {
                if(chselect == true){
                    spAnexosCInspeccionVehiculoDelictivo.setEnabled(true);
                } else if(chselect == false) {
                    spAnexosCInspeccionVehiculoDelictivo.setEnabled(false);
                    spAnexosCInspeccionVehiculoDelictivo.setSelection(0);
                }
            }
        });

        chAnexosDInventarioArmasDelictivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {
                if(chselect == true){
                    spAnexosDInventarioArmasDelictivo.setEnabled(true);
                } else if(chselect == false) {
                    spAnexosDInventarioArmasDelictivo.setEnabled(false);
                    spAnexosDInventarioArmasDelictivo.setSelection(0);
                }
            }
        });

        chEntrevistasAnexoEDelictivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {
                if(chselect == true){
                    spEntrevistasAnexoEDelictivo.setEnabled(true);
                } else if(chselect == false) {
                    spEntrevistasAnexoEDelictivo.setEnabled(false);
                    spEntrevistasAnexoEDelictivo.setSelection(0);
                }
            }
        });

        chAnexosFEntregaRecepcionDelictivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {
                if(chselect == true){
                    spAnexosFEntregaRecepcionDelictivo.setEnabled(true);
                } else if(chselect == false) {
                    spAnexosFEntregaRecepcionDelictivo.setEnabled(false);
                    spAnexosFEntregaRecepcionDelictivo.setSelection(0);
                }
            }
        });

        chSinAnexosDelictivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean chselect) {

                if(chselect == true){
                    chDetencionesAnexoADelictivo.setChecked(false);
                    chUsoFuerzaAnexoBDelictivo.setChecked(false);
                    chAnexosCInspeccionVehiculoDelictivo.setChecked(false);
                    chAnexosDInventarioArmasDelictivo.setChecked(false);
                    chEntrevistasAnexoEDelictivo.setChecked(false);
                    chAnexosFEntregaRecepcionDelictivo.setChecked(false);

                    chDetencionesAnexoADelictivo.setEnabled(false);
                    chUsoFuerzaAnexoBDelictivo.setEnabled(false);
                    chAnexosCInspeccionVehiculoDelictivo.setEnabled(false);
                    chAnexosDInventarioArmasDelictivo.setEnabled(false);
                    chEntrevistasAnexoEDelictivo.setEnabled(false);
                    chAnexosFEntregaRecepcionDelictivo.setEnabled(false);

                    spDetencionesAnexoADelictivo.setSelection(0);
                    spUsoFuerzaAnexoBDelictivo.setSelection(0);
                    spAnexosCInspeccionVehiculoDelictivo.setSelection(0);
                    spAnexosDInventarioArmasDelictivo.setSelection(0);
                    spEntrevistasAnexoEDelictivo.setSelection(0);
                    spAnexosFEntregaRecepcionDelictivo.setSelection(0);

                    spDetencionesAnexoADelictivo.setEnabled(false);
                    spUsoFuerzaAnexoBDelictivo.setEnabled(false);
                    spAnexosCInspeccionVehiculoDelictivo.setEnabled(false);
                    spAnexosDInventarioArmasDelictivo.setEnabled(false);
                    spEntrevistasAnexoEDelictivo.setEnabled(false);
                    spAnexosFEntregaRecepcionDelictivo.setEnabled(false);

                } else if(chselect == false) {
                    chDetencionesAnexoADelictivo.setEnabled(true);
                    chUsoFuerzaAnexoBDelictivo.setEnabled(true);
                    chAnexosCInspeccionVehiculoDelictivo.setEnabled(true);
                    chAnexosDInventarioArmasDelictivo.setEnabled(true);
                    chEntrevistasAnexoEDelictivo.setEnabled(true);
                    chAnexosFEntregaRecepcionDelictivo.setEnabled(true);
                }
            }
        });

        rgAnexoMultimediaDelictivo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSiAnexoMultimediaDelictivo) {
                    varAnexoMultimedia = "SI";
                    lyCargarFotografias.setVisibility(view.VISIBLE);
                    lyEnviarFotografias.setVisibility(view.VISIBLE);
                    gvImagenes.setVisibility(view.VISIBLE);
                } else if (checkedId == R.id.rbNoAnexoMultimediaDelictivo) {
                    varAnexoMultimedia = "NO";
                    lyCargarFotografias.setVisibility(view.GONE);
                    lyEnviarFotografias.setVisibility(view.GONE);
                    gvImagenes.setVisibility(view.GONE);
                }

            }
        });


        btnGuardarHechoDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CeroValidacion();

            }
        });

        //***************** Tarjet para cachar la imagen de picaso  **************************//
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
                byte[] imgBytes = baos.toByteArray();

                String imgString = android.util.Base64.encodeToString(imgBytes, android.util.Base64.NO_WRAP);
                lblFirmaOcultaAutoridadBase64HechosDelictivos.setText(imgString);

                byte[] decodedString = Base64.decode(imgString, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgFirmaAutoridadRealizadaDelictivoMiniatura.setImageBitmap(decodedByte);

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };


        /******************************************************************************/
        return  view;
    }

    public void CeroValidacion() {
        if(chDetencionesAnexoADelictivo.isChecked() || chAnexosDInventarioArmasDelictivo.isChecked() || chUsoFuerzaAnexoBDelictivo.isChecked() ||
                chEntrevistasAnexoEDelictivo.isChecked() || chAnexosCInspeccionVehiculoDelictivo.isChecked() ||
                chAnexosFEntregaRecepcionDelictivo.isChecked() || chSinAnexosDelictivo.isChecked()){
            PrimeraValidacion();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA SI HAY ANEXOS", Toast.LENGTH_SHORT).show();
            lyAnexosUno.requestFocus();
            quintoLinear1.requestFocus();
            quintoLinear1.requestFocus();
        }

    }

    public void PrimeraValidacion() {
        if (chDetencionesAnexoADelictivo.isChecked()){
            if(aux1 != 1){
                SegundaValidacion();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA LA CANTIDAD DE ANEXOS A", Toast.LENGTH_SHORT).show();
                lyAnexosUno.requestFocus();
                lyDetencionesAnexoADelictivo.requestFocus();
            }

        } else {
            SegundaValidacion();
        }
    }

    public void SegundaValidacion() {
        if (chUsoFuerzaAnexoBDelictivo.isChecked()){
            if(aux2 != 1){
                TerceraValidacion();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA LA CANTIDAD DE ANEXOS B", Toast.LENGTH_SHORT).show();
                lyAnexosUno.requestFocus();
                lyUsoFuerzaAnexoBDelictivo.requestFocus();
            }

        } else {
            TerceraValidacion();
        }
    }

    public void TerceraValidacion() {
        if (chAnexosCInspeccionVehiculoDelictivo.isChecked()){
            if(aux3 != 1){
                CuartaValidacion();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA LA CANTIDAD DE ANEXOS C", Toast.LENGTH_SHORT).show();
                lyAnexosUno.requestFocus();
                lyInspeccionVehiculoDelictivo.requestFocus();
            }

        } else {
            CuartaValidacion();
        }
    }

    public void CuartaValidacion() {
        if (chAnexosDInventarioArmasDelictivo.isChecked()){
            if(aux4 != 1){
                QuintaValidacion();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA LA CANTIDAD DE ANEXOS D", Toast.LENGTH_SHORT).show();
                lyAnexosUno.requestFocus();
                lyInventarioArmasDelictivo.requestFocus();
            }

        } else {
            QuintaValidacion();
        }
    }

    public void QuintaValidacion() {
        if (chEntrevistasAnexoEDelictivo.isChecked()){
            if(aux5 != 1){
                SextaValidacion();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA LA CANTIDAD DE ANEXOS E", Toast.LENGTH_SHORT).show();
                lyAnexosUno.requestFocus();
                lyEntrevistasDelictivo.requestFocus();
            }

        } else {
            SextaValidacion();
        }
    }

    public void SextaValidacion() {
        if (chAnexosFEntregaRecepcionDelictivo.isChecked()){
            if(aux6 != 1){
                if(txtFiscaliaAutoridadDelictivo.getText().toString().length() >= 3){
                    SeptimaValidacion();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA LA FISCALÍA O AUTORIDAD A LA QUE PERTENECE", Toast.LENGTH_SHORT).show();
                    txtFiscaliaAutoridadDelictivo.requestFocus();
                    lyFiscaliaAutoridadDelictivo.requestFocus();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "SELECCIONA LA CANTIDAD DE ANEXOS F", Toast.LENGTH_SHORT).show();
                lyAnexosUno.requestFocus();
                lyEntregaRecepcionDelictivo.requestFocus();
            }

        } else {
            if(txtFiscaliaAutoridadDelictivo.getText().toString().length() >= 3){
                SeptimaValidacion();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "ESPECIFICA LA FISCALÍA O AUTORIDAD A LA QUE PERTENECE", Toast.LENGTH_SHORT).show();
                txtFiscaliaAutoridadDelictivo.requestFocus();
                lyFiscaliaAutoridadDelictivo.requestFocus();
            }
        }
    }

    public void SeptimaValidacion(){
        if(lblFirmaAutoridadRealizadaDelictivo.getText().toString().isEmpty()){
            Toast.makeText(getActivity().getApplicationContext(), "INGRESA LA FIRMA DE LA AUTORIDAD PARA CONTINUAR", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
            updateHechoDelictivo();
        }
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HechosDelictivosViewModel.class);
        // TODO: Use the ViewModel
    }
    //***************** GET A LA BD MEDIANTE EL WS **************************//
    private void getNumReferencia() {
        //*************** FECHA **********************//
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        fecha = dateFormat.format(date);
        String [] partsFecha = fecha.split("/");
        anio = partsFecha[0];
        mes = partsFecha[1];
        dia = partsFecha[2];
        //*************** HORA **********************//
        Date time = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        tiempo = timeFormat.format(time);
        String[] partsTiempo = tiempo.split(":");
        hora = partsTiempo[0];
        minutos = partsTiempo[1];
        /******************************************************/
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDHechoDelictivo?usuario="+cargarIdPoliciaPrimerRespondiente)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getActivity(),"ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    HechosDelictivos.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "FALSE";
                                if(myResponse.equals(respuestaJson)){
                                    Toast.makeText(getActivity(),"NO SE CUENTA CON INFORMACIÓN",Toast.LENGTH_SHORT).show();
                                }else{
                                    JSONObject jObj = null;
                                    jObj = new JSONObject(""+myResponse+"");
                                    edo = jObj.getString("IdEntidadFederativa");
                                    inst = jObj.getString("IdInstitucion");
                                    gob = jObj.getString("IdGobierno");
                                    mpio = jObj.getString("IdMunicipio");
                                    noReferencia = edo+inst+gob+mpio+dia+mes+anio+hora+minutos;
                                    txtNoReferenciaDelictivo.setText(edo+"|"+inst+"|"+gob+"|"+mpio+"|"+dia+"|"+mes+"|"+anio+"|"+hora+"|"+minutos);
                                    Log.i("HERE", ""+jObj);
                                }

                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }

                    });
                }
            }

        });
    }

    //***************** UPDATE A LA BD MEDIANTE EL WS **************************//
    private void updateHechoDelictivo() {

        if(chDetencionesAnexoADelictivo.isChecked()){
            anexoDetenciones = "SI";
            numAnexoDetenciones = (String) spDetencionesAnexoADelictivo.getSelectedItem();
        }else{
            anexoDetenciones = "NO";
            numAnexoDetenciones = "000";
        }

        if(chUsoFuerzaAnexoBDelictivo.isChecked()){
            anexoUsoFuerza = "SI";
            numAnexoUsoFuerza = (String) spUsoFuerzaAnexoBDelictivo.getSelectedItem();
        }else{
            anexoUsoFuerza = "NO";
            numAnexoUsoFuerza = "000";
        }

        if(chAnexosCInspeccionVehiculoDelictivo.isChecked()){
            anexoVehiculos = "SI";
            numAnexoVehiculo = (String) spAnexosCInspeccionVehiculoDelictivo.getSelectedItem();
        }else{
            anexoVehiculos = "NO";
            numAnexoVehiculo = "000";
        }

        if(chAnexosDInventarioArmasDelictivo.isChecked()){
            anexoArmasObjetos = "SI";
            numAnexoArmasObjetos = (String) spAnexosDInventarioArmasDelictivo.getSelectedItem();
        }else{
            anexoArmasObjetos = "NO";
            numAnexoArmasObjetos = "000";
        }

        if(chEntrevistasAnexoEDelictivo.isChecked()){
            anexoEntrevista = "SI";
            numAnexoEntrevista = (String) spEntrevistasAnexoEDelictivo.getSelectedItem();
        }else{
            anexoEntrevista = "NO";
            numAnexoEntrevista = "000";
        }

        if(chAnexosFEntregaRecepcionDelictivo.isChecked()){
            anexoLugarIntervencion = "SI";
            numAnexoLugarIntervencion = (String) spAnexosFEntregaRecepcionDelictivo.getSelectedItem();
        }else{
            anexoLugarIntervencion = "NO";
            numAnexoLugarIntervencion = "000";
        }

        if(chSinAnexosDelictivo.isChecked()){
            anexoNoSeEntregan = "NO";
        }

        ModeloHechoDelictivo modeloHechoDelictivo = new ModeloHechoDelictivo(
                cargarIdHechoDelictivo, txtNoReferenciaDelictivo.getText().toString(), txtFechaEntregaReferenciaDelictivo.getText().toString(),
                txtHoraEntregaReferenciaDelictivo.getText().toString(),
                txtNoExpedienteAdmministrativo.getText().toString(), anexoDetenciones,
                numAnexoDetenciones, anexoUsoFuerza,  numAnexoUsoFuerza,  anexoVehiculos,
                 numAnexoVehiculo, anexoArmasObjetos, numAnexoArmasObjetos,
                 anexoEntrevista, numAnexoEntrevista, anexoLugarIntervencion,
                 numAnexoLugarIntervencion, anexoNoSeEntregan,
                cargarIdPoliciaPrimerRespondiente);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo", modeloHechoDelictivo.getIdHechoDelictivo())
                .add("NumReferencia", modeloHechoDelictivo.getNumReferencia())
                .add("Fecha",modeloHechoDelictivo.getFecha())
                .add("Hora", modeloHechoDelictivo.getHora())
                .add("NumExpediente", modeloHechoDelictivo.getNumExpediente())
                .add("AnexoDetenciones", modeloHechoDelictivo.getAnexoDetenciones())
                .add("NumAnexoDetenciones", modeloHechoDelictivo.getNumAnexoDetenciones())
                .add("AnexoUsoFuerza", modeloHechoDelictivo.getAnexoUsoFuerza())
                .add("NumAnexoUsoFuerza", modeloHechoDelictivo.getNumAnexoUsoFuerza())
                .add("AnexoVehiculos",modeloHechoDelictivo.getAnexoVehiculos())
                .add("NumAnexoVehiculo", modeloHechoDelictivo.getNumAnexoVehiculo())
                .add("AnexoArmasObjetos", modeloHechoDelictivo.getAnexoArmasObjetos())
                .add("NumAnexoArmasObjetos", modeloHechoDelictivo.getNumAnexoArmasObjetos())
                .add("AnexoEntrevista", modeloHechoDelictivo.getAnexoEntrevista())
                .add("NumAnexoEntrevista", modeloHechoDelictivo.getNumAnexoEntrevista())
                .add("AnexoLugarIntervencion", modeloHechoDelictivo.getAnexoLugarIntervencion())
                .add("NumAnexoLugarIntervencion", modeloHechoDelictivo.getNumAnexoLugarIntervencion())
                .add("AnexoNoSeEntregan",modeloHechoDelictivo.getAnexoNoSeEntregan())
                .add("IdPoliciaPrimerRespondiente", modeloHechoDelictivo.getIdPoliciaPrimerRespondiente())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDHechoDelictivo/")
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
                    HechosDelictivos.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                //Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                updateRecibeDisposicion();
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

    private void updateRecibeDisposicion() {
        DataHelper dataHelper = new DataHelper(getContext());
        descAutoridad = (String) spAdscripcionDelictivo.getSelectedItem();
        int idAdscripcion = dataHelper.getIdAutoridadAdmin(descAutoridad);
        String adscripcion = String.valueOf(idAdscripcion);

        descCargo = (String) spCargoDelictivo.getSelectedItem();
        int idCargo = dataHelper.getIdCargo(descCargo);
        String cargo = String.valueOf(idCargo);

        String urlImagen = "http://189.254.7.167/WebServiceIPH/FirmaRDDelictivo/"+cargarIdHechoDelictivo+randomUrlImagen+".jpg";

        ModeloRecibeDisposicion_Delictivo recibePuestaDisposicion = new ModeloRecibeDisposicion_Delictivo
                (cargarIdHechoDelictivo,txtFiscaliaAutoridadDelictivo.getText().toString(),
                        adscripcion,adscripcion,cargo,urlImagen);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo", recibePuestaDisposicion.getIdHechoDelictivo())
                .add("NombreRecibeDisposicion", recibePuestaDisposicion.getNombreRecibeDisposicion())
                .add("IdFiscaliaAutoridad", recibePuestaDisposicion.getIdFiscaliaAutoridad())
                .add("IdAdscripcion", recibePuestaDisposicion.getIdFiscaliaAutoridad())
                .add("IdCargo", recibePuestaDisposicion.getIdCargo())
                .add("RutaFirma", recibePuestaDisposicion.getRutaFirma())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDRecibeDisposicion/")
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
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    HechosDelictivos.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO DE LA RECEPCION DE DISPOSICION SE ENVIO CORRECTAMENTE");
                                //Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
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


    //********************************** INSERTA IMAGEN AL SERVIDOR ***********************************//
    public void insertImagen() {
        String cadena = lblFirmaOcultaAutoridadBase64HechosDelictivos.getText().toString();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Description", cargarIdHechoDelictivo + randomUrlImagen + ".jpg")
                .add("ImageData", cadena)
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/MultimediaFirmaRecibeDispocision")
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
                    HechosDelictivos.this.getActivity().runOnUiThread(new Runnable() {
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

    /******************************* ANEXOS ***********************************************/
    public void insertAnexo(){
        ModeloAnexosMultimedia_Delictivo anexoMultimedia = new ModeloAnexosMultimedia_Delictivo
                (cargarIdHechoDelictivo, varAnexoMultimedia,
                        "3", rutaAnexoMultimedia, "NA");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdHechoDelictivo", anexoMultimedia.getIdHechoDelictivo())
                .add("AnexoDocumentacion", anexoMultimedia.getAnexoDocumentacion())
                .add("IdAnexoMultimedia", anexoMultimedia.getIdAnexoMultimedia())
                .add("RutaAnexoMultimedia", anexoMultimedia.getRutaAnexoMultimedia())
                .add("AnexoMultimediaOtro", anexoMultimedia.getAnexoMultimediaOtro())
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDAnexosMultimedia")
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
                    HechosDelictivos.this.getActivity().runOnUiThread(new Runnable() {
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

    public void insertAnexosMultimedia(String cadena, String numImagen){
        OkHttpClient client = new OkHttpClient();

        rutaAnexoMultimedia = "http://189.254.7.167/WebServiceIPH/MultimediaAnexos/"+cargarIdHechoDelictivo+numImagen+".jpg";

        RequestBody body = new FormBody.Builder()
                .add("Description", cargarIdHechoDelictivo + numImagen + ".jpg")
                .add("ImageData", cadena)
                .build();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/MultimediaAnexos")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, FAVOR DE VERIFICAR SU CONEXCIÓN A INTERNET", Toast.LENGTH_LONG).show();

                HechosDelictivos.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        identificador.set(Integer.parseInt(numImagen),R.drawable.ic_clear);
                        baseAdapter = new GridViewAdapter(   getContext(), listaImagenes,identificador);
                        gvImagenes.setAdapter(baseAdapter);
                    }
                });
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    HechosDelictivos.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO DE LA RECEPCION DE DISPOSICION SE ENVIO CORRECTAMENTE"+ numImagen);
                                //Toast.makeText(getContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                //insertImagen();
                                Toast.makeText(getContext(), "ENVIADA"+numImagen, Toast.LENGTH_SHORT).show();
                                identificador.set(Integer.parseInt(numImagen),R.drawable.ic_check);
                                baseAdapter = new GridViewAdapter(   getContext(), listaImagenes,identificador);
                                gvImagenes.setAdapter(baseAdapter);
                                insertAnexo();

                            }else{
                                Toast.makeText(getContext(), "ERROR AL ENVIAR SU REGISTRO, VERIFIQUE SU INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                identificador.set(Integer.parseInt(numImagen),R.drawable.ic_clear);
                                baseAdapter = new GridViewAdapter(   getContext(), listaImagenes,identificador);
                                gvImagenes.setAdapter(baseAdapter);
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
        ArrayList<String> cargos = dataHelper.getAllCargos();
        ArrayList<String> autoridad = dataHelper.getAllAutoridadAdmin();
        if (cargos.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE CARGOS");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, cargos);
            spCargoDelictivo.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON CARGOS ACTIVOS.", Toast.LENGTH_LONG).show();
        }
        if (autoridad.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE AUTORIDAD");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, R.id.txt, autoridad);
            spAdscripcionDelictivo.setAdapter(adapter);
        }else{
            Toast.makeText(getContext(), "LO SENTIMOS, NO CUENTA CON ADSCRIPCIONES.", Toast.LENGTH_LONG).show();
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
    private void cargarHechoDelictivo() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/HDHechoDelictivo?folioInterno="+cargarIdHechoDelictivo)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getContext(), "ERROR AL CONSULTAR DATOS SECCIÓN 1, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
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
                                String ArregloJson = resp.replace("[", "");
                                ArregloJson = ArregloJson.replace("]", "");

                                if(ArregloJson.equals(""))
                                {
                                    //Sin Información. Todos los campos vacíos. Solo se llena
                                    txtFolioInternoDelictivo.setText(cargarIdHechoDelictivo);
                                }
                                else{
                                    //Deserializa el json y colcoa el dato correspondiente en cada campo si existe
                                    try {
                                        JSONObject jsonjObject = new JSONObject(ArregloJson);

                                        txtNoReferenciaDelictivo.setText((jsonjObject.getString("NumReferencia")).equals("null")?"":jsonjObject.getString("NumReferencia"));
                                        txtNoExpedienteAdmministrativo.setText((jsonjObject.getString("NumExpediente")).equals("null")?"":jsonjObject.getString("NumExpediente"));

                                        String[] Fecha = (jsonjObject.getString("Fecha").replace("-","/")).split("T");
                                        txtFechaEntregaReferenciaDelictivo.setText((jsonjObject.getString("Fecha")).equals("null")?"1900/01/01":Fecha[0]);
                                        txtHoraEntregaReferenciaDelictivo.setText((jsonjObject.getString("Hora")).equals("null")?"":jsonjObject.getString("Hora"));



                                        if((jsonjObject.getString("AnexoNoSeEntregan").equals("NO"))){
                                            chSinAnexosDelictivo.setChecked(true);
                                        }
                                        else {
                                            if ((jsonjObject.getString("AnexoDetenciones").equals("SI"))) {
                                                chDetencionesAnexoADelictivo.setChecked(true);
                                                spDetencionesAnexoADelictivo.setSelection(funciones.getIndexSpiner(spDetencionesAnexoADelictivo, jsonjObject.getString("NumAnexoDetenciones")));
                                            }

                                            if ((jsonjObject.getString("AnexoUsoFuerza").equals("SI"))) {
                                                chUsoFuerzaAnexoBDelictivo.setChecked(true);
                                                spUsoFuerzaAnexoBDelictivo.setSelection(funciones.getIndexSpiner(spUsoFuerzaAnexoBDelictivo, jsonjObject.getString("NumAnexoUsoFuerza")));
                                            }

                                            if ((jsonjObject.getString("AnexoVehiculos").equals("SI"))) {
                                                chAnexosCInspeccionVehiculoDelictivo.setChecked(true);
                                                spAnexosCInspeccionVehiculoDelictivo.setSelection(funciones.getIndexSpiner(spAnexosCInspeccionVehiculoDelictivo, jsonjObject.getString("NumAnexoVehiculo")));
                                            }

                                            if ((jsonjObject.getString("AnexoArmasObjetos").equals("SI"))) {
                                                chAnexosDInventarioArmasDelictivo.setChecked(true);
                                                spAnexosDInventarioArmasDelictivo.setSelection(funciones.getIndexSpiner(spAnexosDInventarioArmasDelictivo, jsonjObject.getString("NumAnexoArmasObjetos")));
                                            }

                                            if ((jsonjObject.getString("AnexoEntrevista").equals("SI"))) {
                                                chEntrevistasAnexoEDelictivo.setChecked(true);
                                                spEntrevistasAnexoEDelictivo.setSelection(funciones.getIndexSpiner(spEntrevistasAnexoEDelictivo, jsonjObject.getString("NumAnexoEntrevista")));
                                            }

                                            if ((jsonjObject.getString("AnexoLugarIntervencion").equals("SI"))) {
                                                chAnexosFEntregaRecepcionDelictivo.setChecked(true);
                                                spAnexosFEntregaRecepcionDelictivo.setSelection(funciones.getIndexSpiner(spAnexosFEntregaRecepcionDelictivo, jsonjObject.getString("NumAnexoLugarIntervencion")));
                                            }
                                        }

                                        txtFiscaliaAutoridadDelictivo.setText((jsonjObject.getString("IdFiscaliaAutoridad")).equals("null")?"":jsonjObject.getString("IdFiscaliaAutoridad"));
                                        spAdscripcionDelictivo.setSelection(funciones.getIndexSpiner(spAdscripcionDelictivo, jsonjObject.getString("IdAdscripcion")));
                                        spCargoDelictivo.setSelection(funciones.getIndexSpiner(spCargoDelictivo, jsonjObject.getString("IdCargo")));

                                        //Pendiente la Firma
                                        //firmaURLServer=((jsonjObject.getString("UrlFirmaDetenido")).equals("SIN INFORMACION")?"http://189.254.7.167/WebServiceIPH/Firma/SINFIRMA.jpg":jsonjObject.getString("UrlFirmaDetenido"));
                                        //getFirmaFromURL();
                                        //banderaFirma = 1;


                                        //Firma
                                        lblFirmaAutoridadRealizadaDelictivo.setText((jsonjObject.getString("RutaFirma")).equals("null")?"":"FIRMA CORRECTA");
                                        firmaURLServer = (jsonjObject.getString("RutaFirma").equals("null")?"http://189.254.7.167/WebServiceIPH/Firma/SINFIRMA.jpg":jsonjObject.getString("RutaFirma"));
                                        lblFirmaOcultaAutoridadBase64HechosDelictivos.setText(firmaURLServer);
                                        getFirmaFromURL();


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "ERROR AL DESEREALIZAR EL JSON. LLENE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //*************************
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(getContext(), "ERROR AL SOLICITAR INFORMACIÓN SECCIÓN 1, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
        public void getFirmaFromURL(){
            Picasso.get()
                    .load(firmaURLServer)
                    .into(target);
                    }

       @Override
       public void onActivityResult(int requestCode, int resultCode, Intent data) {
           super.onActivityResult(requestCode, resultCode, data);
           try {
               ClipData clipData = data.getClipData();
               int totalimagenes = 0;
               if (gvImagenes.getAdapter() != null)
               {
                   totalimagenes = gvImagenes.getAdapter().getCount();
                   Log.i("TOTALIMAGENES","NO NULO");
               }

               Log.i("TOTALIMAGENES","Total:"+Integer.toString(totalimagenes));

               if (resultCode == Activity.RESULT_OK && requestCode == 111) {
                   if(clipData == null) {

                       if ((totalimagenes + 1 )>6) { Toast.makeText(getContext(), "SOLO SE PERMITEN CARGAR 6 IMAGENES.", Toast.LENGTH_LONG).show();}
                       else {
                           imageUri = data.getData();
                           listaImagenes.add(imageUri);
                           identificador.add(R.drawable.ic_trash);
                       }
                   } else {
                       Toast.makeText(getContext(), "else", Toast.LENGTH_LONG).show();

                           for (int i = 0; i < clipData.getItemCount() ; i++) {
                               if ((totalimagenes + i +1 )>6) { Toast.makeText(getContext(), "SOLO SE PERMITEN CARGAR 6 IMAGENES.", Toast.LENGTH_LONG).show();}
                               else
                               {
                                   listaImagenes.add(clipData.getItemAt(i).getUri());
                                   identificador.add(R.drawable.ic_trash);
                               }
                           }
                   }
               }

               //Log.i("BASE64", ""+listaImagenes.get(0));


               baseAdapter = new GridViewAdapter(getContext(), listaImagenes,identificador);
               gvImagenes.setAdapter(baseAdapter);
             /*  int ancho = 1200;
               int alto = (int)(Math.round(gvImagenes.getAdapter().getCount()/6))*300 + 300  ;
               int contador = baseAdapter.getCount();
               System.out.println(contador);
               Log.i("Respuesta","Alto:" + Integer.toString(alto));

               LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);

               gvImagenes.setLayoutParams(params);
               */

           }
           catch (Exception e)
           {
               Log.i("Respuesta","catch"+e.toString());
               baseAdapter = new GridViewAdapter(   getContext(), listaImagenes,identificador);
               gvImagenes.setAdapter(baseAdapter);
           }
       }



        public String convertirUriToBase64(Bitmap bitmap) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, baos);
            byte[] bytes = baos.toByteArray();
            String encode = Base64.encodeToString(bytes, Base64.DEFAULT);

            return encode;
        }
}