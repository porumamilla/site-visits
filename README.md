# site-visits
This is a Google data flow project and is created to implement the pipeline for handling the site visit events coming from different sources whether it's batching or streaming. And the event comes in the form of JSON string. 
Pipeline takes the JSON event data and parses it. A composite will take the parsed json data and sums up the timeSpent value by category type.

## Sample JSON Event data
```json
{
"site": "Youtube",
"visits": [
			{"timeSpent": 7200,"contentType": 1,"contentCategory": 2,"url": "youtube.com/godfather"},
			{"timeSpent": 1200,"contentType": 2,"contentCategory": 1,"url": "youtube.com/python"},
			{"timeSpent": 2200,"contentType": 1,"contentCategory": 2,"url": "youtube.com/latenightshow"},
			{"timeSpent": 3200,"contentType": 2,"contentCategory": 1,"url": "youtube.com/agile-methodology"}
		  ]
}
```
## Pipeline implementation in Java
```java
ContentCategoryOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
				.as(ContentCategoryOptions.class);
		Pipeline p = Pipeline.create(options);

		PCollection<String> jsonLines = p.apply(TextIO.read().from(options.getInputFile()));
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
						.withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_TRUNCATE));

		p.run().waitUntilFinish();
```