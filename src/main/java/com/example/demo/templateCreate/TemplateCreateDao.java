package com.example.demo.templateCreate;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TemplateCreateDao extends JpaRepository<TemplateCreateDto, Integer> {

	List<TemplateCreateDto> findByNextCreateDate(LocalDate nextCreateDate);

	TemplateCreateDto findById(int id);

	@Modifying
	@Transactional
	void deleteById(int id);

}
