# -*- coding: utf-8 -*-
import tensorflow as tf
from tensorflow.examples.tutorials.mnist import input_data
import os
#加载mnist_inference.py中定义的常量和前向传播的函数
import mnist_inference

#配置神经网络的参数
BATCH_SIZE = 100
LEARNING_RATE_BASE = 0.8
LEARNING_RATE_DECAY = 0.99
REGULARIZATION_RATE = 0.0001
TRAINING_STEPS = 30000
MOVING_AVERAGE_DECAY = 0.99
#模型保存的路径
MODEL_SAVE_PATH="./MNIST_model"
#模型保存的文件名
MODEL_NAME="mnist_model"

def train(mnist):
    #定义输入输出placeholder
    x = tf.placeholder(tf.float32, [None, mnist_inference.INPUT_NODE], name='x-input')
    y_ = tf.placeholder(tf.float32, [None, mnist_inference.OUTPUT_NODE], name='y-input')

    regularizer = tf.contrib.layers.l2_regularizer(REGULARIZATION_RATE)
    #可以直接使用mnist_inference.py中定义的前向传播过程，并传入正则化参数
    y = mnist_inference.inference(x, regularizer)
    global_step = tf.Variable(0, trainable=False)

    #定义损失函数、学习率、滑动平均、训练过程
    variable_averages = tf.train.ExponentialMovingAverage(MOVING_AVERAGE_DECAY, global_step)
    variables_averages_op = variable_averages.apply(tf.trainable_variables())
    cross_entropy = tf.nn.sparse_softmax_cross_entropy_with_logits(logits=y, labels=tf.argmax(y_, 1))
    cross_entropy_mean = tf.reduce_mean(cross_entropy)
    loss = cross_entropy_mean + tf.add_n(tf.get_collection('losses'))
    learning_rate = tf.train.exponential_decay(
        LEARNING_RATE_BASE,
        global_step,
        mnist.train.num_examples / BATCH_SIZE, LEARNING_RATE_DECAY,
        staircase=True)
    train_step = tf.train.GradientDescentOptimizer(learning_rate).minimize(loss, global_step=global_step)
    with tf.control_dependencies([train_step, variables_averages_op]):
        train_op = tf.no_op(name='train')

    #初始化TensorFLow的持久化类，用于保存与加载模型↓
    saver = tf.train.Saver()
    #variables_to_restore = variable_averages.variables_to_restore()#变量保存
    #loader = tf.train.Saver(variables_to_restore)#初始化用于加载的持久化类
    loader = tf.train.Saver()
    with tf.Session() as sess:
        tf.initialize_all_variables().run()

        ckpt = tf.train.get_checkpoint_state(MODEL_SAVE_PATH)#根据模型保存路径找到保存的模型文件
        if ckpt and ckpt.model_checkpoint_path:#在开始计算之前先判断是否有模型可以加载
            loader.restore(sess, ckpt.model_checkpoint_path)#加载保存的模型

        for i in range(TRAINING_STEPS):
            xs, ys = mnist.train.next_batch(BATCH_SIZE)
            _, loss_value, step = sess.run([train_op, loss, global_step], feed_dict={x: xs, y_: ys})
            if i % 1000 == 0:
#            if i == 0 or (i+1) % 1000 == 0:#test:使重新加载打印时的step不+1的方法1
#            if step % 1000 == 0:#test:使重新加载打印时的step不+1的方法2
                print("After %d training step(s), loss on training batch is %g." % (step, loss_value))
                saver.save(sess, os.path.join(MODEL_SAVE_PATH, MODEL_NAME), global_step=global_step)


def main(argv=None):
    mnist = input_data.read_data_sets("./MNIST_data", one_hot=True)#训练集的路径
    train(mnist)

if __name__ == '__main__':
    tf.app.run()


