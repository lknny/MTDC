import com.zte.ums.cnms.cm.common.mq.MQSessionHolder;
import nrm.model.NrmModelManager;
import nrm.model.changehandler.NeModelManager;
import nrm.model.changehandler.NeModelPath;
import com.zte.ums.zenap.mq.client.api.*;
import com.zte.ums.zenap.mq.client.api.exception.MQException;
import mockit.Mock;
import mockit.MockUp;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;

public class MockFactory {

	public static MQSessionHolder getMQSessionHolderInstance() {
		return new MockUp<MQSessionHolderImpl>() {
			@Mock
			public MQTopicSession getMQTopicSession() throws MQException {
				return new MockUp<MQTopicSession>() {
					@Mock
					public MQTopicPublisher createMQTopicPublisher(MQTopic ss) {
						return new MockUp<MQTopicPublisher>() {

						}.getMockInstance();
					}

				}.getMockInstance();
			}

		}.getMockInstance();
	}

	public static MQSessionHolder getMQSessionHolderInstanceWithException() {
		return new MockUp<MQSessionHolderImpl>() {
			@Mock
			public MQTopicSession getMQTopicSession() throws MQException {
				throw new MQException("");
			}
		}.getMockInstance();
	}

	public static NrmModelManager getModelManagerInstance() {
		NrmModelManager modelManager= new MockUp<NrmModelManager>() {
			@Mock
			public synchronized void addNrmModel(String modelPath, String mimType, String mimVersion) {
			}
			@Mock
			public synchronized void removeNrmModel(String mimType, String mimVersion) {
			}


		}.getMockInstance();
		return modelManager;
	}

	class MQSessionHolderImpl implements MQSessionHolder {

		@Override
		public MQQueueSession getMQQueueSession() throws MQException {
			return null;
		}

		@Override
		public MQTopicSession getMQTopicSession() throws MQException {
			return null;
		}

		@Override
		public <T extends MQListener> void registerConnectionListener(String mqName, boolean isTopic, T mqListener) {

		}

	}

	public static NeModelManager getNeModelManagerInstance() {
		return new MockUp<NeModelManager>() {
			@Mock
			Call<NeModelPath> getActiveModel() {
				return new MockUp<Call<NeModelPath>>() {
					@Mock
					Response<NeModelPath> execute() throws IOException {
						return new MockUp<Response<NeModelPath>>() {
							@Mock
							public NeModelPath body() {
								NeModelPath a = new NeModelPath();
								a.setActiveModelPath(new File("").getAbsolutePath()+File.separator+"src/test/resources/models");
								a.setSerialNumber(11);
								return a;
							}
						}.getMockInstance();
					}
				}.getMockInstance();
			}
		}.getMockInstance();
	}

	public static NeModelManager getModelPackageManagerWithNullInstance() {
		return new MockUp<NeModelManager>() {
			@Mock
			Call<NeModelPath> getActiveModel() {
				return new MockUp<Call<NeModelPath>>() {
					@Mock
					Response<NeModelPath> execute() throws IOException {
						return new MockUp<Response<NeModelPath>>() {
							@Mock
							public NeModelPath body() {
								return null;
							}
						}.getMockInstance();
					}
				}.getMockInstance();
			}
		}.getMockInstance();
	}

	public static NeModelManager getModelPackageManagerWithExceptionInstance() {
		return new MockUp<NeModelManager>() {
			@Mock
			Call<NeModelPath> getActiveModel() {
				return new MockUp<Call<NeModelPath>>() {
					@Mock
					Response<NeModelPath> execute() throws IOException {
						return new MockUp<Response<NeModelPath>>() {
							@Mock
							public NeModelPath body() {
								throw new RuntimeException();
							}
						}.getMockInstance();
					}
				}.getMockInstance();
			}
		}.getMockInstance();
	}

}
