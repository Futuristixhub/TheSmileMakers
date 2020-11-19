package com.smilemakers.data.network

import com.smilemakers.ui.dashBoard.appointmentFragment.AppointmentData
import com.smilemakers.ui.dashBoard.doctorFragment.DoctorData
import com.smilemakers.ui.dashBoard.patientFragment.PatientData
import com.smilemakers.ui.dashBoard.profile.ProfileData
import com.smilemakers.ui.forgotPassword.ForgotPasswordData
import com.smilemakers.data.network.response.DashBoardResponse
import com.smilemakers.data.network.response.LoginResponse
import com.smilemakers.data.network.response.PatientResponse
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
    @POST("login.php")
    suspend fun userLogin(
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("user_type") user_type: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("dashboard_count.php")
    suspend fun dashboardCount(
        @Field("userid") userid: String
    ): Response<DashBoardResponse>

    @FormUrlEncoded
    @POST("add_patient.php")
    suspend fun addPatient(
        @Field("fname") fname: String,
        @Field("lname") lname: String,
        @Field("gender") gender: String,
        @Field("dob") dob: String,
        @Field("age") age: String,
        @Field("refnum") refnum: String,
        @Field("refname") refname: String,
        @Field("mno") mno: String,
        @Field("altmno") altmno: String,
        @Field("retarea") retarea: String,
        @Field("address") address: String,
        @Field("area") area: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("country") country: String,
        @Field("postcode") postcode: String,
        @Field("image") image: String
    ): Response<PatientResponse>

    @FormUrlEncoded
    @POST("profile")
    suspend fun getProfileData(@Field("userid") userid: String): Response<List<ProfileData>>

    @FormUrlEncoded
    @POST("patient_list.php")
    suspend fun getPatientData(@Field("userid") userid: String): Response<PatientData>

    @FormUrlEncoded
    @POST("doctor_list.php")
    suspend fun getDoctorData(@Field("userid") userid: String): Response<DoctorData>

    @FormUrlEncoded
    @POST("appointment_list.php")
    suspend fun getAppointmentData(@Field("userid") userid: String, @Field("user_type") user_type: String): Response<AppointmentData>

    @GET("forgotpassword")
    suspend fun getForgotPasswordData(): Response<List<ForgotPasswordData>>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://thesmilemakers.in/thesmilemakers/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}