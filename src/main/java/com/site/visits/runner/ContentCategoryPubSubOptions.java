package com.site.visits.runner;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;

public interface ContentCategoryPubSubOptions extends DataflowPipelineOptions {
	@Description("Path of the file to read from")
    @Default.String("projects/sitevisits-195700/topics/sitevisits")
	String getPubsubTopic();

	void setPubsubTopic(String pubsubTopic);
}
