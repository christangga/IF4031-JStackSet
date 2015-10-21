/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jgroupsstack;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

/**
 *
 * @author christangga
 * @param <T>
 */
public class ReplSet<T> extends ReceiverAdapter {

    private final HashSet<T> set;
    private static JChannel channel;
    private static final String username = System.getProperty("user.name", "n/a");

    public ReplSet() {
        set = new HashSet();
    }

    /**
     * mengembalikan true jika obj ditambahkan, dan false jika obj telah ada
     * pada set
     *
     * @param obj
     * @return
     */
    public boolean add(T obj) {
        synchronized (set) {
            return set.add(obj);
        }
    }

    /**
     * mengembalikan true jika obj ada pada set
     *
     * @param obj
     * @return
     */
    public boolean contains(T obj) {
        synchronized (set) {
            return set.contains(obj);
        }
    }

    /**
     * mengembalikan true jika obj ada pada set, dan kemudian obj dihapus dari
     * set. Mengembalikan false jika obj tidak ada pada set
     *
     * @param obj
     * @return
     */
    public boolean remove(T obj) {
        synchronized (set) {
            return set.remove(obj);
        }
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        synchronized (set) {
            Util.objectToStream(set, new DataOutputStream(output));
        }
    }

    @Override
    public void setState(InputStream input) throws Exception {
        HashSet<T> tSet = (HashSet<T>) Util.objectFromStream(new DataInputStream(input));
        synchronized (set) {
            set.clear();
            set.addAll(tSet);
        }
    }

    @Override
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    @Override
    public void receive(Message msg) {
        try {
            String line = (String) msg.getObject();
            if (line.startsWith("/add")) {
                String[] splitedObj = line.split(" ", 2);
                add((T) splitedObj[1]);
            } else if (line.startsWith("/remove")) {
                String[] splitedObj = line.split(" ", 2);
                remove((T) splitedObj[1]);
            }
        } catch (Exception ex) {
            Logger.getLogger(ReplStack.class
                .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() throws Exception {
        channel = new JChannel();
        channel.setDiscardOwnMessages(true);
        channel.setReceiver(this);
        channel.connect("ReplSet");
        channel.getState(null, 10000);
    }

    public static void main(String[] args) {
        ReplSet<String> replSet = new ReplSet<>();

        try {
            replSet.start();

            System.out.println("Welcome, " + username + "!");
            System.out.println("Available command:");
            System.out.println("/add <string>");
            System.out.println("/contains <string>");
            System.out.println("/remove <string>");
            System.out.println("/exit");

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("> ");
                System.out.flush();
                String line = in.readLine().toLowerCase();
                if (line.startsWith("/exit")) {
                    break;
                } else if (line.startsWith("/add")) {
                    String[] splittedObj = line.split(" ", 2);
                    if (splittedObj.length < 2) {
                        System.out.println("Invalid command!");
                        System.out.println("/add <string>");
                    } else {
                        Message msg = new Message(null, null, line);
                        channel.send(msg);

                        if (replSet.add(splittedObj[1])) {
                            System.out.println(splittedObj[1] + " has been added to set");
                        } else {
                            System.out.println(splittedObj[1] + " already exists in set");
                        }
                    }
                } else if (line.startsWith("/contains")) {
                    String[] splittedObj = line.split(" ", 2);
                    if (splittedObj.length < 2) {
                        System.out.println("Invalid command!");
                        System.out.println("/contains <string>");
                    } else {
                        if (replSet.contains(splittedObj[1])) {
                            System.out.println(splittedObj[1] + " is found in set");
                        } else {
                            System.out.println(splittedObj[1] + " is not found in set");
                        }
                    }
                } else if (line.startsWith("/remove")) {
                    String[] splittedObj = line.split(" ", 2);
                    if (splittedObj.length < 2) {
                        System.out.println("Invalid command!");
                        System.out.println("/remove <string>");
                    } else {
                        Message msg = new Message(null, null, line);
                        channel.send(msg);

                        if (replSet.remove(splittedObj[1])) {
                            System.out.println(splittedObj[1] + " has been removed from set");
                        } else {
                            System.out.println(splittedObj[1] + " not found in set");
                        }
                    }
                } else {
                    System.out.println("Invalid command!");
                    System.out.println("/add <string>");
                    System.out.println("/contains <string>");
                    System.out.println("/remove <string>");
                    System.out.println("/exit");
                }
            }
            System.out.println("Goodbye, " + username + "!");

            channel.close();
        } catch (Exception e) {
        }
    }

}
