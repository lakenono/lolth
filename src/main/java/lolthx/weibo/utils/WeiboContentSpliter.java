package lolthx.weibo.utils;


import lolthx.weibo.bean.WeiboBean;

import org.apache.commons.lang3.StringUtils;

public class WeiboContentSpliter {

	public static void spliteContent(WeiboBean bean) {
		if (StringUtils.isBlank(bean.getText())) {
			return;
		}

		StringBuffer at = new StringBuffer();
		StringBuffer topic = new StringBuffer();
		StringBuffer mainText = new StringBuffer();
		StringBuffer feelings = new StringBuffer();

		boolean needMainText = true;
		// 评论
		char[] chars = bean.getText().toCharArray();
		int charIdx = 0;
		while (charIdx < chars.length) {
			switch (chars[charIdx]) {
			case '#':
				charIdx = segmentText(topic, chars, charIdx, '#');
				// 添加结束索引
				topic.append('#');
				// 移动游标，表示结束字符处理完成
				charIdx++;
				break;
			case '@':
				charIdx = segmentText(at, chars, charIdx, '#', ' ', ':', ';');
				break;
			case '[':
				charIdx = segmentText(feelings, chars, charIdx, ']');
				feelings.append(']');
				charIdx++;
				break;
			case '/':
				if (needMainText) {
					if (charIdx + 2 < chars.length && chars[charIdx + 1] == '/' && chars[charIdx + 2] == '@') {
						needMainText = false;
					}
				}
			default:
				if (needMainText) {
					mainText.append(chars[charIdx]);
				}

			}
			charIdx++;
		}

		if (StringUtils.contains(bean.getText(), "//@")) {
			bean.setForwardList("//@" + StringUtils.substringAfter(bean.getText(), "//@"));
		}

		bean.setAt(at.toString());
		bean.setMainText(mainText.toString());
		bean.setTopic(topic.toString());
		bean.setFeelings(feelings.toString());
	}

	public static String[] spliteContent(String text) {
		if (StringUtils.isBlank(text)) {
			return null;
		}
		String[] strs ={"","","",""};
		StringBuffer at = new StringBuffer();
		StringBuffer topic = new StringBuffer();
		StringBuffer mainText = new StringBuffer();
		StringBuffer feelings = new StringBuffer();

		boolean needMainText = true;
		// 评论
		char[] chars = text.toCharArray();
		int charIdx = 0;
		while (charIdx < chars.length) {
			switch (chars[charIdx]) {
			case '#':
				charIdx = segmentText(topic, chars, charIdx, '#');
				// 添加结束索引
				topic.append('#');
				// 移动游标，表示结束字符处理完成
				charIdx++;
				break;
			case '@':
				charIdx = segmentText(at, chars, charIdx, '#', ' ', ':', ';');
				break;
			case '[':
				charIdx = segmentText(feelings, chars, charIdx, ']');
				feelings.append(']');
				charIdx++;
				break;
			case '/':
				if (needMainText) {
					if (charIdx + 2 < chars.length && chars[charIdx + 1] == '/' && chars[charIdx + 2] == '@') {
						needMainText = false;
					}
				}
			default:
				if (needMainText) {
					mainText.append(chars[charIdx]);
				}

			}
			charIdx++;
		}

		if (StringUtils.contains(text, "//@")) {
			strs[0] = "//@" + StringUtils.substringAfter(text, "//@");
		}
		strs[1] = at.toString();
		strs[2] = mainText.toString();
		strs[3] = topic.toString();
		return strs;
	}

	// 返回结束标签索引
	private static int segmentText(StringBuffer buffer, char[] chars, int curCharIdx, char... endChars) {
		// 添加开始字符
		buffer.append(chars[curCharIdx]);
		curCharIdx++;

		// 判断下一个字符是否是结束字符
		while (curCharIdx < chars.length) {
			for (char c : endChars) {
				if (chars[curCharIdx] == c) {
					return curCharIdx - 1;
				}
			}
			buffer.append(chars[curCharIdx]);
			curCharIdx++;
		}
		return chars.length - 1;
	}
}
