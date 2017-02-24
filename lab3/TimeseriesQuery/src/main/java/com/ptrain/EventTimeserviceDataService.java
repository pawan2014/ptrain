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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ge.predix.entity.timeseries.datapoints.queryrequest.DatapointsQuery;
import com.ge.predix.entity.timeseries.datapoints.queryresponse.DatapointsResponse;
import com.ge.predix.solsvc.restclient.impl.RestClient;
import com.ge.predix.solsvc.timeseries.bootstrap.config.TimeseriesRestConfig;
import com.ge.predix.solsvc.timeseries.bootstrap.factories.TimeseriesFactory;

@SuppressWarnings("javadoc")
@RestController
@ComponentScan(basePackages = ("com.ge.predix.solsvc"))
public class EventTimeserviceDataService {

	@Autowired
	private RestClient restClient;
	@Autowired
	private TimeseriesRestConfig timeseriesRestConfig;
	@Autowired
	private TimeseriesFactory timeseriesFactory;

	@RequestMapping("/getTimeseriesData")
	public @ResponseBody() List getTimeseriesData(
			@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "machineList") String machineList) {
		// TODO Auto-generated method stub

		List<Header> headers = generateHeaders();
		DatapointsQuery dpQuery = buildTagQueryRequest(Long.parseLong(startDate), Long.parseLong(endDate), machineList,
				Integer.valueOf("1000"));
		DatapointsResponse response = this.timeseriesFactory.queryForDatapoints(this.timeseriesRestConfig.getBaseUrl(),
				dpQuery, headers);

		// RESPONSE ACCUMULATED in LIST
		TimeTag tm = getResponseInVISFormat(response);
		List st = new ArrayList();
		st.add(tm);

		return st;
	}

	@SuppressWarnings("nls")
	@PostConstruct
	public void init() {
		List<Header> headers = generateHeaders();
		headers.add(new BasicHeader("Origin", "http://predix.io")); //$NON-NLS-1$

	}

	@SuppressWarnings({})
	private List<Header> generateHeaders() {
		List<Header> headers = this.restClient.getSecureTokenForClientId();
		this.restClient.addZoneToHeaders(headers, this.timeseriesRestConfig.getZoneId());
		return headers;
	}

	private DatapointsQuery buildTagQueryRequest(long startDate, long endDate, String machineNumber, Integer taglimit) {
		DatapointsQuery datapointsQuery = new DatapointsQuery();
		List<com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag> tags = new ArrayList<com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag>();
		datapointsQuery.setStart(startDate);
		datapointsQuery.setEnd(endDate);
		String[] tagArray = machineNumber.split(","); //$NON-NLS-1$
		List<String> entryTags = Arrays.asList(tagArray);

		for (String entryTag : entryTags) {
			com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag tag = new com.ge.predix.entity.timeseries.datapoints.queryrequest.Tag();
			tag.setName(entryTag);
			tag.setLimit(taglimit);
			tag.setOrder("desc");
			tags.add(tag);
		}
		datapointsQuery.setTags(tags);
		return datapointsQuery;
	}

	public List<DataPointValueObject> getResponseData(DatapointsResponse response) {
		List<DataPointValueObject> dataPointValueObject = new ArrayList<DataPointValueObject>();
		java.util.ListIterator<com.ge.predix.entity.timeseries.datapoints.queryresponse.Tag> itr = response.getTags()
				.listIterator();

		while (itr.hasNext()) {
			com.ge.predix.entity.timeseries.datapoints.queryresponse.Tag tag = itr.next();
			String tagVal = tag.getName();
			List<Object> obj = tag.getResults().get(0).getValues();

			for (Object data : obj) {
				List lp = (List) data;

				Long timestamp = Long.valueOf(lp.get(0).toString());
				String date = unixToDate(timestamp);
				Object dataValue = getFinalValue(lp.get(1).toString());
				Integer quality = getInteger(lp.get(2).toString());
				DataPointValueObject dp1 = new DataPointValueObject(tagVal, timestamp, dataValue, quality, date);
				dataPointValueObject.add(dp1);

			}

		}
		System.out.println("FINAL dataPointValueObject--------->" + dataPointValueObject);

		return dataPointValueObject;
	}
	
	public TimeTag getResponseInVISFormat(DatapointsResponse response) {
		List<DataPointValueObject> dataPointValueObject = new ArrayList<DataPointValueObject>();
		java.util.ListIterator<com.ge.predix.entity.timeseries.datapoints.queryresponse.Tag> itr = response.getTags()
				.listIterator();

		TimeTag timeTag=null;
		while (itr.hasNext()) {
			com.ge.predix.entity.timeseries.datapoints.queryresponse.Tag tag = itr.next();
			String tagVal = tag.getName();
			List<Object> obj = tag.getResults().get(0).getValues();
			
			timeTag = new TimeTag("idnone",tagVal);	
			
			for (Object data : obj) {
				List lp = (List) data;

				Long timestamp = Long.valueOf(lp.get(0).toString());
				String date = unixToDate(timestamp);
				Object dataValue = getFinalValue(lp.get(1).toString());
				
				timeTag.addObj(timestamp, dataValue); 
				/*Integer quality = getInteger(lp.get(2).toString());
				DataPointValueObject dp1 = new DataPointValueObject(tagVal, timestamp, dataValue, quality, date);
				dataPointValueObject.add(dp1);*/

			}

		}
		System.out.println("FINAL dataPointValueObject--------->" + dataPointValueObject);

		return timeTag;
	}
	

	public static String unixToDate(long timestamp) {

		java.util.Date dateTime = new java.util.Date((long) timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		String formattedDate = sdf.format(dateTime);
		return formattedDate;

	}

	@SuppressWarnings({ "nls", "cast" })
	public static Object getFinalValue(String val) {

		boolean chekData = false;
		Object data = null;
		if (null != val) {
			chekData = val.matches("[-+]?\\d*\\.?\\d+");
		} else {
			val = "0";
		}
		if (chekData) {
			try {
				data = ((Object) Long.parseLong(val));
			} catch (NumberFormatException e) {
				data = ((Object) Double.parseDouble(val));
			}
		} else {
			data = val;
		}

		return data;
	}

	public static int getInteger(String s) {
		int inValue = 25;
		try {
			inValue = Integer.parseInt(s);

		} catch (NumberFormatException ex) {
			inValue = 0;
		}
		return inValue;
	}

}
