package com.example.gestoreventos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.gestoreventos.utils.ThemeManager
import androidx.core.content.edit

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewFlipper:
            ViewFlipper

    private lateinit var btnVoltar:
            Button

    private lateinit var btnProximo:
            Button

    private lateinit var txtPular:
            TextView

    private lateinit var txtIndicador:
            TextView

    private lateinit var txtBolinha1:
            TextView

    private lateinit var txtBolinha2:
            TextView

    private lateinit var txtBolinha3:
            TextView

    private lateinit var txtBolinha4:
            TextView

    private var paginaAtual =
        0

    private val totalPaginas =
        4

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeManager.aplicarTema(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding)

        viewFlipper =
            findViewById(R.id.viewFlipper)

        btnVoltar =
            findViewById(R.id.btnVoltar)

        btnProximo =
            findViewById(R.id.btnProximo)

        txtPular =
            findViewById(R.id.txtPular)

        txtIndicador =
            findViewById(R.id.txtIndicador)

        txtBolinha1 =
            findViewById(R.id.txtBolinha1)

        txtBolinha2 =
            findViewById(R.id.txtBolinha2)

        txtBolinha3 =
            findViewById(R.id.txtBolinha3)

        txtBolinha4 =
            findViewById(R.id.txtBolinha4)

        atualizarTela()

        btnProximo.setOnClickListener {

            if (paginaAtual < totalPaginas - 1) {

                paginaAtual++

                viewFlipper.showNext()

                atualizarTela()

            } else {

                pedirPermissaoNotificacao()
            }
        }

        btnVoltar.setOnClickListener {

            if (paginaAtual > 0) {

                paginaAtual--

                viewFlipper.showPrevious()

                atualizarTela()
            }
        }

        txtPular.setOnClickListener {

            finalizarOnboarding()
        }
    }

    private fun atualizarTela() {

        txtIndicador.text =
            getString(
                R.string.onboarding_indicador,
                paginaAtual + 1,
                totalPaginas
            )

        btnProximo.text =
            if (paginaAtual == totalPaginas - 1)
                getString(R.string.onboarding_comecar)
            else
                getString(R.string.onboarding_proximo)

        btnVoltar.visibility =
            if (paginaAtual == 0)
                View.INVISIBLE
            else
                View.VISIBLE

        atualizarBolinha(
            txtBolinha1,
            paginaAtual == 0
        )

        atualizarBolinha(
            txtBolinha2,
            paginaAtual == 1
        )

        atualizarBolinha(
            txtBolinha3,
            paginaAtual == 2
        )

        atualizarBolinha(
            txtBolinha4,
            paginaAtual == 3
        )
    }

    private fun atualizarBolinha(
        bolinha: TextView,
        selecionada: Boolean
    ) {

        bolinha.text =
            if (selecionada)
                "●"
            else
                "○"

        bolinha.alpha =
            if (selecionada)
                1.0f
            else
                0.45f
    }

    private fun pedirPermissaoNotificacao() {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {

            val permissao =
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )

            if (
                permissao !=
                PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    100
                )

            } else {

                finalizarOnboarding()
            }

        } else {

            finalizarOnboarding()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if (requestCode == 100) {

            finalizarOnboarding()
        }
    }

    private fun finalizarOnboarding() {

        getSharedPreferences(
            "app",
            MODE_PRIVATE
        ).edit {

            putBoolean(
                "onboarding_concluido",
                true
            )
        }

        startActivity(
            Intent(
                this,
                MainActivity::class.java
            )
        )

        finish()
    }
}