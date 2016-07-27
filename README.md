# REST file parser

Simple REST service that consumes files with text data and creates word frequency dictionary from the contentof given files.

For example we have file with next content:

```sh
AA
BB
...
```

If we *POST* this file or few files on deployed service by ```<some_host_ip>:8080/dataProcessor``` and then perform *GET* request on the same URL we will get array with parsing results wich will be consisting of JSON objects (string and number of it's occurences in all files) sorted in descending order by the number of particular string occurrences in all files that were passed on processing.

```sh
[
  {"value":"AA", "count":10},
  {"value":"BB", "count":8},
  ...
]
```

## Requirements

* Apache Maven installed in order to build the project
* Oracle Java 8 JDK or JRE installed in order to run the project.

## How to run

Type in a console next command:

```sh
  $ git clone <repository>
  $ cd <folder with cloned project>
  $ mvn clean install
  $ mvn exec:java
```

or

```sh
  $ git clone <repository>
  $ cd <folder with cloned project>
  $ mvn clean deploy
```

Than you can acces service at ```localhost:8080/dataProcessor```.

## Examples

Post some file(s) on arsing:
Http method - ```POST``` or ```PUT```, URL - ```localhost:8080/dataProcessor```

```sh
curl -v -H "Content-Type: multipart/form-data" -F "files=@MOCK_DATA.csv" http://localhost:8080/dataProcessor

*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /dataProcessor HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.43.0
> Accept: */*
> Content-Length: 506
> Expect: 100-continue
> Content-Type: multipart/form-data; boundary=------------------------97dcd27ce52f8a0c
>
< HTTP/1.1 100 Continue
< HTTP/1.1 200 OK
< Server: Apache-Coyote/1.1
< Content-Length: 0
< Date: Wed, 27 Jul 2016 16:35:16 GMT
<
* Connection #0 to host localhost left intact

```

Get results of file parsing:
Http method - ```GET```, URL - ```localhost:8080/dataProcessor```
```sh
  $ curl -v http://localhost:8080/dataProcessor

  [
    {"value":"AA", "count":10},
    {"value":"BB", "count":8},
    ...
  ]
```