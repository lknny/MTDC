//package nrm.model.changehandler;
//
//
//import nrm.model.NrmModelManager;
//
//import javax.annotation.PostConstruct;
//import javax.inject.Inject;
//
///**
// * Created by ${lknny@163.com} on 2017/8/14.
// */
//@Service
//public class ModelChangedManager implements PostConstruct {
//
//	private static final Logger log = Logger.getLogger(ModelChangedManager.class);
//	@Inject
//	MQSessionHolder sessionHolder;
//	@Inject
//	NrmModelManager modelManager;
//
//	@Override
//	public void postConstruct() {
//		registerModelActiveListener();
//	}
//	private void registerModelActiveListener() {
//		MQTopic topic=new MQTopic();
//		topic.setTopicName(ModelContants.MODEL_CHANGED_TOPIC);
//		MQJsonListener<NeModelModifyNotifications> listener=new ModelChangedMQListener(NeModelModifyNotifications.class,modelManager);
//		try {
//			sessionHolder.getMQTopicSession().subscribeMQTopic(topic, listener);
//			sessionHolder.registerConnectionListener(ModelContants.MODEL_CHANGED_TOPIC,true,listener);
//			log.info("Register model activation service.");
//		} catch (MQException e) {
//			log.error("Failed to register model activation service.", e);
//		}
//	}
//}
//
