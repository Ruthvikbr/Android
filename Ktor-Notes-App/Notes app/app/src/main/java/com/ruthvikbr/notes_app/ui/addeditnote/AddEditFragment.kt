package com.ruthvikbr.notes_app.ui.addeditnote

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ruthvikbr.notes_app.R
import com.ruthvikbr.notes_app.data.local.entities.Note
import com.ruthvikbr.notes_app.ui.BaseFragment
import com.ruthvikbr.notes_app.ui.dialogs.ColorPickerDialogFragment
import com.ruthvikbr.notes_app.utils.Constants.DEFAULT_NOTE_COLOR
import com.ruthvikbr.notes_app.utils.Constants.KEY_LOGGED_IN_EMAIL
import com.ruthvikbr.notes_app.utils.Constants.KEY_LOGGED_IN_PASSWORD
import com.ruthvikbr.notes_app.utils.Constants.NO_EMAIL
import com.ruthvikbr.notes_app.utils.Constants.NO_PASSWORD
import com.ruthvikbr.notes_app.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_edit_note.*
import kotlinx.android.synthetic.main.item_note.view.*
import java.util.*
import javax.inject.Inject

const val  TAG = "AddEditNoteFragment"

@AndroidEntryPoint
class AddEditFragment : BaseFragment(R.layout.fragment_add_edit_note) {
    private val viewModel: AddEditViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPreferences
    private val args: AddEditFragmentArgs by navArgs()
    private var curNote: Note? = null
    private var curNoteColor: String = DEFAULT_NOTE_COLOR

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.id.isNotEmpty()) {
            viewModel.getNoteById(args.id)
            subscribeToObservers()
        }

        if(savedInstanceState!=null){
            val colorPickerDialogFragment = parentFragmentManager.findFragmentByTag(TAG)
            as ColorPickerDialogFragment?
            colorPickerDialogFragment?.setPositiveListener {
                changeNoteColor(it)
            }
        }

        viewNoteColor.setOnClickListener {
            ColorPickerDialogFragment().apply {
                setPositiveListener {
                    changeNoteColor(it)
                }
            }.show(parentFragmentManager,TAG)
        }
    }

    private fun changeNoteColor(colorString: String) {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.circle_shape, null)
        drawable?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            val color = Color.parseColor("#${colorString}")
            DrawableCompat.setTint(wrappedDrawable, color)
            viewNoteColor.background = wrappedDrawable
            curNoteColor = colorString
        }
    }

    private fun subscribeToObservers() {
    viewModel.note.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        it?.getContentIfNotHandled()?.let {result->
            when(result.status){
                Status.SUCCESS->{
                    val note = result.data!!
                    curNote = note
                    etNoteTitle.setText(note.title)
                    etNoteContent.setText(note.content)
                    changeNoteColor(note.color)
                }
                Status.ERROR ->{
                    showSnackBar(result?.message?:"Note not found")
                }
                Status.LOADING ->{
                    //No operation
                }
            }
        }
    })
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote() {
        val authEmail = sharedPref.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL
        val authPassword = sharedPref.getString(KEY_LOGGED_IN_PASSWORD, NO_PASSWORD) ?: NO_PASSWORD

        val title = etNoteTitle.text.toString()
        val content = etNoteContent.text.toString()
        if (title.isEmpty() || content.isEmpty()) {
            return
        }
        val date = System.currentTimeMillis()
        val color = curNoteColor
        val id = curNote?.id ?: UUID.randomUUID().toString()
        val owners = curNote?.owners ?: listOf(authEmail)
        val note = Note(title, content, date, owners, color, id = id)
        viewModel.insertNote(note)
    }

}