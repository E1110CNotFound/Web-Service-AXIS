package com.guoyonghui.weatherforecast.webservice;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;


public class WeatherWebService {

	private static final String SERVER_URL = "http://webservice.webxml.com.cn/WebServices/WeatherWebService.asmx";

	private static final String NAMESPACE = "http://WebXml.com.cn/";

	private static final String METHOD_GET_SUPPORT_PROVINCE = "getSupportProvince";

	private static final String METHOD_GET_SUPPORT_CITY = "getSupportCity";

	private static final String METHOD_GET_WEATHER_BY_CITYNAME_PRO = "getWeatherbyCityNamePro";

	private static final String RESULT_GET_SUPPORT_PROVINCE = "getSupportProvinceResult";

	private static final String RESULT_GET_SUPPORT_CITY = "getSupportCityResult";

	private static final String RESULT_GET_WEATHER_BY_CITYNAME_PRO = "getWeatherbyCityNameProResult";

	private static final String USER_ID = "ea0823bc248e4ba793098051a8671668";

	/***
	 * 请求webservice
	 * @param method 请求方法
	 * @param resultType 返回类型
	 * @param params 请求参数
	 * @return 返回的数据
	 */
	@SuppressWarnings("unchecked")
	private static Vector<String> doWebService(String method, String resultType, Map<String, String> params) {
		try {
			Service service = new Service();
			Call call = (Call)service.createCall();

			call.setTargetEndpointAddress(SERVER_URL);
			call.setOperationName(new QName(NAMESPACE, method));
			call.setReturnType(new QName(NAMESPACE, resultType), Vector.class);
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(NAMESPACE + method);

			for(String key: params.keySet()) {
				call.addParameter(new QName(NAMESPACE, key), XMLType.XSD_STRING, ParameterMode.IN);
			}

			Vector<String> vector = (Vector<String>)call.invoke(params.values().toArray());
			return vector;
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 获取省列表
	 * @return 省列表
	 */
	public static Vector<String> getSupportProvince() {
		System.out.println("----Fetch province list.");

		Map<String, String> params = new HashMap<String, String>();
		
		return doWebService(METHOD_GET_SUPPORT_PROVINCE, RESULT_GET_SUPPORT_PROVINCE, params);
	}

	/***
	 * 获取省的市列表
	 * @param province 省
	 * @return 省的市列表
	 */
	public static Vector<String> getSupportCity(String province) {
		System.out.println("----Fetch city list of " + province + ".");

		Map<String, String> params = new HashMap<String, String>();
		params.put("byProvinceName", province);

		return doWebService(METHOD_GET_SUPPORT_CITY, RESULT_GET_SUPPORT_CITY, params);
	}

	/***
	 * 获取城市的天气
	 * @param city 城市
	 * @return 城市的天气
	 */
	public static Vector<String> getWeatherbyCityNamePro(String city) {
		System.out.println("----Fetch weather info of " + city + ".");

		Map<String, String> params = new HashMap<String, String>();
		params.put("theCityName", city);
		params.put("theUserID", USER_ID);

		return doWebService(METHOD_GET_WEATHER_BY_CITYNAME_PRO, RESULT_GET_WEATHER_BY_CITYNAME_PRO, params);
	}
}
