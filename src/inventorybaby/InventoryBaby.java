/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorybaby;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.awt.event.ActionEvent;
import java.io.*;
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
    DecimalFormat df = new DecimalFormat("0.00");
    
    JFrame Cashier = new JFrame();
    
    String[] cols = {"ID Code", "Amount", "Price", "Name"};
    String[] sel = new String[4];
    DefaultTableModel cart = new DefaultTableModel(cols, 0);
    JTable LSTmovies = new JTable(cart);
    JComboBox<String> CMBgenre = new JComboBox<String>();
    String genre[] ={"Action.txt", "Adventure.txt", "Comedy.txt", "Romance.txt", "Sci-Fi.txt"};
    ArrayList<String> category = new ArrayList<String>();
    JButton  logout = new JButton("Logout"), moveToCart = new JButton("Move"), removeToCart = new JButton("Remove");
    double payment, TPrice, change, priceItem[] = new double[9];
    DecimalFormat decForm = new DecimalFormat("#######.00");
    String idItem[] = new String[9], nameItem[] = new String[9];
    DefaultListModel<String> itemList = new DefaultListModel<>();
    JList itemArea = new JList(itemList);
    int listCount = 0, qtyItem[] = new int[9], editConfirmation = 0, tPrice = 0;
    String imageItem[] = new String[9]; 
    ArrayList<String> cartList = new ArrayList<String>();
    JLabel Sum = new JLabel("Php 0");
    Double cartSum = 0.00;
    String movie;

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
        CMBgenre.setBounds(50,100,200,50);
        CMBgenre.addActionListener(new ActionListener(){ //Genre
            public void actionPerformed(ActionEvent e){
            	movie = (String) CMBgenre.getSelectedItem();
	    		try {
	    			ProductReader("products//" + movie + ".txt");	
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
        
        Cashier.add(LSTmovies); // Cart
        LSTmovies.setBounds(650,160,470,400);
        //LSTmovies.setModel(cart);
        Cashier.add(itemArea); // Movie List
        itemArea.setBounds(50,160,500,200);
        
        
        
        Cashier.add(logout);
        logout.setBounds(1050,5,120,30);
        logout.addActionListener(new ActionListener(){ //LogOut
            public void actionPerformed(ActionEvent e){
	            Cashier.dispose();
	            textUser.setText("");
	            textPassword.setText("");
                cart.getDataVector().removeAllElements();
                Login.setVisible(true);
                cartSum = 0.00;
                Sum.setText("Php 0.00");
                
            }
        });
        LSTmovies.getModel().addTableModelListener(new TableModelListener(){
        
            @Override
            public void tableChanged(TableModelEvent e) {

                cartSum = 0.00;
                for (int i = 0, rows = cart.getRowCount(); i < rows; i++)
{                   double quant = Double.parseDouble((String) cart.getValueAt(i, 2));
                    double presyo = Double.parseDouble((String) cart.getValueAt(i, 3));
                        
                        cartSum += presyo ;
                        System.out.println(quant + " " + presyo + " " + cartSum);
                        Sum.setText("Php" + df.format(cartSum));         
}
            }
        });
        Cashier.add(moveToCart);
        moveToCart.setBounds(560,170,80,30);
        moveToCart.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
            int count = 0;
            String selectedItem = (String) itemArea.getSelectedValue();
            int selectedIndex= itemArea.getSelectedIndex();
                if (selectedItem == null){
                    JOptionPane.showMessageDialog(null, "Ui walang laman, vro");
                } 
                else{
                    try{
                    String stockInput = JOptionPane.showInputDialog(null, "Ilan ilalagay mo na " + nameItem[selectedIndex] + " vro?", "Ilan ilalagay mo vro?", JOptionPane.OK_CANCEL_OPTION);
                    if(stockInput != null){
                                int newQty = Integer.parseInt(stockInput);
                                Double waow = newQty*priceItem[selectedIndex];
                                sel[0] = idItem[selectedIndex];
                                sel[1] = nameItem[selectedIndex];
                                sel[2] = Integer.toString(newQty);
                                sel[3] = df.format(waow).toString();
                                Object[] row = {sel[0],sel[1],sel[2],sel[3]};
                                cart.addRow(row);
                                cartSum += newQty*priceItem[selectedIndex];
                                FileWriter("products//" + movie + ".txt", idItem[selectedIndex], newQty, true);
                                ProductReader("products//" + movie + ".txt");
                            }

    
                
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex);
                    System.out.println(ex);
                }
            }
            }
        
        });
        Cashier.add(removeToCart);
        removeToCart.setBounds(560,210,80,30);
        removeToCart.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
            
            int selectedRowIndex = LSTmovies.getSelectedRow();
            try{
                String bab = (cart.getValueAt(selectedRowIndex, 0).toString());
                String babs = (cart.getValueAt(selectedRowIndex, 2).toString());
                int dabs = Integer.parseInt(babs);
                FileWriter("products//" + movie + ".txt", bab, dabs, false);
                ProductReader("products//" + movie + ".txt");}

                catch(Exception x){
                    JOptionPane.showMessageDialog(null,x);
    
                }
            for( int i = cart.getRowCount() - 1; i >= 0; --i )
            {   
                
                if(selectedRowIndex == i){
                    cart.removeRow(selectedRowIndex);
                    if(isEmpty(LSTmovies)==true){
                        cartSum = 0.00;
                        Sum.setText("Php 0.00");
                    }
                }
            }
            
           
            
            
           }
           
        });
        Cashier.add(Sum);
        Sum.setBounds(1050,580,80,30);
        
       
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
    
    public void FileWriter(String txtFile, String idNum, int itemStock, boolean Operation){
        try{
            ArrayList<String> yoinkList = new ArrayList<String>();
            int Sabuco; 
            Scanner input = new Scanner(new FileReader(txtFile));
            while(input.hasNextLine()){
                String line = input.nextLine(); String[] lineArr = line.split("/");
                    int k = 0;
                    if(lineArr[k].equals(idNum)){
                        if (Operation == true)
                            Sabuco = Integer.parseInt(lineArr[2]) - itemStock;
                        else
                            Sabuco = Integer.parseInt(lineArr[2]) + itemStock;
                        yoinkList.add(lineArr[0] + "/" + lineArr[1] + "/" + Sabuco + "/" + lineArr[3] /* + "/" + picID     remove comments if picID is ready*/);
                    }
                    else{
                        yoinkList.add(line);
                    }
                    k++;       
                } 
        
            try{
            PrintWriter RebongJunior = new PrintWriter(txtFile);
                for(String Manolito : yoinkList){ // ahh komedyante ka na nyan?
                    RebongJunior.println(Manolito);
                }
            yoinkList.clear();
            RebongJunior.close();
                }catch( NoSuchElementException yo){}
        }catch(FileNotFoundException ex){
            JOptionPane.showMessageDialog(null, "Vro di ko mabasa file mo", "Ui pre wala dito", JOptionPane.OK_OPTION);
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
                itemList.addElement(String.format("%-20s",idItem[listCount])+ " " + String.format("%-20s",qtyItem[listCount])+ " " + String.format("%-40s",df.format(priceItem[listCount])) + String.format("%-10s",nameItem[listCount]));
                listCount++;
            }
            input.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        }
   /* public void ProductWriter(String textFile, int quantTxt, int quantInput){
            try{
            File file = new File(textFile);
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            
            }catch(IOException ex){

            }
    }*/
    public static boolean isEmpty(JTable jTable) {
        if (jTable != null && jTable.getModel() != null) {
            return jTable.getModel().getRowCount()<=0?true:false;
        }
        return false;
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
