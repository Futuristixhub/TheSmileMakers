package com.smilemakers.dashBoard.appointmentFragment

import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.simplemobiletools.commons.extensions.adjustAlpha
import com.simplemobiletools.commons.extensions.applyColorFilter
import com.simplemobiletools.commons.extensions.beInvisible
import com.simplemobiletools.commons.extensions.beInvisibleIf
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.commons.views.MyRecyclerView
import com.smilemakers.R
import com.smilemakers.utils.*
import kotlinx.android.synthetic.main.event_item_day_view.view.*
import kotlinx.android.synthetic.main.event_item_day_view_simple.view.event_item_color_bar
import kotlinx.android.synthetic.main.event_item_day_view_simple.view.event_item_frame
import kotlinx.android.synthetic.main.event_item_day_view_simple.view.event_item_start
import kotlinx.android.synthetic.main.event_list_item.view.*

class DayEventsAdapter(
    activity: SimpleActivity,
    val events: ArrayList<Event>,
    recyclerView: MyRecyclerView,
    itemClick: (Any) -> Unit
) : MyRecyclerViewAdapter(activity, recyclerView, null, itemClick) {

    private val allDayString = resources.getString(R.string.all_day)
    private val replaceDescriptionWithLocation = activity.config.replaceDescription
    private val dimPastEvents = activity.config.dimPastEvents
    private var eventlst: ArrayList<String>? = null
    var count: Int = 0

    init {
        eventlst = arrayListOf()
        setupDragListener(true)
    }

    override fun getActionMenuId() = R.menu.cab_day

    override fun prepareActionMode(menu: Menu) {}

    override fun actionItemPressed(id: Int) {
        when (id) {
            R.id.cab_share -> shareEvents()
            R.id.cab_delete -> askConfirmDelete()
        }
    }

    override fun getSelectableItemCount() = events.size

    override fun getIsItemSelectable(position: Int) = true

    override fun getItemSelectionKey(position: Int) = events.getOrNull(position)?.id?.toInt()

    override fun getItemKeyPosition(key: Int) = events.indexOfFirst { it.id?.toInt() == key }

    override fun onActionModeCreated() {}

    override fun onActionModeDestroyed() {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /* val layoutId = when (viewType) {
             ITEM_EVENT -> R.layout.event_item_day_view
             else -> R.layout.event_item_day_view_simple
         }*/
        return createViewHolder(R.layout.event_item_day_view_simple, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.bindView(event, true, true) { itemView, layoutPosition ->
            setupView(itemView, event, position)
        }
        bindViewHolder(holder)
    }

    override fun getItemCount() = events.size

    override fun getItemViewType(position: Int): Int {
        val event = events[position]
        val detailField = if (replaceDescriptionWithLocation) event.location else event.description
        return if (detailField.isNotEmpty()) {
            ITEM_EVENT
        } else if (event.startTS == event.endTS) {
            ITEM_EVENT_SIMPLE
        } else if (event.getIsAllDay()) {
            val startCode = Formatter.getDayCodeFromTS(event.startTS)
            val endCode = Formatter.getDayCodeFromTS(event.endTS)
            if (startCode == endCode) {
                ITEM_EVENT_SIMPLE
            } else {
                ITEM_EVENT
            }
        } else {
            ITEM_EVENT
        }
    }

    private fun setupView(view: View, event: Event, position: Int) {
        view.apply {
            //  var event = e
            event_item_frame.isSelected = selectedKeys.contains(event.id?.toInt())

            event_item_description1?.text =
                if (replaceDescriptionWithLocation) event.location else event.description
            event_item_start.text =
                if (event.getIsAllDay()) allDayString else Formatter.getTimeFromTS(
                    context,
                    event.startTS
                )

            if (position > 0) {
                Log.d(
                    "tgggggg",
                    "kkkk......." + events[position] + "........" + events[position - 1]
                )
                if (events.get(position).equals(events.get(position - 1))) {
                    count++
                    if (count == 2) {
                        if (event.location == "Bapunagar") {
                            event_item_title2.text = event.title
                        } else {
                            event_item_title4.text = event.title
                        }
                    } else
                        if (count == 3) {
                            if (event.location == "Bapunagar") {
                                event_item_title1.text = event.title
                            } else {
                                event_item_title3.text = event.title
                            }
                        } else
                            if (count == 4) {
                                if (event.location == "Bapunagar") {
                                    event_item_title2.text = event.title
                                } else {
                                    event_item_title4.text = event.title
                                }
                            }
                } else {
                    count++
                    if (event.location == "Bapunagar") {
                        event_item_title1.text = event.title
                    } else {
                        event_item_title3.text = event.title
                    }
                }
            } else {
                Log.d(
                    "tgggggg",
                    "kkkk......." + events[position]
                )
                count++
                if (event.location == "Bapunagar") {
                    event_item_title1.text = event.title
                } else {
                    event_item_title3.text = event.title
                }
            }

            //  event_item_title1?.text = event.title
            //event_item_title2.text = e.title2
            //event_item_title3.text = e.title3
            //event_item_title4.text = e.title4
            //   event_item_end?.beInvisibleIf(event.startTS == event.endTS)
            event_item_color_bar.background.applyColorFilter(event.color)

            if (event.startTS != event.endTS) {
                val startCode = Formatter.getDayCodeFromTS(event.startTS)
                val endCode = Formatter.getDayCodeFromTS(event.endTS)

//                event_item_end?.apply {
//                    text = Formatter.getTimeFromTS(context, event.endTS)
//                    if (startCode != endCode) {
//                        if (event.getIsAllDay()) {
//                            text = Formatter.getDateFromCode(context, endCode, true)
//                        } else {
//                            append(" (${Formatter.getDateFromCode(context, endCode, true)})")
//                        }
//                    } else if (event.getIsAllDay()) {
//                        beInvisible()
//                    }
//                }
            }

            var newTextColor = textColor
            if (dimPastEvents && event.isPastEvent) {
                newTextColor = newTextColor.adjustAlpha(LOW_ALPHA)
            }

            //    event_item_start.setTextColor(newTextColor)
            //  event_item_end?.setTextColor(newTextColor)
            //event_item_title.setTextColor(newTextColor)
            //event_item_description?.setTextColor(newTextColor)
        }
    }

    private fun shareEvents() = activity.shareEvents(selectedKeys.distinct().map { it.toLong() })

    private fun askConfirmDelete() {
        val eventIds = selectedKeys.map { it.toLong() }.toMutableList()
        val eventsToDelete = events.filter { selectedKeys.contains(it.id?.toInt()) }
        val timestamps = eventsToDelete.map { it.startTS }
        val positions = getSelectedItemPositions()

        val hasRepeatableEvent = eventsToDelete.any { it.repeatInterval > 0 }
        DeleteEventDialog(activity, eventIds, hasRepeatableEvent) { it ->
            events.removeAll(eventsToDelete)

            ensureBackgroundThread {
                val nonRepeatingEventIDs =
                    eventsToDelete.asSequence().filter { it.repeatInterval == 0 }
                        .mapNotNull { it.id }.toMutableList()
                activity.eventsHelper.deleteEvents(nonRepeatingEventIDs, true)

                val repeatingEventIDs =
                    eventsToDelete.asSequence().filter { it.repeatInterval != 0 }
                        .mapNotNull { it.id }.toList()
                activity.handleEventDeleting(repeatingEventIDs, timestamps, it)
                activity.runOnUiThread {
                    removeSelectedItems(positions)
                }
            }
        }
    }
}
