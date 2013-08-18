package kr.spring.batch.chapter14.batch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * kr.spring.batch.chapter14.batch.ImportValidator
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 9:31
 */
public class ImportValidator implements JobParametersValidator, ResourceLoaderAware {

	public static final String PARAM_INPUT_RESOURCE = "inputResource";
	public static final String PARAM_REPORT_RESOURCE = "reportResource";
	private ResourceLoader resourceLoader;

	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		List<String> missing = new ArrayList<String>();
		checkParameter(PARAM_INPUT_RESOURCE, parameters, missing);
		checkParameter(PARAM_REPORT_RESOURCE, parameters, missing);

		if (!missing.isEmpty()) {
			throw new JobParametersInvalidException("Missing parameters: " + missing);
		}
		if (!resourceLoader.getResource(parameters.getString(PARAM_INPUT_RESOURCE)).exists()) {
			throw new JobParametersInvalidException(
					"The input file: " + parameters.getString(PARAM_INPUT_RESOURCE) + " does not exist");
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	private void checkParameter(String key, JobParameters parameters, List<String> missing) {
		if (parameters.getParameters().containsKey(key)) {
			missing.add(key);
		}
	}
}
