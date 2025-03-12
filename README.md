# Welcome to Shopping app 🛍️
A shopping app built using Kotlin for Android. The app fetches product data from an API and offers functionalities such as browsing products, viewing product details, adding items to favorites, and managing the shopping cart. 

## Features
* **Home Screen**: Displays a list of products.
  * Filter products by category.
  * Search through list of products.
  * Browse products while not connected to the internet
    * Local database (dao)  
* **Product Details**: Detailed view of the product selected including description, image and option to add to cart.
  * Add product as favorite
* **Shopping Cart**: Collection of all items added to the cart.
  * Option to remove items from cart.
  * Total price of all products.
  * Proceed to checkout (adds list of products to Order History).
  * Adjust quantity of each product.
* **Order History**: List of previous orders made.
  * Clear history.


## Teach Stack
* **Language**: Kotlin
* **Architecture**: MVVM (Model-View-ViewModel)
* **API**: Rest API for fetching product data: https://dummyjson.com/ 
* **Libraries**:
  * Jetpack Compose: For building UIs
  * Room: For local database management
  * Retrofit: For making HTTP requests
  * Coil: For loading images asynchronously
  * Navigation Compose: For managing in-app navigation

### Prerequisites
1. Android Studio
