package com.example.demo.templateCreate;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryDao extends JpaRepository<SummaryDto, Integer> {

	  /**
	   * dateの期間をキーに検索する
	   */
	  List<SummaryDto> findAllByDateBetween(LocalDate startDate,LocalDate endDate);
}