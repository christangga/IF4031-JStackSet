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
import java.util.Stack;
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
public class ReplStack<T> extends ReceiverAdapter {

    private final Stack<T> stack;
    private static JChannel channel;
    private static final String username = System.getProperty("user.name", "n/a");

    public ReplStack() {
        stack = new Stack();
    }

    public void push(T obj) {
        synchronized (stack) {
            stack.push(obj);
        }
    }

    public T pop() {
        if (!stack.empty()) {
            synchronized (stack) {
                return stack.pop();
            }
        } else {
            return null;
        }
    }

    public T top() {
        if (!stack.empty()) {
            synchronized (stack) {
                return stack.peek();
            }
        } else {
            return null;
        }
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        synchronized (stack) {
            Util.objectToStream(stack, new DataOutputStream(output));
        }
    }

    @Override
    public void setState(InputStream input) throws Exception {
        Stack<T> tStack = (Stack<T>) Util.objectFromStream(new DataInputStream(input));
        synchronized (stack) {
            stack.clear();
            stack.addAll(tStack);
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
            if (line.startsWith("/push")) {
                String[] splitedObj = line.split(" ", 2);
                push((T) splitedObj[1]);
            } else if (line.startsWith("/pop")) {
                pop();
            }
        } catch (Exception ex) {
            Logger.getLogger(ReplStack.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() throws Exception {
        channel = new JChannel();
        channel.setDiscardOwnMessages(true);
        channel.setReceiver(this);
        channel.connect("ReplStack");
        channel.getState(null, 10000);
    }

    public static void main(String[] args) {
        ReplStack<String> replStack = new ReplStack<>();

        try {
            replStack.start();

            System.out.println("Welcome, " + username + "!");
            System.out.println("Available command:");
            System.out.println("/push <string>");
            System.out.println("/pop");
            System.out.println("/top");
            System.out.println("/exit");

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("> ");
                System.out.flush();
                String line = in.readLine().toLowerCase();
                if (line.startsWith("/exit")) {
                    break;
                } else if (line.startsWith("/push")) {
                    String[] splittedObj = line.split(" ", 2);
                    if (splittedObj.length < 2) {
                        System.out.println("Invalid command!");
                        System.out.println("/push <string>");
                    } else {
                        Message msg = new Message(null, null, line);
                        channel.send(msg);

                        replStack.push(splittedObj[1]);
                        System.out.println(splittedObj[1] + " has been pushed to stack");
                    }
                } else if (line.startsWith("/pop")) {
                    Message msg = new Message(null, null, line);
                    channel.send(msg);
                    
                    String poppedObj = replStack.pop();
                    if (poppedObj != null) {
                        System.out.println(poppedObj + " has been popped from stack");
                    } else {
                        System.out.println("Stack is empty");
                    }
                } else if (line.startsWith("/top")) {
                    String peekedObj = replStack.top();
                    if (peekedObj != null) {
                        System.out.println("Top of stack is " + peekedObj);
                    } else {
                        System.out.println("Stack is empty");
                    }
                } else {
                    System.out.println("Invalid command!");
                    System.out.println("/push <string>");
                    System.out.println("/pop");
                    System.out.println("/top");
                    System.out.println("/exit");
                }
            }
            System.out.println("Goodbye, " + username + "!");

            channel.close();
        } catch (Exception e) {
        }
    }

}
