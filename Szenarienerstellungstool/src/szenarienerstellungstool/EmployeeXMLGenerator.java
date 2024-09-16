package szenarienerstellungstool;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlType(propOrder = {"id", "name", "age", "department"})
class Employee {
    private int id;
    private String name;
    private int age;
    private String department;

    // Empty constructor required for JAXB
    public Employee() {
    }

    public Employee(int id, String name, int age, String department) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.department = department;
    }

    @XmlElement
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @XmlElement
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

public class EmployeeXMLGenerator {
    public static void main(String[] args) {
        // Create a list of employees
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "John Doe", 30, "HR"));
        employees.add(new Employee(2, "Jane Smith", 28, "Engineering"));
        // Add more employees as needed

        try {
            // Create JAXB context and marshal the list to XML
            JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class, EmployeeList.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Output to file
            EmployeeList employeeList = new EmployeeList();
            employeeList.setEmployees(employees);
            marshaller.marshal(employeeList, new File("employees_cool.xml"));

            // Output to console for testing
            // marshaller.marshal(employeeList, System.out);

            System.out.println("XML File generated successfully!");

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}

@XmlRootElement
class EmployeeList {
    private List<Employee> employees;

    @XmlElement(name = "employee")
    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
