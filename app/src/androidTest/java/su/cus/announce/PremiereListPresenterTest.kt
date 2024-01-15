package su.cus.announce

import android.content.Context
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import su.cus.announce.API.IRetrofitClient
import su.cus.announce.API.MoviesRepository.Country
import su.cus.announce.API.MoviesRepository.Genre
import su.cus.announce.API.MoviesRepository.Movie
import su.cus.announce.premiere.CachedData
import su.cus.announce.premiere.PremiereListPresenterImpl
import su.cus.announce.premiere.PremiereListPresenterOutput

@RunWith(RobolectricTestRunner::class)
class PremiereListPresenterTest {
    @MockK
    lateinit var output: PremiereListPresenterOutput
    @RelaxedMockK
    lateinit var context: Context
    @MockK
    lateinit var client: IRetrofitClient
    @MockK
    lateinit var cache: CachedData<List<Movie>>
    @InjectMockKs
    val presenter = PremiereListPresenterImpl(context, output, client, cache)
    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks

    @Test
    fun loadMoviesTest() {
        // Assume cache is initially empty
        coEvery { cache.read() } returns emptyList()

        // Mock network response
        val movies = listOf(Movie(
                countries = listOf(Country("USA")),
                duration = 120,
                genres = listOf(Genre("Drama"), Genre("Adventure")),
                kinopoiskId = 12345,
                nameEn = "The Grand Adventure",
                nameRu = "Большое Приключение",
                posterUrl = "http://example.com/poster1.jpg",
                posterUrlPreview = "http://example.com/poster1_preview.jpg",
                premiereRu = "2022-01-01",
                year = 2022
            ),
            Movie(
                countries = listOf(Country("UK")),
                duration = 90,
                genres = listOf(Genre("Comedy")),
                kinopoiskId = 54321,
                nameEn = "The Laughing Man",
                nameRu = "Смеющийся Человек",
                posterUrl = "http://example.com/poster2.jpg",
                posterUrlPreview = "http://example.com/poster2_preview.jpg",
                premiereRu = "2022-05-05",
                year = 2022
            )
        )

        coEvery { presenter.loadMovies() }
        // Verify that movies are written to the cache
        coVerify { cache.write(movies) }
    }
}
