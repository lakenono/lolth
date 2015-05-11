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

public class MuYingCommoditySearchList extends PagingFetchTaskProducer{

	public static final String MUYING_SHOP_LIST = "babytree_bbs_list";
	
	private static final String MUYING_SHOP_LIST_URL = "http://www.muyingzhijia.com/Shopping/SearchResult.aspx?condition={0}&page={1}";
	
	private String keyword;
	
	public MuYingCommoditySearchList(String keyword) {
		super(MUYING_SHOP_LIST);
		this.keyword = keyword;
	}

	@Override
	protected int getMaxPage() {
		// #container > div.goods_wrap > div.foCa.clearfix > div > a:nth-child(3)
		
		try {
			Document doc = GlobalComponents.fetcher.document(buildUrl(1));
			Elements pages = doc.select("#container > div.goods_wrap > div.foCa.clearfix > div > a");
			if(pages.size() == 0){
				return 1;
			}
			if(pages.size() > 1) {
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
		return MessageFormat.format(MUYING_SHOP_LIST_URL, keyword, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(keyword);
		task.setBatchName(MUYING_SHOP_LIST);
		task.setUrl(url);
		return task;
	}

	public static void main(String[] args) {
		String keyword = "惠氏启赋";
		try {
			keyword = URLEncoder.encode(keyword, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new MuYingCommoditySearchList(keyword).run();
	}
	
}
