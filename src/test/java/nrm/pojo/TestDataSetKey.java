import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ${10190990} on 2017/8/17.
 */
public class TestDataSetKey {
	@Test
	public void testDataSetKey(){
		DataSetKey dataSetKey = new DataSetKey(2,null,null);
		assertEquals(2,dataSetKey.getKeyType());
	}
}
