package org.tagUtil;

import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {

    private final Text text;

    public Logger(Text text) {
        this.text = text;
    }

    public void write(String string) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
        text.setText(text.getText() + "\n" + timeOnly.format(cal.getTime()) + ": " + string);
    }

    public void write(String string, Exception e) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
        text.setText(text.getText() + "\n" + timeOnly.format(cal.getTime()) + ": " + string + " " + e.getMessage());
    }
}
