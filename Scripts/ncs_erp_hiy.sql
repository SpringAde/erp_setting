-- ncs_erp_hiy
DROP SCHEMA IF EXISTS ncs_erp_hiy;

-- ncs_erp_hiy
CREATE SCHEMA ncs_erp_hiy;

-- 직책
CREATE TABLE ncs_erp_hiy.title (
	tcode INT(11)     NOT NULL, -- 직책번호
	tname VARCHAR(10) null,     -- 직책명
	PRIMARY KEY (tcode)		    -- 직책번호		
);


-- 사원
CREATE TABLE ncs_erp_hiy.employee (
	eno      INT(11)     NOT NULL, -- 사번
	ename    VARCHAR(20) NOT NULL, -- 사원명
	salary   INT(11)     NULL,     -- 급여
	dno      INT(11)     NULL,     -- 부서
	gender   TINYINT(1)  NULL,     -- 성별
	joindate DATE        NULL,     -- 입사일
	title    INT(11)     null,     -- 직책
	PRIMARY KEY (eno),			   -- 사번
	FOREIGN KEY (dno) REFERENCES ncs_erp_hiy.department (dcode),
	FOREIGN KEY (title) REFERENCES ncs_erp_hiy.title (tcode)
	on delete no action 
	on update cascade
);

-- 부서
CREATE TABLE ncs_erp_hiy.department (
	dcode INT(11)  NOT NULL, -- 부서번호
	dname CHAR(10) NOT NULL, -- 부서명
	floor INT(11)  null,     -- 위치
	PRIMARY KEY (dcode) -- 부서번호
);

-- table 존재 확인 (있으면 1)
SELECT 1
FROM Information_schema.tables 
WHERE table_name = 'title';

-- database 존재 확인
SELECT 1
FROM Information_schema.tables 
WHERE table_schema = 'ncs_erp_hiy';


-- Connection 상태 확인
show full processlist;

-- Connection 동작 확인
show status like 'Threads_connected';
show status like 'Threads_running';

SELECT 1 FROM Information_schema.SCHEMATA WHERE SCHEMA_NAME = 'ncs_erp_hiy';
SELECT 1 FROM Information_schema.tables WHERE TABLE_SCHEMA = 'ncs_erp_hiy' and table_name = 'department';
SELECT 1 FROM Information_schema.tables WHERE table_name = 'title';

use mysql;
select * from mysql.user;


select exists (SELECT 1 FROM Information_schema.tables WHERE TABLE_SCHEMA = 'ncs_erp_hiy' and table_name = 'department');
