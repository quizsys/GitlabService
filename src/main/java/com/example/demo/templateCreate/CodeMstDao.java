package com.example.demo.templateCreate;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeMstDao extends JpaRepository<CodeMstDto, Integer> {

}