package bootstrap;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import lolthx.yhd.fetch.YHDGoodsFetch;
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

		LolthBootStrap lolthBootStrap = new LolthBootStrap();

		// 添加要启动的fetch------------------------------------
		
		// 使用默认延迟
		lolthBootStrap.addLolthSolider(new YHDGoodsFetch(false));
		
		// 使用制定延迟时间
//		lolthBootStrap.addLolthSolider(1, TimeUnit.SECONDS, new YHDGoodsFetch(false));

		// 启动------------------------------------------------
		lolthBootStrap.run();
	}
}
