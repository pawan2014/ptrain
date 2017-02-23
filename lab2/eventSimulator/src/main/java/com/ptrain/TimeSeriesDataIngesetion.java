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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.ge.predix.entity.timeseries.datapoints.ingestionrequest.Body;
import com.ge.predix.entity.timeseries.datapoints.ingestionrequest.DatapointsIngestion;
import com.ge.predix.solsvc.restclient.impl.RestClient;
import com.ge.predix.solsvc.timeseries.bootstrap.config.TimeseriesRestConfig;
import com.ge.predix.solsvc.timeseries.bootstrap.factories.TimeseriesFactory;

//@Profile("dev")
@Component
@ComponentScan("com.ge.predix")
public class TimeSeriesDataIngesetion {
	@Autowired
	private TimeseriesRestConfig timeseriesRestConfig;
	@Autowired
	private RestClient restClient;
	@Autowired
	private TimeseriesFactory timeseriesFactory;

	private static Logger log = LoggerFactory.getLogger(TimeSeriesDataIngesetion.class);

	public void sendOneDataPoint(EventValueObject object) throws InterruptedException, ParseException {
		log.info("Preparing data to be sent.." + object);
		DatapointsIngestion dpIngestion1;
		List<Header> headers = generateHeaders();
		dpIngestion1 = createMetrics(object);
		this.timeseriesFactory.createConnectionToTimeseriesWebsocket(headers);
		this.timeseriesFactory.postDataToTimeseriesWebsocket(dpIngestion1, headers);
		this.timeseriesFactory.closeConnectionToTimeseriesWebsocket();
		log.debug("Sending one data point");

	}

	private Long generateTimestampsWithinYear(Long current) {
		long yearInMMS = Long.valueOf(31536000000L);
		return ThreadLocalRandom.current().nextLong(current - yearInMMS, current + 1);
	}

	private DatapointsIngestion createMetrics(EventValueObject obj) throws InterruptedException {
		log.info(">> createMetrics");
		DatapointsIngestion dpIngestion = new DatapointsIngestion();
		dpIngestion.setMessageId(String.valueOf(System.currentTimeMillis()));
		Body body = new Body();
		body.setName(obj.getMachineNumber()+":"+obj.getTagNumber()); //$NON-NLS-1$
		List<Object> datapoint1 = new ArrayList<Object>();
		datapoint1.add(Long.parseLong(obj.getTimestamp()));
		datapoint1.add(obj.getDatavalue());
		datapoint1.add("1"); // quality

		List<Object> datapoints = new ArrayList<Object>();
		datapoints.add(datapoint1);
		body.setDatapoints(datapoints);

		List<Body> bodies = new ArrayList<Body>();
		bodies.add(body);

		dpIngestion.setBody(bodies);
		return dpIngestion;

	}

	private void postData(List<Header> headers, DatapointsIngestion dpIngestion1) throws InterruptedException {

		this.timeseriesFactory.createConnectionToTimeseriesWebsocket(headers);
		this.timeseriesFactory.postDataToTimeseriesWebsocket(dpIngestion1, headers);

		this.timeseriesFactory.closeConnectionToTimeseriesWebsocket();

	}

	private DatapointsIngestion createWraper(EventValueObject object) throws ParseException {
		log.info("Create Wrapper and return DatapointsIngestion Obj..");
		ArrayList bodyList = new ArrayList();
		//
		DatapointsIngestion dpIngestion1 = new DatapointsIngestion();
		dpIngestion1.setMessageId(String.valueOf(System.currentTimeMillis()));
		//
		Body body = createBody(object.getMachineNumber(), object.getTimestamp(), object.getDatavalue(),
				object.getTagNumber());
		bodyList.add(body);
		//
		dpIngestion1.setBody(bodyList);
		return dpIngestion1;
	}

	private Body createBody(String machineNumber, String timeStamp, String data, String tagName) throws ParseException {
		log.info("Create Body..");
		Body body = new Body();
		body.setName(machineNumber + ":" + tagName);

		List<Object> datapoint1 = new ArrayList<Object>();
		datapoint1.add(getLongFromLong(timeStamp));

		datapoint1.add(data);
		datapoint1.add(3); // quality

		List<Object> datapointsList = new ArrayList<Object>();
		datapointsList.add(datapoint1);

		body.setDatapoints(datapointsList);
		com.ge.predix.entity.util.map.Map map = new com.ge.predix.entity.util.map.Map();
		map.put("machineName", machineNumber); // $NON-NLS-2$
		map.put("Date", getDateAsFormated());
		body.setAttributes(map);

		return body;
	}

	public static long getLongFromLong(String date) throws ParseException {
		return Long.valueOf(date);
	}

	public static String getDateAsFormated() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static long getLongFromDate(String date) throws ParseException {
		// 8/22/2014 16:38i
		// SimpleDateFormat fwm = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		SimpleDateFormat fws = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		d = fws.parse(date);

		long milliseconds = d.getTime();
		return milliseconds;
	}

	@SuppressWarnings({ "unqualified-field-access", "nls" })
	private List<Header> generateHeaders() {
		List<Header> headers = this.restClient.getSecureTokenForClientId();
		headers.add(new BasicHeader("Origin", "http://predix.io"));
		this.restClient.addZoneToHeaders(headers, this.timeseriesRestConfig.getZoneId());
		return headers;
	}
}