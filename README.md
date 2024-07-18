# Invoice Management System

## Description
This Java application provides a command-line interface for managing invoices, customer details, and product sales using a PostgreSQL database. It supports functionalities such as creating new invoices, listing invoices, tracking paid invoices, viewing invoice details, checking customer debit and purchase history, and monitoring product sales performance.

## Features
- **New Invoice Creation:** Allows users to create new invoices, add products, specify quantities, calculate totals with discounts, and update inventory.
- **Invoice Listing:** Displays a list of all invoices stored in the database, including invoice number, total amount, customer phone number, payment status, and balance.
- **Paid Invoice Listing:** Shows details of invoices that have been fully paid, including invoice number, total amount, and customer phone number.
- **Invoice Details:** Provides detailed information about a specific invoice, including customer details, product details (name, quantity, unit, unit price), subtotals, discounts, total amount, payment status, and balance.
- **Customer Debit and Purchase History:** Allows users to check a customer's name, phone number, total balance due, and total purchase amount based on unpaid invoices.
- **Product Sales Performance:** Displays details of each product's sales, including product name, quantity sold, unit of measurement, and total sales amount.

## Database Tables

### Product Table
- **product_name:** Name of the product.
- **instock_quantity:** Quantity of the product available in stock.
- **per_unit_price:** Price of one unit of the product.
- **unit:** Unit of measurement for the product (e.g., pieces, kg).
- **quantity_sold:** Total quantity of the product sold.
- **sold_amount:** Total revenue generated from sales of the product.

### Customer Table
- **phone_number:** Unique identifier for the customer's phone number.
- **customer_name:** Name of the customer.

### Invoice Table
- **invoice_id:** Unique identifier for each invoice.
- **paid_status:** Status indicating whether the invoice has been paid ("y" for paid, "n" for unpaid).
- **phone_number:** Phone number of the customer associated with the invoice.
- **sub_total:** Subtotal amount before applying any discounts.
- **discount:** Discount percentage applied to the subtotal.
- **final_total:** Total amount after applying discounts.
- **date_time:** Date and time when the invoice was created.
- **balance:** Remaining balance to be paid.

### Liveupdate Table
- **invoice_number:** Invoice number associated with the product update.
- **product_name:** Name of the product included in the invoice.
- **per_unit_value:** Unit price of the product at the time of invoicing.
- **quantity:** Quantity of the product sold in the invoice.
- **price:** Total price for the quantity of the product sold.
- **unit:** Unit of measurement for the product (e.g., pieces, kg).

<!-- Include setup, usage, contributors, license sections as needed -->
