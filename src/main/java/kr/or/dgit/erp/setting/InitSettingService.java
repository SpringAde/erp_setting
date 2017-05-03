package kr.or.dgit.erp.setting;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.ShutdownChannelGroupException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class InitSettingService {

	public void initSetting() {
		try {
			Dao dao = Dao.getInstance();
			dao.getUpdateResult("Drop Database if exists " + Config.DB_NAME);
			System.out.println("Drop Database if exists " + Config.DB_NAME);

			dao.getUpdateResult("Create Database " + Config.DB_NAME);
			System.out.println("Create  Database " + Config.DB_NAME);

			dao.getUpdateResult("Use " + Config.DB_NAME);
			System.out.println("Use " + Config.DB_NAME);

			for (int i = 0; i < Config.CREATE_SQL_TABLE.length; i++) {
				dao.getUpdateResult(Config.CREATE_SQL_TABLE[i]);
				//System.err.println(Config.TABLE_NAME[i] + "Table 생성완료");
			}
			dao.getUpdateResult(Config.CREATE_USER);
			dao.getUpdateResult(Config.CREATE_GRANT);
			System.out.println(Config.USER + " 생성완료");
			JOptionPane.showMessageDialog(null, "초기화 완료");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		File buFile = new File(Config.EXPORT_DIR); // BackupFiles폴더
		File[] buFiles = buFile.listFiles(); // 배열에 BackupFiles파일 넣기
		try {
			if (buFiles.length < 1) {
			}
			initSetting();
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "복원 파일이 없습니다");
		}
	}

	public void restore() {
		for (int i = 0; i < Config.TABLE_NAME.length; i++) {
			loadTableData(i); // BackupFiles의 파일을 가져와 테이블에 넣기
		}
		JOptionPane.showMessageDialog(null, "데이터 복원 완료");
	}

	public void backUp() {
		File buFile = new File(Config.EXPORT_DIR);
		File[] buFiles = buFile.listFiles(); 
		if (buFile.exists() == false) {
			buFile.mkdir(); // 폴더 존재여부 확인 후 없으면 폴더생성
		}else{
			for (File f : buFiles) {
				if (f.exists()) {
					f.delete();
				}
			}

		}
		for (int i = 0; i < Config.CREATE_SQL_TABLE.length; i++) {
			BackupTableData(i); // BackupFiles에 있는 파일안의 데이터를 가져와 DB테이블에 삽입
		}		
		JOptionPane.showMessageDialog(null, "데이터 백업 완료");
	}

	private void loadTableData(int tables) {// 파일 복원
		File file = new File(Config.IMPORT_DIR + Config.TABLE_NAME[tables]+".txt");
		String sql = "load data local infile '%s' " + "into table " + Config.TABLE_NAME[tables] + " "
				+ "character set 'UTF8' " + "fields terminated by ',' " + "lines terminated by '\n'";

		executeImportData(String.format(sql, file.getAbsolutePath().replace("\\", "/")), file.getName());
	}

	public void BackupTableData(int tables) {// 파일 백업
		String sql = "select * from " + Config.TABLE_NAME[tables];
		Connection con = DBCon.getConnection(Config.URL + Config.DB_NAME, Config.ROOT_USER, Config.ROOT_PWD);
		try (PreparedStatement pstmt = con.prepareStatement(sql); ResultSet rs = pstmt.executeQuery();) {
			StringBuilder sb = new StringBuilder();
			int colCnt = rs.getMetaData().getColumnCount();
			int count = 0;
			while (rs.next()) {
				for (int i = 1; i <= colCnt; i++) {
					Object obj = rs.getObject(i);
					if (obj.equals(true)) {
						obj = 1;
					} else if (obj.equals(false)) {
						obj = 0;
					}
					sb.append(obj + ",");
					
				}
				sb.replace(sb.length() - 1, sb.length(), "");
				sb.append("\n");
				count++;
			}
			System.out.printf("Export Table(%s) %d Rows Success! %n", Config.TABLE_NAME[tables] + ".txt", count);
			try (BufferedOutputStream bw = new BufferedOutputStream(
					new FileOutputStream(Config.EXPORT_DIR + Config.TABLE_NAME[tables] + ".txt"));
					OutputStreamWriter osw = new OutputStreamWriter(bw, "UTF-8")) {
				osw.write(sb.toString());				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void executeImportData(String sql, String tableName) {
		Statement stmt = null;
		try {
			Connection con = DBCon.getConnection(Config.URL + Config.DB_NAME, Config.ROOT_USER, Config.ROOT_PWD);
			stmt = con.createStatement();
			stmt.execute(sql);
			//System.out.println(sql);			
			System.out.printf("Import Table(%s) %d Rows Success! %n", tableName, stmt.getUpdateCount());
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062) {
				System.err.println("중복데이터 존재");
			}
			e.printStackTrace();
		} finally {
			JdbcUtil.close(stmt);
		}
	}
}