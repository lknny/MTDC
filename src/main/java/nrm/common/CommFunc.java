package nrm.common;

import nrm.core.TableDataSet;
import nrm.pojo.BaseMocInfo;
import nrm.pojo.DataSetKey;

import java.text.SimpleDateFormat;
import java.util.*;

public class CommFunc {
	private CommFunc() {

	}
	/**
	 * 枚举的处理
	 *
	 * @param enumMap
	 * @param value
	 * @return
	 */
	public static String enumHandle(Map<String, String> enumMap, String value) {
		String ret = enumMap.get(value);
		if (ret == null) {
			return enumMap.get("other");
		} else {
			return ret;
		}
	}

	/**
	 * 日期格式
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateHandle(long date, String pattern) {
		Date currDate = new Date(date);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(currDate);
	}

	/**
	 * 将2个同元素个数的list中元素按行合并成一个字符串。list元素之间用定制的分隔符，每行用；分隔
	 * 比如list1=[1,2],list2=[3,4]，分隔符为-。那么合并后字符串为1-3;2-4
	 *
	 * @param separator
	 * @param listValue1
	 * @param listValue2
	 * @return
	 */
	public static String join2List(String separator, List<? extends Object> listValue1, List<? extends Object> listValue2) {
		StringBuilder sb = new StringBuilder();
		int size = listValue1.size();
		for (int i = 0; i < size; i++) {
			sb.append(listValue1.get(i));
			sb.append(separator);
			sb.append(listValue2.get(i));
			if (i < (size - 1)) {
				sb.append(";");
			}
		}
		return sb.toString();
	}

	/**
	 * 将3个同元素个数的list中元素按行合并成一个字符串。list元素之间用定制的分隔符，每行用；分隔
	 * 比如list1=[1,2],list2=[3,4],list3=[5,6]，分隔符为-。那么合并后字符串为1-3-5;2-4-6
	 *
	 * @param separator
	 * @param listValue1
	 * @param listValue2
	 * @param listValue3
	 * @return
	 */
	public static String join3List(String separator, List<? extends Object> listValue1, List<? extends Object> listValue2, List<? extends Object> listValue3) {
		StringBuilder sb = new StringBuilder();
		int size = listValue1.size();
		for (int i = 0; i < size; i++) {
			sb.append(listValue1.get(i));
			sb.append(separator);
			sb.append(listValue2.get(i));
			sb.append(separator);
			sb.append(listValue3.get(i));
			if (i < (size - 1)) {
				sb.append(";");
			}
		}
		return sb.toString();
	}

	/**
	 * 将用；分隔元素的字符串，转换成List
	 *
	 * @param arr
	 * @return
	 */
	public static List<String> moArr2List(String arr) {
		String[] temp = arr.split(";");
		return Arrays.asList(temp);
	}

	/**
	 * moi字符串都是形如A=a,B=b,C=c 的形式。其中A，B，C是mocName。用户输入一个mocName，比如为B，那么截断的结果就是A=a,B=b
	 *
	 * @param moi
	 * @param mocName
	 * @return
	 */
	public static String cutOffLDN(String moi, String mocName) {
		String[] rdnArr = moi.split(",");
		StringBuilder sb = new StringBuilder();
		for (String rdn : rdnArr) {
			sb.append(rdn);
			String currMocName = rdn.substring(0, rdn.indexOf('='));
			if (currMocName.equals(mocName)) {
				break;
			}
			sb.append(",");
		}
		return sb.toString();
	}

	/**
	 * 自定义函数的示例。根据master表中当前记录值，通过pk，找辅表mocName的记录，并提取AttrName的值。
	 * xml文件中pk是第2个key，所以不会绑定到EL表达式中，因此需要用自定义函数来访问。
	 *
	 * @param srcData
	 * @param rowIdx
	 * @param emMocInfo
	 * @param mocName
	 * @param attrName
	 * @return
	 */
	public static String getAttrByPk(Object srcData, Object rowIdx, Object emMocInfo, String mocName, String attrName) {
		TableDataSet[] dataSet = (TableDataSet[]) srcData;
		int row = ((Integer) rowIdx).intValue();
		BaseMocInfo[] smi = (BaseMocInfo[]) emMocInfo;
		TableDataSet masterData = dataSet[0];

		TableDataSet auxData = getTableDataSet(mocName, dataSet);
		DataSetKey key = getDataSetKey(mocName, smi);
		String[] fields = new String[0];
		if (null!=key){
			fields = key.getRefField();
		}
		Object[] values = new Object[fields.length];
		for (int k = 0; k < fields.length; k++) {
			values[k] = masterData.getOneElement(row, fields[k]);
		}//get key values
		Object ret = null;
		if (null!=auxData){
			ret = auxData.getElementByPk(attrName, values);
		}
		return ret == null ? "" : ret.toString();
	}

	private static TableDataSet getTableDataSet(String mocName, TableDataSet[] dataSet) {
		TableDataSet auxData = null;
		for (int i = 1; i < dataSet.length; i++) {
			TableDataSet aux = dataSet[i];
			if (mocName.equals(aux.getTableName())) {
				auxData = aux;
				break;
			}
		}
		return auxData;
	}

	private static DataSetKey getDataSetKey(String mocName, BaseMocInfo[] smi) {
		DataSetKey key = null;
		for (BaseMocInfo bmi : smi) {
			if (mocName.equals(bmi.getMocName())) {
				key = getPk(bmi);
			}
		}
		return key;
	}

	private static DataSetKey getPk( BaseMocInfo bmi) {
		DataSetKey key=null;
		for (DataSetKey dsk : bmi.getKeys()) {
			if (dsk.getKeyType() == DataSetKey.PK) {
				key = dsk;
				break;
			}
		}
		return key;
	}
}
