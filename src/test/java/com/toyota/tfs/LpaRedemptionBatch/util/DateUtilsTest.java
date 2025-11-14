package com.toyota.tfs.LpaRedemptionBatch.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

class DateUtilsTest {

    @Test
    void testAddDays() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.OCTOBER, 1); // Set a specific date
        Date initialDate = calendar.getTime();
        int daysToAdd = 5;

        // Act
        Date resultDate = DateUtils.addDays(initialDate, daysToAdd);

        // Assert
        calendar.add(Calendar.DATE, daysToAdd);
        assertEquals(calendar.getTime(), resultDate);
    }

    @Test
    void testRemoveDays() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.OCTOBER, 10); // Set a specific date
        Date initialDate = calendar.getTime();
        int daysToRemove = 3;

        // Act
        Date resultDate = DateUtils.removeDays(initialDate, daysToRemove);

        // Assert
        calendar.add(Calendar.DATE, -daysToRemove);
        assertEquals(calendar.getTime(), resultDate);
    }

    @Test
    void testGetDateTime() {
        // Act
        String dateTime = new DateUtils().getDateTime();

        // Assert
        assertNotNull(dateTime);
        assertFalse(dateTime.isEmpty());
    }
}