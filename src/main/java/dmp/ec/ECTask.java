package dmp.ec;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import lolthx.taobao.search.TaobaoSearchListProducer;
import lolthx.taobao.tmall.search.TmallSearchListProducer;
import lolthx.yhd.task.YhdSearchProduce;
import lombok.extern.slf4j.Slf4j;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * 根据搜索关键字产生电商抓取队列 目前支持的电商:淘宝、天猫、京东、苏宁,一号店，亚马逊
 * 在生产队列时，先创建bean生产表
 * @author yanghp
 *
 */
@Slf4j
public class ECTask {

	public static void main(String[] args) throws Exception {

		String projectName = "ceshi";
		String keyword = "蓝牙耳机";

		// 注册一号店线程
		final ThreadFactory Factory = new ThreadFactoryBuilder().setNameFormat("yhd-task-%d").setDaemon(false).build();
		ScheduledExecutorService yhdservice = Executors.newScheduledThreadPool(2, Factory);
		yhdservice.submit(new Runnable() {

			@Override
			public void run() {
				try {
					new YhdSearchProduce(projectName, keyword).run();
				} catch (Exception e) {
					log.error("一号店task任务失败 {}", e);
				}
			}
		});
		// 关闭线程
		yhdservice.shutdown();

		// 注册淘宝线程
		ScheduledExecutorService taobaoservice = Executors.newScheduledThreadPool(2, Factory);
		taobaoservice.submit(new Runnable() {

			@Override
			public void run() {
				try {
					new TaobaoSearchListProducer(projectName, keyword).run();
				} catch (Exception e) {
					log.error("淘宝task任务失败 {}", e);
				}
			}
		});
		taobaoservice.shutdown();

		// 天猫 TmallSearchListProducer
		ScheduledExecutorService tmallservice = Executors.newScheduledThreadPool(2, Factory);
		tmallservice.submit(new Runnable() {

			@Override
			public void run() {
				try {
					new TmallSearchListProducer(projectName, keyword).run();
				} catch (Exception e) {
					log.error("天猫task任务失败 {}", e);
				}
			}
		});
		tmallservice.shutdown();

		/*
		 * // 一号店 ECYhdClassfiyProduce yhd = new ECYhdClassfiyProduce(projectName,
		 * keyword); yhd.run(); // 淘宝列表页任务队列 TaobaoSearchListProducer taobao =
		 * new TaobaoSearchListProducer(projectName, keyword); taobao.run(); //
		 * 淘宝商品详细页队列，产生天猫和淘宝的详情任务，需要TmallItemFetch去抓天猫的商品 // tmall的任务队列
		 * TmallSearchListProducer tmall = new
		 * TmallSearchListProducer(projectName, keyword); tmall.run(); // 京东任务,
		 * 还要再启动京东详情页task JDCommoditySearchList jd = new
		 * JDCommoditySearchList(projectName, keyword); jd.run();
		 * 
		 * // 苏宁
		 */
	}
}
