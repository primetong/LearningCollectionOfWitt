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

	//ת���ɻҶ�ͼ  
	Mat leftGray;
	Mat rightGray;
	cvtColor(leftImg, leftGray, CV_BGR2GRAY);
	cvtColor(rightImg, rightGray, CV_BGR2GRAY);

	//**************Create SIFT class pointer
	Ptr<Feature2D> f2d = xfeatures2d::SIFT::create();
	//***************Detect the keypoints,������������ȡ
	vector<KeyPoint> keypoints_left, keypoints_right;
	f2d->detect(leftGray, keypoints_left);
	f2d->detect(rightGray, keypoints_right);
	//***************Calculate descriptors (feature vectors),��������������ȡ
	Mat descriptors_left, descriptors_right;
	f2d->compute(leftGray, keypoints_left, descriptors_left);
	f2d->compute(rightGray, keypoints_right, descriptors_right);

    FlannBasedMatcher matcher;  
    vector<DMatch> matches;
    /* �������������ٽ�ƥ�� */  
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

	//��ȡ���ͼ���ұ�ͼ���ͶӰӳ���ϵ
	Mat homo = findHomography(leftPoints, rightPoints);
	Mat shftMat = (Mat_<double>(3, 3) << 1.0, 0, leftImg.cols, 0, 1.0, 0, 0, 0, 1.0);

	//ƴ��ͼ��  
	Mat tiledImg;
	warpPerspective(leftImg, tiledImg, shftMat*homo, Size(leftImg.cols + rightImg.cols, rightImg.rows));
	rightImg.copyTo(Mat(tiledImg, Rect(leftImg.cols, 0, rightImg.cols, rightImg.rows)));

	//����ͼ��  
	imwrite("tiled.jpg", tiledImg);

	//��ʾƴ�ӵ�ͼ��  
	imshow("tiled image", tiledImg);
	waitKey(0);
	return 0;
}
