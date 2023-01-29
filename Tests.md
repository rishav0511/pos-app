BrandCategoryDtoTest:

1. Add Brand-Category
2. Add Duplicate Brand-Category
3. Invalid brand with null fields
4. GetAll Brand-Category
5. Update Brand-Category 
6. Search valid Id
7. Search Invalid Id
8. Brand And Category made up of blanks

ProductDtoTest:

1. Add Product
2. Add Product without Brand and Category
3. Get All Products
4. Get by barcode 
5. Get Product by id
6. Get Product by invalid id
7. Get by Invalid Barcode
8. Update Product
9. Add product with invalid barcode
10. Add product with invalid name and mrp
11. Add product with Duplicate Barcode

InventoryDtoTest:
1. Add Product to inventory and setting it quantity to zero(0) by default
2. GetAll Inventory
3. Update Inventory
4. Updating invalid barcode

OrderDtoTest:
1. Adding Order
2. Updating Order
3. Ordering for quantity more than inventory 
4. Creating Order for Non-existing barcode
5. Updating Order for Non-existing barcode
6. Get All Orders
7. Get OrderItem from Order
8. Get Path for generated Invoice
9. Get OrderData by orderId
10. Add product with Selling price more than mrp

ReportDtoTest: 
1. Get inventory report 
2. Get brand report
3. Get sales report empty brand-category
4. Get sales report empty category
5. Get sales report empty brand
6. Get sales report
7. Generate dailySalesReport and retrieve 