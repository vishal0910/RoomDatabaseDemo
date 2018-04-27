package android.roomdatabasedemo.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.roomdatabasedemo.database.entity.NameEntity;

import java.util.List;


@Dao
public interface DaoAccess {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRecord(NameEntity nameEntity);

    @Update
    int updaterecord(NameEntity nameEntity);

    @Delete
    int deleteRecord(NameEntity nameEntity);

    @Query("SELECT * FROM user ORDER BY id DESC")
    List<NameEntity> fetchAllRecords();


   /* @Query("CREATE TRIGGER IF NOT EXISTS delete_till_10 INSERT ON user WHEN (select count(*) from user)>9 BEGIN DELETE FROM user WHERE id IN  (SELECT id FROM user ORDER BY id limit (select count(*) -9 from user)); END")
    public void triggerForLimit();*/
}
