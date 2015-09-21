package lolthx.suning.keysearch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lakenono.base.Producer;
import lakenono.base.Task;
import lakenono.core.GlobalComponents;

public class SuningSearchListProducer  extends Producer  {
	
	private static final String SUNING_SEARCH_LIST_URL = "http://search.suning.com/{0}/cp={1}";
	
	private String keyword;
	private String keywordEncode;
	
	public SuningSearchListProducer(String projectName,String keyword) throws UnsupportedEncodingException {
		super(projectName);
		this.keyword = keyword;
		this.keywordEncode = URLEncoder.encode(keyword, "utf-8");
	}

	@Override
	public String getQueueName() {
		return "suning_search_list";
	}

	@Override
	protected int parse() throws Exception {
		Document doc = GlobalComponents.fetcher.document(buildUrl(1));
		Element lastPage = doc.getElementById("pageLast");
		Element firstPage = doc.getElementById("pageFirst");
		if (lastPage != null) {
			String maxPage = lastPage.text();
			if (StringUtils.isNumeric(maxPage)) {
				return Integer.parseInt(maxPage);
			} else {
				return 0;
			}
		}else{
			Element py = doc.getElementById("Py");
			if(py != null){
				return 0;
			}else{
				if(firstPage != null){
					return 1;
				}
			}
		}
		Elements items = doc.select("ul.container li.item");
		if (!items.isEmpty()) {
			return 1;
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(SUNING_SEARCH_LIST_URL, keywordEncode, String.valueOf(pageNum - 1));
	}

	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword);
		return buildTask;
	}
	
	public static void main(String args[]) throws UnsupportedEncodingException, Exception{
		String projectName = "suning search test";
		//"惠氏启赋","wyeth启赋","雅培菁智","多美滋致粹","合生元奶粉","诺优能白金版","美赞臣亲舒"
		String[] keywords = { "惠氏启赋","wyeth启赋","雅培菁智","多美滋致粹","合生元奶粉","诺优能白金版","美赞臣亲舒"  };
		for(int i =0 ;i < keywords.length;i++){
			new SuningSearchListProducer(projectName, keywords[i]).run();
		}
	}
	
}
