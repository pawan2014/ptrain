/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ptrain;

/**
 * 
 * @author predix -
 */
public class DataPointValueObject {

	/**
	 * @param timestamp
	 * @param tag
	 * @param value
	 * @param quality
	 *            -
	 */
	public DataPointValueObject(String tag, Long timestamp, Object value, Integer quality, String date) {
		super();
		this.timestamp = timestamp;
		this.tag = tag;
		this.value = value;
		this.quality = quality;
		this.date = date;
	}

	@SuppressWarnings("javadoc")
	Long timestamp;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@SuppressWarnings({ "nls", "unqualified-field-access" })
	@Override
	public String toString() {
		return "DataPointValueObject [timestamp=" + timestamp + ", tag=" + tag + ", value=" + value + ", quality="
				+ quality + ", date=" + date + "]";
	}

	/**
	 * @return the timestamp
	 */
	public Long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return this.tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the quality
	 */
	public Integer getQuality() {
		return this.quality;
	}

	/**
	 * @param quality
	 *            the quality to set
	 */
	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	String tag;
	Object value;
	Integer quality;
	String date;

	/**
	 * @return the date
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

}