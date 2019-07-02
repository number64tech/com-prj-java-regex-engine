package net.number64.open.regexengine.regexgroup;

import net.number64.open.regexengine.Compiler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class AbstractRegexGroupTest {

    private final Logger Log = LoggerFactory.getLogger(AbstractRegexGroupTest.class.getSimpleName());

    private Method compareAndGetMaxRepetitionCount;
    private Method adjustStartIndexAndCallNextUnit;

    public AbstractRegexGroupTest() throws Exception {
        Class<?> compilerClass = Class.forName("net.number64.open.regexengine.regexgroup.AbstractRegexGroup");
        compareAndGetMaxRepetitionCount = compilerClass.getDeclaredMethod(
                "compareAndGetMaxRepetitionCount", String.class, int.class);
        compareAndGetMaxRepetitionCount.setAccessible(true);
        adjustStartIndexAndCallNextUnit = compilerClass.getDeclaredMethod(
                "adjustStartIndexAndCallNextUnit", String.class, int.class, int.class);
        adjustStartIndexAndCallNextUnit.setAccessible(true);
    }

    @Test
    public void testCompareAndCount_SingleGroup() {
        Log.info("[START] testCompareAndCount_SingleGroup");

        Log.info("+ CASE 1");
        Log.info("  - FixedLength, not match");
        RegexGroupFixedLength groupA = (RegexGroupFixedLength) Compiler.generateRegexGroup("abc");
        assertEquals(IRegexGroup.RESULT_NUM_FOR_MATCHER_NOT_MATCHED, groupA.compareAndCount("ababc", 0));
        Log.info("+ CASE 2");
        Log.info("  - FixedLength, match");
        assertEquals(3, groupA.compareAndCount("ababc", 2));

        Log.info("+ CASE 3");
        Log.info("  - CharAndQ, 1char match");
        RegexGroupCharAndQ groupB = (RegexGroupCharAndQ) Compiler.generateRegexGroup("d?");
        assertEquals(1, groupB.compareAndCount("cde", 1));
        Log.info("+ CASE 4");
        Log.info("  - CharAndQ, 0-size match");
        assertEquals(0, groupB.compareAndCount("cde", 2));
        Log.info("+ CASE 5");
        Log.info("  - CharAndQ, boundary match");
        assertEquals(0, groupB.compareAndCount("cde", 3));

        Log.info("+ CASE 6");
        Log.info("  - CharAndAsterisk, multiple char match");
        RegexGroupCharAndAsterisk groupC = (RegexGroupCharAndAsterisk) Compiler.generateRegexGroup("e*");
        assertEquals(7, groupC.compareAndCount("cdeeeeeeeefg", 3));
        Log.info("+ CASE 7");
        Log.info("  - CharAndAsterisk, 0-size match");
        assertEquals(0, groupC.compareAndCount("cde", 0));
        Log.info("+ CASE 8");
        Log.info("  - CharAndAsterisk, boundary match");
        assertEquals(0, groupC.compareAndCount("cde", 3));

        Log.info("[END]\n");
    }

    @Test
    public void testCompareAndCount_FixedAndFixed() {
        Log.info("[START] testCompareAndCount_FixedAndFixed");
        RegexGroupFixedLength groupA = (RegexGroupFixedLength) Compiler.generateRegexGroup("abc");
        groupA.setNextUnit(Compiler.generateRegexGroup("d.f"));
        Log.info("+ CASE 1");
        Log.info("  - Fixed + Fixed, second group doesn't match");
        assertEquals(IRegexGroup.RESULT_NUM_FOR_MATCHER_NOT_MATCHED, groupA.compareAndCount("abcdeg", 0));
        Log.info("+ CASE 2");
        Log.info("  - Fixed + Fixed, both groups match correctly");
        assertEquals(6, groupA.compareAndCount("abcdef", 0));
        Log.info("+ CASE 3");
        Log.info("  - Fixed + Fixed, both groups match correctly");
        assertEquals(6, groupA.compareAndCount("abcddf", 0));
        Log.info("[END]\n");
    }

    @Test
    public void testCompareAndCount_FixedAndFlex() {
        Log.info("[START] testCompareAndCount_FixedAndFlex");

        Log.info("+ CASE 1");
        Log.info("  - Fixed + CharAndQ, both groups match in full size");
        RegexGroupFixedLength groupA = (RegexGroupFixedLength) Compiler.generateRegexGroup("abc");
        groupA.setNextUnit(Compiler.generateRegexGroup("d?"));
        assertEquals(4, groupA.compareAndCount("abcdefg", 0));
        Log.info("+ CASE 2");
        Log.info("  - Fixed + CharAndQ, only first group matches");
        assertEquals(3, groupA.compareAndCount("abcmefg", 0));

        Log.info("+ CASE 3");
        Log.info("  - CharAndQ + Fixed, both groups match in full size");
        RegexGroupCharAndQ groupB = (RegexGroupCharAndQ) Compiler.generateRegexGroup("a?");
        groupB.setNextUnit(Compiler.generateRegexGroup("bcd"));
        assertEquals(4, groupB.compareAndCount("abcdefg", 0));
        Log.info("+ CASE 4");
        Log.info("  - CharAndQ + Fixed, only second group matches");
        assertEquals(3, groupB.compareAndCount("abcdefg", 1));

        Log.info("+ CASE 5");
        Log.info("  - Fixed + CharAndAsterisk, both groups match in full size");
        RegexGroupFixedLength groupC = (RegexGroupFixedLength) Compiler.generateRegexGroup("abc");
        groupC.setNextUnit(Compiler.generateRegexGroup("d*"));
        assertEquals(8, groupC.compareAndCount("abcdddddeeeffg", 0));
        Log.info("+ CASE 6");
        Log.info("  - Fixed + CharAndAsterisk, only first group matches");
        assertEquals(3, groupC.compareAndCount("abcmmmmm", 0));

        Log.info("+ CASE 7");
        Log.info("  - CharAndAsterisk + Fixed, both groups match in full size");
        RegexGroupCharAndAsterisk groupD = (RegexGroupCharAndAsterisk) Compiler.generateRegexGroup("a*");
        groupD.setNextUnit(Compiler.generateRegexGroup("bcd"));
        assertEquals(8, groupD.compareAndCount("aaaaabcdefg", 0));
        Log.info("+ CASE 8");
        Log.info("  - CharAndAsterisk + Fixed, only second group matches");
        assertEquals(3, groupD.compareAndCount("abcd", 1));

        Log.info("[END]\n");
    }

    @Test
    public void testCompareAndGetMaxRepetitionCount() throws Exception {
        Log.info("[START] testCompareAndGetMaxRepetitionCount");
        RegexGroupFixedLength groupF = (RegexGroupFixedLength) Compiler.generateRegexGroup("abc.");
        RegexGroupCharAndQ groupQ = (RegexGroupCharAndQ) Compiler.generateRegexGroup("d?");
        RegexGroupCharAndAsterisk groupA = (RegexGroupCharAndAsterisk) Compiler.generateRegexGroup("e*");
        RegexGroupCharAndAsterisk groupDA = (RegexGroupCharAndAsterisk) Compiler.generateRegexGroup(".*");

        Log.info("+ CASE 1");
        Log.info("  - results of boundary matching");
        assertEquals(IRegexGroup.RESULT_NUM_FOR_MATCHER_NOT_MATCHED,
                compareAndGetMaxRepetitionCount.invoke(groupF, "abdc", 4));
        assertEquals(0, compareAndGetMaxRepetitionCount.invoke(groupQ, "abcd", 4));
        assertEquals(0, compareAndGetMaxRepetitionCount.invoke(groupA, "abcd", 4));

        Log.info("+ CASE 2");
        Log.info("  - fixed pattern matching");
        assertEquals(IRegexGroup.RESULT_NUM_FOR_MATCHER_NOT_MATCHED,
                compareAndGetMaxRepetitionCount.invoke(groupF, "abdc", 0));
        assertEquals(1, compareAndGetMaxRepetitionCount.invoke(groupF, "abcd", 0));
        assertEquals(IRegexGroup.RESULT_NUM_FOR_MATCHER_NOT_MATCHED,
                compareAndGetMaxRepetitionCount.invoke(groupF, "aabc", 1));
        assertEquals(1, compareAndGetMaxRepetitionCount.invoke(groupF, "xyzabczefg", 3));

        Log.info("+ CASE 3");
        Log.info("  - a char and quantifier");
        assertEquals(0, compareAndGetMaxRepetitionCount.invoke(groupQ, "abdc", 1));
        assertEquals(1, compareAndGetMaxRepetitionCount.invoke(groupQ, "abcd", 3));
        assertEquals(0, compareAndGetMaxRepetitionCount.invoke(groupA, "abdc", 1));
        assertEquals(1, compareAndGetMaxRepetitionCount.invoke(groupA, "abcde", 4));
        assertEquals(1, compareAndGetMaxRepetitionCount.invoke(groupDA, "abdc", 3));
        assertEquals(4, compareAndGetMaxRepetitionCount.invoke(groupDA, "abcd", 0));

        Log.info("[END]\n");
    }

    @Test
    public void testAdjustStartIndexAndCallNextUnit() throws Exception {
        RegexGroupFixedLength groupF = (RegexGroupFixedLength) Compiler.generateRegexGroup("abc");
        RegexGroupCharAndQ groupQ = (RegexGroupCharAndQ) Compiler.generateRegexGroup("d?");
        RegexGroupCharAndAsterisk groupA = (RegexGroupCharAndAsterisk) Compiler.generateRegexGroup("h*");
        groupF.setNextUnit(Compiler.generateRegexGroup("def"));
        groupQ.setNextUnit(Compiler.generateRegexGroup("efg"));
        groupA.setNextUnit(Compiler.generateRegexGroup("hij"));

        Log.info("+ CASE 1");
        Log.info("  - fixed + fixed");
        assertEquals(IRegexGroup.RESULT_NUM_FOR_MATCHER_NOT_MATCHED,
                adjustStartIndexAndCallNextUnit.invoke(groupF, "abcde", 0, 1));
        assertEquals(6, adjustStartIndexAndCallNextUnit.invoke(groupF, "aabcdefg", 1, 1));

        Log.info("+ CASE 2");
        Log.info("  - a char and quantifier + fixed");
        assertEquals(4, adjustStartIndexAndCallNextUnit.invoke(groupQ, "defg", 0, 1));
        assertEquals(3, adjustStartIndexAndCallNextUnit.invoke(groupQ, "aabcdefg", 5, 1));

        Log.info("+ CASE 3");
        assertEquals(5, adjustStartIndexAndCallNextUnit.invoke(groupA, "hhhhhij", 2, 2));
        assertEquals(7, adjustStartIndexAndCallNextUnit.invoke(groupA, "hhhhhijk", 0, 5));
        assertEquals(3, adjustStartIndexAndCallNextUnit.invoke(groupA, "aabhijkg", 3, 0));
    }

    @Test
    public void testSetNecessaryMatchingCount() {
        Log.info("[START] testSetNecessaryMatchingCount");
        RegexGroupFixedLength group = (RegexGroupFixedLength) Compiler.generateRegexGroup("abc");
        Log.info("+ CASE 1");
        Log.info("  - set 100");
        group.necessaryRepetitionCount = 100;
        assertEquals(100, group.getNecessaryRepetitionCount());
        Log.info("[END]\n");
    }

    @Test
    public void testGetRawRegexUnitString() {
        Log.info("[START] testGetRawRegexUnitString");
        RegexGroupCharAndQ group = (RegexGroupCharAndQ) Compiler.generateRegexGroup("a?");
        Log.info("+ CASE 1");
        Log.info("  - a?");
        assertEquals("a?", group.getRawRegexUnitString());
        Log.info("[END]\n");
    }

    @Test
    public void testGetDividedRegex() {
        Log.info("[START] testGetDividedRegex");
        Log.info("+ CASE 1");
        Log.info("  - abc");
        RegexGroupFixedLength group = (RegexGroupFixedLength) Compiler.generateRegexGroup("abc");
        assertEquals(3, group.getDividedRegex().length);
        assertEquals("abc".toCharArray()[0], group.getDividedRegex()[0]);
        assertEquals("abc".toCharArray()[1], group.getDividedRegex()[1]);
        assertEquals("abc".toCharArray()[2], group.getDividedRegex()[2]);
        Log.info("+ CASE 2");
        Log.info("  - 'd?' is divided and dropped last quantifier ");
        RegexGroupCharAndQ group2 = (RegexGroupCharAndQ) Compiler.generateRegexGroup("d?");
        assertEquals(1, group2.getDividedRegex().length);
        assertEquals("d".toCharArray()[0], group2.getDividedRegex()[0]);
        Log.info("+ CASE 3");
        Log.info("  - '.*' is divided and dropped last quantifier ");
        RegexGroupCharAndAsterisk group3 = (RegexGroupCharAndAsterisk) Compiler.generateRegexGroup(".*");
        assertEquals(1, group3.getDividedRegex().length);
        assertEquals(".".toCharArray()[0], group3.getDividedRegex()[0]);
        Log.info("[END]\n");
    }

    @Test
    public void testIsMatchingFirstChar() {
        Log.info("[START] testIsMatchingFirstChar");
        RegexGroupFixedLength group = (RegexGroupFixedLength) Compiler.generateRegexGroup("abc");
        Log.info("+ CASE 1");
        Log.info("  - matching with 'a'");
        assertTrue(group.isMatchingFirstChar("a".charAt(0)));
        Log.info("+ CASE 2");
        Log.info("  - not matching with 'b'");
        assertFalse(group.isMatchingFirstChar("b".charAt(0)));
        Log.info("[END]\n");
    }

    @Test
    public void testIsLastUnit() {
        Log.info("[START] testIsLastUnit");
        RegexGroupFixedLength group = (RegexGroupFixedLength) Compiler.generateRegexGroup("abc");
        Log.info("+ CASE 1");
        Log.info("  - no child = true");
        assertTrue(group.isLastUnit());
        Log.info("+ CASE 2");
        Log.info("  - there is a child = false");
        group.setNextUnit(Compiler.generateRegexGroup("def"));
        assertFalse(group.isLastUnit());
        Log.info("[END]\n");
    }
}