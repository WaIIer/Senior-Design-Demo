package com.example.brand.seniordesigndemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.triggertrap.seekarc.SeekArc

class MonitorActivity : AppCompatActivity() {

    private lateinit var speedSeekArc: SeekArc
    private lateinit var speedText: TextView
    private lateinit var rpmSeekArc: SeekArc
    private lateinit var rpmText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)

        initLayout()
    }

    private fun initLayout() {
        speedSeekArc = findViewById(R.id.speed_seek_bar)
        speedText = findViewById(R.id.speed_text_view)
        rpmSeekArc = findViewById(R.id.rpm_seek_bar)
        rpmText = findViewById(R.id.rpm_text_view)
    }
}
