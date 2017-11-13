package nrm.factory;

import nrm.TestEutranCellTdd;
import nrm.pojo.NrmModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ${lknny@163.com} on 2017/8/30.
 */
public class TestNrmModelFactory {

	@Test
	public void testMk2(){
		NrmModel nrmModel= NrmModelFactory.getNrmModel(TestEutranCellTdd.class.getResource("/").getPath() + "FunctionTest/test4NrmModelFactory.xml");
		assertEquals(6,nrmModel.getRules().size());
	}

}
