package nrm.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述mo的moc信息或NRM moc的信息及其属性信息
 * @author 10015496
 */
public class BaseMocInfo {
	/**
	 * moc名字
	 */
	private String mocName = null;
	/**
	 * 属性集
	 */
	private String[] mocFields = null;
	/**
	 * 如果是nrm moc，那么每个属性有对应的映射关系
	 */
	private String[] fieldMapping = null;
	/**
	 * mo的moc的索引信息
	 * 特别注意，模型初始化后，在进行添加数据时，DataSetKey会缓存索引信息，注意清理。
	 *
	 */
	private DataSetKey[] keys = null;
	/**
	 * 是否是主表
	 */
	private boolean isMaster = false;

	/*
	* 分组字段游标
	* */
	private List<Integer> groupFieldsIndex;

	/*
		* 计算字段游标
		* */
	private List<Integer> calFieldsIndex;


	/**
	 * nrm moc的构造函数
	 * @param mocName
	 * @param mocFields
	 * @param fieldMapping
	 */
	public BaseMocInfo(String mocName, String[] mocFields, String[] fieldMapping){
		this(mocName, mocFields, fieldMapping, null, false);
	}

	/**
	 * mo moc的构造函数
	 * @param mocName
	 * @param mocFields
	 * @param keys
	 * @param isMaster
	 */
	public BaseMocInfo(String mocName, String[] mocFields, DataSetKey[] keys, boolean isMaster){
		this(mocName, mocFields, null, keys, isMaster);
	}


	/**
	 * 全参数构造函数，一般不用
	 * @param mocName
	 * @param mocFields
	 * @param fieldMapping
	 * @param keys
	 * @param isMaster
	 */
	public BaseMocInfo(String mocName, String[] mocFields, String[] fieldMapping, DataSetKey[] keys, boolean isMaster){
		this.mocName = mocName;
		if (null!=mocFields){
			this.mocFields = mocFields.clone();
		}
		if (null != fieldMapping) {
			this.fieldMapping = fieldMapping.clone();
		}
		if (null != keys) {
			this.keys = keys.clone();
		}
		this.isMaster = isMaster;
	}


	/**
	 * 获取映射信息
	 * @return
	 */
	public String[] getFieldMapping() {
		return fieldMapping.clone();
	}
	/**
	 * 获取moc名称信息
	 * @return
	 */
	public String getMocName() {
		return this.mocName;
	}
	/**
	 * 获取属性信息
	 * @return
	 */
	public String[] getMocFields() {
		if (null!=this.mocFields){
			return this.mocFields.clone();
		}
		return new String[0];
	}
	/**
	 * 获取属性信息
	 * @return
	 */
	public DataSetKey[] getKeys() {
		return this.keys.clone();
	}
	/**
	 * 是否主表
	 * @return
	 */
	public boolean isMaster() {
		return this.isMaster;
	}


	public List<Integer> getGroupFieldsIndex() {
		return groupFieldsIndex;
	}

	public void setGroupFieldsIndex(List<Integer> groupFieldsIndex) {
		this.groupFieldsIndex = new ArrayList<>();
		this.groupFieldsIndex.addAll(groupFieldsIndex);
	}

	public List<Integer> getCalFieldsIndex() {
		return calFieldsIndex;
	}

	public void setCalFieldsIndex(List<Integer> calFieldsIndex) {
		this.calFieldsIndex =  new ArrayList<>();
		this.calFieldsIndex.addAll(calFieldsIndex);
	}
}
