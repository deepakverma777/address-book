# Address Book Service 

## address-book 
   
## Description  
 1. How many males are in the address book?
 2. Who is the oldest person in the address book?
 3. How many days older is Bill than Paul?
  
  
## Capabilities
 1. Document Storage service responsible for create/get/download of a document for a customer. It also provides an API to filter documents based on criterias for a customers.
   * POST /compare- How many days older is name1 than name2
   * GET /male/count - Returns count of males 
   * GET /oldest - Returns name of the oldest person
## Interfaces & Endpoints  
* OpenAPI Specifications ([link](https://github.com/deepakverma777/address-book/blob/main/api-specs/swagger.yml]))

* Example
    
    ```GET 'http://localhost:9080/address-book-service/v1.0/oldest'```
    
* Other
    N/A    

## Developer Guide
### Prerequisites
  - Java version > 11
  - maven

```
$ brew update
$ brew tap homebrew/cask-versions
$ brew cask install java11
$ brew install gradle
```

### Running Application
  * To run the application, run the following command in a terminal window in directory:
  
     ```./gradlew bootRun```

### Running test
  * Run JUnit tests
  
    ```./gradlew test```

## Dependencies  
  N/A
  
## Troubleshooting & FAQs  

## Contact Information    
### Maintainers  

* User : Deepak Verma - deepakverma_777@yahoo.com
        
## Release Notes  
  
### address-book-0.1
