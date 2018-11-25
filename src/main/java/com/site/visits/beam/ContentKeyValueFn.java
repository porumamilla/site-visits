package com.site.visits.beam;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

import com.site.visits.Visit;

public class ContentKeyValueFn extends DoFn<Visit, KV<Long, Long>> {
	
	@ProcessElement
	public void processElement(ProcessContext c) {
		try {
			c.output(KV.of(c.element().getContentType(), c.element().getTimeSpent()));
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

}
