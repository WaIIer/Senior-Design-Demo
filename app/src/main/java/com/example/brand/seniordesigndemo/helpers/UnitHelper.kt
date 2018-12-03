package com.example.brand.seniordesigndemo.helpers

import android.content.res.Resources
import com.example.brand.seniordesigndemo.R
import java.math.RoundingMode
import java.text.DecimalFormat

class UnitHelper(resources: Resources) {
    val resources = resources
    var speedUnit: String = resources.getString(R.string.kmh)
    val kmpToMph = 0.621371

   fun getSpeedString(rawSpeed: Double): String {
       val df = DecimalFormat("#.##")
       df.roundingMode = RoundingMode.CEILING

       return if (speedUnit == resources.getString(R.string.kmh)) {
           "${df.format(rawSpeed)}\n${resources.getString(R.string.kmh)}"
       } else {
           "${df.format(rawSpeed * kmpToMph)}\n${resources.getString(R.string.mph)}"
       }
   }

}