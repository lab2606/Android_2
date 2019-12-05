package com.example.android_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android_2.models.CallResult;
import com.example.android_2.models.LoginResult;
import com.example.android_2.utils.Globals;
import com.example.android_2.utils.ReporteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText txtUsername, txtPassword;
    Button btnLogin;
    private ReporteService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.user);
        txtPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);

        service = Globals.getApi().create(ReporteService.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*LLAMAR FUNCION PARA LOGEARSE*/
                verificaLogin();
            }
        });
    }

    private void verificaLogin(){
        Call<LoginResult> llamadaLogin = service.login(
                txtUsername.getText().toString(),
                txtPassword.getText().toString());
        llamadaLogin.enqueue(new  Callback<LoginResult>(){
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if(response.isSuccessful()){
                    LoginResult resultado = response.body();
                    if(!resultado.isError()){
                        Toast.makeText(getApplicationContext(), "Acceso correcto",
                                Toast.LENGTH_LONG).show();
                        /*LLAMA A MAINACTIVITY*/
                        Intent intent =new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                /*FALLA DE CONEXION*/
            }


        });
    }
}
