package com.example.todoapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.data.model.Priority
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "TODO_TABLE")
@Parcelize
data class ToDoData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var priority: Priority,
    var description: String
) : Parcelable
