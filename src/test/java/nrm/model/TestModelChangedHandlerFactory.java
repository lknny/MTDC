import com.zte.ums.cnms.cm.core.model.pojo.NeModelModifyNotification;
import com.zte.ums.cnms.cm.repworker.nrm.model.changehandler.handler.ModelChangedHandlerFactory;
import org.junit.Assert;
import org.junit.Test;


public class TestModelChangedHandlerFactory {
	
	@Test
	public void test_handle_when_not_find_handler() {
		NeModelModifyNotification msg = new NeModelModifyNotification();
		msg.setMimType("notFind");
		ModelChangedHandlerFactory.handle(msg, null);
		Assert.assertTrue(true);
	}

}
