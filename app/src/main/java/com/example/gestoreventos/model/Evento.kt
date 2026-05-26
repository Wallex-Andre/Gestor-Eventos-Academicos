package com.example.gestoreventos.model

data class Evento(

    val id: Int,

    val titulo: String,

    val tipo: String,

    val materia: String,

    val descricao: String,

    val data: String,

    val horaInicio: String,

    val horaFim: String,

    val links: String,

    val status: String,

    val prioridade: String,

    val concluido: Int
)