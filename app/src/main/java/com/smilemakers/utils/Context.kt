package com.smilemakers.utils

import android.content.Context
val Context.config: Config get() = Config.newInstance(applicationContext)

