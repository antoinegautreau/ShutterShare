package ca.unb.mobiledev.shuttershare

import android.R.attr.button
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import ca.unb.mobiledev.shuttershare.databinding.ActivityMainBinding


class JoinEventFragment : Fragment() {
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
    }

}