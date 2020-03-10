package com.example.jpaexample.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jpaexample.model.Employee;
import com.example.jpaexample.repository.EmployeeRepository;
import com.example.jpaexample.userdefinedexception.EmployeeNotFoundException;
import com.philips.services.iot.logger.Logger;
import com.philips.services.iot.logger.models.InfoCategory;
import com.philips.services.iot.logger.models.LogParamBuilder;
import com.philips.services.iot.logger.models.LogType;
import com.philips.services.iothub.commoncomponents.httpabstraction.application.HttpAbstractionFactoryImpl;
import com.philips.services.iothub.commoncomponents.httpabstraction.models.Context;
import com.philips.services.iothub.commoncomponents.httpabstraction.models.HttpStatusCode;
import com.philips.services.iothub.commoncomponents.httpabstraction.models.OperationResultBuilder;
import com.philips.services.iothub.commoncomponents.httpabstraction.models.ProxyDetails;
import com.philips.services.iothub.commoncomponents.httpabstraction.models.RequestAttributes;
import com.philips.services.iothub.commoncomponents.httpabstraction.service.HttpAbstraction;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	@Qualifier("HttpAbstractionFactoryProducerImpl")
	private HttpAbstractionFactoryImpl httpAbstractionFactoryImpl;

	@Autowired
	private QueueService queueService;

	@Autowired
	private ProxyDetails proxyDetails;

	@Autowired
	private Context context;

	@Autowired
	private RequestAttributes requestAttributes;

	@Autowired
	private OperationResultBuilder operationResultBuilder;

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public Employee addEmployee(Employee employee) {
		queueService.addSend(employee);
		return employeeRepository.save(employee);
	}

	@Transactional
	public Employee updateEmployee(Employee employee) throws EmployeeNotFoundException {
		Employee employee1 = getEmployeeByID(employee.getId());
		employee1.setAge(employee.getAge());
		employee1.setDepartment(employee.getDepartment());
		employee1.setFirstName(employee.getFirstName());
		employee1.setLastName(employee.getLastName());

		return employee1;
	}

	public boolean deleteEmployee(Employee employee) throws EmployeeNotFoundException {

		getEmployeeByID(employee.getId());
		employeeRepository.delete(employee);
		queueService.deleteSend(employee);
		return true;
	}

	public Employee getEmployeeByID(int id) throws EmployeeNotFoundException {

		Employee employee = null;
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		if (employeeOptional.isPresent()) {
			employee = employeeOptional.get();
		} else {
			throw new EmployeeNotFoundException("employee not found for this id:" + id);
		}
		return employee;
	}

	public String testAPI() {

		Logger.info(new LogParamBuilder().logType(LogType.DEVELOPER).correlationId("1234")
				.infoCategory(InfoCategory.INTERFACE).description("Inside test API creating request").build());

		HttpAbstraction httpAbstraction = httpAbstractionFactoryImpl.getHttpAbstractionInstance();

		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(URI.create("https://jsonplaceholder.typicode.com/todos/1")).GET().build();
		setRequestParams();
		try {
			HttpResponse<String> httpResponse = httpAbstraction.sendRequest(httpRequest, proxyDetails, context,
					requestAttributes);
			return ((operationResultBuilder.buildOperationResult(httpResponse, context)).getHttpResponseBody());

		} catch (IOException | InterruptedException | TimeoutException | ExecutionException e) {
			Logger.error(new LogParamBuilder().logType(LogType.DEVELOPER).correlationId("1234")
					.infoCategory(InfoCategory.EXCEPTION).description("IoTHttpNetworkException in API call ")
					.exception(e).build());

		}
		return null;

	}

	public void setRequestParams() {
		Logger.traceMethodStart(new LogParamBuilder().correlationId("1234").build());
		proxyDetails.setProxyPort("");
		proxyDetails.setProxyAddress("");
		List<Integer> httpcodesRetryList = new ArrayList<Integer>();
		httpcodesRetryList.add(HttpStatusCode.INTERNAL_SERVER_ERROR);
		requestAttributes.setHttpcodesRetryList(httpcodesRetryList);
		requestAttributes.setMaxNumOfRetries(3);
		requestAttributes.setSuccessHttpStatusCode(HttpStatusCode.SUCCESS);
		requestAttributes.setConnectionTimeOut("10000");
		context.setContextType("Fetching data");
		context.setSubContextType("Data from web");
		Logger.traceMethodExit(new LogParamBuilder().correlationId("1234").build());
	}
}
