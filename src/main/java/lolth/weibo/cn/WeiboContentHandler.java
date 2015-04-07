package lolth.weibo.cn;

import org.apache.commons.lang3.StringUtils;

public class WeiboContentHandler {
	public static void spliteContent(String text) {
		if (StringUtils.isBlank(text)) {
			return;
		}

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
		System.out.println(at);
		System.out.println(mainText);
		System.out.println(topic);
		System.out.println(feelings);
//		bean.setAt(at.toString());
//		bean.setMainText(mainText.toString());
//		bean.setTopic(topic.toString());
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

	public static void main(String[] args) {
		String text = "#ssssssss#→_→//@恋鹿酥_祝鹿晗0420生日快乐:[笑cry]→_→#111111111#//@OPPO:OPPO N3旋转摄像头给你拍出了2米大长腿的即视感[偷乐]  ";
		WeiboContentHandler.spliteContent(text);

		text = "#随手拍OPPO绿#哟哟哟～绿(⊙o⊙)哦@OPPO [爱你] [位置]307县道";
		WeiboContentHandler.spliteContent(text);
	}
}
