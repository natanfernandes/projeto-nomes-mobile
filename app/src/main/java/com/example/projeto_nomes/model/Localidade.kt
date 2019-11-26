package com.example.projeto_nomes.model

import java.io.Serializable

class Localidade(id: Long, nome: String, id_local: String) : Serializable{
    public var id_local: String? = id_local
    public var id: Long? = id
    public var sigla : String? = null
    public var nome : String? = nome
    public var regiao: Regiao? = null
}