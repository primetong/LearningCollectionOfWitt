#coding:utf-8
import time
import tensorflow as tf
from tensorflow.examples.tutorials.mnist import input_data
#加载mnist_inference.py中定义的常量和前向传播的函数
import mnist_inference
#加载mnist_train.py中定义的常量和函数
import mnist_train

#每10秒加载一次最新的模型,并且在测试数据上测试最新保存的模型的正确率
EVAL_INTERVAL_SECS = 10

def evaluate(mnist):
    with tf.Graph().as_default() as g:
        #定义输入输出placeholder
        x = tf.placeholder(tf.float32, [None, mnist_inference.INPUT_NODE], name='x-input')
        y_ = tf.placeholder(tf.float32, [None, mnist_inference.OUTPUT_NODE], name='y-input')
        validate_feed = {x: mnist.validation.images, y_: mnist.validation.labels}

        #可以直接使用mnist_inference.py中定义的前向传播过程，由于测试时不关心正则化损失所以传None
        y = mnist_inference.inference(x, None)
        correct_prediction = tf.equal(tf.argmax(y, 1), tf.argmax(y_, 1))
        accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))

        #通过变量重命名的方式来加载模型，从而可以共用mnist_inference.py中定义的前向传播过程
        variable_averages = tf.train.ExponentialMovingAverage(mnist_train.MOVING_AVERAGE_DECAY)
        variables_to_restore = variable_averages.variables_to_restore()
        saver = tf.train.Saver(variables_to_restore)

        #每间隔EVAL_INTERVAL_SECS = 10秒加载一次最新保存的模型以计算正确率并观察变化
        while True:
            with tf.Session() as sess:
                #tf.train.get_checkpoint_state函数通过checkpoint文件自动找到最新模型的文件名
                ckpt = tf.train.get_checkpoint_state(mnist_train.MODEL_SAVE_PATH)
                if ckpt and ckpt.model_checkpoint_path:
                    saver.restore(sess, ckpt.model_checkpoint_path)#加载模型
                    #通过文件名获得保存模型时迭代的轮数
                    global_step = ckpt.model_checkpoint_path.split('/')[-1].split('-')[-1]
                    accuracy_score = sess.run(accuracy, feed_dict=validate_feed)
                    print("After %s training step(s), validation accuracy = %g" % (global_step, accuracy_score))
                else:
                    print('No checkpoint file found')
                    return
            time.sleep(EVAL_INTERVAL_SECS)
#主程序
def main(argv=None):
    mnist = input_data.read_data_sets("/home/witt/witt/Python/5.tf_mnist_model_persistence/MNIST_data", one_hot=True)#训练集的路径
    evaluate(mnist)

if __name__ == '__main__':
    main()
"""
Extracting /home/witt/witt/Python/5.tf_mnist_model_persistence/MNIST_data/train-images-idx3-ubyte.gz
Extracting /home/witt/witt/Python/5.tf_mnist_model_persistence/MNIST_data/train-labels-idx1-ubyte.gz
Extracting /home/witt/witt/Python/5.tf_mnist_model_persistence/MNIST_data/t10k-images-idx3-ubyte.gz
Extracting /home/witt/witt/Python/5.tf_mnist_model_persistence/MNIST_data/t10k-labels-idx1-ubyte.gz
2017-10-30 16:43:36.714400: W tensorflow/core/platform/cpu_feature_guard.cc:45] The TensorFlow library wasn't compiled to use SSE4.1 instructions, but these are available on your machine and could speed up CPU computations.
2017-10-30 16:43:36.714434: W tensorflow/core/platform/cpu_feature_guard.cc:45] The TensorFlow library wasn't compiled to use SSE4.2 instructions, but these are available on your machine and could speed up CPU computations.
2017-10-30 16:43:36.714449: W tensorflow/core/platform/cpu_feature_guard.cc:45] The TensorFlow library wasn't compiled to use AVX instructions, but these are available on your machine and could speed up CPU computations.
After 14000 training step(s), validation accuracy = 0.9838
After 15000 training step(s), validation accuracy = 0.9846
After 16000 training step(s), validation accuracy = 0.985
After 17000 training step(s), validation accuracy = 0.985
After 18000 training step(s), validation accuracy = 0.9844
After 19000 training step(s), validation accuracy = 0.9848
After 20000 training step(s), validation accuracy = 0.9852
After 21000 training step(s), validation accuracy = 0.9846
After 22000 training step(s), validation accuracy = 0.9848
After 23000 training step(s), validation accuracy = 0.9854
After 24000 training step(s), validation accuracy = 0.9852

"""
