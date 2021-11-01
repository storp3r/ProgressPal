package storper.matt.c196_progress_pal.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    public Date convertStringToDate(String date) {
        Date temp = null;
        try {
            temp = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public String convertDateToString(Date date) {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat formatDate = new SimpleDateFormat(dateFormat, Locale.US);

        return formatDate.format(date.getTime());
    }
}
