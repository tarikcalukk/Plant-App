package ba.unsa.etf.rma24.projekat.pomocneKlase

import android.os.Parcel
import android.os.Parcelable


enum class MedicinskaKorist(val opis: String) : Parcelable {
    SMIRENJE("Smirenje - za smirenje i relaksaciju"),
    PROTUUPALNO("Protuupalno - za smanjenje upale"),
    PROTIVBOLOVA("Protivbolova - za smanjenje bolova"),
    REGULACIJAPRITISKA("Regulacija pritiska - za regulaciju visokog/niskog pritiska"),
    REGULACIJAPROBAVE("Regulacija probave"),
    PODRSKAIMUNITETU("Podrška imunitetu");

    constructor(parcel: Parcel) : this(parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(opis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MedicinskaKorist> {
        override fun createFromParcel(parcel: Parcel): MedicinskaKorist {
            val opis = parcel.readString() ?: ""
            return values().find { it.opis == opis } ?: SMIRENJE // Defaultna vrijednost ako ne pronađe odgovarajuću konstantu
        }

        override fun newArray(size: Int): Array<MedicinskaKorist?> {
            return arrayOfNulls(size)
        }
    }
}