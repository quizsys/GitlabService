package com.example.demo.burnDown;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BurnDownDao extends JpaRepository<BurnDownDto, Integer> {

	  /**
	   * idをキーに検索する
	   */
	  @Query(value = "select * from burndown where milestone = :milestone and label = :label_name", nativeQuery = true)
	  List<BurnDownDto> findByMilestoneAndLabel(@Param("milestone") String milestone, @Param("label_name") String label);
}