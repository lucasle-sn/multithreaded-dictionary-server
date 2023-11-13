/*
 *    The University of Melbourne
 *    School of Computing and Information Systems
 *    COMP90015 Distributed System
 *    Semester 01/2020
 *
 *    Author: QUANG TRUNG LE - 987445
 */



/**
 * Define types of command.
 */
public enum CmdType {
    SEARCH_VOCAB("search",2,3),
    ADD_VOCAB("add",3,3),
    REMOVE_VOCAB("remove",2,3);

    private String value;
    private int minLength, maxLength;

    CmdType(String commandString, int minLength, int maxLength){
        this.value = commandString;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    /**
     * @return The value of CommandType enum
     */
    public String value(){
        return value;
    }

    /**
     * @return The minimum number of arguments for the equivalent command Type.
     */
    public int getMinLength() {
        return minLength;
    }

    /**
     * @return The maximum number of arguments for the equivalent command Type.
     */
    public int getMaxLength() {
        return maxLength;
    }
}
