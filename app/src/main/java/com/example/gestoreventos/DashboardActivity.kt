package com.example.gestoreventos

import android.os.Bundle
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gestoreventos.db.DBHelper
import com.example.gestoreventos.utils.ThemeManager

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeManager.aplicarTema(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        val txtTotal =
            findViewById<TextView>(R.id.txtTotal)

        val txtConcluidos =
            findViewById<TextView>(R.id.txtConcluidos)

        val radioClaro =
            findViewById<RadioButton>(R.id.radioClaro)

        val radioEscuro =
            findViewById<RadioButton>(R.id.radioEscuro)

        val radioSistema =
            findViewById<RadioButton>(R.id.radioSistema)

        val db = DBHelper(this)

        val eventos =
            db.getTodosEventos()

        val concluidos =
            eventos.count {
                it.concluido == 1
            }

        txtTotal.text =
            getString(
                R.string.total_eventos,
                eventos.size
            )

        txtConcluidos.text =
            getString(
                R.string.total_concluidos,
                concluidos
            )

        val prefs =
            getSharedPreferences(
                "tema",
                MODE_PRIVATE
            )

        when (
            prefs.getString(
                "modo",
                "sistema"
            )
        ) {

            "claro" ->
                radioClaro.isChecked = true

            "escuro" ->
                radioEscuro.isChecked = true

            else ->
                radioSistema.isChecked = true
        }

        radioClaro.setOnClickListener {

            prefs.edit()
                .putString(
                    "modo",
                    "claro"
                )
                .apply()

            ThemeManager.aplicarTema(this)

            recreate()
        }

        radioEscuro.setOnClickListener {

            prefs.edit()
                .putString(
                    "modo",
                    "escuro"
                )
                .apply()

            ThemeManager.aplicarTema(this)

            recreate()
        }

        radioSistema.setOnClickListener {

            prefs.edit()
                .putString(
                    "modo",
                    "sistema"
                )
                .apply()

            ThemeManager.aplicarTema(this)

            recreate()
        }
    }
}