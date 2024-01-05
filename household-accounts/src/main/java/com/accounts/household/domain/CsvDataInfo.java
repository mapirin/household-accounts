package com.accounts.household.domain;

/**
 * CSVデータ格納用bean
 * @author neo-matrix506
 * 
 */
public class CsvDataInfo {

	private Integer csvDataId;
	private Integer userId;
	private String csvDataYear;
	private String csvDataMonth;
	private String csvDataDay;
	private Integer csvDataAmount;
	private Integer csvDataMethodId;
	private Integer csvDataCategoryId;
	private String csvDataDetail;
	public Integer getCsvDataId() {
		return csvDataId;
	}
	public void setCsvDataId(Integer csvDataId) {
		this.csvDataId = csvDataId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getCsvDataYear() {
		return csvDataYear;
	}
	public void setCsvDataYear(String csvDataYear) {
		this.csvDataYear = csvDataYear;
	}
	public String getCsvDataMonth() {
		return csvDataMonth;
	}
	public void setCsvDataMonth(String csvDataMonth) {
		this.csvDataMonth = csvDataMonth;
	}
	public String getCsvDataDay() {
		return csvDataDay;
	}
	public void setCsvDataDay(String csvDataDay) {
		this.csvDataDay = csvDataDay;
	}
	public Integer getCsvDataAmount() {
		return csvDataAmount;
	}
	public void setCsvDataAmount(Integer csvDataAmount) {
		this.csvDataAmount = csvDataAmount;
	}
	public Integer getCsvDataMethodId() {
		return csvDataMethodId;
	}
	public void setCsvDataMethodId(Integer csvDataMethodId) {
		this.csvDataMethodId = csvDataMethodId;
	}
	public Integer getCsvDataCategoryId() {
		return csvDataCategoryId;
	}
	public void setCsvDataCategoryId(Integer csvDataCategoryId) {
		this.csvDataCategoryId = csvDataCategoryId;
	}
	public String getCsvDataDetail() {
		return csvDataDetail;
	}
	public void setCsvDataDetail(String csvDataDetail) {
		this.csvDataDetail = csvDataDetail;
	}

}
