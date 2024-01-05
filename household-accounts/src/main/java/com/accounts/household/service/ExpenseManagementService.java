package com.accounts.household.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accounts.household.domain.CategoryInfo;
import com.accounts.household.domain.CsvDataInfo;
import com.accounts.household.domain.DepositDetailInfo;
import com.accounts.household.domain.DepositInfo;
import com.accounts.household.domain.MethodInfo;
import com.accounts.household.domain.SpendingDetailInfo;
import com.accounts.household.domain.SpendingInfo;
import com.accounts.household.persistence.ExpenseListOperateMapper;
import com.accounts.household.persistence.ExpenseRegistMapper;
import com.accounts.household.persistence.ExpenseUserMapper;
import com.accounts.household.web.expense_regist.DepositManagementForm;
import com.accounts.household.web.expense_regist.SpendingManagementForm;
import com.accounts.household.web.list_operate.DepositExpenseListInfo;
import com.accounts.household.web.list_operate.SpendingExpenseListInfo;

/**
 * 値やDBから抽出したデータを加工しコントローラに渡すサービスクラス
 * 
 * @author neo-matrix506
 *
 */
@Service
public class ExpenseManagementService {

	@Autowired
	private ExpenseRegistMapper expenseRegistMapper;

	@Autowired
	private ExpenseListOperateMapper expenseListOperateMapper;

	@Autowired
	private ExpenseUserMapper expenseUserMapper;

	//
	//
	public boolean getUserInfoTableUserExistCheck(String userName) {
		Integer existCount = expenseUserMapper.userInfoTableUserExistCheck(userName);

		if (existCount > 0) {
			return false;
		}
		return true;
	}

	// ユーザ登録
	public void insertUserInfoTableData(String userName, String password) {
		expenseUserMapper.userInfoTableInsertData(userName, password);
	}

	//
	// ログイン
	public boolean getUserInfoTableSelectUserData(String userName, String password) {
		Integer userDataCount = expenseUserMapper.userInfoTableSelectUserData(userName, password);

		if (userDataCount == 0) {
			return false;
		} else {
			return true;
		}
	}

	//
	// ユーザId取得
	public Integer getUserInfoTableSelectId(String userName) {
		return expenseListOperateMapper.userInfoTableSelectId(userName);
	}

	//
	// よく出てきた処理をまとめた
//	public 

	//
	// セレクトボックスに追加する値取得
	public List<String> getMethodInfoTableSelectName() {
		return expenseRegistMapper.methodInfoTableSelectName();
	}

	public List<String> getCategoryInfoTableDepositData() {
		List<String> depositCategoryList = new ArrayList<>();

		for (CategoryInfo info : expenseRegistMapper.categoryInfoTableSelectData()) {
			if (info.getCategoryType() == 0) {
				depositCategoryList.add(info.getCategoryName());
			}
		}
		return depositCategoryList;
	}

	public List<String> getCategoryInfoTableSpendingData() {
		List<String> spendingCategoryList = new ArrayList<>();

		for (CategoryInfo info : expenseRegistMapper.categoryInfoTableSelectData()) {
			if (info.getCategoryType() == 1) {
				spendingCategoryList.add(info.getCategoryName());
			}
		}
		return spendingCategoryList;
	}

	//
	// 入金データ登録
	@Transactional
	public void getDepositInfoTableInsertData(DepositManagementForm depositForm, String userName) {
		DepositInfo depositInfo = new DepositInfo();

		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setMethodName(depositForm.getDepositMethod());
		CategoryInfo categoryInfo = new CategoryInfo();
		categoryInfo.setCategoryType(0);
		categoryInfo.setCategoryName(depositForm.getDepositCategory());

		// ID取得
		Integer methodId = expenseRegistMapper.methodInfoTableSelectId(methodInfo.getMethodName());
		Integer categoryId = expenseRegistMapper.categoryInfoTableSelectId(categoryInfo.getCategoryType(),
				categoryInfo.getCategoryName());
		Integer userId = expenseRegistMapper.userInfoTableSelectId(userName);

		BeanUtils.copyProperties(depositForm, depositInfo);
		depositInfo.setDepositMethodId(methodId);
		depositInfo.setDepositCategoryId(categoryId);
		depositInfo.setUserId(userId);

		expenseRegistMapper.depositInfoTableInsertData(depositInfo);
	}

	// 支出データ登録
	@Transactional
	public void getSpendingInfoTableInsertData(SpendingManagementForm spendingForm, String userName) {
		SpendingInfo spendingInfo = new SpendingInfo();

		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setMethodName(spendingForm.getSpendingMethod());
		CategoryInfo categoryInfo = new CategoryInfo();
		categoryInfo.setCategoryType(１);
		categoryInfo.setCategoryName(spendingForm.getSpendingCategory());
		// ID取得
		Integer methodId = expenseRegistMapper.methodInfoTableSelectId(methodInfo.getMethodName());
		Integer categoryId = expenseRegistMapper.categoryInfoTableSelectId(categoryInfo.getCategoryType(),
				categoryInfo.getCategoryName());
		Integer userId = expenseRegistMapper.userInfoTableSelectId(userName);

		BeanUtils.copyProperties(spendingForm, spendingInfo);
		spendingInfo.setSpendingMethodId(methodId);
		spendingInfo.setSpendingCategoryId(categoryId);
		spendingInfo.setUserId(userId);

		expenseRegistMapper.spendingInfoTableInsertData(spendingInfo);
	}

	//
	// 入金データ一覧検索表示
	public List<DepositExpenseListInfo> getDepositInfoTableAllData(String year, String month, String userName) {

		List<DepositExpenseListInfo> depositDataList = new ArrayList<>();
		Integer userId = expenseListOperateMapper.userInfoTableSelectId(userName);

		for (DepositInfo info : expenseListOperateMapper.depositInfoTableAllData(year, month, userId)) {
			DepositExpenseListInfo listInfo = new DepositExpenseListInfo();

			listInfo.setDepositId(info.getDepositId());
			listInfo.setDepositYear(info.getDepositYear());
			listInfo.setDepositMonth(info.getDepositMonth());
			listInfo.setDepositDay(info.getDepositDay());
			listInfo.setDepositAmount(info.getDepositAmount());
			listInfo.setDepositCommaAmount(String.format("%,d", info.getDepositAmount()));
			// methodNameを取得
			listInfo.setDepositMethod(expenseListOperateMapper.methodInfoTableSelectName(info.getDepositMethodId()));
			// categoryNameを取得
			listInfo.setDepositCategory(
					expenseListOperateMapper.categoryInfoTableSelectName(info.getDepositCategoryId()));
			listInfo.setDepositDetail(info.getDepositDetail());

			depositDataList.add(listInfo);
		}

		return depositDataList;
	}

	// 支出データ一覧検索表示
	public List<SpendingExpenseListInfo> getSpendingInfoTableAllData(String year, String month, String userName) {

		List<SpendingExpenseListInfo> spendingDataList = new ArrayList<>();
		Integer userId = expenseListOperateMapper.userInfoTableSelectId(userName);

		for (SpendingInfo info : expenseListOperateMapper.spendingInfoTableAllData(year, month, userId)) {
			SpendingExpenseListInfo listInfo = new SpendingExpenseListInfo();

			listInfo.setSpendingId(info.getSpendingId());
			listInfo.setSpendingYear(info.getSpendingYear());
			listInfo.setSpendingMonth(info.getSpendingMonth());
			listInfo.setSpendingDay(info.getSpendingDay());
			listInfo.setSpendingAmount(info.getSpendingAmount());
			listInfo.setSpendingCommaAmount(String.format("%,d", info.getSpendingAmount()));
			// methodNameを取得
			listInfo.setSpendingMethod(expenseListOperateMapper.methodInfoTableSelectName(info.getSpendingMethodId()));
			// categoryNameを取得
			listInfo.setSpendingCategory(
					expenseListOperateMapper.categoryInfoTableSelectName(info.getSpendingCategoryId()));
			listInfo.setSpendingDetail(info.getSpendingDetail());

			spendingDataList.add(listInfo);
		}

		return spendingDataList;
	}

	//
	// 合計額を取得
	public Integer getDepositInfoTableSumAmount(String year, String month, Integer userId) {
		Integer depositSumAmount = expenseListOperateMapper.depositInfoTableSumAmount(year, month, userId);
		if (depositSumAmount == null) {
			depositSumAmount = 0;
		}
		return depositSumAmount;
	}

	public Integer getSpendingInfoTableSumAmount(String year, String month, Integer userId) {
		Integer spendingSumAmount = expenseListOperateMapper.spendingInfoTableSumAmount(year, month, userId);
		if (spendingSumAmount == null) {
			spendingSumAmount = 0;
		}
		return spendingSumAmount;
	}

	public Integer getSumAmount(String year, String month, Integer userId) {
		Integer depositSumAmount = getDepositInfoTableSumAmount(year, month, userId);
		Integer spendingSumAmount = getSpendingInfoTableSumAmount(year, month, userId);

		Integer sumAmount = 0;
		if (depositSumAmount != 0 && spendingSumAmount != 0) {
			sumAmount = depositSumAmount - spendingSumAmount;
		} else if (depositSumAmount != 0 && spendingSumAmount == 0) {
			sumAmount = depositSumAmount;
		} else if (depositSumAmount == 0 && spendingSumAmount != 0) {
			sumAmount = spendingSumAmount;
		}
		return sumAmount;
	}

	//
	// 削除確認画面
	public List<DepositExpenseListInfo> getDepositInfoTableDeleteDataConf(Integer depositId) {

		List<DepositExpenseListInfo> depositDeleteConfList = new ArrayList<>();

		for (DepositInfo info : expenseListOperateMapper.depositInfoTableDeleteDataConf(depositId)) {
			DepositExpenseListInfo listInfo = new DepositExpenseListInfo();
			listInfo.setDepositYear(info.getDepositYear());
			listInfo.setDepositMonth(info.getDepositMonth());
			listInfo.setDepositDay(info.getDepositDay());
			listInfo.setDepositAmount(info.getDepositAmount());
			listInfo.setDepositMethod(expenseListOperateMapper.methodInfoTableSelectName(info.getDepositMethodId()));
			listInfo.setDepositCategory(
					expenseListOperateMapper.categoryInfoTableSelectName(info.getDepositCategoryId()));
			listInfo.setDepositDetail(info.getDepositDetail());

			depositDeleteConfList.add(listInfo);
		}

		return depositDeleteConfList;
	}

	public List<SpendingExpenseListInfo> getSpendingInfoTableDeleteDataConf(Integer spendingId) {

		List<SpendingExpenseListInfo> spendingDeleteConfList = new ArrayList<>();

		for (SpendingInfo info : expenseListOperateMapper.spendingInfoTableDeleteDataConf(spendingId)) {
			SpendingExpenseListInfo listInfo = new SpendingExpenseListInfo();
			listInfo.setSpendingYear(info.getSpendingYear());
			listInfo.setSpendingMonth(info.getSpendingMonth());
			listInfo.setSpendingDay(info.getSpendingDay());
			listInfo.setSpendingAmount(info.getSpendingAmount());
			listInfo.setSpendingMethod(expenseListOperateMapper.methodInfoTableSelectName(info.getSpendingMethodId()));
			listInfo.setSpendingCategory(
					expenseListOperateMapper.categoryInfoTableSelectName(info.getSpendingCategoryId()));
			listInfo.setSpendingDetail(info.getSpendingDetail());

			spendingDeleteConfList.add(listInfo);
		}

		return spendingDeleteConfList;
	}

	//
	// 削除処理
	@Transactional
	public void deleteDepositInfoTableData(Integer depositId) {
		expenseListOperateMapper.depositInfoTableDataDelete(depositId);
	}

	@Transactional
	public void deleteSpendingInfoTableData(Integer spendingId) {
		expenseListOperateMapper.spendingInfoTableDataDelete(spendingId);
	}

	//
	// 入金詳細表示
	public Map<String, Integer> depositCategoryAndAmountSelectData(String year, String month, String userName) {

		Integer userId = expenseListOperateMapper.userInfoTableSelectId(userName);
		List<DepositDetailInfo> depositList = expenseListOperateMapper.depositPiechartSelectData(year, month, userId);

		Map<String, Integer> map = new HashMap<>();
		for (DepositDetailInfo info : depositList) {
			map.putIfAbsent(info.getCategoryName(), info.getDepositAmount());
		}
		return map;
	}

	// 支出詳細表示
	public Map<String, Integer> spendingCategoryAndAmountSelectData(String year, String month, String userName) {

		Integer userId = expenseListOperateMapper.userInfoTableSelectId(userName);
		List<SpendingDetailInfo> spendingList = expenseListOperateMapper.spendingPiechartSelectData(year, month,
				userId);

		Map<String, Integer> map = new HashMap<>();
		for (SpendingDetailInfo info : spendingList) {
			map.putIfAbsent(info.getCategoryName(), info.getSpendingAmount());
		}
		return map;
	}

	// TODO
	/**
	 * インポートしたファイルを読み込み、CSVデータ格納用Beanを介してListにデータを格納し、返す
	 * @return csvDataList
	 */
	public List<CsvDataInfo> storeReadingCsvData(String userName) {
		String str = null;
		String[] strArray = null;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		List<CsvDataInfo> csvDataList = new ArrayList<>();

		try {
			fis = new FileInputStream("C:\\Users\\neo-matrix506\\Documents\\個人作業\\家計簿アプリ\\file.txt");
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			Integer userId = expenseRegistMapper.userInfoTableSelectId(userName);

			// インポートファイルの行ごとにデータが存在している場合、カンマごとにデータを分けて配列に格納し
			// 格納用Beanに格納後、リストに追加
			while ((str = br.readLine()) != null) {
				CsvDataInfo csvDataInfo = new CsvDataInfo();
				strArray = str.split(",", 7);

				csvDataInfo.setUserId(userId);
				csvDataInfo.setCsvDataYear(strArray[0]);
				csvDataInfo.setCsvDataMonth(strArray[1]);
				csvDataInfo.setCsvDataDay(strArray[2]);
				csvDataInfo.setCsvDataAmount(Integer.valueOf(strArray[3]));
				csvDataInfo.setCsvDataMethodId(Integer.valueOf(strArray[4]));
				csvDataInfo.setCsvDataCategoryId(Integer.valueOf(strArray[5]));
				csvDataInfo.setCsvDataDetail(strArray[6]);
				
				csvDataList.add(csvDataInfo);
			}
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ie) {
			}
			try {
				if (isr != null) {
					isr.close();
				}
			} catch (IOException ie) {
			}
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException ie) {
			}
		}
		return csvDataList;
	}
	
	/**
	 * csvDataListの各行を回して、フィールド値をdepositDataに格納して返す
	 * 
	 */
	public List<DepositInfo> storeCsvDataInfoToDepositInfo(List<CsvDataInfo> csvDataList) {
		List<DepositInfo> depositDataList = new ArrayList<>();
		for(CsvDataInfo csvDataInfo : csvDataList) {
			DepositInfo depositInfo = new DepositInfo();
			
			depositInfo.setUserId(csvDataInfo.getUserId());
			depositInfo.setDepositYear(csvDataInfo.getCsvDataYear());
			depositInfo.setDepositMonth(csvDataInfo.getCsvDataMonth());
			depositInfo.setDepositDay(csvDataInfo.getCsvDataDay());
			depositInfo.setDepositAmount(csvDataInfo.getCsvDataAmount());
			depositInfo.setDepositMethodId(csvDataInfo.getCsvDataMethodId());
			depositInfo.setDepositCategoryId(csvDataInfo.getCsvDataCategoryId());
			depositInfo.setDepositDetail(csvDataInfo.getCsvDataDetail());
			
			depositDataList.add(depositInfo);
		}
		return depositDataList;
	}
	/**
	 * csvDataListの各行を回して、フィールド値をspendingDataに格納して返す
	 * 
	 */
	public List<SpendingInfo> storeCsvDataInfoToSpendingInfo(List<CsvDataInfo> csvDataList) {
		List<SpendingInfo> spendingDataList = new ArrayList<>();
		for(CsvDataInfo csvDataInfo : csvDataList) {
			SpendingInfo spendingInfo = new SpendingInfo();
			
			spendingInfo.setUserId(csvDataInfo.getUserId());
			spendingInfo.setSpendingYear(csvDataInfo.getCsvDataYear());
			spendingInfo.setSpendingMonth(csvDataInfo.getCsvDataMonth());
			spendingInfo.setSpendingDay(csvDataInfo.getCsvDataDay());
			spendingInfo.setSpendingAmount(csvDataInfo.getCsvDataAmount());
			spendingInfo.setSpendingMethodId(csvDataInfo.getCsvDataMethodId());
			spendingInfo.setSpendingCategoryId(csvDataInfo.getCsvDataCategoryId());
			spendingInfo.setSpendingDetail(csvDataInfo.getCsvDataDetail());
			
			spendingDataList.add(spendingInfo);
		}
		return spendingDataList;
	}
	
	/**
	 * 
	 * 入金データ登録（CSV）
	 */
	@Transactional
	public void insertDepositInfoOfCsvData(DepositInfo depositInfo) {
		expenseRegistMapper.depositInfoTableInsertData(depositInfo);
	}
	/**
	 * 
	 * 出金データ登録（CSV）
	 */
	@Transactional
	public void insertSpendingInfoOfCsvData(SpendingInfo spendingInfo) {
		expenseRegistMapper.spendingInfoTableInsertData(spendingInfo);
	}
}