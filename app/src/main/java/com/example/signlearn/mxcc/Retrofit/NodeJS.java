package com.example.signlearn.mxcc.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NodeJS {

    @POST("/student/register")
    @FormUrlEncoded
    Observable<String> studentRegister(@Field("Name") String Name, @Field("Roll_No") String Roll_No, @Field("Password") String Password);

    @POST("/student/login")
    @FormUrlEncoded
    Observable<String> studentLogin( @Field("Roll_No") String Roll_No, @Field("Password") String Password);

    @POST("/student/getDetails")
    @FormUrlEncoded
    Observable<String> getStudentDetails(@Field("Roll_No") String Roll_No);


}
