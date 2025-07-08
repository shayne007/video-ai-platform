package com.keensense.densecrowd.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.*;
import javax.imageio.stream.*;
import javax.servlet.http.*;
import java.io.*;
import java.math.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;


/**
 * 字符串处理类
 * @author YANGXQ
 * @since 2016/1/17/16:20
 *
 */
@Slf4j
public class StringUtils {
	
	/***
	 * 随机产生32位16进制字符串
	 * @return
	 */
	public static String getRandom32PK(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	} 
	/**
	 * 获取随机数
	 * 
	 * @return
	 */
	public static String getRandom6Number(int size) {
		if (size <= 0)
			size = 6;// 默认6位
		String randString = "0123456789";// 随机产生的字符串
		Random random = new Random();// 随机种子
		String rst = "";// 返回值
		for (int i = 0; i < size; i++) {
			rst += randString.charAt(random.nextInt(6));
		}
		return rst;
	}
	
//	public static String getYMD000_() {
//		return DateUtil.getFormat(DateUtil.getDate(null), DateFormatConst.YMD_) + " 00:00:00";
//	}
//
//	public static String getYMDHMS_() {
//		return DateUtil.getFormat(new Date(), DateFormatConst.YMDHMS_);
//	}
//
	
	/**
	 * 替换字符串中的逗号
	 * @param str
	 * @return
	 */
	public static String replaceStr(String str){
		if(str==null||"".equals(str)){
			str="0";
		}
		if(str.contains(",")){
			str = str.replace(",", "");
		}
		return str;
	}
	
	 /**
     * 得到一个字符在另一个字符串中出现的次数
     * 
     * @param text
     *            文本
     * @param ch
     *            字符
     */
    public static int getIndexOfCount(String text, char ch) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            count = (text.charAt(i) == ch) ? count + 1 : count;
        }
        return count;
    }
	
	/**
	 * 判断字符串是否为空或者空字符串
	 * @param str
	 * @return
	 */
	public static boolean isNotEmptyString(String str){
	    
	    if (str != null && !"".equals(str.trim()) && !"null".equals(str))
	    {
	        return true;
	    }
	    
	    return false;
	}
	
	/**
	 * 判断字符串是否为空或者空字符串
	 * @param str
	 * @return
	 */
	public static boolean isEmptyString(String str){
		return !isNotEmptyString(str);
	}
	
	/**
	 * 判断str是否为null或空字符串，若是，则返回空字符串，否则返回str.trim()。
	 * @param str
	 * @return
	 */
	public static String objToStr(String str){
		if(str == null || "".equals(str)){
			return "";
		}else{
			return str.trim();
		}
	}
	
	/**
	 * 替换用科学计算法显示的数据
	 * @param str
	 * @return
	 */
	public static String repStr(String str){
		BigDecimal bigDecimal = new BigDecimal(str);
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(bigDecimal);
	}
	
	/**
	 * 字符串转换成金额的一般表示方法
	 * @param str
	 * @return
	 */
	public static String repAmount(String str){
		BigDecimal bigDecimal1 = new BigDecimal(str);
		BigDecimal bigDecimal2 = new BigDecimal(10000);
//		DecimalFormat df = new DecimalFormat("###,###,###0.00");
		bigDecimal1=bigDecimal1.divide(bigDecimal2);
//		return df.format(bigDecimal1);
		return bigDecimal1.toString();
	}
	
	/**
	 * 金额相加
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String addAmount(String str1,String str2){
		if(isNotEmptyString(str1)&&str1.contains(",")){
			str1 = str1.replaceAll(",", "");
		}
		if(isNotEmptyString(str2)&&str2.contains(",")){
			str2 = str2.replaceAll(",", "");
		}
		BigDecimal bigDecimal1 = new BigDecimal(str1);
		BigDecimal bigDecimal2 = new BigDecimal(str2);
		bigDecimal1=bigDecimal1.add(bigDecimal2);
		return bigDecimal1.toString();
	}
	
	
	/**
	 * 金额相除并转换成百分比形式
	 * @param str1
	 * @param str2
	 * @param scale
	 * @return
	 */
	public static String divAmount(String str1,String str2,int scale){
		BigDecimal bigDecimal1 = new BigDecimal(str1);
		BigDecimal bigDecimal2 = new BigDecimal(str2);
		double d = (bigDecimal1.divide(bigDecimal2,scale,BigDecimal.ROUND_HALF_UP).doubleValue());
//		NumberFormat nFromat = NumberFormat.getPercentInstance();
//		String rates = nFromat.format(d);
		DecimalFormat df = new DecimalFormat("0.00%");
		String r="";
		if(isNotEmptyString(d+"")){
			r = df.format(d);
		}
		return isNotEmptyString(r)?r:"0.00%";
	}
	
	/**
	 * 金额的差额
	 * @param str
	 * @return
	 */
	public static String getMargin(String str1,String str2){
		if(isNotEmptyString(str1)&&str1.contains(",")){
			str1 = str1.replaceAll(",", "");
		}
		if(isNotEmptyString(str2)&&str2.contains(",")){
			str2 = str2.replaceAll(",", "");
		}
		BigDecimal bigDecimal1 = new BigDecimal(str1);
		BigDecimal bigDecimal2 = new BigDecimal(str2);
//		DecimalFormat df = new DecimalFormat("###,###,###0.00");
		bigDecimal1=bigDecimal1.subtract(bigDecimal2);
//		return df.format(bigDecimal1);
		return bigDecimal1.toString();
	}
	
	/**
	 * 把金额用千分位显示
	 * @param str
	 * @return
	 */
	public static String showAmount(String str){
		BigDecimal bigDecimal1 = new BigDecimal(str);
		DecimalFormat df = new DecimalFormat("###,###,###,###.00");
		return df.format(bigDecimal1);
	}
	
	/**
	 * 正则表达式获取连个值之间的值
	 * @param b
	 * @param e
	 * @param c
	 * @return
	 */
	public static String getRankVal(String b, String e, String c){
		log.info("getRankVal b :=>" + b);
		log.info("getRankVal e :=>" + e);
		log.info("getRankVal c :=>" + c);
		
		Pattern psrjc = Pattern.compile("(?<=" + b + "=)[\\w|-]+(?=" + e + ")");
	    Matcher msrjc = psrjc.matcher(c);
	    if(msrjc.find()){
	    	log.info("getRankVal msrjc.find() true ");
	    	String srjc =  msrjc.group(0);
	    	log.info("getRankVal srjc :=> " + srjc);
	        return srjc.trim();
	    }
	    return "";
	}
	
	/**
	 * 匹配中文
	 * @param str
	 * @return
	 */
	public static boolean regZh(String str){
		String reg = "[\u4E00-\u9FA5]+";
		Pattern p = Pattern.compile(reg); 
		Matcher m = p.matcher(str);
		if(m.find()){
			return true;
		}
		return false;	
	}
	/**
	 * 字符串转成UTF-8
	 * @param str
	 * @return
	 */
	public static String regZh2utf_8(String str){
		if(regZh(str))
		{
			 try {
				return new String(str.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		return str;
	}
	/**
	 * 字符串数组 转 字符串 
	 * @param arr
	 * @return
	 */
	public static String arrToStr(String [] arr){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < arr.length; i++){
		 sb. append(arr[i]);
		}
		return sb.toString();
	}
	
	
	

	
	public static final String inputStream2String(InputStream is) throws IOException{
		StringBuffer sb = new StringBuffer();
		InputStreamReader reader = new InputStreamReader(
				is, "utf-8");
		char[] buff = new char[1024];
		int length = 0;
		while ((length = reader.read(buff)) != -1) {
			sb.append(new String(buff, 0, length));
		}
		return sb.toString();
	}
	
	
	private static boolean isChinese(char c) {  
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
    } 
	
	
	
	public static boolean isMessyCode(String strName) {  
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");  
        Matcher m = p.matcher(strName);  
        String after = m.replaceAll("");  
        String temp = after.replaceAll("\\p{P}", "");  
        char[] ch = temp.trim().toCharArray();  
        float chLength = 0 ;  
        float count = 0;  
        for (int i = 0; i < ch.length; i++) {  
            char c = ch[i];  
            if (!Character.isLetterOrDigit(c)) {  
                if (!isChinese(c)) {  
                    count = count + 1;  
                }  
                chLength++;   
            }  
        }  
        float result = count / chLength ;  
        if (result > 0.4) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
      
	/**
	 * 字符串转ascii码
	 * @param value
	 * @return
	 */
    public static String stringToAscii(String value){
    	
    	if (StringUtils.isEmptyString(value)){
    		//log.error(StringUtils.class.getName(), "stringToAscii", "传入的字符为空");
    		return "";
    	}
    	
    	StringBuffer sbu = new StringBuffer();  
	    char[] chars = value.toCharArray();   
	    for (int j = 0; j < chars.length; j++) {  
	        if(j != chars.length - 1)  
	        {  
	            sbu.append((int)chars[j]).append(",");  
	        }  
	        else {  
	            sbu.append((int)chars[j]);  
	        }  
	    }
	    
	    return sbu.toString();
    }
    /**
     * 
//     * @param selfLongNumber
//     * @param splitChar
     * @return
     */
    public static String replaceStrWithLevel(String str,String splitStr,Integer level,String replaceStr){
    	String [] strArry = str.split(splitStr);
    	StringBuffer result=new StringBuffer();
    	for(int i=0;i<strArry.length;i++){
    		if(i==level-1){
    			strArry[i]=replaceStr;
    		}
    		if(i==strArry.length-1){
    			result.append(strArry[i]);
    		}else{
    			result.append(strArry[i]).append(splitStr);
    		}
    	}
    	return result.toString();
    }
	public static String getParentLongNumber(String selfLongNumber,
			String splitChar) {
		if (isNotEmptyString(selfLongNumber)
				&& selfLongNumber.split(splitChar).length > 0) {
			String[] ss = selfLongNumber.split(splitChar);
			StringBuffer parentLongNumber = new StringBuffer();
			for (int i = 0, len = ss.length - 1; i < len; i++) {
				parentLongNumber.append((i == 0 ? "" : splitChar) + ss[i]);
			}
			return parentLongNumber.toString();
		} else {
			return null;
		}
	}
	
	/**
	 * 判断字符串是否是数字字符串
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
	    if(StringUtils.isEmptyString(str))
	    {
	        return false;
	    }
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    Matcher isNum = pattern.matcher(str);
	    if( !isNum.matches() ){
	        return false; 
	    } 
	    return true; 
	 }
	
	/**
	 * 对象转String
	 * @return
	 */
	public static String ObjectToStr(Object obj){
		String value = "";
		if(obj != null){
			if(!"".equals(obj.toString())){
				value = String.valueOf(obj);
			}
		}else{
			value = "";
		}
		return value;
	}
	
	/**
	 * 格式字符串 格式：a,b,v====>'a','b','v'
	 * @param str 请求参数
	 * @return String 返回类型
	 */
	public static String getFormatString(String str) {
		String strArr[] = str.split(",");
		String retStr = "";
		for (int i = 0; i < strArr.length; i++) {
			if (i > 0) {
				retStr = retStr + ",";
			}
			retStr = retStr + "'" + strArr[i] + "'";
		}
		return retStr;
	}
	public static int[] getInCondition(String str){
		String[] strArr = str.split(",");
		int[] result = new int[strArr.length];
		
		for(int i=0;i<strArr.length;i++){
			if(StringUtils.isNotEmptyString(strArr[i])){
				result[i] = Integer.parseInt(strArr[i]);
			}
		}
		return result;
	}
	
	public static long[] str2longArr(String[] resultArr){
		long[] longArr = new long[resultArr.length];
		for(int i=0;i<resultArr.length;i++){
			longArr[i] = Long.parseLong(resultArr[i]);
		}
		return longArr;
	}
	
	public static String getValueFromIntegerKeyMap(Map<String,Object> map, Integer key){
		String value = "其他";
		if(key != null){
			String k = key.toString();
			if(map.get(k)!=null){
				value = (String) map.get(k);
			}
		}
		return value;
	}
	
	public static String getValueFromStringKeyMap(Map<String,Object> map, String key){
		String value = "其他";
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(key)){
			if(map.get(key)!=null){
				value = (String) map.get(key);
			}
		}
		return value;
	}
	
//	public static Integer getKeyFromStringValueMap(Map<String,Object> map, String key){
//		Integer value = -1;
//		if(StringUtil.isNotNull(key)){
//			if(map.get(key)!=null){
//				value = StringUtil.getInteger(map.get(key).toString()) ;
//			}
//		}
//		return value;
//	}
	
	public static Object getFormatObject(Object obj){
		if(obj == null){
			obj = "";
		}
		return obj;
	}
	
	/**
     * 
     * @return
     */
    public static String getNetAddress(HttpServletRequest request){
    	String url = "";
    	String ip = "";
    	String port = "";
    	String serverName = "";
		ip = request.getServerName();
		port = request.getServerPort()+"";
		serverName = request.getContextPath();
		url = "http://"+ip+":"+port+serverName;
		
    	return url;
    }
	
    /**
	 * 获取图片格式函数
	 * 
	 * @return
	 */
	public static String getExtension(byte[] input) {
		// 图片格式
		String format = "";
		ImageInputStream iis = null;
		try {
			iis = ImageIO.createImageInputStream(new ByteArrayInputStream(input));
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (iter.hasNext()) {

				format = iter.next().getFormatName();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (iis != null) {
				try {
					iis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return format;
	}

	/**
	 * 获取经纬度最大范围区间：如：输入12.000，返回结果为12.0009
	 * */
	public static String getMaxCoordinate(String coordinate){
		if(StringUtils.isEmptyString(coordinate)){
			return null;
		}else{
			if(coordinate.indexOf(".") < 0){
				return coordinate+".9";
			}else{
				return coordinate+"9";
			}
		}
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	// //////////////////////////字符处理//////////////////////////////////////////////////////////
	public static char toUpperCase(char c) {
		if (c >= 'a' && c <= 'z') {
			c -= 32;
		}
		return c;
	}

	public static char toLowerCase(char c) {
		if (c >= 'A' && c <= 'Z') {
			c += 32;
		}
		return c;
	}
	
	public static void main(String[] args) {
		System.out.println(getMaxCoordinate("12.0002"));
		//System.out.println(replaceStrWithLevel("sdadsa!dsfs!fgdg","!",1,"aaaa"));
		//System.out.println(getParentLongNumber("EIS_BASE!BASE_INFO!EIS_BASE_UPLOADUSER","!"));
	}

	/**
	 * 正则校验
	 * @param str,regex
	 * @return
	 */
	public static Boolean checkRegex_ture(String str, String regex) {
		str = str.trim();
		if (str.matches(regex)) {
			return true;
		}
		return false;
	}

	/**
	 * 正则校验
	 * @param str,regex
	 * @return
	 */
	public static Boolean checkRegex_false(String str, String regex) {
		str = str.trim();
		if (!str.matches(regex)) {
			return true;
		}
		return false;
	}
}
