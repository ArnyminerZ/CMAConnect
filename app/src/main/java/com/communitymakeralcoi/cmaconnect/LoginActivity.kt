package com.communitymakeralcoi.cmaconnect

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.communitymakeralcoi.cmaconnect.api.config.ui_version
import com.communitymakeralcoi.cmaconnect.api.view.CMAActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : CMAActivity() {
    override fun setupView() {
        when (ui_version) {
            1 -> {
                setTheme(R.style.Style1_NoTitle)
            }
        }
    }

    override fun updateView() {
        when (ui_version) {
            1 -> {
                setContentView(R.layout.activity_login)

                confirmPassword_editText.text.clear()
                confirmPassword_editText.visibility = View.GONE
            }
        }
    }

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        setupView()
        super.onCreate(savedInstanceState)
        updateView()

        mAuth = FirebaseAuth.getInstance()

        authenticate_button.setOnClickListener {
            if (confirmPassword_editText.text.isEmpty())
                login(it)
            else
                signup(it)
        }
    }

    private fun login(view: View) {
        mAuth.signInWithEmailAndPassword(email_editText.text.toString(), password_editText.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(tag, "signInWithEmail:success")
                        val user = mAuth.currentUser
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(tag, "signInWithEmail:failure", task.exception)
                        if (task.exception is com.google.firebase.auth.FirebaseAuthInvalidUserException) {
                            Snackbar.make(view, R.string.login_confirm_register, Snackbar.LENGTH_SHORT).show()
                            val enterAnimation = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.enter_from_top)
                            confirmPassword_editText.visibility = View.VISIBLE
                            confirmPassword_editText.startAnimation(enterAnimation)
                        }
                    }
                }
    }
    private fun signup(view: View){
        mAuth.createUserWithEmailAndPassword(email_editText.text.toString(), password_editText.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(tag, "createUserWithEmail:success")
                        val user = mAuth.currentUser
                        login(view)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(tag, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }
}
