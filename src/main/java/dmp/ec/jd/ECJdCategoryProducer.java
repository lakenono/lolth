package dmp.ec.jd;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lakenono.util.UrlUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ECJdCategoryProducer extends Producer {

	private final static String JD_CATEGORY_URL = "http://list.jd.com/list.html?cat={0}&page={1}&JL=6_0_0";

	private String catString;

	public ECJdCategoryProducer(String projectName, String catString) {
		super(projectName);
		this.catString = catString;
	}

	@Override
	public String getQueueName() {
		return "ec_dmp_jd_list";
	}

	@Override
	protected int parse() throws Exception {
		Document document = GlobalComponents.fetcher.document(buildUrl(1));
		Elements pageElements = document.select("div#J_bottomPage span.p-skip b");
		if (pageElements.isEmpty()) {
			log.error("jd page is empty:" + pageElements);
			return 0;
		}
		String pageString = pageElements.text();
		if (StringUtils.isNumeric(pageString)) {
			log.info("jd max page is " + pageString);
			return Integer.parseInt(pageString);
		}
		log.error("jd page is error:" + pageElements);
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(JD_CATEGORY_URL, UrlUtils.encode(this.catString), pageNum);
	}
	
	

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra("1");
		return buildTask;
	}

	public static void main(String[] args) {
		String catString = "9855,9856,9899";
		String projectName = "节能灯";
		try {
			new ECJdCategoryProducer(projectName, catString).run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
