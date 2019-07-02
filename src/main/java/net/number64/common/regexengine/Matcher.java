package net.number64.common.regexengine;

import net.number64.common.regexengine.regexgroup.IRegexGroup;

public class Matcher {

    public static int countLongestMatchingChars(String targetString, String regexPattern) {

        // result
        int longestMatchingLength = IRegexGroup.RESULT_NUM_FOR_MATCHER_NOT_MATCHED;

        //
        IRegexGroup compiledRegexUnitChain = Compiler.compile(regexPattern);

        // LIGHT-WEIGHT : if the regex's total FIXED length is over the target's length, through for-loop immediately
        //   and the length will be used to calc limit of starting index
        int theoreticalShortestLength = compiledRegexUnitChain.getFixedLength();
        int limitedStartIndex = (theoreticalShortestLength == 0 ?
                targetString.length() : targetString.length() - theoreticalShortestLength + 1);

        for (int startIndex = 0 ; startIndex < limitedStartIndex ; startIndex++) {
            // can start from this index?
            if (compiledRegexUnitChain.isMatchingFirstChar(targetString.charAt(startIndex))) {
                // TODO: should be multi-thread operating
                int result = startCompare(compiledRegexUnitChain, targetString, startIndex);
                if (longestMatchingLength < result) {
                    longestMatchingLength = result;
                }
                // LIGHT-WEIGHT: if the result length is as same as targetString length, it means "Finish!".
                //   this will work in case of the pattern is like "ab.*cd" and target is very long.
                if (longestMatchingLength == targetString.length()) {
                    break;
                }
            }
        }

        return longestMatchingLength;
    }

    /**
     * This method may be multiple thread execution.
     * @param compiledRegexUnitChain
     * @param targetString
     * @param startIndex
     * @return matching length or NOT_MATCHED (from here)
     */
    private static int startCompare(IRegexGroup compiledRegexUnitChain, String targetString, int startIndex) {
        return compiledRegexUnitChain.compareAndCount(targetString, startIndex) ;
    }

}
