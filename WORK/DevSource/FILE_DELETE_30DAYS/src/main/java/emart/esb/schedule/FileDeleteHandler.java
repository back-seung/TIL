package emart.esb.schedule;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import emart.esb.info.InterfaceInfo;
import lombok.extern.slf4j.Slf4j;

@RestController
@Component
@Slf4j
public class FileDeleteHandler {

	@Autowired
	Map<String, InterfaceInfo> interfaceMap;

	@Scheduled(cron = "${cronExpression}")
	public void onStart() throws Exception {
//		for (String if_id : interfaceMap.keySet()) {
//			InterfaceInfo info = interfaceMap.get(if_id);
//			String path = info.getSendDir();
		String path = "C:\\Users\\seung\\Desktop\\adaptors\\FILE_DELETE_30DAYS\\src\\main\\resources\\folder";
		String fileName = "test.txt";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Path findAllFile = Paths.get(path);
		List<Path> fileList;
		Stream<Path> found = Files.walk(findAllFile);
		fileList = found.filter(Files::isRegularFile).collect(Collectors.toList());

		log.info("##     Delete Scheduler Start     ##");
		if (fileList.size() == 0) {
			log.info("##     Can not Find Any File in this Directory     ##");
			log.info("##     Delete Scheduler End.     ##");
			return;
		}
		for (Path file : fileList) {
			try {
				// 현재일 & 생성일
				Date now = new Date();
				String date = Files.getAttribute(file, "lastModifiedTime").toString();
//				String date = Files.getAttribute(file, "creationTime").toString();
				Date creationDate = format.parse(date);

				// 테스트용 일자
				Calendar cal = Calendar.getInstance();
				cal.setTime(now);
				cal.add(Calendar.MONTH, 1);
				Date test = cal.getTime();

				// 두 날짜 차이 게산
				long subDate = timeConvert(test, creationDate);

				// 현재일 - 파일생성일 차이가 30일이 넘을 시 삭제
				if (subDate >= 30 && !(file.equals(null))) {
					log.info(
							"###     Now File is : [{}], Current Date is : [{}], Creation Date is [{}], Sub Date is [{}], [{}] This File Will be Remove",
							file.getFileName(), format.format(test), format.format(creationDate), subDate,
							file.getFileName());
					Files.delete(file);
				} else {
					log.info(
							"###     Now File is : [{}], Current Date is : [{}], Creation Date is [{}], Sub Date is [{}], [{}] This File is Not Over 30 days after created",
							file.getFileName(), format.format(test), format.format(creationDate), subDate,
							file.getFileName());
				}
			} catch (NoSuchFileException e) {
				log.error("###     File Does not Exist. Please Check your Dir || File Name      ###");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("##     Delete Scheduler End.     ##");
	}

//	}

	public static void main(String[] args) throws Exception {
		FileDeleteHandler handler = new FileDeleteHandler();
		handler.onStart();
	}

	// 두 날짜 차이 계산하여 결과 리턴
	public long timeConvert(Date now, Date creation) {
		TimeUnit time = TimeUnit.DAYS;
		long diff = now.getTime() - creation.getTime();
		long subDate = time.convert(diff, TimeUnit.MILLISECONDS);
		return subDate;
	}
}
