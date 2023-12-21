package su.cus.announce.premiere

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import su.cus.announce.API.MoviesRepository.ItemMoviesList
import su.cus.announce.R

class PremiereListAdapter(private val data: List<ItemMoviesList>, private val listener: PremiereList) : RecyclerView.Adapter<PremiereListAdapter.PremiereViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PremiereViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_program, parent, false)
        return PremiereViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: PremiereViewHolder, position: Int) {
        val movie = data[position]
        holder.bind(movie.posterUrlPreview)
        holder.textView.text = movie.nameRu
        holder.dateView.text = movie.premiereRu
        holder.itemView.tag = movie.kinopoiskId
//        holder.itemView.text =
        holder.imageView.setOnClickListener {
            listener.onItemsClick(movie.kinopoiskId.toString())
        }

    }

    override fun getItemCount() = data.size

    class PremiereViewHolder(view: View, private val listener: PremiereList) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val movieId = view.tag as String
                    listener.onItemsClick(movieId)
                }
            }
        }



        val imageView: ImageView = view.findViewById(R.id.image_prev)
        val textView: TextView = view.findViewById(R.id.text_title)
        val dateView: TextView = view.findViewById(R.id.text_title_date)
        private val context = view.context

        fun bind(imageUrl: String) {
            Glide.with(context)
                .load(imageUrl)
                .into(imageView)
        }

}


}