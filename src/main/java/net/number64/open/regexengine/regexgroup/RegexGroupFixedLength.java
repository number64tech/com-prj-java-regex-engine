package net.number64.open.regexengine.regexgroup;

public class RegexGroupFixedLength extends AbstractRegexGroup {

    public RegexGroupFixedLength(String rawRegexUnit) {
        super(rawRegexUnit);
        if (rawRegexUnit.contains(SINGLE_STRING_POINT_ASTERISK) ||
            rawRegexUnit.contains(SINGLE_STRING_POINT_Q_MARK)) {
            throw new IllegalArgumentException("Invalid regex sub expression:" + rawRegexUnit);
        }
        // Type FixedLength needs one matching (not zero).
        dividedRegex = rawRegexUnitString.toCharArray();
        necessaryRepetitionCount = 1;
    }

    @Override
    protected int getMaxMatchableCount(String targetString, int baseIndex) {
        // Type FixedLength needs one reputation.
        return 1;
    }

    @Override
    protected boolean canMatchOutOfBounds() {
        // FixedLength is not allowed 0 size matching. So result is false.
        return false;
    }

    /**
     * If this unit is first group in the chain, this method will be used to decide index to start.
     * @param compareTarget
     * @return whether the first pattern matches inputted char
     */
    @Override
    public boolean isMatchingFirstChar(char compareTarget) {
        // Asterisk and Question never appear as first pattern
        if (dividedRegex[0] == UNICODE_CODE_POINT_FULL_STOP) {
            return true;
        }
        return (dividedRegex[0] == compareTarget);
    }

    /**
     *
     * @return summary of fixed length, children and this have.
     */
    @Override
    public int getFixedLength() {
        int myFixedLength = dividedRegex.length;
        return (isLastUnit() ? myFixedLength : getNextUnit().getFixedLength() + myFixedLength);
    }
}
