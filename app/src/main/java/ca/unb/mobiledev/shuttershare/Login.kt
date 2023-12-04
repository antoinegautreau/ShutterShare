package ca.unb.mobiledev.shuttershare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import ca.unb.mobiledev.shuttershare.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding //this might need to change
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("ShutterShareLogin", "in login")

        sharedPreferences = getSharedPreferences("ShutterShareData", MODE_PRIVATE)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.textView.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        binding.signInButton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{
                    if(it.isSuccessful){
                        Log.d("ShutterShareLogin", "Successful Login")

                        // TODO Get username from DB
                        val username = email

                        // keep track of the user who is logged in
                        val sharedPrefEditor = sharedPreferences.edit()
                        sharedPrefEditor.putString("username", username)
                        sharedPrefEditor.commit()


                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Log.d("ShutterShareLogin", "Failed Login")
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(this, "Please fill the text boxes", Toast.LENGTH_SHORT).show()
            }

        }
    }
}