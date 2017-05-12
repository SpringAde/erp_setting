package kr.or.dgit.erp.jUnitTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import kr.or.dgit.erp.setting.Config;
import kr.or.dgit.erp.setting.DBCon;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataBaseTest {

	private static Connection connection;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		connection = DBCon.getConnection(Config.URL, Config.ROOT_USER, Config.ROOT_PWD);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		connection = null;
	}

	@Test
	public void aTestDBConnection() {
		Assert.assertNotNull(connection);
	}

	@Test
	public void bTestDBExists() throws SQLException {
		String sql = String.format("SELECT 1 FROM Information_schema.SCHEMATA WHERE SCHEMA_NAME = '%s'", 
									Config.DB_NAME);
		int result = getQueryResult(sql);
		Assert.assertEquals(1, result);
	}

	@Test
	public void cTestEmployeeTableExists() throws SQLException {
		String sql = String.format("select exists (SELECT 1 FROM Information_schema.tables WHERE TABLE_SCHEMA = '%s' and table_name = '%s')",
									Config.DB_NAME, Config.TABLE_NAME[2]);
		int result = getQueryResult(sql);
		Assert.assertEquals(1, result);
	}

	@Test
	public void dTestDepartmentTableExists() throws SQLException {
		String sql = String.format("select exists (SELECT 1 FROM Information_schema.tables WHERE TABLE_SCHEMA = '%s' and table_name = '%s')",
									Config.DB_NAME, Config.TABLE_NAME[1]);
		int result = getQueryResult(sql);
		Assert.assertEquals(1, result);
	}

	@Test
	public void eTestTitleTableExists() throws SQLException {
		String sql = String.format("select exists (SELECT 1 FROM Information_schema.tables WHERE TABLE_SCHEMA = '%s' and table_name = '%s')",
									Config.DB_NAME, Config.TABLE_NAME[0]);
		int result = getQueryResult(sql);
		Assert.assertEquals(1, result);
	}

	@Test
	public void fTestUserExists() throws SQLException {
		String sql = String.format("select exists (select 1 from mysql.`user` where user = '%s')", Config.USER);
		int result = getQueryResult(sql);
		Assert.assertEquals(1, result);
	}

	private int getQueryResult(String sql) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		pstmt = connection.prepareStatement(sql);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			result = rs.getInt(1);
		}
		rs.close();
		pstmt.close();
		return result;
	}

}
