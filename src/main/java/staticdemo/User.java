package staticdemo;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private  String password;   //被transient修饰的变量
    private static String age;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String getAge() {
        return age;
    }

    public static void setAge(String age) {
        User.age = age;
    }
}
