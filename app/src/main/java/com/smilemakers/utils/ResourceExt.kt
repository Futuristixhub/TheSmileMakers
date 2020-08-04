package com.smilemakers.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

fun Int.color(activit: FragmentActivity): Int = ContextCompat.getColor(activit, this)
//fun Int.color(activity: FragmentActivity): Int = ContextCompat.getColor(activity, this)

fun Int.string(activit: FragmentActivity): String = activit.getResources().getString(this)

fun Int.string(context: Context): String = context.getResources().getString(this)

//fun Int.string(val1: String): String = CoreApp.instance!!.getResources().getString(this, val1)
//
//fun Int.string(val1: String, val2: String): String = CoreApp.instance!!.getResources().getString(this, val1, val2)
//fun Int.string(val1: Int): String = CoreApp.instance!!.getResources().getString(this, val1)
//fun Int.string(val1: Int, val2: Int): String = CoreApp.instance!!.getResources().getString(this, val1, val2)

fun Int.drawable(activit: FragmentActivity): Drawable? = ContextCompat.getDrawable(activit, this)
//fun Int.drawable(activity: FragmentActivity): Drawable? = ContextCompat.getDrawable(activity, this)
