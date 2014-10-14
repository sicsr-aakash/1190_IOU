package com.project.iou;

public class Model {
	
	int id;
	String name;
	String debit;
	String debitDescription;
	String credit;
	String creditDescription;
	
	public Model() {}
	
	public Model(String name, String debit, String debitDescription,
			String credit, String creditDescription) {
		super();
		this.name = name;
		this.debit = debit;
		this.debitDescription = debitDescription;
		this.credit = credit;
		this.creditDescription = creditDescription;
	}
	
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
	
	public String getDebit() {
		return debit;
	}
	public void setDebit(String debit) {
		this.debit = debit;
	}
	
	public String getDebitDescription() {
		return debitDescription;
	}
	public void setDebitDescription(String debitDescription) {
		this.debitDescription = debitDescription;
	}
	
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	
	public String getCreditDescription() {
		return creditDescription;
	}
	public void setCreditDescription(String creditDescription) {
		this.creditDescription = creditDescription;
	}
	
	@Override
	public String toString() {
		return "Model [id=" + id + ", name=" + name + ", debit=" + debit
				+ ", debitDescription=" + debitDescription + ", credit="
				+ credit + ", creditDescription=" + creditDescription + "]";
	}
	
}
