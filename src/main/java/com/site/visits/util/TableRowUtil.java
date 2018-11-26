package com.site.visits.util;

import java.util.Date;

import org.apache.beam.sdk.values.KV;

import com.google.api.services.bigquery.model.TableRow;

public class TableRowUtil {
	public static TableRow getTableRowForSumByContentCatType(KV<Long, Long> tuple) {
		return new TableRow().set("USER_NAME", "porumamilla_raghu")
		.set("CAT_TYPE", tuple.getKey()).set("NUM_MINUTES_SPENT", tuple.getValue())
		.set("DATE", DateUtil.getDate(new Date())).set("UPDATED_TIME_STAMP", DateUtil.getTimestamp());
	}
}
