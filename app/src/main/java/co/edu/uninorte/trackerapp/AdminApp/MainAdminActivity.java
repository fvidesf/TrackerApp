package co.edu.uninorte.trackerapp.AdminApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import co.edu.uninorte.trackerapp.MainActivity;
import co.edu.uninorte.trackerapp.Model.Position;
import co.edu.uninorte.trackerapp.Model.User;
import co.edu.uninorte.trackerapp.R;

public class MainAdminActivity extends AppCompatActivity {
    ListView l;
    int ie = 1;
    ListAdapter ladapter;
    ArrayList<User> myUsers = new ArrayList<>();
    private DatabaseReference UserCollection;
    private String usuario="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        final ImageView admin = (ImageView) findViewById(R.id.ImgAdmin);
        Uri temp = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        Picasso.with(this).load(temp).placeholder(R.drawable.com_facebook_auth_dialog_background).into(admin, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap = ((BitmapDrawable) admin.getDrawable()).getBitmap();
                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                imageDrawable.setCircular(true);
                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                admin.setImageDrawable(imageDrawable);
            }

            @Override
            public void onError() {
                admin.setImageResource(R.drawable.com_facebook_button_background);


            }
        });
        l = (ListView) findViewById(R.id.listView);

        UserCollection = FirebaseDatabase.getInstance().getReference("Vendedores");
        ladapter = new ListAdapter(myUsers, this);
        final GenericTypeIndicator<HashMap<String, Position>> typeIndicator = new GenericTypeIndicator<HashMap<String, Position>>() {
        };

        UserCollection.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String Img = dataSnapshot.child("Imagen").getValue(String.class);
                String Na = dataSnapshot.child("Name").getValue(String.class);
                String UI = dataSnapshot.child("UID").getValue(String.class);
                HashMap<String, Position> teao = dataSnapshot.child("Route").getValue(typeIndicator);
                ArrayList<Position> ty = new ArrayList<Position>(teao.values());
                myUsers.add(new User(Na, Img, UI));
                //      myUsers.add(dataSnapshot.getValue(User.class));

                ladapter.data = myUsers;
                l.setAdapter(ladapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                usuario = myUsers.get(position).UID;
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                i.putExtra("usuario",usuario);
                startActivity(i);

            }
        });


        TextView textView = (TextView) findViewById(R.id.NameAdmin);
        String tem = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toUpperCase();
        textView.setText(tem);

    }


    public void LogOut(View view) {
 /*       ArrayList<Position> temp = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            temp.add(new Position(2D, 2D));
        }
        UserCollection.child("Nombre " + ie).setValue(new User("Nombre " + ie, new Uri("ddsd") {
        }));
        ie++;*/
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(MainAdminActivity.this, MainActivity.class));
                        finish();
                    }
                });

    }

}
