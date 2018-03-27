#include <opencv2/opencv.hpp>  
//#include<opencv2/nonfree/nonfree.hpp>  
//#include<opencv2/legacy/legacy.hpp>  
#include <opencv2/xfeatures2d.hpp>
//#include<vector>
//#include <iostream>  
//#include <iomanip>  
//#include "opencv2/core/core.hpp"  
//#include "opencv2/objdetect/objdetect.hpp"  
//#include "opencv2/features2d/features2d.hpp"  
#include "opencv2/highgui/highgui.hpp"  
//#include "opencv2/calib3d/calib3d.hpp"  
//#include "opencv2/imgproc/imgproc_c.h"  
using namespace std;  
using namespace cv;  
int main(){
	Mat leftImg = imread("left.jpg");
	Mat rightImg = imread("right.jpg");

	if (leftImg.data == NULL || rightImg.data == NULL)
		return 0;

	//转化成灰度图  
	Mat leftGray;
	Mat rightGray;
	cvtColor(leftImg, leftGray, CV_BGR2GRAY);
	cvtColor(rightImg, rightGray, CV_BGR2GRAY);

	//**************Create SIFT class pointer
	Ptr<Feature2D> f2d = xfeatures2d::SIFT::create();
	//***************Detect the keypoints,进行特征点提取
	vector<KeyPoint> keypoints_left, keypoints_right;
	f2d->detect(leftGray, keypoints_left);
	f2d->detect(rightGray, keypoints_right);
	//***************Calculate descriptors (feature vectors),进行特征向量提取
	Mat descriptors_left, descriptors_right;
	f2d->compute(leftGray, keypoints_left, descriptors_left);
	f2d->compute(rightGray, keypoints_right, descriptors_right);

    FlannBasedMatcher matcher;  
    vector<DMatch> matches;
    /* 进行特征向量临近匹配 */  
    matcher.match(descriptors_left, descriptors_right, matches);  

	int matchCount = descriptors_left.rows;
	if (matchCount>15)
	{
		matchCount = 15;
		//sort(matches.begin(),matches.begin()+descriptors_left.rows,DistanceLessThan);  
		sort(matches.begin(), matches.begin() + descriptors_left.rows);

	}
	vector<Point2f> leftPoints;
	vector<Point2f> rightPoints;
	for (int i = 0; i < matchCount; i++)
	{
		leftPoints.push_back(keypoints_left[matches[i].queryIdx].pt);
		rightPoints.push_back(keypoints_right[matches[i].trainIdx].pt);
	}

	//获取左边图像到右边图像的投影映射关系
	Mat homo = findHomography(leftPoints, rightPoints);
	Mat shftMat = (Mat_<double>(3, 3) << 1.0, 0, leftImg.cols, 0, 1.0, 0, 0, 0, 1.0);

	//拼接图像  
	Mat tiledImg;
	warpPerspective(leftImg, tiledImg, shftMat*homo, Size(leftImg.cols + rightImg.cols, rightImg.rows));
	rightImg.copyTo(Mat(tiledImg, Rect(leftImg.cols, 0, rightImg.cols, rightImg.rows)));

	//保存图像  
	imwrite("tiled.jpg", tiledImg);

	//显示拼接的图像  
	imshow("tiled image", tiledImg);
	waitKey(0);
	return 0;
}
