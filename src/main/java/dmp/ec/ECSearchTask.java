package dmp.ec;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import lolthx.suning.keysearch.SuningSearchListProducer;
import lolthx.yhd.task.YhdSearchProduce;
import lombok.extern.slf4j.Slf4j;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import dmp.ec.amazon.AmazonSearchProducer;
import dmp.ec.jd.ECJdKeywordProducer;
import dmp.ec.taobao.ECTaobaoProducer;
import dmp.ec.taobao.ECTmallProducer;

/**
 * 根据搜索关键字产生电商抓取队列 目前支持的电商:淘宝、天猫、京东、苏宁,一号店，亚马逊 在生产队列时，先创建bean生产表
 * 
 * @author yanghp
 *
 */
@Slf4j
public class ECSearchTask {

	public static void main(String[] args) throws Exception {

		String projectName = "ceshi";
		String keyword = "月饼";

		// 注册一号店线程
		final ThreadFactory Factory = new ThreadFactoryBuilder().setNameFormat("lolth-task-%d").setDaemon(false).build();
		ScheduledExecutorService yhdservice = Executors.newScheduledThreadPool(2, Factory);
		yhdservice.submit(new Runnable() {
			@Override
			public void run() {
				try {
					YhdSearchProduce yhd = new YhdSearchProduce(projectName, keyword);
					yhd.setECQueue();
					yhd.run();
				} catch (Exception e) {
					log.error("一号店task任务失败 {}", e);
				}
			}
		});
		yhdservice.shutdown();

		// 注册淘宝线程
		ScheduledExecutorService taobaoservice = Executors.newScheduledThreadPool(2, Factory);
		taobaoservice.submit(new Runnable() {

			@Override
			public void run() {
				try {
					new ECTaobaoProducer(projectName, keyword, "").run();
				} catch (Exception e) {
					log.error("淘宝task任务失败 {}", e);
				}
			}
		});
		taobaoservice.shutdown();

		// 天猫
		ScheduledExecutorService tmallservice = Executors.newScheduledThreadPool(2, Factory);
		tmallservice.submit(new Runnable() {

			@Override
			public void run() {
				try {
					new ECTmallProducer(projectName, keyword, "").run();
				} catch (Exception e) {
					log.error("天猫task任务失败 {}", e);
				}
			}
		});
		tmallservice.shutdown();
		// 京东
		ScheduledExecutorService jdservice = Executors.newScheduledThreadPool(2, Factory);
		jdservice.submit(new Runnable() {

			@Override
			public void run() {
				try {
					new ECJdKeywordProducer(projectName, keyword, "").run();
				} catch (Exception e) {
					log.error("京东task任务失败 {}", e);
				}
			}
		});
		jdservice.shutdown();
		// 亚马逊
		ScheduledExecutorService amzservice = Executors.newScheduledThreadPool(2, Factory);
		amzservice.submit(new Runnable() {

			@Override
			public void run() {
				try {
					new AmazonSearchProducer(projectName, keyword).run();
				} catch (Exception e) {
					log.error("亚马逊task任务失败 {}", e);
				}
			}
		});
		amzservice.shutdown();
		// 苏宁
		ScheduledExecutorService snservice = Executors.newScheduledThreadPool(2, Factory);
		snservice.submit(new Runnable() {

			@Override
			public void run() {
				try {
					SuningSearchListProducer sn = new SuningSearchListProducer(projectName, keyword);
					sn.setECQueue();
					sn.run();
				} catch (Exception e) {
					log.error("亚马逊task任务失败 {}", e);
				}
			}
		});
		snservice.shutdown();
	}
}
