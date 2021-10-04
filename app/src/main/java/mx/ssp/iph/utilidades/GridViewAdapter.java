package mx.ssp.iph.utilidades;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import mx.ssp.iph.R;

public class GridViewAdapter extends BaseAdapter {

    Context context;
    List<Uri> listaImagenes;
    List<Integer> identificador;
    LayoutInflater layoutInflater;
/// CARROS CHIDOS
    public GridViewAdapter(Context context, List<Uri> listaImagenes, List<Integer> identificador) {
        this.context = context;
        this.listaImagenes = listaImagenes;
        this.identificador = identificador;
    }

    @Override
    public int getCount() {
        return listaImagenes.size();
    }

    @Override
    public Object getItem(int i) {
        return listaImagenes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_gv_imagenes, null);
        }


        ImageView ivImagen = view.findViewById(R.id.ivImagen);
        ImageButton ibtnEliminar = view.findViewById(R.id.ibtnEliminar);


        //Log.i("LISTAIENTIFICADOR",Integer.toString(identificador.get(i)));
        ivImagen.setImageURI(listaImagenes.get(i));

        ibtnEliminar.setImageResource(identificador.get(i));

        ibtnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaImagenes.remove(i);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
