package ca.unb.mobiledev.shuttershare
import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.Calendar
import java.util.Locale

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        val selectDate = SimpleDateFormat("dd-MM-yyyy", Locale.CANADA).format(calendar.time)
        val selectDateBundle = Bundle()
        selectDateBundle.putString("SELECTED_DATE", selectDate)
        selectDateBundle.putInt("SELECTED_YEAR", year)
        selectDateBundle.putInt("SELECTED_MONTH", month)
        selectDateBundle.putInt("SELECTED_DAY", day)

        setFragmentResult("REQUEST_KEY", selectDateBundle)
    }
}