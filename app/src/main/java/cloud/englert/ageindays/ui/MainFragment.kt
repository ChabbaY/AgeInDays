package cloud.englert.ageindays.ui

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView

import androidx.core.util.size
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

import java.util.Locale

import cloud.englert.ageindays.MainActivity
import cloud.englert.ageindays.R
import cloud.englert.ageindays.custom.BirthdayListAdapter
import cloud.englert.ageindays.data.Birthday
import cloud.englert.ageindays.databinding.FragmentMainBinding

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private lateinit var activity: MainActivity

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        activity = requireActivity() as MainActivity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeContextualActionBar()

        binding.buttonAdd.setOnClickListener { _ ->
            activity.birthdayToEdit = null
            NavHostFragment.findNavController(this@MainFragment)
                .navigate(R.id.action_MainFragment_to_EditFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        updateListView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createListView(view: ListView, data: List<Birthday>) {
        val dates = arrayOfNulls<String>(data.size)
        val people = arrayOfNulls<String>(data.size)
        val years = arrayOfNulls<String>(data.size)
        val days = arrayOfNulls<String>(data.size)

        for ((index, birthday) in data.withIndex()) {
            dates[index] = birthday.date.toString()
            people[index] = birthday.person
            years[index] = String.format(Locale.getDefault(),
                getString(R.string.years), birthday.getAgeYears())
            days[index] = String.format(Locale.getDefault(),
                getString(R.string.days), birthday.getAgeDays())
        }

        val dataAdapter = BirthdayListAdapter(activity, data, dates, people, years, days)
        view.adapter = dataAdapter
    }

    private fun updateListView() {
        val birthdays: List<Birthday> = activity.allBirthdays
        createListView(binding.listView, birthdays)
    }

    private fun initializeContextualActionBar() {
        Log.d(LOG_TAG, "initializing CAB")
        val listView = binding.listView
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        Log.d(LOG_TAG, "choice mode: ${listView.choiceMode}")

        listView.setMultiChoiceModeListener(object : AbsListView.MultiChoiceModeListener {
            var selCount: Int = 0
            override fun onItemCheckedStateChanged(
                mode: ActionMode, position: Int, id: Long, checked: Boolean
            ) {
                val adapter =
                    listView.adapter as BirthdayListAdapter
                if (checked) {
                    selCount++
                    adapter.setSelected(position, true)
                } else {
                    selCount--
                    adapter.setSelected(position, false)
                }
                val cabTitle = selCount.toString() + " " + getString(R.string.cab_checked_string)
                mode.title = cabTitle
                mode.invalidate()
            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                requireActivity().menuInflater
                    .inflate(R.menu.menu_contextual_action_bar, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu): Boolean {
                val itemModify = menu.findItem(R.id.cab_change)
                itemModify.isVisible = selCount == 1 // edit only if exactly 1 selected
                return true
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                val checkedPositions =
                    listView.checkedItemPositions
                if (item.itemId == R.id.cab_delete) {
                    for (i in 0 until checkedPositions.size) {
                        val isChecked = checkedPositions.valueAt(i)
                        if (isChecked) {
                            val positionInListView = checkedPositions.keyAt(i)
                            val birthday =
                                listView.getItemAtPosition(positionInListView) as Birthday
                            activity.deleteBirthday(birthday)
                            Log.d(LOG_TAG, "Deleted birthday with id: ${birthday.id}")
                        }
                    }
                    updateListView()
                    mode.finish()
                    return true
                } else if (item.itemId == R.id.cab_change) {
                    for (i in 0 until checkedPositions.size) {
                        val isChecked = checkedPositions.valueAt(i)
                        if (isChecked) {
                            val positionInListView = checkedPositions.keyAt(i)
                            val birthday =
                                listView.getItemAtPosition(positionInListView) as Birthday

                            activity.birthdayToEdit = birthday
                            NavHostFragment.findNavController(this@MainFragment)
                                .navigate(R.id.action_MainFragment_to_EditFragment)
                        }
                    }
                    mode.finish()
                    return true
                }
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                selCount = 0
                val adapter = listView.adapter as BirthdayListAdapter
                for (i in 0 until adapter.count)
                    adapter.setSelected(i, false)
            }
        })
    }

    companion object {
        private val LOG_TAG = MainFragment::class.java.simpleName
    }
}