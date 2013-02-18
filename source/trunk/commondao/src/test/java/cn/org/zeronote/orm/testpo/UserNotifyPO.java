/**
 * 
 */
package cn.org.zeronote.orm.testpo;

import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMColumn;
import cn.org.zeronote.orm.ORMHash;
import cn.org.zeronote.orm.ORMTable;


/**
 * Auto Generat Code by system
 *
 */
@ORMAutoAssemble
@ORMTable(tableName = "user_notify", cachedKey={"id=I"})
@ORMHash(hashSize = 100, hashRefColumn = "userId", createSQL = "CREATE TABLE `{0}` ("
		+ "`id` bigint(20) NOT NULL AUTO_INCREMENT,"
		+ "`user_id` bigint(20) NOT NULL,"
		+ "`type` tinyint(4) NOT NULL,"
		+ "`content` char(200) NOT NULL,"
		+ "`activity_id` bigint(20) NOT NULL,"
		+ "`friend_id` bigint(20) NOT NULL,"
		+ "`time` datetime NOT NULL,"
		+ "PRIMARY KEY (`id`)" 
		+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;")
public class UserNotifyPO{

	@ORMColumn(value = "id", physicalPkFld=true)
	private java.lang.Long id;

	@ORMColumn(value = "user_id", defaultValue="0")
	private java.lang.Long userId;

	@ORMColumn(value = "type", defaultValue="0")
	private java.lang.Integer type;

	@ORMColumn(value = "content", defaultValue="")
	private java.lang.String content;

	@ORMColumn(value = "activity_id", defaultValue="0")
	private java.lang.Long activityId;

	@ORMColumn(value = "friend_id", defaultValue="0")
	private java.lang.Long friendId;

	@ORMColumn(value = "time", defaultValue=ORMColumn.DEFAULT_DATE)
	private java.util.Date time;

	/**
	 * 
	 */
	public UserNotifyPO() {
	}
	/**
	 * @return the id
	 */
	public java.lang.Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	/**
	 * @return the userId
	 */
	public java.lang.Long getUserId() {
		return userId;
	}
	/**
	 * @param id the userId to set
	 */
	public void setUserId(java.lang.Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the type
	 */
	public java.lang.Integer getType() {
		return type;
	}
	/**
	 * @param id the type to set
	 */
	public void setType(java.lang.Integer type) {
		this.type = type;
	}
	/**
	 * @return the content
	 */
	public java.lang.String getContent() {
		return content;
	}
	/**
	 * @param id the content to set
	 */
	public void setContent(java.lang.String content) {
		this.content = content;
	}
	/**
	 * @return the activityId
	 */
	public java.lang.Long getActivityId() {
		return activityId;
	}
	/**
	 * @param id the activityId to set
	 */
	public void setActivityId(java.lang.Long activityId) {
		this.activityId = activityId;
	}
	/**
	 * @return the friendId
	 */
	public java.lang.Long getFriendId() {
		return friendId;
	}
	/**
	 * @param id the friendId to set
	 */
	public void setFriendId(java.lang.Long friendId) {
		this.friendId = friendId;
	}
	/**
	 * @return the time
	 */
	public java.util.Date getTime() {
		return time;
	}
	/**
	 * @param id the time to set
	 */
	public void setTime(java.util.Date time) {
		this.time = time;
	}

}
