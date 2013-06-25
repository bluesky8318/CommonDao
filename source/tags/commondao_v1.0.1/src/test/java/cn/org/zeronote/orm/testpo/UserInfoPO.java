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
@ORMTable(tableName = "user_info")
public class UserInfoPO implements Serializable{

	private static final long serialVersionUID = 1L;

	@ORMColumn(value = "pk_id", physicalPkFld = true, autoIncrement = true)
	private java.lang.Long pkId;

	@ORMColumn(value = "user_name")
	private java.lang.String userName;

	@ORMColumn(value = "email")
	private java.lang.String email;

	@ORMColumn(value = "tel_number")
	private java.lang.String telNumber;

	@ORMColumn(value = "flag", defaultValue="1")
	private java.lang.Integer flag;

	@ORMColumn(value = "birth_year")
	private java.lang.Integer birthYear;

	@ORMColumn(value = "birth_month")
	private java.lang.Integer birthMonth;

	@ORMColumn(value = "birth_day")
	private java.lang.Integer birthDay;

	@ORMColumn(value = "is_marry")
	private java.lang.Integer isMarry;

	@ORMColumn(value = "income")
	private java.lang.Integer income;

	@ORMColumn(value = "interest", defaultValue="")
	private java.lang.String interest;

	@ORMColumn(value = "address", defaultValue="ADDRESST")
	private java.lang.String address;

	@ORMColumn(value = "update_time", defaultValue=ORMColumn.DEFAULT_DATE)
	private java.util.Date updateTime;

	/**
	 * 
	 */
	public UserInfoPO() {
	}
	/**
	 * @return the pkId
	 */
	public java.lang.Long getPkId() {
		return pkId;
	}
	/**
	 * @param id the pkId to set
	 */
	public void setPkId(java.lang.Long pkId) {
		this.pkId = pkId;
	}
	/**
	 * @return the userName
	 */
	public java.lang.String getUserName() {
		return userName;
	}
	/**
	 * @param id the userName to set
	 */
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}
	/**
	 * @return the email
	 */
	public java.lang.String getEmail() {
		return email;
	}
	/**
	 * @param id the email to set
	 */
	public void setEmail(java.lang.String email) {
		this.email = email;
	}
	/**
	 * @return the telNumber
	 */
	public java.lang.String getTelNumber() {
		return telNumber;
	}
	/**
	 * @param id the telNumber to set
	 */
	public void setTelNumber(java.lang.String telNumber) {
		this.telNumber = telNumber;
	}
	/**
	 * @return the flag
	 */
	public java.lang.Integer getFlag() {
		return flag;
	}
	/**
	 * @param id the flag to set
	 */
	public void setFlag(java.lang.Integer flag) {
		this.flag = flag;
	}
	/**
	 * @return the birthYear
	 */
	public java.lang.Integer getBirthYear() {
		return birthYear;
	}
	/**
	 * @param id the birthYear to set
	 */
	public void setBirthYear(java.lang.Integer birthYear) {
		this.birthYear = birthYear;
	}
	/**
	 * @return the birthMonth
	 */
	public java.lang.Integer getBirthMonth() {
		return birthMonth;
	}
	/**
	 * @param id the birthMonth to set
	 */
	public void setBirthMonth(java.lang.Integer birthMonth) {
		this.birthMonth = birthMonth;
	}
	/**
	 * @return the birthDay
	 */
	public java.lang.Integer getBirthDay() {
		return birthDay;
	}
	/**
	 * @param id the birthDay to set
	 */
	public void setBirthDay(java.lang.Integer birthDay) {
		this.birthDay = birthDay;
	}
	/**
	 * @return the isMarry
	 */
	public java.lang.Integer getIsMarry() {
		return isMarry;
	}
	/**
	 * @param id the isMarry to set
	 */
	public void setIsMarry(java.lang.Integer isMarry) {
		this.isMarry = isMarry;
	}
	/**
	 * @return the income
	 */
	public java.lang.Integer getIncome() {
		return income;
	}
	/**
	 * @param id the income to set
	 */
	public void setIncome(java.lang.Integer income) {
		this.income = income;
	}
	/**
	 * @return the interest
	 */
	public java.lang.String getInterest() {
		return interest;
	}
	/**
	 * @param id the interest to set
	 */
	public void setInterest(java.lang.String interest) {
		this.interest = interest;
	}
	/**
	 * @return the address
	 */
	public java.lang.String getAddress() {
		return address;
	}
	/**
	 * @param id the address to set
	 */
	public void setAddress(java.lang.String address) {
		this.address = address;
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

}
