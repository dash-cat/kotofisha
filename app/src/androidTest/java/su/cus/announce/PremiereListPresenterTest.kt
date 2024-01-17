package su.cus.announce

//
//
//class PremiereListPresenterTest {
//    inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
//    var context: Context = mock(Context::class.java)
//    var output: PremiereListPresenterOutput = (mock(PremiereListPresenterOutput::class.java))
//    var cacheFactory: ICachedDataFactory = (mock(ICachedDataFactory::class.java))
//    var cachePremiere: ICachedData<ListPremiere> = mock()
//    var cacheMovieList: ICachedData<FilmDataItem> = mock()
//    var client: IRetrofitClient = mock(IRetrofitClient::class.java)
//
//    val presenter = PremiereListPresenterImpl(context, output, client, cacheFactory)
//
//
//    @Test
//    fun loadMoviesTest() {
//        // Arrange: Create mock data
//        val mockMovies = listOf(
//            Movie(
//                countries = listOf(Country("USA")),
//                duration = 120,
//                genres = listOf(Genre("Drama"), Genre("Adventure")),
//                kinopoiskId = 12345,
//                nameEn = "The Grand Adventure",
//                nameRu = "Большое Приключение",
//                posterUrl = "http://example.com/poster1.jpg",
//                posterUrlPreview = "http://example.com/poster1_preview.jpg",
//                premiereRu = "2022-01-01",
//                year = 2022
//            ),
//            Movie(
//                countries = listOf(Country("UK")),
//                duration = 90,
//                genres = listOf(Genre("Comedy")),
//                kinopoiskId = 54321,
//                nameEn = "The Laughing Man",
//                nameRu = "Смеющийся Человек",
//                posterUrl = "http://example.com/poster2.jpg",
//                posterUrlPreview = "http://example.com/poster2_preview.jpg",
//                premiereRu = "2022-05-05",
//                year = 2022
//            )
//        )
//
//        val mockListPremiere = ListPremiere(mockMovies, mockMovies.size)
//
//        // Set up the necessary mock responses
//        every { cacheFactory.makeCachedDataForMoviesList() } returns cachePremiere
//        every { cacheFactory.makeCachedDataForMovie(any()) } returns cacheMovieList
//        coEvery { cachePremiere.read() } returns null
//        coEvery { cacheMovieList.read() } returns null
//        coEvery { client.getMovies(2023, "JANUARY") } returns mockListPremiere
//
//        // Act: Call the method under test
//        runBlocking {
//            presenter.loadMovies()
//        }
//
//        // Assert: Verify the interactions and expected outcomes
//        coVerify { client.getMovies(2023, "JANUARY") }
//        coVerify { cachePremiere.write(mockListPremiere) }
//        // Add any other necessary verifications and assertions
//    }
//}
import android.content.Context
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import su.cus.announce.API.FilmDescription.FilmDataItem
import su.cus.announce.API.ICachedData
import su.cus.announce.API.IRetrofitClient
import su.cus.announce.API.MoviesRepository.Country
import su.cus.announce.API.MoviesRepository.Genre
import su.cus.announce.API.MoviesRepository.ListPremiere
import su.cus.announce.API.MoviesRepository.Movie
import su.cus.announce.premiere.PremiereListPresenterImpl
import su.cus.announce.premiere.PremiereListPresenterOutput

class MyTestClass {

    private lateinit var cacheFactory: ICachedDataFactory
    private lateinit var cachePremiere: ICachedData<ListPremiere>
    private lateinit var cacheMovieList: ICachedData<FilmDataItem>
    private lateinit var client: IRetrofitClient
    private lateinit var presenter: PremiereListPresenterImpl

    @Before
    fun setUp() {
        val context: Context = mock()
        val output: PremiereListPresenterOutput = mock()
        cacheFactory = mock()
        cachePremiere = mock()
        cacheMovieList = mock()
        client = mock()
        presenter = PremiereListPresenterImpl(context, output, client, cacheFactory)
    }

    @Test
    fun testLoadMovies() {
        val mockMovies = listOf(
            Movie(
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

        val mockListPremiere = ListPremiere(mockMovies, mockMovies.size)

        // Set up the necessary mock responses
        whenever(cacheFactory.makeCachedDataForMoviesList()).thenReturn(cachePremiere)
        whenever(cacheFactory.makeCachedDataForMovie(any())).thenReturn(cacheMovieList)
        whenever(cachePremiere.read()).thenReturn(null)
        whenever(cacheMovieList.read()).thenReturn(null)


        // Act: Call the method under test
        runBlocking {
            whenever(client.getMovies(2023, "JANUARY")).thenReturn(mockListPremiere)
            presenter.loadMovies()
            verify(client).getMovies(2023, "JANUARY")

        }

        // Assert: Verify the interactions and expected outcomes

        verify(cachePremiere).write(mockListPremiere)
        // Add any other necessary verifications and assertions
    }
}
