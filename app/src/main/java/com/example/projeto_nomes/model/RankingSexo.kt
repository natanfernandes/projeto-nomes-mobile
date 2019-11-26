package com.example.projeto_nomes.model


import java.io.Serializable


class RankingSexo(id: Long, frequencia: String, rank: Long, nome: String) : Serializable{
    public var id: Long? = id
    public var sexo: String? = null
    public var localidade: String? = null
    public var res: Array<ResRanking>? = null
    public var resStr: String? = null
    public var frequencia: String? = frequencia
    public var nome: String? = nome
    public var ranking: Long? = rank
}