package co.edu.uninorte.trackerapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import co.edu.uninorte.trackerapp.Model.Position;
import co.edu.uninorte.trackerapp.Model.User;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static TextView fechaInicialEd;
    public static TextView fechaFinalEd;
    public static TextView horaInicialEd;
    public static TextView horaFinalEd;
    public static String fechaI;
    public static String fechaF;
    public static String horaI;
    public static String horaF;
    User muser;
    ArrayList<ArrayList<Position>> rutas;
    private GoogleMap mMap;
    private int _day;
    private int _month;
    private int _year;
    private Context _context;
    private Date fechaini;
    private Date fechafin;
    private SimpleDateFormat sdf;
    private Date temp;
    private String TAG;
    private String usuario;
    private DatabaseReference myUserCollection;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Intent i = getIntent();
        usuario=i.getStringExtra("usuario");

        myUserCollection = FirebaseDatabase.getInstance().getReference("Vendedores");
        myUserCollection.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                muser= (User) dataSnapshot.child(usuario).getValue();
                rutas.add(muser.Route);
                //la ruta siempre está en la posición 0 del arraylist Rutas
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



        fechaInicialEd = (TextView) findViewById(R.id.fechaInicial);
        fechaFinalEd = (TextView) findViewById(R.id.fechaFinal);
        fechaInicialEd.setTag(1);
        fechaFinalEd.setTag(2);
        fechaInicialEd.setOnClickListener(this);
        fechaFinalEd.setOnClickListener(this);

        //hora Picker Dialog
        horaInicialEd = (TextView) findViewById(R.id.horaInicial);
        horaFinalEd = (TextView) findViewById(R.id.horaFinal);

        horaInicialEd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MapsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        horaInicialEd.setText(hourOfDay+":"+minute);
                        horaI=hourOfDay+":"+minute;
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        horaFinalEd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MapsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        horaFinalEd.setText(hourOfDay+":"+minute);
                        horaF=hourOfDay+":"+minute;
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        _day = Calendar.DAY_OF_MONTH;
        _month = Calendar.MONTH;
        _year = Calendar.YEAR;
        fechaI = _day+" "+_month+" "+_year+" ";
        fechaF =fechaI;
        horaI = Calendar.HOUR_OF_DAY - 1 + ":" + Calendar.MINUTE;
        horaF = Calendar.HOUR_OF_DAY + ":" + Calendar.MINUTE;

        fechaInicialEd.setText( _day+"/"+_month+"/"+_year+"");
        fechaFinalEd.setText( _day+"/"+_month+"/"+_year+"");
        horaInicialEd.setText(horaI);
        horaFinalEd.setText(horaF);


        try {
            fechaini = sdf.parse(fechaI+""+horaI);
            fechafin = sdf.parse(fechaF+""+horaF);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        _year = year;
        _month = month;
        _day = dayOfMonth;
        updateDisplay(view);
    }
    private void updateDisplay(View v) {

        switch(TAG){
            case "1":
                MapsActivity.fechaInicialEd.setText(_day+"/"+_month+"/"+_year+"");
                fechaI=_year+" "+_month+" "+_day+" ";
                break;
            case "2":
                MapsActivity.fechaFinalEd.setText(_day+"/"+_month+"/"+_year+"");
                fechaF=_year+" "+_month+" "+_day+" ";
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        TAG= v.getTag().toString();
        DatePickerDialog dialog = new DatePickerDialog(this, this,_year, _month, _day);
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClickFiltrar(View view) {

        try {
                fechaini = sdf.parse(fechaI+""+horaI);
                fechafin = sdf.parse(fechaF+""+horaF);

            if(fechaini.before(fechafin)){





            }else{
                Toast.makeText(MapsActivity.this,"Inserte fechas coherentes",Toast.LENGTH_LONG);
            }



        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(MapsActivity.this,"Inserte fechas coherentes",Toast.LENGTH_LONG);

        }


    }
}
