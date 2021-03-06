package address.gui;

import address.data.AddressEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

/**
 * public class of MainGui which extents
 */
public class MainGUI extends JFrame {
    /**
     * private JPanel that Creating a panel to add buttons
     */
    private JPanel mainPanel;
    /**
     *  private JScrollPane listingJScrollPane that creates a scroll pane
     */
    private JScrollPane listingJScrollPane;
    /**
     * JButton newButton create a labeled button that Adds
     */
    private JButton newButton;
    /**
     * JButton removeButton create a labeled button that Remove
     */
    private JButton removeButton;
    /**
     * JButton displayButton create a labeled button that Adds
     */
    private JButton displayButton;
    /**
     *  class JList represents a list of text items and have AddressEntry in it.
     */
    private JList <AddressEntry> addressEntryJList;

    /**
     * Vector that has addressEntryList passed
     */

    Vector<AddressEntry> addressEntryList = new Vector<AddressEntry>();

    /**
     * DefaultListModel as a Model (predefined) for JList
     */
    DefaultListModel<AddressEntry> myaddressEntryListModel = new DefaultListModel<AddressEntry>();

    /**
     *  Public MainGUI
     */

    public MainGUI(){

        add(mainPanel);
        setLayout(new GridLayout());

        //listingJScrollPane = new JScrollPane(this.addressEntryJList);
        listingJScrollPane.setViewportView(addressEntryJList);
       //getContentPane().add(this.listingJScrollPane, BorderLayout.CENTER);
        //add(listingJScrollPane);
        this.addressEntryJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        this.addressEntryJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        this.addressEntryJList.setVisibleRowCount(-1);
        //getContentPane().add(listingJScrollPane, BorderLayout.CENTER);
        setTitle("Address Book Application");
        setSize(600,600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
//                         write your code here
                        NewGUI newGUI = new NewGUI();
                        myaddressEntryListModel.add(addressEntryJList.getModel().getSize(),newGUI.getNewEntry());
                        System.out.println(newGUI.getNewEntry());

                    };
                });
            }
        });
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = addressEntryJList.getSelectedIndex();
                System.out.println("Index Value " + index);
                displayData();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = addressEntryJList.getSelectedIndex();

                if(index != -1)//something is selected otherwise do nothing

                {   //retrieve the DeffaultListModel associated
                    // with our JList and remove from it the AddressEntry at this index

                    ((DefaultListModel<AddressEntry>) (addressEntryJList.getModel())).remove(index);
                    deleteFromDatabase(index);
                    System.out.println("Index Value " + index);

                    // NOTE in your project 2 you will also remove it from your AddressBook.addressEntryList
                    // AND ALSO remove it from the associated database table

                }
            }
        });

    }

    /**
     *  dummyData that make a dummy addressEntryList with 2 AddressEntry objects
     */

    public void dummyData(){
        //make a dummy addressEntryList with 2 AddressEntry objects--Project 2 will read from Database instead,etc.
//        addressEntryList.add(new AddressEntry("Lynne", "Grewe", "33 A street", "Hayward", "CA", 9399,"l@csueastbay.edu","555-1212"));

//        addressEntryList.add(new AddressEntry("Jane", "Doe", "22 Cobble street", "Hayward", "CA", 9399,"jane@csueastbay.edu","555-9999"));

        //because we want to REMOVE or ADD to our JList we have to create it

        //from a DefaultListModel (see https://docs.oracle.com/javase/tutorial/uiswing/components/list.html)
        // to which we add the elements of our collection of AddressEntry objects

//        for(int i = 0; i<addressEntryList.size(); i++)
//        {  this.myaddressEntryListModel.add(i, this.addressEntryList.elementAt(i)); }
        //Now when we create our JList do it from our ListModel rather than our vector of AddressEntry

//        addressEntryJList.setModel(myaddressEntryListModel);
        //this.addressEntryJList = new JList<AddressEntry>(this.myaddressEntryListModel);

        //setting up the look of the JList


    }

    /**
     * displayData that reads from Database
     */

    private void displayData(){
        try {
            String sql = "SELECT * FROM ADDRESSENTRYTABLE";
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:mcs1004/wXTOOCL4@adcsdb01.csueastbay.edu:1521/mcspdb.ad.csueastbay.edu");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);

            int count = 0;

            while (result.next()) {
                String firstname = result.getString(1);
                String lastname = result.getString(2);
                String street = result.getString(3);
                String city = result.getString(4);
                String state = result.getString(5);
                int zip = result.getInt(6);
                String phone = result.getString(7);
                String email = result.getString(8);

                addressEntryList.add(new AddressEntry(firstname, lastname, street, city, state, zip, email,phone));
            }
            for(int i = 0; i<addressEntryList.size(); i++) {

                this.myaddressEntryListModel.add(i, this.addressEntryList.elementAt(i));

            }
            addressEntryJList.setModel(myaddressEntryListModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * deleteFromDatabase that removes it from the data base
     * @param index send index to help fi
     */

    private void deleteFromDatabase(int index){
        try {
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:mcs1004/wXTOOCL4@adcsdb01.csueastbay.edu:1521/mcspdb.ad.csueastbay.edu");
            index += 1;
            String sql = "DELETE FROM ADDRESSENTRYTABLE WHERE ID = " + index;


            PreparedStatement statement = conn.prepareStatement(sql);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("A user was deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * create scrollPane associated with JList
     */
    public void initialize() {
        //create scrollPane associated with JList
        //JScrollPane scrollPane = new JScrollPane(this.addressEntryJList);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = addressEntryJList.getSelectedIndex();
                System.out.println("Index Value " + index);

                if(index != -1)//something is selected otherwise do nothing

                {   //retrieve the DeffaultListModel associated
                    // with our JList and remove from it the AddressEntry at this index
                    deleteFromDatabase(index);
//                    ((DefaultListModel<AddressEntry>) (addressEntryJList.getModel())).remove(index);


                    // NOTE in your project 2 you will also remove it from your AddressBook.addressEntryList
                    // AND ALSO remove it from the associated database table
                    System.out.println("Index Value " + index);

                }
            }
        });
        listingJScrollPane.setColumnHeaderView(removeButton);
    }
}
