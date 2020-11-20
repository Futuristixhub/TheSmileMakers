package com.smilemakers.ui.dashBoard.doctorFragment

import android.content.Context
import android.net.Uri
import com.bumptech.glide.Glide
import com.smilemakers.R
import com.smilemakers.databinding.DoctorListSingleItemBinding
import com.xwray.groupie.databinding.BindableItem

class DoctorItem (
    private val doctor: Doctor,val context:Context
) : BindableItem<DoctorListSingleItemBinding>(){

    override fun getLayout() = R.layout.doctor_list_single_item

    override fun bind(viewBinding: DoctorListSingleItemBinding, position: Int) {
        viewBinding.vm = doctor
        Glide.with(context).load(Uri.parse(doctor.image)).into(viewBinding.circleImageView)
    }
}