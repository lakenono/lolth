package lolthx.alexa.search;

import java.text.MessageFormat;

import lakenono.base.Producer;
import lakenono.base.Task;

public class AlexaUrlSearchProducer extends Producer{

	private static final String ALEXA_URL_SEARCH = "http://www.alexa.cn/index.php?url={0}";
	
	private String keyword;
	
	private String searchUrl;
	
	public AlexaUrlSearchProducer(String projectName,String keyword,String searchUrl) {
		super(projectName);
		this.keyword = keyword;
		this.searchUrl = searchUrl;
	}

	@Override
	public String getQueueName() {
		return "alexa_url_list";
	}

	@Override
	protected int parse() throws Exception {
		return 1;
	}

	@Override
	protected String buildUrl(int pageNum) throws Exception {
		return MessageFormat.format(ALEXA_URL_SEARCH, 	searchUrl);
	}
	
	@Override
	protected Task buildTask(String url) {
		Task buildTask = super.buildTask(url);
		buildTask.setExtra(keyword + ":" + searchUrl);
		return buildTask;
	}
	
	public static void main(String args[]){
		String projectName = "政府官网";
		String[] keywords = {
				"杭州","北京","昆明","潍坊","武汉",
				"石家庄","长春","邯郸","成都","福州",
				"广州","贵阳","哈尔滨","济南","兰州",
				"南京","青岛","上海","深圳","沈阳",
				"天津","呼和浩特","银川","长沙","重庆",
				"东莞","无锡","威海","大连","海口",
				"合肥","拉萨","南昌","南宁","宁波",
				"厦门","太原","乌鲁木齐","西安","西宁",
				"郑州","淮安","唐山","洛阳","苏州",
				"佛山","烟台","南通","徐州","泉州",
				"常州","盐城","绍兴","温州","扬州",
				"济宁","鄂尔多斯","临沂","淄博","泰州",
				"镇江","东营","泰安","嘉兴","台州",
				"金华","沧州","中山","惠州","德州",
				"宜昌","襄阳","大庆","保定","聊城",
				"南阳","包头","安庆","鞍山","常德",
				"赣州","桂林","菏泽","衡阳","湖州",
				"吉林","吉安","江门","焦作","九江",
				"茂名","上饶","渭南","咸阳","新乡",
				"宿迁","岳阳","湛江","运城","漳州",
				"周口","珠海","遵义"
				
		} ;
		String[] urls = {
				"http://www.hangzhou.gov.cn","http://www.beijing.gov.cn","http://www.km.gov.cn","http://www.weifang.gov.cn","http://www.wuhan.gov.cn",
				"http://www.sjz.gov.cn","http://www.changchun.gov.cn","http://www.hd.gov.cn","http://www.chengdu.gov.cn","http://www.fuzhou.gov.cn",
				"http://www.gz.gov.cn","http://xxgk.gygov.gov.cn","http://www.harbin.gov.cn","http://www.jinan.gov.cn","http://www.lz.gansu.gov.cn",
				"http://www.nanjing.gov.cn","http://www.qingdao.gov.cn","http://www.shanghai.gov.cn","http://www.sz.gov.cncn","http://www.shenyang.gov.cn",
				"http://www.tj.gov.cn","http://www.huhhot.gov.cn","http://www.yinchuan.gov.cn","http://www.changsha.gov.cn","http://www.cq.gov.cn",
				"http://www.dg.gov.cn","http://www.wuxi.gov.cn","http://www.weihaichina.com","http://www.dl.gov.cn","http://www.haikou.gov.cn",
				"http://www.hefei.gov.cn","http://www.lasa.gov.cn","http://www.nc.gov.cn","http://www.nanning.gov.cn","http://gtog.ningbo.gov.cn",
				"http://www.xm.gov.cn","http://www.taiyuan.gov.cn","http://www.urumqi.gov.cn","http://www.xian.gov.cn","http://www.xining.gov.cn",
				"http://www.zhengzhou.gov.cn","http://www.huaian.gov.cn","http://www.tangshan.gov.cn","http://www.ly.gov.cn","http://www.suzhou.gov.cn",
				"http://www.foshan.gov.cn","http://www.yantai.gov.cn","http://www.nantong.gov.cn","http://www.cnxz.com.cn","http://www.fjqz.gov.cn",
				"http://www.changzhou.gov.cn","http://www.yancheng.gov.cn","http://www.sx.gov.cn","http://xxgk.wenzhou.gov.cn","http://www.yangzhou.gov.cn",
				"http://www.jining.gov.cn","http://www.ordos.gov.cn","http://www.linyi.gov.cn","http://www.zibo.gov.cn","http://www.taizhou.gov.cn",
				"http://www.zhenjiang.gov.cn","http://www.dongying.gov.cn","http://www.taian.gov.cn","http://www.jiaxing.gov.cn","http://www.zjtz.gov.cn",
				"http://www.jinhua.gov.cn","http://www.cangzhou.gov.cn","http://www.zs.gov.cn","http://www.huizhou.gov.cn","http://www.dezhou.gov.cn",
				"http://www.yichang.gov.cn","http://www.xf.gov.cn","http://www.daqing.gov.cn","http://www.bd.gov.cn","http://www.liaocheng.gov.cn",
				"http://www.nanyang.gov.cn","http://www.baotou.gov.cn","http://www.anqing.gov.cn","http://app.anshan.gov.cn","http://www.changde.gov.cn",
				"http://www.ganzhou.gov.cn","http://www.guilin.gov.cn","http://www.heze.gov.cn","http://www.hengyang.gov.cn","http://www.huzhou.gov.cn",
				"http://www.jl.gov.cn","http://www.jian.gov.cn","http://www.jiangmen.gov.cn","http://www.jiaozuo.gov.cn","http://www.jiujiang.gov.cn",
				"http://www.maoming.gov.cn","http://www.zgsr.gov.cn","http://www.weinan.gov.cn","http://www.xianyang.gov.cn","http://www.xinxiang.gov.cn",
				"http://www.suqian.gov.cn","http://www.yueyang.gov.cn","http://www.zhanjiang.gov.cn","http://www.yuncheng.gov.cn","http://www.zhangzhou.gov.cn",
				"http://www.zhoukou.gov.cn","http://www.zhuhai.gov.cn","http://www.zunyi.gov.cn",
				} ;
		
		for(int i = 0 ; i < keywords.length ; i++ ){
			new AlexaUrlSearchProducer(projectName, keywords[i], urls[i]).run();
		}
		
	}
	
	
}
