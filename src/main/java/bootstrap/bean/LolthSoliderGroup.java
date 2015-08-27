package bootstrap.bean;

import java.util.concurrent.TimeUnit;

/**
 * lolth 士兵组
 * 
 * @author shilei
 *
 */
public class LolthSoliderGroup extends Lolth {
	private String info;
	private String groupName;
	private Runnable[] soldiers;

	public LolthSoliderGroup(String groupName, Runnable... soldiers) {
		super();
		this.groupName = groupName;
		this.soldiers = soldiers;
	}

	public LolthSoliderGroup(String groupName, long delay, TimeUnit timeUnit, Runnable... soldiers) {
		super(delay, timeUnit);
		this.groupName = groupName;
		this.soldiers = soldiers;
	}

	@Override
	public void run() {
		for (int i = 0; i < soldiers.length; i++) {
			soldiers[i].run();
		}
	}

	@Override
	public String toString() {
		if (info == null) {
			info = String.format("Name : %s , Delay : %d , TimeUnit : %s ", groupName, super.delay, super.timeUnit);
		}
		return info;
	}
}