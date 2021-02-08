package com.example.demo.templateCreate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateCreateExtraDao extends JpaRepository<TemplateCreateExtraDto, Integer> {

	@Query(value = "SELECT t.*,"
			+ "p.name as project_name,"
			+ "c1.name as create_terms_str,"
			+ "c2.name as create_terms_detail_str,"
			+ "c3.name as issue_date_str,"
			+ "c4.name as issue_date_detail_str,"
		    + "CASE WHEN t.before_create_date is null THEN '-' ELSE CONCAT(t.before_create_date, CASE WHEN before_success_flg THEN ' 成功' ELSE ' 失敗'END) END as before_result"
			+ "  from templatecreate as t"
			+ " join project as p  on p.id    = t.project_id"
			+ " join codemst as c1 on c1.kbn1 = 1 and c1.id = t.create_terms"
			+ " join codemst as c2 on c2.kbn1 = 2 and c2.kbn2 = t.create_terms and c2.id = t.create_terms_detail"
			+ " join codemst as c3 on c3.kbn1 = 3 and c3.kbn2 = t.create_terms and c3.id = t.issue_date"
			+ " left outer join codemst as c4 on c4.kbn1 = 4 and c4.kbn2 = t.create_terms and c4.id = t.issue_date_detail;" , nativeQuery = true)
	List<TemplateCreateExtraDto> findAllExtra();


	@Query(value = "SELECT t.*,"
			+ "p.name as project_name,"
			+ "c1.name as create_terms_str,"
			+ "c2.name as create_terms_detail_str,"
			+ "c3.name as issue_date_str,"
			+ "c4.name as issue_date_detail_str,"
		    + "CASE WHEN t.before_create_date is null THEN '-' ELSE CONCAT(t.before_create_date, CASE WHEN before_success_flg THEN ' 成功' ELSE ' 失敗'END) END as before_result"
			+ "  from templatecreate as t"
			+ " join project as p  on p.id    = t.project_id"
			+ " join codemst as c1 on c1.kbn1 = 1 and c1.id = t.create_terms"
			+ " join codemst as c2 on c2.kbn1 = 2 and c2.kbn2 = t.create_terms and c2.id = t.create_terms_detail"
			+ " join codemst as c3 on c3.kbn1 = 3 and c3.kbn2 = t.create_terms and c3.id = t.issue_date"
			+ " left outer join codemst as c4 on c4.kbn1 = 4 and c4.kbn2 = t.create_terms and c4.id = t.issue_date_detail"
			+ " where t.id = :id" , nativeQuery = true)
	TemplateCreateExtraDto findById(@Param("id") int id);


}
