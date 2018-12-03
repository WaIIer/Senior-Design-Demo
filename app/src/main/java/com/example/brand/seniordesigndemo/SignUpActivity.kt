package com.example.brand.seniordesigndemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var deviceIdEditText: EditText
    private lateinit var startButton:Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpSwitch: Switch


    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initLayout()
    }

    private fun initLayout() {
        deviceIdEditText = findViewById(R.id.device_id_edit_text)
        startButton = findViewById(R.id.start_button)
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        signUpSwitch = findViewById(R.id.sign_up_switch)

        deviceIdEditText.addTextChangedListener(textWatcher)

        startButton.setOnClickListener {
            if (signUpSwitch.isChecked) {
                signUp()
            } else {
                logIn()
            }
        }

        signUpSwitch.setOnClickListener {
            if (signUpSwitch.isChecked) {
                deviceIdEditText.visibility = View.VISIBLE
                signUpSwitch.text = getString(R.string.sign_up)
            } else {
                deviceIdEditText.visibility = View.INVISIBLE
                signUpSwitch.text = getString(R.string.Log_In)
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val emailAddress = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val deviceId = deviceIdEditText.text.toString()

            startButton.isEnabled = emailAddress.isNotEmpty() && password.isNotEmpty()
            if (signUpSwitch.isChecked) {
                startButton.isEnabled = startButton.isEnabled && deviceId.isNotEmpty()
            }
        }

    }

    private fun signUp() {
        val deviceId = deviceIdEditText.text.toString()

        val intent = Intent(this, MonitorActivity::class.java)
        intent.putExtra("DEVICE_ID", deviceId)

        startActivity(intent)
    }

    private fun logIn() {
        val deviceId = deviceIdEditText.text.toString()

        val intent = Intent(this, MonitorActivity::class.java)
        intent.putExtra("DEVICE_ID", deviceId)

        startActivity(intent)
    }
}
