package com.example.carlos.projetocultural.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pubpesq")
public class Pubpesq :Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "nome")
    var nome = ""

    @ColumnInfo(name = "redesocial")
    var redesocial = ""

    @ColumnInfo(name = "endereco")
    var endereco = ""

    @ColumnInfo(name = "contato")
    var contato = ""

    @ColumnInfo(name = "email")
    var email = ""

    @ColumnInfo(name = "atvexercida")
    var atvexercida = ""

    @ColumnInfo(name = "categoria")
    var categoria = ""

    @ColumnInfo(name = "anoinicio")
    var anoinicio = ""

    @ColumnInfo(name = "cnpj")
    var cnpj = ""

    @ColumnInfo(name = "representacao")
    var representacao = ""


    @ColumnInfo(name = "recurso")
    var recurso = ""

    @ColumnInfo(name = "pesquisador")
    var pesquisador:Int = 0


    @ColumnInfo(name = "campo1")
    var campo1 = ""
    @ColumnInfo(name = "campo2")
    var campo2 = ""
    @ColumnInfo(name = "campo3")
    var campo3 = ""
    @ColumnInfo(name = "campo4")
    var campo4 = ""
    @ColumnInfo(name = "campo5")
    var campo5 = ""
    @ColumnInfo(name = "campo6")
    var campo6 = ""
    @ColumnInfo(name = "campo7")
    var campo7 = ""
    @ColumnInfo(name = "campo8")
    var campo8 = ""
    @ColumnInfo(name = "campo9")
    var campo9 = ""
    @ColumnInfo(name = "campo10")
    var campo10 = ""
    @ColumnInfo(name = "campo11")
    var campo11 = ""
    @ColumnInfo(name = "campo12")
    var campo12 = ""
    @ColumnInfo(name = "campo13")
    var campo13 = ""
    @ColumnInfo(name = "campo14")
    var campo14 = ""
    @ColumnInfo(name = "campo15")
    var campo15 = ""
    @ColumnInfo(name = "campo16")
    var campo16 = ""
    @ColumnInfo(name = "campo17")
    var campo17 = ""
    @ColumnInfo(name = "campo18")
    var campo18 = ""
    @ColumnInfo(name = "campo19")
    var campo19 = ""
    @ColumnInfo(name = "campo20")
    var campo20 = ""



    @ColumnInfo(name = "img1")
    var img1 :String ?= ""

    @ColumnInfo(name = "img2")
    var img2 :String ?= ""

    @ColumnInfo(name = "img3")
    var img3 :String ?= ""

    @ColumnInfo(name = "img4")
    var img4 :String ?= ""


    @ColumnInfo(name = "latitude")
    var latitude: String = ""
        get() = if (field.trim().isEmpty()) "0.0" else field

    @ColumnInfo(name = "longitude")
    var longitude :String ?= ""
        get() = if (field!!.trim().isEmpty()) "0.0" else field

    override fun toString(): String {
        return "Pubupesq{nome='$nome'}"
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        // Escreve os dados para serem serializados
        dest.writeInt(id)
        dest.writeString(this.nome)
        dest.writeString(this.redesocial)
        dest.writeString(this.endereco)
        dest.writeString(this.contato)
        dest.writeString(this.email)
        dest.writeString(this.atvexercida)
        dest.writeString(this.categoria)

        dest.writeString(this.anoinicio)
        dest.writeString(this.cnpj)
        dest.writeString(this.representacao)
        dest.writeString(this.recurso)
        dest.writeInt(this.pesquisador)

        dest.writeString(this.campo1)
        dest.writeString(this.campo2)
        dest.writeString(this.campo3)
        dest.writeString(this.campo4)
        dest.writeString(this.campo5)

        dest.writeString(this.campo6)
        dest.writeString(this.campo7)
        dest.writeString(this.campo8)
        dest.writeString(this.campo9)
        dest.writeString(this.campo10)

        dest.writeString(this.campo11)
        dest.writeString(this.campo12)
        dest.writeString(this.campo13)
        dest.writeString(this.campo14)
        dest.writeString(this.campo15)

        dest.writeString(this.campo16)
        dest.writeString(this.campo17)
        dest.writeString(this.campo18)
        dest.writeString(this.campo19)
        dest.writeString(this.campo20)

        dest.writeString(this.img1)
        dest.writeString(this.img2)
        dest.writeString(this.img3)
        dest.writeString(this.img4)
        dest.writeString(this.latitude)
        dest.writeString(this.longitude)
    }

    fun readFromParcel(parcel: Parcel) {
        // Lê os dados na mesma ordem em que foram escritos
        this.id = parcel.readInt()
        this.nome = parcel.readString()
        this.redesocial = parcel.readString()
        this.endereco = parcel.readString()
        this.contato = parcel.readString()
        this.email = parcel.readString()
        this.atvexercida = parcel.readString()
        this.categoria = parcel.readString()

        this.anoinicio = parcel.readString()
        this.cnpj = parcel.readString()
        this.representacao = parcel.readString()
        this.recurso = parcel.readString()
        this.pesquisador = parcel.readInt()

        this.campo1 = parcel.readString()
        this.campo2 = parcel.readString()
        this.campo3 = parcel.readString()
        this.campo4 = parcel.readString()
        this.campo5 = parcel.readString()

        this.campo6 = parcel.readString()
        this.campo7 = parcel.readString()
        this.campo8 = parcel.readString()
        this.campo9 = parcel.readString()
        this.campo10 = parcel.readString()

        this.campo11 = parcel.readString()
        this.campo12 = parcel.readString()
        this.campo13 = parcel.readString()
        this.campo14 = parcel.readString()
        this.campo15 = parcel.readString()

        this.campo16 = parcel.readString()
        this.campo17 = parcel.readString()
        this.campo18 = parcel.readString()
        this.campo19 = parcel.readString()
        this.campo20 = parcel.readString()

        this.img1 = parcel.readString()
        this.img2 = parcel.readString()
        this.img3 = parcel.readString()
        this.img4 = parcel.readString()
        this.latitude = parcel.readString()
        this.longitude = parcel.readString()
    }

    companion object {
        private val serialVersionUID = 6601006766832473959L
        @JvmField val CREATOR: Parcelable.Creator<Pubpesq> = object : Parcelable.Creator<Pubpesq> {
            override fun createFromParcel(p: Parcel): Pubpesq {
                // Cria o objeto carro com um Parcel
                val c = Pubpesq()
                c.readFromParcel(p)
                return c
            }

            override fun newArray(size: Int): Array<Pubpesq?> {
                // Retorna um array vazio
                return arrayOfNulls(size)
            }
        }
    }
}