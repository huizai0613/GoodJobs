package cn.goodjobs.applyjobs.bean;

import java.util.List;

public class JobfairGroup {
	
	private String name;
	private String catalogID;
    private List<JobFairChild> list;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCatalogID() {
		return catalogID;
	}
	public void setCatalogID(String catalogID) {
		this.catalogID = catalogID;
	}
	public List<JobFairChild> getList() {
		return list;
	}
	public void setList(List<JobFairChild> list) {
		this.list = list;
	}
}
