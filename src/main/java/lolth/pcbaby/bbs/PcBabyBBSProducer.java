package lolth.pcbaby.bbs;

import lakenono.base.Producer;

/**
 * 
 * 
 * @author Lakenono
 */
public class PcBabyBBSProducer extends Producer
{
	public static void main(String[] args) throws Exception
	{
		String kw = "美素佳儿";
		new PcBabyBBSProducer("测试Lolth", kw, "备孕妈妈").run();
		new PcBabyBBSProducer("测试Lolth", kw, "怀孕妈妈").run();
		new PcBabyBBSProducer("测试Lolth", kw, "新手妈妈").run();
		new PcBabyBBSProducer("测试Lolth", kw, "早教幼教").run();
		new PcBabyBBSProducer("测试Lolth", kw, "宝宝秀场").run();
		new PcBabyBBSProducer("测试Lolth", kw, "晒货殿堂").run();
		new PcBabyBBSProducer("测试Lolth", kw, "美食分享").run();
		new PcBabyBBSProducer("测试Lolth", kw, "生活百科").run();
		new PcBabyBBSProducer("测试Lolth", kw, "女人心情").run();
		new PcBabyBBSProducer("测试Lolth", kw, "谈天说地").run();
		new PcBabyBBSProducer("测试Lolth", kw, "免费试用").run();
		new PcBabyBBSProducer("测试Lolth", kw, "版务专区").run();
		new PcBabyBBSProducer("测试Lolth", kw, "二胎时代").run();
	}

	private String category;
	private String kw;

	public PcBabyBBSProducer(String projectName, String kw, String category)
	{
		super(projectName);
		this.category = category;
		this.kw = kw;
	}

	@Override
	public String getQueueName()
	{
		return "pcbaby_bbs_list";
	}

	@Override
	protected int parse() throws Exception
	{
		// TODO Auto-generated method stub
		return 0;
	}

	//GB2312
	@Override
	protected String buildUrl(int pageNum) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

}
