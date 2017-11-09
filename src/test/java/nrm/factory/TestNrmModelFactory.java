import com.zte.ums.cnms.cm.repworker.nrm.TestEutranCellTdd;
import com.zte.ums.cnms.cm.repworker.nrm.pojo.NrmModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ${10190990} on 2017/8/30.
 */
public class TestNrmModelFactory {

	@Test
	public void testMk2(){
		NrmModel nrmModel=NrmModelFactory.getNrmModel(TestEutranCellTdd.class.getResource("/").getPath() + "FunctionTest/test4NrmModelFactory.xml");
		assertEquals(6,nrmModel.getRules().size());
	}

}
