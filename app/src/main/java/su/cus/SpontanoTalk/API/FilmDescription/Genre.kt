package su.cus.SpontanoTalk.API.FilmDescription
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Genre(
    val genre: String
) : Parcelable