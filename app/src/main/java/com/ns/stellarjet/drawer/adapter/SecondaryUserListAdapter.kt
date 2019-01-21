package com.ns.stellarjet.drawer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.secondaryusers.SecondaryUserInfoList
import com.ns.stellarjet.R

class SecondaryUserListAdapter(
    private val mSecondaryUsersList : List<SecondaryUserInfoList>,
    private val onSelectUsersListener : (SecondaryUserInfoList) -> Unit ,
    private val onSelectUsersActionListener : (SecondaryUserInfoList , Int) -> Unit ) : RecyclerView.Adapter<SecondaryUserListAdapter.SecondaryUserListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondaryUserListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_row_managers, parent, false)
        return SecondaryUserListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mSecondaryUsersList.size
    }

    override fun onBindViewHolder(holder: SecondaryUserListViewHolder, position: Int) {
        holder.bind(mSecondaryUsersList[position], onSelectUsersListener , onSelectUsersActionListener , position)
    }

    class SecondaryUserListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val mManagersName : TextView = itemView.findViewById(R.id.textView_row_managers_name)
        private val mManagersPhone : TextView = itemView.findViewById(R.id.textView_row_managers_phone)
        private val mOverFlow : ImageView = itemView.findViewById(R.id.imageView_row_managers_overflow)

        fun bind(
            mSecondaryUsers: SecondaryUserInfoList,
            onSelectUsersListener: (SecondaryUserInfoList) -> Unit ,
            onSelectUsersActionListener : (SecondaryUserInfoList , Int) -> Unit ,
            position: Int){

            mManagersName.text = mSecondaryUsers.su_name
            mManagersPhone.text = mSecondaryUsers.su_phone

            mOverFlow.setOnClickListener {
                onSelectUsersActionListener(mSecondaryUsers , position)
            }

            itemView.setOnClickListener {
                onSelectUsersListener(mSecondaryUsers)
            }
        }
    }

}