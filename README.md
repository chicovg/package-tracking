# package-tracking

A graphql service which returns package tracking data for a hypothetical package tracking system.  

This purpose of this project was to get more familiar with graphql-clj, so I chose a database schema that I designed and built the graphql service around it.  


## The database schema

Here's the database schema:

TODO...

## The GraphQL schema

Here is the graphQl schema:

TODO...

## Usage

First start up the database:
```
docker-compose up -d
```

This will start up a postgres instance with some test data.

next, run the service:
```
lein ring server
```



## License

Copyright © 2018 Victor Guthrie

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
