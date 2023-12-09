/*
 *    The University of Melbourne
 *    School of Computing and Information Systems
 *    COMP90015 Distributed System
 *    Semester 01/2020
 *
 *    Author: QUANG TRUNG LE
 */

import org.json.JSONObject;

public interface DictionaryService {
    public JSONObject searching(String vocabulary);
    public JSONObject adding (String vocabulary, String meaning);
    public JSONObject removing (String vocabulary);
}
