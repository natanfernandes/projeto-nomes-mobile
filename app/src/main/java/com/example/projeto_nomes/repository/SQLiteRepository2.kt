package com.example.projeto_nomes.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.projeto_nomes.model.RankingSexo
import com.example.projeto_nomes.model.ResRanking

class SQLiteRepository2(ctx: Context): RankRepository {

    private  val helper: RankSQLHelper = RankSQLHelper(ctx)

    private fun insert(itemRanking: ResRanking){
        val db = helper.writableDatabase
        val cv = ContentValues().apply {

            put(COLUMN_NOME, itemRanking.nome)
            put(COLUMN_FREQUENCIA, itemRanking.frequencia)
            put(COLUMN_RANKING, itemRanking.ranking)
        }

        val id = db.insert(TABLE_RANKING, null, cv)
        if (id != -1L){
            itemRanking.id = id
        }
        db.close()
    }

    private fun update(itemRanking: RankingSexo){
        val db = helper.writableDatabase

        val cv = ContentValues().apply {
            put(COLUMN_NOME, itemRanking.nome)
        }

        db.update(
            TABLE_RANKING,
            cv,
            "$COLUMN_ID = ? ",
            arrayOf(itemRanking.id.toString())
        )

        db.close()

    }


    override fun save(rank: List<RankingSexo>?) {
        val res = rank?.get(0)
        for(item in res?.res!!) {
            insert(item)
        }
    }

    override fun remove(ranking: RankingSexo) {
        val db = helper.writableDatabase

        db.delete(
            TABLE_RANKING,
            "$COLUMN_ID = ? ",
            arrayOf(ranking.id.toString())
        )

        db.close()
    }

    fun cleanTables(){
        val db = helper.writableDatabase

        db.execSQL("delete from "+ TABLE_RANKING);

        db.close()
    }

    override fun list(callback: (MutableList<RankingSexo>) -> Unit) {
        var sql = "SELECT * FROM $TABLE_RANKING ORDER BY $COLUMN_RANKING ASC"
        var args: Array<String>? = null

        val db = helper.readableDatabase
        val cursor = db.rawQuery(sql, args)
        val rank = ArrayList<RankingSexo>()
        while (cursor.moveToNext()){
            val rankPorSexo = RankFromCursor(cursor)
            rank.add(rankPorSexo)
        }
        cursor.close()
        db.close()

        callback(rank)
    }

    private fun RankFromCursor(cursor: Cursor): RankingSexo{
        val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
        val nome = cursor.getString(cursor.getColumnIndex(COLUMN_NOME))
        val freq = cursor.getString(cursor.getColumnIndex(COLUMN_FREQUENCIA))
        val rank = cursor.getLong(cursor.getColumnIndex(COLUMN_RANKING))
        return RankingSexo(id,freq,rank,nome)
    }

}

