package com.smilemakers.dashBoard.doctorFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.smilemakers.R
import com.smilemakers.dashBoard.patientFragment.PatientPOJO
import com.smilemakers.databinding.DoctorListSingleItemBinding
import com.smilemakers.databinding.PatientListSingleItemBinding

class DoctorListAdapter(val mContext: Context, val mItems: ArrayList<DoctorPOJO>): RecyclerView.Adapter<DoctorListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorListViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val binding = DataBindingUtil.inflate<DoctorListSingleItemBinding>(inflater, R.layout.doctor_list_single_item,parent, false)
        return DoctorListViewHolder(binding)
    }

    override fun getItemCount() = mItems.size

    override fun onBindViewHolder(holder: DoctorListViewHolder, position: Int) {
        holder.binding.vm = mItems[position]
    }
}

class DoctorListViewHolder(val binding: DoctorListSingleItemBinding) : RecyclerView.ViewHolder(binding.root) {
//    val name = view.txt_patient_name
//    val email = view.txt_patient_email
//    val mobile = view.txt_patient_number
//    val refId = view.txt_ref_id
//    val bDate = view.txt_birth_date
//    val regFrom = view.txt_registered_from
}