package mx.ssp.iph.administrativo.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.model.CustomExpandableListAdapter;
import mx.ssp.iph.administrativo.ui.fragmets.fragment1;
import mx.ssp.iph.administrativo.ui.fragmets.fragment2;
import mx.ssp.iph.administrativo.ui.fragmets.fragment3;

public class Iph_Administrativo extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private int lastExpandedPosition = -2;
    String miseccion = "NÚMERO DE REFERENCIA";
    Integer miestatus = R.drawable.indicador_amarillo;
    private Button button;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iph_administrativo);

        ActualizarEstatusSecciones();

        button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // miseccion = "Roman";
               //miestatus = R.drawable.indicador_rojo;

                ActualizarEstatusSecciones();

            }
        });


        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if(lastExpandedPosition != -1 && groupPosition != lastExpandedPosition){
                    //Toast.makeText(getBaseContext(),"List click :" + Integer.toString(groupPosition),Toast.LENGTH_SHORT).show();
                    PosicionFragment(groupPosition);
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
    } //onCreate

    //Carga y recarga las secciones de la lista. Nos permitirá cambiar los colores de los indicadores
    private void ActualizarEstatusSecciones() {
        ArrayList<String> listatitulosSeccion;
        ArrayList<Integer> listaColorStatus;

        listatitulosSeccion = new ArrayList<String>(); //Item completo
        listaColorStatus = new ArrayList<Integer>(); //Item completo

        listaColorStatus.add(miestatus);
        listaColorStatus.add(R.drawable.indicador_verde);
        listaColorStatus.add(R.drawable.indicador_amarillo);
        listaColorStatus.add(R.drawable.indicador_verde);
        listaColorStatus.add(R.drawable.indicador_amarillo);
        listaColorStatus.add(R.drawable.indicador_verde);
        listaColorStatus.add(R.drawable.indicador_rojo);


        listatitulosSeccion.add(miseccion);
        listatitulosSeccion.add("SECCIÓN 1. PUESTA A DISPOSICIÓN");
        listatitulosSeccion.add("SECCIÓN 2. DATOS DE LA PROBABLE INFRACCIÓN");
        listatitulosSeccion.add("SECCIÓN 3. LUGAR DE LA INTERVENCIÓN");
        listatitulosSeccion.add("SECCIÓN 4. NARRATIVA DE LOS HECHOS");
        listatitulosSeccion.add("ANEXO A. DETENCIÓN(ES)");
        listatitulosSeccion.add("ANEXO B. DESCRIPCIÓN DE VEHÍCULO");

        this.expandableListView = findViewById(R.id.expandableListViewAdministrativo);
        this.expandableListAdapter = new CustomExpandableListAdapter(this, listatitulosSeccion,listaColorStatus);
        expandableListView.setAdapter(expandableListAdapter);
    }


    //Hace el cambio del Fragmento contenedor al fragmento enviado
    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerAdministrativo, fragment)
                .addToBackStack(null)
                .commit();
    }

    //Determina cual es el fragmento que debe cambiar de acuerdo a la sección seleccionada
    private void PosicionFragment(int posicion) {

        switch (posicion){
            case 0:
                addFragment(new fragment1());
                break;
            case 1:
                addFragment(new fragment2());
                break;
            case 2:
                addFragment(new fragment3());
                break;
            case 3:
                addFragment(new fragment1());
                break;
            case 4:
                addFragment(new fragment2());
                break;
            case 5:
                addFragment(new fragment3());
                break;
            case 6:
                addFragment(new fragment1());
                break;
        }
    }
}
