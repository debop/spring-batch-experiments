package kr.experiments.springbatch.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Job 수행 전/후의 입력값과 결과를 로그에 기록하는 Listener 입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 30. 오후 11:01
 */
public class JobLoggerListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobLoggerListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.debug("잡 [{}]을 실행합니다...인자=[{}]",
                  jobExecution.getJobInstance().getJobName(), jobExecution.getJobParameters());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        log.debug("잡 [{}]을 종료했습니다. 종료 상태=[{}]",
                  jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());

    }
}
