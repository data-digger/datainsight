package com.datadigger.datainsight.util;

import java.awt.Color;
import java.io.*;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.w3c.tidy.Tidy;

import com.datadigger.datainsight.exception.*;

//import net.sourceforge.pinyin4j.PinyinHelper;
import sun.misc.BASE64Decoder;

public class StrUtil {
	private StrUtil() {
	}
	
	public static boolean equals(String src, String dest) {
		if(src == null) {
			if(dest == null)
				return true;
			else
				return false;
		}
		return src.equals(dest);
	}

	public static boolean equalsIgnoreNull(String src, String dest) {
		if(src == null) {
			if(dest == null || dest.length() == 0)
				return true;
			else
				return false;
		}
		if(src.length() == 0)
			return dest == null || dest.length() == 0;
		return src.equals(dest);
	}
	
	public static boolean equalsIgnoreCase(String src, String dest) {
		if(src == null) {
			if(dest == null)
				return true;
			else
				return false;
		}
		return src.equalsIgnoreCase(dest);
	}
	
	public static String nullToEmpty(String str) {
		return str == null ? "" : str;
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static int compareTo(String src, String dest) {
		int result = 0;
		if(src == null)
			result = dest == null ? 0 : 1;
		else
			result = dest == null ? 1 : src.compareTo(dest);
		return result;
	}
	

	public static String replace(String str, String from, String to) {
		StringBuilder buff = new StringBuilder();
		int fromLen = from.length();
		int start = 0;
		int index;

		while ((index = str.indexOf(from, start)) != -1) {
			buff.append(str.substring(start, index));
			buff.append(to);
			start = index + fromLen;
		}
		buff.append(str.substring(start));

		return buff.toString();
	}
	
	
	public static String replaceHTML(String str) {
		if (str == null)
			return null;
		
		StringBuilder buff = new StringBuilder();
		int start = 0;
		
		for(int i = 0; i < str.length(); i++) {
			switch(str.charAt(i)) {
				case '&':
					buff.append(str.substring(start, i)).append("&amp;");
					start = i + 1;
					break;
				case '<':
					buff.append(str.substring(start, i)).append("&lt;");
					start = i + 1;
					break;
				case '>':
					buff.append(str.substring(start, i)).append("&gt;");
					start = i + 1;
					break;
				case '\'':
					buff.append(str.substring(start, i)).append("&apos;");
					start = i + 1;
					break;
				case '"':
					buff.append(str.substring(start, i)).append("&quot;");
					start = i + 1;
					break;
			}
		}
		buff.append(str.substring(start));
		return buff.toString();
	}

	public static List<String> split(String src, String split) {
		List<String> result = new ArrayList<String>();
		int index = src.indexOf(split);
		int from  = 0;
		while(index >= 0) {
			result.add(src.substring(from, index));
			from = index + split.length();
			index = src.indexOf(split, from);
		}
		result.add(src.substring(from));
		return result;
	}

	public static String readFromStream(InputStream is,String charset) throws IOException{
        
        BufferedInputStream in = new BufferedInputStream(is);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();

        BufferedOutputStream bos = new BufferedOutputStream(bo);

        int readed = 0;

        byte[] tmpBytes = new byte[1024];
        while ( (readed = in.read(tmpBytes)) != -1) {
            bos.write(tmpBytes, 0, readed);
        }

        in.close();
        bos.close();

        byte[] bs = bo.toByteArray();

        String s = new String(bs, charset);
        return s;
    }
	
	public static int indexOf(List<String> lst, String value){
		for(int i=0; i<lst.size(); i++){
			if(lst.get(i).equalsIgnoreCase(value)){
				return i;
			}
		}
		return -1;
	}

	public static String changeCharsetFrom(String str, String charset){
		if (isNullOrEmpty(str) || isNullOrEmpty(charset))
			return str;
			
		String rtn = null;
		try { 
			byte[] bs = str.getBytes(charset);
			rtn = new String(bs);
		} catch (UnsupportedEncodingException e) {
			throw new DataDiggerException(DataDiggerErrorCode.DATABASE_NOT_SUPPORT_CHARSET)
					.setDetail(charset);
		}

		return rtn;
	}
	
	public static String changeCharsetTo(String str, String charset){
		if (isNullOrEmpty(str) || isNullOrEmpty(charset))
			return str;
			
		String rtn = null;
		try { 
			byte[] bs = str.getBytes();
			rtn = new String(bs, charset);
		} catch (UnsupportedEncodingException e) {
			throw new DataDiggerException(DataDiggerErrorCode.DATABASE_NOT_SUPPORT_CHARSET)
					.setDetail(charset);
		}

		return rtn;
	}
	

	public static String asciiToUnicode(String str) {
		char[] chararray = str.toCharArray();
		StringBuffer unisb = new StringBuffer();
		for (int i = 0; i < chararray.length; i++) {
			int c = (int) chararray[i];
			if (c > 0xff) {
				unisb.append("\\u");
				unisb.append(Integer.toHexString((int) chararray[i]));
			} else {
				unisb.append(chararray[i]);
			}
		}
		return unisb.toString();
	}
	

	public static String unicodeToAscii(String str){
		return new String(str.getBytes());
	}
	
	

	/**
	 * 判断name里面是否含有不合法字符  !"#$%&'()*+,-./:;<=>?@[\]^`{|}~和中文符号 ，。、《》？；‘：”】【『』！·～、|——  
	 * 中的某个或某些,
	 * @param name
	 * @return 含有那个不合法的字符,如果返回null则意味着所以字符均合法
	 */
	public static String checkStringValid(String name) {
//		if(name == null || name.trim().equals("")){
//			return "输入不允许为空";
//		}

		Pattern p = Pattern.compile("(?!\\_)\\p{Punct}");
		Matcher m = p.matcher(name);
		if(m.find())
			return "输入包含以下非法字符： " + name.substring(m.start(),m.start() + 1);
		else{
			Pattern pp=Pattern.compile("，|。|、|《|》|？|；|‘|：|”|】|【|『|』|！|·|～|、|\\||——|…|（|）|￥");
		    Matcher mm=pp.matcher(name);
		    if(mm.find())
				return "输入包含以下非法字符： " + name.substring(mm.start(),mm.start() + 1);
		}
		return null;
	}
	
	
	/**
	 * 判断字符串中是否含有中文
	 * @param name
	 * @return
	 */
	public static boolean isIncludesChinese(String name){
		Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
	    Matcher m=p.matcher(name);
	    return m.find();

	}
	
	// 将 str 进行 BASE64 编码
	public static String getBASE64(String str) {
		if (str == null)
			return null;
		return (new sun.misc.BASE64Encoder()).encode(str.getBytes());
	}

	// 将 BASE64 编码的字符串 str 进行解码
	public static String getFromBASE64(String str) {
		if (str == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(str);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}
	
	// 将 str 进行 BASE64 编码
	public static String encodeBASE64(String str) {
		if (str == null)
			return null;
		return (new sun.misc.BASE64Encoder()).encode(str.getBytes());
	}
	
	/**
	 * 判断输入的字符串首字母是否为英文字符
	 * @param str
	 * @return
	 */
	public static boolean isCharAtFirst(String str) {
		char ch = str.toLowerCase().charAt(0);
		if((ch >='a' && ch <= 'z'))
			return true;
		else
			return false;
	}
	
	/**
	 * 判断输入的字符串首字母是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumberAtFirst(String str) {
		char ch = str.toLowerCase().charAt(0);
		if((ch >='0' && ch <= '9'))
			return true;
		else
			return false;
	}
	
	/**
	 * 去除中文空格
	 * @param str
	 * @return
	 */
	public static String trimChineseBlank(String str) {
		if(isNullOrEmpty(str))
			return str;
		String ret = str;
		Pattern p=Pattern.compile("　");
		Matcher m = p.matcher(str);
		while(m.find()){
			ret = str.replace(str.substring(m.start(), m.start()+1), "");
		}
		return ret.trim();
	}
	
	/**
	 * 验证日期字符串
	 * @param validStr 检验字符串 默认格式为 yyyy-MM-dd
	 * @return
	 */
	public static boolean isDateStr(String validStr) {
		return isDateStr(validStr, "yyyy-MM-dd");
	}
	
	/**
	 * 验证日期字符串
	 * @param validStr 检验字符串
	 * @param format   日期格式
	 * @return
	 */
	public static boolean isDateStr(String validStr, String format) {
		try {
			SimpleDateFormat simpleDate = new SimpleDateFormat(format);
			simpleDate.setLenient(false);
			Date date1 = simpleDate.parse(validStr);
			String vat = simpleDate.format(date1);
			if(!vat.equals(validStr))
				return false;
			else 
				return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * 判断字符串checkStr是否在字符串数组checkSet之中
	 * 
	 * @param checkStr
	 *            待判断的字符串
	 * @param checkSet
	 *            待检查的字符串数组
	 * @return
	 */
	public static boolean isInArray(String checkStr, String[] checkSet) {
		return (checkSet == null) ? false : Arrays.asList(checkSet).contains(checkStr);
	}

	/**
	 * 按拼音进行字符串的比较
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
//	public static int compareByPinyin(String src, String dest) {
//		if (src == null) {
//			if (dest == null)
//				return 0;
//			else
//				return -1;
//		} else if (dest == null)
//			return 1;
//		for (int i = 0; i < src.length() && i < dest.length(); i++) {
//			int codePoint1 = src.charAt(i);
//			int codePoint2 = dest.charAt(i);
//			if (Character.isSupplementaryCodePoint(codePoint1)
//					|| Character.isSupplementaryCodePoint(codePoint2)) {
//				i++;
//			}
//			if (codePoint1 != codePoint2) {
//				if (Character.isSupplementaryCodePoint(codePoint1)
//						|| Character.isSupplementaryCodePoint(codePoint2)) {
//					return codePoint1 - codePoint2;
//				}
//				String[] pinyins = PinyinHelper.toHanyuPinyinStringArray((char) codePoint1);
//				String pinyin1 = (pinyins == null) ? null : pinyins[0];
//				
//				pinyins = PinyinHelper.toHanyuPinyinStringArray((char) codePoint2);
//				String pinyin2 = (pinyins == null) ? null : pinyins[0];
//
//				if (pinyin1 != null && pinyin2 != null) { // 两个字符都是汉字
//					if (!pinyin1.equals(pinyin2)) {
//						return pinyin1.compareTo(pinyin2);
//					}
//				} else {
//					return codePoint1 - codePoint2;
//				}
//			}
//		}
//		return src.length() - dest.length();
//	}

	/**
	 * 将字符串数组转换为字符串
	 * 
	 * @param strArray
	 * @param separator
	 * @return
	 */
	public static String arrayToString(String[] strArray, String separator) {
		if (strArray == null)
			return null;
		return listToString(Arrays.asList(strArray), separator);
	}

	/**
	 * 将字符串列表转换为字符串
	 * 
	 * @param strArray
	 * @param separator
	 * @return
	 */
	public static String listToString(List<String> strList, String separator) {
		if (strList == null)
			return null;
		if (separator == null)
			separator = "";
		StringBuffer buffer = new StringBuffer();
		for (String str : strList)
			buffer.append(str).append(separator);
		String ret = buffer.toString();
		return ret.substring(0, ret.length() - separator.length());
	}
	
	public static String decodeHTML(String str) {
		if (str == null)
			return null;
		String newString = str;
		if(newString.indexOf("&amp;") != -1)
			newString = replace(newString, "&amp;", "&");
		
		if(newString.indexOf("&lt;") != -1)
			newString = replace(newString, "&lt;", "<");
		
		if(newString.indexOf("&gt;") != -1)
			newString = replace(newString, "&gt;", ">");
	
		if(newString.indexOf("&apos;") != -1)
			newString = replace(newString, "&apos;", "'");
		
		if(newString.indexOf("&quot;") != -1)
			newString = replace(newString, "&quot;", "\"");
		
		if(newString.indexOf("&nbsp;") != -1)
			newString = replace(newString, "&nbsp;", " ");
		return newString;
	}
	
	public static boolean parseDoubleEquals(String src, String dest) {
		double dsrc = 0;
		double ddest = 0;
		try {
			dsrc = Double.parseDouble(src);
			ddest = Double.parseDouble(dest);
		} catch (Exception e) {
			return false;
		}
		return dsrc == ddest;
	}
	
	public static Color parseToColor(final String c) {
		return parseToColor(c, Color.WHITE);
	}

	public static Color parseToColor(final String c, Color defaultColor) {
		if (isNullOrEmpty(c))
			return defaultColor;
		String sc = c.trim();
		if (sc.charAt(0) == '#')
			sc = sc.substring(1);
		try {
			return new Color(Integer.parseInt(sc, 16));
		} catch (NumberFormatException e) {
			return defaultColor;
		}
	}
	
//	public static String parseHtmlToXml(String html) throws IOException {
//		String result = null;
//		if (html == null)
//			return null;
//		Tidy tidy = new Tidy();
//        tidy.setXmlOut(true);
//        //tidy.setPrintBodyOnly(true);
//        tidy.setQuoteNbsp(true);
//		tidy.setQuiet(true);
//		tidy.setShowWarnings(false);
//		StringWriter writer = new StringWriter();
//		StringReader reader = new StringReader(html);
//		try {
//			tidy.parseDOM(reader, writer);
//			result = writer.toString();
//		} finally {
//			writer.close();
//			reader.close();
//		}
//		return result;
//	}
	
	public static String getDebugMapInfo(Hashtable<String,WeakReference> map ){
		System.gc();
		StringBuffer buf = new StringBuffer();
//        buf.append("COUNT: ").append(getNumActive()).append("\n");
//        buf.append("Idle: ").append(getNumIdle()).append("\n");
		
		for(Iterator<java.util.Map.Entry<String,WeakReference>> it = map.entrySet().iterator();it.hasNext();){
			java.util.Map.Entry<String,WeakReference> entry = it.next();
			
			WeakReference ref = entry.getValue();
			if (ref.get()==null){
				it.remove();
			}
		}
		
		buf.append("\t").append("Count:").append(map.size());
//		buf.append(" {");
//
//		int curItem=0;
//		for(String key:map.keySet()){
//			WeakReference ref = map.get(key);
//			buf.append(ref.get().toString()).append(";");
//			if (curItem++>=3) {
//				buf.append("......");
//				break;
//			}
//			
//		}
//
//		buf.append("}\n");
		
        return buf.toString();
	}
	
}
