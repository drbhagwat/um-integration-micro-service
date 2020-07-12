package api.external.util;

import org.springframework.stereotype.Component;

/**
 * Converts JSON parameter to equivalent db parameter
 * 
 * @author Dinesh Bhagwat
 * @version 1.0
 * @since 2019-08-09
 *
 */
@Component
public class Converter {
  public static String toDatabaseColumnName(String str) {
    String convertedString = str.replaceAll("(.)([A-Z])", "$1_$2");
    return (convertedString.toLowerCase());
  }
}
