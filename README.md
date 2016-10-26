# key-counter
Service that stores number of key usages.

It is secured with basic authentication.

## Required services
Redis

## How to build

It's a Spring Boot application build by maven. All that's needed is a single command to compile, run tests and build a jar:

```
mvn verify
```

## How to run locally
### Redis
To run Redis, with persistent storage, in a container you can use following command:
```
docker run --name key-counter-redis -d -p 6379:6379 -v [path on host machine]:/data redis redis-server --appendonly yes
```
Redis will store it's data in `[path on host machine]` directory.

### Environment variables
To run the service locally, the following environment variables need to be defined:
* `REDIS_HOST`, `REDIS_PORT` - default values: `localhost` and `6379`
* `KEY_COUNTER_USER`, `KEY_COUNTER_PASS` - credentials used for basic authentication.

### KeyCounter
```
mvn spring-boot:run
```
After starting a local instance, it's available at http://localhost:9914.
You can retrieve a subsequent key numbers with an exemplary command:
```
curl -v -X POST -u $KEY_COUNTER_USER:$KEY_COUNTER_PASS localhost:9914/api/v1/counter/key-name
```
