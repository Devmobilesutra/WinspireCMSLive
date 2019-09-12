package com.cms.callmanager.dto;

import java.io.Serializable;

/**
 * Created by zogato on 1/7/17.
 */
public class ATMDetailsDTO implements Serializable {

	String ATMName;
	String bankName;
	String distance;
	String latitude;
	String longitude;

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAtmID() {
		return atmID;
	}

	public void setAtmID(String atmID) {
		this.atmID = atmID;
	}

	String atmID;

	public String getATMName() {
		return ATMName;
	}

	public void setATMName(String ATMName) {
		this.ATMName = ATMName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
}
