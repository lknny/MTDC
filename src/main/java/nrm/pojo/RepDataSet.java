package nrm.pojo;

import java.util.List;

/**
 * 配置报表处理数据对象，作为repWorker接收数据入参
 *
 * Created by ${10190990} on 2017/8/6.
 */
public class RepDataSet {

	private String tableName;
	private String[] tableField;
	private List<Object[]> rowData;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String[] getTableField() {
		return tableField.clone();
	}

	public void setTableField(String[] tableField) {
		this.tableField = tableField.clone();
	}

	public List<Object[]> getRowData() {
		return rowData;
	}

	public void setRowData(List<Object[]> rowData) {
		this.rowData = rowData;
	}
}
