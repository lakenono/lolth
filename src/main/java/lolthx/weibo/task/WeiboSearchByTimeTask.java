package lolthx.weibo.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeiboSearchByTimeTask {
	public static void main(String[] args) throws Exception {
		String[] keys = { "旅游", "郊游", "拓展训练", "亲子游", "农家乐", "休闲游", "农业观光游", "徒步", "自驾", "骑行", "漂流", "攀岩", "郊区", "远郊", "度假", "度假地产", "远郊地产", "郊区地产", "度假楼盘", "远郊楼盘", "度假小区", "远郊小区", "远郊地区", "远郊区域", "郊区楼盘", "郊区房产", "郊区景观", "生态农业", "生态绿色", "绿色农业", "别墅", "独栋", "联排", "叠拼", "房山", "上方山", "周吉祥塔", "应公长老寿塔", "金成明牌楼", "姚广孝墓塔", "白水寺石佛", "金陵遗址", "玉皇塔", "中粮万科长阳半岛", "中国铁建国际花园", "中国铁建·原香小镇", "中国山水醉", "长阳国际城", "长海御墅", "云居长河湾", "逸轩", "燕化星城", "旭辉E天地", "兴房苑", "西兰岛", "五矿铭品", "梧杖爱", "文兴家园", "万科幸福汇", "田家园", "天恒乐墅", "天鹅湖西峡谷", "梅花庄小区", "绿地新都会国际花都", "绿地诺亚方舟", "林海嘉园", "蓝爵公馆", "金域缇香", "金隅糖+", "建华北里", "建邦华庭", "嘉禾天地", "华银天鹅湖", "华风龙腾郡", "鸿都第一城", "合景领峰", "昊天嘉园", "昊腾花园", "韩建雅苑", "韩建青春誌", "拱辰星园", "富燕新村", "房山月华新村", "东亚朗悦居", "东礼胡同", "东关家园", "翠林漫步", "城关北里", "碧桂园九龙湾", "碧波园温泉家园", "北庄南里", "北京时代广场" };
		String projectName = "trip";

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		try {
			start.setTime(format.parse("20141016"));
			end.setTime(format.parse("20151016"));
		} catch (ParseException e) {
			log.error("parse time is error:{}", e.getMessage());
		}

		while (start.before(end)) {
			String endTime = format.format(end.getTime());
			for (String key : keys) {
				new WeiboSearchTask(projectName, key, endTime, endTime).run();
				Thread.sleep(15000);
			}
			log.info("{}时间任务处理完成", endTime);
			end.add(Calendar.DAY_OF_MONTH, -1);
		}
	}
}
