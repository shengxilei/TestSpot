package com.jianzhi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCoreUtil {
	
	/** * 日期转化为cron表达式 * @param date * @return */
    public static String getCron(Date  date){
        String dateFormat="ss mm HH dd MM ?";  
        Calendar newTime = Calendar.getInstance();
        newTime.setTime(date);
        
        newTime.add(Calendar.SECOND,-10);//日期减30秒
        Date dt1=newTime.getTime();
        return  DateCoreUtil.fmtDateToStr(dt1, dateFormat);  
    }  

    /** * cron表达式转为日期 * @param cron * @return */
    public static Date getCronToDate(String cron) {  
        String dateFormat="ss mm HH dd MM ?";  
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);  
        Date date = null;  
        try {  
            date = sdf.parse(cron);  
        } catch (ParseException e) {  
            return null;
        }  
        return date;  
    }  

    /** * Description:格式化日期,String字符串转化为Date * * @param date * @param dtFormat * 例如:yyyy-MM-dd HH:mm:ss yyyyMMdd * @return */  
    public static String fmtDateToStr(Date date, String dtFormat) {  
        if (date == null)  
            return "";  
        try {  
            SimpleDateFormat dateFormat = new SimpleDateFormat(dtFormat);  
            return dateFormat.format(date);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return "";  
        }  
    } 

}
