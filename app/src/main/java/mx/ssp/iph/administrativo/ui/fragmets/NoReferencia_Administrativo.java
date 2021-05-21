package mx.ssp.iph.administrativo.ui.fragmets;

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

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.viewModel.NoReferencia_Administrativo_ViewModel;
import mx.ssp.iph.utilidades.ui.Funciones;

public class NoReferencia_Administrativo extends Fragment {

    private NoReferencia_Administrativo_ViewModel mViewModel;
    private EditText txtHoraEntregaReferenciaAdministrativo,txtFechaEntregaReferenciaAdministrativo;
    Funciones funciones;


    public static NoReferencia_Administrativo newInstance() {
        return new NoReferencia_Administrativo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.no_referencia_administrativo_fragment, container, false);
        funciones = new Funciones();
        txtFechaEntregaReferenciaAdministrativo =  view.findViewById(R.id.txtFechaEntregaReferenciaAdministrativo);
        txtHoraEntregaReferenciaAdministrativo = (EditText) view.findViewById(R.id.txtHoraEntregaReferenciaAdministrativo);

        //FEcha
        txtFechaEntregaReferenciaAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaEntregaReferenciaAdministrativo,getContext(),getActivity());
            }
        });

        //Hora
        txtHoraEntregaReferenciaAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.Time(R.id.txtHoraEntregaReferenciaAdministrativo,getContext(),getActivity());
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NoReferencia_Administrativo_ViewModel.class);
        // TODO: Use the ViewModel
    }

/*
    public void calendar(Integer idCajadeTextoCalendario){
        Calendar c;
        DatePickerDialog dpd;

        c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EditText CajadeTextoCalendario;
                CajadeTextoCalendario = (EditText) getActivity().findViewById(idCajadeTextoCalendario);
                CajadeTextoCalendario.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },day,month,year);
        dpd.show();
    }
*/

}