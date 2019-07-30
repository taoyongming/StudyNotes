package javaperformance;

// 包裹类
public class Package {
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void execute() {
        System.out.println(Thread.currentThread().getName()+" executed "+this);
    }
}
