package lolthx.baidu.index;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.javaocr.ocrPlugins.mseOCR.CharacterRange;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.OCRScanner;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.TrainingImage;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.TrainingImageLoader;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

public class BaiduIndexFetch {
	private WebDriver driver;
	Actions actions = null;
	OCRScanner scanner = null;
	StringBuffer sb = null;

	private String username;
	private String password;
	private String keyword;
	private String trainingPath;
	private String savePath = "/Users/gbs/tmp/test/";

	private int index = 0;

	public BaiduIndexFetch(String name, String password, String keyword) {
		this.username = name;
		this.password = password;
		this.keyword = keyword;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void init() throws Exception {
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		driver = new FirefoxDriver(firefoxProfile);
		if (StringUtils.isBlank(trainingPath)) {
			this.trainingPath = BaiduIndexFetch.class.getResource("/baiduorc.png").getPath();
		}
		scanner = new OCRScanner();
		scanner.clearTrainingImages();
		TrainingImageLoader loader = new TrainingImageLoader();
		HashMap<Character, ArrayList<TrainingImage>> trainingImageMap = new HashMap<Character, ArrayList<TrainingImage>>();
		loader.load(trainingPath, new CharacterRange('0', '9'), trainingImageMap);
		scanner.addTrainingImages(trainingImageMap);
		sb = new StringBuffer();
	}

	public void run() throws Exception {

		String url = "http://index.baidu.com/?tpl=trend&word=" + URLEncoder.encode(this.keyword, "gbk");
		driver.get(url);
		Thread.sleep(5000);
		WebElement username = driver.findElement(By.name("userName"));
		username.clear();
		username.sendKeys(this.username);
		WebElement password = driver.findElement(By.name("password"));
		password.clear();
		password.sendKeys(this.password);
		Thread.sleep(1000);
		WebElement submit = driver.findElement(By.cssSelector("input[id$='__submit']"));
		submit.click();
		Thread.sleep(10000);

		WebElement path = driver.findElement(By.cssSelector("#trend > svg > path:nth-child(33)"));
		String attribute = path.getAttribute("d");
		System.out.println(attribute);
		int[] in = toInt(attribute);

		WebElement div = driver.findElement(By.cssSelector("#auto_gsid_15"));
		actions = new Actions(driver);
		for (int i = index; i < in.length;) {
			boolean b = true;
			move(div, in[i]);
			System.out.println(i + "-->" + in[i]);
			Thread.sleep(5000);
			try {
				truncation(driver, i, in[i]);
			} catch (Exception e) {
				b = false;
			}
			if (b) {
				i++;
			} else {
				if (i < in.length - 1) {
					in[i]++;
					if (in[i] == in[i + 1]) {
						i++;
					}
				} else {
					in[i]--;
					if (in[i] <= in[i - 1]) {
						break;
					}
				}

			}
		}
		Thread.sleep(3000);
		System.out.println(sb.toString());
		driver.quit();
	}

	private byte[] takeScreenshot(WebDriver driver) throws IOException {
		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		return takesScreenshot.getScreenshotAs(OutputType.BYTES);
	}

	private BufferedImage createElementImage(WebDriver driver, WebElement webElement) throws IOException {
		// 获得webElement的位置和大小。
		Point location = webElement.getLocation();
		Dimension size = webElement.getSize();
		// 创建全屏截图。
		BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(takeScreenshot(driver)));
		// 截取webElement所在位置的子图。
		BufferedImage croppedImage = originalImage.getSubimage(location.getX(), location.getY(), size.getWidth(), size.getHeight());
		// ImageHelper
		return croppedImage;
	}

	private void truncation(WebDriver driver, int i, int x) throws IOException {
		WebElement viewbox = driver.findElement(By.id("viewbox"));
		BufferedImage image = createElementImage(driver, viewbox);
		List<WebElement> findElements = viewbox.findElements(By.className("view-table-wrap"));
		String date_time = findElements.get(0).getText();
		String time = StringUtils.substringBefore(date_time, " ");

		WebElement view_label = findElements.get(1).findElement(By.className("view-label"));
		String tmp = view_label.getText();
		String name = StringUtils.substring(tmp, 0, tmp.length() - 1);

		WebElement view_value = findElements.get(1).findElement(By.className("view-value"));
		image = createElementImage(driver, view_value);
		image = ImageUtils.reverse(image);
		image = ImageUtils.elimination(image, 250);

		String imageName = this.savePath + time + "_" + name + ".png";
		ImageIO.write(image, "png", new File(imageName));

		image = ImageUtils.getScaledInstance(image, image.getWidth() * 2, image.getHeight() * 2);
		String text = scanner.scan(image, 0, 0, 0, 0, null);
		if (sb.length() > 0) {
			sb.append("\n");
		}
		sb.append(name).append(":").append(time).append(":").append(text);
	}

	private void move(WebElement webElement, int x) {
		actions.moveToElement(webElement, x, 170).perform();
	}

	private int[] toInt(String value) {
		String[] values = StringUtils.split(value, "L");
		int[] in = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			String[] val = StringUtils.split(values[i], ",");
			if (i == 0) {
				in[i] = Integer.parseInt(StringUtils.substringAfter(val[0], "M"));
			} else {
				in[i] = Integer.parseInt(StringUtils.substringBefore(val[0], "."));
			}
		}
		return in;
	}

	public void shutdown() throws Exception {
		if (driver != null) {
			driver.quit();
		}
	}
}
