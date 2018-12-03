package com.example.brand.seniordesigndemo.helpers

import android.content.res.Resources
import com.example.brand.seniordesigndemo.R

class UnitHelper(resources: Resources) {
    val resources = resources
    var speedUnit: String = resources.getString(R.string.kmh)
    val kmpToMph = 0.621371

   fun getSpeedString(rawSpeed: Double): String {
       return if (speedUnit == resources.getString(R.string.kmh)) {
           "$rawSpeed\n${resources.getString(R.string.kmh)}"
       } else {
           "${rawSpeed * kmpToMph}\n${resources.getString(R.string.mph)}"
       }
   }

}