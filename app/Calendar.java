import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class MainPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Button datePickerButton = findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainPage.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Handle the selected date
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                        Log.i("DatePicker", "Selected Date: " + selectedDate);
                        // Start CalendarActivity and pass the selected date
                        Intent intent = new Intent(MainPage.this, CalendarActivity.class);
                        intent.putExtra("selectedDate", selectedDate);
                        startActivity(intent);
                    }
                }, year, month, dayOfMonth);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
}
