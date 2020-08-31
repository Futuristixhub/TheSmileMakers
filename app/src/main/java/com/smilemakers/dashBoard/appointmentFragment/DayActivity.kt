package com.smilemakers.dashBoard.appointmentFragment

import android.app.AlertDialog
import android.app.ListActivity
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.TextView
import com.smilemakers.R

class DayActivity: ListActivity() {

    /**
     * Called when the activity is first created.
     */
    private val HOURS_PER_DAY = 24

    var mContext: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getListView().setBackgroundColor(Color.rgb(12, 12, 12));
        listView.dividerHeight = 0
        listAdapter = object : ListAdapter {
            override fun areAllItemsEnabled(): Boolean {
                // TODO Auto-generated method stub
                return false
            }

            override fun isEnabled(arg0: Int): Boolean {
                // TODO Auto-generated method stub
                return false
            }

            override fun getCount(): Int {
                // TODO Auto-generated method stub
                return HOURS_PER_DAY
            }

            override fun getItem(arg0: Int): Any? {
                // TODO Auto-generated method stub
                return null
            }

            override fun getItemId(arg0: Int): Long {
                // TODO Auto-generated method stub
                return 0
            }

            override fun getItemViewType(arg0: Int): Int {
                // TODO Auto-generated method stub
                return 0
            }

            override fun getView(
                position: Int,
                arg1: View,
                arg2: ViewGroup
            ): View {
                // TODO Auto-generated method stub
                val inflater = layoutInflater
                val listItem =
                    inflater.inflate(R.layout.list_item, listView, false) as View
                val hourTV =
                    listItem.findViewById<View>(R.id.hourTV) as TextView
                val amTV = listItem.findViewById<View>(R.id.amTV) as TextView
                hourTV.setTextColor(Color.BLUE)
                amTV.setTextColor(Color.BLUE)
                val eventsLL =
                    listItem.findViewById<View>(R.id.eventsLL) as LinearLayout
                hourTV.text = ((position + 9) % 24).toString()
                //I set am/pm for each entry ... you could specify which entries
                if (position >= 0 && position <= 2 || position >= 15 && position <= 23) amTV.text =
                    "am" else amTV.text = "pm"
                eventsLL.setOnClickListener { // TODO Auto-generated method stub
                    val alert =
                        AlertDialog.Builder(mContext)
                    alert.setTitle("New Event")
                    alert.setMessage("Event:")

                    // Set an EditText view to get user input
                    val input = EditText(mContext)
                    alert.setView(input)
                    alert.setPositiveButton(
                        "Add"
                    ) { dialog, whichButton ->
                        val A = TextView(mContext)
                        A.text = input.text
                        A.setTextColor(Color.BLACK)
                        eventsLL.addView(A)
                    }
                    alert.setNegativeButton(
                        "Cancel"
                    ) { dialog, whichButton -> }
                    alert.show()
                }
                return listItem
            }

            override fun getViewTypeCount(): Int {
                // TODO Auto-generated method stub
                return 1
            }

            override fun hasStableIds(): Boolean {
                // TODO Auto-generated method stub
                return false
            }

            override fun isEmpty(): Boolean {
                // TODO Auto-generated method stub
                return false
            }

            override fun registerDataSetObserver(arg0: DataSetObserver) {
                // TODO Auto-generated method stub
            }

            override fun unregisterDataSetObserver(arg0: DataSetObserver) {
                // TODO Auto-generated method stub
            }
        }
    }
}