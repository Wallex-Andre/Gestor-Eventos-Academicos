package com.example.gestoreventos.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.gestoreventos.model.Evento

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        val query = (
                "CREATE TABLE $TABLE_NAME (" +
                        "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$TITULO_COL TEXT, " +
                        "$TIPO_COL TEXT, " +
                        "$MATERIA_COL TEXT, " +
                        "$DESCRICAO_COL TEXT, " +
                        "$DATA_COL TEXT, " +
                        "$HORA_INICIO_COL TEXT, " +
                        "$HORA_FIM_COL TEXT, " +
                        "$LINKS_COL TEXT, " +
                        "$STATUS_COL TEXT, " +
                        "$PRIORIDADE_COL TEXT, " +
                        "$CONCLUIDO_COL INTEGER)"
                )

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")

        onCreate(db)
    }

    fun addEvento(
        titulo: String,
        tipo: String,
        materia: String,
        descricao: String,
        data: String,
        horaInicio: String,
        horaFim: String,
        links: String,
        status: String,
        prioridade: String,
        concluido: Int
    ) {

        val values = ContentValues()

        values.put(TITULO_COL, titulo)
        values.put(TIPO_COL, tipo)
        values.put(MATERIA_COL, materia)
        values.put(DESCRICAO_COL, descricao)
        values.put(DATA_COL, data)
        values.put(HORA_INICIO_COL, horaInicio)
        values.put(HORA_FIM_COL, horaFim)
        values.put(LINKS_COL, links)
        values.put(STATUS_COL, status)
        values.put(PRIORIDADE_COL, prioridade)
        values.put(CONCLUIDO_COL, concluido)

        val db = writableDatabase

        db.insert(TABLE_NAME, null, values)

        db.close()
    }

    fun getTags(): ArrayList<String> {

        val lista = ArrayList<String>()

        val db = readableDatabase

        val query =
            "SELECT DISTINCT tipo FROM Evento ORDER BY tipo ASC"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            do {

                val tipo = cursor.getString(0)

                if (!tipo.isNullOrEmpty()) {

                    lista.add(tipo)
                }

            } while (cursor.moveToNext())
        }

        cursor.close()

        db.close()

        return lista
    }

    fun getMaterias(): ArrayList<String> {

        val lista = ArrayList<String>()

        val db = readableDatabase

        val query =
            "SELECT DISTINCT materia FROM Evento ORDER BY materia ASC"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            do {

                val materia = cursor.getString(0)

                if (!materia.isNullOrEmpty()) {

                    lista.add(materia)
                }

            } while (cursor.moveToNext())
        }

        cursor.close()

        db.close()

        return lista
    }
    fun getEventosPorData(data: String): ArrayList<Evento> {

        val lista = ArrayList<Evento>()

        val db = readableDatabase

        val query =
            "SELECT * FROM $TABLE_NAME " +
                    "WHERE $DATA_COL = ? " +
                    "ORDER BY $HORA_INICIO_COL ASC"

        val cursor = db.rawQuery(
            query,
            arrayOf(data)
        )

        if (cursor.moveToFirst()) {

            do {

                lista.add(
                    Evento(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getInt(11)
                    )
                )

            } while (cursor.moveToNext())
        }

        cursor.close()

        db.close()

        return lista
    }

    fun getTodosEventos(): ArrayList<Evento> {

        val lista = ArrayList<Evento>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME " +
                    "ORDER BY $DATA_COL ASC, $HORA_INICIO_COL ASC",
            null
        )

        if (cursor.moveToFirst()) {

            do {

                lista.add(
                    Evento(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getInt(11)
                    )
                )

            } while (cursor.moveToNext())
        }

        cursor.close()

        db.close()

        return lista
    }

    fun concluirEvento(id: Int) {

        val db = writableDatabase

        val values = ContentValues()

        if (id > 0) {

            val cursor = db.rawQuery(
                "SELECT concluido FROM Evento WHERE id = ?",
                arrayOf(id.toString())
            )

            if (cursor.moveToFirst()) {

                val atual =
                    cursor.getInt(0)

                values.put(
                    CONCLUIDO_COL,
                    if (atual == 1) 0 else 1
                )

                values.put(
                    STATUS_COL,
                    if (atual == 1)
                        "Pendente"
                    else
                        "Concluído"
                )
            }

            cursor.close()
        }

        db.update(TABLE_NAME, values, "$ID_COL=?", arrayOf(id.toString()))

        db.close()
    }

    fun deletarEvento(id: Int) {

        val db = writableDatabase

        db.delete(TABLE_NAME, "$ID_COL=?", arrayOf(id.toString()))

        db.close()
    }

    fun pesquisarEventos(texto: String): ArrayList<Evento> {

        val lista = ArrayList<Evento>()

        val db = readableDatabase

        val query =
            "SELECT * FROM Evento " +
                    "WHERE titulo LIKE ? OR materia LIKE ? " +
                    "ORDER BY data ASC, horaInicio ASC"

        val valor = "%$texto%"

        val cursor = db.rawQuery(query, arrayOf(valor, valor))

        if (cursor.moveToFirst()) {

            do {

                lista.add(
                    Evento(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getInt(11)
                    )
                )

            } while (cursor.moveToNext())
        }

        cursor.close()

        db.close()

        return lista
    }

    fun atualizarEvento(
        id: Int,
        titulo: String,
        tipo: String,
        materia: String,
        descricao: String
    ) {

        val db = writableDatabase

        val values = ContentValues()

        values.put(TITULO_COL, titulo)
        values.put(TIPO_COL, tipo)
        values.put(MATERIA_COL, materia)
        values.put(DESCRICAO_COL, descricao)

        db.update(
            "Evento",
            values,
            "id=?",
            arrayOf(id.toString())
        )

        db.close()
    }

    fun getEventosConcluidos(): ArrayList<Evento> {

        val lista = ArrayList<Evento>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM Evento " +
                    "WHERE concluido = 1 " +
                    "ORDER BY data ASC, horaInicio ASC",
            null
        )

        if (cursor.moveToFirst()) {

            do {

                lista.add(
                    Evento(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getInt(11)
                    )
                )

            } while (cursor.moveToNext())
        }

        cursor.close()

        db.close()

        return lista
    }

    fun alterarConclusao(
        id: Int,
        concluido: Int
    ) {

        val db = writableDatabase

        val values =
            ContentValues()

        values.put(
            CONCLUIDO_COL,
            concluido
        )

        values.put(
            STATUS_COL,
            if (concluido == 1)
                "Concluído"
            else
                "Pendente"
        )

        db.update(
            TABLE_NAME,
            values,
            "$ID_COL=?",
            arrayOf(id.toString())
        )

        db.close()
    }

    companion object {

        private const val DATABASE_NAME = "GestorEventos"

        private const val DATABASE_VERSION = 3

        private const val TABLE_NAME = "Evento"

        private const val ID_COL = "id"

        private const val TITULO_COL = "titulo"

        private const val TIPO_COL = "tipo"

        private const val MATERIA_COL = "materia"

        private const val DESCRICAO_COL = "descricao"

        private const val DATA_COL = "data"

        private const val HORA_INICIO_COL = "horaInicio"

        private const val HORA_FIM_COL = "horaFim"

        private const val LINKS_COL = "links"

        private const val STATUS_COL = "status"

        private const val PRIORIDADE_COL = "prioridade"

        private const val CONCLUIDO_COL = "concluido"
    }
}