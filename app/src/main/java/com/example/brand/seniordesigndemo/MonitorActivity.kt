package com.example.brand.seniordesigndemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.triggertrap.seekarc.SeekArc

class MonitorActivity : AppCompatActivity() {

    private lateinit var speedSeekArc: SeekArc
    private lateinit var speedText: TextView
    private lateinit var rpmSeekArc: SeekArc
    private lateinit var rpmText: TextView

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)

        initLayout()
        initFirebase()
    }

    private fun initFirebase() {
        val deviceId = intent.getStringExtra("DEVICE_ID")
        Log.d("DEVICE ID", deviceId)

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabaseReference = firebaseDatabase.getReference("devices").child(deviceId)

        firebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("snapshot", p0.toString())
                Toast.makeText(this@MonitorActivity,
                        "Failed to retrieve reading",
                        Toast.LENGTH_SHORT)
                        .show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val reading = dataSnapshot.value.toString().split(" ")
                Log.d("HERE", dataSnapshot.toString())
                if (reading.size == 2) {
                    speedText.text = reading[0]
                    speedSeekArc.progress = reading[0].toInt()

                    rpmText.text = reading[1]
                    rpmSeekArc.progress = reading[1].toInt()
                }
            }

        })
    }

    private fun initLayout() {
        speedSeekArc = findViewById(R.id.speed_seek_bar)
        speedText = findViewById(R.id.speed_text_view)
        rpmSeekArc = findViewById(R.id.rpm_seek_bar)
        rpmText = findViewById(R.id.rpm_text_view)
    }
}
