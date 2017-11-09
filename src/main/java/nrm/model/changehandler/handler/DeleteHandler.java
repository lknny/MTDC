package nrm.model.changehandler.handler;

import com.zte.ums.cnms.cm.core.model.pojo.NeModelModifyNotification;
import com.zte.ums.cnms.cm.repworker.nrm.model.NrmModelManager;

public class DeleteHandler extends ModelChangedHandler{

	@Override
	public void onChanged(NeModelModifyNotification msg, NrmModelManager modelManager) {
		modelManager.removeNrmModel(msg.getMimType(),msg.getMimVersion());
	}
}
