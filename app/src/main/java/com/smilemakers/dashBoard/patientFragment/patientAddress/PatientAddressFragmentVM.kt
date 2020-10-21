package com.smilemakers.dashBoard.patientFragment.patientAddress

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.doctorFragment.detail.DetailFragment
import com.smilemakers.dashBoard.patientFragment.addPatient.AddPatientFragment
import java.util.*

class PatientAddressFragmentVM() : ViewModel() {

    init {
      //  Places.initialize(mActivity, "AIzaSyBMAD8TRAXSAhr3u9pJ3AGIuKs-vm2qMHw")
       // val placesClient = Places.createClient(mActivity)
    }

    fun onSaveClick(view: View) {

    }

    fun onPreviousClick(view: View) {
        val transaction = (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
     //   transaction.addToBackStack(null)
        transaction.replace(R.id.fl_container, AddPatientFragment.newInstance()!!)
        transaction.commit()
    }

    fun onAddressFieldClick(view: View) {
        val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, fields)
            .build(view.context)
        (view.context as AppCompatActivity).startActivityForResult(intent, 1223)
    }
}