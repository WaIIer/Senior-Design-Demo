package com.example.brand.seniordesigndemo

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.example.brand.seniordesigndemo.helpers.UnitHelper
import com.google.firebase.database.*
import com.triggertrap.seekarc.SeekArc

class MonitorActivity : AppCompatActivity() {

    private lateinit var speedSeekArc: SeekArc
    private lateinit var speedText: TextView
    private lateinit var rpmSeekArc: SeekArc
    private lateinit var rpmText: TextView
    private lateinit var unitSwitch: Switch

    private lateinit var preferences: SharedPreferences

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseDatabaseReference: DatabaseReference

    private lateinit var unitHelper: UnitHelper

    private var lastReading: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)

        unitHelper = UnitHelper(resources)

        initLayout()
        initSharedPreferences()
        initFirebase()
    }

    private fun initFirebase() {
        val deviceId = intent.getStringExtra("DEVICE_ID")

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabaseReference = firebaseDatabase.getReference("devices").child(deviceId)

        firebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@MonitorActivity,
                        "Failed to retrieve reading",
                        Toast.LENGTH_SHORT)
                        .show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val reading = dataSnapshot.value.toString()
                setMeters(reading)
            }

        })
    }

    private fun initSharedPreferences() {
        preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

        val startingUnit = preferences.getString("UNIT", getString(R.string.kmh))
        if (startingUnit == getString(R.string.mph)) {
            unitHelper.speedUnit = getString(R.string.mph)
            unitSwitch.isChecked = true
            unitSwitch.text = getString(R.string.mph)
            setMeters(lastReading)
        }

        unitSwitch.setOnClickListener {
            if (unitSwitch.isChecked) {
                unitSwitch.setText(R.string.mph)
                preferences.edit().putString("UNIT", getString(R.string.mph)).apply()
            } else {
                unitSwitch.setText(R.string.kmh)
                preferences.edit().putString("UNIT", getString(R.string.kmh)).apply()
            }

            unitHelper.speedUnit = unitSwitch.text.toString()
            setMeters(lastReading)
        }
    }

    private fun initLayout() {
        speedSeekArc = findViewById(R.id.speed_seek_bar)
        speedText = findViewById(R.id.speed_text_view)
        rpmSeekArc = findViewById(R.id.rpm_seek_bar)
        rpmText = findViewById(R.id.rpm_text_view)
        unitSwitch = findViewById(R.id.unit_switch)
    }

    private fun setMeters(readingString: String) {
        val reading = readingString.split(" ")

        if (reading.size != 2) {
            return
        }

        lastReading = readingString

        speedText.text = unitHelper.getSpeedString(reading[0].toDouble())
        speedSeekArc.progress = reading[0].toInt()

        rpmText.text = getString(R.string.rpm_reading, reading[1])
        rpmSeekArc.progress = reading[1].toInt()

    }
}
