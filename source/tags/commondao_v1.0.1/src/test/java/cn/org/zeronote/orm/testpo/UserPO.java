/**
 * 
 */
package cn.org.zeronote.orm.testpo;

import java.io.Serializable;

import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMCanUpdate;
import cn.org.zeronote.orm.ORMColumn;
import cn.org.zeronote.orm.ORMTable;


/**
 * Auto Generat Code by system
 *
 */
@ORMAutoAssemble
@ORMTable(tableName = "user", cachedKey={"id=I"})
public class UserPO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ORMColumn(value = "id", physicalPkFld=true)
	private java.lang.Long id;

	@ORMColumn(value = "allow_locate", defaultValue="0")
	private java.lang.Integer allowLocate;

	@ORMColumn(value = "allow_push", defaultValue="0")
	private java.lang.Integer allowPush;

	@ORMColumn(value = "last_active_time", defaultValue=ORMColumn.DEFAULT_DATE)
	private java.util.Date lastActiveTime;

	@ORMColumn(value = "last_active_lat", defaultValue="0")
	private java.lang.Double lastActiveLat;

	@ORMColumn(value = "last_active_lon", defaultValue="0")
	private java.lang.Double lastActiveLon;

	@ORMColumn(value = "last_active_loc1", defaultValue="0")
	private java.lang.Long lastActiveLoc1;

	@ORMColumn(value = "last_active_loc2", defaultValue="0")
	private java.lang.Long lastActiveLoc2;

	@ORMColumn(value = "last_active_loc3", defaultValue="0")
	private java.lang.Long lastActiveLoc3;

	@ORMColumn(value = "notify", defaultValue="")
	private java.lang.String notify;

	@ORMColumn(value = "state", defaultValue="0")
	private java.lang.Integer state;

	@ORMColumn(value = "imei", defaultValue="")
	private java.lang.String imei;

	@ORMColumn(value = "imsi", defaultValue="")
	private java.lang.String imsi;

	@ORMColumn(value = "create_time", defaultValue=ORMColumn.DEFAULT_DATE)
	private java.util.Date createTime;

	@ORMColumn(value = "update_time", defaultValue=ORMColumn.DEFAULT_DATE)
	private java.util.Date updateTime;

	@ORMColumn(value = "activity_illegal_count", defaultValue="0")
	private java.lang.Integer activityIllegalCount;

	@ORMColumn(value = "talk_illegal_count", defaultValue="0")
	private java.lang.Integer talkIllegalCount;

	@ORMColumn(value = "creat_by_app", defaultValue="0")
	private java.lang.Integer creatByApp;

	@ORMColumn(value = "recommended", defaultValue="0")
	private java.lang.Integer recommended;

	/** 标识 */
	@ORMCanUpdate
	private boolean canUpdate = true;
	/**
	 * 
	 */
	public UserPO() {
	}
	
	/**
	 * @return the canUpdate
	 */
	public boolean isCanUpdate() {
		return canUpdate;
	}

	/**
	 * @param canUpdate the canUpdate to set
	 */
	public void setCanUpdate(boolean canUpdate) {
		this.canUpdate = canUpdate;
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
	 * @return the allowLocate
	 */
	public java.lang.Integer getAllowLocate() {
		return allowLocate;
	}
	/**
	 * @param id the allowLocate to set
	 */
	public void setAllowLocate(java.lang.Integer allowLocate) {
		this.allowLocate = allowLocate;
	}
	/**
	 * @return the allowPush
	 */
	public java.lang.Integer getAllowPush() {
		return allowPush;
	}
	/**
	 * @param id the allowPush to set
	 */
	public void setAllowPush(java.lang.Integer allowPush) {
		this.allowPush = allowPush;
	}
	/**
	 * @return the lastActiveTime
	 */
	public java.util.Date getLastActiveTime() {
		return lastActiveTime;
	}
	/**
	 * @param id the lastActiveTime to set
	 */
	public void setLastActiveTime(java.util.Date lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}
	/**
	 * @return the lastActiveLat
	 */
	public java.lang.Double getLastActiveLat() {
		return lastActiveLat;
	}
	/**
	 * @param id the lastActiveLat to set
	 */
	public void setLastActiveLat(java.lang.Double lastActiveLat) {
		this.lastActiveLat = lastActiveLat;
	}
	/**
	 * @return the lastActiveLon
	 */
	public java.lang.Double getLastActiveLon() {
		return lastActiveLon;
	}
	/**
	 * @param id the lastActiveLon to set
	 */
	public void setLastActiveLon(java.lang.Double lastActiveLon) {
		this.lastActiveLon = lastActiveLon;
	}
	/**
	 * @return the lastActiveLoc1
	 */
	public java.lang.Long getLastActiveLoc1() {
		return lastActiveLoc1;
	}
	/**
	 * @param id the lastActiveLoc1 to set
	 */
	public void setLastActiveLoc1(java.lang.Long lastActiveLoc1) {
		this.lastActiveLoc1 = lastActiveLoc1;
	}
	/**
	 * @return the lastActiveLoc2
	 */
	public java.lang.Long getLastActiveLoc2() {
		return lastActiveLoc2;
	}
	/**
	 * @param id the lastActiveLoc2 to set
	 */
	public void setLastActiveLoc2(java.lang.Long lastActiveLoc2) {
		this.lastActiveLoc2 = lastActiveLoc2;
	}
	/**
	 * @return the lastActiveLoc3
	 */
	public java.lang.Long getLastActiveLoc3() {
		return lastActiveLoc3;
	}
	/**
	 * @param id the lastActiveLoc3 to set
	 */
	public void setLastActiveLoc3(java.lang.Long lastActiveLoc3) {
		this.lastActiveLoc3 = lastActiveLoc3;
	}
	/**
	 * @return the notify
	 */
	public java.lang.String getNotify() {
		return notify;
	}
	/**
	 * @param id the notify to set
	 */
	public void setNotify(java.lang.String notify) {
		this.notify = notify;
	}
	/**
	 * @return the state
	 */
	public java.lang.Integer getState() {
		return state;
	}
	/**
	 * @param id the state to set
	 */
	public void setState(java.lang.Integer state) {
		this.state = state;
	}
	/**
	 * @return the imei
	 */
	public java.lang.String getImei() {
		return imei;
	}
	/**
	 * @param id the imei to set
	 */
	public void setImei(java.lang.String imei) {
		this.imei = imei;
	}
	/**
	 * @return the imsi
	 */
	public java.lang.String getImsi() {
		return imsi;
	}
	/**
	 * @param id the imsi to set
	 */
	public void setImsi(java.lang.String imsi) {
		this.imsi = imsi;
	}
	/**
	 * @return the createTime
	 */
	public java.util.Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param id the createTime to set
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the updateTime
	 */
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param id the updateTime to set
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the activityIllegalCount
	 */
	public java.lang.Integer getActivityIllegalCount() {
		return activityIllegalCount;
	}
	/**
	 * @param id the activityIllegalCount to set
	 */
	public void setActivityIllegalCount(java.lang.Integer activityIllegalCount) {
		this.activityIllegalCount = activityIllegalCount;
	}
	/**
	 * @return the talkIllegalCount
	 */
	public java.lang.Integer getTalkIllegalCount() {
		return talkIllegalCount;
	}
	/**
	 * @param id the talkIllegalCount to set
	 */
	public void setTalkIllegalCount(java.lang.Integer talkIllegalCount) {
		this.talkIllegalCount = talkIllegalCount;
	}
	/**
	 * @return the creatByApp
	 */
	public java.lang.Integer getCreatByApp() {
		return creatByApp;
	}
	/**
	 * @param id the creatByApp to set
	 */
	public void setCreatByApp(java.lang.Integer creatByApp) {
		this.creatByApp = creatByApp;
	}
	/**
	 * @return the recommended
	 */
	public java.lang.Integer getRecommended() {
		return recommended;
	}
	/**
	 * @param id the recommended to set
	 */
	public void setRecommended(java.lang.Integer recommended) {
		this.recommended = recommended;
	}

}
