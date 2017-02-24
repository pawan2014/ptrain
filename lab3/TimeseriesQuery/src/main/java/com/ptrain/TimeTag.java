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

import java.util.ArrayList;

/**
 * 
 * @author predix -
 */
public class TimeTag {

	public ArrayList series = new ArrayList();
	public String name;
	public String id;

	public TimeTag(String name, String id) {
		this.name = name;
		this.id = id;

	}

	public void addObj(Long time, Object value) {
		ArrayList series1 = new ArrayList();
		series1.add(time);
		series1.add(value);
		series.add(series1);
	}

	
}
