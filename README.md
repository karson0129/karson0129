# 笔记

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













