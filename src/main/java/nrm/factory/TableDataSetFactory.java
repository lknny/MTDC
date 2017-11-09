package nrm.factory;

import nrm.core.TableDataSet;
import nrm.pojo.BaseMocInfo;
import nrm.pojo.DataSetKey;
import nrm.pojo.RepDataSet;

import java.util.List;

/**
 * Created by ${10190990} on 2017/8/6.
 */
public class TableDataSetFactory {
	private TableDataSetFactory() {

	}

	private static TableDataSet getTableDataSet(BaseMocInfo baseMocInfo, RepDataSet repDataSet){
		TableDataSet tableDataSet = new TableDataSet(repDataSet.getTableName(), repDataSet.getTableField());

		if (null != baseMocInfo.getKeys()) {
			for (DataSetKey dataSetKey :baseMocInfo.getKeys()) {
				tableDataSet.setKey(dataSetKey);
			}
		}

		if (null!=repDataSet.getRowData()){
			for (Object[] objects : repDataSet.getRowData()) {
				tableDataSet.addRow(objects);
			}
		}
		return tableDataSet;
	}

	public static  TableDataSet[] getTableDataSets(BaseMocInfo[] baseMocInfos,List<RepDataSet> repDataSets) {
		TableDataSet[] result = new TableDataSet[repDataSets.size()];
		int i = 0;
		for (RepDataSet repDataSet : repDataSets) {
			result[i] = getTableDataSet(baseMocInfos[i],repDataSet);
			++i;
		}
		return result;
	}
}
