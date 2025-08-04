package cloud.englert.ageindays.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import cloud.englert.ageindays.R
import cloud.englert.ageindays.data.Birthday

class BirthdayListAdapter(
    context: Context,
    data: List<Birthday>,
    private val dates: Array<String?>,
    private val people: Array<String?>,
    private val years: Array<String?>,
    private val days: Array<String?>
) : ArrayAdapter<Birthday?>(context, R.layout.list_birthdays,
    R.id.text_person, data) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView: View = super.getView(position, view, parent)

        (rowView.findViewById<View>(R.id.text_person) as TextView).text = people[position]
        (rowView.findViewById<View>(R.id.text_date) as TextView).text = dates[position]
        (rowView.findViewById<View>(R.id.text_years) as TextView).text = years[position]
        (rowView.findViewById<View>(R.id.text_days) as TextView).text = days[position]

        return rowView
    }

    fun setSelected(position: Int, selected: Boolean) {
        val birthday = getItem(position) ?: return
        birthday.selected = selected
        notifyDataSetChanged()
    }
}