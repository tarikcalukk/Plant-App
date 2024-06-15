package ba.unsa.etf.rma24.projekat.pomocneKlase

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ba.unsa.etf.rma24.projekat.Biljka

@Entity(
    tableName = "BiljkaBitmap",
    foreignKeys = [
        ForeignKey(
            entity = Biljka::class,
            parentColumns = ["id"],
            childColumns = ["idBiljke"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BiljkaBitmap(
    @PrimaryKey(autoGenerate = true) val idBiljke: Long,
    @ColumnInfo(name = "bitmap") val bitmap: Bitmap
)
