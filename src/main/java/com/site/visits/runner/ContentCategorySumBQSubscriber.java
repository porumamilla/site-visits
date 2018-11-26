package com.site.visits.runner;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.Duration;

import com.site.visits.runner.options.ContentCategoryPubSubOptions;
import com.site.visits.transforms.SumByCategoryTypeComposite;
import com.site.visits.util.SchemaUtil;
import com.site.visits.util.TableRowUtil;

public class ContentCategorySumBQSubscriber {

	public static void main(String[] args) {
		
		ContentCategoryPubSubOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
				.as(ContentCategoryPubSubOptions.class);
		options.as(DataflowPipelineOptions.class).setStreaming(true);
		
		Pipeline p = Pipeline.create(options);
	
		FixedWindows window = FixedWindows.of(Duration.standardMinutes(new Integer(2)));
		PCollection<String> jsonLines = p.apply(PubsubIO.readStrings().fromTopic(options.getPubsubTopic())).apply(Window.<String> into(window));
		
		PCollection<KV<Long, Long>> sumByCategoryType = jsonLines.apply(new SumByCategoryTypeComposite());
		sumByCategoryType
				.apply(BigQueryIO.<KV<Long, Long>>write().to(SchemaUtil.TBL_NAME_SUM_BY_CONTENT_CAT_TYPE)
						.withSchema(SchemaUtil.SUM_BY_CONTENT_CAT_TYPE_SCHEMA)
						.withFormatFunction(tuple -> TableRowUtil.getTableRowForSumByContentCatType(tuple))
						.withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));

		p.run().waitUntilFinish();
	}
}
