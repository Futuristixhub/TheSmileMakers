package com.smilemakers.ui.dashBoard.doctorFragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.Window
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.smilemakers.R
import com.smilemakers.databinding.DialogDoctorDetailBinding
import com.smilemakers.databinding.DoctorListSingleItemBinding
import com.xwray.groupie.databinding.BindableItem

class DoctorItem(
    private val doctor: Doctor, val context: Context
) : BindableItem<DoctorListSingleItemBinding>() {

    override fun getLayout() = R.layout.doctor_list_single_item

    override fun bind(viewBinding: DoctorListSingleItemBinding, position: Int) {
        viewBinding.vm = doctor
        if (!doctor.image.isNullOrEmpty()) {
            Glide.with(context).load(Uri.parse(doctor.image)).into(viewBinding.circleImageView)
        }
        viewBinding.root.setOnClickListener {
            val dialog = Dialog(context,R.style.Theme_Dialog)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.getWindow()!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            dialog.getWindow()!!.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))

            val binding: DialogDoctorDetailBinding = DataBindingUtil
                .inflate(LayoutInflater.from(context), R.layout.dialog_doctor_detail, null, false)
            binding.vm = doctor

            if (!doctor.image.isNullOrEmpty()) {
                Glide.with(context).load(Uri.parse(doctor.image)).into(binding.ivProfile)
            }
            dialog.setContentView(binding.root)
            binding.btnCancel.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }
}