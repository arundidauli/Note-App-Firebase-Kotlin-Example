package com.wingshield.technologies.noteappfirebasekotlinexample.repository

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.wingshield.technologies.noteappfirebasekotlinexample.utils.Const

open class FireBaseRepository: AppCompatActivity() {

    protected fun fireBaseCreate(
        databaseReference: DatabaseReference,
        model: Any?,
        callback: CallBack
    ) {
        databaseReference.keepSynced(true)
        databaseReference.setValue(
            model
        ) { databaseError, _ ->
            if (databaseError == null) {
                callback.onSuccess(Const.SUCCESS)
            } else {
                callback.onError(databaseError)
            }
        }
    }

    protected fun fireBaseUpdateChildren(
        databaseReference: DatabaseReference,
        map: Map<String, String>?,
        callback: CallBack
    ) {
        databaseReference.keepSynced(true)
        if (map != null) {
            databaseReference.updateChildren(map
            ) { databaseError, _ ->
                if (databaseError == null) {
                    callback.onSuccess(null)
                } else {
                    callback.onError(databaseError)
                }
            }
        }
    }
    protected fun fireBaseDelete(databaseReference: DatabaseReference, callback: CallBack) {
        databaseReference.keepSynced(true)
        databaseReference.removeValue { databaseError, _ ->
            if (databaseError == null) {
                callback.onSuccess(null)
            } else {
                callback.onError(databaseError)
            }
        }
    }


    protected fun fireBaseReadData(query: Query, callback: CallBack) {
        query.keepSynced(true)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback.onSuccess(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onError(databaseError)
            }
        })
    }

}