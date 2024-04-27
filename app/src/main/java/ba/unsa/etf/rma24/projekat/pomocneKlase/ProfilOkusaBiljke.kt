package ba.unsa.etf.rma24.projekat.pomocneKlase

import android.os.Parcel
import android.os.Parcelable

enum class ProfilOkusaBiljke(val opis: String) : Parcelable {
    MENTA("Mentol - osvježavajući, hladan ukus"),
    CITRUSNI("Citrusni - osvježavajući, aromatičan"),
    SLATKI("Sladak okus"),
    BEZUKUSNO("Obični biljni okus - travnat, zemljast ukus"),
    LJUTO("Ljuto ili papreno"),
    KORIJENASTO("Korenast - drvenast i gorak ukus"),
    AROMATICNO("Začinski - topli i aromatičan ukus"),
    GORKO("Gorak okus");

    constructor(parcel: Parcel) : this(parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(opis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfilOkusaBiljke> {
        override fun createFromParcel(parcel: Parcel): ProfilOkusaBiljke {
            return valueOf(parcel.readString() ?: "")
        }

        override fun newArray(size: Int): Array<ProfilOkusaBiljke?> {
            return arrayOfNulls(size)
        }
    }
}