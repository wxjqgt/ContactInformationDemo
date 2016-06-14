package com.weibo.baidumapdemo;

public class PersonVo {
	private String name;
	private String tel;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public PersonVo(String name, String tel) {
		super();
		this.name = name;
		this.tel = tel;
	}
	public PersonVo() {
		super();
	}
	@Override
	public String toString() {
		return "PersonVo [name=" + name + ", tel=" + tel + "]";
	}
	
}	
