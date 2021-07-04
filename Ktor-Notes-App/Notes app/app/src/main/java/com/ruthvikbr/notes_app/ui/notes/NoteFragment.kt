package com.ruthvikbr.notes_app.ui.notes

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruthvikbr.notes_app.R
import com.ruthvikbr.notes_app.ui.BaseFragment
import com.ruthvikbr.notes_app.ui.adapters.NoteAdapter
import com.ruthvikbr.notes_app.utils.Constants.KEY_LOGGED_IN_EMAIL
import com.ruthvikbr.notes_app.utils.Constants.KEY_LOGGED_IN_PASSWORD
import com.ruthvikbr.notes_app.utils.Constants.NO_EMAIL
import com.ruthvikbr.notes_app.utils.Constants.NO_PASSWORD
import com.ruthvikbr.notes_app.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notes.*
import javax.inject.Inject

@AndroidEntryPoint
class NoteFragment : BaseFragment(R.layout.fragment_notes) {


    private val viewModel: NotesViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPreferences

    private lateinit var noteAdapter: NoteAdapter

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
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
        setUpRecyclerView()
        subscribeToObservers()
        noteAdapter.setOnClickListener {
            findNavController().navigate(
                NoteFragmentDirections.actionNoteFragmentToNoteDetailFragment(it.id)
            )
        }


        fabAddNote.setOnClickListener {
            findNavController().navigate(
                NoteFragmentDirections.actionNoteFragmentToAddEditFragment(
                    ""
                )
            )
        }
    }

    private fun subscribeToObservers() {
        viewModel.AllNotes.observe(viewLifecycleOwner, Observer {
            it?.let { it ->
                val result = it.peekContent()
                when (result.status) {
                    Status.SUCCESS -> {
                        noteAdapter.notes = result.data!!
                        swipeRefreshLayout.isRefreshing = false
                    }
                    Status.LOADING -> {
                        result.data?.let { notes ->
                            noteAdapter.notes = notes
                        }
                        swipeRefreshLayout.isRefreshing = true
                    }
                    Status.ERROR -> {
                        it.getContentIfNotHandled()?.let {errorResource ->
                            errorResource.message?.let { message->
                                showSnackBar(message)
                            }
                        }
                        result.data?.let { notes ->
                            noteAdapter.notes = notes
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        })
    }

    private fun setUpRecyclerView() = rvNotes.apply {
        noteAdapter = NoteAdapter()
        adapter = noteAdapter
        layoutManager = LinearLayoutManager(requireContext())

    }

    private fun logout() {
        sharedPref.edit().putString(KEY_LOGGED_IN_EMAIL, NO_EMAIL).apply()
        sharedPref.edit().putString(KEY_LOGGED_IN_PASSWORD, NO_PASSWORD).apply()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.noteFragment, true)
            .build()
        findNavController().navigate(
            NoteFragmentDirections.actionNoteFragmentToAuthFragment(),
            navOptions
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }
}