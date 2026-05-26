package com.example.gestoreventos.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    fun aplicarTema(context: Context) {

        val prefs = context.getSharedPreferences(
            "tema",
            Context.MODE_PRIVATE
        )

        when (
            prefs.getString(
                "modo",
                "sistema"
            )
        ) {

            "claro" -> {

                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }

            "escuro" -> {

                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }

            else -> {

                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
            }
        }
    }
}