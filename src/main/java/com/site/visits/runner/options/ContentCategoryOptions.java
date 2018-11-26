package com.site.visits.runner.options;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.ValueProvider;

public interface  ContentCategoryOptions extends PipelineOptions  {
	@Description("Path of the file to read from")
    @Default.String("gs://sitevisits/input/SiteVisits.json")
	ValueProvider<String> getInputFile();

    void setInputFile(ValueProvider<String> value);
}
