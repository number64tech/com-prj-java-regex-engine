package net.number64.common.regexengine;

import net.number64.common.regexengine.regexgroup.RegexGroupCharAndAsterisk;
import net.number64.common.regexengine.regexgroup.RegexGroupCharAndQ;
import net.number64.common.regexengine.regexgroup.RegexGroupFixedLength;
import net.number64.common.regexengine.regexgroup.IRegexGroup;

import java.util.ArrayList;

/**
 *
 */
public class Compiler {
    // TODO: comment

    /**
     * This method generates 'regex sub expressions' from specified regex expression.
     * And .
     * @param regexPattern regex string
     * @return regex units (compiled and chained)
     */
    public static IRegexGroup compile(String regexPattern) {
        // divide and convert
        return generateRegexGroupChain(divideRegexPatternToUnits(regexPattern));
    }

    /**
     * This method divides 'regex pattern' following rules below.
     * 1. [a-z]
     * 2.
     * 3.
     * @param regexPattern regex string
     * @return list of divided regex-strings
     */
    private static ArrayList<String> divideRegexPatternToUnits(String regexPattern) {
        ArrayList<String> unitList = new ArrayList<>();
        // index (starts from 0)
        int index = 0;
        int maxIndex = regexPattern.length() - 1;
        StringBuffer unit = new StringBuffer();
        while (true) {
            int targetCodePoint = regexPattern.codePointAt(index);
            if (index == maxIndex) {
                unit.appendCodePoint(targetCodePoint);
                break;
            }
            index++;
            int nextCodePoint = regexPattern.codePointAt(index);
            if (nextCodePoint == IRegexGroup.UNICODE_CODE_POINT_ASTERISK ||
                    nextCodePoint == IRegexGroup.UNICODE_CODE_POINT_Q_MARK){
                // avoid to add blank unit, it will happen in case of 2nd char are "*" or "?"
                if (unit.length() != 0) {
                    unitList.add(unit.toString());
                    unit = new StringBuffer();
                }
            }
            unit.appendCodePoint(targetCodePoint);
            if (targetCodePoint == IRegexGroup.UNICODE_CODE_POINT_ASTERISK ||
                    targetCodePoint == IRegexGroup.UNICODE_CODE_POINT_Q_MARK){
                unitList.add(unit.toString());
                unit = new StringBuffer();
            }
        }
        // add last unit to result
        unitList.add(unit.toString());
        return unitList;
    }

    /**
     *
     * @param subExpressions regex string
     * @return regex units (compiled and chained)
     */
    private static IRegexGroup generateRegexGroupChain(ArrayList<String> subExpressions) {
        IRegexGroup regexUnitChain = null;
        IRegexGroup prevUnit = null;

        //
        for (String regexSubExpression : subExpressions) {
            // compile
            IRegexGroup thisUnit = generateRegexGroup(regexSubExpression);
            // joint thisUnit and prevUnit to make chained object
            if (prevUnit == null) {
                regexUnitChain = thisUnit;
            } else {
                prevUnit.setNextUnit(thisUnit);
            }
            prevUnit = thisUnit;
        }

        return regexUnitChain;
    }

    /**
     *
     * @param regexSubExpression regex string (divided into group)
     * @return a regex group
     */
    public static IRegexGroup generateRegexGroup(String regexSubExpression) {
        if (regexSubExpression.endsWith(IRegexGroup.SINGLE_STRING_POINT_ASTERISK)) {
            return new RegexGroupCharAndAsterisk(regexSubExpression);
        } else if (regexSubExpression.endsWith(IRegexGroup.SINGLE_STRING_POINT_Q_MARK)) {
            return new RegexGroupCharAndQ(regexSubExpression);
        }
        return new RegexGroupFixedLength(regexSubExpression);
    }
}