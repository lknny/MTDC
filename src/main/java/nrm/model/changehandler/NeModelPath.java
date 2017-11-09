package nrm.model.changehandler;

/**
 * Created by ${10190990} on 2017/8/14.
 */

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 与模型管理服务通信消息体
 *
 */
public class NeModelPath {
	@JsonProperty
	private String activeModelPath;
	@JsonProperty
	private long serialNumber;

	public String getActiveModelPath() {
		return activeModelPath;
	}

	public void setActiveModelPath(String activeModelPath) {
		this.activeModelPath = activeModelPath;
	}

	public long getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(long serialNumber) {
		this.serialNumber = serialNumber;
	}
}
