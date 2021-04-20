package main;

import java.awt.*;
import java.io.IOException;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;



class Main {


    public static Firestore db;
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, URISyntaxException {
//        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        String decodedPath = URLDecoder.decode(path, "UTF-8");

        InputStream serviceAccount = Main.class.getResourceAsStream("/" + "key.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();

        SslContextBuilder builder = GrpcSslContexts.forClient();

        ApiFuture<QuerySnapshot> query = db.collection("clients").orderBy("companyName").get();


        db.collection("orders").orderBy("datetime", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        System.err.println("Listen failed: " + e);
                        return;
                    }

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                String id = dc.getDocument().getId();
                                String client = String.valueOf(dc.getDocument().getData().get("clientName"));
                                String agent = String.valueOf(dc.getDocument().getData().get("sellerName"));
                                String date = String.valueOf(dc.getDocument().getData().get("datetime"));
                                String notes = String.valueOf(dc.getDocument().getData().get("notes"));
                                int isOrder = Integer.parseInt(String.valueOf(dc.getDocument().getData().get("isOrder")));
                                String doneDate = String.valueOf(dc.getDocument().getData().get("doneDate"));
                                Order order = new Order(id,client,agent,date,isOrder,notes,doneDate);
                                ArrayList<String> orderCount = (ArrayList<String>) dc.getDocument().getData().get("orderCount");
                                ArrayList<String> orderProduct = (ArrayList<String>) dc.getDocument().getData().get("orderProduct");
                                for (int i=0; i < orderCount.size(); i++) {
                                    order.insertProduct(orderProduct.get(i), orderCount.get(i));
                                }
                                if (isOrder == 0)
                                    MainFrame.addOrder(order);
                                else if (isOrder == 1)
                                    MainFrame.addPendingOrder(order);
                                else if (isOrder == 2)
                                    MainFrame.addDoneOrder(order);
                                break;

                            case MODIFIED:
                                System.out.println("Modified data: " + dc.getDocument().getData());
                                break;
                            case REMOVED:
                                System.out.println("Removed data: " + dc.getDocument().getData());
                                break;
                            default:
                                break;
                        }
                    }
                });



//        db.collection("clients")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot snapshots,
//                                        @Nullable FirestoreException e) {
//                        if (e != null) {
//                            System.err.println("Listen failed: " + e);
//                            return;
//                        }
//
//                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
//                            switch (dc.getType()) {
//                                case ADDED:
//                                    String id = dc.getDocument().getId();
//                                    String companyName = String.valueOf(dc.getDocument().getData().get("companyName"));
//                                    String phoneNumber = "None";
//                                    String address = String.valueOf(dc.getDocument().getData().get("address"));
//                                    String typeOfBusiness = String.valueOf(dc.getDocument().getData().get("typeOfBusiness"));
//                                    Client client = new Client(id,companyName,phoneNumber,address,typeOfBusiness);
//                                    MainFrame.addClient(client);
//                                case MODIFIED:
//                                    System.out.println("Modified data: " + dc.getDocument().getData());
//                                    break;
//                                case REMOVED:
//                                    System.out.println("Removed data: " + dc.getDocument().getData());
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    }
//                });


//        db.collection("products")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot snapshots,
//                                        @Nullable FirestoreException e) {
//                        if (e != null) {
//                            System.err.println("Listen failed: " + e);
//                            return;
//                        }
//
//                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
//                            switch (dc.getType()) {
//                                case ADDED:
//                                    String id = (String) dc.getDocument().getData().get("id");
//                                    String branch = String.valueOf(dc.getDocument().getData().get("branch"));
//                                    String barcode = (String) dc.getDocument().getData().get("barcode");
//                                    String categoryID = (String) dc.getDocument().getData().get("categoryID");
//                                    String name = String.valueOf(dc.getDocument().getData().get("name"));
//                                    Product product = new Product(id,name,categoryID,barcode);
//                                    product.setBranch(branch);
//                                    MainFrame.addProduct(product);
//                                case MODIFIED:
//                                    System.out.println("Modified data: " + dc.getDocument().getData());
//                                    break;
//                                case REMOVED:
//                                    System.out.println("Removed data: " + dc.getDocument().getData());
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    }
//                });


        MainFrame frame = new MainFrame(db);
        LoadFrame loadFrame = new LoadFrame();
        _loadProducts();
        frame.prepareUI();
        _loadClients();
        _loadAgents();
        frame.setSize(1000, 600);
        frame.setResizable(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        loadFrame.dispose();
        frame.setTitle("Aqua Seller");
        frame.setVisible(true);
    }

    private static void _loadProducts() throws ExecutionException, InterruptedException {
        // creating categories with their subcategories
        ApiFuture<QuerySnapshot> query = db.collection("categories").get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (DocumentSnapshot document : documents) {
            String id = document.getId();
            String name = (String) Objects.requireNonNull(document.getData()).get("name");
            String parentID = (String) document.getData().get("parentID");
            Category category = new Category(id, name, parentID);
            MainFrame.insertToCategoryHashMap(category);
        }
        MainFrame.createSubcategories();

        // creating products and storing them to their categories

        ApiFuture<QuerySnapshot> queryProducts = db.collection("products").orderBy("name").get();
        List<QueryDocumentSnapshot> documentsCateg = queryProducts.get().getDocuments();
        for (QueryDocumentSnapshot document : documentsCateg) {
            String id = document.getId();
            String aquaID = (String) document.getData().get("id");
            String name = (String) document.getData().get("name");
            String categoryID = (String) document.getData().get("categoryID");
            String barcode = (String) document.getData().get("barcode");
            String branch = (String) document.getData().get("branch");
            double price = 0;
            try{
                price = (double) document.getData().get("price");
            }catch (Exception exception){
                price = 0;
            }
            Product product = new Product(id, name, categoryID, barcode,price);
            product.setAquaID(aquaID);
            product.setBranch(branch);
            MainFrame.insertToProductHashMap(product);
            MainFrame.addProductJCB(product);
        }
    }

    private static void _loadAgents() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = db.collection("agents").get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (DocumentSnapshot document : documents){
            String id = document.getId();
            String firstName = (String) document.getData().get("firstName");
            String lastName = (String) document.getData().get("lastName");
            String phoneNumber = (String) document.getData().get("phoneNumber");
            String address = (String) document.getData().get("address");
            ArrayList<String> hasClients = (ArrayList<String>) document.getData().get("hasClients");
            Agent agent = new Agent(id,firstName,lastName,phoneNumber,address,hasClients);
            MainFrame.addAgent(agent);
        }
    }


    private static void _loadClients() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = db.collection("clients").orderBy("companyName").get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (DocumentSnapshot document : documents) {
            String id = document.getId();
            String companyName = (String) document.getData().get("companyName");
            String phoneNumber = (String) document.getData().get("phoneNumber");
            String address = (String) document.getData().get("address");
            String typeOfBusiness = (String) document.getData().get("typeOfBusiness");
            String tin = (String) document.getData().get("tin");
            String town = (String) document.getData().get("town");
            Client client = new Client(id,companyName,phoneNumber,address,town,tin,typeOfBusiness);
            MainFrame.addClient(client);
        }
    }

}