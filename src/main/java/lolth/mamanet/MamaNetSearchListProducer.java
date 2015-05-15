package lolth.mamanet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lakenono.core.GlobalComponents;
import lakenono.task.FetchTask;
import lakenono.task.PagingFetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
/**
 * 生成妈妈网URL队列
 * @author hepeng.yang
 *
 */
@Slf4j
public class MamaNetSearchListProducer extends PagingFetchTaskProducer {
	public static final String MAMANET_SEARCH_LIST = "mamanet_search_list";

	private static final String MAMANET_SEARCH_LIST_URL = "http://zhannei.baidu.com/cse/search?q={0}&p={1}&s=8134803871385444951&nsid=1";

	private static final Pattern pattern = Pattern.compile("[^0-9]");

	private String keyword;
	private String keywordEncode;
	private String name;

	public MamaNetSearchListProducer(String name, String keyword) {
		super(MAMANET_SEARCH_LIST);
		this.name = name;
		this.keyword = keyword;
		try {
			this.keywordEncode = URLEncoder.encode(keyword, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int getMaxPage() {
		try {
			// 根据搜索结果数确定页数
			int page = getPage(1);
			// 如果page大于10，重新抓取一次結果条数
			if (page > 10) {
				page = getPage(page);
			}
			// 最大页数为76页
			if (page > 76) {
				page = 76;
			}
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private int getPage(int i) throws IOException, InterruptedException {
		Document document = GlobalComponents.fetcher.document(buildUrl(i));
		Elements resultNum = document.select("span.support-text-top");
		if (!resultNum.isEmpty()) {
			String result = resultNum.text();
			if (StringUtils.isNoneBlank(result)) {
				// 正则抽取数字
				Matcher matcher = pattern.matcher(result);
				result = matcher.replaceAll("");
				// 每页10条数据
				return Integer.parseInt(result) / 10 + 1;
			}
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(MAMANET_SEARCH_LIST_URL, keywordEncode, pageNum - 1);
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask fetchTask = new FetchTask();
		fetchTask.setName(name);
		fetchTask.setBatchName(MAMANET_SEARCH_LIST);
		fetchTask.setUrl(url);
		fetchTask.setExtra(keyword);
		return fetchTask;
	}

	public static void main(String[] args) {
		String name = "惠氏";
		String[] keywords = { "惠氏启赋", "wyeth启赋", "雅培菁致", "多美滋致粹", "合生元奶粉", "诺优能白金版", "美赞臣亲舒" };
		for (String k : keywords) {
			log.info("{} start!", k);
			MamaNetSearchListProducer producer = new MamaNetSearchListProducer(name, k);
			producer.run();
			log.info("{} finish!", k);
		}
	}

}
