package com.example.myapplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class SharedViewModel : ViewModel() {
    val scheduledSessions: MutableState<List<ScheduledSession>> = mutableStateOf(emptyList())
    val completedSessions: MutableState<List<ScheduledSession>> = mutableStateOf(emptyList())

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCompletedSessions() {
        val currentDate = LocalDate.now()
        val completed = scheduledSessions.value.filter { scheduledSession ->
            val sessionDate = LocalDate.parse(scheduledSession.date)
            sessionDate.isBefore(currentDate)
        }
        completedSessions.value = completed
    }
}



