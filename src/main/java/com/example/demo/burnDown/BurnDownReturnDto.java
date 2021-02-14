package com.example.demo.burnDown;

import java.util.List;

public class BurnDownReturnDto {

	private List<BurnDownDto> burnDownList;

	private List<String> holidayList;

	public List<BurnDownDto> getBurnDownList() {
		return burnDownList;
	}

	public void setBurnDownList(List<BurnDownDto> burnDownList) {
		this.burnDownList = burnDownList;
	}

	public List<String> getHolidayList() {
		return holidayList;
	}

	public void setHolidayList(List<String> holidayList) {
		this.holidayList = holidayList;
	}

	@Override
	public String toString() {
		return "BurnDownReturnDto [burnDownList=" + burnDownList + ", holidayList=" + holidayList + "]";
	}

	public BurnDownReturnDto(List<BurnDownDto> burnDownList, List<String> holidayList) {
		super();
		this.burnDownList = burnDownList;
		this.holidayList = holidayList;
	}



}
