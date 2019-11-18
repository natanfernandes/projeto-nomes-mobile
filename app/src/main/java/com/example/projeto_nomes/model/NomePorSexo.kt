package com.example.projeto_nomes.model

import java.io.Serializable


class NomePorSexo : Serializable{
    public var nome: String? = null
    public var sexo: String? = null
    public var localidade: String? = null
    public var res: Array<Res>? = null
}