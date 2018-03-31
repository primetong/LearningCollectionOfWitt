//FaceRecognition - Detect, Cut , Save, train and predict
//Ĭ��ʹ�õ���FaceRecognizer���LBPH�㷨�������޸��㷨�����Prehelper.cpp��Recognition����
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

#define K 3  //�м���

int main(){

	char dir[256] = "D:\\OpenCV\\OpenCVLearning\\FaceRecognition\\FaceRecognition\\TestAmerica\\";

	//detectFaceAndCut(dir, K);	//���ȶ�������������⡢�ü����������ֲ����Ǳ�����ԭ·�������Ѽ��õ����ݿ���ע�Ͳ�ִ��

	vector<Mat> images, testimages;	//ѵ��ͼƬ, ����Ԥ��ͼƬ
	vector<int> labels, testlabels;	//ѵ��ͼƬ�ı�ǩ�� ����Ԥ��ͼƬ�ı�ǩ

	//�ֱ�װ��Ԥ��������ѵ����Ԥ��ͼƬ���ǩ
	loadResizeAndTogray(dir, K, images, labels, testimages, testlabels, "train");
	loadResizeAndTogray(dir, K, images, labels, testimages, testlabels, "predict");

	//FaceRecognition��Ĭ��LBPH�㷨�����Խ��������Recognition����Դ���޸�
	Ptr<FaceRecognizer> model = Recognition(images, labels, testimages, testlabels);

	//�����ݼ�ͼƬĿ¼������model.out��ʲô�ô���������
	//char* dirmodel = new char[256];
	//strcpy(dirmodel, dir); strcat(dirmodel, "model.out");
	//FILE* f = fopen(dirmodel, "w");
	//fwrite(model, sizeof(model), 1, f);
	system("pause");
	return 0;
}