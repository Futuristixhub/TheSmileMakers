package com.smilemakers.dashBoard.doctorFragment

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smilemakers.R
import com.smilemakers.dashBoard.DashboardActivity
import com.smilemakers.dashBoard.doctorFragment.addDoctor.AddDoctorFragment
import com.smilemakers.dashBoard.patientFragment.addPatient.AddPatientFragment

class DoctorFragmentVM(val mFragment: DoctorFragment, val mActivity: DashboardActivity) :
    ViewModel() {

    val patientList = ArrayList<DoctorPOJO>()

    init {
        newPatientList()
    }

    fun createList(recView: RecyclerView) {
        val viewManager = LinearLayoutManager(mFragment.requireContext())
        val mAdapter = DoctorListAdapter(mFragment.requireContext(), patientList)
        recView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager as RecyclerView.LayoutManager?
            adapter = mAdapter
        }
    }

    fun newPatientList() {
        patientList.add(
            DoctorPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "bapunagar",
                "25",
                "Costmetic Dentistry ,Restorative Dentistry,Implant Dentistry"
            )
        )
        patientList.add(
            DoctorPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "nikol",
                "21",
                "Cosmetic Dentistry"
            )
        )
        patientList.add(
            DoctorPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "bapunagar",
                "25",
                "Costmetic Dentistry ,Restorative Dentistry,Implant Dentistry"
            )
        )
        patientList.add(
            DoctorPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "nikol",
                "21",
                "Cosmetic Dentistry"
            )
        )
        patientList.add(
            DoctorPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "bapunagar",
                "25",
                "Costmetic Dentistry ,Restorative Dentistry,Implant Dentistry"
            )
        )
        patientList.add(
            DoctorPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "nikol",
                "21",
                "Cosmetic Dentistry"
            )
        )
        patientList.add(
            DoctorPOJO(
                "Preyansh Brahmbhatt",
                "8866511911",
                "preyansh.brahmbhatt@gmail.com",
                "bapunagar",
                "25",
                "Costmetic Dentistry ,Restorative Dentistry,Implant Dentistry"
            )
        )
        patientList.add(
            DoctorPOJO(
                "Ronak Patel",
                "9988738479",
                "ronak.patel@gmail.com",
                "nikol",
                "21",
                "Cosmetic Dentistry"
            )
        )
    }

    fun onAddDoctorClick(view: View) {
        val transaction = mActivity.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.add(R.id.fl_dash_container, AddDoctorFragment.newInstance(mActivity)!!)
        transaction.commit()
    }
}