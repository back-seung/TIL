package emart.esb.info;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class InterfaceInfo {

	protected String ifId;
	protected String esbIfId;
	protected String tableName;
	protected String targetUrlName;
	protected String sendDir;
	protected String errDir;
	protected String scsDir;
	protected String rcvDir;
	protected String backupDir;
	protected String resultDir;
	protected String targetUrl;
	protected Map<String, List<Map<String, Object>>> type;
}
