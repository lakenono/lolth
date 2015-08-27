package bootstrap.bean;

import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class Lolth implements Runnable {
	@Getter
	protected long delay;
	@Getter
	protected TimeUnit timeUnit;

	public Lolth() {
		this(1, TimeUnit.SECONDS);
	}

	// 运行
	public abstract void run();
}
