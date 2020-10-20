package com.smilemakers.dashBoard.appointmentFragment.calendar

import android.content.Intent
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.simplemobiletools.commons.extensions.applyColorFilter
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.commons.views.MyRecyclerView
import com.smilemakers.R
import com.smilemakers.dashBoard.appointmentFragment.addAppointment.Event
import com.smilemakers.dashBoard.appointmentFragment.addAppointment.Events
import com.smilemakers.dashBoard.appointmentFragment.calendar.SimpleActivity
import com.smilemakers.dashBoard.appointmentFragment.addAppointment.AppointmentFormActivity
import com.smilemakers.utils.*
import kotlinx.android.synthetic.main.event_item_day_view_simple.view.*


class DayEventsAdapter(
    activity: SimpleActivity,
    val events: ArrayList<Events>,
    recyclerView: MyRecyclerView,
    itemClick: (Any) -> Unit
) : MyRecyclerViewAdapter(activity, recyclerView, null, itemClick) {

    private val allDayString = resources.getString(R.string.all_day)
    private val replaceDescriptionWithLocation = activity.config.replaceDescription
    private val dimPastEvents = activity.config.dimPastEvents

    init {
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

    override fun getItemSelectionKey(position: Int) =
        events.getOrNull(position)?.event!!.id?.toInt()

    override fun getItemKeyPosition(key: Int) =
        events.indexOfFirst { it.event!!.id?.toInt() == key }

    override fun onActionModeCreated() {}

    override fun onActionModeDestroyed() {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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

    private fun setupView(view: View, event: Events, position: Int) {
        view.apply {
            event_item_frame.isSelected = selectedKeys.contains(event.event.id?.toInt())

            event_item_start.text =
                if (event.event.getIsAllDay()) allDayString else Formatter.getTimeFromTS(
                    context,
                    event.event.startTS
                )
            var eventlstnew = arrayListOf<Event>()
            for (i in event.eventlst.indices) {
                if (i < event.eventlst.size) {
                    if (event.event.startTS.equals(event.eventlst[i].startTS)) {
                        eventlstnew!!.add(event.eventlst[i])
                    }
                }
            }
            for (i in eventlstnew.indices) {
                if (eventlstnew[i].location == "Bapunagar") {
                    if (event_item_title1.text.isEmpty()) {
                        event_item_title1.text = eventlstnew[i].title
                        event_item_title1.hint= eventlstnew[i].id.toString()
                    } else if (event_item_title2.text.isEmpty()) {
                        event_item_title2.text = eventlstnew[i].title
                        event_item_title2.hint= eventlstnew[i].id.toString()
                    }
                } else if (eventlstnew[i].location == "Nikol") {
                    if (event_item_title3.text.isEmpty()) {
                        event_item_title3.text = eventlstnew[i].title
                        event_item_title3.hint= eventlstnew[i].id.toString()
                    } else if (event_item_title4.text.isEmpty()) {
                        event_item_title4.text = eventlstnew[i].title
                        event_item_title4.hint= eventlstnew[i].id.toString()
                    }
                }
            }

            event_item_title1.setOnClickListener {
               editEvent(event_item_title1.hint.toString(),event.event.startTS)
            }
            event_item_title2.setOnClickListener {
                editEvent(event_item_title2.hint.toString(),event.event.startTS)
            }
            event_item_title3.setOnClickListener {
                editEvent(event_item_title3.hint.toString(),event.event.startTS)
            }
            event_item_title4.setOnClickListener {
                editEvent(event_item_title4.hint.toString(),event.event.startTS)
            }

            event_item_color_bar.background.applyColorFilter(event.event.color)

        }
    }

    private fun editEvent(id: String, startTs:Long) {
        Intent(activity, AppointmentFormActivity::class.java).apply {
            putExtra(EVENT_ID, id.toLong())
            putExtra(EVENT_OCCURRENCE_TS,startTs)
            activity.startActivity(this)
        }
    }

    private fun shareEvents() = activity.shareEvents(selectedKeys.distinct().map { it.toLong() })

    private fun askConfirmDelete() {
        val eventIds = selectedKeys.map { it.toLong() }.toMutableList()
        val eventsToDelete = events.filter { selectedKeys.contains(it.event!!.id?.toInt()) }
        val timestamps = eventsToDelete.map { it.event!!.startTS }
        val positions = getSelectedItemPositions()

        val hasRepeatableEvent = eventsToDelete.any { it.event!!.repeatInterval > 0 }
        DeleteEventDialog(activity, eventIds, hasRepeatableEvent) { it ->
            events.removeAll(eventsToDelete)

            ensureBackgroundThread {
                val nonRepeatingEventIDs =
                    eventsToDelete.asSequence().filter { it.event!!.repeatInterval == 0 }
                        .mapNotNull { it.event!!.id }.toMutableList()
                activity.eventsHelper.deleteEvents(nonRepeatingEventIDs, true)

                val repeatingEventIDs =
                    eventsToDelete.asSequence().filter { it.event!!.repeatInterval != 0 }
                        .mapNotNull { it.event!!.id }.toList()
                activity.handleEventDeleting(repeatingEventIDs, timestamps, it)
                activity.runOnUiThread {
                    removeSelectedItems(positions)
                }
            }
        }
    }
}