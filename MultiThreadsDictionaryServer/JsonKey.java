/*
 *    The University of Melbourne
 *    School of Computing and Information Systems
 *    COMP90015 Distributed System
 *    Semester 01/2020
 *
 *    Author: QUANG TRUNG LE
 */


/**
 * Define JSON Keywords (JSON Objects).
 */
enum JsonKey{
    COMMAND("command"),
    VOCABULARY("vocabulary"),
    MEANING("meaning"),
    ANNOUNCEMENT("announcement");

    private String value;
    JsonKey(String keywordString){
        this.value = keywordString;
    }

    /**
     * @return The value of Json Keyword enum
     */
    public String value(){
        return value;
    }
}