package com.example.productstore
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val onClick: (Item) -> Unit) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var items: List<Item> = emptyList()

    fun submitList(list: List<Item>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        private val textView1: TextView = view.findViewById(R.id.textView1)
        private val textView2: TextView = view.findViewById(R.id.textView2)
        private val textView3: TextView = view.findViewById(R.id.textView3)

        fun bind(item: Item) {
            imageView.setImageURI(Uri.parse(item.imageUri))
            textView1.text = item.text1
            textView2.text = item.text2
            textView3.text = item.text3

            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }
}
