package ca.unb.mobiledev.shuttershare

import android.R.attr.button
import android.content.Intent
import android.graphics.Color
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
import android.widget.Toast
import androidx.fragment.app.Fragment
import ca.unb.mobiledev.shuttershare.databinding.ActivityMainBinding
import ca.unb.mobiledev.shuttershare.util.ActiveEvents
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.Timestamp


class JoinEventFragment : Fragment() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = view.findViewById<EditText>(R.id.editTextCode)
        val joinButton = view.findViewById<Button>(R.id.join_button)

        joinButton.setEnabled(false)
        if (!joinButton.isEnabled)
            joinButton.setBackgroundResource(R.drawable.join_button_background_disabled)


        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.length < 6) {
                    joinButton.setEnabled(false)
                } else {
                    joinButton.setEnabled(true)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
                if (!joinButton.isEnabled)
                    joinButton.setBackgroundResource(R.drawable.join_button_background_disabled)
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
                if (joinButton.isEnabled)
                    joinButton.setBackgroundResource(R.drawable.join_button_background)
            }
        })

        joinButton.setOnClickListener {
            if(joinButton.isEnabled) {
                //check DB if event exists and is not expired
                val eventCode = editText.text.toString().trim()

                database = FirebaseDatabase.getInstance().getReference("events")
                database.child(eventCode).get().addOnSuccessListener {
                    if(it.exists()) {
                        val eventName = it.child("name").value as String
                        var endTimestamp = it.child("endTimestamp").value as Long
                        val nowTimestamp = Calendar.getInstance().timeInMillis
                        Log.d("Timestamps", "NOW -> timeInMillis: " + nowTimestamp)

//                        val endTimestamp = calendar.timeInMillis
//                        val endTimestamp = Timestamp.valueOf(endTimestampStr)
//                        val nowTimestamp = Timestamp(System.currentTimeMillis())

                        // if the event isn't over yet
                        if(nowTimestamp < (endTimestamp)) {
                            // Add to active_events.json
                            val activeEvents = ActiveEvents()
                            activeEvents.addEvent(requireContext(), eventCode, eventName, endTimestamp)

                            Log.d("Timestamps", "NOW is before END TIME")
                            Toast.makeText(context, "You have been added to '$eventName'", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(context, MainActivity::class.java))
                        }
                        else {
                            // indicate on screen that the event is no longer active
                            Toast.makeText(context, "This event is no longer active", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        Toast.makeText(context, "Event does not exist", Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to get event from DB", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}