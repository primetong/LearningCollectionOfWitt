#coding:utf-8
import tensorflow as tf
#加载mnist_inference.py中定义的常量和前向传播的函数
import mnist_inference
#加载mnist_train.py中定义的常量和函数
import mnist_train

from PIL import Image
import numpy as np

def img_read():
    number = str(input("please input one number:"))
    img = Image.open("./handwriting_number/" + number).convert('L').resize((28, 28))
    imgbin = 1 - (np.array(img) ) / 200	#二值化图像并取反，除数是阈值
    print imgbin
    imgline = imgbin.reshape(1, 784)
    #print imgline
    return imgline

def img_predict():
    with tf.Graph().as_default() as g:
        img = img_read()
        #定义输入输出placeholder
        x = tf.placeholder(tf.float32, [None, mnist_inference.INPUT_NODE], name='x-input')

        validate_feed = {x: img}

        #可以直接使用mnist_inference.py中定义的前向传播过程，由于测试时不关心正则化损失所以传None
        y = mnist_inference.inference(x, None)

        print(y)

        result=tf.argmax(y, 1)

        #通过变量重命名的方式来加载模型，从而可以共用mnist_inference.py中定义的前向传播过程
        variable_averages = tf.train.ExponentialMovingAverage(mnist_train.MOVING_AVERAGE_DECAY)
        variables_to_restore = variable_averages.variables_to_restore()
        saver = tf.train.Saver(variables_to_restore)

        with tf.Session() as sess:
            #tf.train.get_checkpoint_state函数通过checkpoint文件自动找到最新模型的文件名
            ckpt = tf.train.get_checkpoint_state(mnist_train.MODEL_SAVE_PATH)
            if ckpt and ckpt.model_checkpoint_path:
                saver.restore(sess, ckpt.model_checkpoint_path)#加载模型

                predict = sess.run(result, feed_dict=validate_feed)  # 加载验证的数据,计算错误率
                print ('OK!I predict the number is %s' % predict)

            else:
                print('No checkpoint file found!')  # 没找到模型
                return
           

def main():
    while 1:
        img_predict()
if __name__ == '__main__':
    main()
 
