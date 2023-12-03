package ca.unb.mobiledev.shuttershare

import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.text.TimeZoneFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.timepicker.TimeFormat
import java.util.Locale

class TimePickerFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener {
    val c = Calendar.getInstance()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }
    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        c.set(Calendar.HOUR_OF_DAY, hour)
        c.set(Calendar.MINUTE, minute)

        var selectTime = "$hour:$minute";
        if(minute < 10) {
            selectTime = "$hour:0$minute";
        }

//        val selectTime = SimpleDateFormat("", Locale.ENGLISH).format(c.time)
        val selectTimeBundle = Bundle()
        selectTimeBundle.putString("SELECTED_TIME", selectTime)
        selectTimeBundle.putInt("SELECTED_HOUR", c.get(Calendar.HOUR_OF_DAY))//hour)
        selectTimeBundle.putInt("SELECTED_MINUTE", c.get(Calendar.MINUTE))//minute)


        setFragmentResult("REQUEST_KEY", selectTimeBundle)
    }

}