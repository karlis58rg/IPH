package mx.ssp.iph.administrativo.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import mx.ssp.iph.administrativo.ui.fragmets.DescripcionVehiculo;
import mx.ssp.iph.administrativo.ui.fragmets.Detenciones;
import mx.ssp.iph.administrativo.ui.fragmets.LugarDeIntervencion;
import mx.ssp.iph.administrativo.ui.fragmets.NarrativaHechos;
import mx.ssp.iph.administrativo.ui.fragmets.NoReferencia_Administrativo;
import mx.ssp.iph.administrativo.ui.fragmets.ProbableInfraccion;
import mx.ssp.iph.administrativo.ui.fragmets.PuestaDisposicion_Administrativo;
import mx.ssp.iph.delictivo.ui.activitys.Iph_Delictivo_Up;
import mx.ssp.iph.principal.ui.activitys.Principal;

public class Iph_Administrativo_Up extends AppCompatActivity{

    ArrayList<String> listaSecciones;
    ArrayList<Integer> listaColorStatus;
    ListView lvSeccionesAdministrativo;
    Fragment seccion1,seccion2,seccion3,seccion4,anexoa,anexob; //referencia,
    ImageView imgbtnCerrarSesion;
    SharedPreferences share;
    SharedPreferences.Editor editor;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_cerrar_sesion,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item_btnCerrarSession:
                //Guarda el usaurio con espacio en blanco
                guardarUsuario();

                //Redirecciona al Login
                Intent intent = new Intent(Iph_Administrativo_Up.this, Login.class);
                startActivity(intent);
                break;
            default:
                //Hacer algo cuando por default
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_iph_administrativo_up);

        imgbtnCerrarSesion = findViewById(R.id.imgbtnCerrarSesion);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAdministrativo);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Coloca Flecha atrás al toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);


        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_cerrar_sesion));


        //Instancio los Fragmentos
        //referencia = new NoReferencia_Administrativo();
        seccion1 = new PuestaDisposicion_Administrativo();
        seccion2 = new ProbableInfraccion();
        seccion3 = new LugarDeIntervencion();
        seccion4 = new NarrativaHechos();
        anexoa = new Detenciones();
        anexob  = new DescripcionVehiculo();

        //Instancio las listas
        listaSecciones = new ArrayList<String>(); //Item completo
        listaColorStatus = new ArrayList<Integer>(); //Item completo
        lvSeccionesAdministrativo = findViewById(R.id.lvSeccionesAdministrativo);

        //Agrego los colores de las listas
        listaColorStatus.add(R.drawable.indicador_amarillo);
        listaColorStatus.add(R.drawable.indicador_amarillo);
        listaColorStatus.add(R.drawable.indicador_amarillo);
        listaColorStatus.add(R.drawable.indicador_amarillo);
        listaColorStatus.add(R.drawable.indicador_amarillo);
        listaColorStatus.add(R.drawable.indicador_amarillo);
        listaColorStatus.add(R.drawable.indicador_amarillo);

        //Agrego las secciones a la lista
        //listaSecciones.add("NÚMERO DE REFERENCIA");
        listaSecciones.add("SECCIÓN 1. PUESTA A DISPOSICIÓN");
        listaSecciones.add("SECCIÓN 2. DATOS DE LA PROBABLE INFRACCIÓN");
        listaSecciones.add("SECCIÓN 3. LUGAR DE LA INTERVENCIÓN");
        listaSecciones.add("SECCIÓN 4. NARRATIVA DE LOS HECHOS");
        listaSecciones.add("ANEXO A. DETENCIÓN(ES)");
        listaSecciones.add("ANEXO B. DESCRIPCIÓN DE VEHÍCULO");

        //Lleno la lista con la clase adaptador
        final SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        ActualizarListaEsttus();
        addFragment(seccion1);


        //clisk de los elementos de las listas para cambiar de fragmentos
        lvSeccionesAdministrativo.setClickable(true);
        lvSeccionesAdministrativo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                switch (position)
                {
                    /*case 0:
                    {
                        addFragment(referencia);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }*/
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
                        addFragment(anexoa);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 5:
                    {
                        addFragment(anexob);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                }
            }
        });

        imgbtnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guarda el usaurio con espacio en blanco
                guardarUsuario();

                //Redirecciona al Login
                Intent intent = new Intent(Iph_Administrativo_Up.this, Login.class);
                startActivity(intent);

            }
        });
    }

    //Desactivar el botón Atrás en android
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Iph_Administrativo_Up.this, Principal.class);
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
        MyAdapter adapter = new MyAdapter(Iph_Administrativo_Up.this, listaSecciones,listaColorStatus);
        lvSeccionesAdministrativo.setAdapter(adapter);
    }

    //Remplaza el fragmento
    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContenedorJusticiacivica, fragment)
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
