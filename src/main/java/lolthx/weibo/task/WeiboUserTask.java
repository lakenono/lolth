package lolthx.weibo.task;

import java.io.IOException;
import java.util.List;

import lolthx.weibo.fetch.WeiboSearchFetch;
import lolthx.weibo.utils.WeiboFileUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

@Slf4j
public class WeiboUserTask {
	
	public WeiboSearchFetch weibo = new WeiboSearchFetch();

	public static void main(String[] args) throws IOException {
		WeiboUserTask wb = new WeiboUserTask();
		wb.run();
	}

	private void run() throws IOException {
		String dir = this.getClass().getResource("/") + "weiboUser";
		//删除macox获取文件路径带file:
		dir = StringUtils.substringAfter(dir, ":");
		String file = WeiboFileUtils.rename2Temp(dir);
		if(file == null){
			log.info("no task file,Program exits!!!");
			return;
		}
		log.info("task begin is :{}", file);
		String projectName = WeiboFileUtils.getProjectName(file);
		List<String> readFile = WeiboFileUtils.readFile(file);
		for (String id : readFile) {
			String userUrl = "";
			if (StringUtils.isNumeric(id)) {
				userUrl = "http://weibo.cn/u/" + id;
			} else {
				userUrl = "http://weibo.cn/" + id;
			}
			weibo.bulidWeiboUserTask(id, userUrl, projectName);
		}
		WeiboFileUtils.rename2Done(file);
	}
}
