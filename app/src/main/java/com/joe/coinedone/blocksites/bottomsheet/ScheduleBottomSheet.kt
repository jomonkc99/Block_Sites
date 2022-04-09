package com.joe.coinedone.blocksites.bottomsheet

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.joe.coinedone.blocksites.R
import com.joe.coinedone.blocksites.activities.ScheduleModeActivity
import com.joe.coinedone.blocksites.interfaces.ScheduleChangeListener
import com.joe.coinedone.blocksites.utils.AppPreferences
import com.joe.coinedone.blocksites.utils.Constants
import com.joe.coinedone.blocksites.utils.Utils
import java.util.*

class ScheduleBottomSheet: BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ScheduleBottomSheet"
    }

    lateinit var viewRoot: View
    lateinit var scheduleListener: ScheduleChangeListener
    var fromModeChange = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewRoot = inflater.inflate(R.layout.layout_schedule_sheet, container, false)
        return viewRoot
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewRoot.findViewById<ImageView>(R.id.imgClose).setOnClickListener {
            dismiss()
        }
        viewRoot.findViewById<TextView>(R.id.txtTimeFrom).setOnClickListener {
            showTimePicker(Constants.TIME_FROM, it)
        }
        viewRoot.findViewById<TextView>(R.id.txtTimeTo).setOnClickListener {
            showTimePicker(Constants.TIME_TO, it)
        }
        viewRoot.findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveSchedule(viewRoot.findViewById<TextView>(R.id.txtTimeFrom).text.toString(), viewRoot.findViewById<TextView>(R.id.txtTimeTo).text.toString())
        }

        setTime(viewRoot.findViewById<TextView>(R.id.txtTimeFrom), AppPreferences.getValueFromSharedPreference(requireContext(), Constants.TIME_FROM))
        setTime(viewRoot.findViewById<TextView>(R.id.txtTimeTo), AppPreferences.getValueFromSharedPreference(requireContext(), Constants.TIME_TO))

        if(fromModeChange)
            viewRoot.findViewById<TextView>(R.id.txtTitle).setText("Edit Timing")

    }

    fun saveSchedule(strTimeFrom: String, strTimeTo: String) {

        if (Utils.checkTheTimes(strTimeFrom, strTimeTo)) {
            AppPreferences.setValueInSharedPreference(requireContext(), Constants.TIME_FROM, strTimeFrom)
            AppPreferences.setValueInSharedPreference(requireContext(), Constants.TIME_TO, strTimeTo)

            Toast.makeText(requireContext(), "Schedule is saved successfully !!", Toast.LENGTH_SHORT).show()
            dismiss()
            if (fromModeChange)
                scheduleListener.onScheduleChanged()
            else
                requireContext().startActivity(Intent(requireContext(), ScheduleModeActivity::class.java))
        }
        else{
            Toast.makeText(requireContext(), "Please check the times entered", Toast.LENGTH_LONG).show()
        }
    }

    fun setTime(textView: TextView, strTime: String){
        if (strTime.isNotEmpty())
            textView.text = strTime
        else
            textView.text = "--"
    }

    fun showTimePicker(type: String, textView: View) {
        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour: Int
        val minute: Int

        val timeFromPref = AppPreferences.getValueFromSharedPreference(requireContext(), type)

        if (timeFromPref.isEmpty()) {
            hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            minute = mcurrentTime.get(Calendar.MINUTE)
        }
        else {
            val timeLocal = Utils.getLocalTime(timeFromPref)
            hour = timeLocal.hour
            minute = timeLocal.minute
        }


        mTimePicker = TimePickerDialog(context, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                var timeRep = "AM"
                var hour = hourOfDay
                if (hour>11){
                    timeRep = "PM"
                    if (hour>12) hour -= 12
                }
                val strTime = String.format("%02d:%02d %s", hour, minute, timeRep)
                (textView as TextView).setText(strTime)
            }
        }, hour, minute, false)
        mTimePicker.show()
    }

    fun setListener(listener: ScheduleChangeListener, fromMode: Boolean){
        this.scheduleListener = listener
        this.fromModeChange = fromMode
    }
}