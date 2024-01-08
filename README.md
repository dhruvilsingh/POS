# POS-spring
Feature of the application - 

1. Brand and Category Management:
- Ability to upload and manage brand/category details using TSV files from the user interface.
- View, create, and edit brand details through the UI for efficient organization.

2. Product Management:
- Upload and manage product details using TSV files, ensuring strict validation against existing brand-category records.
- Seamless view, creation, and editing of product details via the user interface.

3. Inventory Management:
- Upload and modify product-wise inventory using TSV files, enabling dynamic adjustments during customer orders.
- Effortless inventory editing for individual products, ensuring accurate stock maintenance.

4. Order Creation and Management:
- Smooth creation of customer orders by entering barcode, quantity, and MRP.
- Instant reduction of inventory upon order creation, maintaining accurate stock levels.
- Ability to edit existing customer orders for flexibility in managing sales transactions.

5. Customer Invoice Generation:
- Integration of a separate module for generating PDF invoices, allowing for easy printing.
- Functionality to input required invoice fields, receiving Base64 encoded strings for PDF conversion and local storage.

6. Reporting Enhancements:
- Generation of detailed reports including inventory, brand, and sales reports for comprehensive business insights.
- Users can download reports in TSV format for convenient data analysis and processing.

7. Scheduler Integration for Daily Sales Updates:
- Utilized Scheduler in Spring to create a job for updating daily sales data.
- Automated process ensuring timely and accurate updates, enhancing system efficiency.

8. User Authentication and Role-Based Access:
- Basic signup page utilizing email for role assignment (operator/supervisor) based on predefined properties.
- Role-specific access control ensuring operators or supervisors access only relevant functionalities.

9. JUnit Testing:
- Conducted comprehensive JUnit testing, achieving a 92% line and 98% method coverage.
- Ensured robust code quality and functionality validation, guaranteeing system reliability across the application.



# UI 
1. Index Page

![Index Page](https://github.com/dhruvilsingh/POS/assets/72144292/ae5392d0-6ac1-40fd-a589-d5d43010026b)


2. Features Page

![Features Page](https://github.com/dhruvilsingh/POS/assets/72144292/3e5fe236-d1fd-45dd-9cd7-041aab8cf143)


3. Login Page

![Login Page](https://github.com/dhruvilsingh/POS/assets/72144292/b4ccc926-1c38-4eb5-a43d-f7d5e6214c99)


4. Home Page

![Home Page](https://github.com/dhruvilsingh/POS/assets/72144292/993537c1-b87e-44dd-8833-6b5ced23d5d8)


5. Brand Master Page

![Brand Page](https://github.com/dhruvilsingh/POS/assets/72144292/407a75d8-bd45-4992-9e87-e0d98ea93af8)


6. Product Master Page

![Products Page](https://github.com/dhruvilsingh/POS/assets/72144292/074f5a0b-19f8-47d6-8eea-7823634d6659)


7. Inventory Master Page

![Inventory page](https://github.com/dhruvilsingh/POS/assets/72144292/de5f4435-ed75-484c-9005-b9500337debf)


8. Orders Page

![Orders Page](https://github.com/dhruvilsingh/POS/assets/72144292/60c4e031-b634-4767-956e-2a0d03604712)


9. Daily Sales Report

![Daily Sales report](https://github.com/dhruvilsingh/POS/assets/72144292/8479a1b1-5e7c-4a82-a35c-726303c5b261)


10. Revenue Report

![Revenue Report](https://github.com/dhruvilsingh/POS/assets/72144292/a6fd892d-f192-4f65-8e73-2434150b6fd6)


11. Brand Report

![Brand Report](https://github.com/dhruvilsingh/POS/assets/72144292/77a07a05-29fe-45f6-b7f6-d3a0e552f446)


12. Inventory Report

![Inventory Report](https://github.com/dhruvilsingh/POS/assets/72144292/f53a6a40-6340-4a0e-92d0-f70f89a478d6)



# How to run the applicaiton
1. Install JDK 8 and Maven 3.9.6 (If you are using some other version then update the version in pom.xml file).
2. Create a database named 'pos' in MySQL and update the username and password for database in pos.properties file.
3. Run the application using 'mvn jetty:run' command.
4. Your webapp is ready to use on your local machine at the url 'localhost:9000/pos'. 















