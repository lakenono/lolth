package bootstrap;

import java.lang.reflect.InvocationTargetException;

import com.alibaba.fastjson.JSON;

public class Job extends Thread {
	public static enum JobState {
		DEFINE, RUNNING, END
	};

	private JobState state = JobState.DEFINE;

//	private JobClient jobClinet = null;

	private JobBean jobBean;

	private JobResult jobResult;

	public String startWait(String json) {
		jobResult = new JobResult();
		if (state != JobState.DEFINE) {
			jobResult.setState("error");
			jobResult.setMes("初始化任务状态不为DEFINE！");
			return toJsoin(jobResult);
		}
		try {
			jobBean = JSON.parseObject(json, JobBean.class);
		} catch (Exception e) {
			jobResult.setState("error");
			jobResult.setMes("解析json出错了！：[" + e.getMessage() + "]");
			return toJsoin(jobResult);
		}

		Class<?> cla = null;
		try {
			cla = Class.forName(jobBean.getClassName());
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			jobResult.setState("error");
			jobResult.setMes("className是否有错！：[" + e.getMessage() + "]");
			return toJsoin(jobResult);
		}

		Class<?>[] classArray = getClassArray(jobBean.getProjectName().getClass(), jobBean.getKeyword().getClass());
		Object[] objectArray = getObjectArray(jobBean.getProjectName(), jobBean.getKeyword());

		Runnable r = null;

		try {
			cla.asSubclass(Runnable.class).getConstructor(classArray).newInstance(objectArray);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			jobResult.setState("error");
			jobResult.setMes("初始化class出错了，className：" + jobBean.getClassName() + " [" + e.getMessage() + "]");
			return toJsoin(jobResult);
		}
		try {
			r.run();
		} catch (Exception e) {
			jobResult.setState("error");
			jobResult.setMes("类运行出错了，className：" + jobBean.getClassName() + "，projectName：" + jobBean.getProjectName() + "，keyword：" + jobBean.getKeyword() + "[" + e.getMessage() + "]");
			return toJsoin(jobResult);
		}

		jobResult.setState("ok");
		jobResult.setMes("job开始运行中！");
		state = JobState.RUNNING;
//		this.start();
		return toJsoin(jobResult);
	}

	public void runTaks() {

	}

	private Class<?>[] getClassArray(Class<?>... classes) {
		Class<?>[] clas = new Class<?>[classes.length];
		for (int i = 0; i < classes.length; i++) {
			clas[i] = String.class;
		}
		return clas;
	}

	private Object[] getObjectArray(Object... objects) {
		Object[] params = new Object[objects.length];
		for (int i = 0; i < objects.length; i++) {
			params[i] = objects[i];
		}
		return params;
	}

	private String toJsoin(Object obj) {
		return JSON.toJSONString(obj);
	}

	@Override
	public void run() {
		while (state == JobState.RUNNING) {
			
		}
	}

	public static void main(String[] args) {
		JobBean jobBean = new JobBean();
		jobBean.setKeyword("sfsf");
		System.out.println(jobBean.getKeyword().getClass().getTypeName().getClass());
		System.out.println(String.class);
	}
}
