package kr.spring.batch.chapter05.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Spring ORM에서 제공하는 {@link HibernateJpaDialect}가 IsolationLevel을 지원하지 않아 만들었습니다.
 * <p/>
 * 참고: http://stackoverflow.com/questions/5234240/hibernatespringjpaisolation-does-not-work
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 6:42
 */
@Slf4j
public class IsolationSupportHibernateJpaDialect extends HibernateJpaDialect {

    ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>();
    ThreadLocal<Integer> originalIsolation = new ThreadLocal<Integer>();

    @Override
    public Object beginTransaction(EntityManager entityManager,
                                   TransactionDefinition definition)
        throws PersistenceException, SQLException, TransactionException {

        Connection connection = getJdbcConnection(entityManager, definition.isReadOnly()).getConnection();
        connectionThreadLocal.set(connection);
        Integer isolation = DataSourceUtils.prepareConnectionForTransaction(connection, definition);
        originalIsolation.set(isolation);

        entityManager.getTransaction().begin();
        return prepareTransaction(entityManager, definition.isReadOnly(), definition.getName());
    }

    @Override
    public void cleanupTransaction(Object transactionData) {
        try {
            super.cleanupTransaction(transactionData);
            DataSourceUtils.resetConnectionAfterTransaction(connectionThreadLocal.get(),
                                                            originalIsolation.get());
        } finally {
            connectionThreadLocal.remove();
            originalIsolation.remove();
        }
    }

    private static final long serialVersionUID = -5017418689497335313L;
}
