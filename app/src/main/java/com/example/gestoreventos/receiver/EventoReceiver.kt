package com.example.gestoreventos.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.gestoreventos.utils.NotificationHelper

class EventoReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val titulo =
            intent.getStringExtra(
                "titulo"
            ) ?: "Evento"

        val mensagem =
            intent.getStringExtra(
                "mensagem"
            ) ?: "Lembrete"

        val helper =
            NotificationHelper(context)

        helper.mostrarNotificacao(
            titulo,
            mensagem
        )
    }
}