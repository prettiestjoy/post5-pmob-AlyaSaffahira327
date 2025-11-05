package com.aya.post3

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FULL_NAME = "extra_full_name"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AGE = "extra_age"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_GENDER = "extra_gender"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val fullName = intent.getStringExtra(EXTRA_FULL_NAME)
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val age = intent.getStringExtra(EXTRA_AGE)
        val email = intent.getStringExtra(EXTRA_EMAIL)
        val gender = intent.getStringExtra(EXTRA_GENDER)

        val tvResultFullName = findViewById<TextView>(R.id.tvResultFullName)
        val tvResultUsername = findViewById<TextView>(R.id.tvResultUsername)
        val tvResultAge = findViewById<TextView>(R.id.tvResultAge)
        val tvResultEmail = findViewById<TextView>(R.id.tvResultEmail)
        val tvResultGender = findViewById<TextView>(R.id.tvResultGender)

        tvResultFullName.text = "$fullName"
        tvResultUsername.text = "$username"
        tvResultAge.text = "$age"
        tvResultEmail.text = "$email"
        tvResultGender.text = "$gender"
    }
}