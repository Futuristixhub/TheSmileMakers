package com.smilemakers.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
    var pid: String? = null,
    var image: String? = null,
    var fname: String? = null,
    var lname: String? = null,
    var mno: String? = null,
    var gender: String? = null,
    var dob: String? = null,
    var age: String? = null,
    var address: String? = null,
    var area: String? = null,
    var city: String? = null,
    var postcode: String? = null,
    var state: String? = null,
    var country: String? = null
) {
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}