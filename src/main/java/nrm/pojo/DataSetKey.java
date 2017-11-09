package nrm.pojo;

import java.lang.reflect.Method;

/**
 * 数据集的key信息，它用在2个地方，一个是TableDataSet，用于记录key的数据。
 * 一个是BaseMocInfo，记录模型映射文件中的key信息。有些属性是共用的，有些属性是各自使用的
 *
 * @author 10015496
 */
public class DataSetKey {
	public static final int PK = 0;
	public static final int MK1 = 1;
	public static final int MK2 = 2;

	//common attributes
	private int keyType;

	private String[] currField;
	private Method currKeyMethod = null;  //对FieldArr组成的索引数据调用一个方法，其中索引数据是第一个参数
	private String currKeyMethodPara = null;  //这个是第2个参数


	//BaseMocInfo attributes
	private String[] refField;
	private Method refKeyMethod = null;  //对FieldArr组成的索引数据调用一个方法，其中索引数据是第一个参数
	private String refKeyMethodPara = null;  //这个是第2个参数

	/**
	 * 用于初始DataSetKey
	 *
	 * @param keyType
	 * @param currField
	 * @param refField
	 */
	public DataSetKey(int keyType, String[] currField, String[] refField) {
		if (keyType == DataSetKey.PK) {
			initKeyFields(keyType, currField, refField);
		} else if (keyType == DataSetKey.MK1 || keyType == DataSetKey.MK2) {
			initKeyFields(keyType, currField, refField);
		}

	}

	private void initKeyFields(int keyType, String[] currField, String[] refField) {
		this.keyType = keyType;
		if (null!=currField){
			this.currField = currField.clone();
		}
		if (null != refField) {
			this.refField = refField.clone();
		}
	}

	/**
	 * 用于初始化key的函数方法，BaseMocInfo和TableDataSet中都会用到
	 *
	 * @param MethodActPos
	 * @param keyMethod
	 * @param keyMethodPara
	 */
	public void addCurrFieldKey(Method keyMethod, String keyMethodPara) {
		this.currKeyMethod = keyMethod;
		this.currKeyMethodPara = keyMethodPara;
	}

	public void addRefFieldKey(Method keyMethod, String keyMethodPara) {
		this.refKeyMethod = keyMethod;
		this.refKeyMethodPara = keyMethodPara;
	}

	public int getKeyType() {
		return keyType;
	}

	public Method getCurrKeyMethod() {
		return currKeyMethod;
	}

	public String getCurrKeyMethodPara() {
		return currKeyMethodPara;
	}

	public Method getRefKeyMethod() {
		return refKeyMethod;
	}

	public String getRefKeyMethodPara() {
		return refKeyMethodPara;
	}

	public String[] getCurrField() {
		return currField.clone();
	}

	public String[] getRefField() {
		return refField.clone();
	}

}
