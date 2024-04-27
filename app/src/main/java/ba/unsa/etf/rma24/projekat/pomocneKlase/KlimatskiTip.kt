package ba.unsa.etf.rma24.projekat.pomocneKlase

import android.os.Parcel
import android.os.Parcelable

enum class KlimatskiTip(val opis: String) : Parcelable {
    SREDOZEMNA("Mediteranska klima - suha, topla ljeta i blage zime"),
    TROPSKA("Tropska klima - topla i vlažna tokom cijele godine"),
    SUBTROPSKA("Subtropska klima - blage zime i topla do vruća ljeta"),
    UMJERENA("Umjerena klima - topla ljeta i hladne zime"),
    SUHA("Sušna klima - niske padavine i visoke temperature tokom cijele godine"),
    PLANINSKA("Planinska klima - hladne temperature i kratke sezone rasta");

    constructor(parcel: Parcel) : this(parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(opis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KlimatskiTip> {
        override fun createFromParcel(parcel: Parcel): KlimatskiTip {
            val opis = parcel.readString() ?: ""
            return entries.find { it.opis == opis } ?: SREDOZEMNA
        }

        override fun newArray(size: Int): Array<KlimatskiTip?> {
            return arrayOfNulls(size)
        }
    }
}
