import java.sql.*;
import java.util.*;
import java.time.*;
import java.math.BigDecimal;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String url = "jdbc:postgresql://localhost:5432/bill";
        String username = "postgres";
        String password = "vicky@252415";
        Main main = new Main();
        boolean loop = true;
        while (loop) {
            try {
                System.out.print("1)New Invoice\n2)Invoice List\n3)Paid Invoice List\n4)Invoice Detail\n5)Customer Debit and Purchase\n6)Performance of Products\n7)Exit\n\n");
                System.out.println("Enter your choice: ");
                int choice = sc.nextInt();
                Connection con = DriverManager.getConnection(url, username, password);
                switch (choice) {
                    case 1:
                        main.newinvoice(con);
                        break;
                    case 2:
                        main.invoiceList(con);
                        break;
                    case 3:
                        main.paidinvoice(con);
                        break;
                    case 4:
                        System.out.print("Enter the Invoice Number: ");
                        int num = sc.nextInt();
                        main.invoicedetails(con, num);
                        break;
                    case 5:
                        System.out.print("Enter the Customer Phone Number: ");
                        String phone_number = sc.next();
                        main.customerdebit(con, phone_number);
                        break;
                    case 6:
                        main.productsalesdetails(con);
                        break;
                    case 7:
                        loop=false;
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void newinvoice(Connection con) {
        try {
            int invno = getMaxInvoiceNumber(con) + 1;
            boolean loop = true;
            String pname, unit, ans = "name quantity unit  price     cost\n";
            ArrayList<String> product_name = new ArrayList<>();
            ArrayList<BigDecimal> quantity = new ArrayList<>();
            ArrayList<BigDecimal> per_unit_value = new ArrayList<>();
            ArrayList<String> all_unit = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;
            Scanner sc = new Scanner(System.in);

            while (loop) {
                boolean productFound = false;
                while (!productFound) {
                    System.out.print("Enter the product Name: ");
                    pname = sc.nextLine().toLowerCase();
                    String sql1 = "select unit, per_unit_price from product where product_name = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(sql1);
                    preparedStatement.setString(1, pname);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        productFound = true;
                        product_name.add(pname);
                        unit = resultSet.getString(1);
                        System.out.print("Enter the quantity in " + unit + ": ");
                        BigDecimal q = sc.nextBigDecimal();
                        sc.nextLine();

                        quantity.add(q);
                        all_unit.add(unit);
                        BigDecimal pup = resultSet.getBigDecimal(2);
                        per_unit_value.add(pup);
                        total = total.add(pup.multiply(q));
                        ans += pname + "\t" + q + "\t   " + unit + "\t" + String.format("%.2f", pup) + "\t  " + String.format("%.2f", pup.multiply(q)) + "\n";
                        System.out.println(ans);
                        System.out.println("\t\t\t\t  Total : " + String.format("%.2f", total) + "\n");
                    } else {
                        System.out.println("No such product found. Please enter a valid product name.");
                    }
                }

                System.out.println("Continue (Y/N)");
                char v = Character.toLowerCase(sc.next().charAt(0));
                sc.nextLine();
                if (v == 'n') {
                    LocalDateTime now = LocalDateTime.now();
                    Scanner s = new Scanner(System.in);
                    System.out.print("\nEnter the phone Number: ");
                    String ph = s.nextLine();
                    String phcheckquery = "Select count(*) from customer where phone_number=?";
                    PreparedStatement ps1 = con.prepareStatement(phcheckquery);
                    ps1.setString(1, ph);
                    ResultSet rs = ps1.executeQuery();
                    rs.next();
                    int count = rs.getInt(1);
                    if (count == 0) {
                        System.out.println("It is a new customer");
                        System.out.print("Enter the name of the customer : ");
                        String name = sc.next();
                        String insertname = "Insert into customer values(?,?)";
                        PreparedStatement ps2 = con.prepareStatement(insertname);
                        ps2.setString(1, ph);
                        ps2.setString(2, name);
                        ps2.executeUpdate();
                    }
                    System.out.print("Enter the discount percentage: ");
                    BigDecimal dis = s.nextBigDecimal();

                    System.out.println("\n\t\t\t\tABC Stores\n");
                    System.out.println("ph no : " + ph + "\t\t\t" + "Inv no :" + invno + "\n\t\t\t\t\t\t\t" + "Date : " + Timestamp.valueOf(now) + "\n\n" + ans);
                    System.out.println("\t\t\t  Sub Total : " + String.format("%.2f", total));
                    System.out.println("\t\t\t Discount(%): " + dis);
                    System.out.println("\t\t\t" + "_____________________");
                    BigDecimal final_total = total.subtract(total.multiply(dis).divide(new BigDecimal(100)));
                    System.out.println("\t\t\t  Total     : " + String.format("%.2f", final_total));
                    System.out.println("\t\t\t" + "_____________________");
                    System.out.print("Enter the paid amount: ");
                    BigDecimal pa = sc.nextBigDecimal();
                    String ps = "n";
                    BigDecimal balance = (final_total.subtract(pa));
                    if (balance.compareTo(BigDecimal.ZERO) == 0) {
                        ps = "y";
                    }
                    String sql2 = "Insert into invoice values(?,?,?,?,?,?,?,?)";
                    PreparedStatement preparedStatement2 = con.prepareStatement(sql2);
                    preparedStatement2.setInt(1, invno);
                    preparedStatement2.setString(2, ps);
                    preparedStatement2.setString(3, ph);
                    preparedStatement2.setBigDecimal(4, total);
                    preparedStatement2.setBigDecimal(5, dis);
                    preparedStatement2.setBigDecimal(6, final_total);
                    preparedStatement2.setString(7, String.valueOf(Timestamp.valueOf(now)));
                    preparedStatement2.setBigDecimal(8, balance);
                    preparedStatement2.executeUpdate();

                    for (int i = 0; i < product_name.size(); i++) {
                        String sql3 = "Insert into liveupdate values(?,?,?,?,?,?)";
                        PreparedStatement preparedStatement3 = con.prepareStatement(sql3);
                        preparedStatement3.setInt(1, invno);
                        preparedStatement3.setString(2, product_name.get(i));
                        preparedStatement3.setBigDecimal(3, quantity.get(i));
                        preparedStatement3.setBigDecimal(4, per_unit_value.get(i));
                        preparedStatement3.setBigDecimal(5, quantity.get(i).multiply(per_unit_value.get(i)));
                        preparedStatement3.setString(6, all_unit.get(i));
                        preparedStatement3.executeUpdate();
                        String sql4 = "select instock_quantity, sold_amount, quantity_sold from product where product_name = ?";
                        PreparedStatement preparedStatement4 = con.prepareStatement(sql4);
                        preparedStatement4.setString(1, product_name.get(i));
                        ResultSet resultSet1 = preparedStatement4.executeQuery();
                        resultSet1.next();
                        BigDecimal stock = resultSet1.getBigDecimal(1);
                        BigDecimal pre_quan = resultSet1.getBigDecimal(3);
                        BigDecimal pre_sales = resultSet1.getBigDecimal(2);
                        String sql5 = "Update product set instock_quantity = ?, quantity_sold = ?, sold_amount = ? where product_name = ?";
                        PreparedStatement preparedStatement5 = con.prepareStatement(sql5);
                        preparedStatement5.setBigDecimal(1, stock.subtract(quantity.get(i)));
                        preparedStatement5.setBigDecimal(2, pre_quan.add(quantity.get(i)));
                        BigDecimal totalAmount = quantity.get(i).multiply(per_unit_value.get(i));
                        BigDecimal discountAmount = totalAmount.multiply(dis).divide(new BigDecimal(100));
                        BigDecimal finalSalesAmount = totalAmount.subtract(discountAmount);
                        preparedStatement5.setBigDecimal(3, pre_sales.add(finalSalesAmount));
                        preparedStatement5.setString(4, product_name.get(i));
                        preparedStatement5.executeUpdate();
                    }
                    loop = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int getMaxInvoiceNumber(Connection con) throws SQLException {
        int maxInvoiceNumber = 0;
        String sql = "SELECT MAX(invoice_id) FROM invoice";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            maxInvoiceNumber = rs.getInt(1);
        }
        return maxInvoiceNumber;
    }
    public void invoiceList(Connection con) {
        try {
            String sql6 = "SELECT invoice_id, final_total, phone_number, paid_status,balance FROM invoice";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql6);
            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            System.out.println("Inv.no\tTotal\t\t Phone.no   Status     Balance");
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i == 2 || i == 5) {
                        System.out.print(String.format("%.2f", rs.getBigDecimal(i)) + "\t\t");
                    } else {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void paidinvoice(Connection con3) {
        try {
            String sql6 = "SELECT invoice_id, final_total, phone_number FROM invoice where paid_status = 'y'";
            Statement stmt = con3.createStatement();
            ResultSet rs = stmt.executeQuery(sql6);

            if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
                System.out.println("No invoices are paid.");
            } else {
                java.sql.ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                System.out.println("Inv.no\tTotal\t\tPhone.no");
                while (rs.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i == 2) {
                            System.out.print(String.format("%.2f", rs.getBigDecimal(i)) + "\t\t");
                        } else {
                            System.out.print(rs.getString(i) + "\t\t");
                        }
                    }
                    System.out.println();
                }
                System.out.println();
                System.out.println();
            }
            rs.close();
            stmt.close();
            con3.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void invoicedetails(Connection con, int n) {
        try {
            String sql6 = "SELECT phone_number, sub_total, discount, final_total, date_time, paid_status, balance FROM invoice WHERE invoice_id = ?";
            PreparedStatement preparedstatement7 = con.prepareStatement(sql6);
            preparedstatement7.setInt(1, n);
            ResultSet resultSet = preparedstatement7.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No such invoice present.");
                return;
            }

            String phone = resultSet.getString(1);
            BigDecimal st = resultSet.getBigDecimal(2);
            BigDecimal discount = resultSet.getBigDecimal(3);
            BigDecimal ft = resultSet.getBigDecimal(4);
            String dt = resultSet.getString(5);
            String ps = resultSet.getString(6);
            BigDecimal b = resultSet.getBigDecimal(7);

            System.out.println("\n\t\t\t\tABC Stores\n");
            System.out.println("ph no : " + phone + "\t\t\t" + "Inv no :" + n + "\n\t\t\t\t\t\t\t" + "Date : " + dt + "\n\n");

            String sql8 = "SELECT product_name, quantity, unnit, per_unit_value, price FROM liveupdate WHERE invoice_number = ?";
            PreparedStatement preparedstatement8 = con.prepareStatement(sql8);
            preparedstatement8.setInt(1, n);
            ResultSet rs = preparedstatement8.executeQuery();
            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            System.out.println("Product\t\tQty\t\t\tUnit\t  UP\t\tPrice");
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i == 4 || i == 5) {
                        System.out.print(String.format("%.2f", rs.getBigDecimal(i)) + "\t\t");
                    } else {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                }
                System.out.println();
            }

            System.out.println();
            System.out.println("\t\t\t\t\t\t  Sub Total : " + String.format("%.2f", st));
            System.out.println("\t\t\t\t\t\t Discount(%): " + discount);
            System.out.println("\t\t\t\t\t\t" + "_____________________");
            System.out.println("\t\t\t\t\t\t  Total     : " + String.format("%.2f", ft));
            System.out.println("\t\t\t\t\t\t" + "_____________________");
            System.out.println("Payment Status : " + ps);
            System.out.println("Balance : " + b);
            System.out.print("\n\n");

            // Close ResultSets and Statements
            rs.close();
            preparedstatement8.close();
            resultSet.close();
            preparedstatement7.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void customerdebit(Connection con, String pno) {
        try {
            // Check if the phone number exists in the customer table
            String checkCustomer = "SELECT COUNT(*) FROM customer WHERE phone_number=?";
            PreparedStatement psCheckCustomer = con.prepareStatement(checkCustomer);
            psCheckCustomer.setString(1, pno);
            ResultSet rsCheckCustomer = psCheckCustomer.executeQuery();
            rsCheckCustomer.next();
            int customerCount = rsCheckCustomer.getInt(1);

            if (customerCount == 0) {
                System.out.println("No such customer phone number.");
            } else {
                String sql = "SELECT c.customer_name, i.phone_number, SUM(i.balance), SUM(i.final_total) AS total_due " +
                        "FROM invoice i JOIN customer c ON i.phone_number = c.phone_number " +
                        "WHERE i.paid_status = 'n' AND i.phone_number = ? " +
                        "GROUP BY c.customer_name, i.phone_number";

                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, pno);
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next()) {
                    System.out.println("No debit for the customer.");
                } else {

                    String name = rs.getString(1);
                    String phone = rs.getString(2);
                    BigDecimal tot = rs.getBigDecimal(3);
                    BigDecimal totpurchase = rs.getBigDecimal(4);
                    System.out.println("Name\t\tPhone Number\tBalance\tPurchase Amount");
                    System.out.println(name + "\t\t" + phone + "\t\t" + String.format("%.2f", tot) + "\t\t" + String.format("%.2f", totpurchase));
                }
                rs.close();
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void productsalesdetails(Connection con) {
        try {
            String sql6 = "SELECT product_name, quantity_sold, unit, sold_amount FROM product";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql6);
            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            System.out.println("Name\t\tQuantity\tUnit\tCost");
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i == 2 || i == 4) {
                        System.out.print(String.format("%.2f", rs.getBigDecimal(i)) + "\t\t");
                    } else {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                }
                System.out.println();
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
