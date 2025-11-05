package ru.zaikin.messanger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SigninActivity : AppCompatActivity() {
    private val TAG: String = "SignInActivity:"

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var passwordEditConfirm: EditText
    private lateinit var nameEdit: EditText
    private lateinit var loginSignUp: Button
    private lateinit var toggle: TextView
    private var loginModeActive: Boolean = false
    private lateinit var database: FirebaseDatabase
    private lateinit var myUsers: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)

        auth = Firebase.auth

        emailEdit = findViewById<EditText>(R.id.emailEditText)
        passwordEdit = findViewById<EditText>(R.id.passwordEditText)
        nameEdit = findViewById<EditText>(R.id.nameEditText)
        loginSignUp = findViewById<Button>(R.id.loginSignUpButton)
        toggle = findViewById<TextView>(R.id.toggleLoginSignupTextView)
        passwordEditConfirm = findViewById<EditText>(R.id.repeatPasswordEditText)

        database = FirebaseDatabase.getInstance()
        myUsers = database.getReference("users")

        loginSignUp.setOnClickListener {
            loginSignUpUser(emailEdit.text.toString().trim(), passwordEdit.text.toString().trim())
        }

        if (auth.currentUser != null) {
            startActivity(Intent(this, ChatActivity::class.java))
        }

    }

    private fun loginSignUpUser(email: String, password: String) {

        if (loginModeActive) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        startActivity(Intent(this, ChatActivity::class.java))
                        // updateUI(user)
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        // updateUI(null)
                    }
                }
        } else {
            if (!(passwordEdit.text.toString().trim()
                    .equals(passwordEditConfirm.text.toString().trim()))
            ) {
                Toast.makeText(this, "passwords aren't matched", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            createUser(user)
                            val intent: Intent = Intent(this, ChatActivity::class.java)
                            intent.putExtra("name", nameEdit.text.toString())
                            startActivity(intent)
                            //updateUI(user)
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            //updateUI(null)
                        }
                    }
            }
        }

    }

    fun toggleLoginMode(view: View) {
        if (loginModeActive) {
            loginModeActive = false
            loginSignUp.text = "Sign up"
            toggle.text = "Or, log in"
            passwordEditConfirm.visibility = View.VISIBLE
        } else {
            loginModeActive = true
            loginSignUp.text = "Login"
            toggle.text = "Or sign up"
            passwordEditConfirm.visibility = View.INVISIBLE
        }
    }

    fun createUser(firebaseUser: FirebaseUser?) {

        val user: User = User()
        user.id = firebaseUser!!.uid
        user.email = firebaseUser.email.toString()
        user.name = nameEdit.text.toString()

        myUsers.child(firebaseUser.uid).setValue(user)
    }


}

