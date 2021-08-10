package mx.ssp.iph.delictivo.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import mx.ssp.iph.Login;
import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.ui.activitys.Iph_Administrativo_Up;
import mx.ssp.iph.administrativo.ui.fragmets.DescripcionVehiculo;
import mx.ssp.iph.delictivo.ui.fragmets.ConocimientoHecho;
import mx.ssp.iph.delictivo.ui.fragmets.DescripcionVehiculoDelictivo;
import mx.ssp.iph.delictivo.ui.fragmets.Detenciones_Delictivo;
import mx.ssp.iph.delictivo.ui.fragmets.EntregaRecepcion_Delictivo;
import mx.ssp.iph.delictivo.ui.fragmets.EntrevistasDelictivo;
import mx.ssp.iph.delictivo.ui.fragmets.Fragment_ContainerBlank_Delictivo;
import mx.ssp.iph.delictivo.ui.fragmets.HechosDelictivos;
import mx.ssp.iph.delictivo.ui.fragmets.InformeUsoFuerza_Delictivo;
import mx.ssp.iph.delictivo.ui.fragmets.InventarioArmasObjetos;
import mx.ssp.iph.delictivo.ui.fragmets.LugarDeIntervencion_Delictivo;
import mx.ssp.iph.delictivo.ui.fragmets.NarrativaHechos_Delictivo;
import mx.ssp.iph.delictivo.ui.fragmets.PrimerRespondiente;
import mx.ssp.iph.principal.ui.activitys.Principal;

import static android.content.Context.MODE_PRIVATE;

public class Iph_Delictivo_Up extends AppCompatActivity{

    ArrayList<String> listaSeccionesDelictivo;
    ArrayList<Integer> listaColorStatusDelictivo;
    ListView lvSeccionesDelictivo;
    Fragment seccion1,seccion2,seccion3,seccion4,seccion5,anexoa,anexob,anexoc,anexod,anexoe,anexof;
    ImageView imgbtnCerrarSesionDelictivo;
    SharedPreferences share;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_iph_delictivo_up);

        imgbtnCerrarSesionDelictivo = findViewById(R.id.imgbtnCerrarSesionDelictivo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDelictivo);
        setSupportActionBar(toolbar);

        //Coloca Flecha atrás al toolbar

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Instancio los Fragmentos
       // referencia = new HechosDelictivos();
        seccion1 = new HechosDelictivos();
        seccion2 = new PrimerRespondiente();
        seccion3 = new ConocimientoHecho();
        seccion4 = new LugarDeIntervencion_Delictivo();
        seccion5 = new NarrativaHechos_Delictivo();
        anexoa = new Detenciones_Delictivo();
        anexob  = new InformeUsoFuerza_Delictivo();
        anexoc  = new DescripcionVehiculoDelictivo();
        anexod  = new InventarioArmasObjetos();
        anexoe  = new EntrevistasDelictivo();
        anexof  = new EntregaRecepcion_Delictivo();



        //Instancio las listas
        listaSeccionesDelictivo = new ArrayList<String>(); //Item completo
        listaColorStatusDelictivo = new ArrayList<Integer>(); //Item completo
        lvSeccionesDelictivo = findViewById(R.id.lvSeccionesDelictivo);

        //Agrego los colores de las listas
        listaColorStatusDelictivo.add(R.drawable.indicador_amarillo);
        listaColorStatusDelictivo.add(R.drawable.indicador_amarillo);
        listaColorStatusDelictivo.add(R.drawable.indicador_amarillo);
        listaColorStatusDelictivo.add(R.drawable.indicador_amarillo);
        listaColorStatusDelictivo.add(R.drawable.indicador_amarillo);
        listaColorStatusDelictivo.add(R.drawable.indicador_amarillo);
        listaColorStatusDelictivo.add(R.drawable.indicador_amarillo);
        listaColorStatusDelictivo.add(R.drawable.indicador_amarillo);
        listaColorStatusDelictivo.add(R.drawable.indicador_amarillo);
        listaColorStatusDelictivo.add(R.drawable.indicador_amarillo);
        listaColorStatusDelictivo.add(R.drawable.indicador_amarillo);


        //Agrego las secciones a la lista
        listaSeccionesDelictivo.add("SECCIÓN 1. PUESTA A DISPOSICIÓN");
        listaSeccionesDelictivo.add("SECCIÓN 2. PRIMER RESPONDIENTE");
        listaSeccionesDelictivo.add("SECCIÓN 3. CONOCIMIENTO DEL HECHO");
        listaSeccionesDelictivo.add("SECCIÓN 4. LUGAR DE LA INTERVENCIÓN");
        listaSeccionesDelictivo.add("SECCIÓN 5. NARRATIVA DE LOS HECHOS");
        listaSeccionesDelictivo.add("ANEXO A. DETENCIÓN(ES)");
        listaSeccionesDelictivo.add("ANEXO B. INFORME DEL USO DE LA FUERZA");
        listaSeccionesDelictivo.add("ANEXO C. INSPECCIÓN DE VEHÍCULO");
        listaSeccionesDelictivo.add("ANEXO D. INVENTARIO DE ARMAS Y OBJETOS");
        listaSeccionesDelictivo.add("ANEXO E. ENTREVISTAS");
        listaSeccionesDelictivo.add("ANEXO F. ENTREGA-RECEPCIÓN DEL LUGAR DE LA INTERVENSIÓN");

        //Lleno la lista con la clase adaptador
        final SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        ActualizarListaEsttus();
        addFragment(seccion1);

        imgbtnCerrarSesionDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guarda el usaurio con espacio en blanco
                guardarUsuario();

                //Redirecciona al Login
                Intent intent = new Intent(Iph_Delictivo_Up.this, Login.class);
                startActivity(intent);

            }
        });


        //clisk de los elementos de las listas para cambiar de fragmentos
        lvSeccionesDelictivo.setClickable(true);
        lvSeccionesDelictivo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                switch (position)
                {
                    case 0:
                    {
                        addFragment(seccion1);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 1:
                    {
                        addFragment(seccion2);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 2:
                    {
                        addFragment(seccion3);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 3:
                    {
                        addFragment(seccion4);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 4:
                    {
                        addFragment(seccion5);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 5:
                    {
                        addFragment(anexoa);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 6:
                    {
                        addFragment(anexob);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 7:
                    {
                        addFragment(anexoc);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 8:
                    {
                        addFragment(anexod);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 9:
                    {
                        addFragment(anexoe);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 10:
                    {
                        addFragment(anexof);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                }
            }
        });
    }

    //Desactivar el botón Atrás en android
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Iph_Delictivo_Up.this, Principal.class);
        startActivity(intent);
    }

    //Da la acción de volver atrás en Flecha Atrás del toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    //Instancia el Adptador para recrear la lista de secciones. El menú de secciones.
    public void ActualizarListaEsttus(){
        MyAdapter adapter = new MyAdapter(Iph_Delictivo_Up.this, listaSeccionesDelictivo,listaColorStatusDelictivo);
        lvSeccionesDelictivo.setAdapter(adapter);
    }

    //Remplaza el fragmento
    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContenedorDelictivo, fragment)
                //.addToBackStack(null)//Esta línea Evita pila de fragments
                .commit();
    }

    //Clase adaptador para llenar la lista con el row que se creó
    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> titulo;
        ArrayList<Integer> StatusColor;

        MyAdapter (Context c, ArrayList<String> titulo,ArrayList<Integer> StatusColor) {
            super(c, R.layout.row_secciones_administrativo, R.id.txv_mititulo, titulo);
            this.context = c;
            this.titulo = titulo;
            this.StatusColor = StatusColor;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_secciones_administrativo, parent, false);

            TextView txv_tituloSeccion = row.findViewById(R.id.txv_mititulo);
            ImageView imageView = row.findViewById(R.id.imageView);

            // now set our resources on views
            txv_tituloSeccion.setText(titulo.get(position));
            imageView.setImageResource(StatusColor.get(position));

            return row;
        }
    }
        private void guardarUsuario() {
            share = getSharedPreferences("main", MODE_PRIVATE);
            editor = share.edit();
            editor.putString("Usuario", "" );
            editor.commit();
        }
}
