package com.smilemakers.ui.dashBoard.doctorFragment

import android.content.Context
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.smilemakers.R
import com.smilemakers.databinding.DoctorListSingleItemBinding
import com.smilemakers.utils.Coroutines
import com.smilemakers.utils.isImageURL
import com.xwray.groupie.databinding.BindableItem
import java.net.URL
import java.net.URLConnection

class DoctorItem(
    private val doctor: Doctor, val context: Context
) : BindableItem<DoctorListSingleItemBinding>() {

    override fun getLayout() = R.layout.doctor_list_single_item

    override fun bind(viewBinding: DoctorListSingleItemBinding, position: Int) {
        viewBinding.vm = doctor
        if (!doctor.image.isNullOrEmpty()) {
            Glide.with(context).load(Uri.parse(doctor.image)).into(viewBinding.circleImageView)
        }
    }
}