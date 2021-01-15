package net.qzwxsaedc.hiunattendedreport;

import java.io.*;
import java.util.Scanner;

public class User {
    private long qq;
    private String password;
    private User(long qq, String password){
        this.qq = qq;
        this.password = password;
    }

    public long getQQ() {
        return qq;
    }

    public String getPassword() {
        return password;
    }

    public void save(){
        File config = new File("qq.cfg");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(config));
            writer.write(String.format("%d,%s", qq, password));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User readFromFile(){
        File config = new File("qq.cfg");
        if(!config.exists()) return null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(config));
            String line = reader.readLine();
            reader.close();
            String[] args = line.split(",");
            return new User(Long.parseLong(args[0]), args[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User readFromConsole(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入qq号:");
        long qq = scanner.nextLong();
        scanner.nextLine();
        System.out.println("请输入密码:");
        String password = scanner.nextLine();
        return new User(qq, password);
    }

    public static User UserFactory(){
        User user = readFromFile();
        if(user == null)
            user = readFromConsole();
        return user;
    }
}
