package su.cus.announce

import android.content.Context
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import su.cus.announce.premiere.PremiereListPresenterImpl
import su.cus.announce.premiere.PremiereListPresenterOutput

@RunWith(RobolectricTestRunner::class)
class PremiereListPresenterTest {

    @Test
    fun clickingButton_shouldChangeResultsViewText() {
        val output = mockk<PremiereListPresenterOutput>()
        val mContextMock = mockk<Context>(relaxed = true)

        val presenter = PremiereListPresenterImpl(mContextMock, output)

        presenter.loadMovies()

    }
}
