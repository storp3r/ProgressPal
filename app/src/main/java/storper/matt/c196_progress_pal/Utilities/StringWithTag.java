package storper.matt.c196_progress_pal.Utilities;

public class StringWithTag {
    public String mStringName;
    public Object mTag;
    public Object mStartDate;
    public Object mEndDate;

    public StringWithTag(String name, Object tag) {
        this.mStringName = name;
        this.mTag = tag;
    }

    public StringWithTag(String name, Object tag, Object startDate, Object endDate) {
        this.mStringName = name;
        this.mTag = tag;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
    }

    @Override
    public String toString() {
        return mStringName;
    }

}
