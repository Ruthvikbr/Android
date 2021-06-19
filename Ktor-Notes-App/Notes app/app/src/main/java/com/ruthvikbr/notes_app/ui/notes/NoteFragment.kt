package com.ruthvikbr.notes_app.ui.notes

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ruthvikbr.notes_app.R
import com.ruthvikbr.notes_app.ui.BaseFragment
import com.ruthvikbr.notes_app.utils.Constants.KEY_LOGGED_IN_EMAIL
import com.ruthvikbr.notes_app.utils.Constants.KEY_LOGGED_IN_PASSWORD
import com.ruthvikbr.notes_app.utils.Constants.NO_EMAIL
import com.ruthvikbr.notes_app.utils.Constants.NO_PASSWORD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notes.*
import javax.inject.Inject

@AndroidEntryPoint
class NoteFragment : BaseFragment(R.layout.fragment_notes) {

    @Inject
    lateinit var sharedPref:SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabAddNote.setOnClickListener {
            findNavController().navigate(
                NoteFragmentDirections.actionNoteFragmentToAddEditFragment(
                    ""
                )
            )
        }
    }

    private fun logout(){
        sharedPref.edit().putString(KEY_LOGGED_IN_EMAIL, NO_EMAIL).apply()
        sharedPref.edit().putString(KEY_LOGGED_IN_PASSWORD, NO_PASSWORD).apply()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.noteFragment,true)
            .build()
        findNavController().navigate(
            NoteFragmentDirections.actionNoteFragmentToAuthFragment(),
            navOptions
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }
}