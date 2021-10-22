package storper.matt.c196_progress_pal.Utilities;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import storper.matt.c196_progress_pal.Activities.AssessmentListActivity;
import storper.matt.c196_progress_pal.Activities.CourseListActivity;
import storper.matt.c196_progress_pal.MainActivity;
import storper.matt.c196_progress_pal.R;
import storper.matt.c196_progress_pal.Activities.TermListActivity;

public class MenuHandler extends AppCompatActivity {
    private static final String TAG = "MenuHandler";


    //check current activity to dynamically display menu options
    public void setMenuOptions(Class currentClass, Menu menu) {
        String activeClass = getCurrentClass(currentClass);

        for(int i = 0; i < menu.size(); i++){
            MenuItem item = menu.getItem(i);

            switch(activeClass){
                case "MainActivity":
                    item.setVisible(item.getItemId() != R.id.action_home);
                    break;
                case "ViewTermsActivity":
                    item.setVisible(item.getItemId() != R.id.action_terms);
                    break;
                case "ViewCoursesActivity":
                    item.setVisible(item.getItemId() != R.id.action_courses);
                    break;
                case "ViewAssessmentsActivity":
                    item.setVisible(item.getItemId() != R.id.action_assessments);
                    break;
            }

        }
    }



    //handle menu option click functionality
    public Class<?> handleMenuSelection(MenuItem item) {

        Class<?> target = null;

        switch(item.getItemId()){

            case R.id.action_terms:
                target = TermListActivity.class;
                break;
            case R.id.action_courses:
                target = CourseListActivity.class;
                break;
            case R.id.action_assessments:
                target = AssessmentListActivity.class;
                break;
            default:
                target = MainActivity.class;
        }
        return target;
    }



    public String getCurrentClass(Class current) {
        String classString = current.toString();
        String[] stringArray = classString.split("\\.");
        classString = stringArray[stringArray.length - 1];

        return classString;
    }

    public void setVisibility(int id ,View...views) {
//        ImageView upArrow = findViewById(R.id.upArrrow);
//        ImageView downArrow = findViewById(R.id.downArrow);

        if(id > -1){
            for(View view : views) {
                if(view.isShown()) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
            }
        } else {
            //TODO add alert
        }



    }
}