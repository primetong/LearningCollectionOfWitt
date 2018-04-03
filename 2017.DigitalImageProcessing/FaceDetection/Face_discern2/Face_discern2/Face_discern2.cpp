#include "stdafx.h"  
#include "opencv2\core\core.hpp"
#include "opencv2\objdetect\objdetect.hpp"
#include "opencv2\highgui\highgui.hpp"
#include "opencv2\imgproc\imgproc.hpp"
#include <iostream>
#include <stdio.h>

using namespace std;//声明？
using namespace cv;

string face_cascade_name = "haarcascade_frontalface_alt.xml";//面部特征分类器（harr特征），可以选用其他分类器
CascadeClassifier face_cascade;//用于检测人脸的分类器
string window_name0 = "原图";//令一个画图窗口
string window_name1 = "图一：画个圈圈";
string window_name2 = "图二：只看脸";
string window_name3 = "图五：仅切割人脸轮廓（灰度）（依靠‘与’实现）";
string window_name4 = "图六：仅切割人脸轮廓（彩色）（通过‘选填色’实现）";
string window_name5 = "图四：灰度二值化";
string window_name6 = "图三：灰度化";
string window_name7 = "直方图均衡化后的效果";
void detectAndDisplay(Mat frame);

int main(int argc, char** argv){
	Mat image0, image1, thresholded, biner, biner1,image2;//opencv里面的图像矩阵类型
	//Mat frame_slice_face_total, frame_slice_face;
	//image = imread(argv[1]);//这句是在提取图片,建立通道，没啥必要这样作
	image1 = imread("lyf.jpg");//这句是在提取图片
	//image0 = image1;
	//cvtColor(image1, biner, CV_BGR2GRAY);//灰度化
	//imshow(window_name6, biner);
	//equalizeHist(biner, biner1);//前in后out
	//imshow(window_name7, biner1);

	//threshold(biner, thresholded, 100, 255, cv::THRESH_BINARY);//二值化
	//imshow(window_name5, thresholded);
	//imwrite("mei.jpg", thresholded);


	if (argc != 2 || !image1.data){
		printf("[error] 没有图片\n");
		return -1;
	}

	if (!face_cascade.load(face_cascade_name)){
		printf("[error] 无法加载级联分类器文件！\n");
		return -1;
	}

	detectAndDisplay(image1);//提取并且显示人脸（非自适应）
	waitKey(0);
}

void detectAndDisplay(Mat frame){
	std::vector<Rect> faces,conters;//就是数组容器类，尖括号表示里面的RECT是实际类，整体定义了faces对象是一个RECT数组。 不用定义容器大小
	Mat frame_gray;
	Mat frame_slice(frame.rows, frame.cols, frame.type(), Scalar(180, 120, 50));//填充时必须给定大小
	Mat frame_slice_gray, frame_slice_binerary, frame_slice_binerary_slice;
	Mat frame_slice_binerary_slice_color(frame.rows, frame.cols, frame.type(), Scalar(180, 120, 50));//填充时必须给定大小
	cvtColor(frame, frame_gray, CV_BGR2GRAY);//先要进行灰度化操作，把传入的mat矩阵灰度化后放入frame_gray里面
	//equalizeHist(frame_gray, frame_gray);//不知道为什么直方图均衡化，边缘部分将增强？则分类器更能分辨那是不是人脸的一部分，这个向老师咨询？？

	face_cascade.detectMultiScale(frame_gray, faces, 1.1, 2, 0 | CV_HAAR_SCALE_IMAGE, Size(30, 30));//在调用库里的特征提取分类器
	//face_cascade.detectMultiScale(frame_gray, faces, 1.1, 2, 0 | CV_HAAR_DO_CANNY_PRUNING, Size(30, 30));另外一种canny边缘检测

	//conters = faces;
	//接下来将它画出来
	for (int i = 0; i < faces.size(); i++)
	{
		Point2d center(faces[i].x + faces[i].width*0.55, faces[i].y + faces[i].height*0.43);//中心位置,faces[i]是指的第几张人脸，每一个参数都要搞懂！血的教训！
		ellipse(frame, center, Size(faces[i].width*0.52, faces[i].height*0.65), 0, 0, 360, Scalar(255, 0, 0), 4, 8, 0);//使用椭圆
		//rectangle(frame, faces[i].x + faces[i].width*0.58, faces[i].y + faces[i].height*0.43, Scalar(255, 0, 0));//使用矩形

		//接下来将它割出来(使用椭圆)
		for (int x = 0; x < frame.cols; x++)
		{
			for (int y = 0; y < frame.rows; y++)
			{
				//int temp = ((x - center.x) * (x - center.x) + (y - center.y) *(y - center.y)) / (faces[i].width*0.54*faces[i].width*0.54);
				int temp = ((x - center.x) * (x - center.x) /((faces[i].width*0.54)*(faces[i].width*0.54)) + (y - center.y) *(y - center.y) / ((faces[i].height*0.65)*(faces[i].height*0.65)));
				if (temp<1)
				{
					frame_slice.at<Vec3b>(Point(x, y))[0] = frame.at<Vec3b>(Point(x, y))[0];
					frame_slice.at<Vec3b>(Point(x, y))[1] = frame.at<Vec3b>(Point(x, y))[1];
					frame_slice.at<Vec3b>(Point(x, y))[2] = frame.at<Vec3b>(Point(x, y))[2];
				}
				else
				{
					frame_slice.at<Vec3b>(Point(x, y))[0] = 0;
					frame_slice.at<Vec3b>(Point(x, y))[1] = 0;
					frame_slice.at<Vec3b>(Point(x, y))[2] = 0;
				}
			}
		}
		//研究一下数组容器类
		//conters[i] = faces[i];
		//for (int x = 0; x < frame.cols; x++)
		//{
		//	for (int y = 0; y < frame.rows; y++)
		//	{
		//		//int temp = ((x - center.x) * (x - center.x) + (y - center.y) *(y - center.y)) / (faces[i].width*0.54*faces[i].width*0.54);
		//		int temp = ((x - center.x) * (x - center.x) / ((faces[i].width*0.54)*(faces[i].width*0.54)) + (y - center.y) *(y - center.y) / ((faces[i].height*0.65)*(faces[i].height*0.65)));
		//		if (temp<1)
		//		{
		//			conters[i].at<Vec3b>(Point(x, y))[0] = frame.at<Vec3b>(Point(x, y))[0];
		//			conters[i].at<Vec3b>(Point(x, y))[1] = frame.at<Vec3b>(Point(x, y))[1];
		//			conters[i].at<Vec3b>(Point(x, y))[2] = frame.at<Vec3b>(Point(x, y))[2];
		//		}
		//		else
		//		{
		//			conters[i].at<Vec3b>(Point(x, y))[0] = 0;
		//			conters[i].at<Vec3b>(Point(x, y))[1] = 0;
		//			conters[i].at<Vec3b>(Point(x, y))[2] = 0;
		//		}
		//	}
		//}
	}
	//灰度化以及二值化
	cvtColor(frame_slice, frame_slice_gray, CV_BGR2GRAY);//灰度化
	threshold(frame_slice_gray, frame_slice_binerary, 130, 255, cv::THRESH_BINARY);//二值化（无法有效切割人脸与脖子部分的区别，因为这个切割方法太low，需要边缘提取算法来实现）阈值需要自适应(可以设置成自适应)
	//利用与操作实现切割灰度图
	bitwise_and(frame_slice_binerary, frame_slice_gray, frame_slice_binerary_slice);//不能得到原灰度图位置的值，160与255与得到新值？
	//利用二值化的结果，得到原彩色图的人脸（填色）
	for (int x = 0; x < frame.cols; x++)
	{
		for (int y = 0; y < frame.rows; y++)
		{
			int temp = (int)frame_slice_binerary.at<uchar>(y, x);
			if (temp == 255)
			{
				frame_slice_binerary_slice_color.at<Vec3b>(Point(x, y))[0] = frame.at<Vec3b>(Point(x, y))[0];
				frame_slice_binerary_slice_color.at<Vec3b>(Point(x, y))[1] = frame.at<Vec3b>(Point(x, y))[1];
				frame_slice_binerary_slice_color.at<Vec3b>(Point(x, y))[2] = frame.at<Vec3b>(Point(x, y))[2];
			}
			if(temp == 0)
			{
				frame_slice_binerary_slice_color.at<Vec3b>(Point(x, y))[0] = 0;
				frame_slice_binerary_slice_color.at<Vec3b>(Point(x, y))[1] = 0;
				frame_slice_binerary_slice_color.at<Vec3b>(Point(x, y))[2] = 0;
			}
		}
	}

	//显示
	imshow(window_name1, frame);
	imshow(window_name2, frame_slice);
	imshow(window_name6, frame_slice_gray);
	imshow(window_name5, frame_slice_binerary);
	imshow(window_name3, frame_slice_binerary_slice);
	imshow(window_name4, frame_slice_binerary_slice_color);

}