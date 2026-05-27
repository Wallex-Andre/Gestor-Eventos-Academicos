package com.example.gestoreventos.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoreventos.EditarEventoActivity
import com.example.gestoreventos.R
import com.example.gestoreventos.model.Evento
import com.example.gestoreventos.utils.NotificationHelper
import java.time.LocalDate
import java.time.LocalTime
import android.content.Intent
import android.net.Uri

class EventoAdapter(

    private val context: Context,

    private val lista: ArrayList<Evento>,

    private val onConcluir:
        (Evento) -> Unit,

    private val onExcluir:
        (Evento) -> Unit

) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    class EventoViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val txtTitulo:
                TextView =
            view.findViewById(R.id.txtTitulo)

        val txtMateria:
                TextView =
            view.findViewById(R.id.txtMateria)

        val txtHorario:
                TextView =
            view.findViewById(R.id.txtHorario)

        val txtDescricao:
                TextView =
            view.findViewById(R.id.txtDescricao)

        val txtLink: TextView =
            view.findViewById(R.id.txtLink)

        val txtStatus:
                TextView =
            view.findViewById(R.id.txtStatus)

        val txtPrioridade:
                TextView =
            view.findViewById(R.id.txtPrioridade)

        val btnConcluir:
                Button =
            view.findViewById(R.id.btnConcluir)

        val btnExcluir:
                Button =
            view.findViewById(R.id.btnExcluir)

        val btnEditar:
                Button =
            view.findViewById(R.id.btnEditar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventoViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_evento,
                    parent,
                    false
                )

        return EventoViewHolder(view)
    }

    override fun getItemCount(): Int {

        return lista.size
    }

    override fun onBindViewHolder(
        holder: EventoViewHolder,
        position: Int
    ) {

        val evento = lista[position]

        holder.txtTitulo.text =
            evento.titulo

        holder.txtMateria.text =
            "${evento.tipo} • ${evento.materia}"

        val dataFormatada =
            try {

                val data =
                    LocalDate.parse(evento.data)

                "${data.dayOfMonth}/${data.monthValue}/${data.year}"

            } catch (_: Exception) {

                evento.data
            }

        holder.txtHorario.text =
            "$dataFormatada • ${evento.horaInicio} - ${evento.horaFim}"

        holder.txtDescricao.text =
            evento.descricao

        if (
            evento.links.isNotEmpty() &&
            evento.links.startsWith("http")
        ) {

            holder.txtLink.visibility =
                View.VISIBLE

            holder.txtLink.text =
                evento.links

            holder.txtLink.setOnClickListener {

                try {

                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(evento.links)
                    )

                    context.startActivity(intent)

                } catch (_: Exception) {

                }
            }

        } else {

            holder.txtLink.visibility =
                View.GONE
        }

        holder.txtPrioridade.text =
            "Prioridade: ${evento.prioridade}"

        if (evento.prioridade == "Alta") {

            holder.txtPrioridade.setTextColor(
                Color.RED
            )

        } else if (evento.prioridade == "Média") {

            holder.txtPrioridade.setTextColor(
                Color.rgb(255, 165, 0)
            )

        } else {

            holder.txtPrioridade.setTextColor(
                Color.GREEN
            )
        }

        var atrasado = false

        try {

            val hoje =
                LocalDate.now()

            val agora =
                LocalTime.now()

            val dataEvento =
                LocalDate.parse(evento.data)

            val horaFim =
                LocalTime.parse(evento.horaFim)

            if (
                evento.concluido == 0 &&
                (
                        dataEvento.isBefore(hoje)
                                ||
                                (
                                        dataEvento == hoje &&
                                                agora.isAfter(horaFim)
                                        )
                        )
            ) {

                atrasado = true
            }

        } catch (_: Exception) {
        }

        when {

            evento.concluido == 1 -> {

                holder.txtStatus.text =
                    "✓ Concluído"

                holder.txtStatus.setTextColor(
                    Color.GREEN
                )
            }

            atrasado -> {

                holder.txtStatus.text =
                    "⚠ Pendente • Atrasado"

                holder.txtStatus.setTextColor(
                    Color.RED
                )
            }

            else -> {

                holder.txtStatus.text =
                    "⧖ Pendente"

                holder.txtStatus.setTextColor(
                    Color.parseColor("#FFC107")
                )
            }
        }

        holder.btnConcluir.text =
            if (evento.concluido == 1)
                "Desfazer"
            else
                "Concluir"

        holder.btnConcluir.setOnClickListener {

            val concluindo =
                evento.concluido == 0

            androidx.appcompat.app.AlertDialog
                .Builder(context)

                .setTitle(
                    if (concluindo)
                        "Concluir evento"
                    else
                        "Desfazer conclusão"
                )

                .setMessage(
                    if (concluindo)
                        "Deseja marcar como concluído?"
                    else
                        "Deseja marcar como pendente?"
                )

                .setPositiveButton(
                    if (concluindo)
                        "Concluir"
                    else
                        "Desfazer"
                ) { _, _ ->

                    onConcluir(evento)
                }

                .setNegativeButton(
                    "Cancelar",
                    null
                )

                .show()
        }

        holder.btnExcluir.setOnClickListener {

            androidx.appcompat.app.AlertDialog
                .Builder(context)

                .setTitle("Excluir evento")

                .setMessage(
                    "Deseja realmente excluir ${evento.titulo}?"
                )

                .setPositiveButton("Excluir") { _, _ ->
                    onExcluir(evento)
                }

                .setNegativeButton("Cancelar", null)

                .show()
        }

        holder.btnEditar.setOnClickListener {

            AlertDialog.Builder(context)
                .setTitle("Editar evento")
                .setMessage(
                    "Deseja editar \"${evento.titulo}\"?"
                )

                .setPositiveButton("Editar") { _, _ ->

                    val intent =
                        Intent(
                            context,
                            EditarEventoActivity::class.java
                        )

                    intent.putExtra(
                        "id",
                        evento.id
                    )

                    intent.putExtra(
                        "titulo",
                        evento.titulo
                    )

                    intent.putExtra(
                        "tipo",
                        evento.tipo
                    )

                    intent.putExtra(
                        "materia",
                        evento.materia
                    )

                    intent.putExtra(
                        "descricao",
                        evento.descricao
                    )

                    intent.putExtra(
                        "horaInicio",
                        evento.horaInicio
                    )

                    intent.putExtra(
                        "horaFim",
                        evento.horaFim
                    )

                    intent.putExtra(
                        "links",
                        evento.links
                    )

                    intent.putExtra(
                        "prioridade",
                        evento.prioridade
                    )

                    context.startActivity(intent)
                }

                .setNegativeButton(
                    "Cancelar",
                    null
                )
                .show()
        }
    }
}