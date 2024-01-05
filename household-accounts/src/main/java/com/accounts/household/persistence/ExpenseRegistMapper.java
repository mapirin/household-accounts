package com.accounts.household.persistence;

import java.util.List;

import com.accounts.household.domain.CategoryInfo;
import com.accounts.household.domain.DepositInfo;
import com.accounts.household.domain.SpendingInfo;

public interface ExpenseRegistMapper {
	// セレクトボックスに追加する値取得
	public List<String> methodInfoTableSelectName();

	public List<CategoryInfo> categoryInfoTableSelectData();

	// ID取得
	public Integer methodInfoTableSelectId(String methodName);

	public Integer categoryInfoTableSelectId(Integer categoryType, String categoryName);

	public Integer userInfoTableSelectId(String userName);

	// 入出金データ登録
	public void depositInfoTableInsertData(DepositInfo depositInfo);

	public void spendingInfoTableInsertData(SpendingInfo spendingInfo);
}
