package staticdemo;

import java.io.*;

public class demostatic implements Serializable{

    public  static void print(){
        System.out.println("hello world");
    }

    public static void main(String[] args) {
//        demostatic demo=null;
//        demo.print();//即使实体为null 也是可以调用static方法的，这个方法属于class 而不是属于实体
      //  demostatic demostatic=new demostatic();
//               User user = new User();
//                user.setAge("22");
//                user.setName("小明");
//                user.setPassword("admin");
//                System.out.println("序列化之前:"+user.getAge()+"\t"+user.getName()+"\t"+user.getPassword());
//                try {
//                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/Users/laixiaoxing/Desktop/user.txt"));
//                 //   user.setAge("33"); //在序列化后在对static修饰的变量进行一次赋值操作
//                    user.setName("小红");
//                    oos.writeObject(user);
//                    oos.flush();
//                    oos.close();
//
//                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/Users/laixiaoxing/Desktop/user.txt"));
//                    User usersx = (User) ois.readObject();
//                    user.setAge("2233");
//                    System.out.println("反序列化之后:"+usersx.getAge() + "\t" + usersx.getName() + "\t" + usersx.getPassword());
//                    if (usersx==user){
//                        System.out.println("两个对象相等");
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }

    }
}
