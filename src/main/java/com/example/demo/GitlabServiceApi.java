package com.example.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.AuthReturnDto;
import com.example.demo.burnDown.BurnDownDao;
import com.example.demo.burnDown.BurnDownDto;
import com.example.demo.burnDown.BurnDownReturnDto;
import com.example.demo.templateCreate.CalenderUtil;
import com.example.demo.templateCreate.HolidayDao;
import com.example.demo.templateCreate.HolidayDto;
import com.example.demo.templateCreate.SummaryDao;
import com.example.demo.templateCreate.SummaryDto;

@RestController
public class GitlabServiceApi {

	private static final Logger LOGGER = LogManager.getLogger(GitlabServiceApi.class);

	@Autowired
    SummaryDao summaryDao;

	@Autowired
	BurnDownDao burndownDao;



	@Autowired
	HolidayDao holidayDao;


    /**
     * issueの件数情報を取得
     * @throws Exception
     */
	@CrossOrigin
	@GetMapping("/api")
	public List<SummaryDto> getIssuesCount(@RequestParam("startDateStr") String startDateStr,@RequestParam("endDateStr") String endDateStr ) throws Exception {

    	//入力値の文字列を日付型に変換
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate startDate = LocalDate.parse(startDateStr, formatter);
	    LocalDate endDate   = LocalDate.parse(endDateStr, formatter);

	    //今日の日付を取得し、最終日と等しいか確認
		LocalDate today = LocalDate.now();
		boolean endIsTodayFlg = today.isEqual(endDate);

		//今日の日付が最終日の場合、1日前までを取得するようにする
		if(endIsTodayFlg) {
			endDate = endDate.minusDays(1);
		}

    	//DBから情報を取得しDtoに格納
		List<SummaryDto> list = summaryDao.findAllByDateBetween(startDate, endDate);

	    //今日の日付を入力値に含んでいるか確認
		if(endIsTodayFlg) {
			//現時点の値を確認
			GitlabSendRequest gitlabSendRequest = new GitlabSendRequest();
	    	SummaryDto todayDto = gitlabSendRequest.getIssueStatistics();
			list.add(todayDto);
		}

        //値を返却する
        return list;
    }


    /**
     * 一覧を表示（トップページ）
     * @throws Exception
     */
	@CrossOrigin
	@GetMapping("/burndown")
	public BurnDownReturnDto getBurnDown(@RequestParam("milestone") String milestone, @RequestParam("label") String label, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {

//		System.out.println(milestone);
//		System.out.println(label);

		//祝日のリストを取得
		LocalDate since = LocalDate.parse(startDate);
		LocalDate until = LocalDate.parse(endDate);
		List<HolidayDto> tmpholidayList = holidayDao.findByDateBetween(since, until);
		List<String> holidayList = new ArrayList<>();
		for(HolidayDto dto :tmpholidayList) {
			holidayList.add(dto.getDate().toString());
		}

    	//DBから情報を取得
		List<BurnDownDto> burnDownList =  burndownDao.findByMilestoneAndLabel(milestone, label);

		BurnDownReturnDto ret = new BurnDownReturnDto(burnDownList, holidayList);
		System.out.println(ret);

		return ret;
    }


    /**
     * 次回作成日を返却する
     */
	@CrossOrigin
	@GetMapping("/calcNextCreateDate")
	public String calcNextCreateDate(@RequestParam("createTerms") String createTerms, @RequestParam("createTermsDetail") String createTermsDetail) {

    	//次回作成日を計算
    	CalenderUtil calenderUtil = new CalenderUtil(null);
    	LocalDate nextCreateDate = calenderUtil.getThisCreateDate(Integer.parseInt(createTerms) , Integer.parseInt(createTermsDetail));

		return nextCreateDate.toString();
    }

    /**
     * 次回のISSUEの作成日を返却する
     */
	@CrossOrigin
	@GetMapping("/calcNextIssueDate")
	public String calcNextIssueDate(@RequestParam("createTerms") String createTerms, @RequestParam("createTermsDetail") String createTermsDetail,
			@RequestParam("issueDate") String issueDate, @RequestParam("issueDateDetail") String issueDateDetail) {

		CalenderUtil calenderUtil = null;

		if(Integer.parseInt(issueDateDetail) > 31) {
			// 営業日計算ありの場合
	    	LocalDate today = LocalDate.now();
			ArrayList<String> holidayList = getHolidayList(today);
			calenderUtil = new CalenderUtil(holidayList);

		} else {
			// 営業日計算なしの場合
			calenderUtil = new CalenderUtil(null);
		}

    	//次回作成日を計算
    	LocalDate nextCreateDate = calenderUtil.getThisCreateDate(Integer.parseInt(createTerms) , Integer.parseInt(createTermsDetail));

    	//次回のissueの実施日を計算
    	LocalDate nextIssueDate = calenderUtil.getIssueDate(nextCreateDate, Integer.parseInt(issueDate), Integer.parseInt(issueDateDetail));

    	return nextIssueDate.toString();
    }


    /**
     * 次回のISSUE名を返却する
     */
	@CrossOrigin
	@GetMapping("/convertIssueTitle")
	public String convertIssueTitle(@RequestParam("issueTitle") String issueTitle, @RequestParam("issueDate") String issueDate) {

		CalenderUtil calenderUtil = new CalenderUtil(null);
    	//ISSUEのタイトルを補完
    	return calenderUtil.convertIssueTitle(issueTitle, LocalDate.parse(issueDate));
    }



    /**
     * 次回のISSUE名を返却する
     * @throws Exception
     */
	@CrossOrigin
	@PostMapping("/auth")
	public AuthReturnDto auth(@RequestParam("userName") String userName, @RequestParam("password") String password) throws Exception {

		GitlabSendRequest gitlabSendRequest = new GitlabSendRequest();
    	// TOKENを取得
		String token = gitlabSendRequest.getAuthToken(userName, password);
		String gitUrl = "";
		String groupId = "";
		String retCode = "200";
		String errorMessage = "";

		if(token.length() == 3) {
			// エラー発生時
			retCode = token;
			token = "";
			errorMessage = "ユーザ名またはパスワードが正しくありません";
		} else {
			//成功時
			gitUrl = GrobalConfig.GIT_URL;
			groupId = GrobalConfig.GROUP_ID;
		}

		return new AuthReturnDto(token, retCode, errorMessage, gitUrl, groupId);



    }


	//
	/**
	 * 今月と来月の祝日リストを作成
	 * @param today：今日
	 * @return
	 */
	private ArrayList<String> getHolidayList(LocalDate today) {

		LocalDate since = today.minusDays(today.getDayOfMonth() -1);
		LocalDate until = since.plusMonths(2);
		List<HolidayDto> dbHolidayList = holidayDao.findByDateBetween(since, until);
		ArrayList<String> holidayList = new ArrayList<>();

		for(HolidayDto dto :dbHolidayList) {
			holidayList.add(dto.getDate().toString());
		}
//		System.out.println(holidayList);
		return holidayList;
	}

	/**
	 * パラメータをログに出力する
	 * @param msg
	 */
	@CrossOrigin
	@GetMapping("/logger")
	private void logger(@RequestParam("msg") String msg) {
		LOGGER.info(msg);
	}


}
