package ba.unsa.etf.rma24.projekat

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.unsa.etf.rma24.projekat.pomocneKlase.BiljkaDatabase
import ba.unsa.etf.rma24.projekat.pomocneKlase.KlimatskiTip
import ba.unsa.etf.rma24.projekat.pomocneKlase.MedicinskaKorist
import ba.unsa.etf.rma24.projekat.pomocneKlase.ProfilOkusaBiljke
import ba.unsa.etf.rma24.projekat.pomocneKlase.Zemljiste
import ba.unsa.etf.rma24.projekat.pomocneKlase.biljke
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var biljkaDao: BiljkaDao
    private lateinit var db: BiljkaDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, BiljkaDatabase::class.java).build()
        biljkaDao = db.biljkaDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun addBiljkaAndVerify() = runBlocking {
        val biljka = Biljka(
            naziv = "Lavanda",
            porodica = "Lamiaceae",
            medicinskoUpozorenje = "Jaka",
            medicinskeKoristi = listOf(MedicinskaKorist.PODRSKAIMUNITETU, MedicinskaKorist.SMIRENJE),
            jela = listOf("Salata"),
            profilOkusa = ProfilOkusaBiljke.MENTA,
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA,KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.CRNICA)
        )
        biljkaDao.saveBiljka(biljka)

        val allBiljkas = biljkaDao.getAllBiljkas()
        assertEquals(1, allBiljkas.size)
        assertEquals(biljka.naziv, allBiljkas[0].naziv)
        assertEquals(biljka.porodica, allBiljkas[0].porodica)
        assertEquals(biljka.medicinskoUpozorenje, allBiljkas[0].medicinskoUpozorenje)
        assertEquals(biljka.jela, allBiljkas[0].jela)
        assertEquals(biljka.profilOkusa, allBiljkas[0].profilOkusa)
    }

    @Test
    @Throws(Exception::class)
    fun clearDataAndVerify() = runBlocking {
        val biljka = Biljka(
            naziv = "Lavanda",
            porodica = "Lamiaceae",
            medicinskoUpozorenje = "Nema",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTIVBOLOVA),
            jela = listOf("Salata"),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            klimatskiTipovi = listOf(KlimatskiTip.SUHA, KlimatskiTip.PLANINSKA),
            zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.SLJUNKOVITO)
        )
        biljkaDao.saveBiljka(biljka)
        biljkaDao.clearData()

        val allBiljkas = biljkaDao.getAllBiljkas()
        assertTrue(allBiljkas.isEmpty())
    }
    @Test
    fun testInsertBiljkeList() = runBlocking {
        val biljke = biljke

        biljkaDao.insertBiljkeList(biljke)

        val allBiljkas = biljkaDao.getAllBiljkas()
        assertEquals(biljke.size, allBiljkas.size)

        biljke.forEachIndexed { index, biljka ->
            assertEquals(biljka.naziv, allBiljkas[index].naziv)
            assertEquals(biljka.porodica, allBiljkas[index].porodica)
            assertEquals(biljka.medicinskoUpozorenje, allBiljkas[index].medicinskoUpozorenje)
            assertEquals(biljka.medicinskeKoristi, allBiljkas[index].medicinskeKoristi)
            assertEquals(biljka.profilOkusa, allBiljkas[index].profilOkusa)
            assertEquals(biljka.jela, allBiljkas[index].jela)
            assertEquals(biljka.klimatskiTipovi, allBiljkas[index].klimatskiTipovi)
            assertEquals(biljka.zemljisniTipovi, allBiljkas[index].zemljisniTipovi)
        }
    }
}
