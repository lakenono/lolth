package bootstrap;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import lolthx.yhd.ask.fetch.YHDAskFetch;
import lolthx.yhd.comment.fetch.YHDCommentFetch;

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
				new YHDCommentFetch().run();
			}
		}, 0, 5, TimeUnit.SECONDS);
		
		final ThreadFactory threadFactory2 = new ThreadFactoryBuilder().setNameFormat("lolth-topic-%d").setDaemon(false).build();
		ScheduledExecutorService service2 = Executors.newScheduledThreadPool(2, threadFactory2);
		service2.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
//				new AutoHomeBBSTopicFetch().run();
				YHDAskFetch yhd = new YHDAskFetch();
//				yhd.userJsonFetch();
				yhd.run();
			}
		}, 0, 5, TimeUnit.SECONDS);
	}
}
