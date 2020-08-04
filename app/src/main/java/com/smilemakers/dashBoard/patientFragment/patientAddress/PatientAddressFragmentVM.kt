package com.smilemakers.dashBoard.patientFragment.patientAddress

import android.view.View
import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.smilemakers.dashBoard.DashboardActivity
import java.util.*

class PatientAddressFragmentVM(
    val mFragment: PatientAddressFragment,
    val mActivity: DashboardActivity
) : ViewModel() {

    init {
        Places.initialize(mActivity, "AIzaSyBMAD8TRAXSAhr3u9pJ3AGIuKs-vm2qMHw")
        val placesClient = Places.createClient(mActivity)
    }

    fun onSaveClick(view: View) {

    }

    fun onAddressFieldClick(view: View) {
        val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, fields)
            .build(mActivity)
        mActivity.startActivityForResult(intent, 1223)
    }
}