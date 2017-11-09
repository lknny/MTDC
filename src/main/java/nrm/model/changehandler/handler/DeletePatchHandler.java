package nrm.model.changehandler.handler;

/**
 * 删除补丁包，版本名称不改变，所以直接将模型路径覆盖到VersionManager中，由ModelManager进行重新加载
 * 
 * @author lknny@163.com
 */
//public class DeletePatchHandler extends ModelChangedHandler {
//
//	@Override
//	public void onChanged(NeModelModifyNotification msg, NrmModelManager modelManager) {
//		defalutHandle(msg, modelManager);
//	}
//}
