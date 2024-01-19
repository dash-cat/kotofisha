import org.junit.Test
import kotlin.test.assertEquals

class ExampleTest {

    @Test
    fun testString() {
        val expected = "Hello, Kotlin!"
        val actual = "Hello, Kotlin!"
        assertEquals(expected, actual, "Strings do not match")
    }
}
