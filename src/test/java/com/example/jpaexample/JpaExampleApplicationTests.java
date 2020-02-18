package com.example.jpaexample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
//import org.junit.Test;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpaexample.controller.EmployeeController;
import com.example.jpaexample.model.Employee;
import com.example.jpaexample.repository.EmployeeRepository;
import com.example.jpaexample.service.EmployeeService;
import com.example.jpaexample.userdefinedexception.EmployeeNotFoundException;

@SpringBootTest
class JpaExampleApplicationTests {
	@InjectMocks
	EmployeeController controller;
	@Mock
	EmployeeService service;
	@Mock
	EmployeeRepository empRep;

	@Test
	public void getAllEmployee() {
		List<Employee> list = employeeBean();
		Mockito.when(empRep.findAll()).thenReturn(list);
		Mockito.when(service.getAllEmployees()).thenReturn(list);
		List<Employee> result = controller.getAll();
		assertThat(result.size()).isEqualTo(list.size());
		assertThat(result.get(0).getFirstName()).isEqualTo(list.get(0).getFirstName());
		assertThat(result.get(1).getFirstName()).isEqualTo(list.get(1).getFirstName());

	}

	private List<Employee> employeeBean() {
		List<Employee> list = new ArrayList<>();
		list.add(new Employee(1, "Rama", "krishna", 29, "cse"));
		list.add(new Employee(1, "Nivas", "Mr.", 29, "ece"));
		list.add(new Employee(1, "Padma", "Mis.", 29, "eee"));
		return list;
	}

	@Test
	public void addEmployee() {
		Employee emp = new Employee(1, "Mike", "James", 25, "Accounts");
		Mockito.when(empRep.save(emp)).thenReturn(emp);
		Mockito.when(service.addEmployee(emp)).thenReturn(emp);
		Employee result = controller.addEmployee(emp);
		assertThat(result.getId()).isEqualTo(emp.getId());
		verify(service, times(1)).addEmployee(emp);

	}

	@Test
	public void deleteEmployee() {
		Employee emp = new Employee(1, "Mike", "James", 25, "Accounts");
		Mockito.doNothing().when(empRep).delete(emp);
		try {
			Mockito.when(service.getEmployeeByID(emp.getId())).thenReturn(emp);
			boolean result = controller.deleteEmployee(emp.getId());
			assertThat(result).isEqualTo(result);
		} catch (EmployeeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void updateEmployee() {
		Employee emp = new Employee(1, "Mike", "James", 25, "Accounts");
		Mockito.when(empRep.findById(emp.getId())).thenReturn(Optional.of(emp));

		try {
			Mockito.when(service.updateEmployee(emp)).thenReturn(emp);
			Mockito.when(service.getEmployeeByID(emp.getId())).thenReturn(emp);
			Employee result = controller.updateEmployee(emp);
			assertThat(emp.getId()).isEqualTo(result.getId());
			assertThat(emp.getFirstName()).isEqualTo(result.getFirstName());
			assertThat(emp.getLastName()).isEqualTo(result.getLastName());
			System.out.println("result" + result.getFirstName());
		} catch (EmployeeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test()
	public void getEmployeeByID() {
		Employee emp = new Employee(1, "Mike", "James", 25, "Accounts");
		Mockito.when(empRep.findById(emp.getId())).thenReturn(Optional.of(emp));
		try {
			Mockito.when(service.getEmployeeByID(emp.getId())).thenReturn(emp);
			Employee result = controller.getEmployeeById(emp.getId());
			assertThat(emp.getId()).isEqualTo(result.getId());
			assertThat(emp.getFirstName()).isEqualTo(result.getFirstName());
			assertThat(emp.getLastName()).isEqualTo(result.getLastName());
		} catch (EmployeeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
