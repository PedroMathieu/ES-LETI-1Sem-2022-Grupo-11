package pt.iscte.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * Server util test class
 *
 * @author Jose Soares
 */
public class ServerUtilTest {
    private final Random r = new Random();

    @Test
    public void givenWrongMonthRanges_whenValidatingDate_returnsFalse() {
        boolean monthZero = ServerUtil.validateDateParams(2023,0 ,1);
        boolean lessThanMonthRange = ServerUtil.validateDateParams(2023, -1, 1);
        boolean greaterThanMonthRange = ServerUtil.validateDateParams(2023, r.nextInt(Integer.MAX_VALUE - 12) + 12, 1);
        Assertions.assertFalse(monthZero);
        Assertions.assertFalse(lessThanMonthRange);
        Assertions.assertFalse(greaterThanMonthRange);
    }

    @Test
    public void givenWrongYearRange_whenValidatingDate_returnsFalse() {
        boolean lessThanYearRange = ServerUtil.validateDateParams(r.nextInt(1000), 1, 1);
        boolean greaterThanYearRange = ServerUtil.validateDateParams(r.nextInt(Integer.MAX_VALUE - 3000) + 3000, 13, 1);
        Assertions.assertFalse(lessThanYearRange);
        Assertions.assertFalse(greaterThanYearRange);
    }

    @Test
    public void givenWrongDayNumberForSpecifiedMonth_whenValidating_returnsFalse() {
        int randomlyObtainedMonth = r.nextInt(13-1) + 1;
        int greaterThanRangeForMonth = ServerUtil.monthLength[randomlyObtainedMonth-1] + 1;
        int lessThanRangeForMonth = 0;
        Assertions.assertFalse(ServerUtil.validateDateParams(2022, randomlyObtainedMonth, greaterThanRangeForMonth));
        Assertions.assertFalse(ServerUtil.validateDateParams(2022, randomlyObtainedMonth, lessThanRangeForMonth));
    }

    @Test
    public void given29FebruaryOnALeapYear_whenValidatingDate_returnsTrue() {
        boolean leapYearActualValue = ServerUtil.validateDateParams(2024, 2, 29);
        Assertions.assertTrue(leapYearActualValue);
    }

    @Test
    public void whenGeneratingRandomName_returnsCorrectLength() {
        for (String rName : ServerTestHelper.getRandomNames()) {
            boolean correctLength = rName.length() >= 5 && rName.length() < 21;
            Assertions.assertTrue(correctLength);
        }
    }

    @Test
    public void whenGeneratingRandomName_allCharsAreLetters() {
        for (String rName : ServerTestHelper.getRandomNames()) {
            char[] charsOfName = rName.toCharArray();
            for (char c : charsOfName) Assertions.assertTrue(Character.isLetter(c));
        }
    }
}
