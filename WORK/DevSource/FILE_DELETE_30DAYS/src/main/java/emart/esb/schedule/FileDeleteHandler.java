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
		String path = "C:\\Users\\seung\\Desktop\\TIL\\WORK\\DevSource\\FILE_DELETE_30DAYS\\src\\main\\resources\\folder";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Path findAllFile = Paths.get(path);
		List<Path> fileList;
		Stream<Path> founded = Files.walk(findAllFile);
		fileList = founded.filter(Files::isRegularFile).collect(Collectors.toList());

		log.info("##     Delete Scheduler Start     ##");
		if (fileList.size() == 0) {
			log.info("##     Can not Find Any Files in this Directory     ##");
			log.info("##     Delete Scheduler End.     ##");
			return;
		}
		for (Path file : fileList) {
			try {
				// 현재일 & 생성일
				Date now = new Date();
				String date = Files.getAttribute(file, "lastModifiedTime").toString();
				// 수정일
				// String date = Files.getAttribute(file, "creationTime").toString();
				Date creationDate = format.parse(date);

				// 테스트용 file name
//					String fileName = "test.txt";
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(now);
				calendar.add(Calendar.MONTH, 1);
				Date test = calendar.getTime();

				// 두 날짜 차이 게산
				long subDate = timeConvert(test, creationDate);

				// 현재일 - 파일생성일 차이가 30일이 넘을 시 삭제
//				int deleteCycle = Integer.parseInt(info.getDelCycle());
				int deleteCycle = Integer.parseInt("30");

				// 로그에 뿌릴 String 변수 지정
				String fileName = file.getFileName().toString();
				String nowDate = format.format(test);
				String createDate = format.format(creationDate);

				// 두 날짜의 차이가 지정일(DeleteCycle)을 넘는다면 ? 파일 삭제 : 파일 유지
				if (subDate >= deleteCycle && !(file.equals(null))) {
					log.info(
							"###     Now File is : [{}], Current Date is : [{}], Creation Date is [{}], Sub Date is [{}], [{}] This File Will be Remove",
							fileName, nowDate, createDate, subDate, fileName);
					Files.delete(file);
				} else {
					log.info(
							"###     Now File is : [{}], Current Date is : [{}], Creation Date is [{}], Sub Date is [{}], [{}] This File is Not Over {} days after created",
							fileName, nowDate, createDate, subDate, fileName, deleteCycle);
				}
			} catch (NoSuchFileException e) {
				log.error("###     File Does not Exist. Please Check your Dir || File Name      ###");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("##     Delete Scheduler End.     ##");
//		}
	}

	public static void main(String[] args) throws Exception {
		FileDeleteHandler handler = new FileDeleteHandler();
		handler.onStart();
	}

	// 두 날짜 차이 계산하여 결과 리턴
	public long timeConvert(Date now, Date creation) {
		TimeUnit time = TimeUnit.DAYS;
		long difference = now.getTime() - creation.getTime();
		long subDate = time.convert(difference, TimeUnit.MILLISECONDS);
		return subDate;
	}
}
