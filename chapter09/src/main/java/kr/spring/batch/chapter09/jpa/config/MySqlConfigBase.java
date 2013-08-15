package kr.spring.batch.chapter09.jpa.config;

import javax.sql.DataSource;

/**
 * kr.spring.batch.chapter05.jpa.config.MySqlConfigBase
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 5:13
 */
public abstract class MySqlConfigBase extends JpaConfigBase {


	@Override
	public DataSource dataSource() {
		return getTomcatDataSource("com.mysql.jdbc.Driver",
		                           "jdbc:mysql://localhost/" + getDatabaseName(),
		                           "root",
		                           "root");
	}

	@Override
	public String getDialect() {
		return "org.hibernate.dialect.MySQL5InnoDBDialect";
	}
}
