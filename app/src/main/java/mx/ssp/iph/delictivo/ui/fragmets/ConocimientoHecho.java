package mx.ssp.iph.delictivo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mx.ssp.iph.R;
import mx.ssp.iph.delictivo.viewModel.ConocimientoHechoViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;

public class ConocimientoHecho extends Fragment {

    private ConocimientoHechoViewModel mViewModel;
    Button btnGuardarConocimientoHecho;
    private EditText txtFechaConocimientoHecho,txtHoraConocimientoHecho,txtFechaArriboLugar,txtHoraArriboLugar;
    private Funciones funciones;

    public static ConocimientoHecho newInstance() {
        return new ConocimientoHecho();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conocimiento_hecho_fragment, container, false);
        /************************************************************************************/
        btnGuardarConocimientoHecho = view.findViewById(R.id.btnGuardarConocimientoHecho);
        txtFechaConocimientoHecho = view.findViewById(R.id.txtFechaConocimientoHecho);
        txtHoraConocimientoHecho = view.findViewById(R.id.txtHoraConocimientoHecho);
        txtFechaArriboLugar = view.findViewById(R.id.txtFechaArriboLugar);
        txtHoraArriboLugar = view.findViewById(R.id.txtHoraArriboLugar);
        funciones = new Funciones();

        funciones.CambiarTituloSeccionesDelictivo("SECCIÓN 3. CONOCIMIENTO DEL HECHO",getContext(),getActivity());


        //***************** FECHA  **************************//
        txtFechaConocimientoHecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaConocimientoHecho,getContext(),getActivity());
            }
        });

        //***************** HORA **************************//
        txtHoraConocimientoHecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txtHoraConocimientoHecho,getContext(),getActivity());
            }
        });

        //***************** FECHA  **************************//
        txtFechaArriboLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaArriboLugar,getContext(),getActivity());
            }
        });

        //***************** HORA **************************//
        txtHoraArriboLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txtHoraArriboLugar,getContext(),getActivity());
            }
        });

        //***************** GUARDAR **************************//
        btnGuardarConocimientoHecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "UN MOMENTO POR FAVOR, ESTO PUEDE TARDAR UNOS SEGUNDOS", Toast.LENGTH_SHORT).show();
            }
        });


        /****************************************************************************************/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConocimientoHechoViewModel.class);
        // TODO: Use the ViewModel
    }

}