package com.arogya.sahaya.data

import kotlinx.coroutines.flow.Flow

class UserProfileRepository(private val dao: UserProfileDao) {
    fun observeProfile(): Flow<UserProfile?> = dao.observeProfile()
    suspend fun save(profile: UserProfile) = dao.upsert(profile)
}

class MedicineRepository(private val dao: MedicineDao) {
    fun observeActive(): Flow<List<Medicine>> = dao.observeActive()
    suspend fun insert(medicine: Medicine): Int = dao.insert(medicine).toInt()
    suspend fun delete(medicine: Medicine) = dao.delete(medicine)
}

class VitalRepository(private val dao: VitalLogDao) {
    fun observeLatest(): Flow<VitalLog?> = dao.observeLatest()
    fun observeLast7(): Flow<List<VitalLog>> = dao.observeLast7()
    suspend fun insert(log: VitalLog) = dao.insert(log)
}

class AshaRepository(private val dao: AshaEventDao) {
    fun observeAll(): Flow<List<AshaEvent>> = dao.observeAll()
    suspend fun insertAll(events: List<AshaEvent>) = dao.insertAll(events)
}

class DoseHistoryRepository(private val dao: DoseHistoryDao) {
    fun observeAll(): Flow<List<DoseHistory>> = dao.observeAll()
    suspend fun insert(history: DoseHistory) = dao.insert(history)
}
