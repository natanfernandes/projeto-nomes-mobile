package com.example.projeto_nomes.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class NomePorSexo(id: Long, nome: String, sexo: String, localidade : String, resStr: String, freq : String) : Serializable{
    public var id: Long = id
    public var nome: String? = nome
    public var sexo: String? = sexo
    public var localidade: String? = localidade
    public var res: Array<Res>? = null
    public var resStr: String? = resStr
    public var frequencia: String? = freq
}