package bootstrap;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import lolth.hupu.bbs.HupuBBSListTask;
import lolth.hupu.bbs.HupuBBSTopicFetch;
import lolth.hupu.bbs.HupuBBSUserFetch;
import lolth.yhd.YhdFreshFetch;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class LolthBootStrap {

	public static final void shutdownCallback() {
		System.out.println("Shutdown callback is invoked.");
	}

	public static void main(String[] args) {
		// 回调
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				shutdownCallback();
			}
		});

		// 注册线程
		final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("lolth-%d").setDaemon(false).build();
		ScheduledExecutorService service = Executors.newScheduledThreadPool(2, threadFactory);
		service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				new HupuBBSListTask().run();
				new HupuBBSTopicFetch().run();
				new HupuBBSUserFetch().run();
			}
		}, 0, 3, TimeUnit.SECONDS);
	}
}
