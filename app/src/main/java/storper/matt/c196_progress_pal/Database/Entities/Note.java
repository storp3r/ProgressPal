package storper.matt.c196_progress_pal.Database.Entities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "note",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "courseId",
                childColumns = "courseId", onDelete = ForeignKey.CASCADE))

public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteId")
    private int mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "details")
    private String mDetails;

    @ColumnInfo(name = "createdOn")
    private String mCreatedOn;

    @NonNull
    @ColumnInfo(name = "courseId")
    private int mCourseId;

    public Note(@NonNull String name, String details, String createdOn, int courseId) {
        setName(name);
        setDetails(details);
        setCreatedOn(createdOn);
        setCourseId(courseId);
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

    public void setName(@NonNull String mName) {
        this.mName = mName;
    }

    @NonNull
    public String getDetails() {
        return mDetails;
    }

    public void setDetails(@NonNull String details) {
        this.mDetails = details;
    }

    public String getCreatedOn() {
        return mCreatedOn;
    }

    public void setCreatedOn(String mCreatedOn) {
        this.mCreatedOn = mCreatedOn;
    }

    public int getCourseId() {
        return mCourseId;
    }

    public void setCourseId(int mCourseId) {
        this.mCourseId = mCourseId;
    }
}
