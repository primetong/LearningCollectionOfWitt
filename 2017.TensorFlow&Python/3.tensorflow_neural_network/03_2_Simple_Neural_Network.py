#coding:utf-8
#1. 三层简单神经网络

import tensorflow as tf

#1.1 定义变量

w1= tf.Variable(tf.random_normal([2, 3], stddev=1, seed=1))
w2= tf.Variable(tf.random_normal([3, 1], stddev=1, seed=1))
x = tf.constant([[0.7, 0.9]])

#1.2 定义前向传播的神经网络

a = tf.matmul(x, w1)
y = tf.matmul(a, w2)

#1.3 调用会话输出结果

sess = tf.Session()
sess.run(w1.initializer)  
sess.run(w2.initializer)  
print(sess.run(y))  
sess.close()

#[[ 3.95757794]]

#2. 使用placeholder

x = tf.placeholder(tf.float32, shape=(1, 2), name="input")
a = tf.matmul(x, w1)
y = tf.matmul(a, w2)

sess = tf.Session()

init_op = tf.initialize_all_variables()  
sess.run(init_op)

print(sess.run(y, feed_dict={x: [[0.7,0.9]]}))
sess.close()

#[[ 3.95757794]]

#3. 增加多个输入
x = tf.placeholder(tf.float32, shape=(None, 2), name="input")  
a = tf.matmul(x, w1)
y = tf.matmul(a, w2)

sess = tf.Session()
#使用tf.initialize_all_variables()来初始化所有的变量
init_op = tf.initialize_all_variables()  
sess.run(init_op)

print(sess.run(y, feed_dict={x: [[0.7,0.9],[0.1,0.4],[0.5,0.8],[0.8,0.1]]}))
#print(sess.run(w1))
#print(sess.run(w2))

sess.close()

#[[ 3.95757794]
# [ 1.15376544]
# [ 3.16749191]]

