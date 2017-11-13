//import nrm.model.changehandler.NeModelManager;
//import nrm.pojo.NrmModel;
//import MockFactory;
//import mockit.Deencapsulation;
//import org.junit.Test;
//
//import java.io.File;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * TODO
// *
// * @author lknny@163.com
// */
//public class TestModelServerManager {
//
//	@Test
//	public void test_caught_exception_postConstruct() {
//		NrmModelManager modelServerManager = new NrmModelManager();
//		NeModelManager modelPackageManager = MockFactory.getNeModelManagerInstance();
//		Deencapsulation.setField(modelServerManager, modelPackageManager);
//		CountDownLatch countDownLatch = new CountDownLatch(1);
//		modelServerManager.postConstruct();
//		try {
//			countDownLatch.await(2, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		List<NrmModel> nrmModels=modelServerManager.getAllNrmModels();
//
//		assertEquals(2, nrmModels.size());
//		modelServerManager.removeNrmModel("vlte","V16.04.01");
//		assertEquals(0, modelServerManager.getAllNrmModels().size());
//		modelServerManager.addNrmModel(new File("").getAbsolutePath() + File.separator + "src/test/resources/models", "vlte", "V16.04.01");
//		assertEquals(2, nrmModels.size());
//	}
//
////	@Test
////	public void test_normal_reload() {
////		NrmModelManager modelServerManager = new NrmModelManager();
////		Deencapsulation.setField(modelServerManager, MockFactory.getModelLoaderInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getVersionManagerInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getMQSessionHolderInstance());
////		NeModelManager modelPackageManager = MockFactory.getNeModelManagerInstance();
////		Deencapsulation.setField(modelServerManager, modelPackageManager);
////		modelServerManager.reload();
////		Map<String, NeModel> modelMap = modelServerManager.getModelMap();
////		modelServerManager.registerCMModelChangedListener(new ModelChangedListener() {
////			@Override
////			public void onChanged() {
////			}
////		});
////		assertEquals(5, modelMap.size());
////		assertNotNull(modelMap.get("vLTE_5GV1.0"));
////		assertNotNull(modelMap.get("vLTE_5GV1.1"));
////
////	}
////
////	@Test
////	public void test_reload_version(){
////		NrmModelManager modelServerManager = new NrmModelManager();
////		Deencapsulation.setField(modelServerManager, MockFactory.getModelLoaderInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getVersionManagerInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getMQSessionHolderInstance());
////		NeModelManager modelPackageManager = MockFactory.getNeModelManagerInstance();
////		Deencapsulation.setField(modelServerManager, modelPackageManager);
////		CountDownLatch countDownLatch = new CountDownLatch(1);
////		modelServerManager.reload("vLTE_5GV1.0");
////		try {
////			countDownLatch.await(1, TimeUnit.SECONDS);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
////		Map<String, NeModel> modelMap = modelServerManager.getModelMap();
////		assertNotNull(modelMap.get("vLTE_5GV1.0"));
////	}
////
////	@Test
////	public void test_delete_version(){
////		NrmModelManager modelServerManager = new NrmModelManager();
////		Deencapsulation.setField(modelServerManager, MockFactory.getModelLoaderInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getVersionManagerInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getMQSessionHolderInstance());
////		NeModelManager modelPackageManager = MockFactory.getNeModelManagerInstance();
////		Deencapsulation.setField(modelServerManager, modelPackageManager);
////		CountDownLatch countDownLatch = new CountDownLatch(1);
////		modelServerManager.reload("5GV999");
////		try {
////			countDownLatch.await(1, TimeUnit.SECONDS);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
////		Map<String, NeModel> modelMap = modelServerManager.getModelMap();
////		assertEquals(null, modelMap.get("5GV999"));
////	}
////
////
////	@Test
////	public void test_normal_postconstruct() {
////		CountDownLatch countDownLatch = new CountDownLatch(1);
////		NrmModelManager modelServerManager = new NrmModelManager();
////		Deencapsulation.setField(modelServerManager, MockFactory.getModelLoaderInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getVersionManagerInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getMQSessionHolderInstance());
////		NeModelManager modelPackageManager = MockFactory.getNeModelManagerInstance();
////		Deencapsulation.setField(modelServerManager, modelPackageManager);
////
////		modelServerManager.postConstruct();
////		try {
////			countDownLatch.await(4, TimeUnit.SECONDS);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
////		assertEquals(5, modelServerManager.getAllModel().size());
////
////	}
////
////	@Test
////	public void test_getAllNeModelConstraints() {
////		NrmModelManager modelServerManager = new NrmModelManager();
////		Deencapsulation.setField(modelServerManager, MockFactory.getModelLoaderInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getVersionManagerInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getMQSessionHolderInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getNeModelManagerInstance());
////		modelServerManager.reload();
////		ConcurrentMap<String, List<ConstraintInfo>> allNeModelConstraints = modelServerManager.getAllNeModelConstraints();
////		assertNotNull(allNeModelConstraints);
////		assertEquals(1, allNeModelConstraints.size());
////	}
////
////	@Test
////	public void test_serial_no() {
////		NrmModelManager modelServerManager = new NrmModelManager();
////		Deencapsulation.setField(modelServerManager, MockFactory.getModelLoaderInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getVersionManagerInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getMQSessionHolderInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getNeModelManagerInstance());
////		//流水号默认0,或者模型正在加载，流水号也是0
////		assertEquals(0, modelServerManager.getSerialNumber());
////		Deencapsulation.setField(modelServerManager, "serialNo",1);
////		assertEquals(11, modelServerManager.getSerialNumber());
////	}
////
////	@Test
////	public void test_mqsession_exception() {
////		NrmModelManager modelServerManager = new NrmModelManager();
////		Deencapsulation.setField(modelServerManager, MockFactory.getModelLoaderInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getVersionManagerInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getMQSessionHolderInstance());
////		Deencapsulation.setField(modelServerManager,  MockFactory.getVersionManagerInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getNeModelManagerInstance());
////		Deencapsulation.setField(modelServerManager, MockFactory.getMQSessionHolderInstanceWithException());
////		CountDownLatch countDownLatch = new CountDownLatch(1);
////		modelServerManager.postConstruct();
////		try {
////			countDownLatch.await(4, TimeUnit.SECONDS);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
////	}
////
////	@Test
////	public void should_return_when_Exception(){
////	    new MockUp<NrmModelManager>() {
////	        @Mock
////	        private NeModelPath getNeModelInfo() throws IOException {
////	            throw new IOException("IOException in mock");
////	        }
////        };
////	    NrmModelManager modelServerManager = new NrmModelManager();
////	    try {
////	    	modelServerManager.getSerialNumber();
////		} catch (Exception e) {
////			assertTrue(true);
////		}
////	}
//}
