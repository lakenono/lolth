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

public class BigBob
{
	public static void main(String[] args) throws InterruptedException
	{

		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		//driver.manage().window().maximize();

		Thread.sleep(2000);

		{
			//打开百度
			driver.get("http://www.baidu.com");
			Thread.sleep(3000);

			//输入关键字
			driver.findElement(new ByIdOrName("kw")).sendKeys("派择");
			Thread.sleep(3000);

			//点击搜索
			driver.findElement(new ByIdOrName("su")).click();
			Thread.sleep(3000);

			//分析百度新闻
			String baiduNewsSource = driver.getPageSource();
			Document document = Jsoup.parse(baiduNewsSource);

			System.out.println("---新闻标题开始---");
			Elements elements = document.select("div.result.c-container > h3.t");
			for (org.jsoup.nodes.Element element : elements)
			{
				System.out.println(element.text());
			}
			System.out.println("---新闻标题结束---");
		}

		//打开sinaweibo
		driver.get("http://weibo.com/");
		Thread.sleep(3000);

		//输入用户名
		System.out.println("action: username");
		driver.findElement(By.cssSelector(".username > input:nth-child(1)")).sendKeys("angel_night@yeah.net");
		Thread.sleep(3000);

		//输入密码
		System.out.println("action: password");
		driver.findElement(By.cssSelector(".password > input:nth-child(1)")).sendKeys("night870905");
		Thread.sleep(3000);

		// 点击登录按钮
		System.out.println("action: click login");
		driver.findElement(By.cssSelector("div.info_list:nth-child(6) > div:nth-child(1) > a:nth-child(1) > span:nth-child(1)")).click();
		Thread.sleep(3000);

		//输入微博正文
		System.out.println("action: input text");
		driver.findElement(By.cssSelector("textarea.W_input")).sendKeys("我有特别的网页交互能力.." + new Date());
		Thread.sleep(5000);

		//点击发布..
		System.out.println("action: send");
		driver.findElement(By.cssSelector("a.W_btn_a:nth-child(2)")).click();
	}
}
