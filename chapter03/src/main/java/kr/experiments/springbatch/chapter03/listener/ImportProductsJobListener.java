package kr.experiments.springbatch.chapter03.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * kr.experiments.springbatch.chapter03.listener.ImportProductsJobListener
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 11:44
 */
@Slf4j
public class ImportProductsJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("작업 전에 호출되는 리스너의 메소드입니다.");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("작업 결과 상세. 시작=[{}], 완료=[{}], ExitStatus=[{}]",
                 jobExecution.getStartTime(), jobExecution.getEndTime(), jobExecution.getExitStatus());

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("작업이 성공적으로 완료되었습니다.");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.info("작업이 실패했습니다.");
        }
    }
}
