package kr.or.dgit.erp.setting;

public class Config {	
	public static final String DB_NAME = "ncs_erp_hiy";
	public static final String ROOT_USER="root";
	public static final String ROOT_PWD="rootroot";
	public static final String USER = "user_ncs";
	public static final String PWD = "user_ncs";	
	public static final String URL = "jdbc:mysql://localhost:3306/";
	public static final String DRIVER = "com.mysql.jdbc.Driver";

	public static final String[] TABLE_NAME = {"title", "department", "employee"};	

	public static final String[] CREATE_SQL_TABLE = {
			/* 직책 */
			"CREATE TABLE title ("
			+ "tcode INT(11)     NOT NULL, "
			+ "tname VARCHAR(10) null, "
			+ "PRIMARY KEY (tcode))",
			
			/* 부서 */
			"CREATE TABLE department ("
			+ "dcode INT(11)  NOT NULL, "
			+ "dname CHAR(10) NOT NULL, "
			+ "floor INT(11)  null, "
			+ "PRIMARY KEY (dcode))",
					
			/* 사원 */
			"CREATE TABLE employee ("
			+ "eno      INT(11)     NOT NULL, "
			+ "ename    VARCHAR(20) NOT NULL, "
			+ "salary   INT(11)     NULL, "
			+ "dno      INT(11)     NULL, "
			+ "gender   TINYINT(1)  NULL, "
			+ "joindate DATE        NULL, "
			+ "title    INT(11)     null, "
			+ "PRIMARY KEY (eno), "
			+ "FOREIGN KEY (dno) REFERENCES department (dcode) ON UPDATE CASCADE, "
			+ "FOREIGN KEY (title) REFERENCES title (tcode) ON UPDATE CASCADE)"	
	};
		
	// 유저 생성
	public static final String CREATE_USER=
			String.format("GRANT USAGE ON *.* TO '%s'@'localhost' IDENTIFIED BY '%s'", Config.USER, Config.USER);
	
	// 권한 부여
	public static final String CREATE_GRANT = 
			String.format("grant select, insert, delete, update on %s.* to '%s' identified by '%s'", 
						   DB_NAME, USER, PWD);
	

	public static final String EXPORT_DIR = System.getProperty("user.dir")+ "\\BackupFiles\\";	//백업
	public static final String IMPORT_DIR = System.getProperty("user.dir") + "\\DataFiles\\";	//복원
}
