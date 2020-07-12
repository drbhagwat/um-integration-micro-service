package api.external.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import api.external.errors.DateCannotBeBlank;
import api.external.errors.DateIsMandatory;

/**
 * Represents different validations of dates and dates with time
 * 
 * @author Thamilarasi
 * @version 1.0
 * @since 2019-06-24
 *
 */
public class DateParser {
	/*
	 * Given a string, this method checks if it is either null or blank and throws appropriate
	 * exception in either case. Otherwise, it returns the equivalent LocalDate.
	 */
	public static LocalDate validate(String date) throws Exception {
		if (date == null) {
			throw new DateIsMandatory("Date Field in the API is mandatory. Please call the API with a Date Field");
		}

		if (date.isBlank()) {
			throw new DateCannotBeBlank(
					"Date field in the API request cannot be blank. Please call the API with a non-empty Date field.");
		}
		return LocalDate.parse(date);
	}

	/*
	 * Given two dates, this method returns true if the first date comes before or is the same as the second date
	 */
	public static boolean isBeforeOrEqual(LocalDate date1, LocalDate date2) {
		return (date1.isBefore(date2) || date1.isEqual(date2)) ? true : false;
	}

	/*
	 * Given two time stamps, this method returns true if the first timestamp comes before or 
	 * is the same as the second timestamp
	 */
	public static boolean isBeforeOrEqual(LocalDateTime time1, LocalDateTime time2) {
		return (time1.isBefore(time2) || time1.isEqual(time2)) ? true : false;
	}

	/*
	 * Given two dates, this method returns true if the first date comes before the second date.
	 */
	public static boolean isBefore(LocalDate date1, LocalDate date2) {
		return date1.isBefore(date2) ? true: false;
	}

	/*
	 * Given two dates, this method returns true if the first timestamp comes before the second timestamp.
	 */
	public static boolean isBefore(LocalDateTime time1, LocalDateTime time2) {
		return time1.isBefore(time2) ? true: false;
	}
	
	/*
	 * Given a startDate, a dateTime stamp, and an endDate, this method checks if the dateTime stamp lies 
	 * in-between the beginning of the day (of the startDate) and end of the day (of the endDate) - both inclusive. 
	 * It returns true if that is the case, or false otherwise.
	 */
	public static boolean isWithinStartAndEndDates(LocalDate startDate, LocalDateTime dateTime, LocalDate endDate) {
		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

		if ((startDateTime.isBefore(dateTime) || startDateTime.isEqual(dateTime))
				&& (dateTime.isBefore(endDateTime) || dateTime.isEqual(endDateTime))) {
			return true;
		} else {
			return false;
		}
	}
}