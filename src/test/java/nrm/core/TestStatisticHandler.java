package nrm.core;

import nrm.pojo.RepData;
import nrm.util.NrmUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ${lknny@163.com} on 2017/8/17.
 */
public class TestStatisticHandler {
	@Test
	public void testStatisticRepData(){
		assertEquals(0,StatisticHandler.statisticRepDatas(null,null).size());
		assertEquals(0,StatisticHandler.statisticRepDatas(null,new ArrayList<>()).size());
		List<RepData> repDatas = new ArrayList<>();
		RepData repData = new RepData();
		repData.setData(new ArrayList<>());
		repDatas.add(repData);
		assertEquals(0,StatisticHandler.statisticRepDatas(NrmUtil.getNrmModel4Statis().getRules().get("CellRep"),repDatas).size());
	}
}
