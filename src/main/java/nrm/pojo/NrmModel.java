package nrm.pojo;

import java.util.Map;

/**
 * 配置报表模型，每个XMl文件对应一个配置报表模型
 *
 * Created by ${lknny@163.com} on 2017/8/7.
 */
public class NrmModel {
	/*
	* common info
	* */
	private String key;
	private String name;
	private String nrmVersion;
	private String mimType;
	private String mimVersions;
	private String description;
	private String type;
	private String creator;
	private String timestamp;
	private String importInfo;
	private String dnPrefix;
	private String managedElementType;
	//first key is i18n key,second key is zh or en
	private Map<String, Map<String, String>> i18nMap;
	/*
	* nrm moc名称和对应rule
	* */
	private Map<String,NrmRule> rules;
	public String getManagedElementType() {
		return managedElementType;
	}

	public void setManagedElementType(String managedElementType) {
		this.managedElementType = managedElementType;
	}

	public Map<String, NrmRule> getRules() {
		return rules;
	}

	public void setRules(Map<String, NrmRule> rules) {
		this.rules = rules;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNrmVersion() {
		return nrmVersion;
	}

	public void setNrmVersion(String nrmVersion) {
		this.nrmVersion = nrmVersion;
	}

	public String getMimType() {
		return mimType;
	}

	public void setMimType(String mimType) {
		this.mimType = mimType;
	}

	public String getMimVersions() {
		return mimVersions;
	}

	public void setMimVersions(String mimVersions) {
		this.mimVersions = mimVersions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getImportInfo() {
		return importInfo;
	}

	public void setImportInfo(String importInfo) {
		this.importInfo = importInfo;
	}

	public String getDnPrefix() {
		return dnPrefix;
	}

	public void setDnPrefix(String dnPrefix) {
		this.dnPrefix = dnPrefix;
	}

	public Map<String, Map<String, String>> getI18nMap() {
		return i18nMap;
	}

	public void setI18nMap(Map<String, Map<String, String>> i18nMap) {
		this.i18nMap = i18nMap;
	}
}
