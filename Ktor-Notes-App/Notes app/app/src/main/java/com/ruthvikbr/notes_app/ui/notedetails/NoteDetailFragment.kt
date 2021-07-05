package com.ruthvikbr.notes_app.ui.notedetails
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ruthvikbr.notes_app.R
import com.ruthvikbr.notes_app.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_note_detail.*

class NoteDetailFragment  : BaseFragment(R.layout.fragment_note_detail) {
    private val args: NoteDetailFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabEditNote.setOnClickListener {
            findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditFragment(args.id))
        }
    }

}