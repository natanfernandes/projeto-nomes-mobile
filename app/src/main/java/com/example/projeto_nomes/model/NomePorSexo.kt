package com.example.projeto_nomes.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class NomePorSexo : Serializable{
    @SerializedName("id_str") var id: Long = 0
    public var nome: String? = null
    public var sexo: String? = null
    public var localidade: String? = null
    public var res: Array<Res>? = null
}