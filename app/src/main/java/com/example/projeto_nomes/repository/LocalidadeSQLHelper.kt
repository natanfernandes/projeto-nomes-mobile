package com.example.projeto_nomes.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalidadeSQLHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_LOCALIDADES,null, DATABASE_VERSION){

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {

        sqLiteDatabase.execSQL(
            "CREATE TABLE $TABLE_LOCALIDADE("+
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "$COLUMN_IDLOCAL TEXT NOT NULL,"+
                    "$COLUMN_NOME TEXT NOT NULL);")

    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Próximas versões
    }
}






