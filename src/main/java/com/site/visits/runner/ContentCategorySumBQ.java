package com.site.visits.runner;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import com.site.visits.runner.options.ContentCategoryOptions;
import com.site.visits.transforms.SumByCategoryTypeComposite;
import com.site.visits.util.SchemaUtil;
import com.site.visits.util.TableRowUtil;

public class ContentCategorySumBQ {

	public static void main(String[] args) {
		
		ContentCategoryOptions options = PipelineOptionsFactory
										 .fromArgs(args)
										 .withValidation()
										 .as(ContentCategoryOptions.class);
		Pipeline pipeline = Pipeline.create(options);

		PCollection<String> jsonLines = pipeline
										.apply(TextIO.read().from(options.getInputFile()));
		
		PCollection<KV<Long, Long>> sumByCategoryTypeTuples = jsonLines.apply(new SumByCategoryTypeComposite());
		sumByCategoryTypeTuples.apply(BigQueryIO.<KV<Long, Long>>write().to(SchemaUtil.TBL_NAME_SUM_BY_CONTENT_CAT_TYPE)
							   .withSchema(SchemaUtil.SUM_BY_CONTENT_CAT_TYPE_SCHEMA)
						       .withFormatFunction(tuple -> TableRowUtil.getTableRowForSumByContentCatType(tuple))
						       .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));

		pipeline.run().waitUntilFinish();
	}
}
