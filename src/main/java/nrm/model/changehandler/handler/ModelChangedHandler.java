package nrm.model.changehandler.handler;

import com.zte.ums.cnms.cm.core.model.pojo.NeModelModifyNotification;
import com.zte.ums.cnms.cm.repworker.nrm.model.NrmModelManager;

/**
 * 模型变更处理中心
 * @author 10190990
 *
 */
public abstract class ModelChangedHandler {
	static final String CM_MODEL_IDENTIFY = "cm";
	abstract void onChanged(NeModelModifyNotification msg,NrmModelManager modelManager);
	//默认处理
	protected void defalutHandle(NeModelModifyNotification msg,NrmModelManager modelManager){
			modelManager.addNrmModel(msg.getModelPath(),msg.getMimType(),msg.getMimVersion());
	}
}
