import nrm.model.changehandler.NeModelPath;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestNeModelPath {
@Test
public void test_AckObj(){
	NeModelPath ackObj=new NeModelPath();
	ackObj.setActiveModelPath("localhost");
	ackObj.setSerialNumber(1);
	assertEquals("localhost",ackObj.getActiveModelPath());
	assertEquals(1, ackObj.getSerialNumber());
}
}
