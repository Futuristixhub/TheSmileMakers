package com.smilemakers.data.network

import com.smilemakers.ui.dashBoard.appointmentFragment.AppointmentData
import com.smilemakers.ui.dashBoard.doctorFragment.DoctorData
import com.smilemakers.ui.dashBoard.patientFragment.PatientData
import com.smilemakers.ui.dashBoard.profile.ProfileData
import com.smilemakers.ui.forgotPassword.ForgotPasswordData
import com.smilemakers.data.network.response.DashBoardResponse
import com.smilemakers.data.network.response.LoginResponse
import com.smilemakers.data.network.response.PatientResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

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

    @Multipart
    @POST("add_patient.php")
    suspend fun addPatient(
        @Part("fname") fname: RequestBody,
        @Part("lname") lname: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("age") age: RequestBody,
        @Part("refnum") refnum: RequestBody,
        @Part("refname") refname: RequestBody,
        @Part("mno") mno: RequestBody,
        @Part("altmno") altmno: RequestBody,
        @Part("retarea") retarea: RequestBody,
        @Part("address") address: RequestBody,
        @Part("area") area: RequestBody,
        @Part("city") city: RequestBody,
        @Part("state") state: RequestBody,
        @Part("country") country: RequestBody,
        @Part("postcode") postcode: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("image") name: RequestBody
    ): Response<PatientResponse>

    @FormUrlEncoded
    @POST("add_appointment.php")
    suspend fun addAppointment(
        @Field("patient_id") patient_id: String,
        @Field("ftname") ftname: String,
        @Field("retarea") retarea: String,
        @Field("apptdate") apptdate: String,
        @Field("timee") timee: String,
        @Field("typesoftreatment") typesoftreatment: String,
        @Field("doctor") doctor: String,
        @Field("color") color: String
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
    suspend fun getAppointmentData(
        @Field("userid") userid: String,
        @Field("user_type") user_type: String
    ): Response<AppointmentData>

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