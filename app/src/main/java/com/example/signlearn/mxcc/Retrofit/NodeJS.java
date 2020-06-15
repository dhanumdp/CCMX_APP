package com.example.signlearn.mxcc.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NodeJS {

    @POST("/student/register")
    @FormUrlEncoded
    Observable<String> studentRegister(@Field("Name") String Name, @Field("Roll_No") String Roll_No, @Field("email") String email, @Field("Password") String Password);

    @POST("/student/login")
    @FormUrlEncoded
    Observable<String> studentLogin( @Field("Roll_No") String Roll_No, @Field("Password") String Password);

    @POST("/student/getDetails")
    @FormUrlEncoded
    Observable<String> getStudentDetails(@Field("Roll_No") String Roll_No);

    @POST("/student/getAttendance")
    @FormUrlEncoded
    Observable<String> getAttendance( @Field("date") String dateNow, @Field("rollno") String rollno);

    @POST("/student/putAttendance")
    @FormUrlEncoded
    Observable<String> putAttendance( @Field("date") String dateNow, @Field("rollno") String rollno,@Field("reason") String reason, @Field("time") String timeNow);

    @POST("/student/viewComplaints")
    @FormUrlEncoded
    Observable<String> viewComplaintStud(@Field("rollno") String rollno);

    @POST("/student/complaint")
    @FormUrlEncoded
    Observable<String> complaint (@Field("date") String dateNow, @Field("rollno") String rollno, @Field("systemNo") String systemno, @Field("complaint") String complaint, @Field("status") String status);

    @POST("/forgotpassword/student")
    @FormUrlEncoded
    Observable<String> getCode(@Field("rollno") String rollno, @Field("email")String email);


    @POST("/forgotpassword/student/changePassword")
    @FormUrlEncoded
    Observable<String> changePassword(@Field("rollno")String rollno, @Field("password") String password);

    @POST("/student/changePassword")
    @FormUrlEncoded
    Observable<String> newPassword(@Field("rollno")String rollno, @Field("password") String password);


}
