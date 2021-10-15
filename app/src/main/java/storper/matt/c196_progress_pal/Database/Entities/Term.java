package storper.matt.c196_progress_pal.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "termId")
    private int mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "startDate")
    public String mStartDate;

    @ColumnInfo(name = "endDate")
    private String mEndDate;

    public Term() {

    }

    public Term(String name, String startDate, String endDate) {
        this.setName(name);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    public Term(String name) {
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        this.mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }


}
