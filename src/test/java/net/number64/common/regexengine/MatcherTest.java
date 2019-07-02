package net.number64.common.regexengine;

import org.junit.Test;

import static org.junit.Assert.*;

public class MatcherTest {

    // those patterns below were given as samples from recruiting company

    @Test
    public void countLongestMatchingChars_checkSamples_sample1_OK() {
        int result;
        result = Matcher.countLongestMatchingChars(
                "abcdefghij","def");
        assertEquals(3, result);
    }

    @Test
    public void countLongestMatchingChars_checkSamples_sample2_OK() {
        int result;
        result = Matcher.countLongestMatchingChars(
                "thisisaplaintext","a*bc");
        assertEquals(-1, result);
    }

    @Test
    public void countLongestMatchingChars_checkSamples_sample3_OK() {
        int result;
        result = Matcher.countLongestMatchingChars(
                "recruitcommunications","r.*c.*o");
        assertEquals(19, result);
    }

    @Test
    public void countLongestMatchingChars_checkSamples_sample4_OK() {
        int result;
        result = Matcher.countLongestMatchingChars(
                "aaabbbqqqzzz","q*");
        assertEquals(3, result);
    }

    @Test
    public void countLongestMatchingChars_checkSamples_sample5_OK() {
        int result;
        result = Matcher.countLongestMatchingChars(
                "lxyxyzrcolxyyyyrco","xy*z?rco");
        assertEquals(8, result);
    }
}