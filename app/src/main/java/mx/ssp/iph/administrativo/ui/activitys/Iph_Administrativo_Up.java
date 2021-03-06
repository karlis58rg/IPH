package mx.ssp.iph.administrativo.ui.activitys;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.ui.fragmets.DescripcionVehiculo;
import mx.ssp.iph.administrativo.ui.fragmets.Detenciones;
import mx.ssp.iph.administrativo.ui.fragmets.LugarDeIntervencion;
import mx.ssp.iph.administrativo.ui.fragmets.NarrativaHechos;
import mx.ssp.iph.administrativo.ui.fragmets.NoReferencia_Administrativo;
import mx.ssp.iph.administrativo.ui.fragmets.ProbableInfraccion;
import mx.ssp.iph.administrativo.ui.fragmets.PuestaDisposicion_Administrativo;

public class Iph_Administrativo_Up extends AppCompatActivity{

    ArrayList<String> listaSecciones;
    ArrayList<Integer> listaColorStatus;
    ListView lvSeccionesAdministrativo;
    Fragment referencia,seccion1,seccion2,seccion3,seccion4,anexoa,anexob;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_iph_administrativo_up);


        //Instancio los Fragmentos
        referencia = new NoReferencia_Administrativo();
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
        listaColorStatus.add(R.drawable.indicador_verde);
        listaColorStatus.add(R.drawable.indicador_amarillo);
        listaColorStatus.add(R.drawable.indicador_amarillo);
        listaColorStatus.add(R.drawable.indicador_amarillo);
        listaColorStatus.add(R.drawable.indicador_amarillo);

        //Agrego las secciones a la lista
        listaSecciones.add("N??MERO DE REFERENCIA");
        listaSecciones.add("SECCI??N 1. PUESTA A DISPOSICI??N");
        listaSecciones.add("SECCI??N 2. DATOS DE LA PROBABLE INFRACCI??N");
        listaSecciones.add("SECCI??N 3. LUGAR DE LA INTERVENCI??N");
        listaSecciones.add("SECCI??N 4. NARRATIVA DE LOS HECHOS");
        listaSecciones.add("ANEXO A. DETENCI??N(ES)");
        listaSecciones.add("ANEXO B. DESCRIPCI??N DE VEH??CULO");

        //Lleno la lista con la clase adaptador
        final SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        ActualizarListaEsttus();
        addFragment(referencia);


        //clisk de los elementos de las listas para cambiar de fragmentos
        lvSeccionesAdministrativo.setClickable(true);
        lvSeccionesAdministrativo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                switch (position)
                {
                    case 0:
                    {
                        addFragment(referencia);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 1:
                    {
                        addFragment(seccion1);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 2:
                    {
                        addFragment(seccion2);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 3:
                    {
                        addFragment(seccion3);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        break;
                    }
                    case 4:
                    {
                        addFragment(seccion4);
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
                }
            }
        });
    }

    //Instancia el Adptador para recrear la lista de secciones. El men?? de secciones.
    public void ActualizarListaEsttus(){
        MyAdapter adapter = new MyAdapter(Iph_Administrativo_Up.this, listaSecciones,listaColorStatus);
        lvSeccionesAdministrativo.setAdapter(adapter);
    }

    //Remplaza el fragmento
    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContenedorJusticiacivica, fragment)
                //.addToBackStack(null)//Esta l??nea Evita pila de fragments
                .commit();
    }

    //Clase adaptador para llenar la lista con el row que se cre??
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
}
