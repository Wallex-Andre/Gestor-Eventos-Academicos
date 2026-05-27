package com.example.gestoreventos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoreventos.adapter.EventoAdapter
import com.example.gestoreventos.db.DBHelper
import com.example.gestoreventos.utils.NotificationHelper
import com.example.gestoreventos.utils.ThemeManager

class TodosEventosActivity : AppCompatActivity() {

    private lateinit var recycler:
            RecyclerView

    private lateinit var db:
            DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeManager.aplicarTema(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_todos_eventos)

        recycler =
            findViewById(R.id.recyclerTodos)

        recycler.layoutManager =
            LinearLayoutManager(this)

        db =
            DBHelper(this)

        carregarLista()
    }

    private fun carregarLista() {

        val adapter =
            EventoAdapter(

                context = this,

                lista = db.getTodosEventos(),

                onConcluir = { evento ->

                    db.alterarConclusao(
                        evento.id,
                        if (evento.concluido == 1) 0 else 1
                    )

                    NotificationHelper(this)
                        .mostrarNotificacao(
                            evento.titulo,
                            "Status do evento atualizado"
                        )

                    carregarLista()
                },

                onExcluir = { evento ->

                    db.deletarEvento(
                        evento.id
                    )

                    NotificationHelper(this)
                        .mostrarNotificacao(
                            evento.titulo,
                            "Evento excluído"
                        )

                    carregarLista()
                }
            )

        recycler.adapter =
            adapter
    }
}