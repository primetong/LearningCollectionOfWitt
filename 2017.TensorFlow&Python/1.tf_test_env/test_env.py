import tensorflow as tf
import numpy as np
hello = tf.constant('Hello, TensorFlow!')
sess = tf.Session()
print sess.run(hello)

print np.array([2017,10,9])
