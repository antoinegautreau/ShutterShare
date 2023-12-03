package ca.unb.mobiledev.shuttershare

import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ca.unb.mobiledev.shuttershare.databinding.FragmentCreateEvent2Binding
import ca.unb.mobiledev.shuttershare.entity.Event
import ca.unb.mobiledev.shuttershare.util.ActiveEvents
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.Timestamp


class CreateEventFragment : Fragment() {
    private var _binding: FragmentCreateEvent2Binding? = null
    private val binding get() = _binding!!

    private lateinit var startDateBtn:ImageButton
    private lateinit var endDateBtn:ImageButton
    private lateinit var startTimeBtn:ImageButton
    private lateinit var endTimeBtn :ImageButton
    private lateinit var createEventBtn :Button

    private lateinit var eventName :EditText
    private lateinit var endTimeText:TextView
    private lateinit var startTimeText :TextView
    private lateinit var endDateText :TextView
    private lateinit var startDateText:TextView

    // for database
    private var endYear = 0
    private var endMonth = 0
    private var endDay = 0
    private var endHour = 0
    private var endMinute = 0

    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event2, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateEvent2Binding.bind(view)

        startDateBtn = view.findViewById<ImageButton>(R.id.startDateCalendarBtn)
        endDateBtn = view.findViewById<ImageButton>(R.id.endDateCalendarBtn)
        startTimeBtn = view.findViewById<ImageButton>(R.id.StartTimePickerBtn)
        endTimeBtn = view.findViewById<ImageButton>(R.id.EndTimePickerBtn)
        createEventBtn = view.findViewById<Button>(R.id.create_button)

        eventName = view.findViewById<EditText>(R.id.editText)
        endTimeText = view.findViewById<TextView>(R.id.EndTimeEditText)
        startTimeText = view.findViewById<TextView>(R.id.StartTimeEditText)
        endDateText = view.findViewById<TextView>(R.id.endDateEditText)
        startDateText = view.findViewById<TextView>(R.id.startDateEditText)


        _binding.apply {
            startDateBtn?.setOnClickListener{
                Log.println(Log.DEBUG, "CreateEventFragment", "In the onClickListener")
                //DatePickerDialog().show()
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                datePickerFragment.show(supportFragmentManager, "datePicker")

                //set Fragment result listener
                supportFragmentManager?.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY"){
                            val date = bundle.getString("SELECTED_DATE")
//                            val editText = view.findViewById<TextView>(R.id.startDateEditText)
                            startDateText?.setText(date)

                        }
                }
            }

            endDateBtn?.setOnClickListener{
                Log.println(Log.DEBUG, "CreateEventFragment", "In the onClickListener")
                //DatePickerDialog().show()
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                datePickerFragment.show(supportFragmentManager, "datePicker")

                //set Fragment result listener
                supportFragmentManager?.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY"){
                    val date = bundle.getString("SELECTED_DATE")
                    endYear = bundle.getInt("SELECTED_YEAR")
                    endMonth = bundle.getInt("SELECTED_MONTH")
                    endDay = bundle.getInt("SELECTED_DAY")

                    //val editText = view.findViewById<TextView>(R.id.endDateEditText)
                    endDateText?.setText(date)

                }
                }
            }

            startTimeBtn?.setOnClickListener{
                val timePickerFragment = TimePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                timePickerFragment.show(supportFragmentManager, "timePicker")

                //set Fragment result listener
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY"){
                    val time = bundle.getString("SELECTED_TIME")
                    //val editText = view.findViewById<TextView>(R.id.StartTimeEditText)
                    startTimeText?.setText(time)

                }
                }
            }

            endTimeBtn?.setOnClickListener{
                val timePickerFragment = TimePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                timePickerFragment.show(supportFragmentManager, "timePicker")

                //set Fragment result listener
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY"){
                    val time = bundle.getString("SELECTED_TIME")
                    endHour = bundle.getInt("SELECTED_HOUR")
                    endMinute = bundle.getInt("SELECTED_MINUTE")
                    //val editText = view.findViewById<TextView>(R.id.EndTimeEditText)
                    endTimeText?.setText(time)

                }
                }
            }



            createEventBtn.setOnClickListener{
                val eventNameStr : String = eventName.text.toString().trim()
                var startDateStr : String = startDateText.text.toString().trim()
                var endDateStr : String = endDateText.text.toString().trim()
                var startTimeStr: String = startTimeText.text.toString().trim()
                var endTimeStr : String = endTimeText.text.toString().trim()

                if (eventNameStr.isEmpty()){
                    eventName.error = "Event Name Required"
                    return@setOnClickListener
                }
                else if (startDateStr.isEmpty()){
                    startDateStr = "Start Date Required"
                    return@setOnClickListener
                }
                else if (endDateStr.isEmpty()){
                    endDateStr = "End Date Required"
                    return@setOnClickListener
                }
                else if (startTimeStr.isEmpty()){
                    startTimeStr = "Start Time Required"
                    return@setOnClickListener
                }
                else if (endTimeStr.isEmpty()){
                    endTimeStr = "End Time Required"
                    return@setOnClickListener
                }
                else{
                    Toast.makeText(activity, "Event Created Successfully", Toast.LENGTH_SHORT).show()
                }

                // Generating an Event Code
                // SHOULD CHECK DB TO MAKE SURE THIS EVENT CODE DOESN'T EXIST BEFORE UPLOADING IT
                val randomValues = List(6) { (0..9).random() }
                var eventCode = ""
                for (i in 0..5) {
                    eventCode += randomValues[i].toString()
                }

                val calendar: Calendar = Calendar.getInstance()
                calendar.set(endYear, endMonth, endDay, endHour, endMinute, 0)
                val endTimestamp = calendar.timeInMillis
                Log.d("Calendar", "Year: " + endYear + ", Month: " + endMonth + ", Day: " + endDay + ", Hour: " + endHour + ", Minute: " + endMinute)
                Log.d("Calendar", "Actual: " + calendar.toString())
                //Submit to database
                // "events" in the DB is to check to make sure that when we try joining an event that it actually exists and is not expired
                // probably don't need startDate and startTime, can just have an endDate and endTime and can then just assume the event is
                // started from NOW.
                val event = Event(eventCode, eventNameStr, endTimestamp)
                database = FirebaseDatabase.getInstance().getReference("events")
                database.child(eventCode).setValue(event).addOnSuccessListener {
                    val activeEvents = ActiveEvents()
                    activeEvents.addEvent(requireContext(), eventCode, eventNameStr, endTimestamp)

                    Toast.makeText(context, "Event added successfully to DB", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to add event to DB", Toast.LENGTH_SHORT).show()
                }

                // write event into the active_events.json internal storage file
            }

        }

    }

//    private val watcher: TextWatcher = object : TextWatcher {
//        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//        override fun afterTextChanged(s: Editable) {
//            if (T1.getText().toString().length() === 0 && T2.getText().toString()
//                    .length() === 0 && T3.getText().toString().length() === 0 && T4.getText()
//                    .toString().length() === 0 && T5.toString().trim()
//                    .length() === 0 && T6.getText().toString().length() === 0 && T7.getText()
//                    .toString().length() === 0 && T8.getText().toString()
//                    .length() === 0 && T9.getText().toString().length() === 0
//            ) {
//                ButtonScore.setEnabled(false)
//            } else {
//                ButtonScore.setEnabled(true)
//            }
//        }
//    }

}