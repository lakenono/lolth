package lolthx.sogou.wenwen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args){
		String keywordRegex = "";
		String result = "";
		Pattern p = Pattern.compile(keywordRegex);
		Matcher m = p.matcher(result.toString());
		
		while (m.find()) {
				String keyword = m.group(1);
		}	
	}
}
