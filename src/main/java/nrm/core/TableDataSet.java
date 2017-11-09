package nrm.core;

import nrm.pojo.DataSetKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 提供一种数据结构,类似resultset，使用它来装载数据。实现一次性写入数据，后续快速读取的数据结构。
 * 对象中所有行号列号都是从0开始计数的。
 *
 * @author zdh
 */
public class TableDataSet {
	private static final Logger log = LoggerFactory.getLogger(TableDataSet.class);
	private List<List<Object>> rowSet = null; //行集合,含有列数据
	private int tableSize = 0;
	//表字段信息，key是字段名，value是字段位置
	private LinkedHashMap<String, Integer> fieldNameAndIndex = null;
	//TableDataSet attributes
	private Map<Object, List<Integer>> mkMap = null;//允许多值的索引，key是索引的字段值组合，value是记录号的列表（允许多记录）。
	private Map<Object, Integer> pkMap = null;
	private DataSetKey pkInfo = null;
	private DataSetKey mk1Info = null;
	private DataSetKey mk2Info = null;
	private String tableName;

	/**
	 * 构造函数
	 *
	 * @param fieldNames 字段数组
	 */
	public TableDataSet(String tableName, String[] fieldNames) {
		this.tableName = tableName;
		this.fieldNameAndIndex = new LinkedHashMap<>();
		int i = 0;
		for (String f : fieldNames) {
			this.fieldNameAndIndex.put(f, Integer.valueOf(i));
			i++;
		}
		this.rowSet = new ArrayList<>(); //行集合,含有列数据
		this.tableSize = 0;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setKey(DataSetKey dsk) {
		if (dsk.getKeyType() == DataSetKey.PK) {
			this.pkInfo = dsk;
			this.pkMap = new HashMap<>();
		} else if (dsk.getKeyType() == DataSetKey.MK1) {
			this.mk1Info = dsk;
			this.mkMap = new HashMap<>();
		} else if (dsk.getKeyType() == DataSetKey.MK2) {
			this.mk2Info = dsk;
			this.mkMap = new HashMap<>();
		}
	}

	/**
	 * 取得行数
	 *
	 * @return 行数
	 */
	public int getRowSize() {
		return this.tableSize;
	}

	/**
	 * 取得当前的列数
	 *
	 * @return 列数
	 */
	public int getColSize() {
		return this.fieldNameAndIndex.size();
	}

	/**
	 * 追加一行
	 *
	 * @param rowData 行数据
	 * @throws RuntimeException
	 */
	public void addRow(Object[] rowData) {
		if (rowData != null && rowData.length == this.getColSize()) {
			this.addRow(translateToArrayList(rowData));
		} else {
			log.info("Add row data to TableDataSet failed.");
		}
	}

	private static List<Object> translateToArrayList(Object[] objects) {
		return Arrays.asList(objects);
	}

	/**
	 * 追加一行
	 *
	 * @param rowData 行数据
	 * @throws RuntimeException
	 */
	private void addRow(List<Object> rowData) {
		this.rowSet.add(rowData);
		setPkIndex(rowData);
		setMkIndex(this.mk1Info, rowData);
		setMkIndex(this.mk2Info, rowData);
		this.tableSize++;
	}

	private void setPkIndex(List<Object> rowData) {
		//设置key；
		if (this.pkMap != null) {
			//所有主键字段值，进行组合，并保存rowData索引号
			String keyValue = getKeyValue(this.pkInfo, rowData);
			this.pkMap.put(keyValue, this.tableSize);
		}
	}

	private void setMkIndex(DataSetKey dsk, List<Object> rowData) {
		if (null != dsk) {
			String keyValue = getKeyValue(dsk, rowData);
			//设置mk数据索引
			List<Integer> r = this.mkMap.get(keyValue);
			if (r == null) {
				ArrayList<Integer> al = new ArrayList<>();
				al.add(this.tableSize);
				this.mkMap.put(keyValue, al);
			} else {
				r.add(this.tableSize);
			}
		}
	}

	private String getKeyValue(DataSetKey dsk, List<Object> rowData) {
		StringBuilder sb = new StringBuilder();
		for (String field : dsk.getCurrField()) {
			Integer index = this.fieldNameAndIndex.get(field);
			Object keyValue = null;
			if (null!=index){
				keyValue = rowData.get(index.intValue());
			}
			if (keyValue != null) {
				sb.append(keyValue.toString());
			}
		}
		return handleKeyValue(dsk, sb.toString());
	}

	/**
	 * @param currDsk
	 * @param keyValue
	 * @return 默认为keyValue，有func则进行处理
	 */
	private String handleKeyValue(DataSetKey currDsk, String keyValue) {
		String result = keyValue;
		if (currDsk.getCurrKeyMethod() != null) {
			try {
				result = (String) currDsk.getCurrKeyMethod().invoke(null, keyValue, currDsk.getCurrKeyMethodPara());
			} catch (Exception e) {
				log.info(e.getMessage(), e);
			}
		}
		return result;
	}

	/**
	 * 取得表头
	 *
	 * @return 表头
	 */
	public Map<String, Integer> getFieldNameAndIndex() {
		return this.fieldNameAndIndex;
	}

	/**
	 * 获取某一行数据
	 *
	 * @param rowIndex 行索引
	 * @return 行数据对应的数组
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public Object[] getRowData(int rowIndex) {
		return translateToObjArray(this.rowSet.get(rowIndex));
	}

	public Object[] translateToObjArray(List<Object> v) {
		return v.toArray();
	}

	/**
	 * 利用pk获取一条记录，如果无此记录，返回null。
	 *
	 * @param keyValues
	 * @return
	 */
	public List<Object> getRowByPk(Object... keyValues) {
		List<Object> rowData = new ArrayList<>();
		if (this.pkInfo != null) {
			String value = getUnionValue(keyValues);
			Integer key = this.pkMap.get(value);
			if (key != null) {
				rowData = this.rowSet.get(key.intValue());
			}
		}
		return rowData;
	}

	private String getUnionValue(Object[] keyValues) {
		StringBuilder sb = new StringBuilder();
		for (Object key : keyValues) {
			if (key != null) {
				sb.append(key.toString());
			}
		}
		return sb.toString();
	}

	/**
	 * 利用pk获取一个元素
	 *
	 * @param fieldName
	 * @param keyValues
	 * @return
	 */
	public Object getElementByPk(String fieldName, Object... keyValues) {
		List<Object> ret = this.getRowByPk(keyValues);
		Integer index = this.fieldNameAndIndex.get(fieldName);
		return null != index ? ret.get(index.intValue()) : null;
	}

	/**
	 * 利用mk1获取一个元素集，此元素可以包括多条记录的元素。如果没有记录，返回null。
	 * 返回一个list，每个元素是一个list，代表某一列数据中的元素
	 *
	 * @param mk
	 * @param keyValues
	 * @return
	 */
	private List<List<Object>> getRowByMk(Map<Object, List<Integer>> mk, Object... keyValues) {
		List<List<Object>> data = new ArrayList<>();
		if (mk != null) {
			String value = getUnionValue(keyValues);
			List<Integer> key = mk.get(value);  //rowId list, it may be null
			if (key != null) {
				setData4Mk(data, key);
			}
		}

		return data;
	}

	private void setData4Mk(List<List<Object>> data, List<Integer> key) {
		for (int i = 0; i < this.fieldNameAndIndex.size(); i++) {    //for field
			List<Object> colData = new ArrayList<>();
			for (Integer rowId : key) {
				colData.add(this.getOneElement(rowId.intValue(), i));
			}
			data.add(colData);
		}
	}


	/**
	 * 根据key获取数据，如果没有记录，会返回null
	 *
	 * @param keyValues 返回一个list，每个元素是一个list，代表某一列数据中的元素
	 * @return
	 */
	public List<List<Object>> getRowByMk1(Object... keyValues) {
		return this.getRowByMk(this.mkMap, keyValues);
	}

	/**
	 * 根据key获取数据，如果没有记录，不会返回null，但会返回一个size为0的List
	 *
	 * @param keyValues
	 * @return
	 */
	public List<List<Object>> getRowByMk2(Object... keyValues) {
		return this.getRowByMk(this.mkMap, keyValues);
	}

	/**
	 * 利用mk2获取一个元素集，此元素可以包括多条记录的元素。
	 * 如果没有记录，返回null
	 *
	 * @param fieldName
	 * @param keyValues
	 * @return
	 */
	public List<Object> getElementByMk2(String fieldName, Object... keyValues) {
		List<List<Object>> rowList = this.getRowByMk(this.mkMap, keyValues);
		if (rowList.isEmpty()) {
			return Collections.emptyList();
		}
		int index = getIndex(fieldName);
		return -1==index?null:rowList.get(index);
	}

	private int getIndex(String fieldName) {
		Integer index = this.fieldNameAndIndex.get(fieldName);
		return null!=index?index.intValue():-1;
	}

	/**
	 * 利用mk1获取一个元素集，此元素可以包括多条记录的元素。
	 * 如果没有记录，返回null
	 *
	 * @param fieldName
	 * @param keyValues
	 * @return
	 */
	public List<Object> getElementByMk1(String fieldName, Object... keyValues) {
		List<List<Object>> rowList = this.getRowByMk(this.mkMap, keyValues);
		if (rowList.isEmpty()) {
			return Collections.emptyList();
		}
		int index = getIndex(fieldName);
		return -1==index?null:rowList.get(index);
	}

	/**
	 * 根据行号列号获取元素值
	 *
	 * @param row 行号
	 * @param col 列号
	 * @return 元素值
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public Object getOneElement(int row, int col) {
		List<Object> tmp = this.rowSet.get(row);
		return tmp.get(col);
	}

	/**
	 * 根据行号和字段名获取属性
	 *
	 * @param row
	 * @param fieldName
	 * @return
	 */
	public Object getOneElement(int row, String fieldName) {
		Integer index = this.fieldNameAndIndex.get(fieldName);
		return null != index ? this.getOneElement(row, index.intValue()) : null;
	}

	/**
	 * 打印该对象的记录信息，当没有记录时候，返回"null"。
	 *
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		int size = this.getRowSize();
		if (size == 0) {
			buffer.append("null"); //only for display
		} else {
			Set<String> fieldNameSet = this.fieldNameAndIndex.keySet();
			Object[] fieldNames = fieldNameSet.toArray();
			buffer.append("{");
			for (int j = 0; j < size; j++) {
				buffer.append("(");
				Object[] v = this.getRowData(j);
				appendValues(buffer, size, fieldNames, j, v);
			}
			buffer.append("}");
		}
		return buffer.toString();
	}

	private void appendValues(StringBuilder buffer, int size, Object[] fieldNames, int j, Object[] v) {
		for (int k = 0; k < v.length; k++) {
			buffer.append(fieldNames[k]);
			buffer.append("=");
			if (v[k] == null) {
				buffer.append("null");
			} else {
				buffer.append(v[k].toString());
			}
			if (k != v.length - 1) {
				buffer.append(",");
			}
		}
		buffer.append(")");
		if (j != size - 1) {
			buffer.append(",\n");
		}
	}
}