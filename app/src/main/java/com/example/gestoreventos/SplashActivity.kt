package com.example.gestoreventos

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.gestoreventos.utils.ThemeManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeManager.aplicarTema(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        Handler(
            Looper.getMainLooper()
        ).postDelayed({

            val prefs =
                getSharedPreferences(
                    "app",
                    MODE_PRIVATE
                )

            val onboardingConcluido =
                prefs.getBoolean(
                    "onboarding_concluido",
                    false
                )

            if (onboardingConcluido) {

                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )

            } else {

                startActivity(
                    Intent(
                        this,
                        OnboardingActivity::class.java
                    )
                )
            }

            finish()

        }, 1600)
    }
}