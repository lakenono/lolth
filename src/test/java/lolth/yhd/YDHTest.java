package lolth.yhd;

import lakenono.base.Task;
import lakenono.core.GlobalComponents;
import lolth.yhd.ask.fetch.YHDAskFetch;

public class YDHTest {
	public static void main(String[] args) throws Exception {
		String url = "http://item-home.yhd.com/item/ajax/ajaxDetailViewQA.do?product.id=25363717&pmId=29778309&questionPamsMode.questiontype=0&pagenationVO.currentPage=1";
		String text = GlobalComponents.jsonFetch.text(url);
		YHDAskFetch yhd = new YHDAskFetch();
		Task t = new Task();
		t.setUrl(url);
		yhd.parse(text, t);
	}
}	
