package dmp.ec;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import lolthx.suning.keysearch.SuningSearchByKeyProducer;
import lombok.extern.slf4j.Slf4j;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import dmp.ec.amazon.AmazonCategoryProducer;
import dmp.ec.jd.ECJdCategoryProducer;
import dmp.ec.taobao.ECTaobaoProducer;
import dmp.ec.taobao.ECTmallProducer;
import dmp.ec.yhd.ECYhdClassfiyProduce;

@Slf4j
public class ECCategoryTask {
	public static void main(String[] args) {

		String projectName = "ceshi";

		// 注册一号店线程
		final ThreadFactory Factory = new ThreadFactoryBuilder().setNameFormat("lolth-task-%d").setDaemon(false).build();
		ScheduledExecutorService yhdservice = Executors.newScheduledThreadPool(2, Factory);
		yhdservice.submit(new Runnable() {
			@Override
			public void run() {
				try {
					// 分类参数需要单独传进去
					new ECYhdClassfiyProduce(projectName, "c17223-0-81793").run();
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
					// 淘宝分类最后一个参数需单独传 &cat=51112007
					new ECTaobaoProducer(projectName, "", "&cat=51112007").run();
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
					// 天猫分类最后一个参数需单独传
					new ECTmallProducer(projectName, "", "&cat=50100155").run();
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
					// 京东分类最后一个参数需单独传
					new ECJdCategoryProducer(projectName, "").run();
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
					// 亚马逊分类参数需单独传
					new AmazonCategoryProducer(projectName, "160238071").run();
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
					SuningSearchByKeyProducer sn = new SuningSearchByKeyProducer(projectName, "20368", "电风扇");
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
