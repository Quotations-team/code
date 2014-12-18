package com.example.quotations;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;

import com.parse.starter.Quotation;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuotationsHelper {

    final static int MILLISECOND = 1;
    final static int SECOND = 1000 * MILLISECOND;
    final static int MINUTE = 60 * SECOND;
    final static int HOUR = 60 * MINUTE;
    final static int DAY = 24 * HOUR;
    final static int MONTH = 30 * DAY;

    public static void showAlertDialog(Context c, String message) {
        new AlertDialog.Builder(c)
                .setTitle("Guest User")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static String getFormattedNumber(int number) {
        String formattedNumber;
        if (number > 1000000)
            formattedNumber = String.format("%.1fM", number / 1000000.0);
        else if (number > 1000)
            formattedNumber = String.format("%.1fK", number / 1000.0);
        else
            formattedNumber = Integer.toString(number);

        return formattedNumber;
    }

    public static String getFormattedDate(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date dd = dateFormat.parse(date);
            return getFormattedDate(dd);
        }
        catch(Exception ex)
        {
            return "";
        }
    }

    public static String getFormattedDate(Date date) {

        Date currentDate = new Date();
        long diff = currentDate.getTime() - date.getTime();

        if (diff < MINUTE)
        {
            return "Less than a minute ago";
        }
        else if (diff < HOUR)
        {
            return (diff / (60 * 1000)) + " minutes ago";
        }
        else if (diff < DAY)
        {
            long hours = (diff / (60 * 60 * 1000));
            return (hours ==1 ? "one hour ago" : hours + " hours ago");
        }
        else if (diff < 2 * DAY)
        {
            return "yesterday";
        }
        else if (diff < 30 * DAY)
        {
            return (diff / (24 * 60 * 60 * 1000)) + " days ago";
        }
        else if (diff < 12 * MONTH)
        {
            long months = (diff / (30 * 24 * 60 * 60 * 1000));
            return months <= 1 ? "one month ago" : months + " months ago";
        }
        else
        {
            long years = (diff / (365 * 24 * 60 * 60 * 1000));
            return years <= 1 ? "one year ago" : years + " years ago";
        }
    }

    public static String getStatusText(Quotation quote) {
        String statusText = "";
        if(quote != null) {
            if (quote.getLikes() > 0)
                statusText += getFormattedNumber(quote.getLikes()) + (quote.getLikes() == 1 ? " like   " : " Likes   ");
            if (quote.getComments() > 0)
                statusText += getFormattedNumber(quote.getComments()) + (quote.getComments() == 1 ? " Comment" : " Comments");
        }
        return statusText;
    }
}

