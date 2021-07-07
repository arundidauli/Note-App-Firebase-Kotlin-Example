package com.wingshield.technologies.noteappfirebasekotlinexample

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wingshield.technologies.noteappfirebasekotlinexample.databinding.NoteListRowBinding
import com.wingshield.technologies.noteappfirebasekotlinexample.model.Note
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NoteAdapter(

    private var longPressInterface: ItemLongPressInterface
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var noteList: ArrayList<Note> = arrayListOf()
    var mColors = arrayOf(
        "BF55EC",
        "19B5FE",
        "2ABB9B",
        "F4D03F",
        "95A5A6",
        "DB5A6B",
        "2ABB9B",
        "5E97F6",
        "9CCC65",
        "4DD0E1",
        "A1887F"
    )

    private lateinit var binding: NoteListRowBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = NoteListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    fun setNote(noteList1: ArrayList<Note>) {
        noteList.clear()
        noteList.addAll(noteList1)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    class ViewHolder(binding: NoteListRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val r = Random()
        val i1 = r.nextInt(11 - 0) + 0
        val color = "#FF" + mColors[i1]
        binding.rlRoot.setBackgroundColor(Color.parseColor(color))
        with(noteList[position]) {
            binding.note.text = note
            binding.timestamp.text = convertLongToTime(timestamp.toLong())

            binding.rlRoot.setOnLongClickListener{
                longPressInterface.onLongPress(noteList[position])
                true
            }
        }
    }
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyy HH:mm:a", Locale.getDefault())
        return format.format(date)
    }

}
