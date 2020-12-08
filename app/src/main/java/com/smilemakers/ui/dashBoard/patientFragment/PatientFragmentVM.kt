package com.smilemakers.ui.dashBoard.patientFragment

import android.app.Application
import android.app.DatePickerDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.loader.content.CursorLoader
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.appointmentFragment.Area
import com.smilemakers.ui.dashBoard.patientFragment.addPatient.AddPatientActivity
import com.smilemakers.ui.dashBoard.patientFragment.addPatient.AddPatientFragment
import com.smilemakers.ui.dashBoard.patientFragment.patientAddress.PatientAddressFragment
import com.smilemakers.utils.*
import kotlinx.coroutines.Job
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class PatientFragmentVM(val repository: PatientRepository, application: Application) : ViewModel() {

    var authListener: PatientListener? = null
    var location: String? = null

    var context = application.applicationContext

    private lateinit var job: Job
    private val _patients = MutableLiveData<List<Patient>>()

    fun getPatients() {
        job = Coroutines.ioThenMain(
            {
                repository.getPatientData(
                    context!!.getData(
                        context!!,
                        context.getString(R.string.user_id)
                    )
                )
            },
            {
                if (it?.status == false) {
                    authListener?.onFailure(it.message)
                } else {
                    Coroutines.io {
                        var lst = it?.data?.patient_list
                        for (i in lst?.indices!!) {
                            val image = context.isImageURL(lst[i].image)
                            if (!image) {
                                lst[i].image = ""
                            }
                        }
                        Coroutines.main {
                            _patients.value = it?.data?.patient_list
                        }
                    }

                }
            }
        )
    }

    val patients: LiveData<List<Patient>>
        get() = _patients

//    val patients by lazyDeferred {
//        repository.getPatientData(context!!.getData(context!!, context.getString(R.string.user_id)))
//    }

    fun onAddPatientClick(view: View) {

//        val transaction = (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
//        transaction.addToBackStack(null)
//        transaction.add(R.id.fl_dash_container, AddPatientFragment.newInstance()!!)
//        transaction.commit()

        Intent(view.context, AddPatientActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            view.context.startActivity(this)
        }
    }

    val fname = MutableLiveData<String>()
    val lname = MutableLiveData<String>()
    val dob = ObservableField<String>("")
    val age = MutableLiveData<String>()
    val refId = MutableLiveData<String>()
    val refName = MutableLiveData<String>()
    val mNumber = MutableLiveData<String>()
    val altmNumber = MutableLiveData<String>()
    val itemPosition = MutableLiveData<Int>()
    var gender: String? = null
    var image: String? = null

    fun onRadioCheckedChanged(radioGroup: RadioGroup?, id: Int) {
        if (id == R.id.rb_female) {
            gender = radioGroup!!.context.getString(R.string.female)
        } else if (id == R.id.rb_male) {
            gender = radioGroup!!.context.getString(R.string.male)
        } else {
            gender = radioGroup!!.context.getString(R.string.other)
        }
    }

    val items = arrayListOf("Bapunagar", "Nikol")

    // selected item
    private val selectItem
        get() = itemPosition.value?.let {
            items.get(it)
        }

    private lateinit var job1: Job
    private val _Adata = MutableLiveData<List<Area>>()

    fun getData() {
        job1 = Coroutines.ioThenMain(
            {
                repository.getData(
                    context!!.getData(context!!, context.getString(R.string.user_id))
                )
            },
            {
                _Adata.value = it?.data?.area
            }
        )
    }


    val adata: LiveData<List<Area>>
        get() = _Adata


    fun onDobClick(view: View) {
        val cldr: Calendar = Calendar.getInstance()
        val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
        val month: Int = cldr.get(Calendar.MONTH)
        val year: Int = cldr.get(Calendar.YEAR)
        var picker: DatePickerDialog = DatePickerDialog(
            view.context,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                dob.set("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
            },
            year,
            month,
            day
        )
        picker.show()
    }

    fun onNextClick(view: View) {
        view.context.hideKeyboard(view)
        if (isValidl(view)) {

            val bundle = Bundle()
            bundle.putString("fname", fname.value)
            bundle.putString("lname", lname.value)
            bundle.putString("gender", gender)
            bundle.putString("dob", dob.get())
            bundle.putString("age", age.value)
            bundle.putString("refid", refId.value)
            bundle.putString("refname", refName.value)
            bundle.putString("mno", mNumber.value)
            bundle.putString("altmno", altmNumber.value)
            bundle.putString("image", image)
            val fragobj = PatientAddressFragment.newInstance()
            fragobj!!.setArguments(bundle)

            val transaction =
                (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.addToBackStack(null)
            transaction.replace(R.id.fl_container, fragobj)
            transaction.commit()
        }
    }

    fun isValidl(view: View): Boolean {

        if (image.isNullOrEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_image))
            return false
        }
        if (fname.value == null || fname.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_fname))
            return false
        }
        if (lname.value == null || lname.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_lname))
            return false
        }
        if (gender.isNullOrEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_gender))
            return false
        }
        if (dob.get() == null || dob.get()?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_dob))
            return false
        }
        if (age.value == null || age.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_age))
            return false
        }
        if (refId.value == null || refId.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_ref_id))
            return false
        }
        if (refName.value == null || refName.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_ref_name))
            return false
        }
        if (mNumber.value == null || mNumber.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_mob_no))
            return false
        }
        if (mNumber.value?.length!! < 10) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.valid_mob_no))
            return false
        }
        if (altmNumber.value == null || altmNumber.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_mob_no))
            return false
        }
        if (altmNumber.value?.length!! < 10) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.valid_mob_no))
            return false
        }

        return true
    }

    val adr1 = MutableLiveData<String>()
    val adr2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val pinCode = MutableLiveData<String>()
    val country = MutableLiveData<String>()

    init {
        //  Places.initialize(mActivity, "AIzaSyBMAD8TRAXSAhr3u9pJ3AGIuKs-vm2qMHw")
        // val placesClient = Places.createClient(mActivity)
    }

    fun onSaveClick(view: View) {
        view.context.hideKeyboard(view)

        if (isValid(view)) {
            authListener!!.onStarted()
            Coroutines.main {
                try {
                    val file = File(Uri.parse(image).path)
                    var requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
                    var filePart =
                        MultipartBody.Part.createFormData("image", file.name, requestBody)

                    val authResponse =
                        repository.addPatient(
                            RequestBody.create(
                                MediaType.parse("text/plain"),
                                context!!.getData(context!!, context.getString(R.string.user_id))
                            ),
                            RequestBody.create(MediaType.parse("text/plain"), fname.value!!),
                            RequestBody.create(MediaType.parse("text/plain"), lname.value!!),
                            RequestBody.create(MediaType.parse("text/plain"), gender!!),
                            RequestBody.create(MediaType.parse("text/plain"), dob.get()!!),
                            RequestBody.create(MediaType.parse("text/plain"), age.value!!),
                            RequestBody.create(MediaType.parse("text/plain"), refId.value!!),
                            RequestBody.create(MediaType.parse("text/plain"), refName.value!!),
                            RequestBody.create(MediaType.parse("text/plain"), mNumber.value!!),
                            RequestBody.create(MediaType.parse("text/plain"), altmNumber.value!!),
                            RequestBody.create(MediaType.parse("text/plain"), location!!),
                            RequestBody.create(MediaType.parse("text/plain"), adr1.value!!),
                            RequestBody.create(MediaType.parse("text/plain"), adr2.value!!),
                            RequestBody.create(MediaType.parse("text/plain"), city.value!!),
                            RequestBody.create(MediaType.parse("text/plain"), state.value!!),
                            RequestBody.create(MediaType.parse("text/plain"), country.value!!),
                            RequestBody.create(MediaType.parse("text/plain"), pinCode.value!!),
                            filePart, requestBody
                        )
                    if (authResponse.status == false) {
                        authListener?.onFailure(authResponse.message!!)
                    } else {
                        authListener?.onSuccess(authResponse.message!!)
                        return@main
                    }
                    authListener?.onFailure(authResponse.message!!)
                } catch (e: ApiExceptions) {
                    authListener?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    authListener?.onFailure(e.message!!)
                }
            }
        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context!!, contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result: String = cursor.getString(column_index)
        cursor.close()
        return result
    }

    fun onPreviousClick(view: View) {
        view.context.hideKeyboard(view)
        val manager: FragmentManager =
            (view.context as AppCompatActivity).getSupportFragmentManager()
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.remove(AddPatientFragment.newInstance()!!)
        transaction.commit()
        manager.popBackStack()
    }

    fun onAddressFieldClick(view: View) {
        view.context.hideKeyboard(view)
        val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, fields
        )
            .build(view.context)
        (view.context as AppCompatActivity).startActivityForResult(intent, 1223)
    }

    fun isValid(view: View): Boolean {

        if (adr1.value == null || adr1.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_adr))
            return false
        }
        if (adr2.value == null || adr2.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_adr))
            return false
        }
        if (city.value == null || city.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_city))
            return false
        }
        if (state.value == null || state.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_state))
            return false
        }
        if (pinCode.value == null || pinCode.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_pincode))
            return false
        }
        if (country.value == null || country.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_country))
            return false
        }

        return true
    }

}