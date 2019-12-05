package com.example.android_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android_2.models.CallResult;
import com.example.android_2.utils.Globals;
import com.example.android_2.utils.ReporteService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReporteActivity extends AppCompatActivity {

    private SharedPreferences infoDatos;
    //Variabkes guardadas en sharedPreferences
    String mNombre, mEmail, mTelefono;
    //Variables de los campos de texto
    EditText txtNombre, txtEmail, txtTelefono, txtReporte, txtGeo;
    private ReporteService service;

    /*para geolocalización*/
    private FusedLocationProviderClient provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        infoDatos =getSharedPreferences("misDatos", Context.MODE_PRIVATE);
        mNombre = infoDatos.getString("Nombre","");
        mEmail = infoDatos.getString("Email", "");
        mTelefono = infoDatos.getString("Telefono", "");

        txtNombre = findViewById(R.id.txtNombre);
        txtEmail = findViewById(R.id.txtEmail);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtReporte = findViewById(R.id.txtReporte);
        txtGeo = findViewById(R.id.rptGeo);

        txtNombre.setText(mNombre);
        txtEmail.setText(mEmail);
        txtTelefono.setText(mTelefono);

        service = Globals.getApi().create(ReporteService.class);

        Button btnSend = findViewById(R.id.btnEnvReporte);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatos();
            }
        });


        /*conocer la última localización del teléfono*/
        provider = new FusedLocationProviderClient(this);
        getPermisos();

    }

    private void getUbicacion(){
        provider.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            txtGeo.setText(location.getLatitude()+", "+location.getLongitude());
                        }
                    }
                });
    }

    private void guardarDatos(){
        Call<CallResult> llamadaGuardar = service.agregarReporte(
                txtNombre.getText().toString(),
                txtEmail.getText().toString(),
                txtTelefono.getText().toString(),
                txtReporte.getText().toString());
        llamadaGuardar.enqueue(new Callback<CallResult>() {
            @Override
            public void onResponse(Call<CallResult> call, Response<CallResult> response) {
                if(response.isSuccessful()){
                    CallResult resultado = response.body();
                    if(!resultado.isError()){
                        Toast.makeText(getApplicationContext(), "Se agrego el reporte No: "+resultado.getId(),
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<CallResult> call, Throwable t) {

            }
        });
    }

    private final int PERMISO_USUARIO_LOCALIZACION = 1;
    private void getPermisos(){
        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            String[] permisos = {Manifest.permission.ACCESS_COARSE_LOCATION};

            ActivityCompat.requestPermissions(this, permisos, PERMISO_USUARIO_LOCALIZACION);
        }else{
            //txtGeo.setText("Ya tiene permiso");
            getUbicacion();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        switch (requestCode){
            case PERMISO_USUARIO_LOCALIZACION:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //txtGeo.setText("Si dio permiso");
                    getUbicacion();
                }else{
                    txtGeo.setText("No dio permiso");
                }
            }
        }
    }
}
