package com.example.demo.templateCreate;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDao extends JpaRepository<ProjectDto, Integer> {}