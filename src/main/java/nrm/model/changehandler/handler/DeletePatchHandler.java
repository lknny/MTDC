package nrm.model.changehandler.handler;

import com.zte.ums.cnms.cm.core.model.pojo.NeModelModifyNotification;
import com.zte.ums.cnms.cm.repworker.nrm.model.NrmModelManager;

/**
 * 删除补丁包，版本名称不改变，所以直接将模型路径覆盖到VersionManager中，由ModelManager进行重新加载
 * 
 * @author 10190990
 */
public class DeletePatchHandler extends ModelChangedHandler {

	@Override
	public void onChanged(NeModelModifyNotification msg, NrmModelManager modelManager) {
		defalutHandle(msg, modelManager);
	}
}
