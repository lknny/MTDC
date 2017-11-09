import com.zte.ums.cnms.cm.common.mq.MQSessionHolder;
import com.zte.ums.cnms.cm.repworker.nrm.model.changehandler.ModelChangedManager;
import MockFactory;
import mockit.Deencapsulation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestModelChangedManager {
	NrmModelManager modelManager;
	MQSessionHolder mqSessionHolder;
	ModelChangedManager modelChangedManager;
	
	@Before
	public  void setUp(){
		modelManager= MockFactory.getModelManagerInstance();
		modelChangedManager=new ModelChangedManager();
		Deencapsulation.setField(modelChangedManager, modelManager);
	}
	
	@Test
	public void test_model_changed_normal_way() {
		mqSessionHolder= MockFactory.getMQSessionHolderInstance();
		Deencapsulation.setField(modelChangedManager, mqSessionHolder);
		try {
			modelChangedManager.postConstruct();
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void test_model_changed_with_exception(){
		mqSessionHolder= MockFactory.getMQSessionHolderInstanceWithException();
		Deencapsulation.setField(modelChangedManager, mqSessionHolder);
		try {
			modelChangedManager.postConstruct();
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}
}
