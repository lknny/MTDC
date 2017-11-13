package nrm.core;

import nrm.factory.TableDataSetFactory;
import nrm.pojo.NrmRule;
import nrm.pojo.RepDataSet;
import nrm.util.NrmUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ${lknny@163.com} on 2017/8/16.
 */
public class TestTableDataSet {
	@Test
	public void testDataSet(){
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

		TableDataSet[] tableDataSets = TableDataSetFactory.getTableDataSets(nrmRule.getEmMocInfo(), repDataSets);
		assertEquals("[ShenZhen, ShenZhen]",tableDataSets[2].getElementByMk1("city", new String[]{"ShenZhen"}).toString());
		assertEquals("{(MEID=1001,USERLABEL=cellTdd0,VENDORNAME=zte,USERDEFINEDSTATE=ok,SWVERSION=V1.0.0.1,Version=V2.0.1.1,adminStateLTETDD=100;200;300;400,OperationalState=0,METYPE=gnb,MEADDR=1500892830696,mimVersion=v3.4),\n" +
				"(MEID=1002,USERLABEL=cellTdd0,VENDORNAME=ShenZhen,USERDEFINEDSTATE=ok,SWVERSION=V1.0.0.1,Version=V2.0.1.1,adminStateLTETDD=1,OperationalState=5,METYPE=gnb,MEADDR=1500892830696,mimVersion=v3.5)}", tableDataSets[0].toString());
		assertEquals( 0,tableDataSets[2].getRowByMk2(new String[]{"helo"}).size());
		assertEquals( "[ShenZhen, ShenZhen]",tableDataSets[2].getElementByMk2("city", new String[]{"ShenZhen"}).toString());

	}
}
