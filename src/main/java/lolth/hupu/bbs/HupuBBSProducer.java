package lolth.hupu.bbs;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HupuBBSProducer extends Producer {

	private static final String HUPU_BBS_URL = "http://bbs.hupu.com/{0}-{1}";

	private String keyword;

	private String parameter;

	public HupuBBSProducer(String projectName, String keyword, String parameter) {
		super(projectName);
		this.keyword = keyword;
		this.parameter = parameter;
	}

	@Override
	public String getQueueName() {
		return "hupu_bbs_list";
	}

	@Override
	protected int parse() throws Exception {
		Document doc = GlobalComponents.fetcher.document(buildUrl(1));
		// #content > div.page > a:nth-child(6)
		Elements pageElements = doc.select("#content > div.page > a:nth-child(6)");
		if (!pageElements.isEmpty()) {
			String pageStr = pageElements.text();
			return Integer.parseInt(pageStr);
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(HUPU_BBS_URL, parameter, String.valueOf(pageNum));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}

	public static void main(String[] args) {
		String projectName = "CNA";
		String[] keywords = { "东莞烈豹", "广东华南虎", "江苏龙", "辽宁飞豹", "八一火箭", "山西猛龙", "吉林东北虎", "天津金狮", "青岛雄鹰", "浙江稠州金牛", "上海大鲨鱼", "北京鸭", "新疆飞虎", "浙江广厦猛狮", "山东金狮", "福建中华鲟", "佛山龙狮", "江苏大圣", "重庆翱龙", "四川蓝鲸", "中国篮球意见区", "校园篮球" };
		String[] parameter = { "dongguan", "guangdong", "jiangsu", "liaoning", "bayi", "shanxi", "jilin", "tianjin", "qingdao", "zhejiang", "shanghai", "beijing", "xinjiang", "guangsha", "shandong", "fujian", "foshan", "dasheng", "chongqing", "sichuan", "cba-feedback", "cuba" };
		for (int i = 0; i < keywords.length; i++) {
			try {
				new HupuBBSProducer(projectName, keywords[i], parameter[i]).run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
