package lolth.muying;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MuYingCommoditySearchList extends PagingFetchTaskProducer {

	public static final String MUYING_SHOP_LIST = "muying_shop_list";

	private static final String MUYING_SHOP_LIST_URL = "http://www.muyingzhijia.com/Shopping/SearchResult.aspx?condition={0}&cateID={1}&page={2}";

	private String keyword;

	private String keywordCode;

	private String subject;

	private String cateId;

	public MuYingCommoditySearchList(String keyword, String keywordCode, String cateId, String subject) {
		super(MUYING_SHOP_LIST);
		this.keyword = keyword;
		this.keywordCode = keywordCode;
		this.cateId = cateId;
		this.subject = subject;
	}

	@Override
	protected int getMaxPage() {

		try {
			Document doc = GlobalComponents.fetcher.document(buildUrl(1));
			
			Elements elements = doc.select("#container > div.goods_wrap > div.foCa.clearfix > div");
			if(elements.isEmpty()){
				return 0;
			}
			
			Elements pages = elements.select("a");
			if (pages.size() == 0) {
				return 1;
			}
			if (pages.size() > 1) {
				int maxPageIdx = pages.size() - 2;
				String pageStr = pages.get(maxPageIdx).text();
				return Integer.parseInt(pageStr);
			}

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(MUYING_SHOP_LIST_URL, keywordCode, cateId, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(keyword);
		task.setBatchName(MUYING_SHOP_LIST);
		task.setUrl(url);
		task.setExtra(subject+":"+cateId);
		return task;
	}

	public static void main(String[] args) {
		String subject = "惠氏";
		String[] cateIDs = { "39", "205", "206", "207" };
		String[] keywords = { "惠氏启赋","wyeth启赋","雅培菁致","多美滋致粹","合生元奶粉","诺优能白金版","美赞臣亲舒"};
		for (String keyword : keywords) {
			for (String cateId : cateIDs) {
				String keywordCode = keyword;
				try {
					keywordCode = URLEncoder.encode(keyword, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				new MuYingCommoditySearchList(keyword, keywordCode, cateId, subject).run();
			}
		}
	}

}
