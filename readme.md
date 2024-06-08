# Scripting Utils for Java

[![Maintainability](https://api.codeclimate.com/v1/badges/995494f09a9a48aa4857/maintainability)](https://codeclimate.com/github/AnatoliyKozlov/scripting-utils/maintainability)
[![codecov](https://codecov.io/github/AnatoliyKozlov/scripting-utils/graph/badge.svg?token=4HO8QZMZUE)](https://codecov.io/github/AnatoliyKozlov/scripting-utils)

### Description

This project provides a set of utilities for scripting in Java. It allows you to write Java scripts productively and efficiently.

Modules (static objects) with tons of default behavior are available for use. You can use them in your scripts without writing boilerplate code.

### Article on Dev.to

It is a good idea to read the article on Dev.to to get a better understanding of the project:
https://dev.to/toliyansky/scripting-with-java-3i9k

### Installation
```shell
curl -s https://raw.githubusercontent.com/AnatoliyKozlov/scripting-utils/master/install.sh | bash
```

### Requirements
- Java 22 or higher

### How to use
1) Install scripting-utils. Execute one-line command from the Installation section above
2) Create a shebang file (**without** the .java extension) 
3) Write your Java code in the file (as in example below)
```java
#!/usr/bin/env java --source 22 --enable-preview --class-path /Users/toliyansky/scripting-utils

import static scripting.Utils.*;

void main() {
    println("Hello, World!");
    var response = http.get("https://httpbin.org/get");
    log.info(response.body());
}
```
4) Apply command `chmod +x your-file-name` to make it executable
5) Run the file from the command line. For example: `./your-file-name`

### Features

- Functions: `print(obj)`, `println(obj)`, `readln(prompt)` from [JEP 477](https://openjdk.org/jeps/477), available now. No need wait [JEP 477](https://openjdk.org/jeps/477) that will be available in Java 23.

  #### Modules (static objects):
- `http` for HTTP requests
- `terminal` for terminal commands
- `file` for file operations
- `log` for logging
- `thread` for threading
