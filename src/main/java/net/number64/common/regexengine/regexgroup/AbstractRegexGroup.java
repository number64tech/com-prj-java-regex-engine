package net.number64.common.regexengine.regexgroup;

public abstract class AbstractRegexGroup implements IRegexGroup {

    protected String rawRegexUnitString;
    protected char[] dividedRegex;
    protected int necessaryRepetitionCount = -1;
    private IRegexGroup nextUnit = null;

    public AbstractRegexGroup(String rawRegexUnit) {
        this.rawRegexUnitString = rawRegexUnit;
    }

    /**
     * This method counts matching length by using unique method implemented in child class.
     * And will check result. If the result is OK, the next unit will be called to compare forward.
     *
     * If comparision just fail, this return immediately.
     * @param targetString
     * @param baseIndex
     * @return matched length or
     */
    @Override
    public int compareAndCount(String targetString, int baseIndex) {
        if (necessaryRepetitionCount == -1) {
            throw new RuntimeException("necessaryMatchingCount == -1");
        }

        // call unique method implemented in child class
        int maxRepetitionCount = compareAndGetMaxRepetitionCount(targetString, baseIndex);

        // NOT_MATCHED? return immediately
        if (maxRepetitionCount == RESULT_NUM_FOR_MATCHER_NOT_MATCHED) {
            return RESULT_NUM_FOR_MATCHER_NOT_MATCHED;
        }

        // if this is the last unit, it means that matching had finished successfully!
        if (isLastUnit()) {
            return (maxRepetitionCount * dividedRegex.length);
        }

        // the journey have continued
        //   .. parent should watch over the journey of its children

        // return summary of length gained by this and children.
        return adjustStartIndexAndCallNextUnit(targetString, baseIndex, maxRepetitionCount);
    }

    /**
     * Each regex group has 'necessary repetition-count' (Fixed=1, ? and * = 0)
     * @param targetString
     * @param baseIndex
     * @return
     */
    private int compareAndGetMaxRepetitionCount(String targetString, int baseIndex) {
        // in case of base-index is out of bounds, only "0-size matchable" type is permitted.
        if (targetString.length() == baseIndex) {
            return canMatchOutOfBounds() ? 0 : RESULT_NUM_FOR_MATCHER_NOT_MATCHED;
        }
        // in case of base-index + regex length is out of bounds, fail immediately.
        if (targetString.length() < baseIndex + dividedRegex.length) {
            return RESULT_NUM_FOR_MATCHER_NOT_MATCHED;
        }

        // try matching at specified count
        int repetitionCount;
        int maxMatchableCount = getMaxMatchableCount(targetString, baseIndex);
        //
        for (repetitionCount = 0 ; repetitionCount < maxMatchableCount ; repetitionCount++) {
            int patternIndex;
            for (patternIndex = 0 ; patternIndex < dividedRegex.length ; patternIndex++) {
                // [.] matches always. [a-z] require same char.
                if (dividedRegex[patternIndex] != UNICODE_CODE_POINT_FULL_STOP &&
                        dividedRegex[patternIndex] != targetString.charAt(baseIndex + patternIndex)) {
                    break;
                }
            }
            //
            if (patternIndex != dividedRegex.length) {
                // failed
                if (repetitionCount < necessaryRepetitionCount) {
                    return RESULT_NUM_FOR_MATCHER_NOT_MATCHED;
                }
                break;
            }
            // exceeding
            baseIndex = baseIndex + patternIndex;
        }

        return repetitionCount;
    }

    /**
     *
     * @param targetString
     * @param baseIndex
     * @param maxRepetitionCount
     * @return
     */
    private int adjustStartIndexAndCallNextUnit(String targetString, int baseIndex, int maxRepetitionCount) {
        for (int repetitionCount = maxRepetitionCount ; repetitionCount >= necessaryRepetitionCount ; repetitionCount--) {
            int offset = repetitionCount * dividedRegex.length;
            int childBaseIndex = baseIndex + offset;
            int childrenResult = getNextUnit().compareAndCount(targetString, childBaseIndex);
            if (childrenResult != RESULT_NUM_FOR_MATCHER_NOT_MATCHED) {
                return childrenResult + offset;
            }
        }
        return RESULT_NUM_FOR_MATCHER_NOT_MATCHED;
    }

    /**
     *
     * @return How many times can this unit repeat matching on the calculation?
     */
    abstract protected int getMaxMatchableCount(String targetString, int baseIndex);

    /**
     *
     * @return Can this unit match at out of bounds? (= whether 0-size matching is permitted)
     */
    abstract protected boolean canMatchOutOfBounds();

    /**
     * If this unit doesn't have an next Unit, it means this unit is last.
     * @return is this unit last?
     */
    @Override
    public boolean isLastUnit() {
        return (nextUnit == null);
    }

    /**
     * Create unit-chain.
     * @param nextUnit
     */
    @Override
    public void setNextUnit(IRegexGroup nextUnit) {
        this.nextUnit = nextUnit;
    }

    /**
     * Return chained next unit.
     * NOTE: It must be checked that whether this unit is last or not before calling this method.
     * @return nextUnit
     */
    @Override
    public IRegexGroup getNextUnit() {
        if (nextUnit == null) {
            throw new RuntimeException("Must check 'isLastUnit?' before !!");
        }
        return nextUnit;
    }

    // for Unit tests

    @Override
    public int getNecessaryRepetitionCount() {
        return this.necessaryRepetitionCount;
    }

    @Override
    public String getRawRegexUnitString() {
        return rawRegexUnitString;
    }

    @Override
    public char[] getDividedRegex() {
        return dividedRegex.clone();
    }
}
