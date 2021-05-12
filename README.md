#  笔记

# 数据结构

数据结构可以分为 队列、树、堆、数组、栈、链表、图、散列表。

## 链表

概念：链表由一系列结点（链表中每一个元素称为结点）组成，每个结点包括两个部分：一个是存储数据元素的数据域，另一个是存储下一个（上一个）结点地址的指针域。

![这里写图片描述](https://img-blog.csdn.net/20170802162411952?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDYxODE5NA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

> 上图是双链表，代表着节点中有2个指针，一个指向前一个元素，另外一个指向后一个元素。链表其实可以分类为单链表和双链表，双链表比单链表多了一个指向前一个元素的指针而已，相信很好理解。

#### 优点：

1.  插入和删除只需修改指针，不需要移动其他元素，效率高，为O(1);
2. 不要求连续空间，空间利用率高;

#### 缺点：

1. 查找元素的效率低，比如要查找位置为99的元素，需要从第1个开始查找到99，效率非常低

**以上就是关于链表的概念，相信大家可以很好的理解。知道了链表的优缺点，在我们日常开发中需要对数据进行频繁插入和删除的就可以使用链表。**

### 链表在Java中的实现

链表在java中的实现类为LinkedList,这个类

首先java使用一个静态内部类来代表链表的节点：

```java
private static class Node<E> {
    E item;// 数据
    Node<E> next;// 前一个节点
    Node<E> prev;// 后一个节点
    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
```

非常简单粗暴，很好理解。像我们日常使用最常用的方法肯定是:add，remove，get，set。

```java
public boolean add(E e) {
    linkLast(e);
    return true;
}
//首先是add方法，里面调用了linkLast方法，方法名的意思是连接最后一个
void linkLast(E e) {
    final Node<E> l = last;
    final Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    if (l == null)
        first = newNode;
    else
        l.next = newNode;
    size++;
    modCount++;
}
//last记录的是链表中最后一个元素，当列表为空时last为null，这段代码简单，首先new一个节点，头指针指向当前链表节点中的最后一个，尾指针是指向null。然后，如果last为空就说明链表还是空的，那么这个新增的节点就是头节点。
```

再来看remove方法，remove方法有3个重载，remove()删除的是第一个节点，remove(int index)删除的是指定位置节点，remove(Object o)删除的是节点数据为o的节点，其实只需要知道remove(Object o)就行，其余2个重载的方法都使用的是相同的方法。

```java
public E remove(int index) {
    checkElementIndex(index);// 检查index是否为0或是否越界
    return unlink(node(index));
}

private void checkElementIndex(int index) {
    if (!isElementIndex(index))
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}
//上述代码中最关键的就是unlink(node(index))，node方法会根据index查找到对应节点，unlink方法则是删除指定节点
Node<E> node(int index) {
    // assert isElementIndex(index);
    if (index < (size >> 1)) {
        Node<E> x = first;
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}
//这里的size >> 1其实就是size/2，这段代码很巧妙的已链表大小的一半来查询，是为了增加查询的效率。如果index小于size/2，则从链表的第一个节点开始遍历到index然后返回节点；如果index大于size/2，则从链表最后一个节点开始倒叙遍历到index然后返回节点。其实这里就很明显的体现出了链表的缺点了，因为必须要从链表的第一个节点开始查询，且要遍历index次才能找到对应节点。

//node方法找到了对应index的节点，然后unlink则负责断开该节点。
E unlink(Node<E> x) {
    // assert x != null;
    final E element = x.item;
    final Node<E> next = x.next;
    final Node<E> prev = x.prev;
    if (prev == null) {
        first = next;
    } else {
        prev.next = next;
        x.prev = null;
    }
    if (next == null) {
        last = prev;
    } else {
        next.prev = prev;
        x.next = null;
    }
    x.item = null;
    size--;
    modCount++;
    return element;
}
//首先判断该节点是不是头节点，如果是，记录下一个节点作为头节点；如果不是，则让前一个节点的尾指针指向后一个节点。然后再判断是不是尾节点，如果是，记录前一个节点为尾节点；如果不是，让后一个节点的头指针指向前一个节点。这段代码很好理解，完全可以不用看解析。
//然后在来看remove(Object o)
public boolean remove(Object o) {
    if (o == null) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (x.item == null) {
                unlink(x);
                return true;
            }
        }
    } else {
        for (Node<E> x = first; x != null; x = x.next) {
            if (o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
    }
    return false;
}
//很简单粗暴，遍历链表，查看是否有节点数据为o，有，则调用unlink方法删除。这里要注意的一点是，链表的节点数据是允许为null的。
```

再来看一下get方法

```java
public E get(int index) {
    checkElementIndex(index);
    return node(index).item;
}
//还是熟悉的味道和配方，关键的node方法，找到对应节点并返回数据。
public E set(int index, E element) {
    checkElementIndex(index);
    Node<E> x = node(index);
    E oldVal = x.item;
    x.item = element;
    return oldVal;
}
//set方法同样使用的是node方法，不再详述了
```

### 哈希碰撞问题

提到哈希表，必然提一下哈希碰撞问题，哈希碰撞两个不同的原始值在经过哈希运算后得到同样的结果，这样就是哈希碰撞。

### Map

#### HashMap

大家写Map可能绝大部分情况下都是使用HashMap。HashMap底层是通过数组加链表（Java1.8新加了红黑树）来实现。为什么要搞这么复杂呢？又是数组又是链表又是树的？其实还是为了提高效率。上面的把键值对封装成Data，加到List中。get效率跟HashMap比就会非常惨了。因为get只能一个个去遍历，但是HashMap就可以通过key的hashcode非常高效的get数据。



#### LinkedHashMap

看名字就知道多了个Linked，意思就是排序。LinkedHashMap本身就是继承自HashMap，所以增删改查方面和HashMap基本是一致的。区别主要就是通过entrySet遍历了。LinkedHashMap会保存元素的添加顺序然后按顺序遍历，但HashMap就是无序了。



#### TreeMap

TreeMap内部是个红黑树。从使用角度来看，它跟LinkedHashMap主要区别就LinkedHashMap保存的是元素的插入顺序，而TreeMap则是对key排序。遍历可以得到一个按key排序的结果。



#### HashTable

内部是个链表，另外就是线程同步。



#### ArrayMap

ArrayMap内部也是使用数组。查找数据会通过二分法来提高效率，Google推荐用ArrayMap来代替HashMap。



#### SparseArray

SparseArray内部依然是使用数组来实现。但是限制了key只能int，而且没有装箱，HashMap的key只能是Integer。所以Sparse性能会更好，它内部做了数据压缩，来稀疏数组的数据，节省内存。







### 总结

主要讲的是对于数据结构的基本认识和链表的了解，可能对于我们实际开发并不会有很大的实质性帮助，但是这只是数据结构和算法知识的一小部分，我相信当我们对数据结构和算法有一个比较全面的了解和理解的时候，对于我们的实质编码肯定是会有帮助的，加油吧！

## 数组

### 数组的使用

```java
String[] arr1 = new String[3];// 创建一个大小为3的数组
arr1[0] = "0";
arr1[1] = "1";
arr1[2] = "2";
String[] arr2 = new String[]{"0", "1", "2"};// 创建一个大小为3的数据并赋值
```

上述代码，实际开发中，像这样使用数组的地方并不多，我们更多的使用的是像ArrayList这样的数组，其实用容器来形容ArrayList更贴切，在他的内部维护着一个数组，我们可以非常方便的增删查改一个ArrayList就是因为容器中已经提供了大量的方法供我们使用且不需要我们自己维护，可以说是非常棒了。

### 数组的特点

数组分配在一块连续的数组空间上，所以在给他分配空间时要确定它的大小，像我们上面两种声明数组的方式都是确定了大小。但是链表却不同，它时一块动态空间，可以随意的改变长短，所以初始化时不需要确定大小。

#### 优点：

1. 访问、查找元素的效率很高。

#### 缺点;

1. 由于空间是固定的，所以插入和删除元素效率会很低

### 数组容器 ArrayList

ArrayList我们使用的非常多，里面有很多方法都非常的好用，我们就通过这些方法来理解数组以及数组容器，形成数据结构的思想。

#### 构造方法

```java
transient Object[] elementData; 

public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}
```

这里有一个elementData，它就是一个数组，transient修饰符是用来描述序列化相关的，这里我们不关心，这个数组在我们初始化ArrayList就被创建了，容器的用处就是来为维护这个数组方便我们使用。

再来看一下另一个构造方法，此构造方法的参数就是数组的初始化大小：

```java
public ArrayList(int initialCapacity) {
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {
        this.elementData = EMPTY_ELEMENTDATA;
    } else {
        throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
    }
}
```

还有另一个，此构造方法是把另外一个数组的内容复制到这个新的数组中，这里有一个Array.copyOf（）方法，Arrays是一个专门用来操作数组的类，里面提供了大量调用底层C的方法。

```java
public ArrayList(Collection<? extends E> c) {
    elementData = c.toArray();
    if ((size = elementData.length) != 0) {
        // c.toArray might (incorrectly) not return Object[] (see 6260652)
        if (elementData.getClass() != Object[].class)
            elementData = Arrays.copyOf(elementData, size, Object[].class);
    } else {
        // replace with empty array.
        this.elementData = EMPTY_ELEMENTDATA;
    }
}
```

#### add系列方法

```java
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}
```

总共2行代码，第2行就是一个简单的数组赋值操作，把添加新来的元素加入到elementData中并使数组的size加1，还记得我前面讲过的数组的大小是再初始化的时候固定的吗，如果一个大小为1的数组，那么怎么才能往它里面加第二个元素呢，所以我们就可以想到第一行的ensureCapacityInternal方法肯定使用了某种方法增大了原数组的大小。

```java
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    ensureExplicitCapacity(minCapacity);
}
```

这段代码先会判断elementData的内容是否为空，如果是就会给数组设置一个最小容量，这个最小容量是默认大小(10)和当前数组当前大小+1的最大值。确定这个大小之后，剩下的就是给数组扩容了，我们继续看下去：

```java
private void ensureExplicitCapacity(int minCapacity) {
    modCount++;
    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}
```

如果数组的新容量是大于数组当前大小就增容，grow（生长）方法名字也取得相当到位

```java
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

这里还会修正一次容量大小，当数组元素较少，大概为个位数的时候，容量会增到到10，当元素较多，像20个以上时容量会以1.5倍增大，最后一行又用到了Arrays中的方法，同样的是一个复制数组元素的方法。

```java
public void add(int index, E element) {
    rangeCheckForAdd(index);
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    System.arraycopy(elementData, index, elementData, index + 1,
                     size - index);
    elementData[index] = element;
    size++;
}
```

这个add方法是向指定位置插入元素，首先同样的会给数组扩容，因为想一个入组中的某一个位置插入元素，假设这个位置是数组的中间，那么就意味着从数组中间+1位置的元素到最后一个元素都要后移，但是我们知道数组是不具备这个功能的，所以还是用到了System.arraycopy方法来复制数组，这个方法其实就是Arrays.copy()的底层方法。

```java
public static native void arraycopy(Object src,  int  srcPos,
                                    Object dest, int destPos,
                                    int length);
```

src代表数据源，srcPos代表复制的位置，dest代表要复制的结果数据源，destPos，结果数据源的复制位置，length代表复制长度。代入之后就是从elementData中，复制从index到size - index这段元素，然后替换位置从index + 1开始，长度为size - index这段元素，最后给位置为index的地方赋值。看图：
![这里写图片描述](https://img-blog.csdn.net/20170804171056522?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDYxODE5NA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

看了以上两个add方法后，addAll方法的流程其实也是相同的，第一步：扩容，第二步：复制数组。

#### get方法

```java
public E get(int index) {
    rangeCheck(index);//检验是否越界
    return elementData(index);
}

E elementData(int index) {
    return (E) elementData[index];
}
```

get方法就这么点。。。所以说数组查询元素很简单嘛，是不是

#### remove方法

```java
public E remove(int index) {
    rangeCheck(index);//检验是否越界
    modCount++;
    E oldValue = elementData(index);
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index,numMoved);
    elementData[--size] = null; // clear to let GC do its work 将最后一位变成空，让系统GC
    return oldValue;
}
```

这里的大多数方法和内容都多么的似曾相似，见图，不多说了：

![这里写图片描述](https://img-blog.csdn.net/20170804171115744?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDYxODE5NA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

然后，remove(Object o)流程也是相似的

### 总结

把ArrayList中的一系列方法都过了一遍之后，我们可以总结出如下内容：

1. 查询数组中的元素效率非常高。

2. 插入和删除数组中的元素需要复制数组中的元素，效率是很低的。

3. ArrayList作为一个容器动态的帮我们维护了数组的大小和内容，我们只需要关心怎么使用即可。

4. Arrays中有大量操作数组的方法

5. 文章上述的内容更多的是想让大家理解数组以及数组容器而不是源码，希望大家有所收获。

   

## 队列

队列稍有耳闻的同学肯定会知道它有一个特点：先进先出。正是这个特点使得队列在处理一些对于顺序要求很高的需求时有很好的效果，就像网络请求的排序，队列大概是这样的：

![这里写图片描述](https://img-blog.csdn.net/20170807153011495?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDYxODE5NA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

上图可以看出队列是一个很明显的先进先出的结构，中间的元素是不允许修改的。

java中使用Queue（队列）来描述队列，它里面有一系列方法：

- offer方法,向队列尾部入列一个元素；
- poll方法，把队列的第一个元素出列；
- peek方法，查看队列的第一个元素，但是不出列；

除了上面3个方法，其实还有3个方法：add，remove，element与上面3个方法对应，唯一的区别就是这3个方法会抛出异常，这不是我们关注的重点。

java种关于Queue的实现结构如下：

![image](https://img-blog.csdn.net/20170807152858037?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDYxODE5NA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

其中:

**-priorityQueue虽然是Queue的实现类，但是它的实现与队列的“先进先出”特点是有些出入的，因为它会对每次进入队列的元素进行排序，也就是说当使用peek方法取出的元素可能不会是第一个进入队列的元素，而是整个队列最小的元素。**

**-Deque是一个继承了Queue的接口，它在Queue的基础上增加了几个方法，使它成为了一个双端队列的结构，可以通过这些方法来操作队列两端的元素**

**-ArrayDeque是Deque的一个典型实现**

**-LinkedList，当时是把它当作做链表来讲解，其实它还实现了Deque，也就是说它还可以当做双端队列来使用。**

既然java没有给我们提供一个规范的队列实现，我们就自己动手来写一个遵循”先进先出“规则的简单队列，毕竟我们学习数据结构更多的是理解这些数据结构的特点，而不是过多的关心它的实现。

```java
public class Queue<E> {
    private Object[] data = null;// 队列
    private int front;// 队列头，允许删除
    private int rear;// 队列尾，允许插入

    public Queue() {
        this(10);// 默认队列的大小为10
    }

    public Queue(int initialSize) {
        data = new Object[initialSize];
        front = rear = 0;
    }

    // 入列一个元素
    public void offer(E e) {
        data[rear++] = e;
    }

    // 返回队首元素，但不删除
    public E peek() {
        return (E) data[front];
    }

    // 出队排在最前面的一个元素
    public E poll() {
        E value = (E) data[front];// 保留队列的front端的元素的值
        data[front++] = null;// 释放队列的front端的元素
        return value;
    }
}
```

来测试一下：

```java
Queue<String> queue = new Queue<>();
queue.offer("1");
queue.offer("2");
queue.offer("3");
queue.offer("4");
System.out.println("当前第一个元素: " + queue.peek());// 取队列第一个元素
System.out.println("出列第一个元素: " + queue.poll());// 出列第一个元素
System.out.println("当前第一个元素: " + queue.peek());// 取队列第一个元素
//结果如下：
当前第一个元素: 1
出列第一个元素: 1
当前第一个元素: 2
```

代码很简单，没有做异常处理和各种边界处理，但是遵循了“先进先出的原理”。

## 栈

栈的特性是：先进后出。它的性质和队列有些相似，都可以通过他们的特性在某些场景发挥很好的作用：

![这里写图片描述](https://img-blog.csdn.net/20170809110130160?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDYxODE5NA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

在java中使用Stack这个类来描述栈。

```java
public class Stack<E> extends Vector<E> {
    ...
}
```

可以看到Stack是继承自Vector同ArrayList一样，是一个比较古老的类，它是线程安全的。Stack中大部份方法都是直接调用Vector的，所以也是线程安全的。

Stack中有几个常用的方法：

```java
public E push(E item)// 入栈一个元素
public synchronized E pop()// 出栈一个元素
public synchronized E peek()// 查看栈顶元素
public boolean empty()// 判断是否为空
public synchronized int search(Object o)// 到栈中查找一个元素
```

这几个方法都言简意赅，至于源码的流程，Vector中的方法实现与ArrayList基本相同。

实现流程：

1. 新建一个空栈
2. 读入字符串，如遇到开放符号，则入栈，开放符号为:”{“、”(“、”[“，这种。
3. 如果遇到关闭符号，就出栈当前栈顶元素，把出栈的元素与这个关闭符号进行匹配，像”{“对应的是”}”，如果匹配是正确的就说明符号是对应的，如果匹配是不正确的，就说明编码有问题。
4. 如果传入的字符串是正确的，程序按照上述3步运行下来，栈应该还是空的。如果栈非空，说明编码有问题

把上面4步转换成代码，可以得到以下程序：

```java
public static boolean test(String code) {
    Stack<String> stack = new Stack<>();
    char[] chars = code.toCharArray();// 把输入的字符串拆分成char一个一个读取
    for (char aChar : chars) {
        String s = String.valueOf(aChar);// char转String
        if ("{".equals(s) || "[".equals(s) || "(".equals(s)) {
            stack.push(String.valueOf(aChar));// 如果是开放符号就入栈
        }
        if (stack.isEmpty()) {
            // 如果栈是空的，就说明有关闭符号缺少相应的开放符号
            System.out.println("您的代码中有符号不对应");
            return false;
        }
        // 如果是关闭符号，则出栈最上层的元素进行比较
        // 如果是配对的符号，就说明是正确的；如果不是配对的符号，就说明是错误的
        switch (s) {
            case "}":
                if (!"{".equals(stack.pop())) {
                    System.out.println("您的代码中有符号不对应");
                    return false;
                }
                break;
            case "]":
                if (!"[".equals(stack.pop())) {
                    System.out.println("您的代码中有符号不对应");
                    return false;
                }
                break;
            case ")":
                if (!"(".equals(stack.pop())) {
                    System.out.println("您的代码中有符号不对应");
                    return false;
                }
                break;
        }
    }
    if (!stack.isEmpty()) {
        System.out.println("您的代码中缺少闭合符号");
        return false;
    }
    return true;
}
```

代码的注释写的很详细，流程也很清晰，我们来测试一下几种情况：

```java
// 以下模拟了4种情况：
System.out.println(test("{[]}"));// 正确
System.out.println("————————————————————————————————");
System.out.println(test("{[{}{}]"));// 缺少结尾"}"
System.out.println("————————————————————————————————");
System.out.println(test("{[{}{}]}}"));// 结尾多了一个"}"
System.out.println("————————————————————————————————");
System.out.println(test("{[{}{(}]}"));// 中间多了一个"("
```

输出结果为：

```java
true
————————————————————————————————
您的代码中缺少闭合符号
false
————————————————————————————————
您的代码中有符号不对应
false
————————————————————————————————
您的代码中有符号不对应
false
```

可以看到我还判断了出错的大概原因，利用栈结构就很好的完成了编码检查这个工作，像这种思想还可以运用到我们的实际开发中，希望大家都能有所体会。

# 线程

进程是资源分配的最小单位，运行一个进程需要，CPU，内存，磁盘IO；线程是CPU调度的最小单位；线程必须依赖于进程存在。（模块化，异步化，简单化），Linux是开启的线程不能超过1000个，每个开启一个线程都要分配一个栈空间。

```java
public class OnlyMain{
    public static void main(String[] args){
        //Java 虚拟机线程系统的管理接口
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的monitor 和 synchronizer信息，仅仅获取线程和线程堆信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false,false);
        //遍历线程信息，仅打印线程ID和线程名称信息
        for（ThreadInfo threadInfo: threadInfos）{
            System.out.println("["+threadInfo.getThreadId()+"]"
                               +threadInfo.getThreadName());
        }
    }
}
```

新开启线程的方式位两种（官方JDK标注的）！

Thread是java语言对线程的抽象，Runnable是对任务的抽象。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210318234950490.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2thcnNvbk5ldA==,size_16,color_FFFFFF,t_70)

### **死锁**

规范定义：死锁是指两个或两个以上的线程在执行过程中，由于竞争资源或者由于彼此通信而造成的一种阻塞的现象，若无外力作用，它们都将无法推进下去。此时称系统处于死锁状态或者系统产生了死锁。

#### **yield()方法**

让出CPU执行权（让出的时间不能设定），让出后，仍然还是会被选中。（不会让出锁，只是让出执行权）

#### **sleep()方法**

不会释放锁

#### **run()方法**

这个并不是线程的运行方法，只是等于调用了内部方法，真正执行的是主线程；

#### **start()方法**

这个是调用线程运行的方法，将线程变成就绪状态，当线程拿到执行权的时候，才是真正的运行状态。

#### **join（）方法**

类似于插队，面试题中会问到，怎么让线程顺序执行，可以利用join()方法，会让线程变成一个串型执行。

#### **interrupt()方法**

推荐使用这个方法来停止线程

#### **wait()方法**

对象调用wait方法后，他会把他持有的锁给释放掉。 

#### **notify()方法**

不会释放锁，只会唤醒一个等待线程。

#### **notifyAll()方法**  

不会释放锁，会唤醒所有的等待线程。

**!!! 为什么不建议用stop停止线程，因为他会导致线程所占用的资源不会正常的释放**

**!!! JDK线程是协作式的，不是抢占式**

**!!! 处于死锁状态，是不会理会中断的**

**!!!守护线程Run方法里finally不一定会执行**

#### **句柄**

文件标识符

### 线程基础、线程之间得共享和协作

#### **CPU时间片轮转机制（RR调度）**

#### **上下文切换**

大概需要20000CPU周期

#### **并行**

实际上是一起执行

#### **并发**

实际上是交替的运行，在讨论并发的时候一定不能脱离时间单位，比如说单位时间内并发量是多少。（比如说咖啡机一分钟能4个人通过，那么就说这个并发量是一分钟4个）；

### 线程得并发工具类

### 原子锁操 作 CAS（乐观锁）

（Compare And Swap）比较并且交换，原子操作就是不可再分。自旋：就是等于死循环。 

实现原子操作可以使用锁，锁机制，满足基本的需求是没有问题的了，但是有的时候我们的需求并非这么简单，我们需要更有效，更加灵活的机制，synchronized关键字是基于阻塞的锁机制，也就是说当一个线程拥有锁的时候，访问同一资源的其它线程需要等待，直到该线程释放锁，

这里会有些问题：首先，如果被阻塞的线程优先级很高很重要怎么办？其次，如果获得锁的线程一直不释放锁怎么办？（这种情况是非常糟糕的）。还有一种情况，如果有大量的线程来竞争资源，那CPU将会花费大量的时间和资源来处理这些竞争，同时，还有可能出现一些例如死锁之类的情况，最后，其实锁机制是一种比较粗糙，粒度比较大的机制，相对于像计数器这样的需求有点儿过于笨重。

CAS的原理：利用了现代处理器都支持的CAS的指令，循环这个指令，直到成功为止。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210329153019181.png)

**CAS的问题**

**1.ABA问题。**解决办法就是带上一个版本戳 AtomicMarkableReference（关心这个值有没有人动过），AtomicStampedReference（关心这个有没有人动过，还关心被人动过几次）这两个类

**2.开销问题。**自旋CAS如果长时间不成功，会给CPU带来非常大的执行开销。 

**3.只能保证一个共享变量的原子操作。**解决办法AtomicReference，把要修改的值打包到一个类里面去修改

 

### 显示锁和AQS

同步工具类的内部（继承AQS），要求使用 模板方法的设计模式

```java
/**
* 类说明：抽象蛋糕
*/
public abstract class AbstractCake{
    protected abstract void shape();//造型
    protected abstract void apply();//涂抹
    protected abstract void brake();//烤制
    
    public final void run(){
        this.shape();
        this.apply();
        this.brake();
    } 
}
```

AQS的基本思想是CLH队列锁，**AQS自旋锁不会一直自旋，自旋到一定次数就会挂起阻塞等待唤醒**，每一个等拿锁的线程打包成一个节点，挂到一个链表上，每个线程不断检测前面一个线程是否释放锁，释放了就拿锁。state成员变量，靠改变这个成员变量来检测拿锁

### 并发容器

### 线程池和Exector框架

线程时稀缺又昂贵的资源。经常创建和销毁线程会造成严重的资源浪费

#### 线程池的创建，各参数的含义

```java
//7个参数
public ThreadPoolExecutor(
    int corePoolSize,//线程池的核心线程数
    int maximumPoolSize,//可使用的最大线程数
    long keepAliveTime,//控制空闲线程的醋存活时间，如果超过时间会销毁空闲的线程
    TimeUnit unit,//时间单位
    BlockingQueue<Runnable> workQueue,//阻塞队列，如果要工作的线程超过了maximumPoolSize，就会放到阻塞队列里面来，尽量配置成有界的
    ThreadFactory threadFactory,//线程工厂。可以创建线程时微调
    RejectedExecutionHandler handler//拒绝策略
) {
    
}

```

**流程：如果线程池是空的时间，工作线程进来会在corePool里面，当超过了corePoolSize以后，线程会放到workQueue阻塞队列里面去，当workQueue放满以后，线程会在maximumPool里创建，但是不能超过maximumPoolSize数量，一但超过，RejectedExecutionHandler拒绝策略就会起作用。**

!!!JDK为我们提供了4种拒绝策略：

1. DiscardOldestPolicy：直接丢弃最老的那一个
2. AbortPolicy：直接抛出异常，默认策略
3. CallerRunsPolicy：让调用者线程执行任务，谁调用谁执行
4. DiscardPolicy：把最新提交的任务直接扔了

#### 关闭线程池

1. shutdown：尝试关闭线程池，所有当前没有执行的线程进行中断
2. shutdowNow：不管有没有执行，都会尝试进行中断 

#### 任务特性

1. CPU密集型：从内存中取数进行机算，maximumPoolSize配置的时候不要超过CPU核心数 + 1。（cpu+1是为了解决cpu页缺失状态）
2. IO密集型：读取磁盘，网络通讯，线程数：maximumPoolSize配置的时候机器得cpu核心数*2
3. 混合型，综合上面



### **阻塞队列**

概念、生产者消费性模式，平衡性能问题

常用阻塞队列：

- ArrayBlockingQueue：一个由数组结构组成的有界阻塞队列。
- LinkedBlockingQueue：一个由链表结构组成的有界阻塞队列。
- PriorityBlockingQueue：一个支持优先级排序的无界阻塞队列。实现是一个堆 
- DelayQueue：一个使用优先级队列实现的无界阻塞队列。支持元素的延迟获取
- SynchronousQueue：一个不存储元素的阻塞队列。里面不存在任何元素，都在内存里，解决耦合问题
- LinkedTransferQueue：一个由链表结构组成的无界阻塞队列。transfer（）方法，如果有消费者在等待拿东西，生产者直接给消费者，不再放进阻塞队列。
- LinkedBlockingDeque：一个由链表结构组成的双向阻塞队列。

### 线程安全

### JVM和底层原理

### synchronized（悲观锁）

（原子操作）锁的是对象，注意锁的对象不能变化。

使用monitorenter 和 monitorexit 指令实现的

- monitorenter指令是在编译后插入到同步代码块的开始位置，而monitorexit是插入到方法结束处和异常处。
- 每个monitorenter 必须有对应的monitorexit  与之配对
- 任何对象都有一个monitor 与之关联

#### **用处和用法** 

#### **对象锁**

#### **类锁**

其实锁的还是一个对象，不过锁的是每一个类里面在虚拟机里仅有的一个对象。

### volatile

最轻量的同步机制，保证的是可见性（并不保证原子性），加了volatile之后，子线程就可以看到主线程的成员变量的修改。

一写多读的情况下适合使用volatile。

volatile只是强制读的时候在堆内存（主内存）读，并且强制更新回去。

有volatile变量修饰的共享变量进行写操作的时候会使用CPU提供的Lock前缀指令。

- 将当前处理器缓存行的数据写回到系统内存。
- 这个写回内存的操作会使在其他CPU里缓存了该内存地址的数据无效。

**!!!面试题要点 i++ 但是会返回新的内存地址，因为实现的时候其实是返回了一个new integer()**



### ThreadLocal

为每一个线程提供了一个变量副本，实现了线程的隔离。

#### 实现解析

每个线程里都有一个ThreadlocalMap ，以Threadlocal是Key，obj为Value。默认大小为16；

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210320131243874.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2thcnNvbk5ldA==,size_16,color_FFFFFF,t_70)



#### 引发内容泄露分析

remove里面会调用expungeStaleEntry()方法做清除。

**1.强引用**

**2.软引用**

**3.弱引用**

**4.虚引用**

### Thread local的线程不安全

### Fork-Join

体现了分而治之，**算法**中属于分而治之的有 **归并排序**，**快速排序**，**二分查找**

大问题--》分割 相同的小问题，小问题之间无关联

#### 设计思想

#### 策略

### 线程面试题

#### synchronized 的原理以及ReentrantLock的区别

答：synchronized 主要是靠monitorenter 指令来实现， ReentrantLock拿锁的动作可以中断，可以实现可重入。synchronized 是非公平锁，ReentrantLock可以实现公平锁 和 非公平锁。

#### synchronized 做了哪些优化

答：为了提升性能，JDK以入了偏向锁，自旋锁（轻量级锁），重量级锁。

#### volatile能否保证线程安全？在DCL(双重检测锁定)上的作用是什么？

答:虚拟机加载机制，虚拟器会加锁，保证同一时间只有一个线程可以执行类加载机制。  

#### sleep可以中断吗？

答：可以的，所以才会实现sleep方法的时候要时候try

#### ThreadLocal是什么？

答：是线程的本地变量，特殊变量。TheadLocal为每个线程实现的本地的变量副本。 

**!!!静态内部类跟外面的类没有任何关系，可以理解为一个独立的类**

# 序列化

序列化的概念：将数据结构或对象转换成二进制串的过程。

反序列化：将序列化过程中所生成的二进制串转换成数据结构或者对象的过程。

持久化：把数据结构或对象，存储起来。



### 1.Serializable

标识：tags

*1.通过IO对硬盘操作，速度较慢

*2.大小不受限制

*3.大量使用反射，产生内存碎片

objectOutput

objectStreamClass：描述一个对象的结构

### 2.Parcelable

*1.直接在内存操作，效率高，性能好

*2.一般不能超过1m，修改内核也只能4m

### 3.json，xml，protobuf



# C语言

#include <stdio.h>

- <>寻找系统得资源
- ""  寻找我们自己写的资源
- .h   .hpp （声明文件  头文件）
- .c   .cpp  (实现文件)

```c
#include <stdio.h>
//基本数据类型
int main(){
    
    int i = 100;
    double d = 200.21;
    float f = 200;
    long l = 100;
    short s = 100;
    char c = 'd';
    //字符串
    char * str = "Karson";
    
    //不是随便打印，需要占位
    printf("i的值是：%d\n",i);
    printf("d的值是：%lf\n",d); 
    printf("f的值是：%f\n",f);
    printf("l的值是：%d\n",l);
    printf("s的值是：%d\n",s);
    printf("c的值是：%c\n",c);
    printf("str的值是：%s\n",str);
    
    return 0;
}
```

基本数据类型占多少个字节，***不同系统不同！！***

int == 4字节

double == 8字节

char == 1字节







   