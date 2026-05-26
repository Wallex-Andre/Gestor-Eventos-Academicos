package com.example.gestoreventos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestoreventos.db.DBHelper
import com.example.gestoreventos.utils.NotificationHelper
import com.example.gestoreventos.utils.ThemeManager

class EditarEventoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeManager.aplicarTema(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_editar_evento)

        // Campos do formulário
        val edtTitulo = findViewById<EditText>(R.id.edtTitulo)
        val edtTipo = findViewById<EditText>(R.id.edtTipo)
        val edtMateria = findViewById<EditText>(R.id.edtMateria)
        val edtDescricao = findViewById<EditText>(R.id.edtDescricao)

        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        // Recupera os dados enviados
        val id = intent.getIntExtra("id", 0)

        edtTitulo.setText(intent.getStringExtra("titulo"))
        edtTipo.setText(intent.getStringExtra("tipo"))
        edtMateria.setText(intent.getStringExtra("materia"))
        edtDescricao.setText(intent.getStringExtra("descricao"))

        val db = DBHelper(this)

        btnSalvar.setOnClickListener {

            // Atualiza o evento no banco
            db.atualizarEvento(
                id,
                edtTitulo.text.toString(),
                edtTipo.text.toString(),
                edtMateria.text.toString(),
                edtDescricao.text.toString()
            )

            NotificationHelper(this)
                .mostrarNotificacao(
                    edtTitulo.text.toString(),
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
}