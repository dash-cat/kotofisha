package su.cus.anonce.premiere

import android.graphics.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import su.cus.announce.R
import su.cus.anonce.API.MoviesRepository.ItemMoviesList

class PremiereListAdapter(private val data: List<ItemMoviesList>) : RecyclerView.Adapter<PremiereListAdapter.PremiereViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PremiereViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_premiere, parent, false)
        return PremiereViewHolder(view)
    }

    override fun onBindViewHolder(holder: PremiereViewHolder, position: Int) {
        val movie = data[position]
        holder.textView.text = movie.premiereRu

    }

    override fun getItemCount() = data.size
    class PremiereViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.recyclerView)

    }

}