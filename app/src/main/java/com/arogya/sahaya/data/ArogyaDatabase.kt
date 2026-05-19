package com.arogya.sahaya.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Database(
    entities = [UserProfile::class, Medicine::class, DoseHistory::class, VitalLog::class, AshaEvent::class],
    version = 1,
    exportSchema = false
)
abstract class ArogyaDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun medicineDao(): MedicineDao
    abstract fun doseHistoryDao(): DoseHistoryDao
    abstract fun vitalLogDao(): VitalLogDao
    abstract fun ashaEventDao(): AshaEventDao

    companion object {
        @Volatile
        private var instance: ArogyaDatabase? = null

        fun get(context: Context): ArogyaDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ArogyaDatabase::class.java,
                    "arogya_sahaya.db"
                ).addCallback(seedCallback(context.applicationContext))
                    .build()
                    .also { instance = it }
            }

        private fun seedCallback(context: Context) = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val today = LocalDate.now()
                    get(context).ashaEventDao().insertAll(
                        listOf(
                            AshaEvent(title = "General Health Camp", type = "CAMP", date = today.plusDays(3).toString(), startTime = "09:00", endTime = "13:00", location = "Village Community Hall"),
                            AshaEvent(title = "Diabetes Screening", type = "CAMP", date = today.plusDays(11).toString(), startTime = "10:00", endTime = "14:00", location = "Panchayat Bhavan"),
                            AshaEvent(title = "ASHA Home Visit", type = "VISIT", date = today.plusDays(18).toString(), startTime = "11:00", endTime = "12:00", location = "Home"),
                            AshaEvent(title = "Blood Pressure Check", type = "CAMP", date = today.plusDays(27).toString(), startTime = "09:30", endTime = "12:30", location = "Primary Health Centre"),
                            AshaEvent(title = "Eye Checkup Camp", type = "CAMP", date = today.plusDays(41).toString(), startTime = "09:00", endTime = "15:00", location = "School Auditorium"),
                            AshaEvent(title = "Vaccination Follow-up", type = "VISIT", date = today.plusDays(55).toString(), startTime = "10:30", endTime = "11:30", location = "Home")
                        )
                    )
                }
            }
        }
    }
}
