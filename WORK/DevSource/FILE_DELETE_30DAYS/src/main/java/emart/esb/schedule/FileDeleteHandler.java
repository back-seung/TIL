package emart.esb.schedule;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import emart.esb.info.InterfaceInfo;
import lombok.extern.slf4j.Slf4j;

@RestController
@Component
@Slf4j
public class FileDeleteHandler {

	@Autowired
	Map<String, InterfaceInfo> interfaceMap;


	@SuppressWarnings({ "unchecked", "resource", "rawtypes" })
	@Transactional
	@Scheduled(cron = "${cronExpression}")
	public void onStart() throws Exception {

		for (String interfaceId : interfaceMap.keySet()) {
			InterfaceInfo info = interfaceMap.get(interfaceId);
			String path = info.getSendDir();
			String if_Id = info.getEsbIfId();
			String tx_Id = generateTxid(if_Id);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Path findAllFile = Paths.get(path);
			List<Path> fileList;
			Stream<Path> founded = Files.walk(findAllFile);
			fileList = founded.filter(path2 -> !Files.isDirectory(path2, LinkOption.NOFOLLOW_LINKS))
					.collect(Collectors.toList());

			log.info("##[{}]     Delete Scheduler Start   -  {}##", tx_Id, if_Id);
			if (fileList.size() == 0) {
				log.info("##[{}]    Can not Find Any Files in this Directory   -  ##", tx_Id);
				log.info("##[{}]     Delete Scheduler End.   -  {}##", tx_Id, if_Id);
				return;
			}
			for (Path file : fileList) {
				try {
					// 현재일 & 생성일
					Date now = new Date();
					String date = Files.getAttribute(file, "creationTime").toString();
					// 수정일
					Date creationDate = format.parse(date);

					// 두 날짜 차이 게산
					long subDate = timeConvert(now, creationDate);

					// 현재일 - 파일생성일 차이가 30일이 넘을 시 삭제
					int deleteCycle = Integer.parseInt(info.getDelCycle());

					// 로그에 뿌릴 String 변수 지정
					String fileName = file.getFileName().toString();
					String nowDate = format.format(now);
					String createDate = format.format(creationDate);

					// 두 날짜의 차이가 지정일(DeleteCycle)을 넘는다면 ? 파일 삭제 : 파일 유지
					if (subDate >= deleteCycle && !(file.equals(null))) {
						log.info(
								"###[{}]     Now File is : [{}], Current Date is : [{}], Creation Date is [{}], Sub Date is [{}], This File[{}] Will be Remove",
								tx_Id, fileName, nowDate, createDate, subDate, fileName);
						Files.delete(file);
					} else {
						log.info(
								"###[{}]     Now File is : [{}], Current Date is : [{}], Creation Date is [{}], Sub Date is [{}], This File[{}] is Not Over {} days after created",
								tx_Id, fileName, nowDate, createDate, subDate, fileName, deleteCycle);
					}
				} catch (NoSuchFileException e) {
					log.error("###[{}]     File Does not Exist. Please Check your Dir OR File Name ", tx_Id, e);
				} catch (Exception e) {
					log.error("###[{}]     File Does not Exist. Please Check your Dir OR File Name ", tx_Id, e);
				}
			}
			log.info("##[{}]     Delete Scheduler End.   -  ##", tx_Id);
		}
	}

	// 두 날짜 차이 계산하여 결과 리턴
	public long timeConvert(Date now, Date creation) {
		TimeUnit time = TimeUnit.DAYS;
		long difference = now.getTime() - creation.getTime();
		long subDate = time.convert(difference, TimeUnit.MILLISECONDS);
		return subDate;
	}

	// TXID 생성
	public String generateTxid(String if_id) {
		String initTime = DateFormatUtils.format(new java.util.Date(), "yyMMddHHmmssSSS");
		Random random = new Random(System.currentTimeMillis());
		int randomNumber = -1;
		while ((randomNumber = random.nextInt(1000)) < 100)
			;
		return if_id + "_" + initTime + String.valueOf(randomNumber);
	}
}
