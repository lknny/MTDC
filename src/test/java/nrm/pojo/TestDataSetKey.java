package nrm.pojo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ${lknny@163.com} on 2017/8/17.
 */
public class TestDataSetKey {
	@Test
	public void testDataSetKey(){
		DataSetKey dataSetKey = new DataSetKey(2,null,null);
		assertEquals(2,dataSetKey.getKeyType());
	}
}
