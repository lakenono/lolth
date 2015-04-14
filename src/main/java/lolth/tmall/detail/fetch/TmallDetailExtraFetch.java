package lolth.tmall.detail.fetch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lolth.tmall.detail.bean.TmallGoodsExtraBean;
import lolth.tmall.search.bean.TmallGoodsBean;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Slf4j
public class TmallDetailExtraFetch {
	private static final String TMALL_GOODS_DETAIL_URL_PREFIX = "http://detail.tmall.com/item.htm?id=";
	
	private Map<String,String> cookies;

	public static void main(String[] args) throws Exception {
		new TmallDetailExtraFetch().run();
	}

	private void run() throws SQLException {
		buildCookies();
		List<String> goodsIds = getTask();
		for (String id : goodsIds) {
			try {
				TmallGoodsExtraBean bean = parsePage(buildUrl(id));
				if(bean!=null){
					bean.setGoodsId(id);
					bean.persistOnNotExist();
				}
				log.info("{} save !" , bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("all finish!");
	}

	private void buildCookies() {
		String CookieStr = "isg=D76BA634B5EE01C3A345D03D0AF9D1A5; whl=-1%260%260%260; x=__ll%3D-1%26_ato%3D0; cna=12iZDYhr7CYCAT3oC8VdAX91; cookie2=1cd28149b3067a391868dd020b9eb57d; t=9d8b839b27f02fa6bbc6802652c6daaf; _tb_token_=HjFqAVGUdslu; ck1=; uc1=lltime=1427274129&cookie14=UoW1E75hXcSizg%3D%3D&existShop=false&cookie16=W5iHLLyFPlMGbLDwA%2BdvAGZqLg%3D%3D&cookie21=VFC%2FuZ9ainBZ&tag=2&cookie15=W5iHLLyFOGW7aA%3D%3D&pas=0; uc3=nk2=BJUHXc6NqA%3D%3D&id2=UUtDWh1iTKT%2B&vt3=F8dATk9kBhPIHKkXJyw%3D&lg2=VT5L2FSpMGV7TQ%3D%3D; lgc=gcard10; tracknick=gcard10; cookie1=UoSII9%2FFlWF69oG8lp%2B5bqBBE1qPT%2FA5S%2BSSvW0mklo%3D; unb=238566608; _nk_=gcard10; _l_g_=Ug%3D%3D; cookie17=UUtDWh1iTKT%2B; login=true; pnm_cku822=242UW5TcyMNYQwiAiwTR3tCf0J%2FQnhEcUpkMmQ%3D%7CUm5Ockt0TnFLckpySHZOeiw%3D%7CU2xMHDJ%2BH2QJZwBxX39Rb1F%2FX3EtTCpGIV8lC10L%7CVGhXd1llXGNZZlxlXWVfYVltWmdFeUV%2BS3ZMckhwTXdPe0B5Qn5QBg%3D%3D%7CVWldfS0RMQoxCCgUIQEveFZgFGANaRN%2BDGpXaAo6UzlpCiRyJA%3D%3D%7CVmhIGCIePgMjHyEYLAw1CDQMLBAuFS4ONA86GiYYIxg4Aj0IXgg%3D%7CV25Tbk5zU2xMcEl1VWtTaUlwJg%3D%3D; CNZZDATA1000279581=1575176889-1427272894-%7C1427272894; otherx=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; swfstore=93087";
		String[] cookiePairs = StringUtils.split(CookieStr, ";");
		cookies = new HashMap<>();
		
		for(String c : cookiePairs){
			try {
				String[] kv = StringUtils.split(c,"=");
				cookies.put(kv[0], kv[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private TmallGoodsExtraBean parsePage(String url) throws IOException, InterruptedException {
		Thread.sleep(1000);
		
		Connection connect = Jsoup.connect(url);
		connect.cookies(cookies);
		
		Document doc = connect.post();
		
		Elements div = doc.select("div.attributes");
		if (div.size() == 0) {
			return null;
		}

		String brand = "";
		String tiaoliao = "";

		//品牌
		Elements brandLi = div.first().select("li#J_attrBrandName");

		if (brandLi.size() > 0) {
			brand = brandLi.first().attr("title");
		}
		
		Elements tiaoliaoLi = div.first().getElementsMatchingOwnText("调味料:");
		if(tiaoliaoLi.size()>0){
			tiaoliao = tiaoliaoLi.first().attr("title");
		}
		if (StringUtils.isBlank(brand) && StringUtils.isBlank(tiaoliao)) {
			return null;
		}

		TmallGoodsExtraBean bean = new TmallGoodsExtraBean();
		bean.setBrand(StringUtils.trim(brand));
		bean.setTiaoliao(StringUtils.trim(tiaoliao));

		return bean;
	}

	private String buildUrl(String id) {
		return TMALL_GOODS_DETAIL_URL_PREFIX + id;
	}

	public List<String> getTask() throws SQLException {
		List<String> ids = GlobalComponents.db.getRunner().query("select id from data_tmall_goods where id not in (select goodsId from data_tmall_goods_extra);", new ColumnListHandler<String>());
		return ids;
	}
}
