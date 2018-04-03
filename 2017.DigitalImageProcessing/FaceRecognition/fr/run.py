# -*- coding: utf-8 -*-

import os
import shutil
import cv2 as cv
import numpy as np

if __name__ == "__main__":

    # settings
    cascade_path = "haarcascade_frontalface_alt2.xml"
    datasets = os.path.join("fr", "data")
    testsets = os.path.join("crawler", "downloads", "images", "full")
    stdsize = (128, 128)

    # message
    print("{}\n# Start {}\n#".format(78*"#", "Face Recognition"))
    msg = "#  - {}"

    # files must exist
    paths = (cascade_path, datasets, testsets)
    missing = ", ".join(s for s in filter(lambda f: not os.path.exists(f), paths))
    if missing:
        print(msg.format("Missing '{}'".format(missing)))
        exit()

    # face detection Cascade
    face_cascade = cv.CascadeClassifier(cascade_path)
    if not face_cascade:
        print(msg.format("Invalid '{}'".format(cascade_path)))
        exit()

    # Local Binary Patterns Histograms Face Recognizer
    recognizer = cv.face.LBPHFaceRecognizer_create(1, 8, 8, 8, 100.)

    # get training data
    print(msg.format("Load datasets"))
    images = []
    labels = []
    for name in os.listdir(datasets):
        fullname = os.path.join(datasets, name)
        lbl = name[:name.find(".")]
        if not lbl: continue
        else: lbl = int(lbl)
        img = cv.imread(fullname, cv.IMREAD_GRAYSCALE)
        if img is None: continue
        for (x, y, w, h) in face_cascade.detectMultiScale(img, 1.2, 4):
            images.append(img)
            labels.append(lbl)
            #cv.imshow(name, img[y: y + h, x: x + w])
            #cv.waitKey()
        #cv.destroyAllWindows()
    if not images or not labels:
        print(msg.format("No datasets, please check '{}'".format(datasets)))
        exit()

    # start training
    print(msg.format("Start training"))
    recognizer.train(images, np.array(labels))

    # start testing
    print(msg.format("Start testing"))
    result = os.path.join(testsets, "result")
    if os.path.isdir(result):
        shutil.rmtree(result)
    if not os.path.exists(result):
        os.mkdir(result)
    for name in os.listdir(testsets):
        fullname = os.path.join(testsets, name)
        img = cv.imread(fullname, cv.IMREAD_GRAYSCALE)
        if img is None:
            continue
        for (x, y, w, h) in face_cascade.detectMultiScale(img, 1.2, 4):
            roi = img[y: y+h, x: x+w]
            sav = cv.resize(roi, stdsize)
            l, c = recognizer.predict(sav)
            if l == 0:
                newname = "{:02}.{:03}.{}".format(l, int(c), name)
                cv.imwrite(os.path.join(result, newname), sav)

    print(msg.format("All done. Result in '{}'".format(result)))

# References:
#
# [Face Recognition using Python and OpenCV]
# (http://hanzratech.in/2015/02/03/face-recognition-using-opencv.html)
# [AttributeError: module 'cv2.cv2' has no attribute 'createLBPHFaceRecognizer']
# (https://stackoverflow.com/a/44641712)
# [attributeerror: module 'cv2.face' has no attribute 'createlbphfacerecognizer']
# (https://stackoverflow.com/a/45673807)
# [cv2.imread: checking if image is being read]
# (https://stackoverflow.com/a/23628409)
# [Python openCV TypeError: labels data type = 18 is not supported]
# (https://stackoverflow.com/a/34791463)