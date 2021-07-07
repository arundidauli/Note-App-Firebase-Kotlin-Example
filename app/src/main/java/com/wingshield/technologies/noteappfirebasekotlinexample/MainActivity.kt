package com.wingshield.technologies.noteappfirebasekotlinexample

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.viewbinding.library.activity.viewBinding
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.wingshield.technologies.noteappfirebasekotlinexample.databinding.ActivityMainBinding
import com.wingshield.technologies.noteappfirebasekotlinexample.model.Note
import com.wingshield.technologies.noteappfirebasekotlinexample.utils.Const
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Arun Android 07/08/2021
 */
class MainActivity : AppCompatActivity(), ItemLongPressInterface {
    private val binding: ActivityMainBinding by viewBinding()
    private var TAG = MainActivity::class.java.simpleName
    private lateinit var databaseReference: DatabaseReference
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var androidID: String
    private lateinit var noteList:ArrayList<Note>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseReference = FirebaseDatabase.getInstance().getReference(Const.USER_NOTE)
        androidID = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        noteList= arrayListOf()
        val linearLayoutManager=LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.setHasFixedSize(true);

        noteAdapter = NoteAdapter( this)
        if (isNetworkAvailable(this)){
            fetchNote()
        }else{
            showError("No Internet connection")
        }

        binding.fab.setOnClickListener{
            showNoteDialog(false,null)
        }


    }

    private fun fetchNote() {
        binding.progressCircular.isVisible=true
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                noteList.clear()
                for (ds in p0.children) {
                    Log.e(TAG, ds.child("note").getValue(String::class.java).toString())
                    val note = ds.getValue(Note::class.java)
                    noteList.add(note!!)

                }
                binding.progressCircular.isVisible=false
                if (noteList.isEmpty()){
                    binding.txtEmptyNotesView.isVisible=true
                }else{

                    binding.txtEmptyNotesView.isVisible=false
                    noteAdapter.setNote(noteList)
                    binding.recyclerView.adapter=noteAdapter
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                binding.progressCircular.isVisible=false
                Log.e(TAG, p0.message)
            }
        }
        databaseReference.child(androidID).addListenerForSingleValueEvent(valueEventListener)
    }

    private fun showError(message: String) {
        val snackBar = Snackbar
            .make(binding.coordinatorLayout, message, Snackbar.LENGTH_LONG)
        val sbView = snackBar.view
        val textView = sbView.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(Color.YELLOW)
        snackBar.show()
    }


    private fun showActionsDialog(note: Note) {
        val colors = arrayOf<CharSequence>("Edit", "Delete")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose option")
        builder.setItems(colors) { _, which ->
            if (which == 0) {
                showNoteDialog(true, note)
            } else {
                deleteNote(note.id)
            }
        }
        builder.show()
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private fun showNoteDialog(shouldUpdate: Boolean, note: Note?) {
        val layoutInflaterAndroid = LayoutInflater.from(this@MainActivity)
        val view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null)
        val alertDialogBuilderUserInput = AlertDialog.Builder(this@MainActivity)
        alertDialogBuilderUserInput.setView(view)
        val inputNote = view.findViewById<EditText>(R.id.note)
        val dialogTitle = view.findViewById<TextView>(R.id.dialog_title)
        dialogTitle.setText(if (!shouldUpdate) (R.string.lbl_new_note_title) else (R.string.update_note))
        if (shouldUpdate && note != null) {
            inputNote.setText(note.note)
        }
        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(
                if (shouldUpdate) "update" else "save"
            ) { _, _ -> }
            .setNegativeButton(
                "cancel"
            ) { dialogBox, _ -> dialogBox.cancel() }
        val alertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
            View.OnClickListener {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.text.toString())) {
                    Toast.makeText(this@MainActivity, "Enter note!", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                } else {
                    alertDialog.dismiss()
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // update note by it's id
                    val hashMap = HashMap<String, String>()
                    hashMap["note"] = inputNote.text.toString()
                    updateNote(note.id, hashMap)
                } else {
                    // create new note
                    createNote(inputNote.text.toString())
                }
            })
    }

    private fun updateNote(id: String, hashMap: HashMap<String, String>) {
        binding.progressCircular.isVisible=true
        databaseReference.child(androidID).child(id).updateChildren(hashMap as Map<String, Any>, DatabaseReference.CompletionListener {
                databaseError, _ ->
            if (databaseError==null){
                Toast.makeText(applicationContext, "Note updated", Toast.LENGTH_SHORT).show()
                fetchNote()
            }else{
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()

            }

        })
    }

    private fun createNote(note: String) {
        binding.progressCircular.isVisible=true
        val id: String = databaseReference.push().key.toString()
        val demoNote = Note(id, note, System.currentTimeMillis().toString())
        databaseReference.child(androidID).child(id).setValue(demoNote).addOnCompleteListener {
            Toast.makeText(this,"Created",Toast.LENGTH_SHORT).show()
            fetchNote()
        }

    }

    private fun deleteNote(id: String) {
        binding.progressCircular.isVisible=true
        databaseReference.child(androidID).child(id).removeValue().addOnCompleteListener {
            Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show()
            fetchNote()
        }
    }

    override fun onLongPress(note: Note) {
        showActionsDialog(note)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }
}