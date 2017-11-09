package nrm.core;

import com.zte.ums.cnms.cm.repworker.nrm.common.CommFunc;
import com.zte.ums.cnms.cm.repworker.nrm.common.NrmConstant;
import com.zte.ums.cnms.cm.repworker.nrm.pojo.BaseMocInfo;
import com.zte.ums.cnms.cm.repworker.nrm.pojo.DataSetKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELProcessor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 实现MappingNrmRuleBase的抽象类，定义了基本的数据结构和访问方法。
 * 用户继承该抽象类后只需要在构造函数中定义原moc和NRMmoc的模型信息并实现mappingNrmAttr方法。
 *
 * @author 10015496
 */
public class MappingNrmRule {
	private static final Logger log = LoggerFactory.getLogger(MappingNrmRule.class);
	/**
	 * BaseMocInfo[]第1个元素是master moc，后续元素是aux moc。
	 * 注意TableDataSet和BaseMocInfo数组对应的次序要一致
	 */
	private TableDataSet[] emMocData = null;
	private BaseMocInfo[] emMocInfo = null;
	private BaseMocInfo nrmMocInfo = null;
	private ELProcessor elProc = null;
	private Map<String, Map<String, String>> initBeanMap;

	private boolean expandFlag = false;
	private List<Integer> expandFieldsPos = null;
	private String filterStr = null;


	public boolean isExpandFlag() {
		return expandFlag;
	}

	public void initFilter(String filterStr) {
		if (null != filterStr) {
			this.filterStr = filterStr.substring(2, filterStr.lastIndexOf('}'));
		}
	}

	public void initExpandPos(List<Integer> expandFieldsPos) {
		if (null != expandFieldsPos) {
			this.expandFieldsPos = new LinkedList<>();
			this.expandFieldsPos.addAll(expandFieldsPos);
			this.expandFlag = true;
		}
	}

	/**
	 * 初始化模型映射文件
	 */
	public void initModel(BaseMocInfo nrmMocInfo, BaseMocInfo[] emMocInfo, Map<String, Map<String, String>> initBeanMap) {
		this.nrmMocInfo = nrmMocInfo;
		//数组copy
		this.emMocInfo = emMocInfo.clone();
		this.initBeanMap = initBeanMap;
	}

	/**
	 * 初始化外部函数，可以多次调用，每次增加一个外部函数。
	 * 外部函数必须是静态方法，传入的参数可以是普通的常量以及EL表达式上下文注入的变量。
	 *
	 * @param method
	 * @param name
	 * @throws NoSuchMethodException
	 */
	public void initExtFunc(Method method, String name) throws NoSuchMethodException {
		this.elProc.defineFunction("ext", name, method);
	}

	public void initExtBeans(String name, Object bean) {
		this.elProc.defineBean(name, bean);
	}

	/**
	 * 初始化数据
	 *
	 * @param emMocData
	 */
	public void initData(TableDataSet[] emMocData) {
		this.emMocData = emMocData.clone();
		this.elProc = new ELProcessor();
		this.elProc.defineBean("srcData", this.emMocData);
		this.elProc.defineBean("nrmMocInfo", this.nrmMocInfo);
		this.elProc.defineBean("emMocInfo", this.emMocInfo);
		try {
			Method method = CommFunc.class.getMethod("enumHandle", new Class[]{Map.class, String.class});
			this.elProc.defineFunction("naf", "enumHandle", method);

			method = CommFunc.class.getMethod("dateHandle", new Class[]{long.class, String.class});
			this.elProc.defineFunction("naf", "dateHandle", method);

			method = CommFunc.class.getMethod("join2List", new Class[]{String.class, List.class, List.class});
			this.elProc.defineFunction("naf", "join2List", method);

			method = CommFunc.class.getMethod("join3List", new Class[]{String.class, List.class, List.class, List.class});
			this.elProc.defineFunction("naf", "join3List", method);

			method = CommFunc.class.getMethod("moArr2List", new Class[]{String.class});
			this.elProc.defineFunction("naf", "moArr2List", method);

		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}
	}

	/**
	 * 一个NRM moc的一条记录的映射函数，返回nrm属性值的数组
	 * 如果preCheckStr为空的话，一定会返回一个String[]，否则可能返回null，代表略过某条映射记录。
	 *
	 * @param rowIndex master moc的当前记录号
	 * @return nrm属性值的数组
	 */
	public Object[] mappingNrmAttr(int rowIndex) {
		this.elProc.defineBean("rowIdx", rowIndex);
		Map<String, Object> masterDataMap = new HashMap<>();    //masterMap,use by auxdata
		//将数据写入到el表达式的上下文中...
		setElContext(rowIndex, masterDataMap);
		for (Map.Entry<String, Map<String, String>> element : this.initBeanMap.entrySet()) {
			this.elProc.defineBean(element.getKey(), this.initBeanMap.get(element.getKey()));
		}

		if (this.filterStr != null && (!(boolean) this.elProc.getValue(this.filterStr, boolean.class))) {
			return new Object[0];
		}
		return getResult();
	}

	private void setElContext(int rowIndex, Map<String, Object> masterDataMap) {
		for (int i = 0; i < emMocData.length; i++) {//for emMoc array
			String tableName = emMocInfo[i].getMocName();  //moc name
			Set<String> fieldNameSet = emMocData[i].getFieldNameAndIndex().keySet();
			String[] fieldNames = fieldNameSet.toArray(new String[0]);  //get field array
			if (i == 0) { //master moc
				setMasterMocContext(emMocData[i].getRowData(rowIndex), masterDataMap, tableName, fieldNames);
			} else {    //aux moc
				setAuxMocContext(masterDataMap, i, tableName, fieldNames);
			}
		}
	}

	private void setMasterMocContext(Object[] rowData, Map<String, Object> masterDataMap, String tableName, String[] fieldNames) {
		//master moc属性支持fieldName直接访问，和tableName.FieldName的访问方式
		Object[] masterRow = rowData;//master row factory
		for (int j = 0; j < masterRow.length; j++) {  //for field
			masterDataMap.put(fieldNames[j], masterRow[j]);
			this.elProc.defineBean(fieldNames[j], masterRow[j]);
		}
		this.elProc.defineBean(tableName, masterDataMap);
	}


	/**
	 * @return 获得行数据
	 */
	private Object[] getResult() {
		Object[] result = new Object[nrmMocInfo.getFieldMapping().length];
		int i = 0;
		for (String elExpr : nrmMocInfo.getFieldMapping()) {
			//如果是多版本
			if (elExpr.startsWith(NrmConstant.MULTI_VER_FUNC)) {
				String nrmMoc = nrmMocInfo.getMocName();
				String nrmAttr = nrmMocInfo.getMocFields()[i];
				String multiVerField = elExpr.substring(elExpr.indexOf(',') + 1);
				String emVerValue = (String) this.elProc.getValue(multiVerField, String.class);
				String mapName = nrmMoc + "_" + nrmAttr + "_multiVerMap";
				String currFunc = getCurrFunc(emVerValue, mapName);
				if (currFunc != null) {
					result[i] = this.elProc.eval(currFunc);
				} else {
					result[i] = "";
				}
			} else {
				//通过el获取结果
				result[i] = this.elProc.eval(elExpr);
			}
			i++;
		}
		return result;
	}

	private String getCurrFunc(String emVerValue, String mapName) {
		Map<String, String> multiFunc = this.initBeanMap.get(mapName);
		return null != multiFunc ? multiFunc.get(emVerValue) : null;
	}

	private void setAuxMocContext(Map<String, Object> masterDataMap, int i, String tableName, String[] fieldNames) {
		Map<String, Object> auxDataMap = new HashMap<>();
		DataSetKey[] keys = emMocInfo[i].getKeys();
		if (isValidKeys(keys)) {  //if no key, write nothing to elproc.
			DataSetKey key = keys[0];
			String[] refFields = key.getRefField();
			String refMasterFieldsValue = getRefMasterFieldsValue(masterDataMap, key, refFields);
			if (key.getKeyType() == DataSetKey.PK) {
				dealAuxDataByPk(emMocData[i].getRowByPk(refMasterFieldsValue), fieldNames, auxDataMap);
			} else { //MK
				dealAuxDataByMk(emMocData[i], fieldNames, auxDataMap, refMasterFieldsValue);
			}
			this.elProc.defineBean(tableName, auxDataMap);
		}
	}

	private void dealAuxDataByMk(TableDataSet emMocData, String[] fieldNames, Map<String, Object> auxDataMap, String refMasterFieldsValue) {
		//mk对应数据如果是数组（包含分号），则截断，每个值取辅表中定位记录。非数组，直接匹配。pk不支持数组
		String[] arrVal = refMasterFieldsValue.split(";");
		List<List<Object>> result = new ArrayList<>();
		for (String arV : arrVal) {
			List<List<Object>> temR = emMocData.getRowByMk1(arV);//only support mk1
			if (temR != null) {
				setData2EmMocData(result, temR);
			}
		}
		setAuxMocData(fieldNames, auxDataMap, result);
	}

	private void setData2EmMocData(List<List<Object>> result, List<List<Object>> temR) {
		//初次赋值，后续添加
		if (result.isEmpty()) {
			result.addAll(temR);
		} else {
			for (int j = 0; j < result.size(); j++) {
				result.get(j).addAll(temR.get(j));
			}
		}
	}

	private void setAuxMocData(String[] fieldNames, Map<String, Object> auxDataMap, List<List<Object>> result) {
		if (result.isEmpty()) {
			for (int i = 0; i < fieldNames.length; i++) {
				auxDataMap.put(fieldNames[i], new ArrayList<Object>());
			}
		} else {
			for (int i = 0; i < fieldNames.length; i++) {
				auxDataMap.put(fieldNames[i], result.get(i));  //if some ele is null, handle by el
			}
		}
	}

	private void dealAuxDataByPk(List<Object> rowByPk, String[] fieldNames, Map<String, Object> auxDataMap) {
		List<Object> result = rowByPk;
		if (result.isEmpty()) {
			for (int kk = 0; kk < fieldNames.length; kk++) {
				auxDataMap.put(fieldNames[kk], "");
			}
		} else {
			for (int kk = 0; kk < fieldNames.length; kk++) {
				Object r = result.get(kk) == null ? "" : result.get(kk);
				auxDataMap.put(fieldNames[kk], r);
			}
		}
	}

	private String getRefMasterFieldsValue(Map<String, Object> masterDataMap, DataSetKey key, String[] refFields) {
		StringBuilder sb = new StringBuilder();
		//引用主moc的值，可能是组合字段
		for (int k = 0; k < refFields.length; k++) {
			String value = String.valueOf(masterDataMap.get(refFields[k]));
			if (value != null) {
				sb.append(value);
			}
		}//get key values
		String refMasterMocValue = sb.toString();
		if (key.getRefKeyMethod() != null) {
			try {
				refMasterMocValue = (String) (key.getRefKeyMethod().invoke(null, refMasterMocValue, key.getRefKeyMethodPara()));
			} catch (Exception e) {
				log.info(e.getMessage(), e);
			}
		}
		return refMasterMocValue;
	}

	private boolean isValidKeys(DataSetKey[] keys) {
		boolean isValid = false;
		if (null != keys && keys.length > 0) {
			DataSetKey key = keys[0];  //only write factory using keys0
			if (key.getRefField() != null) {  //if no refField, write nothing to elproc. but it has key.
				isValid = true;
			}
		}
		return isValid;
	}

	public Iterator<List<String[]>> iteratorExpand() {
		return new ItrExpand();
	}

	/**
	 * 使用迭代器获取输出结果，一次迭代输出一行数据
	 */
	public Iterator<String[]> iterator() {
		return new Itr();
	}

	/**
	 * 迭代器实现
	 *
	 * @author 10015496
	 */
	private class Itr implements Iterator<String[]> {
		int cursor = 0;

		@Override
		public boolean hasNext() {
			return cursor < emMocData[0].getRowSize();
		}

		@Override
		public String[] next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			String[] ret = null;
			Object[] nrmField = mappingNrmAttr(cursor);
			cursor++;
			if (nrmField != null) {
				ret = new String[nrmField.length];
				for (int i = 0; i < nrmField.length; i++) {
					ret[i] = nrmField[i] == null ? "" : nrmField[i].toString();
				}
			}
			return ret;
		}
	}

	/**
	 * 扩展迭代器实现
	 *
	 * @author 10015496
	 */
	private class ItrExpand implements Iterator<List<String[]>> {
		int cursor = 0;

		@Override
		public boolean hasNext() {
			return cursor < emMocData[0].getRowSize();
		}

		@Override
		public List<String[]> next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			List<String[]> nrmField = mappingExpandNrmAttr(cursor);
			cursor++;
			return nrmField;
		}

		private List<String[]> mappingExpandNrmAttr(int rowIndex) {
			ArrayList<String[]> retArr = new ArrayList<>();
			Object[] ret = mappingNrmAttr(rowIndex);
			if (ret != null) {
				if (expandFlag) {
					expandRec(retArr, ret);
				} else {
					String[] strRet = new String[ret.length];
					for (int i = 0; i < ret.length; i++) {
						strRet[i] = (String) ret[i];
					}
					retArr.add(strRet);
				}
			}
			return retArr;
		}

		private void expandRec(List<String[]> retArr, Object[] rec) {
			List<Object> v = (List<Object>) rec[expandFieldsPos.get(0)];
			int size = v.size();
			for (int i = 0; i < size; i++) {
				String[] oneRow = new String[rec.length];
				for (int j = 0; j < rec.length; j++) {
					if (expandFieldsPos.contains(j)) {
						oneRow[j] = ((List<Object>) rec[j]).get(i).toString();
					} else {
						oneRow[j] = rec[j].toString();
					}
				}
				retArr.add(oneRow);
			}
		}

	}

}
