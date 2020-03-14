package com.neuman.brutus.utils;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.neuman.brutus.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DialogUtils {

    public String boolTitleDialog = "";

    public void showBoolDialog(final View v, Context context) {
        final String[] array = new String[] { "True", "False" };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(boolTitleDialog);
        builder.setSingleChoiceItems(array, -1, (dialogInterface, i) -> {
            ((EditText) v).setText(array[i]);
            dialogInterface.dismiss();
        });
        builder.show();
    }

    public void showEnumDialog(final View v, final ArrayList<String> enums, String title, Context context) {
        final String[] array = enums.toArray(new String[enums.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setSingleChoiceItems(array, -1, (dialogInterface, i) -> {
            ((EditText) v).setText(array[i]);
            dialogInterface.dismiss();
        });
        builder.show();
    }

    public void dialogDatePickerLight(final View v, Resources resources, FragmentManager fragmentManager, String val) {

        Calendar cur_calender = Calendar.getInstance();

        try {
            if (val != null) {
                cur_calender.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(val));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        DatePickerDialog datePicker = DatePickerDialog.newInstance(
            (view, year, monthOfYear, dayOfMonth) -> {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                long date = calendar.getTimeInMillis();

                SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
                ((EditText) v).setText(newFormat.format(new Date(date)));

            },
            cur_calender.get(Calendar.YEAR),
            cur_calender.get(Calendar.MONTH),
            cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(resources.getColor(R.color.colorPrimary));
        datePicker.show(fragmentManager, "Pick a Date");

    }

    public View makeTextView(String text, LayoutInflater lay) {
        View ve = lay.inflate(R.layout.widget_text_view, null);
        TextView textView = ve.findViewById(R.id.add_roma_label);
        textView.setText(text);
        return ve;
    }
}
