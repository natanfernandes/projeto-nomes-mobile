package com.example.projeto_nomes.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.projeto_nomes.model.Localidade

class SQLiteRepository3(ctx: Context): LocalidadeRepository {

    private  val helper: LocalidadeSQLHelper = LocalidadeSQLHelper(ctx)

    private fun insert(itemRanking: Localidade){
        val db = helper.writableDatabase
        val cv = ContentValues().apply {

            put(COLUMN_NOME, itemRanking.nome)
            put(COLUMN_IDLOCAL, itemRanking.id)
        }

        val id = db.insert(TABLE_LOCALIDADE, null, cv)
        if (id != -1L){
            itemRanking.id = id
        }
        db.close()
    }

    private fun update(itemRanking: Localidade){
        val db = helper.writableDatabase

        val cv = ContentValues().apply {
            put(COLUMN_NOME, itemRanking.nome)
        }

        db.update(
            TABLE_LOCALIDADE,
            cv,
            "$COLUMN_ID = ? ",
            arrayOf(itemRanking.id.toString())
        )

        db.close()

    }


    override fun save(rank: List<Localidade>?) {
        for(item in rank!!) {
            insert(item)
        }
    }

    override fun remove(ranking: Localidade) {
        val db = helper.writableDatabase

        db.delete(
            TABLE_LOCALIDADE,
            "$COLUMN_ID = ? ",
            arrayOf(ranking.id.toString())
        )

        db.close()
    }

    fun cleanTables(){
        val db = helper.writableDatabase

        db.execSQL("delete from "+ TABLE_LOCALIDADE);

        db.close()
    }

    override fun list(callback: (MutableList<Localidade>) -> Unit) {
        var sql = "SELECT * FROM $TABLE_LOCALIDADE"
        var args: Array<String>? = null

        val db = helper.readableDatabase
        val cursor = db.rawQuery(sql, args)
        val rank = ArrayList<Localidade>()
        while (cursor.moveToNext()){
            val rankPorSexo = RankFromCursor(cursor)
            rank.add(rankPorSexo)
        }
        cursor.close()
        db.close()

        callback(rank)
    }

    private fun RankFromCursor(cursor: Cursor): Localidade{
        val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
        val nome = cursor.getString(cursor.getColumnIndex(COLUMN_NOME))
        val id_local = cursor.getString(cursor.getColumnIndex(COLUMN_IDLOCAL))
        return Localidade(id,nome,id_local)
    }

}

