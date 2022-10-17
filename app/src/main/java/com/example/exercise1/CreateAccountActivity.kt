package com.example.exercise1

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import java.util.regex.Pattern

class CreateAccountActivity : AppCompatActivity() {

    // These three parameters work as locks for the "Next" button
    private var isValidEmail: Boolean = false
    private var isValidPassword: Boolean = false
    private var isMatchPassword: Boolean = false

    // Password pattern at least one lowercase letter, one uppercase letter, and one number. It accepts special characters (@$!%*#?&)
    private val PASSWORD_PATTERN: String = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9@\$!%*#?&]{8,}"

    // Views declarations
    private lateinit var fabBack: FloatingActionButton
    private lateinit var etEmail: EditText
    private lateinit var tvEmailWarning: TextView
    private lateinit var etPassword: EditText
    private lateinit var etRepeatPassword: EditText
    private lateinit var tvPasswordWarning: TextView
    private lateinit var btnNext: Button

    // Static LinkedList to hold email addresses
    companion object {
        private val emailsList: LinkedList<String> = LinkedList<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        initViews()
    }

    private fun initViews() {
        fabBack = findViewById(R.id.back_floatingActionButton)
        fabBack.setOnClickListener(this::goBackFunction)

        etEmail = findViewById(R.id.email_editTextEmailAddress)
        etEmail.setOnFocusChangeListener { _, hasFocus -> validateEmail(hasFocus) }
        // Below listener is used to lock the "Next" button if the Email text changes.
        etEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }
            override fun afterTextChanged(p0: Editable?) {
                isValidEmail = false
                enableDisableNext()
            }
        })

        tvEmailWarning = findViewById(R.id.emailWarningMsg_textView)

        etPassword = findViewById(R.id.password_editTextPassword)
        etPassword.setOnFocusChangeListener { _, hasFocus -> validatePassword(hasFocus) }
        // Below listener is used to lock the "Next" button if the password text changes.
        etPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }
            override fun afterTextChanged(p0: Editable?) {
                isValidPassword = false
                enableDisableNext()
            }
        })

        etRepeatPassword = findViewById(R.id.repeatPassword_editTextPassword)
        etRepeatPassword.setOnFocusChangeListener { _, hasFocus -> validateRepeatPassword(hasFocus) }
        // Below listener is used to lock the "Next" button if the repeatPassword text changes.
        etRepeatPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }
            override fun afterTextChanged(p0: Editable?) {
                isMatchPassword = false
                enableDisableNext()
            }
        })

        tvPasswordWarning = findViewById(R.id.passwordWarningMsg_textView)

        btnNext = findViewById(R.id.next_button)
        btnNext.setOnClickListener(this::createNewAccount)
    }

    /**
     * Handler function for the "Back" button.
     * It only "finishes" the current activity and go back to the main activity.
     * In another word, the current activity will be popped out of the activity stack and the main
     * activity will be the head of the activity stack.
     */
    private fun goBackFunction(view: View) {
        finish()
    }

    /**
     * Handler function for the Email EditText view to be used to set the value "isValidEmail" parameter based on the validation (Lock/Unlock).
     * Once out of focus, the function will check the Email pattern.
     * If pattern is valid, the function will check if the Email already exists in the Emails list.
     * Based on the validation, the function will change the background to red/green box, add/remove tick, and show warning messages if needed accordingly.
     */
    private fun validateEmail(hasFocus: Boolean) {
        if(!hasFocus){
            isValidEmail = Patterns.EMAIL_ADDRESS.matcher(etEmail.text).matches()
            if(isValidEmail){
                if(!emailsList.contains(etEmail.text.toString())){
                    etEmail.background = getDrawable(R.drawable.green_shape)
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0)
                    tvEmailWarning.visibility = View.GONE
                    tvEmailWarning.text = null
                } else {
                    etEmail.background = getDrawable(R.drawable.et_red_shape)
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    isValidEmail = false
                    tvEmailWarning.text = getText(R.string.email_exist_warning)
                    tvEmailWarning.visibility = View.VISIBLE
                }
            } else {
                etEmail.background = getDrawable(R.drawable.et_red_shape)
                etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                tvEmailWarning.text = getText(R.string.email_format_warning)
                tvEmailWarning.visibility = View.VISIBLE
            }
        }
        enableDisableNext()
    }

    /**
     * Handler function for the password EditText view to be used to set the value "isValidPassword" parameter based on the validation (Lock/Unlock).
     * Once out of focus, the function will check the password against the REGEX defined in PASSWORD_PATTERN.
     * If pattern is valid, the function will call @validateRepeatPassword function to check if both passwords match.
     * Based on the validation, the function will change the background to red/green box, add/remove tick, and show warning messages if needed accordingly.
     */
    private fun validatePassword(hasFocus: Boolean) {
        if(!hasFocus) {
            isValidPassword = Pattern.matches(PASSWORD_PATTERN, etPassword.text.toString())
            if (isValidPassword) {
                etPassword.background = getDrawable(R.drawable.green_shape)
                etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0)
                tvPasswordWarning.visibility = View.GONE
                tvPasswordWarning.text = null
                validateRepeatPassword(false)
            } else {
                etPassword.background = getDrawable(R.drawable.et_red_shape)
                etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                tvPasswordWarning.text = getText(R.string.password_format_warning)
                tvPasswordWarning.visibility = View.VISIBLE

                etRepeatPassword.background = getDrawable(R.color.white)
                etRepeatPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                isMatchPassword = false
            }
        }
        enableDisableNext()
    }

    /**
     * Handler function for the repeat password EditText view to be used to set the value "isMatchPassword" parameter based on the validation (Lock/Unlock).
     * Once out of focus, the function will check if both passwords match.
     * Based on the validation, the function will change the background to red/green box, add/remove tick, and show warning messages if needed accordingly.
     */
    private fun validateRepeatPassword(hasFocus: Boolean){
        if(!hasFocus) {
            isMatchPassword = etPassword.text.toString() == etRepeatPassword.text.toString()
            if (isMatchPassword) {
                etRepeatPassword.background = getDrawable(R.drawable.green_shape)
                etRepeatPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0)
                tvPasswordWarning.visibility = View.GONE
                tvPasswordWarning.text = null
            } else {
                etRepeatPassword.background = getDrawable(R.drawable.et_red_shape)
                etRepeatPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                tvPasswordWarning.text = getText(R.string.passwords_matching_warning)
                tvPasswordWarning.visibility = View.VISIBLE
            }
        }
        enableDisableNext()
    }

    /**
     * Handler function for the "Next" button.
     * It adds the Email to the EmailsList and show a popup message.
     * Once the user clicks on the "OK" button, @resetView function will be called to reset all fields.
     */
    private fun createNewAccount (view: View) {
        emailsList.add(etEmail.text.toString())
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Your Account has been Created Successfully.")
        dialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener {
            _, _ ->
            resetView()
        })
        val alert = dialogBuilder.create()
        alert.setTitle("SUCCESS!")
        alert.show()
    }

    /**
     * This function is used to enable/disable the "Next" button based on the three locks (isValidEmail, isValidPassword, and isMatchPassword)
     */
    private fun enableDisableNext() {
        if (isValidEmail && isValidPassword && isMatchPassword) {
            btnNext.alpha = 1F
            btnNext.isEnabled = true
        } else {
            btnNext.alpha = 0.4F
            btnNext.isEnabled = false
        }
    }

    /**
     * This function is used to clear all EditText fields and reset the locks values.
     * It also calls @enableDisableNext() to disable the "Next" button.
     */
    private fun resetView() {
        etEmail.text.clear()
        etEmail.background = getDrawable(R.color.white)
        etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        isValidEmail = false

        etPassword.text.clear()
        etPassword.background = getDrawable(R.color.white)
        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        isValidPassword = false

        etRepeatPassword.text.clear()
        etRepeatPassword.background = getDrawable(R.color.white)
        etRepeatPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        isMatchPassword = false

        enableDisableNext()
    }
}