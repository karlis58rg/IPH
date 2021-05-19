package mx.ssp.iph.administrativo.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import mx.ssp.iph.R;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listTitulo;
    private List<Integer> listaColorStatus;

    public CustomExpandableListAdapter(Context context,List<String> listTitulo, List<Integer> listaColorStatus) {
        this.context = context;
        this.listTitulo = listTitulo;
        this.listaColorStatus = listaColorStatus;

    }


    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }




        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        convertView.startAnimation(animation);


        return convertView;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String nombre = (String) getGroup(groupPosition);
        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_group, null);

        }

        //Establece la imagen con flecha arriba o abajo dependiendo si la lista está expandida o no
        ImageView img_selection=(ImageView) convertView.findViewById(R.id.imgExpandAdministrativo);
        int imageResourceId = isExpanded ? R.drawable.ic_expand_less : R.drawable.ic_expand_more;
        img_selection.setImageResource(imageResourceId);


        TextView txtNombre = convertView.findViewById(R.id.lblSecctionAdministrativo);
        ImageView imgIndicadorAdministrativo = convertView.findViewById(R.id.imgIndicadorAdministrativo);
        imgIndicadorAdministrativo.setImageResource(listaColorStatus.get(groupPosition));
        txtNombre.setText(nombre);
        return convertView;
    }
    public void CambiarColor()
    {

    }


    @Override
    public int getGroupCount() {
        return this.listTitulo.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listTitulo.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listTitulo.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
