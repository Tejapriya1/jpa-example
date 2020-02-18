package com.example.jpaexample.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@CrossOrigin
public class EmployeeController {
	
	Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/getall")
	public List<Employee> getAll() {
		logger.debug("Inside EmployeeController:getAll");
		return employeeService.getAllEmployees();
	}

	@PostMapping("/addEmployee")
	public Employee addEmployee(@RequestBody Employee employee) {
		logger.debug("Inside EmployeeController:addEmployee");
		return employeeService.addEmployee(employee);
	}
	
	@PutMapping("/updateEmployee")
	public Employee updateEmployee(@RequestBody Employee employee) throws EmployeeNotFoundException {
		logger.debug("Inside EmployeeController:updateEmployee");
		return employeeService.updateEmployee(employee);
	}

	@DeleteMapping("/deleteEmployee/{id}")
	public boolean deleteEmployee(@PathVariable int id) throws EmployeeNotFoundException {
		logger.debug("Inside EmployeeController:deleteEmployee");
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
		logger.debug("Inside EmployeeController:getEmployeeById");
		return employeeService.getEmployeeByID(id);
	}
}
