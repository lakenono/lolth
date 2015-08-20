package lolthx.baidu.zhidao;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.DistributedParser;
import lakenono.base.Task;
import lolthx.baidu.zhidao.bean.BaiduZhidaoUserBean;

public class BaiduZhidaoUserFetch extends DistributedParser {

	@Override
	public String getQueueName() {
		return "baidu_zhidao_user_detail";
	}

	@Override
	public void parse(String result, Task task) throws Exception {
		if (StringUtils.isBlank(result)) {
			return;
		}

		Document doc = Jsoup.parse(result);

		Elements dds = doc.select("dl.userdetail-profile-basic dd");
		if (dds.isEmpty()) {
			return;
		}

		BaiduZhidaoUserBean user = new BaiduZhidaoUserBean();

		for (Element dd : dds) {

			String data = dd.text();

			if (StringUtils.startsWith(data, "性别")) {
				user.setSex(StringUtils.trim(StringUtils.substringAfter(data, "性别")));
			}

			if (StringUtils.startsWith(data, "生日")) {
				user.setBirtyday(StringUtils.trim(StringUtils.substringAfter(data, "生日")));
			}

			if (StringUtils.startsWith(data, "血型")) {
				user.setBloodType(StringUtils.trim(StringUtils.substringAfter(data, "血型")));
			}

			if (StringUtils.startsWith(data, "出生地")) {
				user.setBirtyAddress(StringUtils.trim(StringUtils.substringAfter(data, "出生地")));
			}

			if (StringUtils.startsWith(data, "居住地")) {
				user.setLiveAddress(StringUtils.trim(StringUtils.substringAfter(data, "居住地")));
			}
		}

		user.setName(task.getExtra());
		// String userName =
		// StringUtils.trim(doc.select("p.uname.wordwrap a").first().text());
		// user.setName(userName);
		user.setUrl(task.getUrl());

		user.saveOnNotExist();
	}

	public static void main(String args[]) {
		for (int i = 1; i <= 70; i++) {
			new BaiduZhidaoUserFetch().run();
		}
	}

}
