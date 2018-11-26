package com.site.visits.transforms;

import org.apache.beam.sdk.transforms.DoFn;

import com.site.visits.model.Site;
import com.site.visits.model.Visit;

public class SiteVisitFn extends DoFn<Site, Visit> {
	@ProcessElement
	public void processElement(ProcessContext c) {
		try {
			for (Visit visit : c.element().getVisits()) {
				c.output(visit);
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
}
