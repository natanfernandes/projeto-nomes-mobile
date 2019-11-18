package com.example.projeto_nomes.repository

import com.example.projeto_nomes.model.NomePorSexo


interface NomesRepository {
    fun save(nome: NomePorSexo)
    fun remove(nome: NomePorSexo)
    fun list(callback:(MutableList<NomePorSexo>) -> Unit)
}