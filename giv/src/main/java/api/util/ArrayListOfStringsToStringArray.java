package api.util;

import java.util.List;

/**
 * Converts ArrayList of Strings to String Array
 * 
 * @author : Dinesh Bhagwat
 * @version : 1.0
 * @since : 2019-09-09
 *
 */
public class ArrayListOfStringsToStringArray {
  public static String[] Convert(List<String> list) {
    int size = list.size();
    String result[] = new String[size];

    for (int j = 0; j < size; j++) {
      result[j] = list.get(j);
    }
    return result;
  }
}
