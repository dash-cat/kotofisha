package su.cus.spontanotalk.API.MoviesRepository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ListPremiere(
    val items: @RawValue List<Movie>,
    val total: Int
) : Parcelable