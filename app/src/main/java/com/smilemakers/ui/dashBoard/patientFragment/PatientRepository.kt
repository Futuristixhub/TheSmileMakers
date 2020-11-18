package com.smilemakers.ui.dashBoard.patientFragment

import androidx.lifecycle.MutableLiveData
import com.smilemakers.data.db.AppDatabase
import com.smilemakers.data.network.MyApi
import com.smilemakers.data.network.SafeApiRequest
import com.smilemakers.data.network.response.PatientResponse
import com.smilemakers.utils.Coroutines

class PatientRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    // suspend fun getPatientData() = apiRequest { api.getPatientData("") }

    private val patients = MutableLiveData<List<Patient>>()

    init {
        patients.observeForever {
            savePatients(it)
        }
    }

    private fun savePatients(patient: List<Patient>) {
        Coroutines.io {
            //  prefs.savelastSavedAt(LocalDateTime.now().toString())
            db.getPatientDao().saveAllPatients(patient)
        }
    }

    /* suspend fun getPatientData(userid: String?): LiveData<List<Patient>> {
         return withContext(Dispatchers.IO) {
             try {
                 val response = apiRequest { api.getPatientData(userid!!) }
                 Log.d("tag","this....ffd...."+response.data.patient_list.size)
                 patients.postValue(response.data.patient_list)
             } catch (e: Exception) {
                 e.printStackTrace()
             }
             db.getPatientDao().getPatients()
         }
     }   */

    suspend fun getPatientData(userid: String?) = apiRequest { api.getPatientData(userid!!) }

    suspend fun addPatient(
        fname: String,
        lname: String,
        gender: String,
        dob: String,
        age: String,
        refid: String,
        refname: String,
        mNo: String,
        altmNo: String,
        location: String,
        adr1: String,
        adr2: String,
        city: String,
        state: String,
        country: String,
        pincode: String,
        image: String
    ): PatientResponse {
        return apiRequest {
            api.addPatient(
                fname,
                lname,
                gender,
                dob,
                age,
                refid,
                refname,
                mNo,
                altmNo,
                location,
                adr1,
                adr2,
                city,
                state,
                country,
                pincode,
                image
            )
        }
    }
}
