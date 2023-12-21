package su.cus.announce.premiere

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import su.cus.announce.API.MoviesRepository.ItemMoviesList
import su.cus.announce.R

class PremiereListAdapter(private val data: List<ItemMoviesList>) : RecyclerView.Adapter<PremiereListAdapter.PremiereViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PremiereViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_program, parent, false)
        return PremiereViewHolder(view)
    }

    override fun onBindViewHolder(holder: PremiereViewHolder, position: Int) {
        val movie = data[position]
        holder.textView.text = movie.nameRu
        holder.dateView.text = movie.premiereRu

    }

    override fun getItemCount() = data.size

    class PremiereViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_prev)
        val textView: TextView = view.findViewById(R.id.text_title)
        val dateView: TextView = view.findViewById(R.id.text_title_date)

    }
}