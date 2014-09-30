package py.fpuna.tesis.qoetest.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import py.fpuna.tesis.qoetest.R;


/**
 * Created by User on 15/09/2014.
 */
public class NavDrawerListAdapter extends BaseAdapter {

    TextView tituloItemView;
    ImageView icono;
    ArrayList<NavDrawerItem> arrayItems;
    LayoutInflater inflator;

    public NavDrawerListAdapter(LayoutInflater inflator,
                                ArrayList<NavDrawerItem> listItem){
        super();
        this.inflator = inflator;
        this.arrayItems = listItem;
    }

    @Override
    public int getCount() {
        return arrayItems.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Fila viewFila;
        if (view == null) {
            viewFila = new Fila();
            NavDrawerItem item = arrayItems.get(i);
            view = inflator.inflate(R.layout.drawer_list_item, null);
            viewFila.tituloItem = (TextView) view
                    .findViewById(R.id.title);
            viewFila.tituloItem.setText(item.getTitle());
            viewFila.icono = (ImageView) view.findViewById(R.id.icon);
            viewFila.icono.setImageResource(item.getIcon());
            view.setTag(viewFila);
        }else{
            viewFila = (Fila) view.getTag();
        }
        return view;
    }

    public static class Fila {
        TextView tituloItem;
        ImageView icono;
    }
}
