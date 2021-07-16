package com.smilemakers.ui.dashBoard.patientFragment

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
import com.smilemakers.databinding.DialogPatientDetailBinding
import com.smilemakers.databinding.PatientListSingleItemBinding
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
        viewBinding.root.setOnClickListener {
            val dialog = Dialog(context,R.style.Theme_Dialog)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.getWindow()!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            dialog.getWindow()!!.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))

            val binding: DialogPatientDetailBinding = DataBindingUtil
                .inflate(LayoutInflater.from(context), R.layout.dialog_patient_detail, null, false)
            binding.vm = patient
            if (!patient.image.isNullOrEmpty()) {
                Glide.with(context).load(Uri.parse(patient.image)).into(binding.ivProfile)
            }
            dialog.setContentView(binding.root)
            binding.btnCancel.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }
}