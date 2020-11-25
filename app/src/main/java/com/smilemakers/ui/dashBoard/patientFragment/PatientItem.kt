package com.smilemakers.ui.dashBoard.patientFragment

import android.content.Context
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.smilemakers.R
import com.smilemakers.databinding.PatientListSingleItemBinding
import com.smilemakers.utils.Coroutines
import com.smilemakers.utils.isImageURL
import com.xwray.groupie.databinding.BindableItem

class PatientItem(
    private val patient: Patient, val context: Context
) : BindableItem<PatientListSingleItemBinding>() {

    override fun getLayout() = R.layout.patient_list_single_item

    override fun bind(viewBinding: PatientListSingleItemBinding, position: Int) {
        viewBinding.vm = patient
        if(!patient.image.isNullOrEmpty()) {
            Glide.with(context).load(Uri.parse(patient.image)).into(viewBinding.circleImageView)
        }
    }
}