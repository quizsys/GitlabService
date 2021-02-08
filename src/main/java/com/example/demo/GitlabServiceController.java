package com.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.templateCreate.CalenderUtil;
import com.example.demo.templateCreate.CodeMstDao;
import com.example.demo.templateCreate.HolidayDao;
import com.example.demo.templateCreate.HolidayDto;
import com.example.demo.templateCreate.ProjectDao;
import com.example.demo.templateCreate.ProjectDto;
import com.example.demo.templateCreate.TemplateCreateDao;
import com.example.demo.templateCreate.TemplateCreateDto;
import com.example.demo.templateCreate.TemplateCreateExtraDao;
import com.example.demo.templateCreate.TemplateCreateExtraDto;

@Controller
public class GitlabServiceController {

	@Autowired
	TemplateCreateExtraDao templateCreateExtraDao;

	@Autowired
	TemplateCreateDao templateCreateDao;

	@Autowired
	CodeMstDao codeMstDao;

	@Autowired
	ProjectDao projectDao;

	@Autowired
	HolidayDao holidayDao;

	/**
     * 一覧を表示（トップページ）
     */
    @RequestMapping(value = "/plan")
    public String index(Model model) {

    	//DBから情報を取得しDtoに格納
		List<TemplateCreateExtraDto> list = templateCreateExtraDao.findAllExtra();

		// 情報編集
//		for(TemplateCreateExtraDto dto: list) {
//			System.out.println(dto);
//		}

        model.addAttribute("list", list);

        //トップページに遷移する
        return "plan";
    }




    /**
     * 新規画面への遷移
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {

    	//コードマスタを渡す
    	model.addAttribute("codeMst", codeMstDao.findAll());

    	//空のDtoを渡す
    	model.addAttribute("formModel", new TemplateCreateExtraDto());

    	//編集画面に遷移する
        return "edit";
    }



    /**
     * 編集画面への遷移
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Model model, @RequestParam("id") int id) {

    	//コードマスタを渡す
    	model.addAttribute("codeMst", codeMstDao.findAll());

    	//DBから情報を取得しDtoに格納
		TemplateCreateDto dto = templateCreateDao.findById(id);

    	//Dtoを渡す
    	model.addAttribute("formModel", dto);

    	//編集画面に遷移する
        return "edit";
    }


    /**
     * 詳細画面への遷移
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(Model model, @RequestParam("id") int id) {

//    	System.out.println(id);

    	//DBから情報を取得しDtoに格納
		TemplateCreateExtraDto dto = templateCreateExtraDao.findById(id);

    	//Dtoを渡す
    	model.addAttribute("dto", dto);

    	//次回作成時のISSUE名
    	CalenderUtil calenderUtil = null;

		if(dto.getIssueDateDetail() > 31) {
			// 営業日計算ありの場合
	      	LocalDate today = LocalDate.now();
			ArrayList<String> holidayList = getHolidayList(today);
			calenderUtil = new CalenderUtil(holidayList);

		} else {
			// 営業日計算なしの場合
			calenderUtil = new CalenderUtil(null);
		}

		if(dto.getIssueDate() != 0) {
	    	LocalDate nextIssueDate = calenderUtil.getIssueDate(dto.getNextCreateDate(), dto.getIssueDate(), dto.getIssueDateDetail());
	    	String nextCreateIssueName = calenderUtil.convertIssueTitle(dto.getIssueTitle(), nextIssueDate);
	    	model.addAttribute("nextCreateIssueName", nextCreateIssueName);
		} else {
			model.addAttribute("nextCreateIssueName", dto.getIssueTitle());
		}

    	//編集画面に遷移する
        return "detail";
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
     *  更新処理
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("formModel") TemplateCreateDto formModel, Model model, @RequestParam("projectName") String projectName) {

    	//次回作成日を計算
    	CalenderUtil calenderUtil = new CalenderUtil(null);
    	LocalDate baseDate = LocalDate.now();
//    	LocalDate nextCreateDate = calenderUtil.getNextCreateDate(baseDate , formModel.getCreateTerms(), formModel.getCreateTermsDetail());
    	LocalDate nextCreateDate = calenderUtil.getThisCreateDate(baseDate , formModel.getCreateTerms(), formModel.getCreateTermsDetail());
    	formModel.setNextCreateDate(nextCreateDate);

    	//更新の場合
    	if(formModel.getId() != 0) {
        	//現在値を設定
        	TemplateCreateDto nowDto = templateCreateDao.findById(formModel.getId());
        	formModel.setBeforeSuccessFlg(nowDto.isBeforeSuccessFlg());
        	formModel.setBeforeCreateDate(nowDto.getBeforeCreateDate());
    	}

    	//更新日時を設定
    	formModel.setUpdateDateTime(LocalDateTime.now());

//    	System.out.println(formModel);

    	//DB更新
    	TemplateCreateDto ret = templateCreateDao.save(formModel);

    	//プロジェクト名を登録
    	if(!projectName.equals("")) {

    		ProjectDto projectDto = new ProjectDto();
    		projectDto.setId(formModel.getProjectId());
    		projectDto.setName(projectName);
    		ProjectDto saveResult = projectDao.save(projectDto);
    		System.out.println("プロジェクト名を更新 id:" + saveResult.getId() + ", name:" + saveResult.getName());
    	}

    	//メッセージを追加
        model.addAttribute("message", "「" + ret.getIssueTitle() + "」を登録しました");

    	//トップページに遷移する
    	return "redirect:/plan";
	}



    /**
     * 削除処理
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(Model model, @RequestParam("id") int id) {

    	System.out.println("削除ID：" + id);

    	//DB更新
    	templateCreateDao.deleteById(id);

    	//メッセージを追加
        model.addAttribute("message", "削除しました");

    	//トップページに遷移する
    	return "redirect:/plan";
    }


}
