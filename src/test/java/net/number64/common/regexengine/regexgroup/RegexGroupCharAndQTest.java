package net.number64.common.regexengine.regexgroup;

import net.number64.common.regexengine.Compiler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class RegexGroupCharAndQTest {

    private final Logger Log = LoggerFactory.getLogger(RegexGroupCharAndQTest.class.getSimpleName());

    /**
     * Test classes are inhibited to have constructors that takes any parameters.
     * I really want to make this test class to extend RegexGroup for simplicity,
     * But I use the Reflection classes unwillingly.
     */
    public RegexGroupCharAndQTest() throws Exception {
        Class<?> compilerClass = Class.forName("net.number64.common.regexengine.regexgroup.RegexGroupCharAndQ");
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
        RegexGroupCharAndQ group = (RegexGroupCharAndQ) Compiler.generateRegexGroup("a?");
        Log.info("+ CASE 1");
        Log.info("  - always get 1");
        assertEquals(1, group.getMaxMatchableCount("testGetMaxMatchableCount", 0));
        Log.info("[END]\n");
    }

    @Test
    public void testCanMatchOutOfBounds() {
        Log.info("[START] testCanMatchOutOfBounds");
        RegexGroupCharAndQ group = (RegexGroupCharAndQ) Compiler.generateRegexGroup(".?");
        Log.info("+ CASE 1");
        Log.info("  - true is expected");
        assertTrue(group.canMatchOutOfBounds());
        Log.info("[END]\n");
    }

    @Test
    public void testGetFixedLength() {
        Log.info("[START] testGetFixedLength");
        RegexGroupCharAndQ group1 = (RegexGroupCharAndQ) Compiler.generateRegexGroup("a?");
        RegexGroupCharAndQ group2 = (RegexGroupCharAndQ) Compiler.generateRegexGroup("a?");
        group2.setNextUnit(Compiler.generateRegexGroup("Compiler"));
        Log.info("+ CASE 1");
        Log.info("  - group1 has no child.");
        assertEquals(0, group1.getFixedLength());
        Log.info("+ CASE 2");
        Log.info("  - group2 has the next group.");
        assertEquals(8, group2.getFixedLength());
        Log.info("[END]\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_tooShortExpression() {
        Log.info("[START] testConstructor_tooShortExpression");
        Log.info("+ CASE 1");
        Log.info("[END]\n");
        @SuppressWarnings("unused")
        IRegexGroup illegalLengthExpression = Compiler.generateRegexGroup("?");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_tooLongExpression() {
        Log.info("[START] testConstructor_tooLongExpression");
        Log.info("+ CASE 1");
        Log.info("[END]\n");
        @SuppressWarnings("unused")
        IRegexGroup illegalLengthExpression = Compiler.generateRegexGroup("aa?");
    }
}