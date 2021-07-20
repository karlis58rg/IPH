package mx.ssp.iph.delictivo.ui.fragmets;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import mx.ssp.iph.R;
import mx.ssp.iph.delictivo.viewModel.LugarDeIntervencionDelictivoViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.ContenedorMaps;
import mx.ssp.iph.utilidades.ui.Funciones;

import static android.content.Context.MODE_PRIVATE;

public class LugarDeIntervencion_Delictivo extends Fragment {

    private LugarDeIntervencionDelictivoViewModel mViewModel;
    Button btnGuardarLugarIntervencionDelictivo;
    private ImageView imgMapDelictivo,imgCroquisDelictivo;
    final private int REQUEST_CODE_ASK_PERMISSION = 111;
    private Funciones funciones;
    SharedPreferences share;
    SharedPreferences.Editor editor;

    public static LugarDeIntervencion_Delictivo newInstance() {
        return new LugarDeIntervencion_Delictivo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lugar_de_intervencion_delictivo_fragment, container, false);
        /*******************************************************************************/
        btnGuardarLugarIntervencionDelictivo = view.findViewById(R.id.btnGuardarLugarIntervencionDelictivo);
        imgMapDelictivo = view.findViewById(R.id.imgMapDelictivo);
        imgCroquisDelictivo = view.findViewById(R.id.imgCroquisDelictivo);
        funciones = new Funciones();

        funciones.CambiarTituloSeccionesDelictivo("SECCIÓN 4. LUGAR DE LA INTERVENCIÓN",getContext(),getActivity());


        //***************** BOTÓN ABRIR FRAGMENT MAPS  **************************//
        imgMapDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permisoubicacion = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (permisoubicacion != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSION);
                        Toast.makeText(getContext(),"POR FAVOR ACTIVA LOS PERMISOS DE UBICACIÓN",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    CargarBANDERAMAPA("DELICTIVO");
                    ContenedorMaps dialog = new ContenedorMaps();
                    dialog.show( getActivity().getSupportFragmentManager(),"Maps");
                }
            }
        });

        //***************** FIRMA **************************//
        imgCroquisDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirma dialog = new ContenedorFirma(R.id.lblCroquisDelictivo,R.id.lblCroquisDelictivoOculto,R.id.imgMapDelictivoMiniatura);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });

        btnGuardarLugarIntervencionDelictivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
            }
        });





        /********************************************************************************/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LugarDeIntervencionDelictivoViewModel.class);
        // TODO: Use the ViewModel
    }

    //Coloca una bandera en sharedpref para indicar si el mapa se está abriendo de delictivo y no administrativo
    public void CargarBANDERAMAPA(String Bandera){
        share = getActivity().getSharedPreferences("main", MODE_PRIVATE);
        editor = share.edit();
        editor.putString("BANDERAMAPA", Bandera );
        editor.commit();
    }

}