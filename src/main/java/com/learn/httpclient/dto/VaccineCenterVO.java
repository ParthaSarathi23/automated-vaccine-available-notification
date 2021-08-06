package com.learn.httpclient.dto;

public class VaccineCenterVO {

	private String centerName;
	private Double pincode;
	private String blockName;
	private Double availableCapacity;
	private String availableDate;

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public Double getPincode() {
		return pincode;
	}

	public void setPincode(Double pincode) {
		this.pincode = pincode;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public Double getAvailableCapacity() {
		return availableCapacity;
	}

	public void setAvailableCapacity(Double availableCapacity) {
		this.availableCapacity = availableCapacity;
	}

	public String getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(String availableDate) {
		this.availableDate = availableDate;
	}

	@Override
	public String toString() {
		return "VaccineCenterVO [centerName=" + centerName + ", pincode=" + pincode + ", blockName=" + blockName
				+ ", availableCapacity=" + availableCapacity + ", availableDate=" + availableDate + "]";
	}

}
