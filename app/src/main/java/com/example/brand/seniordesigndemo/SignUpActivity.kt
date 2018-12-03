package com.example.brand.seniordesigndemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.example.brand.seniordesigndemo.helpers.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var deviceIdEditText: EditText
    private lateinit var startButton:Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpSwitch: Switch


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initFirebase()
        initLayout()
    }

    private fun initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("users")
        Log.d("reference", reference.toString())
        Log.d("reference", reference.toString())
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
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val deviceId = deviceIdEditText.text.toString()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, R.string.account_created, Toast.LENGTH_SHORT)
                                .show()

                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        completeSignUp(deviceId)
                                    } else {
                                        Log.d("log in", "log in failed")
                                        Toast.makeText(this, R.string.failed_to_sign_in, Toast.LENGTH_SHORT)
                                                .show()
                                    }
                                }
                    } else {
                        Toast.makeText(this, R.string.failed_to_create_account, Toast.LENGTH_SHORT)
                                .show()
                    }
                }


    }

    private fun completeSignUp(deviceId: String) {
        Log.d("log in", "log in success")
        firebaseUser = firebaseAuth.currentUser!!
        Log.d("user", firebaseUser.uid.toString())
        reference = reference.child(firebaseUser.uid)
        Log.d("reference", reference.toString())
        val push = reference.push()
        Log.d("push", push.key)
        push.setValue(UserData(deviceId)).addOnCompleteListener { task ->
            Log.d("push", "tyying to write to the data base")
            if (task.isSuccessful) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT)
                        .show()
            } else {
                Toast.makeText(this, "failure", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        startMonitor(deviceId)
    }

    private fun logIn() {
        val email = emailEditText.text.toString()
        val passwrod = passwordEditText.text.toString()
        lateinit var userData: UserData
        var deviceId = ""

        var isSuccessful = false

        firebaseAuth.signInWithEmailAndPassword(email, passwrod)
                .addOnSuccessListener {
                    isSuccessful = true
                }
                .addOnFailureListener {
                    Toast.makeText(this, R.string.failed_to_sign_in, Toast.LENGTH_SHORT)
                            .show()
                }

        if (!isSuccessful)
            return

        firebaseUser = firebaseAuth.currentUser!!
        reference = reference.child(firebaseUser.uid)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { child ->
                    userData = child.getValue(UserData::class.java)!!
                    deviceId = userData.deviceId
                }
            }

        })

        if (deviceId.isEmpty())
            return

        if (deviceId.isNotEmpty()) {
            startMonitor(deviceId)
        } else {
            firebaseAuth.signOut()
            Toast.makeText(this, R.string.failed_to_sign_in, Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun startMonitor(deviceId: String) {
        Log.d("startMonitor", "attempting to start monitor")
        val intent = Intent(this, MonitorActivity::class.java)
        intent.putExtra("DEVICE_ID", deviceId)
        startActivity(intent)
    }
}
