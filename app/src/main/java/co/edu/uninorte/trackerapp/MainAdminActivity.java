package co.edu.uninorte.trackerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainAdminActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    public void onClickGOMAP(View view) {
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }
}
