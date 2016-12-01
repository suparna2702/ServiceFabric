package com.similan.domain.entity.acccount;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name="AccountRecord")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "AccountRecordDiscriminatorType", 
                discriminatorType = DiscriminatorType.STRING)
public class AccountRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column
	private AccountRecordState accountRecordState;
	
	@Column
	private Date timeStamp;
	
	@Column
	private float transactionAmount;
	
	@Enumerated(EnumType.STRING)
	@Column
	private AccountRecordType recordType;
	
	public AccountRecordType getRecordType() {
		return recordType;
	}

	public void setRecordType(AccountRecordType recordType) {
		this.recordType = recordType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public float getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(float transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public AccountRecordState getAccountRecordState() {
		return accountRecordState;
	}

	public void setAccountRecordState(AccountRecordState accountRecordState) {
		this.accountRecordState = accountRecordState;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
