package com.example.gestoreventos

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestoreventos.db.DBHelper
import com.example.gestoreventos.utils.NotificationHelper
import com.example.gestoreventos.utils.ThemeManager
import java.util.Calendar
import java.util.Locale

class EventoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeManager.aplicarTema(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_evento)

        val txtData =
            findViewById<TextView>(R.id.txtData)

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

        val data =
            intent.getStringExtra("data") ?: ""

        txtData.text =
            getString(
                R.string.data_texto,
                data
            )

        val db = DBHelper(this)

        /*
         * TAGS AUTOMÁTICAS
         */

        val tags: ArrayList<String> =
            db.getTags()

        val adapterTags =
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                tags
            )

        edtTipo.setAdapter(adapterTags)

        /*
         * MATÉRIAS AUTOMÁTICAS
         */

        val materias: ArrayList<String> =
            db.getMaterias()

        val adapterMaterias =
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                materias
            )

        edtMateria.setAdapter(adapterMaterias)

        /*
         * PRIORIDADE
         */

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

        /*
         * RELÓGIO PARA HORA
         */

        edtInicio.setOnClickListener {

            abrirTimePicker(edtInicio)
        }

        edtFim.setOnClickListener {

            abrirTimePicker(edtFim)
        }

        /*
         * SALVAR EVENTO
         */

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

            db.addEvento(
                titulo,
                tipo,
                materia,
                descricao,
                data,
                inicio,
                fim,
                links,
                "Pendente",
                prioridade,
                0
            )

            NotificationHelper(this)
                .mostrarNotificacao(
                    edtTitulo.text.toString(),
                    "Evento criado com sucesso"
                )

            Toast.makeText(
                this,
                getString(R.string.evento_salvo),
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }

    /*
     * TIME PICKER
     */

    private fun abrirTimePicker(
        editText: EditText
    ) {

        val calendar =
            Calendar.getInstance()

        val hora =
            calendar.get(Calendar.HOUR_OF_DAY)

        val minuto =
            calendar.get(Calendar.MINUTE)

        val dialog = TimePickerDialog(
            this,

            { _, hourOfDay, minute ->

                val horario =
                    String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hourOfDay,
                        minute
                    )

                editText.setText(horario)
            },

            hora,
            minuto,
            true
        )

        dialog.show()
    }
}