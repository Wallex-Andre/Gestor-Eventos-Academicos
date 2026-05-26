package com.example.gestoreventos.utils

import com.example.gestoreventos.model.Evento

fun filtrarConcluidos(
    lista: ArrayList<Evento>
): ArrayList<Evento> {

    return ArrayList(
        lista.filter {
            it.concluido == 1
        }
    )
}