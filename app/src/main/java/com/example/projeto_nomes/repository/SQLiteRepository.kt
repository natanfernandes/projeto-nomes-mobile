package com.example.projeto_nomes.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.projeto_nomes.model.NomePorSexo

class SQLiteRepository(ctx: Context): NomesRepository {

    private  val helper: NomeSQLHelper = NomeSQLHelper(ctx)

    private fun insert(nomePorSexo: NomePorSexo){
        val db = helper.writableDatabase
        val cv = ContentValues().apply {
            put(COLUMN_NOME, nomePorSexo.nome)
            put(COLUMN_LOCALIDADE, nomePorSexo.localidade)
            put(COLUMN_SEXO, nomePorSexo.sexo)
            val builder = StringBuilder()
            val freq = StringBuilder()
            for (item in nomePorSexo.res!!) {
                var str = item.periodo?.replace("[","")
                str = str?.replace("]","")
                if(str != null && str != "1930"){
                    str = str.drop(5)
                    Log.d("teste",str)
                }
                builder.append(str)
                builder.append(",")
                freq.append(item.frequencia.toString())
                freq.append(",")
            }
            put(COLUMN_RES, builder.toString())
            put(COLUMN_FREQUENCIA, freq.toString())
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

        Log.d("identificador", nomePorSexo.id.toString())
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
        var sexo = cursor.getString(cursor.getColumnIndex(COLUMN_SEXO))
        val localidade = cursor.getString(cursor.getColumnIndex(COLUMN_LOCALIDADE))
        val res = cursor.getString(cursor.getColumnIndex(COLUMN_RES))
        val freq = cursor.getString(cursor.getColumnIndex(COLUMN_FREQUENCIA))
        if(sexo == null){
            sexo = "M"
        }
        return NomePorSexo(id,nome,sexo,localidade, res, freq)
    }

}

