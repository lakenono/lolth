package lolth.baidu.zhidao;

import java.sql.SQLException;

import lakenono.task.FetchTaskProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaiduZhidaoCleaner {
	public static void main(String[] args) {
		String keyword = "惠氏";

		String[] batchNames = { BaiduZhidaoListTaskProducer.BAIDU_ZHIDAO_LIST, BaiduZhidaoListFetch.BAIDU_ZHIDAO_DETAIL, BaiduZhidaoDetailFetch.BAIDU_ZHIDAO_USER };

		for (String batchName : batchNames) {
			try {
				FetchTaskProducer.cleanAllTask(batchName);
				FetchTaskProducer.cleanAllTaskLog(keyword, batchName);
			} catch (SQLException e) {
				log.error("{} , {} clean error : ", keyword, batchName, e);
			}
		}

	}
}
