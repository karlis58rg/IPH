package mx.ssp.iph.utilidades.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.ByteArrayOutputStream;

import mx.ssp.iph.R;
import mx.ssp.iph.utilidades.model.Modelo_ContenedorFirma;

public class ContenedorCroquis extends DialogFragment {
        private Button btnCancelar,btnLimpiar,btnGuardar;
        public TextView lblCroquis,lblCroquisBase64;
        private Integer idlblCroquis,idlblCroquisBase64,IdimgCroquisMiniatura,IdlblCroquisOculto;
        private ImageView imgCroquisMiniatura;
         String Base64Blank = "iVBORw0KGgoAAAANSUhEUgAABBoAAAHCCAYAAABBiaq6AAAABHNCSVQICAgIfAhkiAAABz9JREFUeJztwTEBAAAAwqD1T20JT6AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgL8B2jYAAWdS8F4AAAAASUVORK5CYII=";


    public ContenedorCroquis(Integer idlblCroquis, Integer idlblCroquisBase64, Integer IdimgCroquisMiniatura) {
        this.idlblCroquis = idlblCroquis;
        this.idlblCroquisBase64 = idlblCroquisBase64;
        this.IdimgCroquisMiniatura = IdimgCroquisMiniatura;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contenedor_croquis, container,false);
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
                String encoded = "";
                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                //Log.i("FIRMA", "BASE64: " + encoded);
                //Log.i("FIRMA", "BASE64Blank: " + Base64Blank);
                //Log.i("FIRMA", "BASE64Encode: " + encoded.replaceAll("\n", ""));

                if (Base64Blank.equals(encoded.replace("\n", "")))
                {
                    Toast.makeText(getContext(), "EL CROQUIS NO PUEDE SER EN BLANCO. VUELVA A PULSAR EL BOTÓN NUEVO CROQUIS PARA COMENZAR A DIBUJAR", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }
                else {
                    lienzo.destroyDrawingCache();

                    //Indicador de que el coquis se ha guardado
                    lblCroquis = (TextView) getActivity().findViewById(idlblCroquis);
                    lblCroquis.setText("CROQUIS AGREGADO");

                    //Base 64 o URL oculto
                    lblCroquisBase64 = (TextView) getActivity().findViewById(idlblCroquisBase64);
                    lblCroquisBase64.setText(encoded);

                    //Imagen en Miniatura
                    imgCroquisMiniatura = (ImageView) getActivity().findViewById(IdimgCroquisMiniatura);

                    byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imgCroquisMiniatura.setImageBitmap(decodedByte);

                    //
                    getDialog().dismiss();
                }
            }
        });
        return view;
    }
}
