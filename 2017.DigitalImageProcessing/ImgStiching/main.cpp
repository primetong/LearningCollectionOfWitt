#include <opencv2/opencv.hpp>  
#include "highgui/highgui.hpp"    
#include <opencv2/xfeatures2d.hpp>

using namespace cv;
using namespace std;

//����ԭʼͼ���λ�ھ�������任����Ŀ��ͼ���϶�Ӧλ��  
Point2f getTransformPoint(const Point2f originalPoint, const Mat &transformMaxtri);

int main(int argc, char *argv[])
{
	Mat image01 = imread("left2.jpg");
	Mat image02 = imread("right2.jpg");

	if (image01.data == NULL || image02.data == NULL)
		return 0;
	imshow("��ƴ��ͼ����ͼ", image01);
	imshow("��ƴ��ͼ����ͼ", image02);

	//�Ҷ�ͼת��  
	Mat image1, image2;
	cvtColor(image01, image1, CV_RGB2GRAY);
	cvtColor(image02, image2, CV_RGB2GRAY);

	//��ȡ������    
	Ptr<Feature2D> f2d = xfeatures2d::SIFT::create();	//�޸�SIFT���������޸��㷨������SURF��
	vector<KeyPoint> keyPoint1, keyPoint2;
	f2d->detect(image1, keyPoint1);
	f2d->detect(image2, keyPoint2);

	//������������Ϊ�±ߵ�������ƥ����׼��    
	Mat imageDesc1, imageDesc2;
	f2d->compute(image1, keyPoint1, imageDesc1);
	f2d->compute(image2, keyPoint2, imageDesc2);

	//���ƥ�������㣬����ȡ�������     
	FlannBasedMatcher matcher;
	vector<DMatch> matchePoints;
	matcher.match(imageDesc1, imageDesc2, matchePoints, Mat());
	sort(matchePoints.begin(), matchePoints.end()); //����������    
	//��ȡ����ǰN��������ƥ��������  
	vector<Point2f> imagePoints1, imagePoints2;
	for (int i = 0; i<10; i++)
	{
		imagePoints1.push_back(keyPoint1[matchePoints[i].queryIdx].pt);
		imagePoints2.push_back(keyPoint2[matchePoints[i].trainIdx].pt);
	}

	//��ȡͼ��1��ͼ��2��ͶӰӳ����󣬳ߴ�Ϊ3*3  
	Mat homo = findHomography(imagePoints1, imagePoints2, CV_RANSAC);
	Mat adjustMat = (Mat_<double>(3, 3) << 1.0, 0, image01.cols, 0, 1.0, 0, 0, 0, 1.0);
	Mat adjustHomo = adjustMat*homo;

	//��ȡ��ǿ��Ե���ԭʼͼ��;���任��ͼ���ϵĶ�Ӧλ�ã�����ͼ��ƴ�ӵ�Ķ�λ  
	Point2f originalLinkPoint, targetLinkPoint, basedImagePoint;
	originalLinkPoint = keyPoint1[matchePoints[0].queryIdx].pt;
	targetLinkPoint = getTransformPoint(originalLinkPoint, adjustHomo);
	basedImagePoint = keyPoint2[matchePoints[0].trainIdx].pt;

	//ͼ����׼  
	Mat imageTransform1;
	warpPerspective(image01, imageTransform1, adjustMat*homo, Size(image02.cols + image01.cols + 110, image02.rows));

	//����ǿƥ��������ص���������ۼӣ����ν��ȶ����ɣ�����ͻ��  
	Mat image1Overlap, image2Overlap; //ͼ1��ͼ2���ص�����     
	image1Overlap = imageTransform1(Rect(Point(targetLinkPoint.x - basedImagePoint.x, 0), Point(targetLinkPoint.x, image02.rows)));
	image2Overlap = image02(Rect(0, 0, image1Overlap.cols, image1Overlap.rows));
	Mat image1ROICopy = image1Overlap.clone();  //����һ��ͼ1���ص�����  
	for (int i = 0; i<image1Overlap.rows; i++)
	{
		for (int j = 0; j<image1Overlap.cols; j++)
		{
			double weight;
			weight = (double)j / image1Overlap.cols;  //�����ı���ı�ĵ���ϵ��  
			image1Overlap.at<Vec3b>(i, j)[0] = (1 - weight)*image1ROICopy.at<Vec3b>(i, j)[0] + weight*image2Overlap.at<Vec3b>(i, j)[0];
			image1Overlap.at<Vec3b>(i, j)[1] = (1 - weight)*image1ROICopy.at<Vec3b>(i, j)[1] + weight*image2Overlap.at<Vec3b>(i, j)[1];
			image1Overlap.at<Vec3b>(i, j)[2] = (1 - weight)*image1ROICopy.at<Vec3b>(i, j)[2] + weight*image2Overlap.at<Vec3b>(i, j)[2];
		}
	}
	Mat ROIMat = image02(Rect(Point(image1Overlap.cols, 0), Point(image02.cols, image02.rows)));  //ͼ2�в��غϵĲ���  
	ROIMat.copyTo(Mat(imageTransform1, Rect(targetLinkPoint.x, 0, ROIMat.cols, image02.rows))); //���غϵĲ���ֱ���ν���ȥ  
	namedWindow("ƴ�ӽ��-SIFT", 0);
	imshow("ƴ�ӽ��-SIFT", imageTransform1);
	imwrite("ƴ�ӽ��-SIFT.jpg", imageTransform1);
	waitKey();
	return 0;
}

//����ԭʼͼ���λ�ھ�������任����Ŀ��ͼ���϶�Ӧλ��  
Point2f getTransformPoint(const Point2f originalPoint, const Mat &transformMaxtri)
{
	Mat originelP, targetP;
	originelP = (Mat_<double>(3, 1) << originalPoint.x, originalPoint.y, 1.0);
	targetP = transformMaxtri*originelP;
	float x = targetP.at<double>(0, 0) / targetP.at<double>(2, 0);
	float y = targetP.at<double>(1, 0) / targetP.at<double>(2, 0);
	return Point2f(x, y);
}