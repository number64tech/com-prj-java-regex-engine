package net.number64.common.regexengine.regexgroup;

public interface IRegexGroup {

    int RESULT_NUM_FOR_MATCHER_NOT_MATCHED = -1;

    //int UNICODE_CODE_POINT_SMALL_A = 0x0061;
    //int UNICODE_CODE_POINT_SMALL_Z = 0x007A;
    int UNICODE_CODE_POINT_FULL_STOP = 0x002E;
    int UNICODE_CODE_POINT_Q_MARK = 0x003F;
    int UNICODE_CODE_POINT_ASTERISK = 0x002A;

    //String SINGLE_STRING_FULL_STOP = ".";
    String SINGLE_STRING_POINT_Q_MARK = "?";
    String SINGLE_STRING_POINT_ASTERISK = "*";

    int getNecessaryRepetitionCount();

    String getRawRegexUnitString();

    char[] getDividedRegex();

    int compareAndCount(String targetString, int baseIndex);

    boolean isMatchingFirstChar(char compareTarget);

    boolean isLastUnit();

    void setNextUnit(IRegexGroup nextUnit);

    IRegexGroup getNextUnit();

    int getFixedLength();

}
