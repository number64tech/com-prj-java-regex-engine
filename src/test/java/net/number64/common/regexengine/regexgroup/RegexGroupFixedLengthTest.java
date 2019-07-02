package net.number64.common.regexengine.regexgroup;

import net.number64.common.regexengine.Compiler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class RegexGroupFixedLengthTest {

    private final Logger Log = LoggerFactory.getLogger(RegexGroupFixedLengthTest.class.getSimpleName());

    /**
     * Test classes are inhibited to have constructors that takes any parameters.
     * I really want to make this test class to extend RegexGroup for simplicity,
     * But I use the Reflection classes unwillingly.
     */
    public RegexGroupFixedLengthTest() throws Exception {
        Class<?> compilerClass = Class.forName("net.number64.common.regexengine.regexgroup.RegexGroupFixedLength");
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
        RegexGroupFixedLength group = (RegexGroupFixedLength) Compiler.generateRegexGroup("abc");
        Log.info("+ CASE 1");
        Log.info("  - always get 1");
        assertEquals(1, group.getMaxMatchableCount("testGetMaxMatchableCount", 0));
        Log.info("[END]\n");
    }

    @Test
    public void testCanMatchOutOfBounds() {
        Log.info("[START] testCanMatchOutOfBounds");
        RegexGroupFixedLength group = (RegexGroupFixedLength) Compiler.generateRegexGroup("de.");
        Log.info("+ CASE 1");
        Log.info("  - false is expected");
        assertFalse(group.canMatchOutOfBounds());
        Log.info("[END]\n");
    }

    @Test
    public void testGetFixedLength() {
        Log.info("[START] testGetFixedLength");
        RegexGroupFixedLength group1 = (RegexGroupFixedLength) Compiler.generateRegexGroup("abc");
        RegexGroupFixedLength group2 = (RegexGroupFixedLength) Compiler.generateRegexGroup("def");
        group2.setNextUnit(Compiler.generateRegexGroup(".."));
        Log.info("+ CASE 1");
        Log.info("  - group1 has no child.");
        assertEquals(3, group1.getFixedLength());
        Log.info("+ CASE 2");
        Log.info("  - group2 has the next group.");
        assertEquals(5, group2.getFixedLength());
        Log.info("[END]\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_containsInvalidChar_Asterisk() {
        Log.info("[START] testConstructor_containsInvalidChar_Asterisk");
        Log.info("+ CASE 1");
        Log.info("[END]\n");
        @SuppressWarnings("unused")
        IRegexGroup illegalLengthExpression = new RegexGroupFixedLength("a*");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_containsInvalidChar_Question() {
        Log.info("[START] testConstructor_containsInvalidChar_Question");
        Log.info("+ CASE 1");
        Log.info("[END]\n");
        @SuppressWarnings("unused")
        IRegexGroup illegalLengthExpression = new RegexGroupFixedLength("b?");
    }
}