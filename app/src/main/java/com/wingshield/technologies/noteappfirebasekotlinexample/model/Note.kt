package com.wingshield.technologies.noteappfirebasekotlinexample.model

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp
@IgnoreExtraProperties
data class Note(
    val id:String="",
    val note:String="",
    val timestamp:String=""
)