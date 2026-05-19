package com.arogya.sahaya

import android.app.Application
import com.arogya.sahaya.data.*
import com.arogya.sahaya.remoteconfig.RemoteConfigManager

class ArogyaApplication : Application() {

    lateinit var userProfileRepository: UserProfileRepository
    lateinit var medicineRepository: MedicineRepository
    lateinit var vitalRepository: VitalRepository
    lateinit var ashaRepository: AshaRepository
    lateinit var doseHistoryRepository: DoseHistoryRepository

    override fun onCreate() {
        super.onCreate()
        val database = ArogyaDatabase.get(this)
        
        userProfileRepository = UserProfileRepository(database.userProfileDao())
        medicineRepository = MedicineRepository(database.medicineDao())
        vitalRepository = VitalRepository(database.vitalLogDao())
        ashaRepository = AshaRepository(database.ashaEventDao())
        doseHistoryRepository = DoseHistoryRepository(database.doseHistoryDao())

        // Initialize Remote Config
        RemoteConfigManager.init()
    }
}
