#coding:utf-8
#1.导入模块，生成模拟数据集。
import tensorflow as tf
from numpy.random import RandomState
BATCH_SIZE = 8

rdm = RandomState(1)
X = rdm.rand(128,2)
Y = [[int(x0 + x1 < 1)] for (x0, x1) in X]


#2. 定义神经网络的常量,参数，输入和输出节点,定义前向传播过程。
w1= tf.Variable(tf.random_normal([2, 3], stddev=1, seed=1))
w2= tf.Variable(tf.random_normal([3, 1], stddev=1, seed=1))


x = tf.placeholder(tf.float32, shape=(None, 2), name="x-input")
y_= tf.placeholder(tf.float32, shape=(None, 1), name='y-input')


a = tf.matmul(x, w1)
y = tf.matmul(a, w2)

#3. 定义损失函数及反向传播算法。
cross_entropy = -tf.reduce_mean(y_ * tf.log(tf.clip_by_value(y, 1e-10, 1.0))) 
train_step = tf.train.AdamOptimizer(0.001).minimize(cross_entropy)
#train_step = tf.train.GradientDescentOptimizer(0.001).minimize(cross_entropy)
#train_step = tf.train.MomentumOptimizer(0.001,0.9).minimize(cross_entropy)


#4. 创建一个会话来运行TensorFlow程序。反复运行反向传播
with tf.Session() as sess:
    init_op = tf.initialize_all_variables()
    sess.run(init_op)
    # 输出目前（未经训练）的参数取值。
    print "w1:\n", sess.run(w1)
    print "w2:\n", sess.run(w2)
    print "\n"
    
    # 训练模型。
    STEPS = 5000
    for i in range(STEPS):
        start = (i*BATCH_SIZE) % 128
        end = (i*BATCH_SIZE) % 128 + BATCH_SIZE
        sess.run(train_step, feed_dict={x: X[start:end], y_: Y[start:end]})
        if i % 1000 == 0:
            total_cross_entropy = sess.run(cross_entropy, feed_dict={x: X, y_: Y})
            print("After %d training step(s), cross entropy on all data is %g" % (i, total_cross_entropy))
    
    # 输出训练后的参数取值。
    print "\n"
    print "w1:\n", sess.run(w1)
    print "w2:\n", sess.run(w2)

"""
w1: [[-0.81131822  1.48459876  0.06532937]
 [-2.44270396  0.0992484   0.59122431]]
w2: [[-0.81131822]
 [ 1.48459876]
 [ 0.06532937]]


After 0 training step(s), cross entropy on all data is 0.0674925
After 1000 training step(s), cross entropy on all data is 0.0163385
After 2000 training step(s), cross entropy on all data is 0.00907547
After 3000 training step(s), cross entropy on all data is 0.00714436
After 4000 training step(s), cross entropy on all data is 0.00578471


w1: [[-1.9618274   2.58235407  1.68203783]
 [-3.4681716   1.06982327  2.11788988]]
w2: [[-1.8247149 ]
 [ 2.68546653]
 [ 1.41819501]]
"""
