# transaction-blocking
Project sample which emulates two levels of optimistic transaction blocking in REST and data layers. 
Built with JAX-RS, Hibernate, embedded scheduled requests with JAX-RS client.
REST layer - using etag header of HTTP requests.
Data layer - using Hibernate versioning.
