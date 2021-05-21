package mx.ssp.iph.utilidades.ui;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.io.ByteArrayOutputStream;
import mx.ssp.iph.R;
import mx.ssp.iph.utilidades.model.Modelo_ContenedorFirma;

public class ContenedorFirma extends DialogFragment {
        private Button btnCancelar,btnLimpiar,btnGuardar;
        public TextView lblFirma,lblFirmaBase64;
        private Integer idlblFirma,idlblFirmaBase64;



    public ContenedorFirma(Integer idlblFirma, Integer idlblFirmaBase64) {
        this.idlblFirma = idlblFirma;
        this.idlblFirmaBase64 = idlblFirmaBase64;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contenedor_firma, container,false);
        btnCancelar = view.findViewById(R.id.btnCancelar);
        btnLimpiar = view.findViewById(R.id.btnLimpiar);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        LinearLayout milinear = (LinearLayout) view.findViewById(R.id.lyLienzo);
        Modelo_ContenedorFirma lienzo = new Modelo_ContenedorFirma(getContext());
        milinear.addView(lienzo);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getDialog().dismiss();
            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lienzo.limpiarcanvas();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lienzo.setDrawingCacheEnabled(true);
                Bitmap miimagen;
                miimagen = lienzo.getDrawingCache();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                miimagen.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();

                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                lienzo.destroyDrawingCache();

                lblFirma = (TextView) getActivity().findViewById(idlblFirma);
                lblFirma.setText("FIRMA CORRECTA");

                lblFirmaBase64= (TextView) getActivity().findViewById(idlblFirmaBase64);
                lblFirmaBase64.setText(encoded);

                getDialog().dismiss();
            }
        });
        return view;
    }
}
