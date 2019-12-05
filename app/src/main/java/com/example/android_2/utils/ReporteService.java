package com.example.android_2.utils;

import com.example.android_2.models.CallResult;
import com.example.android_2.models.Empleado;
import com.example.android_2.models.LoginResult;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReporteService {
    //http://isantos.com/cursoAndroid/getEmpleado.php?id=5
    @GET("getEmpleado.php")
    Call<Empleado> getEmpleadoUnico(@Query("id") int idEmpleado);

    //http://isantos.com/cursoAndroid/getAll.php
    @GET("getAll.php")
    Call<List<Empleado>> getTodos();

    @FormUrlEncoded
    @POST("addReporte.php")
    Call<CallResult> agregarReporte(@Field("nombre")String nombre,
                                    @Field("email")String email,
                                    @Field("telefono")String telefono,
                                    @Field("reporte")String reporte);

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResult> login(@Field("username")String usuario,
                            @Field("password")String password);
}
