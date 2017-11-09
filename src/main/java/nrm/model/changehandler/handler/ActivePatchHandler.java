package nrm.model.changehandler.handler;

import com.zte.ums.cnms.cm.core.model.pojo.NeModelModifyNotification;
import com.zte.ums.cnms.cm.repworker.nrm.model.NrmModelManager;

import java.util.List;

/**
 * 仅仅对CM模块进行处理
 * 
 * @author 10190990
 */
public class ActivePatchHandler extends ModelChangedHandler{
	
	@Override
	public void onChanged(NeModelModifyNotification msg,NrmModelManager modelManager) {
		if (isCmModel(msg.getTargets())) {
			defalutHandle(msg, modelManager);
		}
	}
	
	private boolean isCmModel(List<String> targets){
		for (String t : targets) {
			if (isValid(t)&&	t.split("/")[1].equals(ModelChangedHandler.CM_MODEL_IDENTIFY)	) {
					return true;
			}
		}
		return false;
	}
	
	private boolean isValid(String target){
		if (target.split("/").length<2) {
			return false;
		}
		return true;
	}
}
