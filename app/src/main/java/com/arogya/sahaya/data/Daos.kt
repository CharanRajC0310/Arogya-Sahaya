package com.arogya.sahaya.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observeProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getProfile(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfile)
}

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines WHERE isActive = 1 ORDER BY createdAt DESC")
    fun observeActive(): Flow<List<Medicine>>

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getById(id: Int): Medicine?

    @Insert
    suspend fun insert(medicine: Medicine): Long

    @Delete
    suspend fun delete(medicine: Medicine)
}

@Dao
interface DoseHistoryDao {
    @Query("SELECT * FROM dose_history ORDER BY scheduledTime DESC")
    fun observeAll(): Flow<List<DoseHistory>>

    @Insert
    suspend fun insert(history: DoseHistory)
}

@Dao
interface VitalLogDao {
    @Query("SELECT * FROM vital_log ORDER BY timestamp DESC LIMIT 7")
    fun observeLast7(): Flow<List<VitalLog>>

    @Query("SELECT * FROM vital_log ORDER BY timestamp DESC LIMIT 1")
    fun observeLatest(): Flow<VitalLog?>

    @Insert
    suspend fun insert(vitalLog: VitalLog)
}

@Dao
interface AshaEventDao {
    @Query("SELECT * FROM asha_events ORDER BY date ASC")
    fun observeAll(): Flow<List<AshaEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<AshaEvent>)
}
