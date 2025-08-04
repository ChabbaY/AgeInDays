package cloud.englert.ageindays.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

import cloud.englert.ageindays.MainActivity
import cloud.englert.ageindays.R
import cloud.englert.ageindays.data.Birthday
import cloud.englert.ageindays.data.Date
import cloud.englert.ageindays.databinding.FragmentEditBinding

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the second destination in the navigation.
 */
class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private lateinit var activity: MainActivity
    private var birthday: Birthday? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        activity = requireActivity() as MainActivity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        birthday = activity.birthdayToEdit
        if (birthday != null) {
            binding.datePicker.updateDate(birthday!!.date.year,
                birthday!!.date.month - 1,
                birthday!!.date.day)
            binding.editTextPerson.setText(birthday!!.person)
        }

        binding.buttonSave.setOnClickListener { _ -> save() }
        binding.buttonCancel.setOnClickListener { _ -> cancel() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun save() {
        if (birthday == null) {
            birthday = Birthday(-1, Date.fromString("2025-01-01"), "",
                false)
        }

        birthday!!.date = Date(binding.datePicker.year, binding.datePicker.month + 1,
            binding.datePicker.dayOfMonth)
        birthday!!.person = binding.editTextPerson.text.toString()

        activity.saveBirthday(birthday!!)

        cancel()
    }

    private fun cancel() {
        resetFields()
        navigateBack()
    }

    private fun resetFields() {
        binding.editTextPerson.setText("")
    }

    private fun navigateBack() {
        NavHostFragment.findNavController(this@EditFragment)
            .navigate(R.id.action_EditFragment_to_MainFragment)
    }
}