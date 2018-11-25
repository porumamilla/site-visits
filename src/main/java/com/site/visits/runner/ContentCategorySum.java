package com.site.visits.runner;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import com.site.visits.beam.SumByCategoryTypeComposite;

public class ContentCategorySum {
	/** A SimpleFunction that converts a Word and Count into a printable string. */
	  public static class FormatAsTextFn extends SimpleFunction<KV<Long, Long>, String> {
	    @Override
	    public String apply(KV<Long, Long> input) {
	    	System.out.println(input.getKey() + ": " + input.getValue());
	      return input.getKey() + ": " + input.getValue();
	    }
	  }
	/*public static void main(String[] args) {
		ContentCategoryOptions options =
		        PipelineOptionsFactory.fromArgs(args).withValidation().as(ContentCategoryOptions.class);
		Pipeline p = Pipeline.create(options);
		PCollection<String> jsonLines = p.apply(TextIO.read().from(options.getInputFile()));
		PCollection<KV<Long, Long>> sumByCategoryType = jsonLines.apply(new SumByCategoryTypeComposite());
		sumByCategoryType
				.apply(MapElements.via(new FormatAsTextFn()))
				.apply(TextIO.write().to(options.getOutput()));
		p.run().waitUntilFinish();
	}*/
}
