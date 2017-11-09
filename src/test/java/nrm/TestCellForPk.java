import nrm.pojo.NrmRule;
import nrm.pojo.RepDataSet;
import NrmUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ${lknny@163.com} on 2017/8/10.
 */
public class TestCellForPk {
	@Test
	public void testCell() {
		NrmRule nrmRule = NrmUtil.getNrmRule("Cell");
		//"Id","UserLabel","AdministrativeState","OperationalState","EnbId","X2IpAddressList","X2BlackList","X2WhiteList","X2HoBlackList","IntegrityProtAlgorithm","CipheringAlgorithm"
		RepDataSet cellDataSet = new RepDataSet();
		cellDataSet.setTableName("cell");
		cellDataSet.setTableField(new String[]{"LDN", "UserLabel", "cellLocalId"});
		List<Object[]> rowData = new ArrayList<>();

		ArrayList<Object> al = new ArrayList<Object>();
		al.add("me=1001,enb=1,cell=100");
		al.add("cell100");
		al.add(1100);
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("me=1001,enb=1,cell=101");
		al.add("cell101");
		al.add(1101);
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("me=1001,enb=2,cell=102");
		al.add("cell102");
		al.add(1102);
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("me=1001,enb=3,cell=103");
		al.add("cell103");
		al.add(1103);
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("me=1001,enb=3,cell=104");
		al.add("cell104");
		al.add(1104);
		rowData.add(al.toArray());

		cellDataSet.setRowData(rowData);
		RepDataSet enbFunction = new RepDataSet();
		enbFunction.setTableName("EnbFunction");
		enbFunction.setTableField(new String[]{"funcLDN", "mnn", "mnc"});
		rowData = new ArrayList<>();

		al = new ArrayList<Object>();
		al.add("me=1001,enb=1");
		al.add("300");
		al.add("101");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("me=1001,enb=2");
		al.add("301");
		al.add("102");
		rowData.add(al.toArray());

		al = new ArrayList<Object>();
		al.add("me=1001,enb=3");
		al.add("303");
		al.add("103");
		rowData.add(al.toArray());

		enbFunction.setRowData(rowData);

		List<RepDataSet> repDataSets = new ArrayList<RepDataSet>() {{
			add(cellDataSet);
			add(enbFunction);
		}};

		RepManager repManager = new RepManager();
		List<String[]> result = repManager.getResult(nrmRule, repDataSets);
		assertEquals("me=1001,enb=1,cell=100,cell100,1100,me=1001,enb=1,300,101," +
						"me=1001,enb=1,cell=101,cell101,1101,me=1001,enb=1,300,101," +
						"me=1001,enb=2,cell=102,cell102,1102,me=1001,enb=2,301,102," +
						"me=1001,enb=3,cell=103,cell103,1103,me=1001,enb=3,303,103," +
						"me=1001,enb=3,cell=104,cell104,1104,me=1001,enb=3,303,103,"
		, NrmUtil.getResult(result));
	}
}
