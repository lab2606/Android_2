package com.example.android_2.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.android_2.R;
import com.example.android_2.models.Empleado;
import com.example.android_2.utils.Globals;
import com.example.android_2.utils.ReporteService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private Gson gson;
    private TextView txtRes;
    private ReporteService service;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        gson = new Gson();
        final Empleado empleadoObjeto = new Empleado(1,"Juan Perez","Patito");
        final String jsonObjeto = "{ id: 4, nombre: \"Jaime Hernadez\", empresa: \"ACME\" }";

        txtRes = root.findViewById(R.id.txtResult);

        Button toJsonBtn = root.findViewById(R.id.btnJson);
        toJsonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claseJson(empleadoObjeto);
            }
        });

        Button fromJsonBtn = root.findViewById(R.id.btnClase);
        fromJsonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonAClase(jsonObjeto);
            }
        });

        service = Globals.getApi().create(ReporteService.class);

        Button btnLlamada = root.findViewById(R.id.btnUnico);
        btnLlamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEpleado(5);
            }
        });

        return root;
    }

    private Call<Empleado> getEmpleadoCall;

    private void getEpleado(int id){
        getEmpleadoCall = service.getEmpleadoUnico(id);
        getEmpleadoCall.enqueue(new Callback<Empleado>() {
            @Override
            public void onResponse(Call<Empleado> call, Response<Empleado> response) {
                if(response.isSuccessful()){
                    Empleado empResult = response.body();
                    claseJson(empResult);
                }
            }

            @Override
            public void onFailure(Call<Empleado> call, Throwable t) {

            }
        });

    }

    private void claseJson(Empleado empleado) {
        String resultado = gson.toJson(empleado);
        txtRes.setText(resultado);
    }
        private void jsonAClase (String json){
                Empleado empResult = gson.fromJson(json, Empleado.class);
                String resultado = "id: "+empResult.getId();
                resultado += "\nnombre: "+empResult.getNombre();
                resultado += "\nempresa: "+empResult.getEmpresa();
                txtRes.setText(resultado);
                txtRes.setText(resultado);
        }


}