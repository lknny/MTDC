package nrm.model.changehandler;

import com.zte.ums.cnms.cm.common.mq.MQSessionHolder;
import com.zte.ums.cnms.cm.core.model.ModelContants;
import com.zte.ums.cnms.cm.core.model.pojo.NeModelModifyNotifications;
import com.zte.ums.cnms.cm.repworker.nrm.model.NrmModelManager;
import com.zte.ums.zenap.mq.client.api.MQJsonListener;
import com.zte.ums.zenap.mq.client.api.MQTopic;
import com.zte.ums.zenap.mq.client.api.exception.MQException;
import org.apache.log4j.Logger;
import org.glassfish.hk2.api.PostConstruct;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

/**
 * Created by ${10190990} on 2017/8/14.
 */
@Service
public class ModelChangedManager implements PostConstruct {

	private static final Logger log = Logger.getLogger(ModelChangedManager.class);
	@Inject
	MQSessionHolder sessionHolder;
	@Inject
	NrmModelManager modelManager;

	@Override
	public void postConstruct() {
		registerModelActiveListener();
	}
	private void registerModelActiveListener() {
		MQTopic topic=new MQTopic();
		topic.setTopicName(ModelContants.MODEL_CHANGED_TOPIC);
		MQJsonListener<NeModelModifyNotifications> listener=new ModelChangedMQListener(NeModelModifyNotifications.class,modelManager);
		try {
			sessionHolder.getMQTopicSession().subscribeMQTopic(topic, listener);
			sessionHolder.registerConnectionListener(ModelContants.MODEL_CHANGED_TOPIC,true,listener);
			log.info("Register model activation service.");
		} catch (MQException e) {
			log.error("Failed to register model activation service.", e);
		}
	}
}

