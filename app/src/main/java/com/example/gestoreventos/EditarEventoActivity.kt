package com.example.gestoreventos

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestoreventos.db.DBHelper
import com.example.gestoreventos.utils.NotificationHelper
import com.example.gestoreventos.utils.ThemeManager
import java.util.Calendar
import java.util.Locale

class EditarEventoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeManager.aplicarTema(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_editar_evento)

        val edtTitulo =
            findViewById<EditText>(R.id.edtTitulo)

        val edtTipo =
            findViewById<AutoCompleteTextView>(R.id.edtTipo)

        val edtMateria =
            findViewById<AutoCompleteTextView>(R.id.edtMateria)

        val edtDescricao =
            findViewById<EditText>(R.id.edtDescricao)

        val edtInicio =
            findViewById<EditText>(R.id.edtInicio)

        val edtFim =
            findViewById<EditText>(R.id.edtFim)

        val edtLinks =
            findViewById<EditText>(R.id.edtLinks)

        val spinnerPrioridade =
            findViewById<Spinner>(R.id.spinnerPrioridade)

        val btnSalvar =
            findViewById<Button>(R.id.btnSalvar)

        val db =
            DBHelper(this)

        val id =
            intent.getIntExtra("id", 0)

        edtTitulo.setText(
            intent.getStringExtra("titulo") ?: ""
        )

        edtTipo.setText(
            intent.getStringExtra("tipo") ?: ""
        )

        edtMateria.setText(
            intent.getStringExtra("materia") ?: ""
        )

        edtDescricao.setText(
            intent.getStringExtra("descricao") ?: ""
        )

        edtInicio.setText(
            intent.getStringExtra("horaInicio") ?: ""
        )

        edtFim.setText(
            intent.getStringExtra("horaFim") ?: ""
        )

        edtLinks.setText(
            intent.getStringExtra("links") ?: ""
        )

        val tags =
            db.getTags()

        val adapterTags =
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                tags
            )

        edtTipo.setAdapter(adapterTags)

        val materias =
            db.getMaterias()

        val adapterMaterias =
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                materias
            )

        edtMateria.setAdapter(adapterMaterias)

        val prioridades =
            arrayOf(
                "Baixa",
                "Média",
                "Alta"
            )

        val adapterPrioridade =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                prioridades
            )

        spinnerPrioridade.adapter =
            adapterPrioridade

        val prioridadeAtual =
            intent.getStringExtra("prioridade") ?: "Média"

        val posicaoPrioridade =
            prioridades.indexOf(prioridadeAtual)

        if (posicaoPrioridade >= 0) {

            spinnerPrioridade.setSelection(
                posicaoPrioridade
            )
        }

        edtInicio.setOnClickListener {

            abrirTimePicker(edtInicio)
        }

        edtFim.setOnClickListener {

            abrirTimePicker(edtFim)
        }

        btnSalvar.setOnClickListener {

            val titulo =
                edtTitulo.text.toString().trim()

            val tipo =
                edtTipo.text.toString().trim()

            val materia =
                edtMateria.text.toString().trim()

            val descricao =
                edtDescricao.text.toString().trim()

            val inicio =
                edtInicio.text.toString().trim()

            val fim =
                edtFim.text.toString().trim()

            val links =
                edtLinks.text.toString().trim()

            val prioridade =
                spinnerPrioridade.selectedItem.toString()

            if (
                id == 0 ||
                titulo.isEmpty() ||
                tipo.isEmpty() ||
                materia.isEmpty() ||
                inicio.isEmpty() ||
                fim.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    getString(R.string.preencha_campos),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            db.atualizarEvento(
                id,
                titulo,
                tipo,
                materia,
                descricao,
                inicio,
                fim,
                links,
                prioridade
            )

            NotificationHelper(this)
                .mostrarNotificacao(
                    titulo,
                    "Evento atualizado"
                )

            Toast.makeText(
                this,
                "Evento atualizado",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }

    private fun abrirTimePicker(
        editText: EditText
    ) {

        val calendar =
            Calendar.getInstance()

        val hora =
            calendar.get(Calendar.HOUR_OF_DAY)

        val minuto =
            calendar.get(Calendar.MINUTE)

        val dialog =
            TimePickerDialog(
                this,

                { _, hourOfDay, minute ->

                    val horario =
                        String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            hourOfDay,
                            minute
                        )

                    editText.setText(
                        horario
                    )
                },

                hora,
                minuto,
                true
            )

        dialog.show()
    }
}