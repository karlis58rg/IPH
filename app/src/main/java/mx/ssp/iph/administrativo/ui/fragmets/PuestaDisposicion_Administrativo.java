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
import android.widget.TextView;

import mx.ssp.iph.R;
import mx.ssp.iph.administrativo.viewModel.PuestaDisposicionAdministrativoViewModel;

public class PuestaDisposicion_Administrativo extends Fragment {

    private PuestaDisposicionAdministrativoViewModel mViewModel;
    EditText txtFechaPuestaDisposicionAdministrativo,txthoraPuestaDisposicionAdministrativo,txtNoExpedienteAdmministrativo,txtPrimerApellidoAdministrativo,txtSegundoApellidoAdministrativo,txtNombresAdministrativo,
            txtInstitucionAdscripcionAdministrativo,txtGradoCargoAdministrativo,txtUnidadDeArriboAdministrativo,txtFiscaliaAutoridadAdministrativo,txtAdscripcionAdministrativo,txtCargoAdministrativo;
    TextView lblFirmaAutoridadRealizadaAdministrativo;
    Button btnGuardarPuestaDisposicioAdministrativo;

    public static PuestaDisposicion_Administrativo newInstance() {
        return new PuestaDisposicion_Administrativo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.puesta_disposicion_administrativo_fragment, container, false);
        //************************************** ACCIONES DE LA VISTA **************************************//
        txtFechaPuestaDisposicionAdministrativo = root.findViewById(R.id.txtFechaPuestaDisposicionAdministrativo);
        txthoraPuestaDisposicionAdministrativo = root.findViewById(R.id.txthoraPuestaDisposicionAdministrativo);
        txtNoExpedienteAdmministrativo = root.findViewById(R.id.txtNoExpedienteAdmministrativo);
        txtPrimerApellidoAdministrativo = root.findViewById(R.id.txtPrimerApellidoAdministrativo);
        txtSegundoApellidoAdministrativo = root.findViewById(R.id.txtSegundoApellidoAdministrativo);
        txtNombresAdministrativo = root.findViewById(R.id.txtNombresAdministrativo);
        txtInstitucionAdscripcionAdministrativo = root.findViewById(R.id.txtInstitucionAdscripcionAdministrativo);
        txtGradoCargoAdministrativo = root.findViewById(R.id.txtGradoCargoAdministrativo);
        txtUnidadDeArriboAdministrativo = root.findViewById(R.id.txtUnidadDeArriboAdministrativo);
        txtFiscaliaAutoridadAdministrativo = root.findViewById(R.id.txtFiscaliaAutoridadAdministrativo);
        txtAdscripcionAdministrativo = root.findViewById(R.id.txtAdscripcionAdministrativo);
        txtCargoAdministrativo = root.findViewById(R.id.txtCargoAdministrativo);
        lblFirmaAutoridadRealizadaAdministrativo  = root.findViewById(R.id.lblFirmaAutoridadRealizadaAdministrativo);
        btnGuardarPuestaDisposicioAdministrativo = root.findViewById(R.id.btnGuardarPuestaDisposicioAdministrativo);

        btnGuardarPuestaDisposicioAdministrativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //****************************************************************************//
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PuestaDisposicionAdministrativoViewModel.class);
        // TODO: Use the ViewModel
    }

}