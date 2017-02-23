/*
 * Copyright (c) 2016 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ptrain;

import java.util.Random;

/**
 * 
 * @author predix -
 */
public class EventValueObject {

	private String tagNumber;
	private String timestamp;
	private String machineNumber;
	private String datavalue;
	private String quality;
	static Random randomno = new Random();

	/**
	 * -
	 */
	public EventValueObject() {
		super();
	}

	/**
	 * @param timestamp
	 * @param machineNumber
	 * @param datavalue
	 * @param quality
	 *            -
	 */
	public EventValueObject(String timestamp, String machineNumber, String datavalue, String quality) {
		super();
		this.timestamp = timestamp;
		this.machineNumber = machineNumber;
		this.datavalue = datavalue;
		this.quality = quality;
	}

	public static EventValueObject createObject(String someValue) {
		EventValueObject b1 = new EventValueObject();
		b1.setMachineNumber(someValue);
		b1.setTagNumber("vibration_tg");
		b1.setDatavalue(Double.toString(getRandonVaue()));
		b1.setQuality("1");
		b1.setTimestamp("" + System.currentTimeMillis());

		return b1;
	}

	public static EventValueObject createObject(String machine, String tag, String data, String quality) {
		EventValueObject b1 = new EventValueObject();
		b1.setMachineNumber(machine);
		b1.setTagNumber(tag);
		b1.setDatavalue(data);
		b1.setQuality(quality);
		b1.setTimestamp("" + System.currentTimeMillis());

		return b1;
	}

	public static double getRandonVaue() {
		
		int max=13;
		int min=0;

		double[] intArray = new double[20];
		intArray[0] = 91.23;
		intArray[1] = 92.251;
		intArray[2] = 90.10;
		intArray[3] = 85.26;
		intArray[4] = 80.01;
		intArray[5] = 75.00;
		intArray[6] = 70.23;
		intArray[7] = 65.36;
		intArray[8] = 60.23;
		intArray[9] = 55.02;
		intArray[10] = 40.36;
		intArray[11] = 35.89;
		intArray[12] = 30.25;
		intArray[13] = 6.36;
		intArray[14] = 100.36;

		int c = randomno.nextInt(max - min + 1) + min;
		return intArray[c];
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the machineNumber
	 */
	public String getMachineNumber() {
		return this.machineNumber;
	}

	/**
	 * @param machineNumber
	 *            the machineNumber to set
	 */
	public void setMachineNumber(String machineNumber) {
		this.machineNumber = machineNumber;
	}

	/**
	 * @return the datavalue
	 */
	public String getDatavalue() {
		return this.datavalue;
	}

	/**
	 * @param datavalue
	 *            the datavalue to set
	 */
	public void setDatavalue(String datavalue) {
		this.datavalue = datavalue;
	}

	/**
	 * @return the quality
	 */
	public String getQuality() {
		return this.quality;
	}

	/**
	 * @param quality
	 *            the quality to set
	 */
	public void setQuality(String quality) {
		this.quality = quality;
	}

	/**
	 * @return the tagNumber
	 */
	public String getTagNumber() {
		return this.tagNumber;
	}

	/**
	 * @param tagNumber
	 *            the tagNumber to set
	 */
	public void setTagNumber(String tagNumber) {
		this.tagNumber = tagNumber;
	}

	@Override
	public String toString() {
		return "AWSEventValueObject [tagNumber=" + tagNumber + ", timestamp=" + timestamp + ", machineNumber="
				+ machineNumber + ", datavalue=" + datavalue + ", quality=" + quality + "]";
	}

}
