package cloud.englert.ageindays.custom

import android.content.Context
import android.content.res.Configuration
import android.text.Html
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

        val birthday = getItem(position) ?: return rowView

        val nameView = (rowView.findViewById<View>(R.id.text_person) as TextView)
        nameView.text = people[position]
        (rowView.findViewById<View>(R.id.text_date) as TextView).text = dates[position]
        (rowView.findViewById<View>(R.id.text_years) as TextView).text =
            Html.fromHtml(years[position], Html.FROM_HTML_MODE_COMPACT)
        (rowView.findViewById<View>(R.id.text_days) as TextView).text =
            Html.fromHtml(days[position], Html.FROM_HTML_MODE_COMPACT)

        val textColor = if ((context.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
            context.getColor(R.color.light_grey)
        else
            context.getColor(R.color.dark_grey)
        val highlightColor = context.getColor(R.color.green)

        if (birthday.selected) {
            nameView.setTextColor(highlightColor)
        } else {
            nameView.setTextColor(textColor)
        }

        return rowView
    }

    fun setSelected(position: Int, selected: Boolean) {
        val birthday = getItem(position) ?: return
        birthday.selected = selected
        notifyDataSetChanged()
    }
}