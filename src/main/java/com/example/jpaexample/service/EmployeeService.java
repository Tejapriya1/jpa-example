package com.example.jpaexample.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.jpaexample.model.Employee;
import com.example.jpaexample.repository.EmployeeRepository;
import com.example.jpaexample.userdefinedexception.EmployeeNotFoundException;

@Component
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}
	
	public Employee addEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}
	
	@Transactional
	public Employee updateEmployee(Employee employee) throws EmployeeNotFoundException {
		Employee employee1= getEmployeeByID(employee.getId());
		employee1.setAge(employee.getAge());
		employee1.setDepartment(employee.getDepartment());
		employee1.setFirstName(employee.getFirstName());
		employee1.setLastName(employee.getLastName());
		
		return employee1;
	}
	
	public boolean deleteEmployee(Employee employee) throws EmployeeNotFoundException {
		
		getEmployeeByID(employee.getId());
		employeeRepository.delete(employee);
		
		return true;
	}
	
	public Employee getEmployeeByID(int id) throws EmployeeNotFoundException {
	
		Employee employee=null;
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		if(employeeOptional.isPresent()) {
			employee = employeeOptional.get();
		}else {
			throw new EmployeeNotFoundException("employee not found for this id:"+id);
		}
		return employee;
	}

}
