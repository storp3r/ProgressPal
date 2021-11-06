package storper.matt.c196_progress_pal.Utilities;

import android.content.Context;

public class DataIntegrity {
Alert alert = new Alert();
    public boolean noNullStrings(String... args) {
        for(String arg : args) {
            if(arg.isEmpty()){
                return false;
            }
        }
        return true;
    }
}
