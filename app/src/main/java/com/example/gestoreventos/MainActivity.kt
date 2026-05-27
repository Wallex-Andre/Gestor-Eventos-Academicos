package com.example.gestoreventos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestoreventos.adapter.EventoAdapter
import com.example.gestoreventos.db.DBHelper
import com.example.gestoreventos.model.Evento
import com.example.gestoreventos.utils.NotificationHelper
import com.example.gestoreventos.utils.ThemeManager
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale
import java.time.format.TextStyle
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var calendarView:
            com.kizitonwose.calendar.view.CalendarView

    private lateinit var recyclerEventos:
            RecyclerView

    private lateinit var txtSemEventos:
            TextView

    private lateinit var txtMesAno:
            TextView

    private lateinit var btnAnterior:
            ImageButton

    private lateinit var btnProximo:
            ImageButton

    private lateinit var btnDashboard:
            Button

    private lateinit var btnTodosEventos:
            Button

    private lateinit var adapter:
            EventoAdapter

    private lateinit var db:
            DBHelper

    private var dataSelecionada:
            LocalDate? = null

    private var mesAtual =
        YearMonth.now()

    private val notificacoesEnviadas =
        mutableSetOf<String>()

    private val datasComEventos =
        mutableSetOf<String>()

    private lateinit var btnNovoEvento:
            com.google.android.material.floatingactionbutton.FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeManager.aplicarTema(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        db = DBHelper(this)

        calendarView =
            findViewById(R.id.calendarView)

        recyclerEventos =
            findViewById(R.id.recyclerEventos)

        txtSemEventos =
            findViewById(R.id.txtSemEventos)

        txtMesAno =
            findViewById(R.id.txtMesAno)

        btnAnterior =
            findViewById(R.id.btnAnterior)

        btnProximo =
            findViewById(R.id.btnProximo)

        btnDashboard =
            findViewById(R.id.btnDashboard)

        btnTodosEventos =
            findViewById(R.id.btnTodosEventos)

        btnNovoEvento =
            findViewById(R.id.btnNovoEvento)

        recyclerEventos.layoutManager =
            LinearLayoutManager(this)

        carregarTodosEventos()

        atualizarMes()

        txtMesAno.setOnClickListener {

            val meses = arrayOf(
                "Janeiro",
                "Fevereiro",
                "Março",
                "Abril",
                "Maio",
                "Junho",
                "Julho",
                "Agosto",
                "Setembro",
                "Outubro",
                "Novembro",
                "Dezembro"
            )

            AlertDialog.Builder(this)

                .setTitle("Escolha")

                .setItems(
                    arrayOf(
                        "Selecionar mês",
                        "Selecionar ano"
                    )
                ) { _, opcao ->

                    if (opcao == 0) {

                        AlertDialog.Builder(this)

                            .setTitle("Escolha o mês")

                            .setItems(meses) { _, which ->

                                irParaMes(
                                    YearMonth.of(
                                        mesAtual.year,
                                        which + 1
                                    )
                                )
                            }

                            .show()

                    } else {

                        val anos =
                            (2020..2035)
                                .map { it.toString() }
                                .toTypedArray()

                        AlertDialog.Builder(this)

                            .setTitle("Escolha o ano")

                            .setItems(anos) { _, index ->

                                irParaMes(
                                    YearMonth.of(
                                        anos[index].toInt(),
                                        mesAtual.monthValue
                                    )
                                )
                            }

                            .show()
                    }
                }

                .show()
        }

        val currentMonth =
            YearMonth.now()

        calendarView.setup(
            currentMonth.minusMonths(24),
            currentMonth.plusMonths(24),
            DayOfWeek.SUNDAY
        )

        calendarView.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) :
            ViewContainer(view) {

            val textDia =
                view.findViewById<TextView>(
                    R.id.calendarDayText
                )

            val layoutDia =
                view.findViewById<View>(
                    R.id.layoutDia
                )

            val indicador =
                view.findViewById<View>(
                    R.id.indicadorEvento
                )
        }

        calendarView.dayBinder =
            object : MonthDayBinder<DayViewContainer> {

                override fun create(
                    view: View
                ): DayViewContainer {

                    return DayViewContainer(view)
                }

                override fun bind(
                    container: DayViewContainer,
                    data: CalendarDay
                ) {

                    container.textDia.text =
                        data.date.dayOfMonth.toString()

                    if (
                        data.position ==
                        DayPosition.MonthDate
                    ) {

                        container.textDia.visibility =
                            View.VISIBLE

                        container.textDia.alpha =
                            1.0f

                    } else {

                        container.textDia.visibility =
                            View.VISIBLE

                        container.textDia.alpha =
                            0.35f
                    }

                    container.layoutDia.setBackgroundResource(
                        R.drawable.bg_calendar_day
                    )

                    if (
                        datasComEventos.contains(
                            data.date.toString()
                        )
                    ) {

                        container.indicador.visibility =
                            View.VISIBLE

                    } else {

                        container.indicador.visibility =
                            View.INVISIBLE
                    }

                    if (
                        data.date ==
                        LocalDate.now()
                    ) {

                        container.layoutDia.setBackgroundResource(
                            R.drawable.bg_dia_atual
                        )
                    }

                    if (
                        data.date ==
                        dataSelecionada
                    ) {

                        container.layoutDia.setBackgroundResource(
                            R.drawable.bg_dia_selecionado
                        )
                    }

                    container.view.setOnClickListener {

                        if (
                            data.position !=
                            DayPosition.MonthDate
                        ) {
                            return@setOnClickListener
                        }

                        val dataAntiga =
                            dataSelecionada

                        dataSelecionada =
                            data.date

                        carregarEventos(
                            data.date.toString()
                        )

                        mesAtual =
                            YearMonth.from(data.date)

                        atualizarMes()

                        if (dataAntiga != null) {

                            calendarView.notifyDateChanged(
                                dataAntiga
                            )
                        }

                        calendarView.notifyDateChanged(
                            data.date
                        )
                    }
                }
            }

        btnAnterior.setOnClickListener {

            irParaMes(
                mesAtual.minusMonths(1)
            )
        }

        btnProximo.setOnClickListener {

            irParaMes(
                mesAtual.plusMonths(1)
            )
        }

        btnDashboard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    DashboardActivity::class.java
                )
            )
        }

        btnTodosEventos.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    TodosEventosActivity::class.java
                )
            )
        }

        btnNovoEvento.setOnClickListener {

            val intent =
                Intent(
                    this,
                    EventoActivity::class.java
                )

            val dataEvento =
                dataSelecionada ?: LocalDate.now()

            intent.putExtra(
                "data",
                dataEvento.toString()
            )

            startActivity(intent)
        }
    }

    private fun irParaMes(
        novoMes: YearMonth
    ) {

        mesAtual =
            novoMes

        dataSelecionada =
            mesAtual.atDay(1)

        calendarView.scrollToMonth(
            mesAtual
        )

        carregarEventos(
            dataSelecionada.toString()
        )

        atualizarMes()

        calendarView.notifyCalendarChanged()
    }

    override fun onResume() {

        super.onResume()

        if (dataSelecionada != null) {

            carregarEventos(
                dataSelecionada.toString()
            )

        } else {

            carregarTodosEventos()
        }

        verificarEventos()

        calendarView.notifyCalendarChanged()
    }

    private fun atualizarMes() {

        val nomeMes =
            mesAtual.month.getDisplayName(
                TextStyle.FULL,
                Locale("pt", "BR")
            ).replaceFirstChar {

                if (it.isLowerCase()) {
                    it.titlecase(
                        Locale("pt", "BR")
                    )
                } else {
                    it.toString()
                }
            }

        txtMesAno.text =
            "$nomeMes ${mesAtual.year}"
    }

    private fun carregarEventos(
        data: String
    ) {

        val listaEventos:
                ArrayList<Evento> =
            db.getEventosPorData(data)

        adapter = EventoAdapter(

            context = this,

            lista = listaEventos,

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

                carregarEventos(data)

                calendarView.notifyCalendarChanged()
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

                carregarEventos(data)
            }
        )

        recyclerEventos.adapter =
            adapter

        txtSemEventos.visibility =
            if (listaEventos.isEmpty())
                View.VISIBLE
            else
                View.GONE
    }

    private fun carregarTodosEventos() {

        val lista =
            db.getTodosEventos()

        datasComEventos.clear()

        lista.forEach {

            datasComEventos.add(it.data)
        }

        adapter = EventoAdapter(

            context = this,

            lista = lista,

            onConcluir = { evento ->

                db.alterarConclusao(
                    evento.id,
                    if (evento.concluido == 1) 0 else 1
                )

                NotificationHelper(this)
                    .mostrarNotificacao(
                        evento.titulo,
                        "Evento concluído"
                    )

                carregarTodosEventos()

                calendarView.notifyCalendarChanged()
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

                carregarTodosEventos()

                calendarView.notifyCalendarChanged()
            }
        )

        recyclerEventos.adapter =
            adapter

        txtSemEventos.visibility =
            if (lista.isEmpty())
                View.VISIBLE
            else
                View.GONE
    }

    private fun verificarEventos() {

        val helper =
            NotificationHelper(this)

        val hoje =
            LocalDate.now().toString()

        val agora =
            java.time.LocalTime.now()

        val eventos =
            db.getEventosPorData(hoje)

        eventos.forEach { evento ->

            try {

                val inicio =
                    java.time.LocalTime.parse(
                        evento.horaInicio
                    )

                val fim =
                    java.time.LocalTime.parse(
                        evento.horaFim
                    )

                // EVENTO COMEÇOU

                if (
                    agora.hour == inicio.hour &&
                    agora.minute == inicio.minute
                ) {

                    if (
                        !notificacoesEnviadas.contains(
                            "${evento.id}_inicio"
                        )
                    ) {

                        helper.mostrarNotificacao(
                            evento.titulo,
                            "Seu evento começou agora"
                        )

                        notificacoesEnviadas.add(
                            "${evento.id}_inicio"
                        )
                    }
                }

                // EVENTO ATRASADO

                if (
                    agora.isAfter(fim) &&
                    evento.concluido == 0
                ) {

                    if (
                        !notificacoesEnviadas.contains(
                            "${evento.id}_atrasado"
                        )
                    ) {

                        helper.mostrarNotificacao(
                            evento.titulo,
                            "Evento pendente fora do horário"
                        )

                        notificacoesEnviadas.add(
                            "${evento.id}_atrasado"
                        )
                    }
                }

            } catch (_: Exception) {

            }
        }
    }
}