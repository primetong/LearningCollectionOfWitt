#coding:utf-8
import tensorflow as tf
from numpy.random import RandomState
BATCH_SIZE = 8

#1. 生成模拟数据集。
rdm = RandomState(1)
X = rdm.rand(128,2)
Y = [[x1+x2+(rdm.rand()/10.0-0.05)] for (x1, x2) in X]

#2. 定义神经网络的相关参数和变量。
x = tf.placeholder(tf.float32, shape=(None, 2), name="x-input")
y_ = tf.placeholder(tf.float32, shape=(None, 1), name='y-input')
w1= tf.Variable(tf.random_normal([2, 1], stddev=1, seed=1))
y = tf.matmul(x, w1)

#3. 设置自定义的损失函数。
# 定义损失函数使得预测少了的损失大，于是模型应该偏向多的方向预测。
loss_less = 10
loss_more = 1
loss = tf.reduce_sum(tf.select(tf.greater(y, y_), (y - y_) * loss_more, (y_ - y) * loss_less))
train_step = tf.train.AdamOptimizer(0.001).minimize(loss)

#4. 训练模型。
with tf.Session() as sess:
    init_op = tf.initialize_all_variables()
    sess.run(init_op)
    STEPS = 5000
    for i in range(STEPS):
        start = (i*BATCH_SIZE) % 128
        end = (i*BATCH_SIZE) % 128 + BATCH_SIZE
        sess.run(train_step, feed_dict={x: X[start:end], y_: Y[start:end]})
        if i % 1000 == 0:
            print("After %d training step(s), w1 is: " % (i))
            print sess.run(w1), "\n"
    print "Final w1 is: \n", sess.run(w1)

"""
After 0 training step(s), w1 is: 
[[-0.81031823]
 [ 1.4855988 ]] 

After 1000 training step(s), w1 is: 
[[ 0.01247112]
 [ 2.1385448 ]] 

After 2000 training step(s), w1 is: 
[[ 0.45567414]
 [ 2.17060661]] 

After 3000 training step(s), w1 is: 
[[ 0.69968724]
 [ 1.8465308 ]] 

After 4000 training step(s), w1 is: 
[[ 0.89886665]
 [ 1.29736018]] 

Final w1 is: 
[[ 1.01934695]
 [ 1.04280889]]
"""

#5. 重新定义损失函数，使得预测多了的损失大，于是模型应该偏向少的方向预测。
loss_less = 1
loss_more = 10
loss = tf.reduce_sum(tf.select(tf.greater(y, y_), (y - y_) * loss_more, (y_ - y) * loss_less))
train_step = tf.train.AdamOptimizer(0.001).minimize(loss)

with tf.Session() as sess:
    init_op = tf.initialize_all_variables()
    sess.run(init_op)
    STEPS = 5000
    for i in range(STEPS):
        start = (i*BATCH_SIZE) % 128
        end = (i*BATCH_SIZE) % 128 + BATCH_SIZE
        sess.run(train_step, feed_dict={x: X[start:end], y_: Y[start:end]})
        if i % 1000 == 0:
            print("After %d training step(s), w1 is: " % (i))
            print sess.run(w1), "\n"
    print "Final w1 is: \n", sess.run(w1)
"""
After 0 training step(s), w1 is: 
[[-0.81231821]
 [ 1.48359871]] 

After 1000 training step(s), w1 is: 
[[ 0.18643527]
 [ 1.07393336]] 

After 2000 training step(s), w1 is: 
[[ 0.95444274]
 [ 0.98088616]] 

After 3000 training step(s), w1 is: 
[[ 0.95574027]
 [ 0.9806633 ]] 

After 4000 training step(s), w1 is: 
[[ 0.95466018]
 [ 0.98135227]] 

Final w1 is: 
[[ 0.95525807]
 [ 0.9813394 ]]
"""

#6. 定义损失函数为MSE。
#loss = tf.losses.mean_squared_error(y, y_)
loss = tf.reduce_mean(tf.square(y_ - y))
train_step = tf.train.AdamOptimizer(0.001).minimize(loss)

with tf.Session() as sess:
    init_op = tf.initialize_all_variables()
    sess.run(init_op)
    STEPS = 5000
    for i in range(STEPS):
        start = (i*BATCH_SIZE) % 128
        end = (i*BATCH_SIZE) % 128 + BATCH_SIZE
        sess.run(train_step, feed_dict={x: X[start:end], y_: Y[start:end]})
        if i % 1000 == 0:
            print("After %d training step(s), w1 is: " % (i))
            print sess.run(w1), "\n"
    print "Final w1 is: \n", sess.run(w1)

"""
After 0 training step(s), w1 is: 
[[-0.81031823]
 [ 1.4855988 ]] 

After 1000 training step(s), w1 is: 
[[-0.13337609]
 [ 1.81309223]] 

After 2000 training step(s), w1 is: 
[[ 0.32190299]
 [ 1.52463484]] 

After 3000 training step(s), w1 is: 
[[ 0.67850214]
 [ 1.25297272]] 

After 4000 training step(s), w1 is: 
[[ 0.89473999]
 [ 1.08598232]] 

Final w1 is: 
[[ 0.97437561]
 [ 1.0243336 ]]
"""
