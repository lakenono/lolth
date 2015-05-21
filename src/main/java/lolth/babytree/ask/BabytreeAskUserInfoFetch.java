package lolth.babytree.ask;

import java.util.List;

import lakenono.core.GlobalComponents;
import lolth.babytree.ask.bean.UserInfoBean;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BabytreeAskUserInfoFetch {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public void run() throws Exception {
		while (true) {
			List<String> userUrls = GlobalComponents.db.getRunner().query("select questionerUrl from data_babytree_ask where status = 'answer_success';", new ColumnListHandler<String>());
			for (String userUrl : userUrls) {
				try {
					this.process(userUrl);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					this.log.error("", e);
				}
				Thread.sleep(2000);
			}
			Thread.sleep(300000);
		}
	}

	public void process(String url) throws Exception {
		this.log.info("process {}", url);

		// 匿名用户
		if (StringUtils.equals(url, "none")) {
			return;
		}

		if (UserInfoBean.isExist(url)) {
			this.log.info("已经抓取完毕...");
			return;
		}

		UserInfoBean bean = new UserInfoBean();

		String html = GlobalComponents.fetcher.fetch(url);
		Document document = Jsoup.parse(html);

		// url
		bean.setUserUrl(url);

		// username
		String username = document.select("div#mytree-username").first().text();
		bean.setUsername(username);

		// area
		if (document.select("div#mytree-basic-info ul li.info-location span.location").size() != 0) {
			String area = document.select("div#mytree-basic-info ul li.info-location span.location").text();
			bean.setArea(area);
		} else {
			bean.setArea("none");
		}

		if (document.select("div#mytree-basic-info ul li[style] span").size() != 0) {
			// baby gender
			String babyGender = document.select("div#mytree-basic-info ul li[style] span").first().attr("class");
			bean.setBabyGender(babyGender);

			// baby status
			String babyStatus = document.select("div#mytree-basic-info ul li[style] span").first().html();
			bean.setBabyStatus(babyStatus);
		} else {
			bean.setBabyGender("none");
			bean.setBabyStatus("none");
		}

		try {
			bean.persist();
		} catch (Exception e) {
			if (StringUtils.contains(e.getMessage(), "for key 'PRIMARY'")) {
				this.log.info("重复数据");
			} else {
				this.log.error("", e);
			}
		}

		this.log.info(bean.toString());
	}

	public static void main(String[] args) throws Exception {
		new BabytreeAskUserInfoFetch().run();
	}

}
