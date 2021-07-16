package com.smilemakers.ui.dashBoard.patientFragment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Patient(
    @PrimaryKey(autoGenerate = false)
    val sno: String,
    val pid: String,
    val fname: String,
    val lname: String,
    val retarea: String,
    val age: String,
    val mno: String,
    var image: String,
    var refname: String,
    var gender: String,
    var address: String,
    var altmno: String,
    var city: String,
    var postcode: String,
    var state: String,
    var country: String
)