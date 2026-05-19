package com.arogya.sahaya.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
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
public final class DoseHistoryDao_Impl implements DoseHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DoseHistory> __insertionAdapterOfDoseHistory;

  public DoseHistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDoseHistory = new EntityInsertionAdapter<DoseHistory>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `dose_history` (`id`,`medicineId`,`medicineName`,`session`,`scheduledTime`,`actualTime`,`status`,`date`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DoseHistory entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMedicineId());
        statement.bindString(3, entity.getMedicineName());
        statement.bindString(4, entity.getSession());
        statement.bindLong(5, entity.getScheduledTime());
        if (entity.getActualTime() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getActualTime());
        }
        statement.bindString(7, entity.getStatus());
        statement.bindString(8, entity.getDate());
      }
    };
  }

  @Override
  public Object insert(final DoseHistory history, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDoseHistory.insert(history);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DoseHistory>> observeAll() {
    final String _sql = "SELECT * FROM dose_history ORDER BY scheduledTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"dose_history"}, new Callable<List<DoseHistory>>() {
      @Override
      @NonNull
      public List<DoseHistory> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMedicineId = CursorUtil.getColumnIndexOrThrow(_cursor, "medicineId");
          final int _cursorIndexOfMedicineName = CursorUtil.getColumnIndexOrThrow(_cursor, "medicineName");
          final int _cursorIndexOfSession = CursorUtil.getColumnIndexOrThrow(_cursor, "session");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfActualTime = CursorUtil.getColumnIndexOrThrow(_cursor, "actualTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final List<DoseHistory> _result = new ArrayList<DoseHistory>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DoseHistory _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpMedicineId;
            _tmpMedicineId = _cursor.getInt(_cursorIndexOfMedicineId);
            final String _tmpMedicineName;
            _tmpMedicineName = _cursor.getString(_cursorIndexOfMedicineName);
            final String _tmpSession;
            _tmpSession = _cursor.getString(_cursorIndexOfSession);
            final long _tmpScheduledTime;
            _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
            final Long _tmpActualTime;
            if (_cursor.isNull(_cursorIndexOfActualTime)) {
              _tmpActualTime = null;
            } else {
              _tmpActualTime = _cursor.getLong(_cursorIndexOfActualTime);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            _item = new DoseHistory(_tmpId,_tmpMedicineId,_tmpMedicineName,_tmpSession,_tmpScheduledTime,_tmpActualTime,_tmpStatus,_tmpDate);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
