package com.obss.pokedex.library.util;

import java.util.Date;

public class DateUtil {
    private DateUtil() {
    }

    public static String dateToLocalDateString(Date date) {
        return date != null ? new java.sql.Date(date.getTime()).toLocalDate().toString() : null;
    }
}
