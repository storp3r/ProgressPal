package storper.matt.c196_progress_pal.Utilities;

import android.content.Context;

public class DataIntegrity {

    public boolean noNullStrings(String... args) {
        for(String arg : args) {
            if(arg.isEmpty()){
                return false;
            }
        }
        return true;
    }
}
