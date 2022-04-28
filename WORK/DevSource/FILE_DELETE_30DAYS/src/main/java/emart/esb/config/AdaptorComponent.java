package emart.esb.config;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mb.indigo2.springsupport.AdaptorConfig;
import com.mb.indigo2.springsupport.AdaptorConfigBean;
@Component
public class AdaptorComponent {
	@Autowired
	AdaptorConfigBean bean;
	
	@PostConstruct
	public void adaptorConfigConfig() throws Exception {
	AdaptorConfig.getInstance().setAdaptorName(bean.getAdaptorName());
	}
}
