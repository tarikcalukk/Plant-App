package ba.unsa.etf.rma24.projekat.pomocneKlase

import android.os.Parcel
import android.os.Parcelable

enum class Zemljiste(val naziv: String) : Parcelable {
    PJESKOVITO("Pjeskovito zemljište"),
    GLINENO("Glinеno zemljište"),
    ILOVACA("Ilovača"),
    CRNICA("Crnica"),
    SLJUNKOVITO("Šljunkovito zemljište"),
    KRECNJACKO("Krečnjačko zemljište");

    constructor(parcel: Parcel) : this(parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(naziv)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Zemljiste> {
        override fun createFromParcel(parcel: Parcel): Zemljiste {
            val opis = parcel.readString() ?: ""
            return values().find { it.naziv == opis } ?: PJESKOVITO
        }

        override fun newArray(size: Int): Array<Zemljiste?> {
            return arrayOfNulls(size)
        }
    }

}