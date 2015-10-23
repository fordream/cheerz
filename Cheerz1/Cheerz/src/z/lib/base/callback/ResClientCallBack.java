package z.lib.base.callback;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.acv.cheerz.util.CheerzUtils;

public abstract class ResClientCallBack extends CallBack {
	private Map<String, String> maps = new HashMap<String, String>();

	public final void addParam(String name, String value) {
		maps.put(name, value);
	}

	public RestClient.RequestMethod getMedthod() {
		return RestClient.RequestMethod.POST;
	}

	public abstract String getApiName();

	@Override
	public Object execute() {
		RestClient client = new RestClient(CheerzUtils.API.BASESERVER + getApiName());
		Set<String> set = maps.keySet();
		for (String key : set) {
			client.addParam(key, maps.get(key));
		}
		try {
			client.execute(getMedthod());
		} catch (Exception e) {
		}
		
		if (client.getResponseCode() == 200) {
			onSaveInAsynTask(client);
		}
		return client;
	}

	public void onSaveInAsynTask(RestClient client) {

	}

	@Override
	public void onCallBack(Object object) {
		RestClient client = (RestClient) object;
		if (client.getResponseCode() == 200) {
			onSuccess(client.getResponse());
		} else {
			onError(client.getErrorMessage());
		}
	}

	public void onError(String errorMessage) {

	}

	public void onSuccess(String response) {

	}
}