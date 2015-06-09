package lolth.power;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ByIdOrName;

public class BigBob {
	public static void main(String[] args) throws InterruptedException {

		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		// driver.manage().window().maximize();

		Thread.sleep(2000);

		// {
		// //打开百度
		// driver.get("http://www.baidu.com");
		// Thread.sleep(3000);
		//
		// //输入关键字
		// driver.findElement(new ByIdOrName("kw")).sendKeys("派择");
		// Thread.sleep(3000);
		//
		// //点击搜索
		// driver.findElement(new ByIdOrName("su")).click();
		// Thread.sleep(3000);
		//
		// //分析百度新闻
		// String baiduNewsSource = driver.getPageSource();
		// Document document = Jsoup.parse(baiduNewsSource);
		//
		// System.out.println("---新闻标题开始---");
		// Elements elements = document.select("div.result.c-container > h3.t");
		// for (org.jsoup.nodes.Element element : elements)
		// {
		// System.out.println(element.text());
		// }
		// System.out.println("---新闻标题结束---");
		// }

		// 打开sinaweibo
		driver.get("http://www.weibo.com/login");
		Thread.sleep(3000);

		// 输入用户名
		System.out.println("action: username");
		driver.findElement(By.cssSelector(".username > input:nth-child(1)")).sendKeys("gengbushuang@163.com");
		Thread.sleep(3000);

		// 输入密码
		System.out.println("action: password");
		driver.findElement(By.cssSelector(".password > input:nth-child(1)")).sendKeys("xieshijing");
		Thread.sleep(3000);

		// 点击登录按钮
		System.out.println("action: click login");
		driver.findElement(By.cssSelector("div.info_list:nth-child(6) > div:nth-child(1) > a:nth-child(1) > span:nth-child(1)")).click();
		Thread.sleep(3000);

		driver.get("http://weibo.com/1991428685/ChjbMkHMK?type=repost");
		Thread.sleep(5000);

		// 翻到最后一页
		driver.findElement(By.cssSelector("a.page:nth-child(8)")).click();
		System.out.println("last page");

		for (int i = 954; i > 0; i--) {
			// 点上一页
			Thread.sleep(2000);
			driver.findElement(By.cssSelector("a.page:nth-child(1)")).click();
			System.out.println("last page");
		}

	}
}
