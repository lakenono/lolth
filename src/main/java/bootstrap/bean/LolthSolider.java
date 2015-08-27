package bootstrap.bean;

import java.util.concurrent.TimeUnit;

/**
 * lolth 士兵
 * 
 * @author shilei
 *
 */
public class LolthSolider extends Lolth {

	private Runnable soldier;
	private String info;

	public LolthSolider(Runnable soldier) {
		super();
		this.soldier = soldier;
	}

	public LolthSolider(long delay, TimeUnit timeUnit, Runnable soldier) {
		super(delay, timeUnit);
		this.soldier = soldier;
	}

	@Override
	public void run() {
		soldier.run();
	}

	@Override
	public String toString() {
		if (info == null) {
			info = String.format("Name : %s , Delay : %d , TimeUnit : %s ", soldier.getClass().getSimpleName(), super.delay, super.timeUnit);
		}
		return info;
	}
}