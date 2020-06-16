/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorybaby;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.*;

/**
 *
 * @author David Yabis
 */
 
public class InventoryBaby extends JFrame implements ItemListener, ActionListener{
    JFrame Login = new JFrame();
    JLabel labelUsername, labelPassword;
    JTextField textUser;
    JPasswordField textPassword;
    JButton bEnter, bCancel;
    JRadioButton rCash, rAdmin;
    ButtonGroup Grp1 = new ButtonGroup();
    
    String LUser, LPass, LType;
    String user, pass;
    String cashier = "cashier", admin = "admin", accType;
    
    JFrame Cashier = new JFrame();
    JList LSTmovies = new JList();
    JComboBox<String> CMBgenre = new JComboBox<String>();
    String address = "C:\\Users\\David Yabis\\Documents\\NetBeansProjects\\inventoryBaby\\src\\inventorybaby\\";
    String genre[] ={address+"Action.txt", address+"Adventure.txt", address+"Comedy.txt", address+"Romance.txt", address+"Sci-Fi.txt"};
    JButton  logout = new JButton("Logout");;
    double payment, TPrice, change, priceItem[] = new double[9	];
    DecimalFormat decForm = new DecimalFormat("#######.00");
    String idItem[] = new String[9], nameItem[] = new String[9];
    int listCount = 0, qtyItem[] = new int[9], PqtyItem,selectedIndex, editConfirmation = 0, tPrice = 0;
    String selectedItem, imageItem[] = new String[9];
    DefaultListModel<String> itemList = new DefaultListModel<>();
    JList itemArea = new JList(itemList);
    
    JFrame Admin = new JFrame();
    
    public InventoryBaby(){
        Login.setSize(400, 600); 
        Login.setLocationRelativeTo(null);
        Login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Login.setLayout(null);
        Login.setVisible(true);
        Login.setLayout(null);
        //Login Objects Beginning
        labelUsername =  new JLabel("Username");
        Login.add(labelUsername);
        labelUsername.setBounds(90, 240, 100,20);
        labelUsername.setForeground(Color.white);

        textUser = new JTextField(40);
        Login.add(textUser);
        textUser.setBounds(90, 260, 200,20);

        labelPassword =  new JLabel("Password");
        Login.add(labelPassword);
        labelPassword.setBounds(90, 280, 100,20);
        labelPassword.setForeground(Color.white);

        textPassword = new JPasswordField(40);
        Login.add(textPassword);
        textPassword.setBounds(90, 300, 200,20);

        bCancel = new JButton("Cancel");
        Login.add(bCancel);
        bCancel.setBounds(90, 340, 100,20);
        bCancel.addActionListener(new ActionListener(){ //Exit
            public void actionPerformed(ActionEvent e){
            System.exit(0);
            }
        });

        bEnter = new JButton("Enter");
        Login.add(bEnter);
        bEnter.setBounds(190, 340, 100,20);
        bEnter.addActionListener(new ActionListener(){ //Enter
            public void actionPerformed(ActionEvent e){
           		user = textUser.getText();
           		pass = String.valueOf(textPassword.getPassword());
           		System.out.println(user + " " + pass);
            	UserScanner("accounts.txt");
            }
        });
        rCash = new JRadioButton("Cashier");
        Login.add(rCash);
        rCash.setBounds(90, 200, 100, 20);
        Grp1.add(rCash);
        rCash.setBackground( new Color(2,16,81));
        rCash.setForeground(Color.white);
        rCash.addItemListener(this);

        rAdmin = new JRadioButton("Admin");
        Login.add(rAdmin);
        rAdmin.setBounds(200, 200, 100, 20);
        Grp1.add(rAdmin);
        rAdmin.setBackground( new Color(2,16,81));
        rAdmin.setForeground(Color.white);
        rAdmin.addItemListener(this);
        //Login Objects Ending

        
        Cashier.setSize(1200, 700);
        Cashier.setLocationRelativeTo(null);
        Cashier.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Cashier.setLayout(null);
        Cashier.setTitle("Cashier");
        
        
        Cashier.add(CMBgenre);
        CMBgenre.setBounds(100,100,200,50);
        CMBgenre.addActionListener(new ActionListener(){ //Genre
            public void actionPerformed(ActionEvent e){
            	String movie = (String) CMBgenre.getSelectedItem();
	    		try {
	    			ProductReader(movie + ".txt");	
	    		} catch (Exception x){
	    			JOptionPane.showMessageDialog(null, "heywaitaminute\n" + x);
	    		}
            }
        });
        CMBgenre.addItem("Action");
        CMBgenre.addItem("Adventure");
        CMBgenre.addItem("Comedy");
        CMBgenre.addItem("Romance");
        CMBgenre.addItem("Sci-Fi");
        
        Cashier.add(LSTmovies);
        LSTmovies.setBounds(100,160,400,400);
        Cashier.add(itemArea);
        itemArea.setBounds(700,160,400,400);
        
        Cashier.add(logout);
        logout.setBounds(1050,5,120,30);
        logout.addActionListener(new ActionListener(){ //LogOut
            public void actionPerformed(ActionEvent e){
	            Cashier.dispose();
	            textUser.setText("");
	            textPassword.setText("");
	            Login.setVisible(true);
            }
        });
       
        Admin.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        Admin.setLocationRelativeTo(null);
        Admin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Admin.setLayout(null);
        Admin.setTitle("Admin");
    } //END OF CONSTRUCTOR
    
    
    public void actionPerformed(ActionEvent e){

    }
    
    public void UserScanner(String Text){
        if(rCash.isSelected()){
	        try{
	            Scanner users = new Scanner(new FileReader(Text));
	            while(users.hasNextLine()){
	                LUser = users.next();
	                LPass = users.next();
	                LType = users.next();
	                if(user.equals(LUser) && pass.equals(LPass) && accType.equals(LType)){ //cashier type
	                    Login.dispose();
	                    Cashier.setVisible(true);
	                    System.out.println("yoink");
	                    break;
	                }   
	                System.out.println(LUser + " " + LPass + " " + LType);
	            }
	            
	        }
	        catch (Exception ex){
	        }
	        }
        else if(rAdmin.isSelected()){
	        try{
	            Scanner users = new Scanner(new FileReader(Text));
	            while(users.hasNextLine()){
	                LUser = users.next();
	                LPass = users.next();
	                LType = users.next();
	                if(user.equals(LUser) && pass.equals(LPass) && accType.equals(LType)){ //admin type
	                    Login.dispose();
	                    Admin.setVisible(true);
	                    System.out.println("boink");
	                    break;
	                }
	                System.out.println(LUser + " " + LPass + " " + LType);
	            }
	            
	        }
	        catch (Exception ex){
	        }
        }
    }
    
    public void ProductReader(String txt) throws FileNotFoundException{
        listCount = 0;
        itemList.removeAllElements();
        try{
            Scanner input = new Scanner(new FileReader(txt));
            while(input.hasNextLine()){
                String line = input.nextLine(); String[] lineArr = line.split("/");
                idItem[listCount] = lineArr[0]; //String
                //imageItem[listCount] = input.next();
                nameItem[listCount] = lineArr[1]; //String 
                qtyItem[listCount] = Integer.parseInt(lineArr[2]); //Integer
                priceItem[listCount] = Double.parseDouble(lineArr[3]); //Double;
                itemList.addElement(idItem[listCount] + "/" + nameItem[listCount] + "/" +qtyItem[listCount]+"/"+ priceItem[listCount]);
                listCount++;
            }
            input.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        }
    
    public void itemStateChanged(ItemEvent e){
        if(rCash.isSelected()){
            accType = "cashier";
        }
        if(rAdmin.isSelected()){
            accType = "admin";
        }
    }
    
    public static void main(String[] args) {
        InventoryBaby bonk = new InventoryBaby();
    }
    
}
