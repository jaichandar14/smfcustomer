package com.smf.customer.view.addServices.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smf.customer.BuildConfig
import com.smf.customer.R
import com.smf.customer.data.model.response.ServiceData

class AddServiceAdapter(val context: Context) :
    RecyclerView.Adapter<AddServiceAdapter.AddServiceViewHolder>() {

    private var servicesList = ArrayList<ServiceData>()
    var preSelectedServices = ArrayList<String>()
    var selectedServices = ArrayList<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateServicesList(
        servicesList: ArrayList<ServiceData>,
        preSelectedServices: ArrayList<String>,
        selectedServices: ArrayList<String>
    ) {
        this.servicesList = servicesList
        this.preSelectedServices = preSelectedServices
        this.selectedServices = selectedServices
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddServiceViewHolder {
        // Initialize view
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_dialog_list_item, parent, false
        )
        // Pass holder view
        return AddServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddServiceViewHolder, position: Int) {
        holder.setData(servicesList[position], position)
    }

    override fun getItemCount(): Int {
        return servicesList.size
    }

    inner class AddServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var serviceIconView: ImageView = view.findViewById(R.id.service_image)
        private var tvItem: TextView = view.findViewById(R.id.tvItem)
        var serviceSelectionImage: ImageView = view.findViewById(R.id.service_selection_image)

        fun setData(serviceData: ServiceData, position: Int) {
            tvItem.text = serviceData.serviceName
            setServiceIcon(serviceData.serviceTemplateIcon, serviceIconView)
            // and Update service selection images
            if (preSelectedServices.contains(serviceData.serviceName) ||
                selectedServices.contains(serviceData.serviceName)
            ) {
                serviceSelectionImage.setImageResource(R.drawable.new_selection)
                serviceSelectionImage.tag = R.drawable.new_selection
            } else {
                serviceSelectionImage.setImageResource(R.drawable.unselect)
                serviceSelectionImage.tag = R.drawable.unselect
            }
            // Initialize onClick listener
            itemView.setOnClickListener {
                // Avoid preSelectedService clickable
                if (preSelectedServices.contains(serviceData.serviceName).not()) {
                    updateData(position)
                }
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private fun setServiceIcon(serviceTemplateIcon: String?, serviceIconView: ImageView) {
            if (serviceTemplateIcon != null) {
                if (serviceTemplateIcon.startsWith(context.getString(R.string.data))) {
                    val img =
                        serviceTemplateIcon.substring(serviceTemplateIcon.indexOf(",") + 1).trim()
                    Glide.with(context).load(Base64.decode(img, Base64.DEFAULT))
                        .error(R.drawable.custom_button_corner_ok_fade).into(serviceIconView)
                } else {
                    Glide.with(context)
                        .load((BuildConfig.base_url + serviceTemplateIcon).replace(" ", "%20"))
                        .error(R.drawable.custom_button_corner_ok_fade)
                        .into(serviceIconView)
                }
            } else {
                // make sure Glide doesn't load anything into this view until told otherwise
                Glide.with(context).clear(serviceIconView)
                serviceIconView.setImageDrawable(context.getDrawable(R.drawable.custom_button_corner_ok_fade))
            }
        }

        // Method for update image after onClick
        private fun updateData(position: Int) {
            // Get tag and update icon
            var integer = serviceSelectionImage.tag
            integer = integer ?: 0

            when (integer) {
                R.drawable.unselect -> {
                    serviceSelectionImage.setImageResource(R.drawable.new_selection)
                    serviceSelectionImage.tag = R.drawable.new_selection
                    serviceClickListener?.onServiceClicked(position, true)
                }
                R.drawable.new_selection -> {
                    serviceSelectionImage.setImageResource(R.drawable.unselect)
                    serviceSelectionImage.tag = R.drawable.unselect
                    serviceClickListener?.onServiceClicked(position, false)
                }
                else -> {
                    serviceSelectionImage.setImageResource(R.drawable.unselect)
                    serviceSelectionImage.tag = R.drawable.unselect
                }
            }
        }
    }

    private var serviceClickListener: ServiceClickListener? = null

    // Initializing service clickListener interface
    fun setOnClickListener(listener: ServiceClickListener) {
        serviceClickListener = listener
    }

    // Interface for services selection
    interface ServiceClickListener {
        fun onServiceClicked(listPosition: Int, status: Boolean)
    }
}