package com.arogya.sahaya.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MedicineDao_Impl implements MedicineDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Medicine> __insertionAdapterOfMedicine;

  private final EntityDeletionOrUpdateAdapter<Medicine> __deletionAdapterOfMedicine;

  public MedicineDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMedicine = new EntityInsertionAdapter<Medicine>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `medicines` (`id`,`name`,`dosage`,`morning`,`morningTime`,`afternoon`,`afternoonTime`,`night`,`nightTime`,`notes`,`isActive`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Medicine entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDosage());
        final int _tmp = entity.getMorning() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getMorningTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getMorningTime());
        }
        final int _tmp_1 = entity.getAfternoon() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        if (entity.getAfternoonTime() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAfternoonTime());
        }
        final int _tmp_2 = entity.getNight() ? 1 : 0;
        statement.bindLong(8, _tmp_2);
        if (entity.getNightTime() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNightTime());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getNotes());
        }
        final int _tmp_3 = entity.isActive() ? 1 : 0;
        statement.bindLong(11, _tmp_3);
        statement.bindLong(12, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfMedicine = new EntityDeletionOrUpdateAdapter<Medicine>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `medicines` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Medicine entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final Medicine medicine, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMedicine.insertAndReturnId(medicine);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Medicine medicine, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMedicine.handle(medicine);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Medicine>> observeActive() {
    final String _sql = "SELECT * FROM medicines WHERE isActive = 1 ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"medicines"}, new Callable<List<Medicine>>() {
      @Override
      @NonNull
      public List<Medicine> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfMorning = CursorUtil.getColumnIndexOrThrow(_cursor, "morning");
          final int _cursorIndexOfMorningTime = CursorUtil.getColumnIndexOrThrow(_cursor, "morningTime");
          final int _cursorIndexOfAfternoon = CursorUtil.getColumnIndexOrThrow(_cursor, "afternoon");
          final int _cursorIndexOfAfternoonTime = CursorUtil.getColumnIndexOrThrow(_cursor, "afternoonTime");
          final int _cursorIndexOfNight = CursorUtil.getColumnIndexOrThrow(_cursor, "night");
          final int _cursorIndexOfNightTime = CursorUtil.getColumnIndexOrThrow(_cursor, "nightTime");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Medicine> _result = new ArrayList<Medicine>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Medicine _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDosage;
            _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            final boolean _tmpMorning;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfMorning);
            _tmpMorning = _tmp != 0;
            final String _tmpMorningTime;
            if (_cursor.isNull(_cursorIndexOfMorningTime)) {
              _tmpMorningTime = null;
            } else {
              _tmpMorningTime = _cursor.getString(_cursorIndexOfMorningTime);
            }
            final boolean _tmpAfternoon;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAfternoon);
            _tmpAfternoon = _tmp_1 != 0;
            final String _tmpAfternoonTime;
            if (_cursor.isNull(_cursorIndexOfAfternoonTime)) {
              _tmpAfternoonTime = null;
            } else {
              _tmpAfternoonTime = _cursor.getString(_cursorIndexOfAfternoonTime);
            }
            final boolean _tmpNight;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfNight);
            _tmpNight = _tmp_2 != 0;
            final String _tmpNightTime;
            if (_cursor.isNull(_cursorIndexOfNightTime)) {
              _tmpNightTime = null;
            } else {
              _tmpNightTime = _cursor.getString(_cursorIndexOfNightTime);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final boolean _tmpIsActive;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_3 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Medicine(_tmpId,_tmpName,_tmpDosage,_tmpMorning,_tmpMorningTime,_tmpAfternoon,_tmpAfternoonTime,_tmpNight,_tmpNightTime,_tmpNotes,_tmpIsActive,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getById(final int id, final Continuation<? super Medicine> $completion) {
    final String _sql = "SELECT * FROM medicines WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Medicine>() {
      @Override
      @Nullable
      public Medicine call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfMorning = CursorUtil.getColumnIndexOrThrow(_cursor, "morning");
          final int _cursorIndexOfMorningTime = CursorUtil.getColumnIndexOrThrow(_cursor, "morningTime");
          final int _cursorIndexOfAfternoon = CursorUtil.getColumnIndexOrThrow(_cursor, "afternoon");
          final int _cursorIndexOfAfternoonTime = CursorUtil.getColumnIndexOrThrow(_cursor, "afternoonTime");
          final int _cursorIndexOfNight = CursorUtil.getColumnIndexOrThrow(_cursor, "night");
          final int _cursorIndexOfNightTime = CursorUtil.getColumnIndexOrThrow(_cursor, "nightTime");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final Medicine _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDosage;
            _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            final boolean _tmpMorning;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfMorning);
            _tmpMorning = _tmp != 0;
            final String _tmpMorningTime;
            if (_cursor.isNull(_cursorIndexOfMorningTime)) {
              _tmpMorningTime = null;
            } else {
              _tmpMorningTime = _cursor.getString(_cursorIndexOfMorningTime);
            }
            final boolean _tmpAfternoon;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfAfternoon);
            _tmpAfternoon = _tmp_1 != 0;
            final String _tmpAfternoonTime;
            if (_cursor.isNull(_cursorIndexOfAfternoonTime)) {
              _tmpAfternoonTime = null;
            } else {
              _tmpAfternoonTime = _cursor.getString(_cursorIndexOfAfternoonTime);
            }
            final boolean _tmpNight;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfNight);
            _tmpNight = _tmp_2 != 0;
            final String _tmpNightTime;
            if (_cursor.isNull(_cursorIndexOfNightTime)) {
              _tmpNightTime = null;
            } else {
              _tmpNightTime = _cursor.getString(_cursorIndexOfNightTime);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final boolean _tmpIsActive;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_3 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new Medicine(_tmpId,_tmpName,_tmpDosage,_tmpMorning,_tmpMorningTime,_tmpAfternoon,_tmpAfternoonTime,_tmpNight,_tmpNightTime,_tmpNotes,_tmpIsActive,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
