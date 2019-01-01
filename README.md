# Medivh

[![Build Status](https://travis-ci.org/moyada/medivh.svg?branch=master)](https://travis-ci.org/moyada/medivh)
![version](https://img.shields.io/badge/java-%3E%3D6-red.svg)
![java lifecycle](https://img.shields.io/badge/java%20lifecycle-compile-yellow.svg)
[![Maven Central](https://img.shields.io/badge/maven%20central-1.1.0-brightgreen.svg)](https://search.maven.org/search?q=g:%22io.github.moyada%22%20AND%20a:%22medivh%22)
[![license](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/moyada/medivh/blob/master/LICENSE)

[简体中文](README_CN.md) | English

A Java annotation processor, generate verify logic for method input by configuration rule.

## Features

* By modify the syntax tree at `compile time`, to adding verify logic for input parameter of method.

* Support null check for Object type.

* Support range check for byte, short, int, long, float and double.

* Support length check for String and array.

* Support capacity check for Collection and Map.

## Requirement

JDK 1.6 or higher.

## Quick start

### Adding dependencies to your project 

Using Maven

```
<dependencies>
    <dependency>
        <groupId>io.github.moyada</groupId>
        <artifactId>medivh</artifactId>
        <version>1.1.0</version>
        <scope>provided</scope>
    </dependency>
<dependencies/>
```

Using Gradle

```
dependencies {
  compileOnly 'io.github.moyada:medivh:1.1.0'
  // before 2.12 version
  // provided 'io.github.moyada:medivh:1.1.0'
}
```

Without build tool, you can download last jar from 
[![release](https://img.shields.io/badge/release-v1.1.0-blue.svg)](https://github.com/moyada/medivh/releases/latest) 
or
[![Maven Central](https://img.shields.io/maven-central/v/io.github.moyada/medivh.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.moyada%22%20AND%20a:%22medivh%22)
.

### Configure annotation in you program

Annotation usage

| Annotation Class | Action Scope | Effect |
| :---- | :----- | :---- |
| io.moyada.medivh.annotation.Nullable | field, no parameter method, method parameter | indicates whether or not this field allowed to be null, except primitive type, precede over NotNull. |
| io.moyada.medivh.annotation.NotNull | field, no parameter method | indicates whether or not this field not allowed to be null, default when use any Rule. |
| io.moyada.medivh.annotation.NumberRule | field, no parameter method | provide validate rule for number type field in class. |
| io.moyada.medivh.annotation.SizeRule | field, no parameter method | provide validate rule for size type field in class. |
| io.moyada.medivh.annotation.Throw | method parameter | configure check logic for method parameter, , except the primitive type. |
| io.moyada.medivh.annotation.Return | method parameter | configure check logic for method parameter, except the primitive type. |
| io.moyada.medivh.annotation.Variable | non-abstract method | configure the name of temporary variable, generated by the verify logic. |

Attribute description

| Attribute | Effect |
| :--- | :--- |
| NumberRule.min() | set the minimum allowed value of number type. |
| NumberRule.max() | set the maximum allowed value of number type. |
| SizeRule.min() | set the minimum allowed length or capacity of String, Array, Collection and Map. |
| SizeRule.max() | set the maximum allowed length or capacity of String, Array, Collection and Map. |
| Throw.value() | configure thrown exception, the exception type must have a String constructor, default is IllegalArgumentException. |
| Throw.message() | set the message head of thrown exception. |
| Return.value() | set the return value, support return type as primitive type or object, need to have a correspond constructor when return type is object. |
| Return.type() | configure class type of return value, the class type must be implement or sub from return type. |
| Variable.value() | set the name of temporary variable. |

* A use example is [here](#example).

### Compile project

Use compile commands of build tool, like `mvn compile` or `gradle build`.
 
Or use java compile command, like `javac -cp medivh.jar MyApp.java`.

#### System properties option

| Property | Effect |
| :--- | :--- |
| -Dmedivh.method | configure the name of validate method, default is `invalid0` . |
| -Dmedivh.var | configure the name of default temporary variable, default is `mvar_0` . |
| -Dmedivh.message | configure the default message head of exception, default is `Invalid input parameter` . |
| -Dmedivh.info.null | configure the default info of null check, default is `is null` . |
| -Dmedivh.info.equals | configure the default info of equals check, default is `cannot equals` . |
| -Dmedivh.info.less | configure the default info of less check, default is `less than` . |
| -Dmedivh.info.great | configure the default info of great check, default is `great than` . |

After compilation phase, the verification logic will be generated.

## Example

[More use cases](https://github.com/moyada/medivh/tree/master/src/test/java/cn/moyada/test)

```
import io.moyada.medivh.annotation.*;
import java.util.HashMap;
import java.util.List;

public class MyApp {

    public Info run(@Throw(RuntimeException.class) Args args,
                    @Throw(message = "something error") @Nullable Info info,
                    @Return({"test", "-0.503"}) String name,  // can be check null value for normal Object
                    @Return("null") int num // ineffective type
                    ) {
        // process
        ...
    }

    class Args {
    
        @NumberRule(max = "1000") int id;

        @NotNull HashMap<String, Object> param;

        @Nullable @SizeRule(min = 5) boolean[] value;
    }
    
    class Info {

        @SizeRule(min = 50) String name;

        @Nullable @NumberRule(min = "-25.02", max = "200") Double price;

        @SizeRule(min = 10, max = 10) List<String> extra;

        Info(String name, Double price) {
            this.name = name;
            this.price = price;
        }
    }
}
```

As the example code, the compiled content will be:

```
public Info run(Args args, Info info, String name, int num) {
    if (args == null) {
        throw new RuntimeException("Invalid input parameter, cause args is null"));
    } else {
        String mvar_0 = args.invalid0();
        if (mvar_0 != null) {
            throw new RuntimeException("Invalid input parameter, cause " + mvar_0);
        } else {
            if (info != null) {
                mvar_0 = info.invalid0();
                if (mvar_0 != null) {
                    throw new IllegalArgumentException("something error, cause " + mvar_0);
                }
            }

            if (name == null) {
                return new ProcessorTest.Info("test", -0.503D);
            } else {
                // process
                ...
            }
        }
    }
}

class Info {
    String name;
    Double price;
    List<String> extra;

    Info(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String invalid0() {
        if (this.name == null) {
            return "name is null";
        } else if (this.name.length() < 50) {
            return "name.length() less than 50";
        } else if (this.extra == null) {
            return "extra is null";
        } else if (this.extra.size() == 10) {
            return "extra.size() cannot equals 10";
        } else {
            if (this.price != null) {
                if (this.price > 200.0D) {
                    return "price great than 200.0";
                }

                if (this.price < -25.02D) {
                    return "price less than -25.02";
                }
            }

            return null;
        }
    }
}

class Args {
    int id;
    HashMap<String, Object> param;
    String[] values;

    Args() {
    }

    public String invalid0() {
        if (this.id > 1000) {
            return "id great than 1000";
        } else if (this.param == null) {
            return "param is null";
        } else {
            return this.value != null && this.value.length < 5 ? "value.length less than 5" : null;
        }
    }
}
``` 
