package com.example.projeto_nomes.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.projeto_nomes.model.NomePorSexo
import com.example.projeto_nomes.model.Res

class SQLiteRepository(ctx: Context): NomesRepository {



    private  val helper: NomeSQLHelper = NomeSQLHelper(ctx)

    private fun insert(nomePorSexo: NomePorSexo){
        Log.d("identificador", nomePorSexo.id.toString())
        val db = helper.writableDatabase
        val cv = ContentValues().apply {
            put(COLUMN_NOME, nomePorSexo.nome)
        }
        val id = db.insert(TABLE_NAME, null, cv)
        if (id != -1L){
            nomePorSexo.id = id
        }
        db.close()
    }

    private fun update(nomePorSexo: NomePorSexo){
        val db = helper.writableDatabase

        val cv = ContentValues().apply {
            put(COLUMN_NOME, nomePorSexo.nome)
        }

        db.update(
            TABLE_NAME,
            cv,
            "$COLUMN_ID = ? ",
            arrayOf(nomePorSexo.id.toString())
        )

        db.close()

    }


    override fun save(nomePorSexo: NomePorSexo) {


        insert(nomePorSexo)
    }

    override fun remove(nomePorSexo: NomePorSexo) {
        val db = helper.writableDatabase

        db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ? ",
            arrayOf(nomePorSexo.id.toString())
        )

        db.close()
    }

    fun cleanTables(){
        val db = helper.writableDatabase

        db.execSQL("delete from "+ TABLE_NAME);

        db.close()
    }

    override fun list(callback: (MutableList<NomePorSexo>) -> Unit) {
        var sql = "SELECT * FROM $TABLE_NAME"
        var args: Array<String>? = null

        sql += " ORDER BY $COLUMN_ID"
        val db = helper.readableDatabase
        val cursor = db.rawQuery(sql, args)
        val nomes = ArrayList<NomePorSexo>()
        while (cursor.moveToNext()){
            val nomePorSexo = NomeFromCursor(cursor)
            nomes.add(nomePorSexo)
        }
        cursor.close()
        db.close()

        callback(nomes)
    }

    private fun NomeFromCursor(cursor: Cursor): NomePorSexo{
        val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
        val nome = cursor.getString(cursor.getColumnIndex(COLUMN_NOME))
        val sexo = cursor.getString(cursor.getColumnIndex(COLUMN_SEXO))
        val localidade = cursor.getString(cursor.getColumnIndex(COLUMN_LOCALIDADE))

        return NomePorSexo()
    }

}