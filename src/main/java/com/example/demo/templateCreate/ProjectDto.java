package com.example.demo.templateCreate;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity                  /* エンティティクラス */
@Table(name="project")  /* テーブル名を定義する */
public class ProjectDto {

	@Id
	private int id;

	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
