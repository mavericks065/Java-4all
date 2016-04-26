package myKatas.ng.StringCalculator.solution;

import myKatas.ng.StringCalculator.solution.exceptions.NegativeNumbersException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringCalculator {

    private String[] delimiters = new String[]{",", "\n"};
    private int[] numbersToAdd = new int[]{};
    private static final String DELIMITER_PATTERN = "\\[([^\\[\\]]+)\\]";

    public int add(String inputString) throws NegativeNumbersException {
        int result;

        if (inputString.startsWith("//")) {
            delimiters = getDelimiterFromInput(inputString);
            inputString = removeDelimiterLine(inputString);
        }

        if (stringIsEmpty(inputString)) {
            result = 0;
        } else {
            numbersToAdd = getNumbersFromInput(inputString);

            throwExceptionIfAnyNumberIsNegative();
            ignoreNumbersBiggerThanThousand();

            result = addNumbers();
        }

        return result;
    }

    private void ignoreNumbersBiggerThanThousand() {
        int[] numbersToAddCopy = ArrayUtils.clone(numbersToAdd);

        for (int i = 0; i < numbersToAddCopy.length; i++) {
            int currentNumber = numbersToAddCopy[i];
            if (currentNumber > 1000) {
                numbersToAdd = ArrayUtils.removeElement(numbersToAdd, currentNumber);
            }
        }
    }

    private int[] getNumbersFromInput(String inputString) {
        String[] numbersAsString = splitString(inputString);
        int[] numbers = new int[]{};

        for (int i = 0; i < numbersAsString.length; i++) {
            String currentNumberAsString = numbersAsString[i];
            numbers = ArrayUtils.add(numbers, stringToInt(currentNumberAsString));
        }

        return numbers;
    }

    private void throwExceptionIfAnyNumberIsNegative() {
        String[] negativeNumbers = new String[]{};

        for (int i = 0; i < numbersToAdd.length; i++) {
            int currentNumber = numbersToAdd[i];
            if (currentNumber < 0) {
                negativeNumbers = ArrayUtils.add(negativeNumbers, String.valueOf(currentNumber));
            }
        }

        if (ArrayUtils.isNotEmpty(negativeNumbers)) {
            String test = StringUtils.join(negativeNumbers, ", ");
            throw new NegativeNumbersException(StringUtils.join(negativeNumbers, ", "));
        }
    }

    private String removeDelimiterLine(String inputString) {
        int newLineIndex = inputString.indexOf("\n");
        return inputString.substring(newLineIndex + 1);
    }

    private String[] getDelimiterFromInput(String inputString) {
        String[] newDelimiters = new String[]{};

        if (inputString.contains("[") && inputString.contains("]")) {
            Pattern p = Pattern.compile(DELIMITER_PATTERN);
            Matcher m = p.matcher(inputString);
            while (m.find()) {
                String currentDelimiter = m.group(1);
                currentDelimiter = escapeRegexMetaChars(currentDelimiter);
                newDelimiters = ArrayUtils.add(newDelimiters, currentDelimiter);
            }
        } else if(inputString.startsWith("//")) {
            String newDelimiter = escapeRegexMetaChars(inputString.substring(2,3));
            newDelimiters = ArrayUtils.add(newDelimiters, newDelimiter);
        } else {
            String newDelimiter = escapeRegexMetaChars(inputString);
            newDelimiters = ArrayUtils.add(newDelimiters, newDelimiter);
        }

        return newDelimiters;
    }

    private String escapeRegexMetaChars(String delimiter) {
        return Pattern.quote(delimiter);
    }

    private int addNumbers() {
        int result = 0;

        for (int i = 0; i < numbersToAdd.length; i++) {
            int currentNumber = numbersToAdd[i];
            result += currentNumber;
        }

        return result;
    }

    private String[] splitString(String inputString) {
        return inputString.split(StringUtils.join(delimiters, "|"));
    }

    private boolean stringIsEmpty(String inputString) {
        return StringUtils.isEmpty(inputString);
    }

    private int stringToInt(String s) {
        return Integer.parseInt(s);
    }

    public void setDelimiters(String s) {
        delimiters = getDelimiterFromInput(s);
    }
}