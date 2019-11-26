package com.example.projeto_nomes.repository

import com.example.projeto_nomes.model.Localidade
import com.example.projeto_nomes.model.RankingSexo


interface LocalidadeRepository {
    fun save(rank: List<Localidade>?)
    fun remove(ranking: Localidade)
    fun list(callback:(MutableList<Localidade>) -> Unit)
}