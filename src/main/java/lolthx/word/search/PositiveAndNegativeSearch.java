package lolthx.word.search;

import java.util.List;

import lakenono.core.GlobalComponents;
import lakenono.db.DBBean;
import lolthx.baidu.visualize.bean.BaiduWebpageVisBean;
import lolthx.word.bean.PositiveAndNegativeWord;

import org.apache.commons.dbutils.handlers.BeanListHandler;

public class PositiveAndNegativeSearch {
	
	public static void main(String[] args){
		
		String[] citys = {"安庆","保定","北京","沧州", "常德","常州","成都","大连","德州","东莞",
				"佛山","福州","赣州","广州","贵阳","哈尔滨","海口","邯郸","杭州","合肥",
				"菏泽","衡阳","呼和浩特","湖州","淮安","惠州","吉安","吉林","济南","济宁",
				"嘉兴","江门","焦作","金华","九江","昆明","拉萨","兰州","聊城","临沂",
				"洛阳","茂名","南昌","南京","南宁","南通","南阳","宁波","青岛","泉州",
				"厦门","上海","上饶","绍兴","深圳","沈阳","石家庄","苏州","台州","太原",
				"泰安","唐山","天津","威海","潍坊","渭南","温州","乌鲁木齐","无锡","武汉",
				"西安","西宁","咸阳","襄阳","新乡","宿迁","徐州","烟台","盐城","扬州","宜昌",
				"银川","岳阳","运城","湛江","漳州","长春","长沙","镇江","郑州","重庆",
				"周口","珠海","淄博","遵义","桂林","鞍山","大庆","泰州","包头"
			}; 
		
		String[] positive =  {
			"文明","卫生","优良","蓝天","完善","绿色","著名","特色","发展","成效",
			"提升","增强","有序","支持","活跃","历史","文化","升华","开发","促进",
			"改造","加快","满意","吸引","推进","便捷","规范","安全","积极","鼓励",
			"创新","信息化","健全","表彰","和谐","改善","友好","合作","加强","整治",
			"示范","智慧","品质","统一","平稳","惠民","合理","宜居","集约","可持续",
			"美化","低碳","平衡","发达","领先","回暖","中高端化","回升","缓解","功臣",
			"开源节流","开放","明朗","自主研发","带动","稳定","改革","强劲实力","智能化","先进",
			"产融结合","涨势","增加","加大","改造","更新","上升","鼓励","涨幅","猛增",
			"提高","晋升","受益","争取","加快","建设","传承","创新","展示","经济适用",
			"实现","高效","希望","解决","机会","修订","伟业","继承","节节攀升","高达",
			"重任","支持","拓展","良好","倡导","发挥作用","监督","处理","措施","整治",
			"深化","强化","治理","稳定","整改","捣毁","抓获","查清","铲除","推动",
			"扶持","不断壮大","引以为戒","扼制","打击","修缮","维护","拉动","持续升温","倡议",
			"华丽转身","升值潜力","整改","积极作为","突破","扩增","落实","升级","有力支撑","全面发展",
			"严格执行","新引擎","机遇","崛起","推进","振兴","优化"	
		};
		String[] negative =  {
			"危险","暴力","犯罪","案件","担忧","污染","批评","差距","薄弱","下降","落后",
			"困难","问题","垃圾","脏","乱","差","曝光","缺失","隐患","事故",
			"违法","纠纷","盲目","嘈杂","失业","崩溃","腐败","拥堵","矛盾","恶化",
			"债务","短板","滞后","不足","限制","缺乏","严重","雾霾","堵车","衰退",
			"下滑","萎缩","下跌","紧张","转差","回落","偏弱","降低","不利","减少",
			"减弱","萎缩","恐慌","过度","吐槽","脆弱","无能为力","不以为然","视而不见","声讨",
			"欠妥","投诉","擅自","抗议","愤懑","错误","冲突","调解","谴责","安全隐患",
			"起诉","争端","贪污","侵占","虚报","冒领","侵害","粗暴","不合格","危害",
			"误导","销毁","伪造","假冒","逮捕","虚假","敲诈","处罚","处理","不实",
			"未经核实","编造","漂浮","失实","诱骗","强迫","威胁","侮辱","乱象","欺诈",
			"乘人之危","负面","纠纷","造假","徇私舞弊","滥用职权","谋取私利","无序","隐患","不规范",
			"违纪","违法","涉嫌","下滑","爆料","事态升级","漏洞百出","挤","肮脏","卑鄙",
			"背地里","不干不净","不公正","不合法","不合理","不堪一击","不平衡","不平等","不切实际","不清不楚",
			"不人道","不透明","不卫生","不文明","不稳定","不现实","不协调","残酷","惨淡",
			"陈腐","陈旧","奢靡","丑陋","臭名远扬","臭名昭著","粗糙","粗暴","低贱","低劣",
			"恶劣","不方便","毫无意义","毫无价值","毫无用处","失望","铺张浪费","强横","散乱","散漫",
			"奢靡","无益","无意义","虚假",
		};
		
		int[] posiInt = {
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0
		};
		
		int[] negaInt = {
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
				0,0,0,0
		};
		
		
		System.out.println("城市文明正负面词汇");
		String projectName = "城市文明";
		//String projectName = "政府微信";
		//String projectName = "媒体评价";
		//String projectName = "微博信息";
		
		for(int i = 0 ; i <citys.length ; i++){
			try {
				List<BaiduWebpageVisBean> list =  GlobalComponents.db.getRunner().query("select * from " + DBBean.getTableName(BaiduWebpageVisBean.class) + " where projectName=? and city=? ", new BeanListHandler<>(BaiduWebpageVisBean.class), projectName,citys[i]);
				/**
				List<WeiXinBean> list =  GlobalComponents.db.getRunner().query("select * from " + DBBean.getTableName(WeiXinBean.class) + 
						" where projectName=?  and postTime like '2014-8-%' or "+
								"postTime like '2014-9-%' or postTime like '2014-10-%' or  postTime like '2014-11-%' or postTime like '2014-12-%' or postTime like '2015-1-%' or  "+
								"postTime like '2015-2-%' or  postTime like '2015-3-%' or  postTime like '2015-4-%' or  postTime like '2015-5-%' or postTime like '2015-6-%' or  "+
								"postTime like '2015-7-%' or  postTime like '2015-8-%' ", new BeanListHandler<>(WeiXinBean.class), projectName);
				*/
				//List<BaiduNewsVisBean> list =  GlobalComponents.db.getRunner().query("select * from " + DBBean.getTableName(BaiduNewsVisBean.class) + " where projectName=? and city=? ", new BeanListHandler<>(BaiduNewsVisBean.class), projectName,citys[i]);
				//List<Map<String, Object>> list =  GlobalComponents.db.getRunner().query("select * from data_sina_weibo_country ", new MapListHandler());

				System.out.println(">>>> list.size" + list.size());
				
				int zhengmian = 0;
				int fumian = 0;
				int pinci = list.size();
				
				for(int n = 0 ; n < list.size() ; n++){
					StringBuilder sb =  new StringBuilder();
					String title = "";
					String content= "";
					
					if(list.get(n).getTitle() != null){
						title = list.get(n).getTitle();//城市文明,微信,媒体评价
					}
					if( list.get(n).getText() != null){
						content = list.get(n).getText();//城市文明,微信，媒体评价
					}
					
					/**
					if(list.get(n).get("text") != null){
						title = list.get(n).get("text").toString();
					}
					if( list.get(n).get("mainText") != null){
						content = list.get(n).get("mainText").toString();
					}
					*/
					
					sb.append(title).append(content);
					
					//if(sb.indexOf(citys[i]) > 0){
						//pinci = pinci + 1;
						for(int m = 0 ; m < positive.length ; m++){
							//posiInt[m] = sb.indexOf(positive[m]) > 0?posiInt[m] + 1:posiInt[m];
							if(sb.indexOf(positive[m]) > 0){
								zhengmian = zhengmian + 1;
								break;
							}
						}
						
						for(int m = 0 ; m < negative.length ; m++){
							//negaInt[m] = sb.indexOf(negative[m]) > 0?negaInt[m] + 1:negaInt[m];
							if(sb.indexOf(negative[m]) > 0){
								fumian = fumian + 1;
								break;
							}
							
						}
					}
				//}
				
				PositiveAndNegativeWord bean = new PositiveAndNegativeWord();
				bean.setId(projectName);
				bean.setKeyword(citys[i]);
				bean.setPostiveOrNegative("PostiveOrNegative");
				bean.setStr0(String.valueOf(pinci));
				bean.setStr1(String.valueOf(zhengmian));
				bean.setStr2(String.valueOf(fumian));
				
				bean.saveOnNotExist();	
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
		
	}
	
}
