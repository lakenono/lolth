package bootstrap;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class JobBean {
	
	private String taskId;
	
	private String taskName;

	private String webClass;
	
	private List<Map<String,String>> classBeanList;
	
	private List<Map<String,String>> forumList;
	
	private String startDate;
	
	private String endDate;
	
	private String classType;
}
