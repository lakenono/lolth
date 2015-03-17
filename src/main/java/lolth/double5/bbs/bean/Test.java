package lolth.double5.bbs.bean;

import org.apache.commons.lang3.StringUtils;

public class Test {
	public static void main(String[] args) {
		String test = "【回复：76    查看：2160】";
		System.out.println(StringUtils.substringBetween(test,"回复：", "查看"));
		System.out.println(StringUtils.substringBetween(test,"查看：", "】"));
	}
}
