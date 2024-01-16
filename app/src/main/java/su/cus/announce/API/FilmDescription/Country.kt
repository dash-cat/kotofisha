package su.cus.announce.API.FilmDescription

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Country(
    val country: String
) : Parcelable