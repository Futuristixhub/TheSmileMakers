package com.smilemakers.ui.dashBoard.profile

import androidx.room.Entity

@Entity
data class Profile(
    var fname: String? = null,
    var lname: String? = null,
    var email: String? = null,
    var mobile: String? = null,
    var address: String? = null,
    var image: String? = null
)
