package net.number64.open.regexengine.regexgroup;

public class RegexGroupCharAndQ extends AbstractRegexGroup {

    public RegexGroupCharAndQ(String rawRegexUnit) {
        super(rawRegexUnit);
        if (rawRegexUnit.length() != 2 || ! rawRegexUnit.endsWith(SINGLE_STRING_POINT_Q_MARK)) {
            throw new IllegalArgumentException("Invalid regex sub expression:" + rawRegexUnit);
        }
        // Type CharAndQ needs one regex char, and needs 0 or 1 matching (zero is allowed).
        dividedRegex = (rawRegexUnitString.substring(0, 1)).toCharArray();
        necessaryRepetitionCount = 0;
    }

    @Override
    protected int getMaxMatchableCount(String targetString, int baseIndex) {
        // Type CharAndQ behaves greedy. So result is 1.
        return 1;
    }

    @Override
    protected boolean canMatchOutOfBounds() {
        // Question allows 0 size matching. So result is true.
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
