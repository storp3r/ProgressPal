package storper.matt.c196_progress_pal.Database.Entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructors",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "courseId",
                childColumns = "courseId", onDelete = ForeignKey.CASCADE))

public class Instructor {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "instructorId")
    private int mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "phone")
    private long mPhone;

    @ColumnInfo(name = "email")
    private String mEmail;

    @ColumnInfo(name = "courseId")
    private int mCourseId;

    public Instructor(@NonNull String name, long phone, String email) {
        setName(name);
        setPhone(phone);
        setEmail(email);
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

    public long getPhone() {
        return mPhone;
    }

    public void setPhone(long mPhone) {
        this.mPhone = mPhone;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public int getCourseId() {
        return mCourseId;
    }

    public void setCourseId(int courseId) {
        this.mCourseId = courseId;
    }
}
