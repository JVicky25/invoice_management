<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Invoice Management System - README</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        h1, h2 {
            color: #333;
            border-bottom: 1px solid #ccc;
            padding-bottom: 5px;
        }
        ul {
            list-style-type: none;
            padding: 0;
        }
        li {
            margin-bottom: 10px;
        }
        pre {
            background-color: #f0f0f0;
            padding: 10px;
            border-radius: 5px;
        }
        code {
            font-family: Consolas, monospace;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <h1>Invoice Management System</h1>

    <h2>Description</h2>
    <p>This Java application provides a command-line interface for managing invoices, customer details, and product sales using a PostgreSQL database. It supports functionalities such as creating new invoices, listing invoices, tracking paid invoices, viewing invoice details, checking customer debit and purchase history, and monitoring product sales performance.</p>

    <h2>Features</h2>
    <ul>
        <li><strong>New Invoice Creation:</strong> Allows users to create new invoices, add products, specify quantities, calculate totals with discounts, and update inventory.</li>
        <li><strong>Invoice Listing:</strong> Displays a list of all invoices stored in the database, including invoice number, total amount, customer phone number, payment status, and balance.</li>
        <li><strong>Paid Invoice Listing:</strong> Shows details of invoices that have been fully paid, including invoice number, total amount, and customer phone number.</li>
        <li><strong>Invoice Details:</strong> Provides detailed information about a specific invoice, including customer details, product details (name, quantity, unit, unit price), subtotals, discounts, total amount, payment status, and balance.</li>
        <li><strong>Customer Debit and Purchase History:</strong> Allows users to check a customer's name, phone number, total balance due, and total purchase amount based on unpaid invoices.</li>
        <li><strong>Product Sales Performance:</strong> Displays details of each product's sales, including product name, quantity sold, unit of measurement, and total sales amount.</li>
    </ul>

    <h2>Database Tables</h2>

    <h3>Product Table</h3>
    <ul>
        <li><strong>product_name:</strong> Name of the product.</li>
        <li><strong>instock_quantity:</strong> Quantity of the product available in stock.</li>
        <li><strong>per_unit_price:</strong> Price of one unit of the product.</li>
        <li><strong>unit:</strong> Unit of measurement for the product (e.g., pieces, kg).</li>
        <li><strong>quantity_sold:</strong> Total quantity of the product sold.</li>
        <li><strong>sold_amount:</strong> Total revenue generated from sales of the product.</li>
    </ul>

    <h3>Customer Table</h3>
    <ul>
        <li><strong>phone_number:</strong> Unique identifier for the customer's phone number.</li>
        <li><strong>customer_name:</strong> Name of the customer.</li>
    </ul>

    <h3>Invoice Table</h3>
    <ul>
        <li><strong>invoice_id:</strong> Unique identifier for each invoice.</li>
        <li><strong>paid_status:</strong> Status indicating whether the invoice has been paid ("y" for paid, "n" for unpaid).</li>
        <li><strong>phone_number:</strong> Phone number of the customer associated with the invoice.</li>
        <li><strong>sub_total:</strong> Subtotal amount before applying any discounts.</li>
        <li><strong>discount:</strong> Discount percentage applied to the subtotal.</li>
        <li><strong>final_total:</strong> Total amount after applying discounts.</li>
        <li><strong>date_time:</strong> Date and time when the invoice was created.</li>
        <li><strong>balance:</strong> Remaining balance to be paid.</li>
    </ul>

    <h3>Liveupdate Table</h3>
    <ul>
        <li><strong>invoice_number:</strong> Invoice number associated with the product update.</li>
        <li><strong>product_name:</strong> Name of the product included in the invoice.</li>
        <li><strong>per_unit_value:</strong> Unit price of the product at the time of invoicing.</li>
        <li><strong>quantity:</strong> Quantity of the product sold in the invoice.</li>
        <li><strong>price:</strong> Total price for the quantity of the product sold.</li>
        <li><strong>unit:</strong> Unit of measurement for the product (e.g., pieces, kg).</li>
    </ul>
</body>
</html>

