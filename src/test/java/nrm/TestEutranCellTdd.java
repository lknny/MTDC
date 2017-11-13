package nrm;

import nrm.pojo.NrmRule;
import nrm.pojo.RepDataSet;
import nrm.util.NrmUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ${lknny@163.com} on 2017/8/6.
 */
public class TestEutranCellTdd {
	@Test
	public void testFunc(){
		NrmRule nrmRule = NrmUtil.getNrmRule("EutranCellTdd");
		RepDataSet eUtranCellTDD = new RepDataSet();
		eUtranCellTDD.setTableName("EUtranCellTDD");
		eUtranCellTDD.setTableField(new String[]{"MEID", "USERLABEL", "VENDORNAME", "USERDEFINEDSTATE", "SWVERSION", "Version", "adminStateLTETDD", "OperationalState", "METYPE", "MEADDR", "mimVersion"});
		List<Object[]> rowData = new ArrayList<>();
		ArrayList<Object> al = new ArrayList<Object>();
		al.add("1001");
		al.add("cellTdd" + (int)Math.random()*1000);
		al.add("zte");
		al.add("ok");
		al.add("V1.0.0.1");
		al.add("V2.0.1.1");
		al.add("100;200;300;400");
		al.add("0");
		al.add("gnb");
		al.add(Long.valueOf(1500892830696L));
		al.add("v3.4");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("1002");
		al.add("cellTdd" + (int)Math.random()*1000);
		al.add("ShenZhen");
		al.add("ok");
		al.add("V1.0.0.1");
		al.add("V2.0.1.1");
		al.add("1");
		al.add("5");
		al.add("gnb");
		al.add(Long.valueOf(1500892830696L));
		al.add("v3.5");
		rowData.add(al.toArray());
		eUtranCellTDD.setRowData(rowData);

		RepDataSet phyChannelTDD = new RepDataSet( );
		phyChannelTDD.setTableName("PhyChannelTDD");
		phyChannelTDD.setTableField(new String[]{"MEID","LOCATIONNAME"});
		rowData = new ArrayList<>();
		al = new ArrayList<Object>();
		al.add("1001");
		al.add("ShenZhen");
		rowData.add(al.toArray());
		phyChannelTDD.setRowData(rowData);

		RepDataSet ueTimerTDD = new RepDataSet( );
		ueTimerTDD.setTableName("UeTimerTDD");
		ueTimerTDD.setTableField(new String[]{"MEID","city","b"});
		rowData = new ArrayList<>();
		al = new ArrayList<Object>();
		al.add("1001");
		al.add("ShenZhen");
		al.add("set1");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("1002");
		al.add("ShenZhen");
		al.add("set2");
		rowData.add(al.toArray());
		ueTimerTDD.setRowData(rowData);

		List<RepDataSet> repDataSets = new ArrayList<RepDataSet>() {{
			add(eUtranCellTDD);
			add(phyChannelTDD);
			add(ueTimerTDD);
		}};

		RepManager repManager = new RepManager();
		List<String[]> result = repManager.getResult(nrmRule, repDataSets);
		assertEquals("1001,cellTdd0,DC=www.zte.com.cn,SubNetwork=ZTE_EUTRAN_SYSTEM,zte,ShenZhen,DC=www.zte.com.cn,SubNetwork=ZTE_EUTRAN_SYSTEM,ManagementNode=OMC,ok,V1.0.0.1,100-200-300-400-,Locked,gnb,set1,2017-07-24 18:40:30,V2.0.1.1_Patch,," +
				"1002,cellTdd0,DC=www.zte.com.cn,SubNetwork=ZTE_EUTRAN_SYSTEM,ShenZhen,,DC=www.zte.com.cn,SubNetwork=ZTE_EUTRAN_SYSTEM,ManagementNode=OMC,ok,V1.0.0.1,1-,Unknown type,gnb,set2,2017-07-24 18:40:30,V1.0.0.1_Patch,ShenZhen-set1;ShenZhen-set2,"
		, NrmUtil.getResult(result));
	}
}
