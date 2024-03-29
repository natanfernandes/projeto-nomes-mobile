package com.example.projeto_nomes.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NomeSQLHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION){

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {

        sqLiteDatabase.execSQL(
            "CREATE TABLE $TABLE_NAME("+
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "$COLUMN_NOME TEXT NOT NULL," +
                    "$COLUMN_SEXO TEXT," +
                    "$COLUMN_LOCALIDADE TEXT NOT NULL," +
                    "$COLUMN_FREQUENCIA TEXT NOT NULL," +
                    "$COLUMN_RES TEXT NOT NULL);" +
                    "")


    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Próximas versões
    }
}






