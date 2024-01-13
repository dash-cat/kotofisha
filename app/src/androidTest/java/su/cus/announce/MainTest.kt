package su.cus.announce

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import su.cus.announce.premiere.PremiereList

@RunWith(AndroidJUnit4::class)
class MainActivityTest {


    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        // Инициализация Intents перед каждым тестом
        Intents.init()
    }

    @After
    fun tearDown() {
        // Освобождение Intents после каждого теста
        Intents.release()
    }



    @Test
    fun clickButton() {
        // Find the button and perform a click
        onView(withId(R.id.welcomeButton)).perform(click())

        // Проверка, что был запущен Intent
        intended(hasComponent(PremiereList::class.java.name))
    }
}
