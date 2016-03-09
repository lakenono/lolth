package lolthx.sogou.wenwen;

import lakenono.base.Producer;

public class SogouWenwenListProducer extends Producer{
	
	private static final String SOGOU_WENWEN_LIST_URL = "";
	
	private String keyword;
	
	public SogouWenwenListProducer(String projectName,String keyword) {
		super(projectName);
		this.keyword = keyword;
	}

	@Override
	public String getQueueName() {
		return "";
	}

	@Override
	protected int parse() throws Exception {
		return 0;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return null;
	}

	
	
	
}
