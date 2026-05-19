package com.arogya.sahaya.reminder

import android.content.Context
import androidx.work.*
import com.arogya.sahaya.data.Medicine
import java.util.concurrent.TimeUnit
import java.time.LocalTime
import java.time.Duration
import java.time.LocalDateTime

class ReminderManager(private val context: Context) {

    fun scheduleMedicineReminders(medicine: Medicine) {
        if (medicine.morning) {
            scheduleSession(medicine, "Morning", medicine.morningTime ?: "08:00")
        }
        if (medicine.afternoon) {
            scheduleSession(medicine, "Afternoon", medicine.afternoonTime ?: "14:00")
        }
        if (medicine.night) {
            scheduleSession(medicine, "Night", medicine.nightTime ?: "20:00")
        }
    }

    private fun scheduleSession(medicine: Medicine, session: String, timeStr: String) {
        val time = LocalTime.parse(timeStr)
        val now = LocalDateTime.now()
        var scheduledDateTime = now.with(time)
        
        if (scheduledDateTime.isBefore(now)) {
            scheduledDateTime = scheduledDateTime.plusDays(1)
        }

        val initialDelay = Duration.between(now, scheduledDateTime).toMinutes()

        val data = workDataOf(
            "medicineId" to medicine.id,
            "medicineName" to medicine.name,
            "session" to session,
            "dosage" to medicine.dosage
        )

        val workRequest = PeriodicWorkRequestBuilder<MedicineReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MINUTES)
            .setInputData(data)
            .addTag("med_${medicine.id}_$session")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "med_${medicine.id}_$session",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    fun cancelReminders(medicineId: Int) {
        WorkManager.getInstance(context).cancelAllWorkByTag("med_$medicineId")
    }
}
