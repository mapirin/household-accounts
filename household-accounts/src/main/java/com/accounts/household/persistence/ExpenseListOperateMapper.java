package com.accounts.household.persistence;

import java.util.List;

import org.apache.ibatis.annotations.MapKey;

import com.accounts.household.domain.DepositDetailInfo;
import com.accounts.household.domain.DepositInfo;
import com.accounts.household.domain.SpendingDetailInfo;
import com.accounts.household.domain.SpendingInfo;

public interface ExpenseListOperateMapper {

	public Integer userInfoTableSelectId(String userName);

	public List<DepositInfo> depositInfoTableAllData(String year, String month, Integer userId);

	public List<SpendingInfo> spendingInfoTableAllData(String year, String month, Integer userId);

	public String methodInfoTableSelectName(Integer methodId);

	public String categoryInfoTableSelectName(Integer categoryId);

	public Integer depositInfoTableSumAmount(String year, String month,Integer userId);

	public Integer spendingInfoTableSumAmount(String year, String month,Integer userId);

	public List<DepositInfo> depositInfoTableDeleteDataConf(Integer depositId);

	public List<SpendingInfo> spendingInfoTableDeleteDataConf(Integer spendingId);

	public void depositInfoTableDataDelete(Integer depositId);

	public void spendingInfoTableDataDelete(Integer spendingId);

	@MapKey("categoryName")
	public List<DepositDetailInfo> depositPiechartSelectData(String year, String month, Integer userId);
	
	@MapKey("categoryName")
	public List<SpendingDetailInfo> spendingPiechartSelectData(String year, String month, Integer userId);
}
