package com.site.visits.beam;

import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Sum;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import com.site.visits.Site;
import com.site.visits.Visit;

public class SumByContentTypeComposite extends PTransform<PCollection<String>, PCollection<KV<Long, Long>>> {
	@Override
	public PCollection<KV<Long, Long>> expand(PCollection<String> lines) {

		// Convert lines of json into site objects.
		PCollection<Site> sites = lines.apply(ParDo.of(new SiteFn()));
		PCollection<Visit> siteVisits = sites.apply(ParDo.of(new SiteVisitFn()));
		PCollection<KV<Long, Long>> categoryTypeKeyValues = siteVisits.apply(ParDo.of(new ContentKeyValueFn()));
		// Count the number of times each word occurs.
		PCollection<KV<Long, Long>> wordCounts = categoryTypeKeyValues.apply(Sum.<Long>longsPerKey());

		return wordCounts;
	}

}
