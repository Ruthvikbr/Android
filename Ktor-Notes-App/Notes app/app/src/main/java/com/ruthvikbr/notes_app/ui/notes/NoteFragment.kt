package com.ruthvikbr.notes_app.ui.notes

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ruthvikbr.notes_app.R
import com.ruthvikbr.notes_app.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_notes.*

class NoteFragment : BaseFragment(R.layout.fragment_notes) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabAddNote.setOnClickListener {
            findNavController().navigate(NoteFragmentDirections.actionNoteFragmentToAddEditFragment(""))
        }
    }
}