package com.smilemakers.ui.dashBoard.doctorFragment

import com.smilemakers.R
import com.smilemakers.databinding.DoctorListSingleItemBinding
import com.xwray.groupie.databinding.BindableItem

class DoctorItem (
    private val doctor: Doctor
) : BindableItem<DoctorListSingleItemBinding>(){

    override fun getLayout() = R.layout.doctor_list_single_item

    override fun bind(viewBinding: DoctorListSingleItemBinding, position: Int) {
        viewBinding.vm = doctor
    }
}