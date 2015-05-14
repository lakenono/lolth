package lolth.babytree.bbs;

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

@Slf4j
public class BabytreeBBSSearchList extends PagingFetchTaskProducer {

	public static final String BABYTREE_BBS_LIST = "babytree_bbs_list";

	private static final String BABYTREE_BBS_LIST_URL = "http://www.babytree.com/s.php?q={0}&c=community&cid=0&range=&pg={1}";

	private static final Pattern pattern = Pattern.compile("[^0-9]");

	private String keyword;

	private String keywordCode;

	private String subject;

	public BabytreeBBSSearchList(String keyword, String keywordCode, String subject) {
		super(BABYTREE_BBS_LIST);
		this.keyword = keyword;
		this.keywordCode = keywordCode;
		this.subject = subject;
	}

	@Override
	protected int getMaxPage() {
		try {
			Document doc = GlobalComponents.fetcher.document(buildUrl(1));

			Elements pages = doc.select("#pagination1 > div > span.page-number");
			String pageStr = pages.text();
			if (StringUtils.isBlank(pageStr)) {
				return 1;
			}

			Matcher matcher = pattern.matcher(pageStr);
			pageStr = matcher.replaceAll("");
			if (StringUtils.isBlank(pageStr)) {
				return 1;
			}
			return Integer.parseInt(pageStr);

		} catch (Exception e) {
			log.error("Get max page error!", e);
		}
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) {
		return MessageFormat.format(BABYTREE_BBS_LIST_URL, keywordCode, String.valueOf(pageNum));
	}

	@Override
	protected FetchTask buildTask(String url) {
		FetchTask task = new FetchTask();
		task.setName(subject);
		task.setBatchName(BABYTREE_BBS_LIST);
		task.setUrl(url);
		task.setExtra(keyword);
		return task;
	}

	public static void main(String[] args) {
		String subject = "惠氏";
		String[] keywords = { "惠氏启赋", "wyeth启赋", "雅培菁致", "多美滋致粹", "合生元奶粉", "诺优能白金版", "美赞臣亲舒" };
		for (String keyword : keywords) {
			String keywordCode = keyword;
			try {
				keywordCode = URLEncoder.encode(keyword, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new BabytreeBBSSearchList(keyword, keywordCode, subject).run();
		}
	}

}
