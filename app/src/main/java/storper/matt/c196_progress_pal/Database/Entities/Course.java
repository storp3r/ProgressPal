package storper.matt.c196_progress_pal.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import storper.matt.c196_progress_pal.Database.Entities.Term;

@Entity(tableName = "courses", foreignKeys = @ForeignKey(entity = Term.class,
parentColumns = "termId",
childColumns = "termId", onDelete = ForeignKey.RESTRICT))
public class Course {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "courseId")
    private int mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "startDate")
    public String mStartDate;

    @NonNull
    @ColumnInfo(name = "endDate")
    public String mEndDate;

    @ColumnInfo(name = "status")
    public String mStatus;

    @NonNull
    @ColumnInfo(name = "termId")
    public int mTermId;



    public Course() {

    }

    public Course(String name, String startDate, String endDate, String status, int termId) {
        setName(name);
        setStartDate(startDate);
        setEndDate(endDate);
        setStatus(status);
        setTermId(termId);
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        this.mName = name;
    }

    @NonNull
    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(@NonNull String startDate) {
        this.mStartDate = startDate;
    }

    @NonNull
    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(@NonNull String endDate) {
        this.mEndDate = endDate;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public int getTermId() {
        return mTermId;
    }

    public void setTermId(int termId) {
        this.mTermId = termId;
    }




}
