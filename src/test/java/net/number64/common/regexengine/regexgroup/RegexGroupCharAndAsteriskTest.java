package net.number64.common.regexengine.regexgroup;

import net.number64.common.regexengine.Compiler;
import org.junit.Test;

import static org.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class RegexGroupCharAndAsteriskTest {

    private final Logger Log = LoggerFactory.getLogger(RegexGroupCharAndAsteriskTest.class.getSimpleName());

    /**
     * Test classes are inhibited to have constructors that takes any parameters.
     * I really want to make this test class to extend RegexGroup for simplicity,
     * But I use the Reflection classes unwillingly.
     */
    public RegexGroupCharAndAsteriskTest() throws Exception {
        Class<?> compilerClass = Class.forName("net.number64.common.regexengine.regexgroup.RegexGroupCharAndAsterisk");
        Method method1 = compilerClass.getDeclaredMethod("getMaxMatchableCount", String.class, int.class);
        method1.setAccessible(true);
        Method method2 = compilerClass.getDeclaredMethod("canMatchOutOfBounds");
        method2.setAccessible(true);
        Method method3 = compilerClass.getDeclaredMethod("getFixedLength");
        method3.setAccessible(true);
    }

    @Test
    public void testGetMaxMatchableCount() {
        Log.info("[START] testGetMaxMatchableCount");
        RegexGroupCharAndAsterisk group = (RegexGroupCharAndAsterisk) Compiler.generateRegexGroup("a*");
        Log.info("+ CASE 1");
        Log.info("  - from top of the target");
        assertEquals(14, group.getMaxMatchableCount("abcdefghijklmn", 0));
        Log.info("+ CASE 2");
        Log.info("  - from mid of the target");
        assertEquals(10, group.getMaxMatchableCount("abcdefghijklmn", 4));
        Log.info("+ CASE 3");
        Log.info("  - at end of the target");
        assertEquals(1, group.getMaxMatchableCount("abcdefghijklmn", 13));
        Log.info("[END]\n");
    }

    @Test
    public void testCanMatchOutOfBounds() {
        Log.info("[START] testCanMatchOutOfBounds");
        RegexGroupCharAndAsterisk group = (RegexGroupCharAndAsterisk) Compiler.generateRegexGroup("a*");
        Log.info("+ CASE 1");
        assertTrue(group.canMatchOutOfBounds());
        Log.info("[END]\n");
    }

    @Test
    public void testGetFixedLength() {
        Log.info("[START] testGetFixedLength");
        RegexGroupCharAndAsterisk group1 = (RegexGroupCharAndAsterisk) Compiler.generateRegexGroup("a*");
        RegexGroupCharAndAsterisk group2 = (RegexGroupCharAndAsterisk) Compiler.generateRegexGroup("a*");
        group2.setNextUnit(Compiler.generateRegexGroup("abc"));
        Log.info("+ CASE 1");
        Log.info("  - group1 has no child.");
        assertEquals(0, group1.getFixedLength());
        Log.info("+ CASE 2");
        Log.info("  - group2 has the next group.");
        assertEquals(3, group2.getFixedLength());
        Log.info("[END]\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_tooShortExpression() {
        Log.info("[START] testConstructor_tooShortExpression");
        Log.info("+ CASE 1");
        Log.info("[END]\n");
        @SuppressWarnings("unused")
        IRegexGroup illegalLengthExpression = Compiler.generateRegexGroup("*");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_tooLongExpression() {
        Log.info("[START] testConstructor_tooLongExpression");
        Log.info("+ CASE 1");
        Log.info("[END]\n");
        @SuppressWarnings("unused")
        IRegexGroup illegalLengthExpression = Compiler.generateRegexGroup("aa*");
    }
}