package com.ns.stellarjet.personalize.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.SavedAddresse
import com.ns.stellarjet.R

class SavedAddressListAdapter (
    private val mSavedAddress: List<SavedAddresse>?,
    private val onSelectDishListenerParams: (SavedAddresse) -> Unit): RecyclerView.Adapter<SavedAddressListAdapter.SavedAddressListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAddressListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_row_saved_address, parent, false)
        return SavedAddressListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mSavedAddress?.size!!
    }

    override fun onBindViewHolder(holder: SavedAddressListViewHolder, position: Int) {
        holder.bind(mSavedAddress?.get(position)!!, onSelectDishListenerParams)
    }

    class SavedAddressListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val mFoodName : TextView = itemView.findViewById(R.id.textView_saved_address_name)
        private val mFoodDescription : TextView = itemView.findViewById(R.id.textView_saved_address)

        fun bind(savedAddresse: SavedAddresse, onSelectDishListenerParams: (SavedAddresse) -> Unit){
            mFoodName.text = savedAddresse.address_tag
            mFoodDescription.text = savedAddresse.address

            itemView.setOnClickListener {
                onSelectDishListenerParams(savedAddresse)
            }
        }
    }
}