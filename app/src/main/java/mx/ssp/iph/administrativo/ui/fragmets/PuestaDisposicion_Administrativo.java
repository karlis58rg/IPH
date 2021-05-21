package mx.ssp.iph.administrativo.ui.fragmets;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.viewModel.PuestaDisposicionAdministrativoViewModel;
import mx.ssp.iph.utilidades.ui.ContenedorFirma;
import mx.ssp.iph.utilidades.ui.Funciones;

public class PuestaDisposicion_Administrativo extends Fragment {

    private PuestaDisposicionAdministrativoViewModel mViewModel;
    private ImageView imgFirmaAutoridadAdministrativo;
    private EditText txtFechaPuestaDisposicionAdministrativo,txthoraPuestaDisposicionAdministrativo;
    Funciones funciones;

    public static PuestaDisposicion_Administrativo newInstance() {
        return new PuestaDisposicion_Administrativo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.puesta_disposicion_administrativo_fragment, container, false);
        funciones = new Funciones();

        imgFirmaAutoridadAdministrativo = (ImageView) view.findViewById(R.id.imgFirmaAutoridadAdministrativo);
        txtFechaPuestaDisposicionAdministrativo = (EditText)view.findViewById(R.id.txtFechaPuestaDisposicionAdministrativo);
        txthoraPuestaDisposicionAdministrativo = (EditText)view.findViewById(R.id.txthoraPuestaDisposicionAdministrativo);

        txtFechaPuestaDisposicionAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaPuestaDisposicionAdministrativo,getContext(),getActivity());
            }
        });
        txthoraPuestaDisposicionAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txthoraPuestaDisposicionAdministrativo,getContext(),getActivity());
            }
        });
        imgFirmaAutoridadAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContenedorFirma dialog = new ContenedorFirma(R.id.lblFirmaAutoridadRealizadaAdministrativo,R.id.lblFirmaOcultaAutoridadBase64);
                dialog.show( getActivity().getSupportFragmentManager(),"Dia");
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PuestaDisposicionAdministrativoViewModel.class);
        // TODO: Use the ViewModel
    }

}