指针与数组

一元运算符&可用于取一个对象的地址。

一元运算符*是间接寻址或间接引用运算符。当它作用于指针时，讲访问指针所指向的对象。

数组名所代表的就是该数组最开始的一个元素的地址。

pa=&a[0]可以写成 pa = a

a[i]  可以写成  *(a+i)

&a[i] 和a+i 相同的

当把数组名传递给一个函数时，实际上传递的是该数组的第一个元素地址。

在函数定义中，形式参数char s[] 和char *s是等价的 

```c
int strlen(char *s)
{
    int n;
    for (n = 0; *s != '\0', s++)
    	n++;
    return n;
}
```

地址算数运算

```c
#define ALLOCSIZE 10000 /* size of available space */
static char allocbuf[ALLOCSIZE]; /* storage for alloc */
static char *allocp = allocbuf; /* next free position */
char *alloc(int n) /* return pointer to n characters */
{
if (allocbuf + ALLOCSIZE allocp
>= n) { /* it fits */
allocp += n;
return allocp n;
/* old p */
} else /* not enough room */
return 0;
}
void afree(char *p) /* free storage pointed to by p */
{
if (p >= allocbuf && p < allocbuf + ALLOCSIZE)
allocp = p;
}
```

字符指针与函数

字符数组以空字符‘\0’结尾。

指向函数的指针

```
int *f();      /* f: function returning pointer to int */

int (*pf)();     /* pf: pointer to function returning int */
```

