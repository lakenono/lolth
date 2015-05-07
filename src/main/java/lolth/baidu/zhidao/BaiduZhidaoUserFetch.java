package lolth.baidu.zhidao;

import lakenono.task.FetchTask;
import lakenono.task.PageParseFetchTaskHandler;
import lolth.baidu.zhidao.bean.BaiduZhidaoUserBean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BaiduZhidaoUserFetch extends PageParseFetchTaskHandler {

	public BaiduZhidaoUserFetch() {
		super(BaiduZhidaoDetailFetch.BAIDU_ZHIDAO_USER);
	}

	public static void main(String[] args) {
		BaiduZhidaoUserFetch fetch = new BaiduZhidaoUserFetch();
		fetch.setSleep(1000);
		fetch.run();
	}

	@Override
	protected void parsePage(Document doc, FetchTask task) throws Exception {
		Elements dds = doc.select("dl.userdetail-profile-basic dd");
		if (dds.isEmpty()) {
			return;
		}

		BaiduZhidaoUserBean user = new BaiduZhidaoUserBean();

		for (Element dd : dds) {
			String data = dd.text();
			
			if(StringUtils.startsWith(data, "性别")){
				user.setSex(StringUtils.trim(StringUtils.substringAfter(data, "性别")));
			}
			
			if(StringUtils.startsWith(data, "生日")){
				user.setBirtyday(StringUtils.trim(StringUtils.substringAfter(data, "生日")));
			}
			
			if(StringUtils.startsWith(data, "血型")){
				user.setBloodType(StringUtils.trim(StringUtils.substringAfter(data, "血型")));
			}
			
			if(StringUtils.startsWith(data, "出生地")){
				user.setBirtyAddress(StringUtils.trim(StringUtils.substringAfter(data, "出生地")));
			}
			
			if(StringUtils.startsWith(data, "居住地")){
				user.setLiveAddress(StringUtils.trim(StringUtils.substringAfter(data, "居住地")));
			}
		}
		
		user.setName(task.getExtra());
		user.setUrl(task.getUrl());
		
		user.persistOnNotExist();
	}

}
