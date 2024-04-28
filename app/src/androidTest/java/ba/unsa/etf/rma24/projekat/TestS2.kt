package ba.unsa.etf.rma24.projekat

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test


class TestS2 {

    @get:Rule
    var activityRule = ActivityScenarioRule(NovaBiljkaActivity::class.java)

    @Test
    fun prebaciNaDruguAktivnost() {
        val activity = ActivityScenario.launch(MainActivity::class.java)
        Intents.init()
        onView(withId(R.id.novaBiljkaBtn)).perform(click())
        intended(hasComponent(NovaBiljkaActivity::class.java.name))
        Intents.release()
        activity.close()
    }

    @Test
    fun testValidacijaPolja_NazivKratak() {
        onView(withId(R.id.nazivET)).perform(typeText("a"))
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Naziv mora biti između 2 i 20 znakova")))
    }

    @Test
    fun testValidacijaPolja_PorodicaKratak() {
        onView(withId(R.id.nazivET)).perform(typeText("Grah"))
        onView(withId(R.id.porodicaET)).perform(typeText("b"))
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Porodica mora biti između 2 i 20 znakova")))
    }

    @Test
    fun testValidacijaPolja_MedicinskoUpozorenjeKratak() {
        onView(withId(R.id.nazivET)).perform(typeText("Grah"))
        onView(withId(R.id.porodicaET)).perform(typeText("Mahunarke"))
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("c"))
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Medicinsko upozorenje mora biti između 2 i 20 znakova")))
    }

    @Test
    fun testValidacijaPolja_JeloKratak() {
        onView(withId(R.id.nazivET)).perform(typeText("Grah"))
        onView(withId(R.id.porodicaET)).perform(typeText("Mahunarke"))
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("Kiseline"))
        onView(withId(R.id.jeloET)).perform(typeText("d"))
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Jelo mora biti između 2 i 20 znakova")))
    }

    private fun dodavanjeET() {
        onView(withId(R.id.nazivET)).perform(typeText("Grah"))
        onView(withId(R.id.porodicaET)).perform(typeText("Mahunarke"))
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("Kiseline"))
        onView(withId(R.id.jeloET)).perform(typeText("Corba"))
    }

    @Test
    fun testValidacijaMedicinskaKoristLV() {
        dodavanjeET()
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.dodajBiljkuBtn))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testValidacijaKlimatskiTipLV() {
        dodavanjeET()
        onView(allOf(withParent(withId(R.id.medicinskaKoristLV)), withText("SMIRENJE")))
            .perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.dodajBiljkuBtn))
            .check(matches(isDisplayed()))
    }


    @Test
    fun testValidacijaZemljisniTipLV() {
        dodavanjeET()
        onView(allOf(withParent(withId(R.id.medicinskaKoristLV)), withText("SMIRENJE")))
            .perform(click())
        onView(allOf(withParent(withId(R.id.klimatskiTipLV)), withText("SREDOZEMNA")))
            .perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.dodajBiljkuBtn))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testValidacijaProfilOkusaLV() {
        dodavanjeET()
        onView(allOf(withParent(withId(R.id.medicinskaKoristLV)), withText("SMIRENJE")))
            .perform(click())
        onView(allOf(withParent(withId(R.id.klimatskiTipLV)), withText("SREDOZEMNA")))
            .perform(click())
        onView(allOf(withParent(withId(R.id.zemljisniTipLV)), withText("GLINENO")))
            .perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.dodajBiljkuBtn))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testValidacijaDuplikata() {
        onView(withId(R.id.jeloET)).perform(typeText("Grah"))
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).perform(typeText("grah"))
        onView(withId(R.id.dodajJeloBtn)).perform(click())

        onView(withId(R.id.dodajJeloBtn))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testValidacijaBarJednoJelo() {
        onView(withId(R.id.nazivET)).perform(typeText("Grah"))
        onView(withId(R.id.porodicaET)).perform(typeText("Mahunarke"))
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("Kiselkast"))
        onView(withId(R.id.jeloET)).perform(typeText("Jaje"))
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        onView(withId(R.id.dodajBiljkuBtn))
            .check(matches(isDisplayed()))
    }


    @Test
    fun testUslikajBiljkuBtn() {
        /*      TEST PROLAZI TEK KADA KORISNIK USLIKA SLIKU     */
        onView(withId(R.id.uslikajBiljkuBtn)).perform(click())
        onView(withId(R.id.slikaIV)).check(matches(withBitmap()))
    }

    private fun withBitmap(): Matcher<View> {
        return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("with bitmap drawable")
            }
            override fun matchesSafely(imageView: ImageView?): Boolean {
                val drawable = imageView?.drawable
                return (drawable is BitmapDrawable && (drawable.bitmap != null))
            }
        }
    }
}