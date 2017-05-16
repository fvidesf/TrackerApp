package co.edu.uninorte.trackerapp.AdminApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import co.edu.uninorte.trackerapp.Model.User;
import co.edu.uninorte.trackerapp.R;


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
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.profile_image);
        Uri tem = Uri.parse(text.Imagen);
        Picasso.with(context).load(tem).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                imageDrawable.setCircular(true);
                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                imageView.setImageDrawable(imageDrawable);
            }

            @Override
            public void onError() {

            }
        });
        nombre.setText(text.Name);

        return convertView;

    }

}