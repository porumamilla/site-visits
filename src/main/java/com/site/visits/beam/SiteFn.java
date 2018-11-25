package com.site.visits.beam;

import java.util.ArrayList;
import java.util.List;

import org.apache.beam.sdk.transforms.DoFn;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.site.visits.Site;
import com.site.visits.Visit;

public class SiteFn extends DoFn<String, Site> {

	@ProcessElement
	public void processElement(ProcessContext c) {
		try {
			c.output(parseJson(c.element()));
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	public Site parseJson(String json) throws Exception {
		// TODO Auto-generated method stub
		JsonFactory jfactory = new JsonFactory();
		JsonParser jParser = jfactory.createParser(json);
		Site site = new Site();

		while (jParser.nextToken() != JsonToken.END_OBJECT) {
			String fieldname = jParser.getCurrentName();
			if ("site".equals(fieldname)) {
				jParser.nextToken();
				site.setName(jParser.getText());
			}

			if ("visits".equals(fieldname)) {
				jParser.nextToken();
				List<Visit> visits = new ArrayList<Visit>();
				while (jParser.nextToken() != JsonToken.END_ARRAY) {
					Visit visit = new Visit();
					while (jParser.nextToken() != JsonToken.END_OBJECT) {
						fieldname = jParser.getCurrentName();
						jParser.nextToken();
						if ("timeSpent".equals(fieldname)) {
							visit.setTimeSpent(jParser.getValueAsLong());
						} else if ("contentType".equals(fieldname)) {
							visit.setContentType(jParser.getValueAsLong());
						} else if ("contentCategory".equals(fieldname)) {
							visit.setContentCategory(Long.parseLong(jParser.getText()));
						} else if ("url".equals(fieldname)) {
							visit.setUrl(jParser.getText());
						}
						
						
					}
					visits.add(visit);
				}
				site.setVisits(visits);
			}
		}
		jParser.close();
		return site;

	}
	
	public static void main(String[] args) throws Exception {
		SiteFn parseJsonFn = new SiteFn();
		Site site = parseJsonFn.parseJson("{\"site\": \"Youtube\",\"visits\": [{\"timeSpent\": 7200,\"contentType\": 1,\"contentCategory\": 2,\"url\": \"youtube.com/godfather\"},{\"timeSpent\": 1200,\"contentType\": 2,\"contentCategory\": 1,\"url\": \"youtube.com/python\"},{\"timeSpent\": 2200,\"contentType\": 1,\"contentCategory\": 2,\"url\": \"youtube.com/latenightshow\"},{\"timeSpent\": 3200,\"contentType\": 2,\"contentCategory\": 1,\"url\": \"youtube.com/agile-methodology\"}]}");
		System.out.println(site);
	}

}
