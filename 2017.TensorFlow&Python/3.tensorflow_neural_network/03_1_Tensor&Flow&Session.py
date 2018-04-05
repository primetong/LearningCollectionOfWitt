#coding:utf-8

import tensorflow as tf

#1. 张量的概念

import tensorflow as tf
a = tf.constant([1.0, 2.0], name="a")
b = tf.constant([2.0, 3.0], name="b")
#result = tf.add(a, b,name="add")
result = a + b
print result

"""
Tensor("add:0", shape=(2,), dtype=float32)i
"""

#2.计算图中节点
x = tf.constant([[3., 3.]])  # 创建一个常量 op，产生1*2矩阵
w = tf.constant([[2.],[2.]]) # 创建另外一个常量 op, 产生一个 2x1 矩阵.
y = tf.matmul(x, w) # 创建一个矩阵乘法 matmul op , 把 'x' 和 'w' 作为输入.
print y

#3. 会话的使用

#3.1 创建和关闭会话

# 创建一个会话。
sess = tf.Session()
# 使用会话得到之前计算的结果。
print(sess.run(result))
print(sess.run(y))
# 关闭会话使得本次运行中使用到的资源可以被释放。
sess.close()

"""
[ 3.  5.]
[[12.]]
"""

#3.2 使用with statement 来创建会话

with tf.Session() as sess:
    print(sess.run(result))
    print(sess.run(y))
"""
[ 3.  5.]
[[12.]]
"""
