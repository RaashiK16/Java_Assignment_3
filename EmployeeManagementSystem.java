import java.io.*;
import java.util.*;

class EmployeeManagementSystem {
    private Map<Integer, FTE> ftes = new HashMap<>();
    private Map<Integer, Intern> interns = new HashMap<>();
    private Map<Integer, Contractor> contractors = new HashMap<>();
    private String fteLogFile = "fte_log.txt";
    private String internLogFile = "intern_log.txt";
    private String contractorLogFile = "contractor_log.txt";

    public void readFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("-");
                if (data.length == 4) {
                    String type = data[0];
                    String[] employeeData = Arrays.copyOfRange(data, 1, 4);
                    Employee employee = new Employee(employeeData[0], employeeData[1], employeeData[2]);
                    int employeeId = Integer.parseInt(data[0]);
                    switch (type) {
                        case "F":
                            FTE fte = (FTE) employee;
                            fte.setEmployeeId(employeeId);
                            ftes.put(fte.getEmployeeId(), fte);
                            logData(fte, "File");
                            break;
                        case "I":
                            Intern intern = (Intern) employee;
                            intern.setEmployeeId(employeeId);
                            interns.put(intern.getEmployeeId(), intern);
                            logData(intern, "File");
                            break;
                        case "C":
                            Contractor contractor = (Contractor) employee;
                            contractor.setEmployeeId(employeeId);
                            contractors.put(contractor.getEmployeeId(), contractor);
                            logData(contractor, "File");
                            break;
                        default:
                            System.out.println("Invalid employee type.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String readFromConsole() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter employee name :");
        String name = sc.nextLine();
        System.out.print("Enter employee department :");
        String department = sc.nextLine();
        System.out.print("Enter employee manager:");
        String manager = sc.nextLine();
        System.out.println("Enter employee type (FTE, Intern, Contractor) :");
        String type = sc.nextLine();
        Employee employee = null;
        switch (type) {
            case "FTE":
            case "F":
                employee = new FTE(name, department, manager);
                break;
            case "Intern":
            case "I":
                employee = new Intern(name, department, manager);
                break;
            case "Contractor":
            case "C":
                employee = new Contractor(name, department, manager);
                break;
            default:
                System.out.println("Invalid employee type.");
                sc.close();
                return "";
        }
        employee.setEmployeeId(generateEmployeeId());
        switch (type) {
            case "FTE":
            case "F":
                ftes.put(employee.getEmployeeId(), (FTE) employee);
                logData((FTE) employee, "Console");
                break;
            case "Intern":
            case "I":
                interns.put(employee.getEmployeeId(), (Intern) employee);
                logData((Intern) employee, "Console");
                break;
            case "Contractor":
            case "C":
                contractors.put(employee.getEmployeeId(), (Contractor) employee);
                logData((Contractor) employee, "Console");
                break;
            default:
                System.out.println("Invalid employee type.");
        }
        System.out.println("Employee details stored successfully!");
        sc.close();
        return type;
    }

    private void logData(Employee employee, String source) {
        try (FileWriter fw = new FileWriter(getLogFile(employee), true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {

            out.println(employee.getEmployeeId() + "-" +
                    employee.getEmployeeName() + "-" +
                    employee.getEmployeeDepartment() + "-" +
                    employee.getEmployeeManager() + "-" +
                    source);

        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }

    private int generateEmployeeId() {
        Random rand = new Random();
        int id = rand.nextInt(90000) + 10000;
        if (ftes.containsKey(id) || interns.containsKey(id) || contractors.containsKey(id)) {
            return generateEmployeeId();
        }
        return id;
    }

    private String getLogFile(Employee employee) {
        if (employee instanceof FTE) {
            return fteLogFile;
        } else if (employee instanceof Intern) {
            return internLogFile;
        } else if (employee instanceof Contractor) {
            return contractorLogFile;
        } else {
            return "";
        }
    }

    public void displayEmployees(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("-");
                if (data.length == 5) {
                    System.out.print("Employee ID - " + data[0] + ", ");
                    System.out.print("Name - " + data[1] + ", ");
                    System.out.print("Department - " + data[2] + ", ");
                    System.out.println("Manager - " + data[3]);
                }

            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {

        EmployeeManagementSystem ems = new EmployeeManagementSystem();
        // REading first from a file
        ems.readFromFile("employees.txt");
        // Reading from the console
        String type = ems.readFromConsole();
        // displaying the list of all employees
        System.out.println("\n\nHere are details of all the employees of the chosen category - ");
        switch (type) {
            case "FTE":
            case "F":
                ems.displayEmployees(ems.fteLogFile);
                break;
            case "Intern":
            case "I":
                ems.displayEmployees(ems.internLogFile);
                break;
            case "Contractor":
            case "C":
                ems.displayEmployees(ems.contractorLogFile);
                break;
            default:
                System.out.println("Invalid employee type.");
        }

    }
}

class Employee {
    private int employeeId;
    private String employeeName;
    private String employeeDepartment;
    private String employeeManager;

    public Employee(String name, String department, String manager) {
        this.employeeName = name;
        this.employeeDepartment = department;
        this.employeeManager = manager;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeDepartment() {
        return employeeDepartment;
    }

    public String getEmployeeManager() {
        return employeeManager;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", employeeDepartment='" + employeeDepartment + '\'' +
                ", employeeManager='" + employeeManager + '\'' +
                '}';
    }
}

class FTE extends Employee {
    public FTE(String name, String department, String manager) {
        super(name, department, manager);
    }
}

class Intern extends Employee {
    public Intern(String name, String department, String manager) {
        super(name, department, manager);
    }
}

class Contractor extends Employee {
    public Contractor(String name, String department, String manager) {
        super(name, department, manager);
    }
}
