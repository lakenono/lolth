package dmp.ec;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import lolthx.taobao.item.TaobaoItemFetch;
import lolthx.taobao.search.TaobaoSearchListFetch;
import lolthx.taobao.tmall.item.TmallItemFetch;
import lolthx.taobao.tmall.search.TmallSearchListFetch;
import lolthx.yhd.fetch.YHDGoodsFetch;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ECFetch {
	public static void main(String[] args) {

		final ThreadFactory Factory = new ThreadFactoryBuilder().setNameFormat("Lolth-%d").setDaemon(false).build();
		// 一号店
		ScheduledExecutorService ydh_service = Executors.newScheduledThreadPool(2, Factory);
		ydh_service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				new YHDGoodsFetch(false).run();
			}
		}, 0, 1, TimeUnit.SECONDS);

		// 淘宝
		ScheduledExecutorService taobao_service = Executors.newScheduledThreadPool(2, Factory);
		taobao_service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				TaobaoItemFetch taobao = new TaobaoItemFetch(false);
				taobao.useDynamicFetch();
				taobao.run();
			}
		}, 0, 1, TimeUnit.SECONDS);

		// 淘宝列表任务使用动态爬取
		ScheduledExecutorService tb_list_service = Executors.newScheduledThreadPool(2, Factory);
		tb_list_service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				TaobaoSearchListFetch taobao = new TaobaoSearchListFetch();
				taobao.useDynamicFetch();
				taobao.run();
			}
		}, 0, 5, TimeUnit.SECONDS);

		// 天猫
		ScheduledExecutorService tmall_service = Executors.newScheduledThreadPool(2, Factory);
		tmall_service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				new TmallItemFetch(false).run();
			}
		}, 0, 1, TimeUnit.SECONDS);

		// 天猫详情
		ScheduledExecutorService tm_list_service = Executors.newScheduledThreadPool(2, Factory);
		tm_list_service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				new TmallSearchListFetch(false).run();
			}
		}, 0, 5, TimeUnit.SECONDS);

	}
}
