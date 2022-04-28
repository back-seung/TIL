package emart.esb.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import emart.esb.info.InterfaceInfo;

@Configuration
public class AppConfig {

	@Autowired
	List interfaceList;
	
	@Autowired
	JdbcTemplate sourcedb;

	@Bean(name = "interfaceMap")
	public Map<String, InterfaceInfo> interfaceMap() {
		Map<String, InterfaceInfo> interfaceMap = new LinkedHashMap<String, InterfaceInfo>();

		for (int i = 0; i < interfaceList.size(); i++) {
			InterfaceInfo interfaceInfo = (InterfaceInfo) interfaceList.get(i);

			interfaceMap.put(interfaceInfo.getEsbIfId(), interfaceInfo);
		}
		return interfaceMap;
	}

}