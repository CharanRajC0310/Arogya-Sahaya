package com.arogya.sahaya.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ArogyaDatabase_Impl extends ArogyaDatabase {
  private volatile UserProfileDao _userProfileDao;

  private volatile MedicineDao _medicineDao;

  private volatile DoseHistoryDao _doseHistoryDao;

  private volatile VitalLogDao _vitalLogDao;

  private volatile AshaEventDao _ashaEventDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `age` INTEGER NOT NULL, `chronicConditions` TEXT NOT NULL, `caregiverName` TEXT, `caregiverNumber` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `medicines` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `dosage` TEXT NOT NULL, `morning` INTEGER NOT NULL, `morningTime` TEXT, `afternoon` INTEGER NOT NULL, `afternoonTime` TEXT, `night` INTEGER NOT NULL, `nightTime` TEXT, `notes` TEXT, `isActive` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `dose_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `medicineId` INTEGER NOT NULL, `medicineName` TEXT NOT NULL, `session` TEXT NOT NULL, `scheduledTime` INTEGER NOT NULL, `actualTime` INTEGER, `status` TEXT NOT NULL, `date` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `vital_log` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `systolic` INTEGER NOT NULL, `diastolic` INTEGER NOT NULL, `heartRate` INTEGER NOT NULL, `glucose` REAL, `notes` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `asha_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `type` TEXT NOT NULL, `date` TEXT NOT NULL, `startTime` TEXT NOT NULL, `endTime` TEXT NOT NULL, `location` TEXT NOT NULL, `description` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7f50a56c2cb1dfeda6c8a2b61d2a4817')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `user_profile`");
        db.execSQL("DROP TABLE IF EXISTS `medicines`");
        db.execSQL("DROP TABLE IF EXISTS `dose_history`");
        db.execSQL("DROP TABLE IF EXISTS `vital_log`");
        db.execSQL("DROP TABLE IF EXISTS `asha_events`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUserProfile = new HashMap<String, TableInfo.Column>(7);
        _columnsUserProfile.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("age", new TableInfo.Column("age", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("chronicConditions", new TableInfo.Column("chronicConditions", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("caregiverName", new TableInfo.Column("caregiverName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("caregiverNumber", new TableInfo.Column("caregiverNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserProfile = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserProfile = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserProfile = new TableInfo("user_profile", _columnsUserProfile, _foreignKeysUserProfile, _indicesUserProfile);
        final TableInfo _existingUserProfile = TableInfo.read(db, "user_profile");
        if (!_infoUserProfile.equals(_existingUserProfile)) {
          return new RoomOpenHelper.ValidationResult(false, "user_profile(com.arogya.sahaya.data.UserProfile).\n"
                  + " Expected:\n" + _infoUserProfile + "\n"
                  + " Found:\n" + _existingUserProfile);
        }
        final HashMap<String, TableInfo.Column> _columnsMedicines = new HashMap<String, TableInfo.Column>(12);
        _columnsMedicines.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicines.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicines.put("dosage", new TableInfo.Column("dosage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicines.put("morning", new TableInfo.Column("morning", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicines.put("morningTime", new TableInfo.Column("morningTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicines.put("afternoon", new TableInfo.Column("afternoon", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicines.put("afternoonTime", new TableInfo.Column("afternoonTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicines.put("night", new TableInfo.Column("night", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicines.put("nightTime", new TableInfo.Column("nightTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicines.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicines.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedicines.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMedicines = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMedicines = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMedicines = new TableInfo("medicines", _columnsMedicines, _foreignKeysMedicines, _indicesMedicines);
        final TableInfo _existingMedicines = TableInfo.read(db, "medicines");
        if (!_infoMedicines.equals(_existingMedicines)) {
          return new RoomOpenHelper.ValidationResult(false, "medicines(com.arogya.sahaya.data.Medicine).\n"
                  + " Expected:\n" + _infoMedicines + "\n"
                  + " Found:\n" + _existingMedicines);
        }
        final HashMap<String, TableInfo.Column> _columnsDoseHistory = new HashMap<String, TableInfo.Column>(8);
        _columnsDoseHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDoseHistory.put("medicineId", new TableInfo.Column("medicineId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDoseHistory.put("medicineName", new TableInfo.Column("medicineName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDoseHistory.put("session", new TableInfo.Column("session", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDoseHistory.put("scheduledTime", new TableInfo.Column("scheduledTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDoseHistory.put("actualTime", new TableInfo.Column("actualTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDoseHistory.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDoseHistory.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDoseHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDoseHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDoseHistory = new TableInfo("dose_history", _columnsDoseHistory, _foreignKeysDoseHistory, _indicesDoseHistory);
        final TableInfo _existingDoseHistory = TableInfo.read(db, "dose_history");
        if (!_infoDoseHistory.equals(_existingDoseHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "dose_history(com.arogya.sahaya.data.DoseHistory).\n"
                  + " Expected:\n" + _infoDoseHistory + "\n"
                  + " Found:\n" + _existingDoseHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsVitalLog = new HashMap<String, TableInfo.Column>(8);
        _columnsVitalLog.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLog.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLog.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLog.put("systolic", new TableInfo.Column("systolic", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLog.put("diastolic", new TableInfo.Column("diastolic", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLog.put("heartRate", new TableInfo.Column("heartRate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLog.put("glucose", new TableInfo.Column("glucose", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLog.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVitalLog = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVitalLog = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVitalLog = new TableInfo("vital_log", _columnsVitalLog, _foreignKeysVitalLog, _indicesVitalLog);
        final TableInfo _existingVitalLog = TableInfo.read(db, "vital_log");
        if (!_infoVitalLog.equals(_existingVitalLog)) {
          return new RoomOpenHelper.ValidationResult(false, "vital_log(com.arogya.sahaya.data.VitalLog).\n"
                  + " Expected:\n" + _infoVitalLog + "\n"
                  + " Found:\n" + _existingVitalLog);
        }
        final HashMap<String, TableInfo.Column> _columnsAshaEvents = new HashMap<String, TableInfo.Column>(8);
        _columnsAshaEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAshaEvents.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAshaEvents.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAshaEvents.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAshaEvents.put("startTime", new TableInfo.Column("startTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAshaEvents.put("endTime", new TableInfo.Column("endTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAshaEvents.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAshaEvents.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAshaEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAshaEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAshaEvents = new TableInfo("asha_events", _columnsAshaEvents, _foreignKeysAshaEvents, _indicesAshaEvents);
        final TableInfo _existingAshaEvents = TableInfo.read(db, "asha_events");
        if (!_infoAshaEvents.equals(_existingAshaEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "asha_events(com.arogya.sahaya.data.AshaEvent).\n"
                  + " Expected:\n" + _infoAshaEvents + "\n"
                  + " Found:\n" + _existingAshaEvents);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "7f50a56c2cb1dfeda6c8a2b61d2a4817", "83c8811795f780107eff06d0f6c1b9da");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "user_profile","medicines","dose_history","vital_log","asha_events");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `user_profile`");
      _db.execSQL("DELETE FROM `medicines`");
      _db.execSQL("DELETE FROM `dose_history`");
      _db.execSQL("DELETE FROM `vital_log`");
      _db.execSQL("DELETE FROM `asha_events`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserProfileDao.class, UserProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MedicineDao.class, MedicineDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DoseHistoryDao.class, DoseHistoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VitalLogDao.class, VitalLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AshaEventDao.class, AshaEventDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserProfileDao userProfileDao() {
    if (_userProfileDao != null) {
      return _userProfileDao;
    } else {
      synchronized(this) {
        if(_userProfileDao == null) {
          _userProfileDao = new UserProfileDao_Impl(this);
        }
        return _userProfileDao;
      }
    }
  }

  @Override
  public MedicineDao medicineDao() {
    if (_medicineDao != null) {
      return _medicineDao;
    } else {
      synchronized(this) {
        if(_medicineDao == null) {
          _medicineDao = new MedicineDao_Impl(this);
        }
        return _medicineDao;
      }
    }
  }

  @Override
  public DoseHistoryDao doseHistoryDao() {
    if (_doseHistoryDao != null) {
      return _doseHistoryDao;
    } else {
      synchronized(this) {
        if(_doseHistoryDao == null) {
          _doseHistoryDao = new DoseHistoryDao_Impl(this);
        }
        return _doseHistoryDao;
      }
    }
  }

  @Override
  public VitalLogDao vitalLogDao() {
    if (_vitalLogDao != null) {
      return _vitalLogDao;
    } else {
      synchronized(this) {
        if(_vitalLogDao == null) {
          _vitalLogDao = new VitalLogDao_Impl(this);
        }
        return _vitalLogDao;
      }
    }
  }

  @Override
  public AshaEventDao ashaEventDao() {
    if (_ashaEventDao != null) {
      return _ashaEventDao;
    } else {
      synchronized(this) {
        if(_ashaEventDao == null) {
          _ashaEventDao = new AshaEventDao_Impl(this);
        }
        return _ashaEventDao;
      }
    }
  }
}
