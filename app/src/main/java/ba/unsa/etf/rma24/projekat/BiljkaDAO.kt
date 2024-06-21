package ba.unsa.etf.rma24.projekat

import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ba.unsa.etf.rma24.projekat.pomocneKlase.BiljkaBitmap
import ba.unsa.etf.rma24.projekat.trefle.TrefleDAO

@Dao
interface BiljkaDao {
    @Transaction
    suspend fun saveBiljka(biljka: Biljka): Boolean {
        return if (getBiljkaById(biljka.id ?: 0L) != null) {
            updateBiljka(biljka)
        } else {
            insertBiljka(biljka) != -1L
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBiljka(biljka: Biljka): Long

    @Update
    suspend fun updateBiljka(biljka: Biljka): Boolean {
        return update(biljka) > 0
    }

    @Update
    suspend fun update(biljka: Biljka) : Int

    @Query("SELECT * FROM biljka WHERE id = :id")
    suspend fun getBiljkaById(id: Long): Biljka?

    @Transaction
    suspend fun addImage(idBiljke: Long, bitmap: Bitmap): Boolean {
        getBiljkaById(idBiljke) ?: return false

        val existingBitmap = getImageByIdBiljke(idBiljke)
        if (existingBitmap != null) {
            // Ako već postoji slika za datu biljku, vraćamo false
            return false
        }
        val newBitmap = BiljkaBitmap(idBiljke = idBiljke, bitmap = bitmap)
        return insertImage(newBitmap) != -1L
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImage(biljkaBitmap: BiljkaBitmap): Long

    @Query("SELECT * FROM BiljkaBitmap WHERE idBiljke = :idBiljke")
    suspend fun getImageByIdBiljke(idBiljke: Long): BiljkaBitmap?

    @Query("SELECT * FROM biljka")
    suspend fun getAllBiljkas(): List<Biljka>

    @Query("DELETE FROM biljka")
    suspend fun clearData()

    @Query("SELECT * FROM biljka WHERE onlineChecked = 0")
    suspend fun getOfflineBiljke(): List<Biljka>

    @Transaction
    suspend fun fixOfflineBiljka(): Int {
        var updatedCount = 0
        val offlineBiljke = getOfflineBiljke()
        offlineBiljke.forEach { biljka ->
            val fixedBiljka = TrefleDAO().fixData(biljka)
            if (biljka != fixedBiljka) {
                saveBiljka(fixedBiljka)
                updatedCount++
            }
        }
        return updatedCount
    }

    @Transaction
    suspend fun insertBiljkeList(biljke: List<Biljka>) {
        biljke.forEach { saveBiljka(it) }
    }
}