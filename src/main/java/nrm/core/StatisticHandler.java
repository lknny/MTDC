package nrm.core;

import nrm.pojo.NrmRule;
import nrm.pojo.RepData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by ${lknny@163.com} on 2017/8/15.
 */
public class StatisticHandler {
	private static final Logger log = LoggerFactory.getLogger(StatisticHandler.class);

	private StatisticHandler() {
	}

	public static List<String[]> statistic(NrmRule nrmRule, List<String[]> result) {
		/*
	* first key:分组字段值（组合），second key:计算字段，value:计算字段统计值
	* */
		Map<String, Map<String, Integer>> groupCalMap = new LinkedHashMap<>();
		getStatisticMap(nrmRule, result, groupCalMap);
		return convertStatisticMap2RepData(groupCalMap);
	}

	public static List<String[]> statisticRepDatas(NrmRule nrmRule, List<RepData> repDatas) {
		Map<String, Map<String, Integer>> groupCalMap = new LinkedHashMap<>();
		if (isValid(repDatas)) {
			for (RepData repData : repDatas) {
				getStatisticMap(nrmRule, repData.getData(), groupCalMap);
			}
		}
		return convertStatisticMap2RepData(groupCalMap);
	}


	private static List<String[]> convertStatisticMap2RepData(Map<String, Map<String, Integer>> groupCalMap) {
		List<String[]> statisResult = new LinkedList<>();
		for (Map.Entry<String, Map<String, Integer>> groupAndCal : groupCalMap.entrySet()) {
			String[] groupValues = groupAndCal.getKey().split(",");
			int calLen = groupAndCal.getValue().size();
			String[] statisRecord = new String[groupValues.length + calLen];
			System.arraycopy(groupValues, 0, statisRecord, 0, groupValues.length);
			int i = groupValues.length;
			for (Map.Entry<String, Integer> stringIntegerEntry : groupAndCal.getValue().entrySet()) {
				statisRecord[i++] = String.valueOf(stringIntegerEntry.getValue());
			}
			statisResult.add(statisRecord);
		}
		return statisResult;
	}

	private static void getStatisticMap(NrmRule nrmRule, List<String[]> result, Map<String, Map<String, Integer>> groupCalMap) {
		List<Integer> groupIndex = nrmRule.getNrmMocInfo().getGroupFieldsIndex();
		List<Integer> calIndex = nrmRule.getNrmMocInfo().getCalFieldsIndex();

		for (String[] values : result) {
			String groupFieldKey = getGroupFieldsValue(groupIndex, values);
			if (null == groupCalMap.get(groupFieldKey)) {
				groupCalMap.put(groupFieldKey, new LinkedHashMap<>());
			}
			for (Integer integer : calIndex) {
				String calFieldName = nrmRule.getNrmMocInfo().getMocFields()[integer.intValue()];
				try {
					statisticCalField(calFieldName, groupCalMap.get(groupFieldKey), values[integer.intValue()]);
				} catch (Exception e) {
					log.info("The Cal Field: " + calFieldName + " ,index: " + integer + " ,row data: " + Arrays.toString(values));
					log.info(e.getMessage(), e);
				}
			}
		}
	}

	private static void statisticCalField(String calFieldName, Map<String, Integer> calFieldStatisMap, String value) {
		if (null != calFieldStatisMap) {
			int calFieldValue = 0;
			if (!StringUtils.isEmpty(value)) {
				calFieldValue = Integer.parseInt(value);
			}
			if (null == calFieldStatisMap.get(calFieldName)) {
				calFieldStatisMap.put(calFieldName, calFieldValue);
			} else {
				int oldFieldValue = getOldFieldValue(calFieldName, calFieldStatisMap);
				calFieldStatisMap.put(calFieldName, calFieldValue + oldFieldValue);
			}
		}
	}

	private static int getOldFieldValue(String calFieldName, Map<String, Integer> calFieldStatisMap) {
		Integer index = calFieldStatisMap.get(calFieldName);
		return null != index ? index.intValue() : 0;
	}

	private static String getGroupFieldsValue(List<Integer> groupIndex, String[] values) {
		//获得分组字段值
		StringBuilder groupValue = new StringBuilder();
		for (Integer integer : groupIndex) {
			groupValue.append(values[integer.intValue()]).append(",");
		}
		int len = groupValue.length();
		if (len > 1) {
			groupValue.deleteCharAt(len - 1);
		}
		return groupValue.toString();
	}

	private static boolean isValid(List<?> dataSet) {
		return null != dataSet && !dataSet.isEmpty();
	}
}
