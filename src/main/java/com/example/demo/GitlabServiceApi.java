package com.example.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class GitlabServiceApi {

	@Autowired
    SummaryDao summaryDao;

    /**
     * 一覧を表示（トップページ）
     * @throws Exception
     */
	@GetMapping
	public List<SummaryDto> getAll(@RequestParam("startDateStr") String startDateStr,@RequestParam("endDateStr") String endDateStr ) throws Exception {

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
	    	SummaryDto todayDto = GitlabSendRequest.get();
			list.add(todayDto);
		}

        //トップページに遷移する
        return list;
    }


}
