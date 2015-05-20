package lolth.weibo.bean;

import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@DBTable(name="data_sina_weibo_user_concern_ref")
@Data
@ToString(callSuper=false)
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class WeiboUserConcernRefBean extends BaseBean{
	@DBConstraintPK
	private String userId;
	@DBConstraintPK
	private String concernUserId;
	
	public static void main(String[] args) throws Exception{
		new WeiboUserConcernRefBean().buildTable();
	}
}
