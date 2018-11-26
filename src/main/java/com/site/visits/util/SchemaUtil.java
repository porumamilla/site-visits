package com.site.visits.util;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

import avro.shaded.com.google.common.collect.ImmutableList;

public class SchemaUtil {
	public static String TBL_NAME_SUM_BY_CONTENT_CAT_TYPE = "sitevisits-195700:sitevisits.SUM_BY_CONTENT_CAT_TYPE";
	
	public static final TableSchema SUM_BY_CONTENT_CAT_TYPE_SCHEMA = new TableSchema().setFields(
			ImmutableList.of(new TableFieldSchema().setName("USER_NAME").setType("STRING"),
					new TableFieldSchema().setName("CAT_TYPE").setType("INTEGER"),
					new TableFieldSchema().setName("NUM_MINUTES_SPENT").setType("NUMERIC"),
					new TableFieldSchema().setName("DATE").setType("DATE"),
					new TableFieldSchema().setName("UPDATED_TIME_STAMP").setType("TIMESTAMP")));
}
