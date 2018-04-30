package com.example.carlos.projetocultural.domain

/**
 * Created by carlo on 16/03/2018.
 */

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pubuser")
public class Pubuser :Parcelable {
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
        return "Pubuser{nome='$nome'}"
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
        dest.writeString(this.campo1)
        dest.writeString(this.campo2)
        dest.writeString(this.campo3)
        dest.writeString(this.campo4)
        dest.writeString(this.campo5)
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
        this.campo1 = parcel.readString()
        this.campo2 = parcel.readString()
        this.campo3 = parcel.readString()
        this.campo4 = parcel.readString()
        this.campo5 = parcel.readString()
        this.img1 = parcel.readString()
        this.img2 = parcel.readString()
        this.img3 = parcel.readString()
        this.img4 = parcel.readString()
        this.latitude = parcel.readString()
        this.longitude = parcel.readString()
    }

    companion object {
        private val serialVersionUID = 6601006766832473959L
        @JvmField val CREATOR: Parcelable.Creator<Pubuser> = object : Parcelable.Creator<Pubuser> {
            override fun createFromParcel(p: Parcel): Pubuser {
                // Cria o objeto carro com um Parcel
                val c = Pubuser()
                c.readFromParcel(p)
                return c
            }

            override fun newArray(size: Int): Array<Pubuser?> {
                // Retorna um array vazio
                return arrayOfNulls(size)
            }
        }
    }
}