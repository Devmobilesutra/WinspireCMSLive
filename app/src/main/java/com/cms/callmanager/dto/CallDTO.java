package com.cms.callmanager.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Monika on 1/7/17.
 */
public class CallDTO implements Serializable {


	String atmID;
	String callDate;
	String bankName;
	String responseTime;
	String status;
	String resolutionTime;
	String diagnosis;
	String targetReqTime;
	String targetRespTime;

	String latitude;
	String longitude;



	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

	Boolean isActive;

	public String getMainStatus() {
		return mainStatus;
	}

	public void setMainStatus(String mainStatus) {
		this.mainStatus = mainStatus;
	}

	String mainStatus;

	public String getMobileActivity() {
		return mobileActivity;
	}

	public void setMobileActivity(String mobileActivity) {
		this.mobileActivity = mobileActivity;
	}

	String mobileActivity;

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getTargetReqTime() {
		return targetReqTime;
	}

	public void setTargetReqTime(String targetReqTime) {
		this.targetReqTime = targetReqTime;
	}

	public String getTargetRespTime() {
		return targetRespTime;
	}

	public void setTargetRespTime(String targetRespTime) {
		this.targetRespTime = targetRespTime;
	}

	public String getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(String previousStatus) {
		this.previousStatus = previousStatus;
	}

	String previousStatus;
	String docketNo;
	//Date engineerStartDate;

	String callType;
	String subCallType;
	String diagnosisError;
	String actionTaken;
	String dispatchDate;

	public ArrayList<String> getAttachment() {
		return attachment;
	}

	public void setAttachment(ArrayList<String> attachment) {
		this.attachment = attachment;
	}

	ArrayList<String> attachment;

	public String getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(String dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getSubCallType() {
		return subCallType;
	}

	public void setSubCallType(String subCallType) {
		this.subCallType = subCallType;
	}

	public String getDiagnosisError() {
		return diagnosisError;
	}

	public void setDiagnosisError(String diagnosisError) {
		this.diagnosisError = diagnosisError;
	}

	public String getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public String getAtmID() {
		return atmID;
	}

	public void setAtmID(String atmID) {
		this.atmID = atmID;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCallDate() {
		return callDate;
	}

	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getResolutionTime() {
		return resolutionTime;
	}

	public void setResolutionTime(String resolutionTime) {
		this.resolutionTime = resolutionTime;
	}

	public String getDocketNo() {
		return docketNo;
	}

	public void setDocketNo(String docketNo) {
		this.docketNo = docketNo;
	}

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


/*	public String getEngineerStartDate() {
		return engineerStartDate;
	}

	public void setEngineerStartDate(String engineerStartDate) {
		this.engineerStartDate = engineerStartDate;
	}*/

}
