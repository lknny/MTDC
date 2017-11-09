import nrm.TestEutranCellTdd;
import nrm.common.CommFunc;
import nrm.factory.NrmModelFactory;
import nrm.pojo.NrmModel;
import nrm.pojo.NrmRule;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${lknny@163.com} on 2017/8/10.
 */
public class NrmUtil {
	private static NrmModel nrmModel4Rep;
	private static NrmModel nrmModel4Statis;
	static {
		nrmModel4Rep = NrmModelFactory.getNrmModel(TestEutranCellTdd.class.getResource("/").getPath() + "FunctionTest/nrm.xml");
		nrmModel4Statis=NrmModelFactory.getNrmModel(TestEutranCellTdd.class.getResource("/").getPath() + "FunctionTest/statistic.xml");
	}

	public static NrmRule getNrmRule(String nrmMocName){
		NrmRule nrmRule= nrmModel4Rep.getRules().get(nrmMocName);
		if (nrmMocName.equals("EutranCellTdd")){
			try{
				Method method = CommFunc.class.getMethod("getAttrByPk", new Class[] {Object.class, Object.class, Object.class, String.class, String.class});
				Map<String, Method> extFunc = new HashMap<>();
				extFunc.put("getAttrByPk",method);
			nrmRule.setExtMethod(extFunc);
			}catch(Exception e){
				e.printStackTrace();
			}
			return nrmRule;
		}
		return nrmRule;
	}

	public static NrmModel getNrmModel4Statis(){
		return nrmModel4Statis;
	}

	public static String getResult(List<String[]> result) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String[] strings : result) {
			for (String string : strings) {
				stringBuilder.append(string).append(",");
			}
		}
		return stringBuilder.toString();
	}
}
