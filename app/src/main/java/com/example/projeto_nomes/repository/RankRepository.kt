package com.example.projeto_nomes.repository

import com.example.projeto_nomes.model.RankingSexo


interface RankRepository {
    fun save(rank: List<RankingSexo>?)
    fun remove(ranking: RankingSexo)
    fun list(callback:(MutableList<RankingSexo>) -> Unit)
}