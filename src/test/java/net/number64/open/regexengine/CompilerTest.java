package net.number64.open.regexengine;

import net.number64.open.regexengine.regexgroup.IRegexGroup;
import net.number64.open.regexengine.regexgroup.RegexGroupCharAndAsterisk;
import net.number64.open.regexengine.regexgroup.RegexGroupCharAndQ;
import net.number64.open.regexengine.regexgroup.RegexGroupFixedLength;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class CompilerTest {

    private final Logger SlfLogger = LoggerFactory.getLogger(CompilerTest.class.getSimpleName());

    @Test
    public void testDivideRegexPatternToUnits_NoQuantifier() throws Exception {
        SlfLogger.info("[START] testDivideRegexPatternToUnits_NoQuantifier");
        Method divideRegexPatternToUnits = extractDivideRegexPatternToUnits();

        // CASE 1: single alphabet
        testDivideRegexPatternToUnits_NoQuantifier_Helper(
                1, "a", divideRegexPatternToUnits);

        // CASE 2: single full-stop
        testDivideRegexPatternToUnits_NoQuantifier_Helper(
                2, ".", divideRegexPatternToUnits);

        // CASE 3: starts with & ends with a full-stop
        testDivideRegexPatternToUnits_NoQuantifier_Helper(
                3, ".bc.", divideRegexPatternToUnits);

        // CASE 4: 1000 chars
        testDivideRegexPatternToUnits_NoQuantifier_Helper(
                4, "\".thousand.length.string.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"", divideRegexPatternToUnits);

        SlfLogger.info("[END]\n");
    }

    private void testDivideRegexPatternToUnits_NoQuantifier_Helper(
            int caseNumber, String expression, Method divideRegexPatternToUnits) throws Exception {
        SlfLogger.info("+ CASE " + caseNumber);
        SlfLogger.info("  - expression: {}", expression);
        Object resultInvoked = divideRegexPatternToUnits.invoke(new Compiler(), expression);
        @SuppressWarnings("unchecked")
        ArrayList<String> result = (ArrayList<String>) resultInvoked;
        assertEquals(1, result.size());
        assertEquals(expression, result.get(0));
    }

    @Test
    public void testDivideRegexPatternToUnits_WithQuantifiers() throws Exception {
        SlfLogger.info("[START] testDivideRegexPatternToUnits_WithQuantifiers");
        Method divideRegexPatternToUnits = extractDivideRegexPatternToUnits();

        // CASE 1: single alphabet and ?
        testDivideRegexPatternToUnits_WithQuantifiers_Helper(
                1, "a?", Arrays.asList("a?"), divideRegexPatternToUnits);

        // CASE 2: single alphabet and *
        testDivideRegexPatternToUnits_WithQuantifiers_Helper(
                2, "a*", Arrays.asList("a*"), divideRegexPatternToUnits);

        // CASE 3: single alphabet and ? and c
        testDivideRegexPatternToUnits_WithQuantifiers_Helper(
                3, "a?c", Arrays.asList("a?", "c"), divideRegexPatternToUnits);

        // CASE 4: single alphabet and * and c
        testDivideRegexPatternToUnits_WithQuantifiers_Helper(
                4, "a*c", Arrays.asList("a*", "c"), divideRegexPatternToUnits);

        // CASE 5: multiple alphabet and ?
        testDivideRegexPatternToUnits_WithQuantifiers_Helper(
                5, "abcdefg?i?k?", Arrays.asList("abcdef", "g?", "i?", "k?"), divideRegexPatternToUnits);

        // CASE 6: multiple alphabet and ? and *
        testDivideRegexPatternToUnits_WithQuantifiers_Helper(
                6, "ab?de*.*.*", Arrays.asList("a", "b?", "d", "e*", ".*", ".*"), divideRegexPatternToUnits);

        SlfLogger.info("[END]\n");
    }

    private void testDivideRegexPatternToUnits_WithQuantifiers_Helper(
            int caseNumber, String expression, List<String> expected, Method divideRegexPatternToUnits) throws Exception {
        SlfLogger.info("+ CASE " + caseNumber);
        SlfLogger.info("  - expression: {}", expression);
        Object resultInvoked = divideRegexPatternToUnits.invoke(new Compiler(), expression);
        @SuppressWarnings("unchecked")
        ArrayList<String> result = (ArrayList<String>) resultInvoked;
        assertEquals(expected.size(), result.size());
        for (int index = 0; index < expected.size() ; index++) {
            assertEquals(expected.get(index), result.get(index));
        }
    }

    @Test
    public void testGenerateRegexGroupChain() throws Exception {
        SlfLogger.info("[START] testGenerateRegexGroupChain");
        Method generateRegexGroupChain = extractGenerateRegexGroupChain();

        // CASE 1: single alphabet
        testGenerateRegexUnitChain_Helper(1, generateRegexGroupChain,
                Arrays.asList("a"),
                Arrays.asList("a"),
                Arrays.asList(Compiler.generateRegexGroup("a"))
        );

        // CASE 2: single alphabets
        testGenerateRegexUnitChain_Helper(2, generateRegexGroupChain,
                Arrays.asList("a", "bb"),
                Arrays.asList("a", "bb"),
                Arrays.asList(Compiler.generateRegexGroup("a"), Compiler.generateRegexGroup("bb"))
        );

        // CASE 3: valid patterns
        testGenerateRegexUnitChain_Helper(3, generateRegexGroupChain,
                Arrays.asList("aaa", "b?", "c*", ".?", ".*"),
                Arrays.asList("aaa", "b", "c", ".", "."),
                Arrays.asList(Compiler.generateRegexGroup("aaa"), Compiler.generateRegexGroup("b?"),
                        Compiler.generateRegexGroup("c*"), Compiler.generateRegexGroup(".?"),
                        Compiler.generateRegexGroup(".*"))
        );

        SlfLogger.info("[END]\n");
    }

    private void testGenerateRegexUnitChain_Helper(int caseNumber, Method generateRegexUnitChain,
            List<String> inSubExpressions, List<String> innerString, List<IRegexGroup> expected) throws Exception {
        SlfLogger.info("+ CASE " + caseNumber);
        SlfLogger.info("  - sub expressions: {}", inSubExpressions.stream().collect(Collectors.joining(", ")));
        ArrayList<String> subExpressions = new ArrayList<>(inSubExpressions);
        IRegexGroup groups = (IRegexGroup) generateRegexUnitChain.invoke(new Compiler(), subExpressions);
        ArrayList<IRegexGroup> dividedChain = new ArrayList<>();
        while(true) {
            dividedChain.add(groups);
            if (groups.isLastUnit()) {
                break;
            }
            groups = groups.getNextUnit();
        }
        assertEquals(dividedChain.size(), expected.size());
        for (int index = 0; index < expected.size() ; index++) {
            assertEquals("A-" + index, subExpressions.get(index), dividedChain.get(index).getRawRegexUnitString());
            assertEquals("B-" + index, innerString.get(index), String.valueOf(expected.get(index).getDividedRegex()));
            assertEquals("C-" + index, expected.get(index).getClass(), dividedChain.get(index).getClass());
        }
    }

    @Test
    public void testGenerateRegexUnit() throws Exception {
        SlfLogger.info("[START] testGenerateRegexGroup");
        Method generateRegexGroup = extractGenerateRegexGroup();

        SlfLogger.info("+ CASE 1");
        SlfLogger.info("  - expression: aaaaa");
        IRegexGroup case1 = (IRegexGroup) generateRegexGroup.invoke(new Compiler(),"aaaaa");
        assertEquals(RegexGroupFixedLength.class, case1.getClass());

        SlfLogger.info("+ CASE 2");
        SlfLogger.info("  - expression: b?");
        IRegexGroup case2 = (IRegexGroup) generateRegexGroup.invoke(new Compiler(),"b?");
        assertEquals(RegexGroupCharAndQ.class, case2.getClass());

        SlfLogger.info("+ CASE 3");
        SlfLogger.info("  - expression: c*");
        IRegexGroup case3 = (IRegexGroup) generateRegexGroup.invoke(new Compiler(),"c*");
        assertEquals(RegexGroupCharAndAsterisk.class, case3.getClass());

        SlfLogger.info("[END]\n");
    }

    //----------------------------------------------------------------------------------------------------

    private Method extractDivideRegexPatternToUnits() throws Exception {
        Class<?> compilerClass = Class.forName("net.number64.open.regexengine.Compiler");
        Method method = compilerClass.getDeclaredMethod("divideRegexPatternToUnits", String.class);
        method.setAccessible(true);
        return method;
    }

    private Method extractGenerateRegexGroupChain() throws Exception {
        Class<?> compilerClass = Class.forName("net.number64.open.regexengine.Compiler");
        Method method = compilerClass.getDeclaredMethod("generateRegexGroupChain", (new ArrayList<String>()).getClass());
        method.setAccessible(true);
        return method;
    }

    private Method extractGenerateRegexGroup() throws Exception {
        Class<?> compilerClass = Class.forName("net.number64.open.regexengine.Compiler");
        Method method = compilerClass.getDeclaredMethod("generateRegexGroup", String.class);
        method.setAccessible(true);
        return method;
    }
}