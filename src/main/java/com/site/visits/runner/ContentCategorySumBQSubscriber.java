package com.site.visits.runner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import org.joda.time.Duration;
import com.site.visits.beam.SumByCategoryTypeComposite;

import avro.shaded.com.google.common.collect.ImmutableList;

public class ContentCategorySumBQSubscriber {
	/**
	 * A SimpleFunction that converts a Word and Count into a printable string.
	 */
	public static class FormatAsTextFn extends SimpleFunction<KV<Long, Long>, String> {
		@Override
		public String apply(KV<Long, Long> input) {
			System.out.println(input.getKey() + ": " + input.getValue());
			return input.getKey() + ": " + input.getValue();

		}
	}

	public static String getDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		return sdf.format(date);
	}

	public static String getTimestamp() {
		SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormatUTC.format(new Date());

	}

	public static void main(String[] args) {
		
		ContentCategoryPubSubOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
				.as(ContentCategoryPubSubOptions.class);
		options.as(DataflowPipelineOptions.class).setStreaming(true);
		
		Pipeline p = Pipeline.create(options);
	
		FixedWindows window = FixedWindows.of(Duration.standardMinutes(new Integer(2)));
		PCollection<String> jsonLines = p.apply(PubsubIO.readStrings().fromTopic(options.getPubsubTopic())).apply(Window.<String> into(window));
		
		PCollection<KV<Long, Long>> sumByCategoryType = jsonLines.apply(new SumByCategoryTypeComposite());
		sumByCategoryType
				.apply(BigQueryIO.<KV<Long, Long>>write().to("sitevisits-195700:sitevisits.SUM_BY_CONTENT_CAT_TYPE")
						.withSchema(new TableSchema().setFields(
								ImmutableList.of(new TableFieldSchema().setName("USER_NAME").setType("STRING"),
										new TableFieldSchema().setName("CAT_TYPE").setType("INTEGER"),
										new TableFieldSchema().setName("NUM_MINUTES_SPENT").setType("NUMERIC"),
										new TableFieldSchema().setName("DATE").setType("DATE"),
										new TableFieldSchema().setName("UPDATED_TIME_STAMP").setType("TIMESTAMP"))))
						.withFormatFunction(quote -> new TableRow().set("USER_NAME", "porumamilla_raghu")
								.set("CAT_TYPE", quote.getKey()).set("NUM_MINUTES_SPENT", quote.getValue())
								.set("DATE", getDate(new Date())).set("UPDATED_TIME_STAMP", getTimestamp()))
						.withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));

		p.run().waitUntilFinish();
	}
}
