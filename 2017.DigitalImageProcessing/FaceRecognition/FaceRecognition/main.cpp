//FaceRecognition - Detect, Cut , Save, train and predict
//默认使用的是FaceRecognizer类的LBPH算法，如需修改算法请进入Prehelper.cpp的Recognition方法
//@Author : Witt

#include "opencv2/objdetect/objdetect.hpp"  
#include "opencv2/highgui/highgui.hpp"  
#include "opencv2/imgproc/imgproc.hpp"  

#include <cctype>  
#include <iostream>  
#include <iterator>  
#include <stdio.h>  
#include "BrowseDir.h"  
#include "StatDir.h"  
#include "Prehelper.h"  

using namespace std;
using namespace cv;

#define K 3  //有几类

int main(){

	char dir[256] = "D:\\OpenCV\\OpenCVLearning\\FaceRecognition\\FaceRecognition\\TestAmerica\\";

	//detectFaceAndCut(dir, K);	//仅先对数据做人脸检测、裁剪出人脸部分并覆盖保存在原路径，如已检测好的数据可以注释不执行

	vector<Mat> images, testimages;	//训练图片, 用作预测图片
	vector<int> labels, testlabels;	//训练图片的标签， 用作预测图片的标签

	//分别装载预处理过后的训练、预测图片与标签
	loadResizeAndTogray(dir, K, images, labels, testimages, testlabels, "train");
	loadResizeAndTogray(dir, K, images, labels, testimages, testlabels, "predict");

	//FaceRecognition，默认LBPH算法，可以进入下面的Recognition函数源码修改
	Ptr<FaceRecognizer> model = Recognition(images, labels, testimages, testlabels);

	//在数据集图片目录下生成model.out（什么用处）？？？
	//char* dirmodel = new char[256];
	//strcpy(dirmodel, dir); strcat(dirmodel, "model.out");
	//FILE* f = fopen(dirmodel, "w");
	//fwrite(model, sizeof(model), 1, f);
	system("pause");
	return 0;
}