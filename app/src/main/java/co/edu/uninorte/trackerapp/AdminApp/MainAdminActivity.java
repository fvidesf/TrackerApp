package co.edu.uninorte.trackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
        l = (ListView) findViewById(R.id.listView);
        UserCollection = FirebaseDatabase.getInstance().getReference("Vendedores");
        ladapter = new ListAdapter(myUsers, this);
        UserCollection.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                myUsers.add(dataSnapshot.getValue(User.class));

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

                usuario=myUsers.get(position).getUID();
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                i.putExtra("usuario",usuario);
                startActivity(i);

            }
        });
    }


    public void LogOut(View view) {
        ArrayList<Position> temp = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            temp.add(new Position(2D, 2D));
        }
        UserCollection.child("Nombre " + ie).setValue(new User("Nombre " + ie, temp));
        ie++;
     /*   AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(MainAdminActivity.this, MainActivity.class));
                        finish();
                    }
                });

    }*/
    }
}
