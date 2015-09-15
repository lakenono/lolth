package dmp.ec;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import dmp.ec.amazon.AmazonGoodsFetch;
import dmp.ec.jd.ECJdItemFetch;
import dmp.ec.jd.ECJdListFetch;
import dmp.ec.taobao.ECTaobaoListFetch;
import dmp.ec.taobao.ECTmallListFetch;
import dmp.ec.yhd.ECYhdSearchFetch;

public class ECSearchFetch {
	public static void main(String[] args) {

		final ThreadFactory Factory = new ThreadFactoryBuilder().setNameFormat("Lolth-%d").setDaemon(false).build();
		// 一号店
		ScheduledExecutorService ydh_service = Executors.newScheduledThreadPool(2, Factory);
		ydh_service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				new ECYhdSearchFetch().run();
			}
		}, 0, 1, TimeUnit.SECONDS);

		// 淘宝 动态抓取
		ScheduledExecutorService taobao_service = Executors.newScheduledThreadPool(2, Factory);
		taobao_service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				ECTaobaoListFetch taobao = new ECTaobaoListFetch();
				taobao.useDynamicFetch();
				taobao.run();
			}
		}, 0, 1, TimeUnit.SECONDS);

		// 天猫
		ScheduledExecutorService tmall_service = Executors.newScheduledThreadPool(2, Factory);
		tmall_service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				new ECTmallListFetch().run();
			}
		}, 0, 1, TimeUnit.SECONDS);

		// 京东 list  模板有变化，需要修改
		ScheduledExecutorService jd_list_service = Executors.newScheduledThreadPool(2, Factory);
		jd_list_service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				new ECJdListFetch().run();
			}
		}, 0, 1, TimeUnit.SECONDS);
		
		//京东 item
		ScheduledExecutorService jd_item_service = Executors.newScheduledThreadPool(2, Factory);
		jd_item_service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				new ECJdItemFetch().run();
			}
		}, 0, 1, TimeUnit.SECONDS);

		//亚马逊
		ScheduledExecutorService amz_service = Executors.newScheduledThreadPool(2, Factory);
		amz_service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				new AmazonGoodsFetch().run();
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
}
