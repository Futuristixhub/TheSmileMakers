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
    val mno: String,
    val typetement: String,
    var image: String
)
