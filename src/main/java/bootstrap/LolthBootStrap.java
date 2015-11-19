package bootstrap;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import lolthx.autohome.bbs.AutoHomeBBSCommentFetch;
import lolthx.autohome.bbs.AutoHomeBBSListFetch;
import lolthx.autohome.buy.AutohomePriceListFetch;
import lolthx.autohome.k.AutoHomeWordOfMouthFetch;
import lolthx.baidu.news.BaiduNewsListFetch;
import lolthx.baidu.news.BaiduNewsNumIteratorFetch;
import lolthx.baidu.news.BaiduNewsNumListFetch;
import lolthx.baidu.post.BaiduPostDetailByKwFirstFetch;
import lolthx.baidu.post.BaiduPostDetailByKwSecondFetch;
import lolthx.baidu.post.BaiduPostDetailFetch;
import lolthx.baidu.post.BaiduPostListFetch;
import lolthx.baidu.visualize.BaiduNewsVisListFetch;
import lolthx.baidu.visualize.BaiduWebpageVisFirstFetch;
import lolthx.baidu.visualize.BaiduWebpageVisListFetch;
import lolthx.baidu.visualize.BaiduWebpageVisSecondFetch;
import lolthx.baidu.webpage.BaiduSpotsListFetch;
import lolthx.baidu.webpage.BaiduWebSiteFetch;
import lolthx.baidu.webpage.BaiduWebpageListFetch;
import lolthx.baidu.zhidao.BaiduZhidaoDetailFetch;
import lolthx.baidu.zhidao.BaiduZhidaoListFetch;
import lolthx.baidu.zhidao.BaiduZhidaoUserFetch;
import lolthx.bitauto.bbs.BitautoBBSCommentFetch;
import lolthx.bitauto.bbs.BitautoBBSListFetch;
import lolthx.bitauto.k.BitautoWordOfMouthFetch;
import lolthx.douban.search.DoubanSearchCommentFetch;
import lolthx.douban.search.DoubanSearchDetailFetch;
import lolthx.douban.search.DoubanSearchResloveFetch;
import lolthx.pacuto.bbs.PacutoBBSListFetch;
import lolthx.pacuto.k.PacutoWordOfMouthListFetch;
import lolthx.weibo.fetch.WeiboFansNumFetch;
import lolthx.weibo.fetch.WeiboSearchFetch;
import lolthx.weibo.fetch.WeiboUserFetch;
import lolthx.weibo.fetch.WeiboUserTagFetch;
import lolthx.weixin.sogou.WeiXinArticleListFetch;
import lolthx.weixin.sogou.WeiXinUserArtListFetch;
import lolthx.xcar.bbs.XCarBBSListFetch;
import lolthx.xcar.k.XCarWordOfMouthListFetch;
import lolthx.yhd.fetch.YHDGoodsFetch;
import lolthx.yoka.bbs.YokaBBSComment;
import lolthx.yoka.bbs.YokaBBSDetail;
import lolthx.yoka.bbs.YokaBBSListResolveFetch;
import lolthx.yoka.cosmetics.YokaCosmeticDetailFetch;
import lolthx.yoka.cosmetics.YokaCosmeticListFetch;
import lolthx.yoka.cosmetics.YokaCosmeticResolveFetch;
import lolthx.yoka.cosmetics.YokaCosmeticUserFetch;
import lombok.extern.slf4j.Slf4j;
import bootstrap.bean.Lolth;
import bootstrap.bean.LolthSolider;
import bootstrap.bean.LolthSoliderGroup;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Slf4j
public class LolthBootStrap {

	// lolth fetch 列表
	private List<Lolth> lolths = new LinkedList<>();

	// 添加要启动的Fetch
	public void addLolth(Lolth lolth) {
		lolths.add(lolth);
	}

	/**
	 * 添加任务
	 * 
	 * @param solider
	 */
	public void addLolthSolider(Runnable solider) {
		addLolth(new LolthSolider(solider));
	}

	/**
	 * 添加可指定时间的任务
	 * 
	 * @param delay
	 * @param timeUnit
	 * @param solider
	 */
	public void addLolthSolider(long delay, TimeUnit timeUnit, Runnable solider) {
		addLolth(new LolthSolider(delay, timeUnit, solider));
	}

	public void addLolthSoliderGroup(String groupName, Runnable solider) {
		addLolth(new LolthSoliderGroup(groupName, solider));
	}

	public void addLolthSoliderGroup(String groupName, long delay, TimeUnit timeUnit, Runnable... soliders) {
		addLolth(new LolthSoliderGroup(groupName, delay, timeUnit, soliders));
	}

	public void run() {
		// 初始化线程工厂
		final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Lolth-Thread-%d").setDaemon(false).build();

		// 初始化线程池，线程池大小同fetch数量
		ScheduledExecutorService lolthThreadPools = Executors.newScheduledThreadPool(lolths.size(), threadFactory);

		// 添加定时任务
		for (Lolth lolth : lolths) {
			lolthThreadPools.scheduleWithFixedDelay(lolth, 0, lolth.getDelay(), lolth.getTimeUnit());
			log.info("{} start! ", lolth);
		}

		log.info("start finish! Thread Size: [{}] ", lolths.size());
	}

	public static final void shutdownCallback() {
		System.out.println("Shutdown callback is invoked.");
	}

	/**
	 * 启动入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 回调
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				shutdownCallback();
			}
		});

		log.info("Lolth boot strap ...");
		LolthBootStrap lolthBootStrap = new LolthBootStrap();

		// 添加要启动的fetch
		build(lolthBootStrap);

		// 启动
		lolthBootStrap.run();

		log.info("Lolth boot finish !");
	}

	// ---------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 添加fetch
	 * 
	 * @param lolthBootStrap
	 */
	public static void build(LolthBootStrap lolthBootStrap) {
		// 使用默认延迟
		lolthBootStrap.addLolthSolider(new YHDGoodsFetch(false));

		// 使用制定延迟时间
		// lolthBootStrap.addLolthSolider(1, TimeUnit.SECONDS, new
		// YHDGoodsFetch(false))
		
		// 汽车之家  add by wuhao 20150827
		lolthBootStrap.addLolthSolider(1, TimeUnit.SECONDS,new AutoHomeBBSListFetch());
		lolthBootStrap.addLolthSolider(new AutoHomeBBSCommentFetch());
		lolthBootStrap.addLolthSolider(new AutohomePriceListFetch());
		lolthBootStrap.addLolthSolider(new AutoHomeWordOfMouthFetch());
		
		//易车网
		lolthBootStrap.addLolthSolider(new BitautoBBSListFetch());
		lolthBootStrap.addLolthSolider(new BitautoBBSCommentFetch());
		lolthBootStrap.addLolthSolider(new BitautoWordOfMouthFetch());
		
		
		//太平洋
		lolthBootStrap.addLolthSolider(new PacutoBBSListFetch());
		lolthBootStrap.addLolthSolider(new PacutoWordOfMouthListFetch());
		
		//爱卡
		lolthBootStrap.addLolthSolider(new XCarBBSListFetch());
		lolthBootStrap.addLolthSolider(new XCarWordOfMouthListFetch());
		
		
		//微信
		lolthBootStrap.addLolthSolider(30, TimeUnit.SECONDS,new WeiXinArticleListFetch());
		lolthBootStrap.addLolthSolider(30, TimeUnit.SECONDS,new WeiXinUserArtListFetch());
		
		//百度新闻
		lolthBootStrap.addLolthSolider(new BaiduNewsListFetch());
		//百度贴吧
		lolthBootStrap.addLolthSolider(new BaiduPostListFetch());
		lolthBootStrap.addLolthSolider(new BaiduPostDetailFetch());
		lolthBootStrap.addLolthSolider(new BaiduPostDetailByKwFirstFetch());
		lolthBootStrap.addLolthSolider(new BaiduPostDetailByKwSecondFetch());
		//百度知道
		lolthBootStrap.addLolthSolider(new BaiduZhidaoListFetch());
		lolthBootStrap.addLolthSolider(new BaiduZhidaoDetailFetch());
		lolthBootStrap.addLolthSolider(new BaiduZhidaoUserFetch());
		//百度 国家形象 特出填写类
		lolthBootStrap.addLolthSolider(new BaiduWebpageListFetch());//keyword 搜索关键字 number
		lolthBootStrap.addLolthSolider(new BaiduSpotsListFetch());//处理城市联系词汇
		lolthBootStrap.addLolthSolider(new BaiduWebSiteFetch());//处理城市主词汇方法
		lolthBootStrap.addLolthSolider(new BaiduNewsNumListFetch());//搜索百度新闻number
		lolthBootStrap.addLolthSolider(new BaiduNewsNumIteratorFetch());//处理百度新闻主词汇
		//百度 国家形象 百度网页 百度新闻 内容爬取
		lolthBootStrap.addLolthSolider(new BaiduNewsVisListFetch());
		lolthBootStrap.addLolthSolider(new BaiduWebpageVisFirstFetch());
		lolthBootStrap.addLolthSolider(new BaiduWebpageVisSecondFetch());
		lolthBootStrap.addLolthSolider(new BaiduWebpageVisListFetch());
		
		//微博 国家形象
		lolthBootStrap.addLolthSolider(15, TimeUnit.SECONDS,new WeiboSearchFetch());
		lolthBootStrap.addLolthSolider(15, TimeUnit.SECONDS,new WeiboUserFetch());
		lolthBootStrap.addLolthSolider(15, TimeUnit.SECONDS,new WeiboFansNumFetch());
		lolthBootStrap.addLolthSolider(15, TimeUnit.SECONDS,new WeiboUserTagFetch());
		
		//YOKA
		lolthBootStrap.addLolthSolider(new YokaBBSListResolveFetch());
		lolthBootStrap.addLolthSolider(new YokaBBSDetail());
		lolthBootStrap.addLolthSolider(new YokaBBSComment());
		lolthBootStrap.addLolthSolider(new YokaCosmeticResolveFetch());
		lolthBootStrap.addLolthSolider(new YokaCosmeticListFetch());
		lolthBootStrap.addLolthSolider(new YokaCosmeticDetailFetch());
		lolthBootStrap.addLolthSolider(new YokaCosmeticUserFetch());
		
		//豆瓣
		lolthBootStrap.addLolthSolider(30, TimeUnit.SECONDS,new DoubanSearchResloveFetch());
		lolthBootStrap.addLolthSolider(30, TimeUnit.SECONDS,new DoubanSearchDetailFetch());
		lolthBootStrap.addLolthSolider(30, TimeUnit.SECONDS,new DoubanSearchCommentFetch());
		
	}
	// ---------------------------------------------------------------------------------------------------------------------------------
}
