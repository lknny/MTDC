package nrm.factory;

import nrm.common.NrmConstant;
import nrm.pojo.BaseMocInfo;
import nrm.pojo.DataSetKey;
import nrm.pojo.NrmModel;
import nrm.pojo.NrmRule;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by ${lknny@163.com} on 2017/8/6.
 */
public class NrmModelFactory {
	private static final Logger log = LoggerFactory.getLogger(NrmModelFactory.class);

	private NrmModelFactory() {
	}

	public static NrmModel getNrmModel(String nrmMappingFile) {
		NrmModel nrmModel = new NrmModel();
		Map<String, NrmRule> rules = new HashMap<>();
		//生成映射规则对象
		SAXBuilder builder = new SAXBuilder();
		Document doc = getNrmModelDocument(nrmMappingFile, builder);
		if (null != doc) {
			Element rootEle = doc.getRootElement();
			Element fileHeadEle = rootEle.getChild("FileHead");
			Element i18nEle = rootEle.getChild("i18n");
			setCommonInfo(nrmModel, fileHeadEle);
			setI18nInfo(nrmModel, i18nEle);

			String importPath = nrmModel.getImportInfo();
			Element moTreeEle = rootEle.getChild("moTree");
			List<Element> mocEleList = moTreeEle.getChildren("moc");
			for (Element moc : mocEleList) {   //pooling moc, everyMoc create a rule,add the rule to rules
				List<BaseMocInfo> emMocList = new ArrayList<>(); //rec BaseMocInfo list
				Map<String, Set<String>> emMoc = new LinkedHashMap<>(); //rec all emMoc's fields
				Map<String, List<DataSetKey>> emMocKey = new LinkedHashMap<>(); //rec emMoc's keys
				Map<String, Map<String, String>> initBeanMap = new HashMap<>();  //放enum的枚举定义，多版本的func
				List<Element> emMocEleList = moc.getChildren("emMoc");
				String masterMocName = analyzeEmMocs(importPath, emMoc, emMocKey, emMocEleList);
				String nrmMoc = moc.getAttributeValue("nrm");
				String isStatistic = moc.getAttributeValue("statistic");
				List<String> attrs = new ArrayList<>(); //rec nrm attrs
				List<String> funcs = new ArrayList<>(); //rec nrm funcs
				List<Element> nrmAttrList = moc.getChildren("attr");

				analyzeNrmMoc(emMoc, masterMocName, initBeanMap, nrmMoc, attrs, funcs, nrmAttrList);
				put2Set(emMoc, masterMocName, "managedElementType");
				put2Set(emMoc, masterMocName, "managedElement");

				//construct nrmMocInfo
				BaseMocInfo nrmMocInfo = new BaseMocInfo(nrmMoc, attrs.toArray(new String[0]), funcs.toArray(new String[0]));
				dealGroupAndCalFieldsIndex(nrmAttrList, nrmMocInfo);

				int i = -1;
				for (Map.Entry<String, Set<String>> emMocElement : emMoc.entrySet()) {
					i++;
					String emMocName = emMocElement.getKey();
					//String mocName, String[] mocFields, DataSetKey[] keys, boolean isMaster
					Set<String> fieldInfo = emMocElement.getValue();
					List<DataSetKey> dskList = getDataSetKeys(emMocKey, emMocName);
					add2EmMocList(emMocList, i, emMocName, fieldInfo, dskList);
				}

				NrmRule nrmRule = new NrmRule();
				nrmRule.setNrmMocInfo(nrmMocInfo);
				nrmRule.setEmMocInfo(emMocList.toArray(new BaseMocInfo[0]));
				nrmRule.setBeanMap(initBeanMap);
				setStatistic(isStatistic, nrmRule);

				String filterStr = moc.getAttributeValue("filter");
				String expandStr = moc.getAttributeValue("expand");

				nrmRule.setFilterStr(filterStr);
				setExpand(nrmMocInfo, nrmRule, expandStr);
				rules.put(nrmRule.getNrmMocInfo().getMocName(), nrmRule);

			} //pooling moc, everyMoc create a rule,add the rule to rules
			nrmModel.setRules(rules);
		}
		return nrmModel;
	}

	private static void setExpand(BaseMocInfo nrmMocInfo, NrmRule nrmRule, String expandStr) {
		if (expandStr != null && null != nrmMocInfo.getMocFields()) {
			List<Integer> pos = dealExpand(nrmMocInfo, expandStr);
			nrmRule.setExpandFieldsPos(pos);
		}
	}

	private static void add2EmMocList(List<BaseMocInfo> emMocList, int i, String emMocName, Set<String> fieldInfo, List<DataSetKey> dskList) {
		if (null != dskList) {
			BaseMocInfo emMocc = new BaseMocInfo(emMocName, fieldInfo.toArray(new String[0]), dskList.toArray(new DataSetKey[0]), i == 0);
			emMocList.add(emMocc);
		}
	}

	private static List<DataSetKey> getDataSetKeys(Map<String, List<DataSetKey>> emMocKey, String emMocName) {
		return null == emMocKey.get(emMocName) ? null : emMocKey.get(emMocName);
	}

	private static Document getNrmModelDocument(String nrmMappingFile, SAXBuilder builder) {
		Document doc = null;
		try {
			doc = builder.build(new java.io.File(nrmMappingFile));
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}
		return doc;
	}

	private static void setStatistic(String isStatistic, NrmRule nrmRule) {
		if ("1".equals(isStatistic)) {
			nrmRule.setStatistic(true);
		}
	}

	/**
	 * 设置分组字段游标
	 *
	 * @param nrmAttrList
	 * @param nrmMocInfo
	 */
	private static void dealGroupAndCalFieldsIndex(List<Element> nrmAttrList, BaseMocInfo nrmMocInfo) {
		int i = 0;
		List<Integer> groupFieldsIndex = new LinkedList<>();
		List<Integer> calFieldsIndex = new LinkedList<>();
		for (Element element : nrmAttrList) {
			String groupFlag = element.getAttributeValue("group");
			String calFlag = element.getAttributeValue("cal");
			if ("true".equals(groupFlag)) {
				groupFieldsIndex.add(i);
			} else if ("true".equals(calFlag)) {
				calFieldsIndex.add(i);
			}
			++i;
		}
		setGroupAndCalFieldsIndex(nrmMocInfo, groupFieldsIndex, calFieldsIndex);
	}

	private static void setGroupAndCalFieldsIndex(BaseMocInfo nrmMocInfo, List<Integer> groupFieldsIndex, List<Integer> calFieldsIndex) {
		if (!groupFieldsIndex.isEmpty()) {
			nrmMocInfo.setGroupFieldsIndex(groupFieldsIndex);
		}
		if (!calFieldsIndex.isEmpty()) {
			nrmMocInfo.setCalFieldsIndex(calFieldsIndex);
		}
	}

	private static List<Integer> dealExpand(BaseMocInfo nrmMocInfo, String expandStr) {
		List<Integer> pos = new ArrayList<>();
		String[] expandArr = expandStr.split(",");
		for (int j = 0; j < expandArr.length; j++) {
			add2Pos(nrmMocInfo, pos, expandArr[j]);
		}
		return pos;
	}

	private static void add2Pos(BaseMocInfo nrmMocInfo, List<Integer> pos, String s) {
		String[] mocFields = nrmMocInfo.getMocFields();
		for (int k = 0; k < mocFields.length; k++) {
			if (s.equals(mocFields[k])) {
				pos.add(k);
				break;
			}
		}
	}

	private static void analyzeNrmMoc(Map<String, Set<String>> emMoc, String masterMocName, Map<String, Map<String, String>> initBeanMap, String nrmMoc, List<String> attrs, List<String> funcs, List<Element> nrmAttrList) {
		for (Element attrEle : nrmAttrList) {  //pooling attr
			String nrmAttr = attrEle.getAttributeValue("nrm");
			attrs.add(nrmAttr);
			String func = attrEle.getAttributeValue("func");
			if (func == null) {  //one to one mapping
				func = attrEle.getAttributeValue("em");
			} else {
				func = dealFuncs(initBeanMap, nrmMoc, attrEle, nrmAttr, func);
			}
			funcs.add(func);
			String emXmlAttr = attrEle.getAttributeValue("em"); //gather emMoc fields
			if (emXmlAttr != null) {
				gatherEmMocFields(emMoc, masterMocName, emXmlAttr);
			}
		}
	}

	private static void gatherEmMocFields(Map<String, Set<String>> emMoc, String masterMocName, String emXmlAttr) {
		String[] emArrs = emXmlAttr.split(",");
		for (String oneArr : emArrs) {
			String mocName;
			String fieldName;
			int dot = oneArr.indexOf('.');   //judge master moc or aux moc
			if (dot == -1) { //master moc
				mocName = masterMocName;  //
				fieldName = oneArr;
			} else {
				mocName = oneArr.substring(0, dot);
				fieldName = oneArr.substring(dot + 1);
			}
			put2Set(emMoc, mocName, fieldName);
		}
	}

	/**
	 * 处理函数
	 */
	private static String dealFuncs(Map<String, Map<String, String>> initBeanMap, String nrmMoc, Element attrEle, String nrmAttr, String func) {
		String newFunc = "";
		if (func.startsWith("$")) {
			newFunc = func.substring(2, func.lastIndexOf('}'));
		} else if ("enum".equals(func)) {
			String emFieldName = attrEle.getAttributeValue("em");
			String mapName = nrmMoc + "_" + nrmAttr + "_enumMap"; //read enmu's case, take it to a map
			initBeanMap.put(mapName, getEnumMap(attrEle));
			newFunc = "naf:enumHandle(" + mapName + ", " + emFieldName + ")";
		} else if ("multiVer".equals(func)) {
			List<Element> multiVerEleList = attrEle.getChildren("multiVer");
			Map<String, String> multiVerMap = new HashMap<>();
			String mapName = nrmMoc + "_" + nrmAttr + "_multiVerMap";
			String emVerAttr = attrEle.getAttributeValue("emVerAttr"); //this attr must set in el context
			for (Element emModelEle : multiVerEleList) {
				String emVerValue = emModelEle.getAttributeValue("emVerValue");
				String tempFunc = emModelEle.getAttributeValue("func");
				tempFunc = tempFunc.substring(2, tempFunc.lastIndexOf('}'));
				multiVerMap.put(emVerValue, tempFunc);
			}
			initBeanMap.put(mapName, multiVerMap);
			newFunc = NrmConstant.MULTI_VER_FUNC + "," + emVerAttr; //put the VerAttr to func String
		}
		return newFunc;
	}

	private static Map<String, String> getEnumMap(Element attrEle) {
		List<Element> enumEleList = attrEle.getChildren("enum");
		Map<String, String> enumMap = new HashMap<>();
		for (Element enumEle : enumEleList) {
			enumMap.put(enumEle.getAttributeValue("em"), enumEle.getAttributeValue("nrm"));
		}
		return enumMap;
	}

	private static String analyzeEmMocs(String importPath, Map<String, Set<String>> emMoc, Map<String, List<DataSetKey>> emMocKey, List<Element> emMocEleList) {
		String masterMocName = "";
		for (int i = 0; i < emMocEleList.size(); i++) {  //pooling emMoc, the first one is master moc
			String mocN = emMocEleList.get(i).getAttributeValue("em");
			Set<String> oneMocFields;
			if (i == 0) { //master moc
				masterMocName = mocN;
			}
			oneMocFields = emMoc.get(mocN);
			if (oneMocFields == null) { //curr moc is not set
				emMoc.put(mocN, new HashSet<String>());
			}
			List<Element> keyEleList = emMocEleList.get(i).getChildren("key");
			List<DataSetKey> dskList = new ArrayList<>(); //rec DataSetKey
			dealKeys(importPath, emMoc, masterMocName, mocN, keyEleList, dskList);
			emMocKey.put(mocN, dskList);
		} //pooling emMoc, the first one is master moc
		return masterMocName;
	}

	private static void dealKeys(String importPath, Map<String, Set<String>> emMoc, String masterMocName, String mocN, List<Element> keyEleList, List<DataSetKey> dskList) {
		for (Element key : keyEleList) {   //pooling key
			String pk = key.getAttributeValue("pk");
			String mk1 = key.getAttributeValue("mk1");
			String mk2 = key.getAttributeValue("mk2");
			if (pk != null) { //curr is pk
				dealPk(importPath, emMoc, masterMocName, mocN, dskList, key, pk);
			} else if (mk1 != null) {
				dealMk1(importPath, emMoc, masterMocName, mocN, dskList, key, mk1);
			} else if (mk2 != null) {
				dealMk2(importPath, emMoc, masterMocName, mocN, dskList, key, mk2);
			}
		}
	}

	private static void dealMk2(String importPath, Map<String, Set<String>> emMoc, String masterMocName, String mocN, List<DataSetKey> dskList, Element key, String mk2) {
		String refKey = key.getAttributeValue("mk2Ref");
		String mk2RefFunc = key.getAttributeValue("mk2RefFunc");
		String mk2RefFuncPara = key.getAttributeValue("mk2RefFuncPara");
		String mk2Func = key.getAttributeValue("mk2Func");
		String mk2FuncPara = key.getAttributeValue("mk2FuncPara");
		String[] keyFields = mk2.split(",");
		for (String f : keyFields) {
			put2Set(emMoc, mocN, f);
		}
		String[] masterKeyFields = null;
		if (refKey != null) {
			masterKeyFields = refKey.split(",");
			for (String f : masterKeyFields) {
				put2Set(emMoc, masterMocName, f);
			}
		}
		DataSetKey dsk = setDataSetKey(DataSetKey.MK2, importPath, keyFields, masterKeyFields, mk2RefFunc, mk2RefFuncPara, mk2Func, mk2FuncPara);
		dskList.add(dsk);
	}

	private static void dealMk1(String importPath, Map<String, Set<String>> emMoc, String masterMocName, String mocN, List<DataSetKey> dskList, Element key, String mk1) {
		String refKey = key.getAttributeValue("mk1Ref");
		String mk1RefFunc = key.getAttributeValue("mk1RefFunc");
		String mk1RefFuncPara = key.getAttributeValue("mk1RefFuncPara");
		String mk1Func = key.getAttributeValue("mk1Func");
		String mk1FuncPara = key.getAttributeValue("mk1FuncPara");
		String[] keyFields = mk1.split(",");
		for (String f : keyFields) {
			put2Set(emMoc, mocN, f);
		}
		String[] masterKeyFields = null;
		if (refKey != null) {
			masterKeyFields = refKey.split(",");
			for (String f : masterKeyFields) {
				put2Set(emMoc, masterMocName, f);
			}
		}
		DataSetKey dsk = setDataSetKey(DataSetKey.MK1, importPath, keyFields, masterKeyFields, mk1RefFunc, mk1RefFuncPara, mk1Func, mk1FuncPara);
		dskList.add(dsk);
	}

	private static void put2Set(Map<String, Set<String>> emMoc, String key, String value) {
		Set<String> set = emMoc.get(key);
		if (null != set) {
			set.add(value);
		}
	}

	private static void dealPk(String importPath, Map<String, Set<String>> emMoc, String masterMocName, String mocN, List<DataSetKey> dskList, Element key, String pk) {
		String refKey = key.getAttributeValue("pkRef");  //allow null
		String pkRefFunc = key.getAttributeValue("pkRefFunc");
		String pkRefFuncPara = key.getAttributeValue("pkRefFuncPara");
		String pkFunc = key.getAttributeValue("pkFunc");
		String pkFuncPara = key.getAttributeValue("pkFuncPara");

		String[] keyFields = pk.split(",");
		for (String f : keyFields) {
			put2Set(emMoc, mocN, f);
		}
		String[] masterKeyFields = null;
		if (refKey != null) {
			masterKeyFields = refKey.split(",");
			for (String f : masterKeyFields) {
				put2Set(emMoc, masterMocName, f);
			}
		}
		DataSetKey dsk = setDataSetKey(DataSetKey.PK, importPath, keyFields, masterKeyFields, pkRefFunc, pkRefFuncPara, pkFunc, pkFuncPara);
		dskList.add(dsk);
	}

	/**
	 * 处理报表18n信息
	 *
	 * @param nrmModel
	 * @param i18nEle
	 */
	private static void setI18nInfo(NrmModel nrmModel, Element i18nEle) {
		if (null == i18nEle) {
			return;
		}
		Map<String, Map<String, String>> i18nMap = new LinkedHashMap<>();
		for (Element element : i18nEle.getChildren()) {
			Map<String, String> zhAndEnMap = new HashMap<>();
			String key = element.getAttribute("key").getValue();
			String zhValue = element.getAttribute("zh_CN").getValue();
			String enValue = element.getAttribute("en_US").getValue();
			zhAndEnMap.put(NrmConstant.ZH_CN, zhValue);
			zhAndEnMap.put(NrmConstant.EN_US, enValue);
			i18nMap.put(key, zhAndEnMap);
		}
		nrmModel.setI18nMap(i18nMap);
	}

	/**
	 * @param keyType
	 * @param importPath
	 * @param currKeyFields
	 * @param refKeyFields
	 * @param para[reffunc,refFuncPara,currFunc,currFuncPara]
	 * @return
	 */
	private static DataSetKey setDataSetKey(int keyType, String importPath, String[] currKeyFields, String[] refKeyFields, String... para) {
		DataSetKey dsk = new DataSetKey(keyType, currKeyFields, refKeyFields);
		if (para[0] != null) {
			Method keyMethod = getRefFuncMethod(importPath, para[0]);
			dsk.addRefFieldKey(keyMethod, para[1]);
		} else if (para[2] != null) {
			Method keyMethod = getRefFuncMethod(importPath, para[2]);
			dsk.addCurrFieldKey(keyMethod, para[3]);//TableDataSet will handle it.
		}
		return dsk;
	}

	private static Method getRefFuncMethod(String importPath, String refFunc) {
		Method method = null;
		try {
			String className = importPath + "." + refFunc.substring(0, refFunc.lastIndexOf('.'));
			String methodName = refFunc.substring(refFunc.lastIndexOf('.') + 1);
			Class<?> cc = Class.forName(className);
			method = cc.getMethod(methodName, new Class[]{String.class, String.class});
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}
		return method;
	}


	private static void setCommonInfo(NrmModel nrmModel, Element fileHeadElement) {
		nrmModel.setName(fileHeadElement.getChildText("name"));
		nrmModel.setNrmVersion(fileHeadElement.getChildText("nrmVersion"));
		nrmModel.setMimVersions(fileHeadElement.getChildText("mimVersionList"));
		nrmModel.setDescription(fileHeadElement.getChildText("description"));
		nrmModel.setManagedElementType(fileHeadElement.getChildText("managedElementType"));

		nrmModel.setType(fileHeadElement.getChildText("type"));
		nrmModel.setCreator(fileHeadElement.getChildText("creator"));
		nrmModel.setTimestamp(fileHeadElement.getChildText("timestamp"));
		nrmModel.setImportInfo(fileHeadElement.getChildText("import"));
		nrmModel.setDnPrefix(fileHeadElement.getChildText("dnPrefix"));
	}

}
