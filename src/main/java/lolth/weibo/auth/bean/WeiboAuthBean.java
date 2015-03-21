package lolth.weibo.auth.bean;

import java.util.Map;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBTable;

import org.apache.commons.collections.MapUtils;

import com.alibaba.fastjson.JSON;

@DBTable(name = "cluster_sina_weibo_auth")
public class WeiboAuthBean extends BaseBean {
	private String uuid;

	private String cookies;

	private String type;

	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	public String getUuid() {
		return uuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getCookieMap() {
		if (cookies != null) {
			return (Map<String, String>) JSON.parse(cookies);
		}
		return null;
	}

	public void setCookieMap(Map<String, String> cookieMap) {
		if (MapUtils.isNotEmpty(cookieMap)) {
			cookies = JSON.toJSONString(cookieMap);
		}
	}

	public static void main(String[] args) throws Exception {
		new WeiboAuthBean().buildTable();
	}
}
