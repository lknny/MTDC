package nrm.model;

import com.zte.ums.cnms.cm.common.util.FileUtil;
import com.zte.ums.cnms.cm.repworker.nrm.factory.NrmModelFactory;
import nrm.model.changehandler.NeModelManager;
import nrm.model.changehandler.NeModelPath;
import nrm.pojo.NrmModel;
import org.apache.commons.io.FileUtils;
import org.glassfish.hk2.api.PostConstruct;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ${10190990} on 2017/8/14.
 */
@Service
public class NrmModelManager implements PostConstruct {
	private static Logger log = LoggerFactory.getLogger(NrmModelManager.class.getName());
	private static final String PATH_PREFIX = "/plugins/cm/report/";
	private static final String FILE_TYPE= "xml";
	private static final String FILE_SUFFIX = "-rep.xml";
	private Map<String, NrmModel> models = new ConcurrentHashMap<>();
	private Timer timer = new Timer();
	private NeModelPath neModelPath;

	@Inject
	NeModelManager neModelManager;

	@Override
	public synchronized void postConstruct() {
		initModelManagerTimer();
	}

	private void initModelManagerTimer() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					neModelPath = neModelManager.getActiveModel().execute().body();
				} catch (Exception e) {
					log.info("The RepWorker initial failed.", e);
				}
				if (neModelPath != null) {
					log.info("Get the active model directory: " + neModelPath.getActiveModelPath());
					initNrmModelManager(neModelPath);
					timer.cancel();
				}
			}
		}, 0, 3000);
	}

	/**
	 * version 变更为vnfType_version
	 */
	private void initNrmModelManager(NeModelPath neModelPath) {
		for (File nftypeDir : FileUtil.listDir(neModelPath.getActiveModelPath())) {
			for (File versionDir : FileUtil.listDir(nftypeDir.getAbsolutePath())) {
				parseNrmModel(versionDir.getAbsolutePath() + File.separator + PATH_PREFIX,
						nftypeDir.getName(), versionDir.getName());
			}
		}
	}

	private void parseNrmModel(String nrmModelPath, String mimType, String mimVersion) {
		if (FileUtil.exists(nrmModelPath)) {
			Iterator<File> iterator = FileUtils.listFiles(new File(nrmModelPath), new String[]{FILE_TYPE}, true).iterator();
			while (iterator.hasNext()) {
				File file = iterator.next();
				if (file.getName().endsWith(FILE_SUFFIX)){
					NrmModel nrmModel = NrmModelFactory.getNrmModel(file.getAbsolutePath());
					nrmModel.setKey(getKey(mimType, mimVersion, file.getName()));
					this.models.put(nrmModel.getKey(), nrmModel);
					log.info("Finished loading NRM model file: "+file.getAbsolutePath());
				}
			}
		}
	}

	private String getKey(String mimType, String mimVersion, String fileName) {
		return new StringBuilder(mimType)
				.append("_")
				.append(mimVersion)
				.append("_")
				.append(fileName).toString();
	}

	public void addNrmModel(String modelPath, String mimType, String mimVersion) {
		//加载一个版本报表模型时，先清除此版本所有报表模型
		removeNrmModel(mimType,mimVersion);
		parseNrmModel(modelPath + File.separator + PATH_PREFIX, mimType, mimVersion);
	}

	public synchronized void removeNrmModel(String mimType, String mimVersion) {
		Iterator<String> iterator = models.keySet().iterator();
		while (iterator.hasNext()) {
			if (iterator.next().startsWith(mimType + "_" + mimVersion)) {
				iterator.remove();
			}
		}
	}

	public List<NrmModel> getAllNrmModels() {
		List<NrmModel> nrmModels = new LinkedList<>();
		for (Iterator<NrmModel> iterator = models.values().iterator(); iterator.hasNext(); ) {
			nrmModels.add(iterator.next());
		}
		return nrmModels;
	}

	public NrmModel getNrmModel(String key) {
		return models.get(key);
	}

}
