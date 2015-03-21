package lolth.double5.bbs.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;

@DBTable(name = "data_55bbs_post")
public class Double5BBSPostBean extends BaseBean {
	private String title;

	private String url;

	@DBField(type = "text")
	private String text;

	private String postTime;

	private String replys;

	private String views;

	private String author;

	private String authorUrl;

	private String posts;

	private String classicPosts;

	private String region;

	private String regTime;

	private String sax;

	private String astro;

	private String lifeStage;

	public void update() throws SQLException {
		GlobalComponents.db
				.getRunner()
				.update("update "
						+ BaseBean.getTableName(Double5BBSPostBean.class)
						+ " set text=?,postTime=?,replys=?,views=?,author=?,authorUrl=?,posts=?,classicPosts=?,region=?,regTime=? where url=?",
						text, postTime, replys, views, author, authorUrl,
						posts, classicPosts, region, regTime, url);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public String getReplys() {
		return replys;
	}

	public void setReplys(String replys) {
		this.replys = replys;
	}

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorUrl() {
		return authorUrl;
	}

	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}

	public String getPosts() {
		return posts;
	}

	public void setPosts(String posts) {
		this.posts = posts;
	}

	public String getClassicPosts() {
		return classicPosts;
	}

	public void setClassicPosts(String classicPosts) {
		this.classicPosts = classicPosts;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getSax() {
		return sax;
	}

	public void setSax(String sax) {
		this.sax = sax;
	}

	public String getAstro() {
		return astro;
	}

	public void setAstro(String astro) {
		this.astro = astro;
	}

	public String getLifeStage() {
		return lifeStage;
	}

	public void setLifeStage(String lifeStage) {
		this.lifeStage = lifeStage;
	}

	public static void main(String[] args) throws SQLException {
		new Double5BBSPostBean().buildTable();
	}
}
