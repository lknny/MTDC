package nrm.model.changehandler;

import com.zte.ums.cnms.cm.repworker.Constants;
import com.zte.ums.zenap.httpclient.retrofit.annotaion.ServiceHttpEndPoint;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ${10190990} on 2017/8/14.
 */

	@FunctionalInterface
	@ServiceHttpEndPoint(serviceName = Constants.SERVICE_NAME_MODEL_MANGER_SERVICE, serviceVersion = Constants.API_VERSION)
	public interface NeModelManager {

		@GET("getAll")
		Call<NeModelPath> getActiveModel();
	}

