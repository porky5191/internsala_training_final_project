package porky.training.zypko.fragment.navigation

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import porky.training.zypko.R
import porky.training.zypko.adapter.FAQAdapter
import porky.training.zypko.model.FAQ
import java.util.*

class FragFAQ : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (root == null) {
            root = inflater.inflate(R.layout.frag_faq, container, false)
            init()
        }
        return root
    }

    private fun init() {
        //change the title of the toolbar
        activity!!.title = "Frequently Asked Questions"

        //Initializing the recycler
        val recycler: RecyclerView = root!!.findViewById(R.id.recycler)

        //creating a Arraylist from the String a and q
        val itemList = ArrayList<FAQ>()
        for (i in q.indices) {
            val faq = FAQ()
            faq.question = q[i]
            faq.answer = a[i]
            itemList.add(faq)
        }

        //initializing the adapter and setting list to it
        val adapter = FAQAdapter(activity!!, itemList)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(activity)
        //adding divider to the recycler view
        recycler.addItemDecoration(DividerItemDecoration(recycler.context, DividerItemDecoration.VERTICAL))
        val ll_progress = root!!.findViewById<LinearLayout>(R.id.ll_progress)
        //make the progress layout visible
        //given time for better animated entry
        Handler().postDelayed({ ll_progress.visibility = View.GONE }, 400)
    }

    //----------------------------------------------
    private var root: View? = null

    //----------------Questions------------------
    private val q = arrayOf("Can I place order on call?",
            "How do I know my order is confirmed?",
            "How will be my orders delivered?",
            "Is delivery free?",
            "What if I have a problem with my delivery?",
            "I received wrong products, what should I do?",
            "When I have to pay?",
            "Will I get an invoice?",
            "How have you selected these products?",
            "Why is Zypko doing this?",
            "How are you guys are different?"
    )

    //----------------Answers------------------
    private val a = arrayOf("Sorry, we don’t accept orders on call. However you can call us on +91-7259085922 for any help related to placing order.",
            " We will send you a order confirmation email once we receive your order.",
            "Orders are delivered directly by the Zypko Packaging Assist suppliers. Different items in an order could be fulfilled by different suppliers. We will share the contact details and amount payable for all deliveries via email and SMS.",
            "Yes. There are no delivery or handling charges.",
            "Please call Zypko Partner Support on +91-7259085922 or write email to support@zypko.com mentioning your Order # and they will help you out.",
            "Please see our return and cancellation policy",
            "You will have to pay at the time of delivery as per the payment details we send to you via email.",
            "Yes. You will get your invoice from our supplier at the time of delivery.",
            "We have show products which have passed our quality tests and are best suited for delivery. We will keep updating our catalog with better products. Stay tuned!",
            "At Zypko we strive to add value to your business. We understand the challenges you face on a daily basis in sourcing the best quality packaging material. Zypko Packaging Assist is to take care of all your packaging needs.",
            """
                Best products - We only list the products which have passed our tests and are the best suitable for delivery. They will help in giving a better consumer experience, so you can get get repeated orders.
                Best prices - We have negotiated rates with our distributors exclusive for Zypko Restaurant.
                Convenience - No need to haggle for prices. No need to follow-up on delivery. We track all orders to ensure you have a hassle free experience.
                """.trimIndent()
    )
}