package com.smilemakers.dashBoard.patientFragment

import android.app.DatePickerDialog
import android.content.Intent
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.smilemakers.R
import com.smilemakers.dashBoard.DashBoardListener
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.doctorFragment.addDoctor.AddDoctorActivity
import com.smilemakers.dashBoard.patientFragment.addPatient.AddPatientActivity
import com.smilemakers.dashBoard.patientFragment.addPatient.AddPatientFragment
import com.smilemakers.dashBoard.patientFragment.patientAddress.PatientAddressFragment
import com.smilemakers.login.AuthListener
import com.smilemakers.utils.*
import java.util.*
import kotlin.collections.ArrayList


class PatientFragmentVM(val repository: PatientRepository) : ViewModel() {

    val patientList = ArrayList<PatientPOJO>()
    var authListener: PatientListener? = null
    var location: String? = null


    init {
        newPatientList()
    }

    fun createList(recView: RecyclerView) {
        val viewManager = LinearLayoutManager(recView.context)
        val mAdapter = PatientListAdapter(recView.context, patientList)
        recView.apply {
            setHasFixedSize(true)
            //   layoutManager = viewManager as RecyclerView.LayoutManager?
            adapter = mAdapter
        }
    }

    fun newPatientList() {
        patientList.add(
            PatientPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "54234",
                "21",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "56830",
                "21",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "54234",
                "23",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "56830",
                "23",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "54234",
                "33",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "56830",
                "33",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "54234",
                "11",
                "Bapunagar"
            )
        )
        patientList.add(
            PatientPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "56830",
                "11",
                "Bapunagar"
            )
        )
    }

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
    val dob = MutableLiveData<String>()
    val age = MutableLiveData<String>()
    val refId = MutableLiveData<String>()
    val refName = MutableLiveData<String>()
    val mNumber = MutableLiveData<String>()
    val itemPosition = MutableLiveData<Int>()
    var gender: String? = null

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

    fun onDobClick(view: View) {
        val cldr: Calendar = Calendar.getInstance()
        val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
        val month: Int = cldr.get(Calendar.MONTH)
        val year: Int = cldr.get(Calendar.YEAR)
        var picker: DatePickerDialog = DatePickerDialog(
            view.context,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                dob.value = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
            },
            year,
            month,
            day
        )
        picker.show()
    }

    fun onNextClick(view: View) {
        view.context.hideKeyboard(view)
        //if (isValidl(view)) {
        val transaction =
            (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
        //  transaction.addToBackStack(null)
        transaction.replace(R.id.fl_container, PatientAddressFragment.newInstance()!!)
        transaction.commit()
        // }
    }

    fun isValidl(view: View): Boolean {

        if (fname.value == null || fname.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_fname))
            return false
        }
        if (lname.value == null || lname.value?.isEmpty()!!) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_lname))
            return false
        }
        if (dob.value == null || dob.value?.isEmpty()!!) {
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
        if (selectItem.equals("Nikol")) {
            location = "4"
        } else {
            location = "5"
        }

        //  if (isValid(view)) {
        authListener!!.onStarted()
        Coroutines.main {
            try {
                val authResponse =
                    repository.addPatient(
                        fname.value!!,
                        lname.value!!, gender!!,
                        dob.value!!, age.value!!, refId.value!!,
                        refName.value!!, mNumber.value!!, location!!,
                        adr1.value!!, adr2.value!!, city.value!!,
                        state.value!!, country.value!!, pinCode.value!!
                    )
                authResponse.data?.let {
                    authListener?.onSuccess()
                    return@main
                }
                authListener?.onFailure(authResponse.message!!)
            } catch (e: ApiExceptions) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }
        // }
    }

    fun onPreviousClick(view: View) {
        view.context.hideKeyboard(view)
        val transaction =
            (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
        //   transaction.addToBackStack(null)
        transaction.replace(R.id.fl_container, AddPatientFragment.newInstance()!!)
        transaction.commit()
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