package com.chinamworld.bocmbci.biz.remittance.adapter.model;

public class RemittanceCollectionBankItem {
	
	public String countriesRegions;
	public String cBankName;
	public String eBankName;
	public String swift;
	public String note;
	
	public RemittanceCollectionBankItem() {}

	public RemittanceCollectionBankItem(String countriesRegions,
			String cBankName, String eBankName, String swift,String note) {
		super();
		this.countriesRegions = countriesRegions;
		this.cBankName = cBankName;
		this.eBankName = eBankName;
		this.swift = swift;
		this.note = note;
	}
	
	public RemittanceCollectionBankItem(String countriesRegions,
			String cBankName, String eBankName, String swift) {
		super();
		this.countriesRegions = countriesRegions;
		this.cBankName = cBankName;
		this.eBankName = eBankName;
		this.swift = swift;
		
	}
	
	

}
