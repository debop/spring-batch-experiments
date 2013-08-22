package kr.spring.batch.infrastructure.jpa;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.hibernate.ConnectionReleaseMode;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate3.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * JPA 환경설정
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 4:53
 */
@Slf4j
public abstract class JpaConfigBase {

	public String getDatabaseName() {
		return "hibernate";
	}

	public abstract String getDialect();

	public abstract String[] getMappedPackageNames();

	public String getPersistentUnitName() {
		return "";
	}

	public Properties jpaProperties() {
		Properties props = new Properties();

		props.put(Environment.HBM2DDL_AUTO, "create");    // create | spawn | spawn-drop | update | validate | none

		props.put(Environment.FORMAT_SQL, "true");
		props.put(Environment.SHOW_SQL, "true");
		props.put(Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.ON_CLOSE);
		props.put(Environment.AUTOCOMMIT, "true");
		props.put(Environment.STATEMENT_BATCH_SIZE, 100);
		props.put(Environment.DIALECT, getDialect());

		return props;
	}

	@Bean
	public DataSource dataSource() {
		return getTomcatDataSource("org.hsqldb.jdbcDriver",
		                           "jdbc:hsqldb:mem:" + getDatabaseName() + ";MVCC=TRUE;",
		                           "sa",
		                           "");
	}

	protected void setupEntityManagerFactory(LocalContainerEntityManagerFactoryBean factoryBean) {
		// 상속 받아서 정의해 주세요.
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		JpaConfigBase.log.info("EntityManagerFactory 를 생성합니다...");

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

		String[] packageNames = getMappedPackageNames();
		if (packageNames != null && packageNames.length > 0) {
			JpaConfigBase.log.info("JPA용 엔티티를 스캔합니다.");
			factoryBean.setPackagesToScan(packageNames);
		}
		if (!StringUtils.isEmpty(getPersistentUnitName()))
			factoryBean.setPersistenceUnitName(getPersistentUnitName());

		factoryBean.setJpaProperties(jpaProperties());
		factoryBean.setDataSource(dataSource());

		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setGenerateDdl(true);
		factoryBean.setJpaVendorAdapter(adapter);

		setupEntityManagerFactory(factoryBean);

		factoryBean.afterPropertiesSet();
		EntityManagerFactory factory = factoryBean.getObject();
		addEventListener(factory);

		return factory;
	}

	protected void addEventListener(EntityManagerFactory emf) {

		// HINT: Listener 를 등록하세요. ( hconnect-data 를 참고하세요 )

	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager(entityManagerFactory());
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}


	/**
	 * Tomcat DataSource 를 빌드합니다.
	 *
	 * @param driverClass the driver class
	 * @param url         the url
	 * @param username    the username
	 * @param passwd      the passwd
	 * @return the tomcat kr.hconnect.data source
	 */
	protected DataSource getTomcatDataSource(String driverClass, String url, String username, String passwd) {

		PoolProperties p = new PoolProperties();
		p.setUrl(url);
		p.setDriverClassName(driverClass);
		p.setUsername(username);
		p.setPassword(passwd);

		p.setJmxEnabled(true);
		p.setTestWhileIdle(true);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(200);
		p.setInitialSize(10);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(10);

		return new org.apache.tomcat.jdbc.pool.DataSource(p);
	}
}
