package com.BuracosDCApi.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;

@SuppressWarnings("unused")
public class DateHelper {

	public static Timestamp getTimestampFromDate(Date date) {
		return new Timestamp(date.getTime());
	}

	public static Date getDateFromTimestamp(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}

	private static String getStringFromDateAndFormat(Date date, String dateFormat) {
		return new SimpleDateFormat(dateFormat).format(date);
	}

	public static String getDateAsDateString(Date date) {

		return getStringFromDateAndFormat(date, DateFormat.DATE);
	}

	public static String getDateAsDateTimeString(Date date) {

		return getStringFromDateAndFormat(date, DateFormat.DATETIME);
	}

	public static LocalDateTime getLocalDateTimeFromDate(Date date) {

		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	public static Date getDateFromFormattedString(String date, String formato) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
		return getDateFromLocalDateTime(LocalDate.parse(date, formatter).atStartOfDay());
	}

	public static Date getDateFromLocalDateTime(LocalDateTime ldt) {

		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date updateDaysOfDate(Date date, int days) {

		return getDateFromLocalDateTime(getLocalDateTimeFromDate(date).plusDays(days));
	}

	public static Date updateMonthsOfDate(Date date, int months) {

		return getDateFromLocalDateTime(getLocalDateTimeFromDate(date).plusMonths(months));
	}

	public static Date updateYearsOfDate(Date date, int years) {

		return getDateFromLocalDateTime(getLocalDateTimeFromDate(date).plusYears(years));
	}

	public static long getDifferenceDays(Date date1, Date date2) {
	    long diff = date1.getTime() - date2.getTime();
	    long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	    return Math.abs(days);
	}

	public static String getPeriod(Date date1, Date date2, PeriodFormatter periodFormatter){
		Period period = new Period(new DateTime(date1), new DateTime(date2));
		return period.toString(periodFormatter);
	}

	public static Date getHoje() {
		return Calendar.getInstance().getTime();
	}

	public static Date getOntem() {
		return updateDaysOfDate(getHoje(), -1);
	}

	public static Date getAmanha() {
		return updateDaysOfDate(getHoje(), 1);
	}

	public static long getHoursToMinutes(String hora) {
		String[] split = hora.split(":");
		try {
			if (split.length == 0) {
				return 0;
			}
			LocalTime localTime = LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));

			return (localTime.toSecondOfDay()/60);
		} catch (Exception e) {
			return 0;
		}
	}

	public static Timestamp getTimestampAtual() {
		return new Timestamp(Calendar.getInstance().getTime().getTime());
	}

	public static long calcWeekDays(LocalDate start, LocalDate end) {
	    DayOfWeek startW = start.getDayOfWeek();
	    DayOfWeek endW = end.getDayOfWeek();

	    long days = ChronoUnit.DAYS.between(start, end);
	    long daysWithoutWeekends = days - 2 * ((days + startW.getValue())/7);

	    return daysWithoutWeekends + (startW == DayOfWeek.SUNDAY ? 1 : 0) + (endW == DayOfWeek.SUNDAY ? 1 : 0);
	}

	public static long calcWeekendDays(LocalDate start, LocalDate end) {
	    DayOfWeek startW = start.getDayOfWeek();
	    DayOfWeek endW = end.getDayOfWeek();
	    long days = 2 * ((ChronoUnit.DAYS.between(start, end) + startW.getValue() + 1)/7);

	    if(startW == DayOfWeek.SUNDAY) days--;
	    if(endW == DayOfWeek.SATURDAY) days--;

	    return days;
	}
}
