package bootstrap;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lakenono.base.Producer;
import lakenono.db.DBBean;

import com.alibaba.fastjson.JSON;

public class Job {

	public static enum JobState {
		ERROR,DEFINE, RUNNING, END
	};
	
	
	public String startLolthTask(JobBean jobBean){
		boolean bool = true;
		if(jobBean != null){
			try {
				String taskName = jobBean.getTaskName();
				String taskId = jobBean.getTaskId();
				String webClass = jobBean.getWebClass();
				String startDate = jobBean.getStartDate();
				String endDate = jobBean.getEndDate();
				
				List<Map<String,String>> classBeanList = new ArrayList<Map<String,String>>();
				classBeanList = jobBean.getClassBeanList();
				
				List<Map<String,String>> forumList = new ArrayList<Map<String,String>>();
				forumList = jobBean.getForumList();
				
				if(classBeanList != null){
					for(int i = 0 ; i < classBeanList.size() ; i++){
						Map<String,String> classMap = new HashMap<String,String>();
						classMap = classBeanList.get(i);
						String classBean = classMap.get("classBean");
						this.createTable(classBean, taskId);
					}
					
					for(int i = 0 ; i < forumList.size() ; i++){
						Map<String,String> forumMap = new HashMap<String,String>();
						forumMap = forumList.get(i);
						String forumId = forumMap.get("forumId");
						String keyword = forumMap.get("keyword");
						
						this.classProducer(webClass, taskName, taskId, forumId, keyword, startDate, endDate);
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "2";
			}
		}
		return "1";
	}
	
	public void  createTable(String classBean , String taskId ) throws ClassNotFoundException, SQLException{
		
		DBBean.createTable((Class<? extends DBBean>) Class.forName(classBean), taskId);
		
	}
	
	public boolean classProducer(String webClass,String projectName, String taskId,String forumId, String keyword,String startDate,String endDate){
		try {
			System.out.println(" >>>>>>>>>>> : >  webClass " +  webClass);
			System.out.println(" >>>>>>>>>>> : > projectName " +  projectName);
			System.out.println(" >>>>>>>>>>> : > taskId " + taskId);
			System.out.println(" >>>>>>>>>>> : > forumId " + forumId );
			System.out.println(" >>>>>>>>>>> : > keyword " + startDate);
			System.out.println(" >>>>>>>>>>> : > endDate" + endDate);
			
			((Class<? extends Producer>)Class.forName(webClass)).getConstructor(String.class,String.class,String.class,String.class,String.class,String.class).newInstance(projectName, taskId, forumId, keyword, startDate, endDate).run();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return false;
	}
	

	public static void main(String[] args) {
		JobBean jobBean = new JobBean();
		
		jobBean.setTaskId("100001");
		jobBean.setTaskName("1000001");
		jobBean.setWebClass("lolthx.autohome.bbs.AutoHomeBBSListProducer");
		jobBean.setStartDate("2015-01-01");
		jobBean.setEndDate("2016-01-01");
		List<Map<String,String>> classBeanList = new ArrayList<Map<String,String>>();
		Map<String,String> classMap1 = new HashMap<String,String>();
		classMap1.put("classBean", "lolthx.autohome.bbs.bean.AutoHomeBBSBean");
		classBeanList.add(classMap1);
		Map<String,String> classMap2 = new HashMap<String,String>();
		classMap2.put("classBean", "lolthx.autohome.bbs.bean.AutoHomeBBSCommentBean");
		classBeanList.add(classMap2);
		Map<String,String> classMap3 = new HashMap<String,String>();
		classMap3.put("classBean", "lolthx.autohome.bbs.bean.AutoHomeBBSUserBean");
		classBeanList.add(classMap3);
		jobBean.setClassBeanList(classBeanList);
		
		List<Map<String,String>> forumList = new ArrayList<Map<String,String>>();
		Map<String,String> forumMap1 = new HashMap<String,String>();
		forumMap1.put("id", "18");
		forumMap1.put("keyword", "A6L");
		forumList.add(forumMap1);
		Map<String,String> forumMap2 = new HashMap<String,String>();
		forumMap2.put("id", "403");
		forumMap2.put("keyword", "Lexus ES");
		forumList.add(forumMap2);
		Map<String,String> forumMap3 = new HashMap<String,String>();
		forumMap3.put("id", "66");
		forumMap3.put("keyword", "BMW 3Li");
		forumList.add(forumMap3);
		jobBean.setForumList(forumList);
		
		Job job = new Job();
		job.startLolthTask(jobBean);
		
	}
}
