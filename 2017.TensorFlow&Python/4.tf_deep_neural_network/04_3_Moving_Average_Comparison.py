#coding:utf-8
import tensorflow as tf

#1. 定义变量及滑动平均类
#定义一个32位浮点变量，初始值为0.0  这个代码就是不断更新v1参数，优化v1参数，滑动平均做了个v1的影子
v1 = tf.Variable(0, dtype=tf.float32)
#定义num_updates（NN的迭代轮数）,初始值为0，不可被优化（训练），这个参数不训练
steps = tf.Variable(0, trainable=False)
#实例化滑动平均类，给删减率delay0.99，当前轮数steps
ema = tf.train.ExponentialMovingAverage(0.99, steps)
#定义滑动平均，每次运行刷新列表中的元素被更新，每次sess.run（maintain_avaerages_op）更新v1
ema_op = ema.apply([v1])

#2. 查看不同迭代中变量取值的变化。

with tf.Session() as sess:
    
    # 初始化
    init_op = tf.initialize_all_variables()
    sess.run(init_op)
    print sess.run([v1, ema.average(v1)]) #ema.average(v1)获取滑动平均后的取值
    
    # 更新变量v1的取值
    sess.run(tf.assign(v1, 5))
    sess.run(ema_op)
    print sess.run([v1, ema.average(v1)]) 
    
    # 更新step和v1的取值
    sess.run(tf.assign(steps, 10000))  
    sess.run(tf.assign(v1, 10))
    sess.run(ema_op)
    print sess.run([v1, ema.average(v1)])       
    
    # 更新一次v1的滑动平均值
    sess.run(ema_op)
    print sess.run([v1, ema.average(v1)])

    sess.run(ema_op)
    print sess.run([v1, ema.average(v1)])

    sess.run(ema_op)
    print sess.run([v1, ema.average(v1)])

"""
[0.0, 0.0]
[5.0, 4.5]
[10.0, 4.5549998]
[10.0, 4.6094499]
[10.0, 4.6633554]
[10.0, 4.7167215]
"""
