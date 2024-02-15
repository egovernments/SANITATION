# Vendor

Vehicle Registry is a system that enables ULB Employees to create and search Vendor i.e Desluding Operator (DSO) and driver entities with appropriate vehicle Entities  for FSM Application. This document contains the details about how to setup the Vendor and describe the functionalities provided.

### DB UML Diagram

![plot](./vendor.png)

### Service Dependencies


- egov-mdms-service
- egov-user-service
- boundary-service
- vehicle



### Swagger API Contract

Link to the swagger API contract [yaml](https://raw.githubusercontent.com/egovernments/municipal-services/master/docs/fsm/Vendor_Registration_Contract.yaml) and editor link like below


### Postman Collection
Link to the postman collection [here](https://api.postman.com/collections/13428435-67194a8e-f288-473e-b7cc-8f26fd964ace?access_key=PMAT-01HK7FAQ5CJ5J1QPAH2ZJRC7H8)


## Service Details

**Vendor Registry**

- Contains the API's to create,  search Vendor i.e DSO in FSM Case




### API Details

`v1/_create` 		: The create api to create Vendor in the system

`v1/_search`		: The search api to fetch the Vendors in the system based on the search criteria



### Reference Document
TBD


### Kafka Consumers
NA

### Kafka Producers


- **save-vendor-application** 			: service sends data to this topic to create new Vendor.