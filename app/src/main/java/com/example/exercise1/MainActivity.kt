package com.example.exercise1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var btnCreateAccount: Button
    private val launchCreateAccount = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        btnCreateAccount = findViewById(R.id.createAccount_button)
        btnCreateAccount.setOnClickListener(this::handleButtonPress)
    }

    private fun handleButtonPress(v: View) {
        val createAccountActivity = Intent()
        createAccountActivity.setClass(this, CreateAccountActivity::class.java)
        launchCreateAccount.launch(createAccountActivity)
    }
}