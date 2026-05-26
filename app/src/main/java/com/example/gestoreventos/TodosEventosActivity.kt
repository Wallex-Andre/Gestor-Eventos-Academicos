package com.example.gestoreventos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoreventos.adapter.EventoAdapter
import com.example.gestoreventos.db.DBHelper
import com.example.gestoreventos.utils.ThemeManager

class TodosEventosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeManager.aplicarTema(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_todos_eventos)

        val recycler =
            findViewById<RecyclerView>(
                R.id.recyclerTodos
            )

        recycler.layoutManager =
            LinearLayoutManager(this)

        val db = DBHelper(this)

        val lista =
            db.getTodosEventos()

        val adapter =
            EventoAdapter(

                context = this,

                lista = lista,

                onConcluir = { evento ->

                    db.concluirEvento(evento.id)

                    recycler.adapter =
                        EventoAdapter(
                            context = this,
                            lista = db.getTodosEventos(),
                            onConcluir = { },
                            onExcluir = { }
                        )
                },

                onExcluir = { evento ->

                    db.deletarEvento(evento.id)

                    recycler.adapter =
                        EventoAdapter(
                            context = this,
                            lista = db.getTodosEventos(),
                            onConcluir = { },
                            onExcluir = { }
                        )
                }
            )

        recycler.adapter =
            adapter
    }
}