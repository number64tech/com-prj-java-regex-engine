package net.number64.common.regexengine.regexgroup;

public class RegexGroupCharAndAsterisk extends AbstractRegexGroup {

    public RegexGroupCharAndAsterisk(String rawRegexUnit) {
        super(rawRegexUnit);
        if (rawRegexUnit.length() != 2 || ! rawRegexUnit.endsWith(SINGLE_STRING_POINT_ASTERISK)) {
            throw new IllegalArgumentException("Invalid regex sub expression:" + rawRegexUnit);
        }
        // Type CharAndAsterisk needs one regex char, and needs 0 or more matching (zero is allowed).
        dividedRegex = (rawRegexUnitString.substring(0, 1)).toCharArray();
        necessaryRepetitionCount = 0;
    }

    @Override
    protected int getMaxMatchableCount(String targetString, int baseIndex) {
        // Asterisk behaves greedy. result is length 'to the end'
        int targetStringRemainingLength = targetString.length() - baseIndex;
        return (targetStringRemainingLength / dividedRegex.length);
    }

    @Override
    protected boolean canMatchOutOfBounds() {
        // Asterisk allows 0 size matching. So result is true.
        return true;
    }

    /**
     * If this unit is first group in the chain, this method will be used to decide index to start.
     * NOTE: Quantifier 'Question' can match 0-size, so this method always returns true.
     * @param compareTarget
     * @return whether the first pattern matches inputted char
     */
    @Override
    public boolean isMatchingFirstChar(char compareTarget) {
        return true;
    }

    /**
     *
     * @return summary of fixed length children have. (this is not fixed-type, so 0 anyway)
     */
    @Override
    public int getFixedLength() {
        return (isLastUnit() ? 0 : getNextUnit().getFixedLength());  // + 0
    }
}
