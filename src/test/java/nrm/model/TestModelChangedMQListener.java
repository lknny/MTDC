import com.zte.ums.cnms.cm.core.model.pojo.NeModelModifyNotification;
import com.zte.ums.cnms.cm.core.model.pojo.NeModelModifyNotifications;
import nrm.model.changehandler.ModelChangedMQListener;
import MockFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestModelChangedMQListener {
	NrmModelManager manager;
	ModelChangedMQListener modelChangedMQListener;

	@Before
	public void Init() {
		manager = MockFactory.getModelManagerInstance();
		modelChangedMQListener = new ModelChangedMQListener(NeModelModifyNotifications.class, manager);
	}

	@Test
	public void test_model_active_mq_listener() {
		NeModelModifyNotifications notifications = new NeModelModifyNotifications();
		List<NeModelModifyNotification> ns = new ArrayList<>();
		NeModelModifyNotification msg = new NeModelModifyNotification();
		msg.setType("1");
		msg.setMimVersion("v1");
		msg.setModelPath("localhost");
		msg.setSerialNumber("1");
		ns.add(msg);
		notifications.setNotifications(ns);
		modelChangedMQListener.onMessage(notifications);
		assertEquals("v1", msg.getMimVersion());
		assertEquals("localhost", msg.getModelPath());

	}

	@Test
	public void test_model_delete_mq_listener() {
		NeModelModifyNotifications notifications = new NeModelModifyNotifications();

		List<NeModelModifyNotification> ns = new ArrayList<>();

		NeModelModifyNotification msg = new NeModelModifyNotification();
		msg.setType("2");
		msg.setMimVersion("v1");
		msg.setModelPath("localhost");
		msg.setSerialNumber("1");
		ns.add(msg);
		notifications.setNotifications(ns);

		modelChangedMQListener.onMessage(notifications);
		assertEquals("v1", msg.getMimVersion());
		assertEquals("localhost", msg.getModelPath());

	}

	@Test
	public void test_model_patch_active_mq_listener() {
		NeModelModifyNotifications notifications = new NeModelModifyNotifications();

		List<NeModelModifyNotification> ns = new ArrayList<>();

		NeModelModifyNotification msg = new NeModelModifyNotification();
		msg.setType("3");
		msg.setMimVersion("v1");
		msg.setModelPath("localhost");
		List<String> target=new ArrayList<>();
		target.add("abc/cm/abdd.yang");
		msg.setTargets(target);
		msg.setSerialNumber("1");
		ns.add(msg);
		notifications.setNotifications(ns);

		modelChangedMQListener.onMessage(notifications);
		assertEquals("v1", msg.getMimVersion());
		assertEquals("localhost", msg.getModelPath());

	}

	@Test
	public void test_model_patch_active_not_CM__mq_listener() {

		NeModelModifyNotifications notifications = new NeModelModifyNotifications();

		List<NeModelModifyNotification> ns = new ArrayList<>();

		NeModelModifyNotification msg = new NeModelModifyNotification();
		msg.setType("3");
		List<String> target=new ArrayList<>();
		target.add("fm/fm/fm");
		msg.setTargets(target);
		msg.setMimVersion("v1");
		msg.setModelPath("localhost");
		msg.setSerialNumber("1");
		ns.add(msg);
		notifications.setNotifications(ns);

		modelChangedMQListener.onMessage(notifications);
		assertEquals("v1", msg.getMimVersion());
		assertEquals("localhost", msg.getModelPath());
		
		
		msg.setType("3");
		target=new ArrayList<>();
		target.add("fm");
		msg.setTargets(target);
		msg.setMimVersion("v1");
		msg.setModelPath("localhost");
		msg.setSerialNumber("1");
		ns.add(msg);
		notifications.setNotifications(ns);

		modelChangedMQListener.onMessage(notifications);
		assertEquals("v1", msg.getMimVersion());
		assertEquals("localhost", msg.getModelPath());
	}

	@Test	
	public void test_model_other_mq_listener() {
		NeModelModifyNotifications notifications = new NeModelModifyNotifications();

		List<NeModelModifyNotification> ns = new ArrayList<>();

		NeModelModifyNotification msg = new NeModelModifyNotification();
		msg.setType("1");
		msg.setSerialNumber("1");
		ns.add(msg);
		msg.setModelPath("localhost");
		notifications.setNotifications(ns);
		
		modelChangedMQListener.onMessage(notifications);
	}
	
	@Test
	public void test_model_patch_delete_mq_listener() {

		NeModelModifyNotifications notifications = new NeModelModifyNotifications();

		List<NeModelModifyNotification> ns = new ArrayList<>();

		NeModelModifyNotification msg = new NeModelModifyNotification();
		msg.setType("4");
		List<String> target=new ArrayList<>();
		target.add("cm");
		msg.setTargets(target);
		msg.setMimVersion("v1");
		msg.setModelPath("localhost");
		
		msg.setSerialNumber("1");
		ns.add(msg);
		notifications.setNotifications(ns);

		modelChangedMQListener.onMessage(notifications);
		assertEquals("v1", msg.getMimVersion());
		assertEquals("localhost", msg.getModelPath());

	}
	
	@Test
	public void test_model_patch_delete_not_CM__mq_listener() {

		NeModelModifyNotifications notifications = new NeModelModifyNotifications();

		List<NeModelModifyNotification> ns = new ArrayList<>();

		NeModelModifyNotification msg = new NeModelModifyNotification();
		msg.setType("4");
		List<String> target=new ArrayList<>();
		target.add("fm");
		msg.setTargets(target);
		msg.setMimVersion("v1");
		msg.setSerialNumber("1");
		msg.setModelPath("localhost");
		ns.add(msg);
		notifications.setNotifications(ns);

		modelChangedMQListener.onMessage(notifications);
		assertEquals("v1", msg.getMimVersion());
		assertEquals("localhost", msg.getModelPath());

	}
}
