package com.smilemakers.dashBoard.patientFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.smilemakers.R
import com.smilemakers.databinding.PatientListSingleItemBinding
import kotlinx.android.synthetic.main.patient_list_single_item.view.*

class PatientListAdapter(val mContext: Context, val mItems: ArrayList<PatientPOJO>): RecyclerView.Adapter<PatientListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientListViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val binding = DataBindingUtil.inflate<PatientListSingleItemBinding>(inflater, R.layout.patient_list_single_item,parent, false)
        return PatientListViewHolder(binding)
    }

    override fun getItemCount() = mItems.size

    override fun onBindViewHolder(holder: PatientListViewHolder, position: Int) {
        holder.binding.vm = mItems[position]
    }
}

class PatientListViewHolder(val binding: PatientListSingleItemBinding) : RecyclerView.ViewHolder(binding.root) {
//    val name = view.txt_patient_name
//    val email = view.txt_patient_email
//    val mobile = view.txt_patient_number
//    val refId = view.txt_ref_id
//    val bDate = view.txt_birth_date
//    val regFrom = view.txt_registered_from
}