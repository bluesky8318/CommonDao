/**
 * 
 */
package cn.org.zeronote.orm.testpo;

import java.io.Serializable;

import cn.org.zeronote.orm.ORMAutoAssemble;
import cn.org.zeronote.orm.ORMColumn;
import cn.org.zeronote.orm.ORMTable;


/**
 * Auto Generat Code by system
 *
 */
@ORMAutoAssemble
@ORMTable(tableName = "active_device", cachedKey={"id=I"})
public class ActiveDevicePO implements Serializable{

	private static final long serialVersionUID = 1L;

	@ORMColumn(value = "id", physicalPkFld=true, autoIncrement = true)
	private java.lang.Long id;

	@ORMColumn(value = "platform", defaultValue="")
	private java.lang.String platform;

	@ORMColumn(value = "agent", defaultValue="")
	private java.lang.String agent;

	@ORMColumn(value = "imei", defaultValue="")
	private java.lang.String imei;

	@ORMColumn(value = "version", defaultValue="")
	private java.lang.String version;

	@ORMColumn(value = "vendor", defaultValue="")
	private java.lang.String vendor;

	@ORMColumn(value = "channel", defaultValue="")
	private java.lang.String channel;

	@ORMColumn(value = "create_time", defaultValue=ORMColumn.DEFAULT_DATE)
	private java.util.Date createTime;

	/**
	 * 
	 */
	public ActiveDevicePO() {
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
	 * @return the platform
	 */
	public java.lang.String getPlatform() {
		return platform;
	}
	/**
	 * @param id the platform to set
	 */
	public void setPlatform(java.lang.String platform) {
		this.platform = platform;
	}
	/**
	 * @return the agent
	 */
	public java.lang.String getAgent() {
		return agent;
	}
	/**
	 * @param id the agent to set
	 */
	public void setAgent(java.lang.String agent) {
		this.agent = agent;
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
	 * @return the version
	 */
	public java.lang.String getVersion() {
		return version;
	}
	/**
	 * @param id the version to set
	 */
	public void setVersion(java.lang.String version) {
		this.version = version;
	}
	/**
	 * @return the vendor
	 */
	public java.lang.String getVendor() {
		return vendor;
	}
	/**
	 * @param id the vendor to set
	 */
	public void setVendor(java.lang.String vendor) {
		this.vendor = vendor;
	}
	/**
	 * @return the channel
	 */
	public java.lang.String getChannel() {
		return channel;
	}
	/**
	 * @param id the channel to set
	 */
	public void setChannel(java.lang.String channel) {
		this.channel = channel;
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

}
