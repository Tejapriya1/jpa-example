package com.example.jpaexample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpaexample.model.Employee;
import com.example.jpaexample.service.EmployeeService;
import com.example.jpaexample.userdefinedexception.EmployeeNotFoundException;
import com.philips.services.iot.logger.Logger;
import com.philips.services.iot.logger.models.LogParamBuilder;


@RestController
@CrossOrigin
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/getall")
	public List<Employee> getAll() {
		Logger.traceMethodStart(new LogParamBuilder().correlationId("1234").build());
		return employeeService.getAllEmployees();
	}

	@PostMapping("/addEmployee")
	public Employee addEmployee(@RequestBody Employee employee) {
		Logger.traceMethodStart(new LogParamBuilder().correlationId("1234").build());
		return employeeService.addEmployee(employee);
	}
	
	@PutMapping("/updateEmployee")
	public Employee updateEmployee(@RequestBody Employee employee) throws EmployeeNotFoundException {
		Logger.traceMethodStart(new LogParamBuilder().correlationId("1234").build());
		return employeeService.updateEmployee(employee);
	}

	@DeleteMapping("/deleteEmployee/{id}")
	public boolean deleteEmployee(@PathVariable int id) throws EmployeeNotFoundException {
		Logger.traceMethodStart(new LogParamBuilder().correlationId("1234").build());
		try
		{
		Employee employee = employeeService.getEmployeeByID(id);
		return employeeService.deleteEmployee(employee);
		}
		catch( EmployeeNotFoundException e)
		{
			throw new EmployeeNotFoundException(" not found");
		}
		
	}

	@GetMapping("/getEmployeeById/{id}")
	public Employee getEmployeeById(@PathVariable int id) throws EmployeeNotFoundException {
		Logger.traceMethodStart(new LogParamBuilder().correlationId("1234").build());
		return employeeService.getEmployeeByID(id);
	}
	
	@GetMapping("/testApi")
	public String testAPI()  {
		Logger.traceMethodStart(new LogParamBuilder().correlationId("1234").build());
		Logger.traceMethodExit(new LogParamBuilder().correlationId("1234").build());
		System.out.println("inside controller");
		return employeeService.testAPI();
	}
}
