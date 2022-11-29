package iscte.server;

import java.util.Random;

/**
 * This util class is used by the server when we have to do something trivial.
 *
 * @author Jose Soares
 */
public class ServerUtil {

    public static int[] monthLength = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    /**
     * Checks if date is valid. By checking year, month and day ranges.
     * Also checks if the given days are in the range of the given month.
     *
     * @param year  requested year to validate
     * @param month requested month to validate
     * @param day   request day to validate
     * @return true if date is valid, false otherwise
     */
    public static boolean validateDateParams(int year, int month, int day) {
        // Date could not be parsed from request
        if (year == -1 && month == -1 && day == -1) return false;

        // Check the ranges of month and year
        if (year < 1000 || year > 3000 || month <= 0 || month > 12)
            return false;

        // Adjust for leap years
        if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
            monthLength[1] = 29;

        // Check the range of the day
        return day > 0 && day <= monthLength[month - 1];
    }

    /**
     * Generates a random string of letters to be used to create
     * a temp file with a random name. Just to avoid duplicate files
     * when uploading multiple files. This a scalable way to upload calendars.
     *
     * @return string with random characters
     */
    public static String generateRandomTempName() {
        Random random = new Random();
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = random.nextInt(21-5) + 5;
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }
}
