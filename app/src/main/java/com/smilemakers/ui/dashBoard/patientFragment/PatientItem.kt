package com.smilemakers.ui.dashBoard.patientFragment

import com.smilemakers.R
import com.smilemakers.databinding.PatientListSingleItemBinding
import com.xwray.groupie.databinding.BindableItem

class PatientItem(
private val patient: Patient
) : BindableItem<PatientListSingleItemBinding>(){

    override fun getLayout() = R.layout.patient_list_single_item

    override fun bind(viewBinding: PatientListSingleItemBinding, position: Int) {
        viewBinding.vm = patient
    }
}