package nrm.pojo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 配置报表规则，每个xml文件中每个moc节点对应一个NrmRule
 *
 * Created by ${lknny@163.com} on 2017/8/6.
 */
public class NrmRule {

	/*
	* rule info
	* */
	private BaseMocInfo[] emMocInfo = null;
	private BaseMocInfo nrmMocInfo = null;
	private Map<String, Map<String, String>> beanMap;
	private Map<String, Method> extMethod;
	private Map<String, Object> extBean;
	private boolean statistic=false;
	private boolean expand = false;
	private List<Integer> expandFieldsPos = null;
	private String filterStr = null;

	public boolean isStatistic() {
		return statistic;
	}

	public void setStatistic(boolean statistic) {
		this.statistic = statistic;
	}

	public boolean isExpand() {
		return expand;
	}

	public void setExpand(boolean expand) {
		this.expand = expand;
	}

	public List<Integer> getExpandFieldsPos() {
		return expandFieldsPos;
	}

	public void setExpandFieldsPos(List<Integer> expandFieldsPos) {
		if (!expandFieldsPos.isEmpty()){
			this.expandFieldsPos = expandFieldsPos;
			setExpand(true);
		}
	}

	public String getFilterStr() {
		return filterStr;
	}

	public void setFilterStr(String filterStr) {
		this.filterStr = filterStr;
	}
	public BaseMocInfo[] getEmMocInfo() {
		return emMocInfo.clone();
	}

	public void setEmMocInfo(BaseMocInfo[] emMocInfo) {
		this.emMocInfo = emMocInfo.clone();
	}

	public BaseMocInfo getNrmMocInfo() {
		return nrmMocInfo;
	}

	public void setNrmMocInfo(BaseMocInfo nrmMocInfo) {
		this.nrmMocInfo = nrmMocInfo;
	}

	public Map<String, Map<String, String>> getBeanMap() {
		return beanMap;
	}

	public void setBeanMap(Map<String, Map<String, String>> beanMap) {
		this.beanMap = beanMap;
	}

	public Map<String, Method> getExtMethod() {
		return extMethod;
	}

	public void setExtMethod(Map<String, Method> extMethod) {
		this.extMethod = extMethod;
	}

	public Map<String, Object> getExtBean() {
		return extBean;
	}

	public void setExtBean(Map<String, Object> extBean) {
		this.extBean = extBean;
	}
}
