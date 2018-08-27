package com.tc.emms.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tc.emms.service.BusinessRequest;

public class JsonUtils {
	/**
	 * 得到请求报文
	 * 
	 * @return
	 */
	public static String paramsToJson(BusinessRequest mRequest) {
		String strParams = null;
		if (mRequest.requestType == BusinessRequest.REQUEST_TYPE_GET) {
			strParams = NetUtils.mapToParams(mRequest.params);
		} else if (mRequest.params != null) {
			strParams = JSON.toJSONString(mRequest.params);
		} else if (mRequest.paramsObject != null) {
			// strParams = JSON.toJSONString(mRequest.paramsObject);
			strParams = JSON.toJSONString(mRequest.paramsObject,
					SerializerFeature.SortField);
		} else {
			strParams = mRequest.paramsJSON;
		}

		return strParams;
	}


}
