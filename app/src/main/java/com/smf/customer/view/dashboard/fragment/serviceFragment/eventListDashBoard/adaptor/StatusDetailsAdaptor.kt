package com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.adaptor


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.smf.customer.R
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.view.dashboard.fragment.serviceFragment.eventListDashBoard.model.ItemClass

public class StatusDetailsAdaptor : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var itemClassList = ArrayList<ItemClass>()

    override fun getItemViewType(position: Int): Int {
        return when (itemClassList[position].viewType) {
            0 -> AppConstant.LAYOUTONE
            1 -> AppConstant.LAYOUTTWO
            else -> -1
        }
    }

    // Create classes for each layout ViewHolder.
    internal inner class LayoutOneViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView
        private val dotLine: ImageView
        private val dotLine1: ImageView
        private val textview: TextView
        internal val linearLayout: LinearLayout

        init {
            icon = itemView.findViewById(R.id.add_service_icon)
            dotLine = itemView.findViewById(R.id.add_service_icon_line)
            dotLine1 = itemView.findViewById(R.id.add_service_icon_line1)
            // Find the Views
            textview = itemView.findViewById(R.id.text)
            linearLayout = itemView.findViewById(R.id.linearlayout)
        }

        // method to set the views that will
        // be used further in onBindViewHolder method.
        internal fun setView(text: String?, status: Int) {
            if (text?.equals(MyApplication.appContext.getString(R.string.add_or_remove_services)) == true) {
                itemView.findViewById<ImageView>(R.id.add_service_icon_line1).visibility =
                    View.INVISIBLE
            }
            when (status) {
                0 -> {
                    dotLine.setImageResource(R.drawable.straight_line)
                    dotLine1.setImageResource(R.drawable.straight_line)
                    icon.setImageResource(R.drawable.completed)
                }
                1 -> {
                    dotLine1.setImageResource(R.drawable.straight_line)
                    icon.setImageResource(R.drawable.in_progress)
                }
                else -> {
                    icon.setImageResource(R.drawable.notstarted)
                }
            }
            //   icon.setImageResource(image)
            textview.text = text
        }
    }

    // similarly a class for the second layout is also
    // created.
    internal inner class LayoutTwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView
        private val textOne1: TextView
        private val dotLine1: ImageView

        //   private val text_two: TextView
        internal val linearLayout: LinearLayout

        init {
            icon = itemView.findViewById(R.id.image)
            textOne1 = itemView.findViewById(R.id.text_one)
            dotLine1 = itemView.findViewById(R.id.add_service_icon_line1)
            // text_two = itemView.findViewById(R.id.text_two)
            linearLayout = itemView.findViewById(R.id.linearlayout)
        }

        internal fun setViews(
            textOne: String?,
            textTwo: String?,
            status: Int
        ) {
            textOne1.text = textOne
            //   text_two.text = textTwo
            when (status) {
                0 -> {
                    dotLine1.setImageResource(R.drawable.straight_line)
                    icon.setImageResource(R.drawable.completed)
                }
                1 -> {
                    icon.setImageResource(R.drawable.in_progress)
                }
                else -> {
                    icon.setImageResource(R.drawable.notstarted)
                }
            }
        }
    }

    // In the onCreateViewHolder, inflate the
    // xml layout as per the viewType.
    // This method returns either of the
    // ViewHolder classes defined above,
    // depending upon the layout passed as a parameter.
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            AppConstant.LAYOUTONE -> {
                val layoutOne = LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.stepper_first_view, parent,
                        false
                    )
                LayoutOneViewHolder(
                    layoutOne
                )
            }
            AppConstant.LAYOUTTWO -> {
                val layoutTwo = LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.stepper_last_view, parent,
                        false
                    )
                LayoutTwoViewHolder(
                    layoutTwo
                )
            }
            else -> {
                val layoutTwo = LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.stepper_last_view, parent,
                        false
                    )
                LayoutTwoViewHolder(
                    layoutTwo
                )
            }
        }
    }

    // This method returns the count of items present in the
    // RecyclerView at any given time.
    override fun getItemCount(): Int {
        return itemClassList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (itemClassList[position].viewType) {
            AppConstant.LAYOUTONE -> {
                //   val image = itemClassList[position].geticon1()
                val text = itemClassList[position].text
                val status = itemClassList[position].status
                (holder as LayoutOneViewHolder).setView(text, status)

                // The following code pops a toast message
                // when the item layout is clicked.
                // This message indicates the corresponding
                // layout.
                holder.linearLayout.setOnClickListener { view ->
//                    Toast
//                        .makeText(
//                            view.context,
//                            "Hello from Layout One!",
//                            Toast.LENGTH_SHORT
//                        )
//                        .show()
                }
            }
            AppConstant.LAYOUTTWO -> {
                //    val image = itemClassList[position].geticon()
                val textOne = itemClassList[position].text_one
                val textTwo = itemClassList[position].text_two
                val status = itemClassList[position].status1
                (holder as LayoutTwoViewHolder)
                    .setViews(textOne, textTwo, status)
                holder.linearLayout.setOnClickListener { view ->
//                    Toast
//                        .makeText(
//                            view.context,
//                            "Hello from Layout Two!",
//                            Toast.LENGTH_SHORT
//                        )
//                        .show()
                }
            }
            else -> return
        }
    }

    //Method For Refreshing Invoices
    @SuppressLint("NotifyDataSetChanged")
    fun refreshItems(invoice: List<ItemClass>) {
        itemClassList.clear()
        itemClassList.addAll(invoice)
        notifyDataSetChanged()
    }
}