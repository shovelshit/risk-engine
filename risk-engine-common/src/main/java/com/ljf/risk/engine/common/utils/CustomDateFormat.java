package com.ljf.risk.engine.common.utils;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lijinfeng
 */
public class CustomDateFormat extends DateFormat {

    private final DateFormat dateFormat;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CustomDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return format.format(date, toAppendTo, fieldPosition);
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        Date date;
        try {
            date = dateFormat.parse(source, pos);
        } catch (Exception e) {
            date = format.parse(source, pos);
        }
        return date;
    }

    @Override
    public Date parse(String source) throws ParseException {
        Date date;
        try {
            // 默认规则
            date = dateFormat.parse(source);
        } catch (Exception e) {
            // 自定义规则
            date = format.parse(source);
        }
        return date;
    }

    @Override
    public Object clone() {
        Object format = dateFormat.clone();
        return new CustomDateFormat((DateFormat) format);
    }


}
