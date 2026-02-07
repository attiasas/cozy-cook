package com.cozycook.calendar;

import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;

/**
 * Helper to add meal-prep or grocery events to calendar.
 * Uses CalendarContract intent so user can choose their calendar app.
 * Replace or extend with Google Calendar API / Tasks API if needed.
 */
public final class CalendarHelper {

    /**
     * Create intent to add an event to the default calendar app.
     * @param title Event title (e.g. "Meal prep: Pasta")
     * @param startMillis Start time
     * @param endMillis End time (e.g. start + 60 min)
     * @param description Optional description
     */
    public static Intent createAddEventIntent(String title, long startMillis, long endMillis, String description) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, title);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis);
        if (description != null && !description.isEmpty()) {
            intent.putExtra(CalendarContract.Events.DESCRIPTION, description);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * One-hour block for "Prep: [recipe title]" on the given day at 10:00.
     */
    public static Intent prepEventForDay(String recipeTitle, long dayStartMillis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dayStartMillis);
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 0);
        long start = cal.getTimeInMillis();
        cal.add(Calendar.HOUR, 1);
        long end = cal.getTimeInMillis();
        return createAddEventIntent("Prep: " + recipeTitle, start, end, null);
    }

    /**
     * Grocery shopping block (e.g. 14:00–15:00).
     */
    public static Intent groceryEventForDay(long dayStartMillis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dayStartMillis);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 0);
        long start = cal.getTimeInMillis();
        cal.add(Calendar.HOUR, 1);
        long end = cal.getTimeInMillis();
        return createAddEventIntent("Grocery shopping", start, end, "From Cozy Cook plan");
    }
}
