#TrivialReflection
The strings are directly available. No control- or data-flow analysis is required.
##TR1
[//]: # (MAIN: tr1.Foo)
Test reflection with respect to static methods.
```java
// tr1/Foo.java
package tr1;

import lib.annotations.callgraph.IndirectCall;
class Foo {
    static String staticToString() { return "Foo"; }

    @IndirectCall(
        name = "staticToString", returnType = String.class, line = 12,
        resolvedTargets = "Ltr1/Foo;"
    )
    public static void main(String[] args) throws Exception {
        Class.forName("tr1.Foo").getDeclaredMethod("staticToString").invoke(null);
    }
}
```
[//]: # (END)

##TR2
[//]: # (MAIN: tr2.Foo)
Test reflection with respect to instance methods.
```java
// tr2/Foo.java
package tr2;

import lib.annotations.callgraph.IndirectCall;
class Foo {
    public String toString() { return "Foo"; }

    @IndirectCall(
        name = "toString", returnType = String.class, line = 12,
        resolvedTargets = "Ltr2/Foo;"
    )
    void m() throws Exception {
        Class.forName("tr2.Foo").getDeclaredMethod("toString").invoke(this);
    }

    public static void main(String[] args) throws Exception { new Foo().m(); }
}
```
[//]: # (END)

##TR3
[//]: # (MAIN: tr3.Foo)
Test reflection with respect to constructors.
```java
// tr3/Foo.java
package tr3;

import lib.annotations.callgraph.IndirectCall;
class Foo {
    public Foo(String s) {    }

    @IndirectCall(
        name = "<init>", parameterTypes = String.class, line = 12,
        resolvedTargets = "Ltr3/Foo;"
    )
    public static void main(String[] args) throws Exception {
        Class.forName("tr3.Foo").getConstructor(String.class).newInstance("ASD");
    }
}
```
[//]: # (END)

##TR4
[//]: # (MAIN: tr4.Foo)
Test reflection with respect to the default constructor
```java
// tr4/Foo.java
package tr4;

import lib.annotations.callgraph.IndirectCall;
class Foo {
    public Foo() {    }

    @IndirectCall(name = "<init>", line = 9, resolvedTargets = "Ltr4/Foo;")
    public static void main(String[] args) throws Exception {
        Class.forName("tr4.Foo").newInstance();
    }
}
```
[//]: # (END)

TODO parameter types for invoke/constructor
TODO autoboxing/unboxing
TODO forName(String name, boolean initialize, ClassLoader loader)
TODO fieldAccess


#LocallyResolvableReflection
The complete information is locally (intra-procedurally) available.
##LRR1
[//]: # (MAIN: lrr1.Foo)
Test reflection with respect to static methods where the target class is dynamically decided
```java
// lrr1/Foo.java
package lrr1;

import lib.annotations.callgraph.IndirectCall;
class Bar {
    static String staticToString() { return "bar"; }
}
class Foo {
    static String staticToString() { return "foo"; }

    public static void main(String[] args) throws Exception {
        m(args.length % 2 == 0);
    }

    @IndirectCall(
        name = "staticToString", returnType = String.class, line = 19,
        resolvedTargets = "Llrr1/Foo;"
    )
    static void m(boolean b) throws Exception {
        Class.forName(b ? "lrr1.Foo" : "lrr1.Bar").getDeclaredMethod("staticToString").invoke(null);
    }
}
```
[//]: # (END)

##LRR2
[//]: # (MAIN: lrr2.Foo1)
```java
// lrr2/Foo1.java
package lrr2;

import lib.annotations.callgraph.IndirectCall;
class Foo1 {
    static String staticToString() { return "1"; }

    @IndirectCall(
        name = "staticToString", returnType = String.class, line = 17,
        resolvedTargets = { "Llrr2/Foo1;", "Llrr2/Foo2;" }
    )
    static void m(boolean b) throws Exception {
        String className;
        if (b)
            className = "lrr2.Foo" + 1;
        else
            className = "lrr2.Foo" + 2;
        Class.forName(className).getDeclaredMethod("staticToString").invoke(null);
    }

    public static void main(String[] args) throws Exception {
        m(args.length % 2 == 0);
    }
}
class Foo2 {
    static String staticToString() { return "2"; }
}
```
[//]: # (END)

##LRR3
[//]: # (MAIN: lrr3.Foo)
```java
// lrr3/Foo.java
package lrr3;

import lib.annotations.callgraph.IndirectCall;
class Foo {
    static String staticToString1() { return "1"; }
    static String staticToString2() { return "2"; }

    @IndirectCall(
        name = "staticToString1", returnType = String.class, line = 17,
        resolvedTargets = "Llrr3/Foo;"
    )
    @IndirectCall(
        name = "staticToString2", returnType = String.class, line = 17,
        resolvedTargets = "Llrr3/Foo;"
    )
    static void m(boolean b) throws Exception {
        Class.forName("lrr3.Foo").getDeclaredMethod(b ? "staticToString1" : "staticToString2").invoke(null);
    }

    public static void main(String[] args) throws Exception {
        m(args.length % 2 == 0);
    }
}
```
[//]: # (END)

#ContextSensitiveReflection
The concrete strings require information about the context.

##CSR1
[//]: # (MAIN: csr1.Foo)
The method name is passed as an argument.
```java
// csr1/Foo.java
package csr1;

import lib.annotations.callgraph.IndirectCall;
class Foo {
    static String staticToString1() { return "1"; }
    static String staticToString2() { return "2"; }

    @IndirectCall(
        name = "staticToString1", returnType = String.class, line = 13,
        resolvedTargets = "Lcsr1/Foo;"
    )
    static void m(String methodName) throws Exception {
        Class.forName("csr1.Foo").getDeclaredMethod(methodName).invoke(null);
    }

    public static void main(String[] args) throws Exception {
        m("staticToString1");
    }
}
```
[//]: # (END)

##CSR2
[//]: # (MAIN: csr2.Foo)
The class name is passed as an argument.
```java
// csr2/Foo.java
package csr2;

import lib.annotations.callgraph.IndirectCall;
class Foo {
    static String staticToString() { return "Foo"; }

    @IndirectCall(
        name = "staticToString", returnType = String.class, line = 12,
        resolvedTargets = "Lcsr2/Foo;"
    )
    static void m(String className) throws Exception {
        Class.forName(className).getDeclaredMethod("staticToString").invoke(null);
    }

    public static void main(String[] args) throws Exception {
        m("csr2.Foo");
    }
}

class Bar {
    static String staticToString() { return "Bar"; }
}
```
[//]: # (END)

##CSR3
[//]: # (MAIN: csr3.Foo)
The method name is unknown.
```java
// csr3/Foo.java
package csr3;

import lib.annotations.callgraph.IndirectCall;
public class Foo {
    static String staticToString1() { return "1"; }
    static String staticToString2() { return "2"; }

    @IndirectCall(
        name = "staticToString1", returnType = String.class, line = 17,
        resolvedTargets = "Lcsr3/Foo;"
        )
    @IndirectCall(
        name = "staticToString2", returnType = String.class, line = 17,
        resolvedTargets = "Lcsr3/Foo;"
    )
    static void m(String methodName) throws Exception {
        Class.forName("csr1.Foo").getDeclaredMethod(methodName).invoke(null);
    }

    public static void main(String[] args) throws Exception {
        m(args[0]);
    }
}
```
[//]: # (END)

##CSR4
[//]: # (MAIN: csr4.Foo)
The class name is passed as an argument.
```java
// csr4/Foo.java
package csr4;

import lib.annotations.callgraph.IndirectCall;
public class Foo {
    static String staticToString() { return "Foo"; }

    @IndirectCall(
        name = "staticToString", returnType = String.class, line = 12,
        resolvedTargets = { "Lcsr4/Foo;", "Lcsr4/Bar;" }
    )
    static void m(String className) throws Exception {
        Class.forName(className).getDeclaredMethod("staticToString").invoke(null);
    }

    public static void main(String[] args) throws Exception {
        m(args[0]);
    }
}

class Bar {
    static String staticToString() { return "Bar"; }
}
```
[//]: # (END)

##CSR5
[//]: # (MAIN: csr5.Foo)
Here newInstance could call every default constructor.
```java
// csr5/Foo.java
package csr5;

import lib.annotations.callgraph.IndirectCall;
class Foo {
    @IndirectCall(
        name = "<init>", line = 10, 
        resolvedTargets = { "Lcsr5/Foo;", "Lcsr5/Bar;"}
    )
    public static void main(String[] args) throws Exception {
        Object o = Class.forName(args[0]).newInstance();
        Bar.m((Foo) o);
        o.toString();
    }
}
class Bar {
  static void m(Foo f) { }
}

```
[//]: # (END)

##CSR6
[//]: # (MAIN: csr6.Foo)
TODO
```java
// csr6/Foo.java
package csr6;

import lib.annotations.callgraph.CallSite;
class Foo {
    public String toString() { return ""; }

    @CallSite(
        name = "toString", returnType = String.class, line = 14,
        resolvedTargets = "Lcsr6/Foo;"
    )
    public static void main(String[] args) throws Exception {
        Object o = Class.forName(args[0]).newInstance();
        if (o instanceof Foo)
            o.toString();
    }
}
```
[//]: # (END)