package com.smilemakers.dashBoard.doctorFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smilemakers.db.AppDatabase
import com.smilemakers.network.MyApi
import com.smilemakers.network.SafeApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDateTime

class DoctorRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun getDoctorData() = apiRequest { api.getDoctorData("") }

}
