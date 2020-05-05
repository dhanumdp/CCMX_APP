package com.example.signlearn.mxcc.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Retrofit instance;

    public  static  Retrofit getInstance(){

        if(instance == null)
        {
           instance= new Retrofit.Builder()
                   .baseUrl("https://mxcc.azurewebsites.net/") //in emulator, localhost will change to 10.0.2.2
                   .addConverterFactory(ScalarsConverterFactory.create())
                   .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                   .build();


        }
        return  instance;
    }

}

