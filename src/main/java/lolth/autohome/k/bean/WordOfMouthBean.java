package lolth.autohome.k.bean;

import java.sql.SQLException;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;

import org.apache.commons.lang.StringUtils;

@DBTable(name = "data_autohome_koubei")
public class WordOfMouthBean extends BaseBean
{
	public static void main(String[] args) throws SQLException
	{
		new WordOfMouthBean().buildTable();
	}

	// 用户名
	private String username;

	// 用户url
	private String userUrl;

	// 点评帖子url
	private String sourceUrl;

	// 点评帖子title
	private String sourceTitle;

	// 用户认证车型
	private String authCar;

	// 购买车辆
	private String carType;

	// 购买地点
	private String purchasedFrom;

	// 购车经销商
	private String dealer;

	// 购买时间
	private String buyTime;

	// 裸车购买价
	private String price;

	// 油耗
	private String fuelConsumption;

	// 目前行驶 
	private String kilometre;

	// 评分-空间
	private String interspaceGrade;

	// 评分-动力
	private String powerGrade;

	// 评分-操控
	private String manipulationGrade;

	// 评分-油耗
	private String fuelConsumptionGrade;

	// 评分-舒适性
	private String comfortGrade;

	// 评分-外观
	private String appearanceGrade;

	// 评分-内饰
	private String innerDecorationGrade;

	// 评分-性价比
	private String performancePriceGrade;

	// 购车目的
	private String aims;

	// 发帖时间
	private String publishTime;

	// 帖子内容
	@DBField(type = "text")
	private String text;

	// 浏览次数
	private String views;

	// 点赞 25
	private String likes;

	@Override
	public String toString()
	{
		return "WordOfMouthBean [username=" + username + ", userUrl=" + userUrl + ", sourceUrl=" + sourceUrl + ", sourceTitle=" + sourceTitle + ", AuthCar=" + authCar + ", carType=" + carType + ", purchasedFrom=" + purchasedFrom + ", dealer=" + dealer + ", buyTime=" + buyTime + ", price=" + price + ", fuelConsumption=" + fuelConsumption + ", kilometre=" + kilometre + ", interspaceGrade=" + interspaceGrade + ", powerGrade=" + powerGrade + ", manipulationGrade=" + manipulationGrade + ", fuelConsumptionGrade=" + fuelConsumptionGrade + ", comfortGrade=" + comfortGrade + ", appearanceGrade=" + appearanceGrade + ", innerDecorationGrade=" + innerDecorationGrade + ", performancePriceGrade=" + performancePriceGrade + ", aims=" + aims + ", publishTime=" + publishTime + ", text=" + StringUtils.substring(text, 0, 20) + ", views=" + views + ", likes=" + likes + "]";
	}

	public String getSourceTitle()
	{
		return sourceTitle;
	}

	public void setSourceTitle(String sourceTitle)
	{
		this.sourceTitle = sourceTitle;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getUserUrl()
	{
		return userUrl;
	}

	public void setUserUrl(String userUrl)
	{
		this.userUrl = userUrl;
	}

	public String getSourceUrl()
	{
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl)
	{
		this.sourceUrl = sourceUrl;
	}

	public String getAuthCar()
	{
		return authCar;
	}

	public void setAuthCar(String authCar)
	{
		this.authCar = authCar;
	}

	public String getCarType()
	{
		return carType;
	}

	public void setCarType(String carType)
	{
		this.carType = carType;
	}

	public String getPurchasedFrom()
	{
		return purchasedFrom;
	}

	public void setPurchasedFrom(String purchasedFrom)
	{
		this.purchasedFrom = purchasedFrom;
	}

	public String getDealer()
	{
		return dealer;
	}

	public void setDealer(String dealer)
	{
		this.dealer = dealer;
	}

	public String getBuyTime()
	{
		return buyTime;
	}

	public void setBuyTime(String buyTime)
	{
		this.buyTime = buyTime;
	}

	public String getPrice()
	{
		return price;
	}

	public void setPrice(String price)
	{
		this.price = price;
	}

	public String getFuelConsumption()
	{
		return fuelConsumption;
	}

	public void setFuelConsumption(String fuelConsumption)
	{
		this.fuelConsumption = fuelConsumption;
	}

	public String getKilometre()
	{
		return kilometre;
	}

	public void setKilometre(String kilometre)
	{
		this.kilometre = kilometre;
	}

	public String getInterspaceGrade()
	{
		return interspaceGrade;
	}

	public void setInterspaceGrade(String interspaceGrade)
	{
		this.interspaceGrade = interspaceGrade;
	}

	public String getPowerGrade()
	{
		return powerGrade;
	}

	public void setPowerGrade(String powerGrade)
	{
		this.powerGrade = powerGrade;
	}

	public String getManipulationGrade()
	{
		return manipulationGrade;
	}

	public void setManipulationGrade(String manipulationGrade)
	{
		this.manipulationGrade = manipulationGrade;
	}

	public String getFuelConsumptionGrade()
	{
		return fuelConsumptionGrade;
	}

	public void setFuelConsumptionGrade(String fuelConsumptionGrade)
	{
		this.fuelConsumptionGrade = fuelConsumptionGrade;
	}

	public String getComfortGrade()
	{
		return comfortGrade;
	}

	public void setComfortGrade(String comfortGrade)
	{
		this.comfortGrade = comfortGrade;
	}

	public String getAppearanceGrade()
	{
		return appearanceGrade;
	}

	public void setAppearanceGrade(String appearanceGrade)
	{
		this.appearanceGrade = appearanceGrade;
	}

	public String getInnerDecorationGrade()
	{
		return innerDecorationGrade;
	}

	public void setInnerDecorationGrade(String innerDecorationGrade)
	{
		this.innerDecorationGrade = innerDecorationGrade;
	}

	public String getPerformancePriceGrade()
	{
		return performancePriceGrade;
	}

	public void setPerformancePriceGrade(String performancePriceGrade)
	{
		this.performancePriceGrade = performancePriceGrade;
	}

	public String getAims()
	{
		return aims;
	}

	public void setAims(String aims)
	{
		this.aims = aims;
	}

	public String getPublishTime()
	{
		return publishTime;
	}

	public void setPublishTime(String publishTime)
	{
		this.publishTime = publishTime;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getViews()
	{
		return views;
	}

	public void setViews(String views)
	{
		this.views = views;
	}

	public String getLikes()
	{
		return likes;
	}

	public void setLikes(String likes)
	{
		this.likes = likes;
	}

}
