package nrm;

import nrm.core.MappingNrmRule;
import nrm.core.StatisticHandler;
import nrm.factory.TableDataSetFactory;
import nrm.model.NrmModelManager;
import nrm.pojo.NrmModel;
import nrm.pojo.NrmRule;
import nrm.pojo.RepDataSet;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${10190990} on 2017/8/6.
 */
@Service
public class RepManager {
	private static final Logger log = LoggerFactory.getLogger(RepManager.class);
	@Inject
	NrmModelManager nrmModelManager;

	public List<NrmModel> getAllNrmModel() {
		return nrmModelManager.getAllNrmModels();
	}
	public NrmModel getNrmModelByKey(String key) {
		return nrmModelManager.getNrmModel(key);
	}
	public List<String[]> getResult(NrmRule nrmRule, List<RepDataSet> repDataSets) {
		List<String[]> result = new LinkedList<>();
		MappingNrmRule mappingNrmRule = getMappingNrmRule(nrmRule, repDataSets);
		if (nrmRule.isExpand()){
			setResult4Expand(result, mappingNrmRule);
		}else {
			setResult(result, mappingNrmRule);
		}
		if (nrmRule.isStatistic()) {
			result = StatisticHandler.statistic(nrmRule, result);
		}
		return result;
	}

	private void setResult(List<String[]> result, MappingNrmRule mappingNrmRule) {
		Iterator<String[]> iterator = mappingNrmRule.iterator();
		while (iterator.hasNext()){
			String[] r = iterator.next();
			if (null != r) {
				result.add(r);
			}
		}
	}

	private void setResult4Expand(List<String[]> result, MappingNrmRule mappingNrmRule) {
		Iterator<List<String[]>> iterator = mappingNrmRule.iteratorExpand();
		while (iterator.hasNext()){
			List<String[]> r = iterator.next();
			if (null != r) {
				for (String[] strings : r) {
					result.add(strings);
				}
			}
		}
	}
	private MappingNrmRule getMappingNrmRule(NrmRule nrmRule, List<RepDataSet> repDataSets) {
		MappingNrmRule mappingNrmRule = new MappingNrmRule();
		//初始化模型
		mappingNrmRule.initModel(nrmRule.getNrmMocInfo(), nrmRule.getEmMocInfo(), nrmRule.getBeanMap());
		//从初始化数据，包含elprocess初始化过程
		mappingNrmRule.initData(TableDataSetFactory.getTableDataSets(nrmRule.getEmMocInfo(), repDataSets));
		//初始化filter和expand
		mappingNrmRule.initFilter(nrmRule.getFilterStr());
		mappingNrmRule.initExpandPos(nrmRule.getExpandFieldsPos());
		//初始化extBean
		if (null != nrmRule.getExtBean()) {
			for (Map.Entry<String, Object> entry : nrmRule.getExtBean().entrySet()) {
				mappingNrmRule.initExtBeans(entry.getKey(), entry.getValue());
			}
		}
		//初始化extMethod
		initExtFunc(nrmRule, mappingNrmRule);
		return mappingNrmRule;
	}

	private void initExtFunc(NrmRule nrmRule, MappingNrmRule mappingNrmRule) {
		if (null != nrmRule.getExtMethod()) {
			for (Map.Entry<String, Method> entry : nrmRule.getExtMethod().entrySet()) {
				try {
					mappingNrmRule.initExtFunc(entry.getValue(), entry.getKey());
				} catch (NoSuchMethodException e) {
					log.info(e.getMessage(),e);
				}
			}
		}
	}
}
