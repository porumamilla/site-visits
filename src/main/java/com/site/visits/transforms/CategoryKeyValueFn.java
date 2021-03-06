package com.site.visits.transforms;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

import com.site.visits.model.Visit;

public class CategoryKeyValueFn extends DoFn<Visit, KV<Long, Long>> {
	
	@ProcessElement
	public void processElement(ProcessContext c) {
		try {
			c.output(KV.of(c.element().getContentCategory(), c.element().getTimeSpent()));
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
}
