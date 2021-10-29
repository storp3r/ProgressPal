package storper.matt.c196_progress_pal.Utilities;

public class Transaction {
    public enum Status {SUCCESS, FAILED}

    Status current;

    public Transaction() {

    }

    public void setCurrentStatus(Status status) {
        this.current = status;
    }

    public Status getCurrentStatus() {
        return current;
    }

}
