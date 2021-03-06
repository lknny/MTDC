//package nrm.model.changehandler;
//
//import nrm.model.NrmModelManager;
//import nrm.model.changehandler.handler.ModelChangedHandlerFactory;
//
//import java.util.List;
//
///**
// * Created by ${lknny@163.com} on 2017/8/14.
// */
//public class ModelChangedMQListener extends MQJsonListener<NeModelModifyNotifications> {
//	private static final Logger log = Logger.getLogger(ModelChangedMQListener.class);
//	private NrmModelManager modelManager;
//
//	public ModelChangedMQListener(Class<NeModelModifyNotifications> valueType,NrmModelManager modelManager) {
//		super(valueType);
//		this.modelManager=modelManager;
//	}
//
//	/**
//	 * 网元版本变更时，CM模块增量处理，只处理涉及到的网元版本
//	 */
//	@Override
//	public void onMessage(NeModelModifyNotifications msg) {
//		log.info("Received the model change message from the NE model service.");
//		List<NeModelModifyNotification> notifications=msg.getNotifications();
//		for (NeModelModifyNotification neModelModifyNotification : notifications) {
//			//模型变更处理
//			log.info("mimType: "+neModelModifyNotification.getMimType()+" ,mimVersion: "+neModelModifyNotification.getMimVersion()
//					+" ,type: "+neModelModifyNotification.getType());
//			ModelChangedHandlerFactory.handle(neModelModifyNotification,modelManager);
//		}
//	}
//}
