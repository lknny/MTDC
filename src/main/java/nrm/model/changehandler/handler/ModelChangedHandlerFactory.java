package nrm.model.changehandler.handler;

import com.zte.ums.cnms.cm.core.model.pojo.NeModelModifyNotification;
import com.zte.ums.cnms.cm.repworker.nrm.model.NrmModelManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${10190990} on 2017/8/14.
 */
public class ModelChangedHandlerFactory {

	private static final Map<String, ModelChangedHandler> configMap=new HashMap<>();

	private static final String ACTIVATE_MODEL_MSG="1";
	private static final String DELETE_MODEL_MSG="2";
	private static final String ACTIVATE_MODEL_PATCH_MSG="3";
	private static final String DELETE_MODEL_PATCH_MSG="4";
	static{
		configMap.put(ACTIVATE_MODEL_MSG, new ActiveHandler());
		configMap.put(DELETE_MODEL_MSG, new DeleteHandler());
		configMap.put(ACTIVATE_MODEL_PATCH_MSG, new ActivePatchHandler());
		configMap.put(DELETE_MODEL_PATCH_MSG, new DeletePatchHandler());
	}
	private ModelChangedHandlerFactory(){}
	public static void handle(NeModelModifyNotification msg,  NrmModelManager modelManager){
		ModelChangedHandler modelChangedHandler = configMap.get(msg.getType());
		if(null != modelChangedHandler){
			modelChangedHandler.onChanged(msg, modelManager);
		}
	}
}
