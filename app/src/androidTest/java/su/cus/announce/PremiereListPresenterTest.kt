package su.cus.announce

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PremiereListPresenterTest {
//    @RelaxedMockK
//    lateinit var context: Context
//    @MockK
//    lateinit var cacheFactory: ICachedDataFactory
//    @MockK
//    lateinit var cachePremiere: ICachedData<ListPremiere>
//    @MockK
//    lateinit var cacheMovieList: ICachedData<FilmDataItem>
//    @MockK
//    lateinit var client: IRetrofitClient
//    @MockK
//    lateinit var output: PremiereListPresenterOutput
//
//    @InjectMockKs
//    val presenter = PremiereListPresenterImpl(context, output, client, cacheFactory)
//    @Before
//    fun setUp() {
//        MockKAnnotations.init(this, relaxUnitFun = true)
//
//    }
    @Test
    fun loadMoviesTest() {
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
    }
}
