# site-visits
This is a Google data flow project and is created to implement the pipeline for handling the site visit events coming from different sources whether it's batching or streaming. And the event comes in the form of JSON string. 
Pipeline reads the JSON event data and a composite will take the json data and sums up the timeSpent value by category type. Then finally saves the aggregated sum in the BigQuery table SUM_BY_CONTENT_CAT_TYPE

## Sample JSON Event data
```json
{
"site": "Youtube",
"visits":
	[
	{"timeSpent": 7200,"contentType": 1,"contentCategory": 2,"url": "youtube.com/godfather"},
	{"timeSpent": 1200,"contentType": 2,"contentCategory": 1,"url": "youtube.com/python"},
	{"timeSpent": 2200,"contentType": 1,"contentCategory": 2,"url": "youtube.com/latenightshow"},
	{"timeSpent": 3200,"contentType": 2,"contentCategory": 1,"url": "youtube.com/agile-methodology"}
	]
}
```
## Batch Pipeline implementation
```java
//Create a batch pipe line options object and it contains where to read the file from
ContentCategoryOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().
								 as(ContentCategoryOptions.class);
								 
//Create a pipeline with the above options object								 
Pipeline pipeline = Pipeline.create(options);

//Read the json lines from input file
PCollection<String> jsonLines = pipeline.apply(TextIO.read().from(options.getInputFile()));

//Get the sum aggregations by category type		
PCollection<KV<Long, Long>> sumByCategoryTypeTuples = jsonLines.apply(new SumByCategoryTypeComposite());

//Write the aggregation tupes to the BigQuery table
sumByCategoryTypeTuples.apply(BigQueryIO.<KV<Long, Long>>write()
				.to(SchemaUtil.TBL_NAME_SUM_BY_CONTENT_CAT_TYPE)
				.withSchema(SchemaUtil.SUM_BY_CONTENT_CAT_TYPE_SCHEMA)
				.withFormatFunction(tuple -> TableRowUtil.getTableRowForSumByContentCatType(tuple))
				.withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));
//Run the pipeline
pipeline.run().waitUntilFinish();
```
## Streaming Pipeline implementation
```java
ContentCategoryPubSubOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
				.as(ContentCategoryPubSubOptions.class);
options.as(DataflowPipelineOptions.class).setStreaming(true);
		
Pipeline p = Pipeline.create(options);
	
FixedWindows window = FixedWindows.of(Duration.standardMinutes(new Integer(2)));
PCollection<String> jsonLines = p.apply(PubsubIO.readStrings().fromTopic(
options.getPubsubTopic())).apply(Window.<String> into(window));
		
PCollection<KV<Long, Long>> sumByCategoryType = jsonLines.apply(new SumByCategoryTypeComposite());
sumByCategoryType
	.apply(BigQueryIO.<KV<Long, Long>>write().to(SchemaUtil.TBL_NAME_SUM_BY_CONTENT_CAT_TYPE)
	.withSchema(SchemaUtil.SUM_BY_CONTENT_CAT_TYPE_SCHEMA)
	.withFormatFunction(tuple -> TableRowUtil.getTableRowForSumByContentCatType(tuple))
	.withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));

	p.run().waitUntilFinish();
```