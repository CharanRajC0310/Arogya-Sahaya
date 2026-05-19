package com.arogya.sahaya.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val age: Int,
    val chronicConditions: String,
    val caregiverName: String? = null,
    val caregiverNumber: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dosage: String,
    val morning: Boolean,
    val morningTime: String?,
    val afternoon: Boolean,
    val afternoonTime: String?,
    val night: Boolean,
    val nightTime: String?,
    val notes: String?,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "dose_history")
data class DoseHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medicineId: Int,
    val medicineName: String,
    val session: String,
    val scheduledTime: Long,
    val actualTime: Long?,
    val status: String,
    val date: String
)

@Entity(tableName = "vital_log")
data class VitalLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val timestamp: Long = System.currentTimeMillis(),
    val systolic: Int,
    val diastolic: Int,
    val heartRate: Int,
    val glucose: Float? = null,
    val notes: String? = null
)

@Entity(tableName = "asha_events")
data class AshaEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val type: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val location: String,
    val description: String? = null
)
