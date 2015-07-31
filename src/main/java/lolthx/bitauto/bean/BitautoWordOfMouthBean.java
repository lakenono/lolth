package lolthx.bitauto.bean;

import java.sql.SQLException;

import lakenono.core.GlobalComponents;
import lakenono.db.BaseBean;
import lakenono.db.annotation.DBConstraintPK;
import lakenono.db.annotation.DBField;
import lakenono.db.annotation.DBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@DBTable(name = "data_bitauto_kb")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class BitautoWordOfMouthBean extends BaseBean {
	//------------------------------------同 BitautoBBSPostBean
	@DBConstraintPK
	private String id;
	private String title;
	private String url;
	// 性质
	private String type;

	private String postTime;

	@DBField(type = "text")
	private String content;
	
	private String forumId;
	
	//发帖用户
	private String author;
	private String authorId;

	// 查看数
	private String views;

	// 回复数
	private String replys;
	
	private String projectName;
	private String keyword;
	
	//-----------------------------------------------------------
	//外观
	private String exteriorScores;
	private String exteriorComment;

	//内饰
	private String interiorScores;
	private String interiorComment;

	//空间
	private String spaceScores;
	private String spaceComment;

	//动力
	private String powerScores;
	private String powerComment;

	//操控
	private String operationScores;
	private String operationComment;

	//配置
	private String configScores;
	private String configComment;

	//性价比
	private String costperformanceScores;
	private String costperformanceComment;

	//舒适度
	private String comfortScores;
	private String comfortComment;

	private String buyTime;
	private String price;
	private String currentMiles;
	
	
	public static void main(String[] args) throws Exception {
		new BitautoWordOfMouthBean().buildTable();
	}
	
	public void update() throws SQLException{
		GlobalComponents.db.getRunner().update("update " +  BaseBean.getTableName(BitautoWordOfMouthBean.class) + 
				" set views=? , replys = ?,  content = ? , "
				+ " exteriorScores = ? , exteriorComment = ? , interiorScores = ? , interiorComment = ? , spaceScores = ? , spaceComment = ? ,"+
	"powerScores = ? , powerComment = ? , operationScores = ? , operationComment = ? , configScores = ? , configComment = ? ," +
	"costperformanceScores = ? , costperformanceComment = ? , comfortScores = ? , comfortComment = ? , buyTime = ? , price = ? , currentMiles = ? "
	+ "where id =?",
		this.views,this.replys,this.content,
		this.exteriorScores,this.exteriorComment,this.interiorScores,this.interiorComment,this.spaceScores,this.spaceComment,
		this.powerScores,this.powerComment,this.operationScores,this.operationComment,this.configScores,this.configComment,
		this.costperformanceScores,this.costperformanceComment,this.comfortScores,this.comfortComment,this.buyTime,this.price,this.currentMiles,
		this.id);
	}
	
	
	
}
