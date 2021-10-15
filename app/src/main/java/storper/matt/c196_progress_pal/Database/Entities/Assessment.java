package storper.matt.c196_progress_pal.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments",
foreignKeys = @ForeignKey(entity = Course.class,
parentColumns = "courseId",
childColumns = "courseId", onDelete = ForeignKey.RESTRICT))

public class Assessment {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "assessmentId")
    private int mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "type")
    private String mType;

    @NonNull
    @ColumnInfo(name = "dueDate")
    private String dueDate;

    @NonNull
    @ColumnInfo(name = "courseId")
        public int mCourseId;

    public Assessment() {

    }

    public Assessment(String name, String type, String dueDate, int courseId) {
        setName(name);
        setType(type);
        setDueDate(dueDate);
        setCourseId(courseId);
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

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    @NonNull
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(@NonNull String dueDate) {
        this.dueDate = dueDate;
    }

    public int getCourseId() {
        return mCourseId;
    }

    public void setCourseId(int courseId) {
        this.mCourseId = courseId;
    }
}
