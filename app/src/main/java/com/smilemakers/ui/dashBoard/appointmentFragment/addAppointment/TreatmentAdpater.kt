package com.smilemakers.ui.dashBoard.appointmentFragment.addAppointment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.appointmentFragment.Doctors
import com.smilemakers.ui.dashBoard.appointmentFragment.Patients
import com.smilemakers.ui.dashBoard.appointmentFragment.Treatments
import java.util.*
import kotlin.collections.ArrayList

class TreatmentAdpater(val context: Context, var list: ArrayList<Treatments>) : BaseAdapter() ,Filterable{

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    var lst: ArrayList<Treatments> = list

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return super.getDropDownView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.simple_spinner_item, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = list.get(position).treatment_name

        return view
    }

    private class ItemHolder(row: View?) {
        val label: TextView

        init {
            label = row?.findViewById(R.id.name) as TextView
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                val filteredList: MutableList<Treatments> = arrayListOf()

                if (constraint!!.isEmpty()) {
                    filteredList.addAll(lst)
                } else {
                    val filterPattern =
                        constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                    for (item in lst) {
                        if (item.treatment_name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList

                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                if (filterResults.values != null) {
                    list = filterResults.values as ArrayList<Treatments>
                    notifyDataSetChanged()
                }
            }

        }

    }

}