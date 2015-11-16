package lolthx.weibo.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.io.Files;

public class WeiboFileUtils {
	public static String rename2Temp(String path) {
		File file = new File(path);
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return StringUtils.endsWith(pathname.getName(), ".dat");
				}

			});
			for (File f : listFiles) {
				String fileName = StringUtils.substringBefore(f.getName(), ".");
				fileName = f.getParent() + "/" + fileName + ".temp";
				f.renameTo(new File(fileName));
				return fileName;
			}
		}
		return null;
	}

	public static List<String> readFile(String path) throws IOException {
		if (StringUtils.isBlank(path)) {
			return null;
		}
		return Files.readLines(new File(path), Charset.defaultCharset());

	}

	public static void rename2Done(String path) {
		File file = new File(path);
		String fileName = StringUtils.substringBefore(file.getName(), ".");
		fileName = file.getParent() + "/" + fileName + ".done";
		file.renameTo(new File(fileName));
	}

	public static String getProjectName(String path) {
		String project = StringUtils.substringAfterLast(path, "/");
		project = StringUtils.substringBefore(project, ".");
		return project;
	}

	public static void main(String[] args) throws IOException {
		String file = rename2Temp("/Users/yanghp/Desktop/work");
		List<String> readFile = readFile(file);
		for (String string : readFile) {
			System.out.println(string);
		}
		rename2Done(file);
		String project = StringUtils.substringAfterLast(file, "/");
		project = StringUtils.substringBefore(project, ".");
		System.out.println(project);
		
	}
}
