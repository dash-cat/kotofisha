package su.cus.spontanotalk.API.FilmDescription
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Genre(
    val genre: String
) : Parcelable