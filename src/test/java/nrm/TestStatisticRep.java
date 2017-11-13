package nrm;

import nrm.pojo.NrmRule;
import nrm.pojo.RepDataSet;
import nrm.util.NrmUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ${lknny@163.com} on 2017/8/15.
 */
public class TestStatisticRep {

	@Test
	public void testStatis(){
		NrmRule nrmRule = NrmUtil.getNrmModel4Statis().getRules().get("CellRep");
		RepDataSet cellDataSet = new RepDataSet();
		cellDataSet.setTableName("Cell");
		cellDataSet.setTableField(new String[]{"managedElement","parentMoDesc","cellid"});
		List<Object[]> rowData = new ArrayList<>();

		ArrayList<Object> al = new ArrayList<Object>();
		al.add(1000);
		al.add("me=1000,gnb=1");
		al.add("cell100");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add(1000);
		al.add("me=1000,gnb=1");
		al.add("cell100");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add(1000);
		al.add("me=1000,gnb=1");
		al.add("cell100");
		rowData.add(al.toArray());


		al = new ArrayList<Object>();
		al.add(1001);
		al.add("me=1001,gnb=1");
		al.add("cell101");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add(1002);
		al.add("me=1002,gnb=2");
		al.add("cell102");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add(1003);
		al.add("me=1003,gnb=3");
		al.add("cell103");
		rowData.add(al.toArray());
		cellDataSet.setRowData(rowData);
//--------------------------------------------------------
		RepDataSet gnbFunction = new RepDataSet();
		gnbFunction.setTableName("GNBFunction");
		gnbFunction.setTableField(new String[]{"moIdentify", "gid"});
		rowData = new ArrayList<>();

		al = new ArrayList<Object>();
		al.add("me=1000,gnb=1");
		al.add("g6666");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("me=1001,gnb=1");
		al.add("g7777");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("me=1002,gnb=2");
		al.add("g8888");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("me=1003,gnb=3");
		al.add("g9999");
		rowData.add(al.toArray());
		gnbFunction.setRowData(rowData);

		//------------------------------

		RepDataSet me = new RepDataSet();
		me.setTableName("ManagedElement");
		me.setTableField(new String[]{"managedElement", "linkStatus"});
		rowData = new ArrayList<>();

		al = new ArrayList<Object>();
		al.add("1000");
		al.add("1");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("1001");
		al.add("1");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("1002");
		al.add("0");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("1003");
		al.add("2");
		rowData.add(al.toArray());
		me.setRowData(rowData);

		//特别注意，数据的顺序和nrmRule的emMoc顺序是严格一致的
		List<RepDataSet> repDataSets = new ArrayList<RepDataSet>() {{
			add(cellDataSet);
			add(me);
			add(gnbFunction);
		}};
		RepManager repManager = new RepManager();
		List<String[]> result = repManager.getResult(nrmRule, repDataSets);
		assertEquals("1000,g6666,cell100,3,3," +
						"1001,g7777,cell101,1,1," +
						"1002,g8888,cell102,1,0," +
						"1003,g9999,cell103,1,0,"
				, NrmUtil.getResult(result));





	}
}
