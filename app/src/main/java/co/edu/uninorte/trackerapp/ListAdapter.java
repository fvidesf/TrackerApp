package co.edu.uninorte.trackerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fdjvf on 5/14/2017.
 */
public class ListAdapter extends BaseAdapter {

    public ArrayList<User> data;
    private Context context;

    public ListAdapter(ArrayList<User> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override

    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {

        return data.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final User text = data.get(position);

        if (convertView == null) {

            LayoutInflater T = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = T.inflate(R.layout.vendor_row, null);
        }

        TextView nombre = (TextView) convertView.findViewById(R.id.NombreUsuario);
        nombre.setText(text.getUID());

        return convertView;

    }

}