package lolthx.yhd.fresh.task;

import java.text.MessageFormat;

import lakenono.base.Producer;
/**
 * 1号店任务生成类
 * 跑两次任务，一个页面两次请求
 * url1=http://list.yhd.com/searchPage/c20947-0-81806/b/a-s1-v0-p11-price-d0-f0-m1-rt0-pid-mid0-k/?isLargeImg=0
 * http://search.yhd.com/c0-0-0/b/a-s1-v0-p2-price-d0-f0-m1-rt0-pid-mid0-k%E6%B4%97%E5%8F%91%E9%9C%B2/
 * url2=http://list.yhd.com/searchPage/c20947-0-81806/b/a-s1-v0-p{0}-price-d0-f0-m1-rt0-pid-mid0-k/?isGetMoreProducts=1&moreProductsDefaultTemplate=0&isLargeImg=0";
 * @author hepeng.yang
 *
 */
public  class YhdFreshProducer extends Producer{
	
	private static final String YHD_FRESH_URL = "http://list.yhd.com/searchPage/c20947-0-81806/b/a-s1-v0-p{0}-price-d0-f0-m1-rt0-pid-mid0-k/?isGetMoreProducts=1&moreProductsDefaultTemplate=0&isLargeImg=0";

	public YhdFreshProducer(String projectName) {
		super(projectName);
	}

	@Override
	public String getQueueName() {
		return "yhd_fresh_list";
	}

	@Override
	protected int parse() throws Exception {
//		Document doc = GlobalComponents.fetcher.document(buildUrl(1));
//		Elements elements = doc.select("a#lastPage");
//		if(!elements.isEmpty()){
//			String page = elements.first().text();
//			return Integer.parseInt(page);
//		}
		//1号店总数50页
		return 50;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(YHD_FRESH_URL,pageNum);
	}
	
	public static void main(String[] args) throws Exception {
		
		new YhdFreshProducer("yhd_fresh").run();
	}
	
}
