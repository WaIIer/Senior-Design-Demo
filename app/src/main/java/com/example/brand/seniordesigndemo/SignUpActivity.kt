package com.example.brand.seniordesigndemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText

class SignUpActivity : AppCompatActivity() {

    private lateinit var deviceIdEditText: EditText
    private lateinit var startButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initLayout()
    }

    private fun initLayout() {
        deviceIdEditText = findViewById(R.id.device_id_edit_text)
        startButton = findViewById(R.id.start_button)

        deviceIdEditText.addTextChangedListener(textWatcher)

        startButton.setOnClickListener {
            val deviceId = deviceIdEditText.text.toString()

            //TODO change to launch next activity
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("DEVICE_ID", deviceId)

            startActivity(intent)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val deviceId = deviceIdEditText.text.toString()

            startButton.isEnabled = deviceId.isNotEmpty()
        }

    }
}
