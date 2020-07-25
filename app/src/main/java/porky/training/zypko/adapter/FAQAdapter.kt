package porky.training.zypko.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import porky.training.zypko.R
import porky.training.zypko.model.FAQ
import java.util.*

class FAQAdapter(private val context: Context, private val arrayList: ArrayList<FAQ>) : RecyclerView.Adapter<FAQAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_faq, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var position = position
        val faq = arrayList[position]
        holder.l_question.text = "Q." + ++position + " " + faq.question
        holder.l_answer.text = "Ans: " + faq.answer
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var l_question: TextView
        var l_answer: TextView

        init {
            l_question = itemView.findViewById(R.id.l_question)
            l_answer = itemView.findViewById(R.id.l_answer)
        }
    }

}