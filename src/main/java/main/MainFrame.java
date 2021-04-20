package main;// TODO: Client add/edit/delete - DONE
//       Agent add/edit/delete - DONE
//       Product add/edit/delete - DONE
//       Category add/edit/delete - DONE
//       Add order with .xlsx file
//       Order add/edit/delete

// FIXME: Sorting stuff
//        Exceptions
//        Update clientsList for agent
//        Fix done order date ffs

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import org.jdesktop.swingx.autocomplete.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

//final JPopupMenu popupMenu = new JPopupMenu();
//        JMenuItem deleteItem = new JMenuItem("Delete");

public class MainFrame extends JFrame{

    private String selectedTreeCategory;
    private static DefaultMutableTreeNode selectedNode;

    private final JPopupMenu categoryPopup;
    private final JPopupMenu productPopup;
    private final JPopupMenu categoryAndProductPopup;
    private JTree tree;
    private static DefaultTreeModel dtm;
    private JScrollPane pendingOrdersScrollPane;
    private JScrollPane doneOrdersScrollPane;

    private static ArrayList<Order> ordersList;
    private static ArrayList<Order> doneOrdersList;
    private static ArrayList<Order> pendingOrdersList;

    private static HashMap<String,Category> categoryHashMap;
    private static HashMap<String,Product> productHashMap;
    private static HashMap<String,Client> clientsHashMap;
    private static HashMap<String,Order> ordersHashMap;
    private static HashMap<String,Agent> agentsHashMap;

    //FIXME: change to more efficient way (storing admin,clients by their id in database)
    public static HashMap<String,Agent> agentsHashMapNames;
    public static HashMap<String,Client> clientsHashMapNames;

    private static ArrayList<Product> productsList;
    private static ArrayList<Client> clientsList;
    private static ArrayList<Agent> agentsList;

    private final String[] ordersColumns;
    private final String[] pendingOrdersColumns;
    private final String[] doneOrdersColumns;
    private final String[] clientsColumns;
    private final String[] agentsColumns;
    private final String[] productsColumns;

    private static String[][] ordersData;
    private static String[][] pendingOrdersData;
    private static String[][] doneOrdersData;

    private static DefaultTableModel clientsModel;
    private static DefaultTableModel agentsModel;
    private static DefaultTableModel productsModel;
    private static DefaultTableModel ordersModel;
    private static DefaultTableModel doneOrdersModel;
    private static DefaultTableModel pendingOrdersModel;

    private static String[][] clientsData;
    private static String[][] productsData;
    private static String[][] agentsData;

    private static JTable ordersTable;
    private static JTable pendingOrdersTable;
    private static JTable doneOrdersTable;

    private static JTable clientsTable;
    private static JTable agentsTable;
    private static JTable productsTable;

    private static JTextField searchField;

    private static Firestore db;
    private final ButtonListener myListener;


    private static JComboBox<Client> clientsJCB;
    private static JComboBox<Agent> agentsJCB;
    private static JComboBox<Product> productJComboBox;

    private TableRowSorter<TableModel> sorter;
    private TableRowSorter<TableModel> sorter2;
    private TableRowSorter<TableModel> sorter3;

    //Menu stuff
    private JMenuBar menuBar = new JMenuBar();
    private JMenuItem newOrderItem,loadItem,saveAsItem,clientsItem,addClientItem,agentsItem,
            addAgentItem,productsItem,addProductItem,statisticsItem,exitItem,deleteItem,
            editItem,printItem,editItem2,deleteItem2,addCategoryItem,addProductToCategoryItem,
            editItem3,deleteItem3,addCategoryItem3,addProductToCategoryItem3,promotionsItem;

    private static FileNameExtensionFilter fileFilter;
    JTabbedPane tabbedPane = new JTabbedPane();

    public MainFrame(Firestore db) {
        fileFilter = new FileNameExtensionFilter("xlsx Files (*.xlsx)","xlsx");

        MainFrame.db = db;
        categoryPopup = new JPopupMenu();
        productPopup = new JPopupMenu();
        categoryAndProductPopup = new JPopupMenu();

        searchField = new HintTextField("Search");
        searchField.setColumns(35);

        deleteItem = new JMenuItem("Delete");
        editItem = new JMenuItem("Edit");
        deleteItem2 = new JMenuItem("Delete");
        editItem2 = new JMenuItem("Edit");
        deleteItem3 = new JMenuItem("Delete");
        editItem3 = new JMenuItem("Edit");
        addCategoryItem = new JMenuItem("Add category");
        addProductToCategoryItem = new JMenuItem("Add product");
        addCategoryItem3 = new JMenuItem("Add category");
        addProductToCategoryItem3 = new JMenuItem("Add product");
//        statisticsItem = new JMenuItem("Statistics");

        //create tree popups
        TreeListener treeListener = new TreeListener();
        addCategoryItem.addActionListener(treeListener);
        addProductToCategoryItem.addActionListener(treeListener);
        addCategoryItem3.addActionListener(treeListener);
        addProductToCategoryItem3.addActionListener(treeListener);
        editItem.addActionListener(treeListener);
        editItem2.addActionListener(treeListener);
        editItem3.addActionListener(treeListener);
        deleteItem.addActionListener(treeListener);
        deleteItem2.addActionListener(treeListener);
        deleteItem3.addActionListener(treeListener);

        categoryPopup.add(addCategoryItem);
        categoryPopup.add(editItem);
        categoryPopup.add(deleteItem);
        productPopup.add(addProductToCategoryItem);
        productPopup.add(editItem2);
        productPopup.add(deleteItem2);
        categoryAndProductPopup.add(addCategoryItem3);
        categoryAndProductPopup.add(addProductToCategoryItem3);
        categoryAndProductPopup.add(editItem3);
        categoryAndProductPopup.add(deleteItem3);

        //end tree popups

        // create button listener
        myListener = new ButtonListener();
        // create menu and menu items
        JMenu fileMenu = new JMenu("File");
        JMenu clientMenu = new JMenu("Clients");
        JMenu agentMenu = new JMenu("Agents");

        fileMenu.setMnemonic('F');
        clientMenu.setMnemonic('C');
        agentMenu.setMnemonic('A');

        newOrderItem = fileMenu.add("New order");
        newOrderItem.addActionListener(myListener);
        statisticsItem = fileMenu.add("Statistics");
        statisticsItem.addActionListener(myListener);
        promotionsItem = fileMenu.add("Promotions");
        promotionsItem.addActionListener(myListener);
        fileMenu.addSeparator();

        loadItem = fileMenu.add("Load file");
        loadItem.addActionListener(myListener);


        fileMenu.addSeparator();

//        statisticsItem = fileMenu.add("Statistics");
//        statisticsItem.addActionListener(myListener);

        printItem = fileMenu.add("Print");
        printItem.addActionListener(myListener);
        printItem.setAccelerator(KeyStroke.getKeyStroke('P', Event.CTRL_MASK));

        fileMenu.addSeparator();

        exitItem = fileMenu.add("Exit");
        exitItem.addActionListener(myListener);

        clientsItem = clientMenu.add("Clients");
        clientsItem.addActionListener(myListener);

        addClientItem = clientMenu.add("Add client");
        addClientItem.addActionListener(myListener);

        agentsItem = agentMenu.add("Agents");
        agentsItem.addActionListener(myListener);

        addAgentItem = agentMenu.add("Add agent");
        addAgentItem.addActionListener(myListener);

        menuBar.add(fileMenu);
        menuBar.add(clientMenu);
        menuBar.add(agentMenu);

        ordersList = new ArrayList<>();
        doneOrdersList = new ArrayList<>();
        pendingOrdersList = new ArrayList<>();

        agentsList = new ArrayList<>();
        clientsList = new ArrayList<>();
        productsList = new ArrayList<>();
        doneOrdersList = new ArrayList<>();

        categoryHashMap = new HashMap<>();
        productHashMap = new HashMap<>();
        clientsHashMap = new HashMap<>();
        ordersHashMap = new HashMap<>();
        agentsHashMap = new HashMap<>();

        agentsHashMapNames = new HashMap<>();
        clientsHashMapNames = new HashMap<>();

        agentsData = new String[][] {};
        clientsData = new String[][] {};
        productsData = new String[][] {};

        ordersColumns = new String[] {"Order ID","Client","Agent","Date"};
        pendingOrdersColumns = new String[] {"Order ID", "Client", "Agent", "Date"};
        doneOrdersColumns = new String[] {"Order ID", "Client", "Agent","Date","Done Date"};
        clientsColumns = new String[] {"Client ID", "Company name","TIN","Address","Town","Phone number","Type Of Business"};
        productsColumns = new String[] {"Product ID", "Product name", "Category","Barcode"};
        agentsColumns = new String[] {"Agent ID","First name","Last name","Phone number","Address"};
        ordersData = new String[][] {};

        ordersTable = new JTable(ordersData,ordersColumns);
        pendingOrdersTable = new JTable(pendingOrdersData,pendingOrdersColumns);
        doneOrdersTable = new JTable(doneOrdersData,doneOrdersColumns);

        clientsTable = new JTable(clientsData,clientsColumns);
        agentsTable = new JTable(agentsData,agentsColumns);
        productsTable = new JTable(productsData,productsColumns);


        ordersTable.getTableHeader().setReorderingAllowed(false);
        ordersModel = new DefaultTableModel(ordersColumns,0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ordersTable.setModel(ordersModel);
        ordersTable.setAutoCreateRowSorter(true);
        sorter = new TableRowSorter<>(ordersModel);
        ordersTable.setRowSorter(sorter);

        pendingOrdersModel =  new DefaultTableModel(pendingOrdersColumns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pendingOrdersTable.setModel(pendingOrdersModel);
        pendingOrdersTable.setAutoCreateRowSorter(true);
        pendingOrdersTable.getTableHeader().setReorderingAllowed(false);
        sorter2 = new TableRowSorter<>(pendingOrdersModel);
        pendingOrdersTable.setRowSorter(sorter2);

        doneOrdersModel = new DefaultTableModel(doneOrdersColumns,0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        doneOrdersTable.setModel(doneOrdersModel);
        doneOrdersTable.setAutoCreateRowSorter(true);
        doneOrdersTable.getTableHeader().setReorderingAllowed(false);
        sorter3 = new TableRowSorter<>(doneOrdersModel);
        doneOrdersTable.setRowSorter(sorter3);

        clientsModel = new DefaultTableModel(clientsColumns,0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        clientsTable.getTableHeader().setReorderingAllowed(false);
        clientsTable.setModel(clientsModel);
        clientsTable.setAutoCreateRowSorter(true);


        agentsModel = new DefaultTableModel(agentsColumns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        agentsTable.getTableHeader().setReorderingAllowed(false);
        agentsTable.setModel(agentsModel);
        agentsTable.setAutoCreateRowSorter(true);

        productsModel = new DefaultTableModel(productsColumns,0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productsTable.getTableHeader().setReorderingAllowed(false);
        productsTable.setModel(productsModel);
    }

    public void prepareUI() {
        clientsJCB = new JComboBox<>();
        agentsJCB = new JComboBox<>();
        productJComboBox = new JComboBox<>();


        AutoCompleteDecorator.decorate(productJComboBox);
        AutoCompleteDecorator.decorate(agentsJCB);
        AutoCompleteDecorator.decorate(clientsJCB);
        clientsJCB.setSelectedItem("");
        agentsJCB.setSelectedItem("");
        productJComboBox.setSelectedItem("");


//        System.out.println(productsList.size());
//        System.out.println(clientsList.size());
//        System.out.println(agentsList.size());


        for (Product product: productsList){
            productJComboBox.addItem(product);
        }
//        for (Client client : clientsList){
//        for (Client client : clientsList){
//            clientsJCB.addItem(client);
//        }
//
//        for (Agent agent : agentsList){
//            agentsJCB.addItem(agent);
//        }
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(menuBar);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Products");
        //creating tree start
        for (String key:categoryHashMap.keySet()){
            if (categoryHashMap.get(key).getParentID().isEmpty()){
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(categoryHashMap.get(key));
                root.add(node);
                _addNode(categoryHashMap.get(key).getSubcategoriesList(),node);
            }
        }

        tree = new JTree(root);
        tree.setRootVisible(true);
        dtm = (DefaultTreeModel) tree.getModel();

        JScrollPane ordersScrollPane = new JScrollPane(ordersTable);
        JScrollPane treeScrollPane = new JScrollPane(tree);
        JPanel searchPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPane.add(searchField);
        searchPane.setPreferredSize(new Dimension(100,27));

        pendingOrdersScrollPane = new JScrollPane(pendingOrdersTable);

        doneOrdersScrollPane = new JScrollPane(doneOrdersTable);

        tabbedPane.add("Orders", ordersScrollPane);
        tabbedPane.add("Pending Orders",pendingOrdersScrollPane);
        tabbedPane.add("Done Orders",doneOrdersScrollPane);

        this.add(searchPane,BorderLayout.SOUTH);
        this.add(tabbedPane, BorderLayout.CENTER);
        this.add(treeScrollPane,BorderLayout.EAST);

        ordersTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String orderID = (String) table.getValueAt(row,0);
                    MainFrame._showOrder(row,orderID);
                }
            }
        });

        doneOrdersTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String orderID = (String) table.getValueAt(row,0);
                    MainFrame._showDoneOrder(row,orderID);
                }
            }
        });

        pendingOrdersTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String orderID = (String) table.getValueAt(row,0);
                    MainFrame._showPendingOrder(row,orderID);
                }
            }
        });

        tree.addTreeSelectionListener(e -> {
            try {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode.getUserObject() instanceof Product) {
                    MainFrame.selectedNode = selectedNode;
                }
            }catch (Exception exception){
                System.out.println("hay");
            }
        });

        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int selRow = tree.getRowForLocation(e.getX(), e.getY());
                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    tree.setSelectionPath(selPath);
                    if (selRow > -1) {
                        tree.setSelectionRow(selRow);
                        selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                        if (selectedNode.getUserObject() instanceof Product) {
                            Product product = (Product) selectedNode.getUserObject();
                            _showProduct(product);
                        }else{
                            Category category = (Category) selectedNode.getUserObject();
                            if (category.getSubcategoriesList().size() > 0 && category.getProductsList().size() == 0){
                                categoryPopup.show(e.getComponent(), e.getX(), e.getY());
                                selectedTreeCategory = category.getId();
                            }
                            else if (category.getSubcategoriesList().size() == 0 && category.getProductsList().size() > 0) {
                                productPopup.show(e.getComponent(), e.getX(), e.getY());
                                selectedTreeCategory = category.getId();
                            }else if (category.getSubcategoriesList().size() == 0 && category.getProductsList().size() == 0){
                                categoryAndProductPopup.show(e.getComponent(),e.getX(),e.getY());
                                selectedTreeCategory = category.getId();
                            }
                        }
                    }
                }
            }
        };
        tree.addMouseListener(ml);

        agentsTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String id = (String) table.getValueAt(row,0);
                    MainFrame._showAgent(id,row);
                }
            }
        });


        clientsTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String id = (String) table.getValueAt(row,0);
                    MainFrame._showClient(id,row);
                }
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update(e);
            }
            public void changedUpdate(DocumentEvent e) {
                update(e);
            }
            public void removeUpdate(DocumentEvent e) {
                update(e);
            }
            public void update(DocumentEvent e) {
                String text = searchField.getText();
                if (text.trim().length() == 0 || text.equals("Search")) {
                    sorter.setRowFilter(null);
                    sorter2.setRowFilter(null);
                    sorter3.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    sorter2.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    sorter3.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

    }

    class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonName = e.getActionCommand();
            if (buttonName.equals("Add client")) {
                _openNewClientFrame();
            } else if (buttonName.equals("Clients")) {
                _openClientsFrame();
            } else if (buttonName.equals("Agents")) {
                _openAgentsFrame();
            } else if (buttonName.equals("Add agent")) {
                _openNewAgentFrame();
            } else if (buttonName.equals("Add product")) {
                _openNewProductFrame("hey");
            } else if (buttonName.equals("Products")) {
                _openProductsFrame();
            } else if (buttonName.equals("Print")) {
                try {
                    _printOrders();
                } catch (IOException | PrinterException ioException) {
                    ioException.printStackTrace();
                }
            } else if (buttonName.equals("Load file")) {
                _loadFromFile(loadItem);
            }else if (buttonName.equals("Promotions")){
                _openPromotionFrame(new Promotion("",""));
            }else if(buttonName.equals("Exit")){
                dispose();
                System.exit(0);
            }else if(buttonName.equals("New order")){
                _openNewOrderFrame();
            }else if(buttonName.equals("Statistics")){
                String uri = "http://aquaaromatics.azurewebsites.net/stats/api/v1.0/general";
                try {
                    Desktop.getDesktop().browse(new URI(uri));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (URISyntaxException uriSyntaxException) {
                    uriSyntaxException.printStackTrace();
                }
            }
        }
    }

    class TreeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonName = e.getActionCommand();
            if (buttonName.equals("Add category")) {
                _openNewCategoryFrame(selectedTreeCategory);
            } else if (buttonName.equals("Add product")) {
                _openNewProductFrame(selectedTreeCategory);
            }else if (buttonName.equals("Edit")) {
                _showCategory(selectedTreeCategory);
            }else if(buttonName.equals("Delete")){
                Category category = categoryHashMap.get(selectedTreeCategory);
                System.out.println("Deleted pressed");
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to" +
                                " delete "+category.getName()+" ?",
                        "Confirmation", JOptionPane.OK_CANCEL_OPTION);
                System.out.println(result);
                if (result == 0) {
                _deleteCategoryFromTree();
                _deleteCategory(categoryHashMap.get(selectedTreeCategory));
                }
            }
        }
    }

    // ------ my methods ------



    private void _openNewClientFrame(){
        NewClientFrame newClientFrame = new NewClientFrame();
        newClientFrame.setTitle("New Client");
        newClientFrame.prepareUI();
        newClientFrame.setSize(330,250);
        newClientFrame.setResizable(true);
        newClientFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        newClientFrame.setLocation(dim.width/2-newClientFrame.getSize().width/2, dim.height/2-newClientFrame.getSize().height/2);
        newClientFrame.setVisible(true);
        newClientFrame.setAlwaysOnTop(true);
    }

    private void _openNewOrderFrame(){
        NewOrderFrame newOrderFrame = new NewOrderFrame(clientsJCB,agentsJCB,productJComboBox);
        newOrderFrame.setTitle("New Order");
        newOrderFrame.prepareUI();
        newOrderFrame.setSize(600,250);
        newOrderFrame.setAlwaysOnTop(true);
        newOrderFrame.setResizable(true);
        newOrderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newOrderFrame.setVisible(true);
        newOrderFrame.setAlwaysOnTop(true);
    }

    private void _openPromotionFrame(Promotion promotion){
        PromotionFrame promotionFrame = new PromotionFrame(promotion);
        promotionFrame.setTitle("Promotions");
        promotionFrame.prepareUI();
        promotionFrame.setSize(400,150);
        promotionFrame.setResizable(true);
        promotionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        promotionFrame.setLocation(dim.width/2-promotionFrame.getSize().width/2, dim.height/2-promotionFrame.getSize().height/2);
        promotionFrame.setVisible(true);
        promotionFrame.setAlwaysOnTop(true);
    }


    private void _openNewOrderFromXLSXFrame(String filePath){
        NewOrderFromXLSXFrame newOrderFromXLSXFrame = new NewOrderFromXLSXFrame(agentsJCB,clientsJCB,filePath);
        newOrderFromXLSXFrame.setTitle(filePath);
        newOrderFromXLSXFrame.prepareUI();
        newOrderFromXLSXFrame.setSize(500,200);
        newOrderFromXLSXFrame.setResizable(true);
        newOrderFromXLSXFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        newOrderFromXLSXFrame.setLocation(dim.width/2-newOrderFromXLSXFrame.getSize().width/2, dim.height/2-newOrderFromXLSXFrame.getSize().height/2);
        newOrderFromXLSXFrame.setVisible(true);
        newOrderFromXLSXFrame.setAlwaysOnTop(true);
    }

    private void _openNewAgentFrame(){
        NewAgentFrame newAgentFrame = new NewAgentFrame(clientsJCB);
        newAgentFrame.setTitle("New Agent");
        newAgentFrame.prepareUI();
        newAgentFrame.setSize(400,350);
        newAgentFrame.setResizable(true);
        newAgentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        newAgentFrame.setLocation(dim.width/2-newAgentFrame.getSize().width/2, dim.height/2-newAgentFrame.getSize().height/2);
        newAgentFrame.setVisible(true);
        newAgentFrame.setAlwaysOnTop(true);
    }

    private void _openClientsFrame(){
        ClientsFrame clientsFrame = new ClientsFrame(clientsTable);
        clientsFrame.setTitle("Clients");
            clientsFrame.prepareUI();
        clientsFrame.setSize(600,300);
        clientsFrame.setSize(600,300);
        clientsFrame.setResizable(true);
        clientsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        clientsFrame.setLocation(dim.width/2-clientsFrame.getSize().width/2, dim.height/2-clientsFrame.getSize().height/2);
        clientsFrame.setVisible(true);
        clientsFrame.setAlwaysOnTop(true);
    }

    private void _openAgentsFrame(){
        AgentsFrame agentsFrame = new AgentsFrame(agentsTable);
        agentsFrame.setTitle("Agents");
        agentsFrame.prepareUI();
        agentsFrame.setSize(600,300);
        agentsFrame.setResizable(true);
        agentsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        agentsFrame.setLocation(dim.width/2-agentsFrame.getSize().width/2, dim.height/2-agentsFrame.getSize().height/2);
        agentsFrame.setVisible(true);
        agentsFrame.setAlwaysOnTop(true);
    }

    private void _openNewProductFrame(String categoryID){
        NewProductFrame newProductFrame = new NewProductFrame(categoryID);
        newProductFrame.setTitle("New Product");
        newProductFrame.prepareUI();
        newProductFrame.setSize(270,175);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        newProductFrame.setLocation(dim.width/2-newProductFrame.getSize().width/2, dim.height/2-newProductFrame.getSize().height/2);
        newProductFrame.setResizable(true);
        newProductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newProductFrame.setVisible(true);
        newProductFrame.setAlwaysOnTop(true);
    }

    private void _openNewCategoryFrame(String parentID){
        NewCategoryFrame newCategoryFrame = new NewCategoryFrame(parentID);
        newCategoryFrame.setTitle("New Category");
        newCategoryFrame.prepareUI();
        newCategoryFrame.setSize(270,140);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        newCategoryFrame.setLocation(dim.width/2-newCategoryFrame.getSize().width/2, dim.height/2-newCategoryFrame.getSize().height/2);
        newCategoryFrame.setResizable(true);
        newCategoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newCategoryFrame.setVisible(true);
        newCategoryFrame.setAlwaysOnTop(true);
    }


    private void _openProductsFrame(){
        ProductsFrame productsFrame = new ProductsFrame(productsTable);
        productsFrame.setTitle("Products");
        productsFrame.prepareUI();
        productsFrame.setSize(340,270);
        productsFrame.setResizable(true);
        productsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        productsFrame.setLocation(dim.width/2-productsFrame.getSize().width/2, dim.height/2-productsFrame.getSize().height/2);
        productsFrame.setVisible(true);
        productsFrame.setAlwaysOnTop(true);
    }

    public static void addClient(Client client){
        clientsList.add(client);
        clientsHashMap.put(client.getId(),client);
        clientsHashMapNames.put(client.getCompanyName(),client);
        clientsModel.addRow(client.toRow());
        clientsJCB.addItem(client);
    }


    public static void addClientToDb(Client client){
        Map<String,Object> data = new HashMap<>();
        data.put("companyName", client.getCompanyName());
        data.put("phoneNumber",client.getPhoneNumber());
        data.put("address",client.getAddress());
        data.put("town",client.getTown());
        data.put("tin",client.getTin());
        data.put("typeOfBusiness",client.getTypeOfBusiness());
        db.collection("clients").document(client.getId()).set(data);
    }

    public static void addAgent(Agent agent){
        agentsList.add(agent);
        agentsHashMap.put(agent.getId(),agent);
        agentsHashMapNames.put(agent.getFirstName()+" "+agent.getLastName(),agent);
        agentsModel.addRow(agent.toRow());
        agentsJCB.addItem(agent);
    }

    public static void deleteAgent(Agent agent,int index){
        int modelRow = agentsTable.convertRowIndexToModel(index);
        agentsModel.removeRow(modelRow);
        agentsJCB.removeItem(agent);
        agentsHashMap.remove(agent.getId());
        db.collection("agents").document(agent.getId()).delete();

    }

    public static void addAgentToDb(Agent agent){
        Map<String,Object> data = new HashMap<>();
        data.put("address", agent.getAddress());
        data.put("firstName",agent.getFirstName());
        data.put("lastName",agent.getLastName());
        data.put("phoneNumber",agent.getPhoneNumber());
        data.put("hasClients",agent.getHasClients());
        DocumentReference ref = db.collection("agents").document();
        agent.setId(ref.getId());
        db.collection("agents").document(agent.getId()).set(data);
    }

    public static void addOrderToDb(String clientName,int isOrder,String notes,String sellerName,ArrayList<String> orderProduct
    ,ArrayList<String> orderCount){
        Map<String,Object> data = new HashMap<>();
        data.put("clientName",clientName);
        data.put("isOrder",isOrder);
        data.put("doneDate","");
        data.put("notes",notes);
        data.put("sellerName",sellerName);
        data.put("orderCount",orderCount);
        data.put("orderProduct",orderProduct);
        Timestamp timestamp = Timestamp.now();
        data.put("datetime",timestamp);
        DocumentReference ref = db.collection("orders").document();
        db.collection("orders").document(ref.getId()).set(data);
    }

    public static void addProductJCB(Product product){
        System.out.println(product.getName());
//        productJComboBox.addItem(product);
        productsList.add(product);
    }

    public static void addProduct(Product product){
        productsList.add(product);
//        productJComboBox.addItem(product);
        System.out.println(product.getName());
        productsModel.addRow(product.toRow());
        productHashMap.put(product.getId(),product);
        categoryHashMap.get(product.getCategoryID()).insertProduct(product);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(product);
        selectedNode.add(node);
        dtm.nodesWereInserted(selectedNode,new int[] {selectedNode.getIndex(node)});
    }

    public static void addProductToDb(Product product){
        Map<String, Object> data = new HashMap<>();
        data.put("name", product.getName());
        data.put("id",product.getAquaID());
        data.put("barcode", product.getBarcode());
        data.put("categoryID",product.getCategoryID());
        data.put("branch",product.getBranch());
        DocumentReference ref = db.collection("products").document();
        product.setId(ref.getId());
        db.collection("products").document(product.getId()).set(data);
    }

    public static void deleteProduct(Product product,DefaultMutableTreeNode productNode){
        productJComboBox.removeItem(product);
        productHashMap.remove(product.getId());
        selectedNode = (DefaultMutableTreeNode) productNode.getParent();
        dtm.removeNodeFromParent(productNode);
        db.collection("products").document(product.getId()).delete();
    }

    private static void _deleteCategory(Category category){
//      categoryHashMap.remove(categoryID);
//      dtm.removeNodeFromParent(MainFrame.selectedNode);
        if (category.getSubcategoriesList().size() > 0){
            for (Category subcategory : category.getSubcategoriesList()){
                _deleteCategory(subcategory);
            }
        }else if(category.getProductsList().size() > 0 ){
            for (Product product : category.getProductsList()){
                productHashMap.remove(product.getId());
                db.collection("products").document(product.getId()).delete();
                System.out.println("Removed "+product.getName());
            }
        }
        categoryHashMap.remove(category.getId());
        db.collection("categories").document(category.getId()).delete();
        System.out.println("Removed "+category.getName());
//      selectedNode = (DefaultMutableTreeNode) selectedNode.getParent();
    }

    private static void _deleteCategoryFromTree(){
        dtm.removeNodeFromParent(MainFrame.selectedNode);
        selectedNode = (DefaultMutableTreeNode) selectedNode.getParent();
    }


    public static void addCategory(Category category){
        categoryHashMap.put(category.getId(),category);
        categoryHashMap.get(category.getParentID()).insertSubcategory(category);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(category);
        selectedNode.add(node);
        dtm.nodesWereInserted(selectedNode,new int[] {selectedNode.getIndex(node)});
    }

    public static void addCategoryToDb(Category category){
        Map<String, Object> data = new HashMap<>();
        data.put("parentID",category.getParentID());
        data.put("name",category.getName());
        DocumentReference ref = db.collection("categories").document();
        category.setId(ref.getId());
        db.collection("categories").document(category.getId()).set(data);
    }

    public static void addOrder(Order order){
        ordersList.add(order);
        ordersHashMap.put(order.getId(),order);
        ordersModel.addRow(order.toRow());
    }

    public static void addPendingOrder(Order order){
        pendingOrdersList.add(order);
        ordersHashMap.put(order.getId(),order);
        pendingOrdersModel.addRow(order.toRow());
    }

    public static void addDoneOrder(Order order){
        doneOrdersList.add(order);
        ordersHashMap.put(order.getId(),order);
        doneOrdersModel.addRow(order.toRowDone());
    }

    public static void deleteOrderFromDB(Order order){
        ordersHashMap.remove(order.getId());
        db.collection("orders").document(order.getId()).delete();
    }

    public static void deleteOrder(Integer index){
        int modelRow = ordersTable.convertRowIndexToModel(index);
        ordersModel.removeRow(modelRow);
    }

    public static void deleteDoneOrder(Integer index){
        int modelRow = doneOrdersTable.convertRowIndexToModel(index);
        doneOrdersModel.removeRow(modelRow);
    }

    public static void deletePendingOrder(Integer index){
        int modelRow = pendingOrdersTable.convertRowIndexToModel(index);
        pendingOrdersModel.removeRow(modelRow);
    }

    public static Product getProduct(int index){
        return productsList.get(index);
    }

//    public static void deleteProduct(int index){
//        productsModel.removeRow(index);
//        productsList.remove(index);
//    }

    private static void _showOrder(int index,String orderID) {
        Order selectedOrder = ordersHashMap.get(orderID);
        OrderFrame orderFrame = new OrderFrame(selectedOrder,index,agentsJCB,clientsJCB);
        orderFrame.setTitle("Order ID: "+selectedOrder.getId());
        orderFrame.prepareUI();
        orderFrame.setSize(850,350);
        orderFrame.setResizable(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        orderFrame.setLocation(dim.width/2-orderFrame.getSize().width/2, dim.height/2-orderFrame.getSize().height/2);
        orderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        orderFrame.setVisible(true);
    }

    private static void _showPendingOrder(int index,String orderID) {
        Order selectedOrder = ordersHashMap.get(orderID);
        PendingOrderFrame pendingOrderFrame = new PendingOrderFrame(selectedOrder,index,agentsJCB,clientsJCB);
        pendingOrderFrame.setTitle("Order ID: "+selectedOrder.getId());
        pendingOrderFrame.prepareUI();
        pendingOrderFrame.setSize(850,350);
        pendingOrderFrame.setResizable(true);
        pendingOrderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pendingOrderFrame.setVisible(true);
    }

    private static void _showDoneOrder(int index,String orderID) {
        Order selectedOrder = ordersHashMap.get(orderID);
        System.out.println(selectedOrder.getHasProducts());
        System.out.println(selectedOrder.getHasProducts());
        DoneOrderFrame doneOrderFrame = new DoneOrderFrame(selectedOrder,index,agentsJCB,clientsJCB);
        doneOrderFrame.setTitle("Done order ID: "+selectedOrder.getId());
        doneOrderFrame.prepareUI();
        doneOrderFrame.setSize(900,350);
        doneOrderFrame.setResizable(true);
        doneOrderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        doneOrderFrame.setVisible(true);
    }

    private static void _showProduct(Product product) {
        ProductFrame productFrame = new ProductFrame(product,selectedNode);
        productFrame.setTitle("Product: "+product.getName());
        productFrame.prepareUI();
        productFrame.setSize(500,250);
        productFrame.setResizable(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        productFrame.setLocation(dim.width/2-productFrame.getSize().width/2, dim.height/2-productFrame.getSize().height/2);
        productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productFrame.setVisible(true);
        productFrame.setAlwaysOnTop(true);
    }

    private static void _showCategory(String categoryID) {
        Category category = categoryHashMap.get(categoryID);
        CategoryFrame categoryFrame = new CategoryFrame(category);
        categoryFrame.setTitle("Category: "+category.getName());
        categoryFrame.prepareUI();
        categoryFrame.setSize(475,220);
        categoryFrame.setResizable(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        categoryFrame.setLocation(dim.width/2-categoryFrame.getSize().width/2, dim.height/2-categoryFrame.getSize().height/2);
        categoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        categoryFrame.setVisible(true);
        categoryFrame.setAlwaysOnTop(true);
    }

    public static void _showClient(String id,int index) {
        Client selectedClient = clientsHashMap.get(id);
        ClientFrame clientFrame = new ClientFrame(selectedClient,index);
        clientFrame.setTitle("Company's name: "+selectedClient.getCompanyName());
        clientFrame.prepareUI();
        clientFrame.setSize(390,270);
        clientFrame.setResizable(true);
        clientFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        clientFrame.setAlwaysOnTop(true);
        clientFrame.setFocusable(true);
        clientFrame.setVisible(true);
        clientFrame.setAlwaysOnTop(true);
    }

    public static void _showAgent(String id,int index){
        Agent selectedAgent = agentsHashMap.get(id);
        AgentFrame agentFrame = new AgentFrame(selectedAgent,index,clientsJCB);
        agentFrame.setTitle("Agent's name "+selectedAgent.getFirstName());
        agentFrame.prepareUI();
        agentFrame.setSize(390,270);
        agentFrame.setResizable(true);
        agentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        agentFrame.setVisible(true);
        agentFrame.setAlwaysOnTop(true);
    }


    private static void  _printOrders() throws IOException, PrinterException {

        JTextPane printPane = new JTextPane();

        String htmlDocument="";

        int[] selection = ordersTable.getSelectedRows();
        for (int row : selection) {
            String id = (String) ordersTable.getValueAt(row,0);
            htmlDocument = htmlDocument + ordersHashMap.get(id).toHTML();
        }

        printPane.setContentType("text/html");
        printPane.setText(htmlDocument);
        printPane.print();

    }

    public static void insertToCategoryHashMap(Category category) {
        categoryHashMap.put(category.getId(),category);
    }

    public static void createSubcategories(){
        for (String key : categoryHashMap.keySet()) {
            String parentKey = categoryHashMap.get(key).getParentID();
            if (!parentKey.equals("")) {
                categoryHashMap.get(parentKey).insertSubcategory(categoryHashMap.get(key));
            }
        }
    }

    public static void insertToProductHashMap(Product product){
        productHashMap.put(product.getId(),product);
        categoryHashMap.get(product.getCategoryID()).insertProduct(product);
    }

    private static void _addNode(ArrayList<Category> subcategories,DefaultMutableTreeNode rootNode){
        for (Category category : subcategories){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(category);
            rootNode.add(node);
            if (category.getSubcategoriesList().size() > 0)
                _addNode(category.getSubcategoriesList(),node);
            else
                for (Product product:category.getProductsList()){
                    DefaultMutableTreeNode productNode = new DefaultMutableTreeNode(product);
                    node.add(productNode);
                }
        }
    }

    private void _loadFromFile(Component component){
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(fileFilter);
        fc.setAcceptAllFileFilterUsed(false);

        int returnVal = fc.showOpenDialog(component);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String fileName = fc.getSelectedFile().getPath();

            if (!fileName.isEmpty()) {
                _openNewOrderFromXLSXFrame(fileName);
            }

        }
    }

    public static void updateClient(Client client,int index){
        Map<String,Object> data = new HashMap<>();
        data.put("companyName", client.getCompanyName());
        data.put("phoneNumber",client.getPhoneNumber());
        data.put("address",client.getAddress());
        data.put("town",client.getTown());
        data.put("tin",client.getTin());
        data.put("typeOfBusiness",client.getTypeOfBusiness());
        index = clientsTable.convertRowIndexToModel(index);
        clientsModel.setValueAt(client.getId(),index,0);
        clientsModel.setValueAt(client.getCompanyName(),index,1);
        clientsModel.setValueAt(client.getTin(),index,2);
        clientsModel.setValueAt(client.getAddress(),index,3);
        clientsModel.setValueAt(client.getTown(),index,4);
        clientsModel.setValueAt(client.getPhoneNumber(),index,5);
        clientsModel.setValueAt(client.getTypeOfBusiness(),index,6);
        db.collection("clients").document(client.getId()).set(data);
    }
    public static void updateAgent(Agent agent,int index){
        Map<String,Object> data = new HashMap<>();
        data.put("firstName",agent.getFirstName());
        data.put("lastName",agent.getLastName());
        data.put("address",agent.getAddress());
        data.put("phoneNumber",agent.getPhoneNumber());
        data.put("hasClients",agent.getHasClients());
        index = agentsTable.convertRowIndexToModel(index);
        agentsModel.setValueAt(agent.getId(),index,0);
        agentsModel.setValueAt(agent.getFirstName(),index,1);
        agentsModel.setValueAt(agent.getLastName(),index,2);
        agentsModel.setValueAt(agent.getPhoneNumber(),index,3);
        agentsModel.setValueAt(agent.getAddress(),index,4);
        db.collection("agents").document(agent.getId()).set(data);
    }


    public static void updateOrder(Order order,int index){
        ArrayList<String> orderCount = new ArrayList<>();
        ArrayList<String> orderProduct = new ArrayList<>();
        for (String key : order.getHasProducts().keySet()){
            orderProduct.add(key);
            orderCount.add(String.valueOf(order.getHasProducts().get(key)));
        }

        db.collection("orders").document(order.getId()).update("clientName",order.getClient());
        db.collection("orders").document(order.getId()).update("sellerName",order.getAgent());
        db.collection("orders").document(order.getId()).update("notes",order.getNotes());
        db.collection("orders").document(order.getId()).update("orderProduct",orderProduct);
        db.collection("orders").document(order.getId()).update("orderCount",orderCount);
        if (order.getIsOrder() == 0) {
            index = ordersTable.convertRowIndexToModel(index);
            ordersTable.setValueAt(order.getClient(), index, 1);
            ordersTable.setValueAt(order.getAgent(), index, 2);
        }else if (order.getIsOrder() == 1){
            index = pendingOrdersTable.convertRowIndexToModel(index);
            pendingOrdersTable.setValueAt(order.getClient(), index, 1);
            pendingOrdersTable.setValueAt(order.getAgent(), index, 2);
        }else if (order.getIsOrder() == 2){
            index = doneOrdersTable.convertRowIndexToModel(index);
            doneOrdersTable.setValueAt(order.getClient(), index, 1);
            doneOrdersTable.setValueAt(order.getAgent(), index, 2);
        }
    }





    public static void updateOrderState(Order order){
        db.collection("orders").document(order.getId()).update("isOrder",order.getIsOrder());
        db.collection("orders").document(order.getId()).update("doneDate",order.getDoneDate());
    }

    public static void updateProduct(Product product){
       Map<String,Object> data = new HashMap<>();
       data.put("barcode",product.getBarcode());
       data.put("branch",product.getBranch());
       data.put("categoryID",product.getCategoryID());
       data.put("id",product.getAquaID());
       data.put("name",product.getName());
       data.put("price",product.getPrice());
       db.collection("products").document(product.getId()).set(data);
    }

    public static void updateCategory(Category category){
        db.collection("categories").document(category.getId()).update("name",category.getName());
    }

    public  static void deleteClient(Client client,int index){
        int modelRow = clientsTable.convertRowIndexToModel(index);
        clientsModel.removeRow(modelRow);
        clientsJCB.removeItem(client);
        clientsHashMap.remove(client.getId());
        db.collection("clients").document(client.getId()).delete();
    }

    public static void enableClientsTable(){
        clientsTable.setEnabled(true);
    }
    public static void disableClientsTable(){
        clientsTable.setEnabled(false);
    }
    public static void enableAgentsTable(){
        agentsTable.setEnabled(true);
    }
    public static void disableAgentsTable(){
        agentsTable.setEnabled(false);
    }

    public static String getProductName() {
        return ((Product) MainFrame.selectedNode.getUserObject()).getName();
    }
}

class HintTextField extends JTextField implements FocusListener {

    private final String hint;
    private boolean showingHint;

    public HintTextField(final String hint) {
        super(hint);
        this.hint = hint;
        this.showingHint = true;
        super.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText("");
            showingHint = false;
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText(hint);
            showingHint = true;
        }
    }

    @Override
    public String getText() {
        return showingHint ? "" : super.getText();
    }
}