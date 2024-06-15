package ba.unsa.etf.rma24.projekat.pomocneKlase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {
    @TypeConverter
    fun fromMedicinskaKoristList(value: List<MedicinskaKorist>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toMedicinskaKoristList(value: String): List<MedicinskaKorist> {
        return if (value.isBlank()) {
            emptyList()
        } else {
            value.split(",").map { MedicinskaKorist.valueOf(it) }
        }
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isBlank()) {
            emptyList()
        } else {
            value.split(",")
        }
    }

    @TypeConverter
    fun fromProfilOkusaBiljke(value: ProfilOkusaBiljke?): String {
        return value?.name ?: ""
    }

    @TypeConverter
    fun toProfilOkusaBiljke(value: String): ProfilOkusaBiljke? {
        return if (value.isBlank()) {
            null
        } else {
            ProfilOkusaBiljke.valueOf(value)
        }
    }

    @TypeConverter
    fun fromKlimatskiTipList(value: List<KlimatskiTip>?): String {
        return value?.joinToString(",") { it.name } ?: ""
    }

    @TypeConverter
    fun toKlimatskiTipList(value: String): List<KlimatskiTip> {
        return if (value.isBlank()) {
            emptyList()
        } else {
            value.split(",").map { KlimatskiTip.valueOf(it) }
        }
    }

    @TypeConverter
    fun fromZemljisniTipList(value: List<Zemljiste>?): String {
        return value?.joinToString(",") { it.name } ?: ""
    }

    @TypeConverter
    fun toZemljisniTipList(value: String): List<Zemljiste> {
        return if (value.isBlank()) {
            emptyList()
        } else {
            value.split(",").map { Zemljiste.valueOf(it) }
        }
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): String? {
        return if (bitmap != null) {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } else {
            null
        }
    }

    @TypeConverter
    fun toBitmap(encodedString: String?): Bitmap? {
        return if (encodedString != null) {
            val byteArray = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } else {
            null
        }
    }
}
