package com.bs.opt.conciliador.vo;

/**
 * Custom exception class to the application
 */
public class ConciliationExceptionVO extends Exception {

    private String code;
    private String description;
    private String causeDetail; 

	public String getCauseDetail() {
		return causeDetail;
	}

	public void setCauseDetail(String causeDetail) {
		this.causeDetail = causeDetail;
	}

	public ConciliationExceptionVO() {
    }

    public ConciliationExceptionVO(String code, String description, String causeDetail) {
        this.code = code;
        this.description = description;
        this.causeDetail = causeDetail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description; 
    }
}

