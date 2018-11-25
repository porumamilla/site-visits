package com.site.visits.runner;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import com.site.visits.beam.SumByContentTypeComposite;

public class ContentTypeSum {
	/** A SimpleFunction that converts a Word and Count into a printable string. */
	  public static class FormatAsTextFn extends SimpleFunction<KV<Long, Long>, String> {
	    @Override
	    public String apply(KV<Long, Long> input) {
	    	System.out.println(input.getKey() + ": " + input.getValue());
	      return input.getKey() + ": " + input.getValue();
	    }
	  }
	public static void main(String[] args) {
		PipelineOptions options = PipelineOptionsFactory.create();
		Pipeline p = Pipeline.create(options);
		PCollection<String> jsonLines = p.apply(TextIO.read().from("C:\\Projects\\GCP\\Beam\\site-visits\\SiteVisits.json"));
		PCollection<KV<Long, Long>> sumByCategoryType = jsonLines.apply(new SumByContentTypeComposite());
		sumByCategoryType
				.apply(MapElements.via(new FormatAsTextFn()))
				.apply(TextIO.write().to("SumByContentTypeComposite"));
		p.run().waitUntilFinish();
	}
}
