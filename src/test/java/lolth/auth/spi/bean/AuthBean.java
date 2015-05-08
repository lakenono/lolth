package lolth.auth.spi.bean;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.alibaba.fastjson.JSON;

@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@DBTable(name = "auth_data")
public class AuthBean extends BaseBean implements Comparable<AuthBean> {
	// 标志
	@DBConstraintPK
	private String id;

	// 登陆服务器一级域名
	private String domain;

	// 认证类型：cookies或params
//	private String authType;

	// 认证数据
	@DBField(type = "varchar(300)")
	private Map<String, String> authData;

	private boolean autoLogin;

	private String username;
	private String password;

	// 是否失效
	private boolean failure;

	// 使用该认证信息的服务器地址
	@DBField(serialization = false)
	private List<String> clients = new LinkedList<>();

	public void addClientIp(String clientIp) {
		clients.add(clientIp);
	}

	/**
	 * 按照使用终端进行排序
	 */
	@Override
	public int compareTo(AuthBean o) {
		if (o == null) {
			return -1;
		}

		return this.getClients().size() - o.getClients().size();
	}

	public void updateAuthData() throws SQLException {
		String audhData = null;
		if(getAuthData()!=null){
			audhData = JSON.toJSONString(getAuthData());
		}
		GlobalComponents.db.getRunner().update("UPDATE " + BaseBean.getTableName(AuthBean.class) + " SET authData=? WHERE id=?", audhData, this.getId());
	}
	
	public void updateFailure() throws SQLException{
		GlobalComponents.db.getRunner().update("UPDATE " + BaseBean.getTableName(AuthBean.class) + " SET failure=? WHERE id=?", this.isFailure(), this.getId());
	}
	
	public static void main(String[] args) throws Exception {
		new AuthBean().buildTable();
		
		AuthBean authBean = new AuthBean();
		authBean.setId(BaseBean.UUID.generateId());
		authBean.setUsername("pzhmath@163.com");
		authBean.setPassword("Mypassword");
		authBean.setDomain("weibo.cn");
		authBean.setAutoLogin(true);
		
		authBean.persistOnNotExist();
		
		authBean.setId(BaseBean.UUID.generateId());
		authBean.setUsername("Pzhmath1@126.com");
		authBean.setPassword("Mypassword");
		authBean.setDomain("weibo.cn");
		authBean.setAutoLogin(true);
		
		authBean.persistOnNotExist();
		
		authBean.setId(BaseBean.UUID.generateId());
		authBean.setUsername("954768034@qq.com");
		authBean.setPassword("Mypassword");
		authBean.setDomain("weibo.cn");
		authBean.setAutoLogin(true);
		
		authBean.persistOnNotExist();
	}
}
