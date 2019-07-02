package net.number64.common.regexengine;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        // input "String"
        String targetString = scanner.next();

        // input "Regex"
        String regex = scanner.next();

        // count
        int result = Matcher.countLongestMatchingChars(targetString, regex);

        // output
        System.out.println("Result: " + result);

    }
}
