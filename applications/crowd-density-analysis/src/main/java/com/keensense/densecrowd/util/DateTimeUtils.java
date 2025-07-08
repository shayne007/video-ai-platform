package com.keensense.densecrowd.util;

import com.keensense.common.util.DateTime;

import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.*;
import java.util.regex.*;

/**
 * 时间日期帮助类
 * @author Administrator
 *
 */
public class DateTimeUtils {

	public static final String DEFAULT_FORMAT_DATE_WITHOUT_TIME = "yyyy-MM-dd";
	public static final String DEFAULT_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";
	public static final Pattern DATE_PATTERN = Pattern.compile("^(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])([-/.]?)(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])([-/.]?)(?:29|30)|(?:0?[13578]|1[02])([-/.]?)31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2([-/.]?)29)$");

	/**
	 * 校验时间格式
	 * @param dateText
	 * @return
	 */
	public static boolean validateDateFormat(String dateText){
		return DATE_PATTERN.matcher(dateText).matches();
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static Timestamp now(){
		return new Timestamp(System.currentTimeMillis());
	}
	
	
	public static Date resetTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 比较两个时间，如果endtime晚于begintime，则返回true，否则返回false
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static boolean compareTime(String beginTime, String endTime) {
		long begin = DateTime.dateTimeParse(beginTime, DEFAULT_FORMAT_DATE);
		long end = DateTime.dateTimeParse(endTime, DEFAULT_FORMAT_DATE);
		if (end > begin) {
			return true;
		}
		return false;
	}
	
	/**
	 * 比较两个时间，如果endtime晚于begintime，则返回true，否则返回false
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static boolean compareTime(String beginTime, String endTime,String dateformat) {
		long begin = DateTime.dateTimeParse(beginTime, dateformat);
		long end = DateTime.dateTimeParse(endTime, dateformat);
		if (end > begin) {
			return true;
		}
		return false;
	}

	/**
	 * date转string
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static String formatDate(Date date, String formatStr) {
		return (new SimpleDateFormat((formatStr == null ? DEFAULT_FORMAT_DATE
				: formatStr))).format(date);
	}

	   /**
	    * Date转String
     *  date String
     * @param dateStr
     * @param formatStr
     * @return
     * @throws ParseException
     */
    public static String dateFormatString(Date date){
        if(null == date)
        {
            return null;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_DATE);  
        String  str = sdf.format(date);  
        
        return str;
    }
    
    
	/**
	 *  string转date
	 * @param dateStr
	 * @param formatStr
	 * @return
	 * @throws ParseException
	 */
	public static Date formatDate(String dateStr, String formatStr){
		try{
			return (new SimpleDateFormat((formatStr == null ? DEFAULT_FORMAT_DATE
					: formatStr))).parse(dateStr);
		}catch(ParseException e){
			System.err.println(e);
			return null;
		}
		
	}
	
	
	   /**
     * @return 把毫秒时间转换成日期类型
     */
	public static String timeToDefaultDate(Long mms)
	{
	     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     return sdf.format(mms);
	}
	
	/**
	 * 获取当填00:00:00 时间
	 */
	public static String getBeginDateTimeStr()
	{
	    Calendar currentDate = new GregorianCalendar();   
	    currentDate.set(Calendar.HOUR_OF_DAY, 0);  
	    currentDate.set(Calendar.MINUTE, 0);  
	    currentDate.set(Calendar.SECOND, 0);  
	    Long time = currentDate.getTime().getTime();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return sdf.format(time);
	}

	  /**
     * 获取当填00:00:00 时间
     */
    public static Date getBeginDateTime()
    {
        Calendar currentDate = new GregorianCalendar();   
        currentDate.set(Calendar.HOUR_OF_DAY, 0);  
        currentDate.set(Calendar.MINUTE, 0);  
        currentDate.set(Calendar.SECOND, 0);  
        return currentDate.getTime();
    }
    
    /** 
    *  字符串的日期格式的计算，两日期相差的天数
     * @throws ParseException 
    */  
    public static int daysBetween(String smdate,String bdate)
    {   if (StringUtils.isNotEmptyString(smdate) && StringUtils.isNotEmptyString(bdate))
        {
            SimpleDateFormat sdf=new SimpleDateFormat(DEFAULT_FORMAT_DATE);  
            Calendar cal = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();  
            
            try
            {
                cal.setTime(sdf.parse(smdate));
                cal2.setTime(sdf.parse(bdate));
            }
            catch (ParseException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }    
            long time1 = cal.getTimeInMillis();
            long time2 = cal2.getTimeInMillis(); 
            long between_days=(time2-time1)/(1000*3600*24);
            return Integer.parseInt(String.valueOf(between_days));
        }
        else
        {
            return -1;
        }
      
    }  

    /** 
    *  字符串的日期格式的计算，两日期相差的天数
     * @throws ParseException 
    */  
    public static int daysBetween2(String smdate,String bdate,String format)
    {   
        if (StringUtils.isNotEmptyString(smdate) && StringUtils.isNotEmptyString(bdate))
        {
            SimpleDateFormat sdf = null ;
            if(StringUtils.isEmptyString(format))
            {
                sdf =new SimpleDateFormat(DEFAULT_FORMAT_DATE);  
            }
            else
            {
                sdf =new SimpleDateFormat(format);  
            }
             
            Calendar cal = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();  
            
            try
            {
                cal.setTime(sdf.parse(smdate));
                cal2.setTime(sdf.parse(bdate));
            }
            catch (ParseException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }    
            long time1 = cal.getTimeInMillis();
            long time2 = cal2.getTimeInMillis(); 
            long between_days=(time2-time1)/(1000*3600*24);
            return Integer.parseInt(String.valueOf(between_days));
        }
        else
        {
            return -1;
        }
      
    } 
    
    /**
     * 获取前days天的日期
     * @param days
     * @return
     */
    public static Date getBeforeDaysDateTime(int days){

            Date dNow = new Date();   //当前时间
            Date dBefore = new Date();
            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(dNow);//把当前时间赋给日历
            calendar.add(Calendar.DAY_OF_MONTH, -days);  //设置为前一天
            dBefore = calendar.getTime();   //得到前一天的时间
            
/*            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
            String defaultStartDate = sdf.format(dBefore);    //格式化前一天
            String defaultEndDate = sdf.format(dNow); //格式化当前时间
*/            return dBefore;

    }
	/**
	 * 秒转成 HH:MM:SS
	 * @param timeS
	 * @return
	 */
	public static String formateTime(int timeS)
	{
	     int hh = timeS/3600;
	     int mm = (timeS - hh*3600)/60;
	     int ss = timeS - mm * 60 - hh*3600;
	     
	     String hhStr = String.valueOf(hh);
	     String mmStr = String.valueOf(mm);
	     String ssStr = String.valueOf(ss);
	     if (hh < 10)
	     {
	         hhStr = "0" + hh;
	     }
	     
	     if (mm < 10)
	    {
	         mmStr = "0" + mm;
	    }
	     
	     if (ss < 10)
	    {
	         ssStr = "0" + ss;
	    }
	     
	     return hhStr + ":" + mmStr + ":" + ssStr;
	    
	}
	
	/**
     * 判断输入的字符串是否满足时间格式 ： yyyy-MM-dd HH:mm:ss
     * @param patternString 需要验证的字符串
     * @return 合法返回 true ; 不合法返回false
     */
    public static boolean isTimeLegal(String patternString) {
           
    	Pattern a=Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$"); 
    
        Matcher b=a.matcher(patternString); 
        if(b.matches() && patternString.length()==19) {
              return true;
         } else {
               return false;
         }
    }
    
    public static Date getSomeDay(Date date, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }
    
    public static String getSomeDayYMD(Date date, int day){
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return formatDate.format(calendar.getTime());
    }
    
    public static long getIntervalSecond(long totalSecond,int count,long maxSecond){
    	long second = totalSecond/count;
    	int count1 = count;
    	if(second < maxSecond){
    		count1 = count1 - 1;
    		if(count <= 1){
    			return totalSecond;
    		}else{
    			getIntervalSecond(totalSecond,count1,maxSecond);
    		}
    	}
    	return second;
    }
    
    public static String getTomorrow(String today) {
    	Date date = null;
		try {
			date = formatDate(today,null);
	        Calendar   calendar   =   new   GregorianCalendar(); 
	        calendar.setTime(date); 
	        //calendar.add(calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
	        //calendar.add(calendar.DAY_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
	        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
	        //calendar.add(calendar.WEEK_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
	        date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return formatDate(date,null);
    }
    
    /**
     * 获取当前时间字符串
     * 
     * @param args
     */
    public static String getNowTime() {
        return formatDate(new Date(), null);
    }
    
    public static void main(String[] args) {
    	
		System.out.println(getSomeDayYMD(new Date(),1));
	}

}
