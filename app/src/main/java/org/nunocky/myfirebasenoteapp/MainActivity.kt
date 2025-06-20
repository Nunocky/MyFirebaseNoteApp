package org.nunocky.myfirebasenoteapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import org.nunocky.myfirebasenoteapp.ui.theme.myfirebasenoteappTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            myfirebasenoteappTheme {
                AppRouting()
            }
        }
        handleIntent(intent)
    }

    // intentの受信
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        Log.d("MainActivity", "handleIntent called with intent: $intent")
        val data: Uri? = intent?.data
        if (data != null) {
            Log.d("MainActivity", "Received deep link: $data")
            
            // Firebase Email Link認証の処理
            val auth = FirebaseAuth.getInstance()
            if (auth.isSignInWithEmailLink(data.toString())) {
                Log.d("MainActivity", "This is a Firebase email link")
                
                // 保存されたメールアドレスを取得
                val email = getSharedPreferences("auth", MODE_PRIVATE)
                    .getString("email_for_sign_in", null)
                
                if (email != null) {
                    auth.signInWithEmailLink(email, data.toString())
                        .addOnSuccessListener { result ->
                            Log.d("MainActivity", "Email link sign-in successful")
                            // 保存されたメールアドレスをクリア
                            getSharedPreferences("auth", MODE_PRIVATE)
                                .edit()
                                .remove("email_for_sign_in")
                                .apply()
                        }
                        .addOnFailureListener { e ->
                            Log.e("MainActivity", "Email link sign-in failed", e)
                        }
                } else {
                    Log.e("MainActivity", "No email found for email link sign-in")
                }
            }
        }
    }
}
