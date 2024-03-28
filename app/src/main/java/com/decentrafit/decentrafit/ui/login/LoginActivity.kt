package com.decentrafit.decentrafit.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.decentrafit.decentrafit.R
import com.decentrafit.decentrafit.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)


        val loginChecked = false

        // Keep the splash screen visible for this Activity.
        splashScreen.setKeepOnScreenCondition {
            loginChecked
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()


        setContentView(R.layout.activity_login)
    }
}