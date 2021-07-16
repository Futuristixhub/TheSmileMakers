package com.smilemakers.ui.dashBoard.doctorFragment

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Doctor(
    @PrimaryKey(autoGenerate = false)
    val did: String,
    val fname: String,
    val lname: String,
    val retarea: String,
    val age: String,
    val education: String,
    val mno: String,
    val altmno: String,
    val gender: String,
    var typetement: String,
    var addr: String,
    var city: String,
    var postcode: String,
    var state: String,
    var country: String,
    var image: String
)
