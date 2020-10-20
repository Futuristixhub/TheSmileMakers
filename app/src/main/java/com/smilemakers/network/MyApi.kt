package com.smilemakers.network

import com.smilemakers.dashBoard.appointmentFragment.AppointmentData
import com.smilemakers.dashBoard.doctorFragment.DoctorData
import com.smilemakers.dashBoard.patientFragment.PatientData
import com.smilemakers.dashBoard.profile.ProfileData
import com.smilemakers.forgotPassword.ForgotPasswordData
import com.smilemakers.network.response.LoginResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MyApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("profile")
    suspend fun getProfileData() : Response<List<ProfileData>>

    @GET("patient")
    suspend fun getPatientData() : Response<List<PatientData>>

    @GET("doctor")
    suspend fun getDoctorData() : Response<List<DoctorData>>

    @GET("appointment")
    suspend fun getAppointmentData() : Response<List<AppointmentData>>

    @GET("forgotpassword")
    suspend fun getForgotPasswordData() : Response<List<ForgotPasswordData>>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.simplifiedcoding.in/course-apis/mvvm/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}