package lolth.muying;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskCleaner {

	public static void main(String[] args) {
		String[] keywords = { "惠氏启赋","wyeth启赋","雅培菁致","多美滋致粹","合生元奶粉","诺优能白金版","美赞臣亲舒" };

		String[] batchNames = { MuYingCommoditySearchList.MUYING_SHOP_LIST, MuYingCommodityDetailTaskProducer.MUYING_SHOP_LIST_DETAIL, MuYingCommodityDetailFetch.MUYING_SHOP_DETAIL_COMMENT };

		for (String batchName : batchNames) {
			for (String keyword : keywords) {
				try {
					FetchTaskProducer.cleanAllTask(batchName);
					FetchTaskProducer.cleanAllTaskLog(keyword, batchName);
				} catch (SQLException e) {
					log.error("{} , {} clean error : ", keyword, batchName, e);
				}
			}
		}
	}
}
