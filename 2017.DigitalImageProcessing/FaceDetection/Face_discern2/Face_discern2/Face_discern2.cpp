#include "stdafx.h"  
#include "opencv2\core\core.hpp"
#include "opencv2\objdetect\objdetect.hpp"
#include "opencv2\highgui\highgui.hpp"
#include "opencv2\imgproc\imgproc.hpp"
#include <iostream>
#include <stdio.h>

using namespace std;//������
using namespace cv;

string face_cascade_name = "haarcascade_frontalface_alt.xml";//�沿������������harr������������ѡ������������
CascadeClassifier face_cascade;//���ڼ�������ķ�����
string window_name0 = "ԭͼ";//��һ����ͼ����
string window_name1 = "ͼһ������ȦȦ";
string window_name2 = "ͼ����ֻ����";
string window_name3 = "ͼ�壺���и������������Ҷȣ����������롯ʵ�֣�";
string window_name4 = "ͼ�������и�������������ɫ����ͨ����ѡ��ɫ��ʵ�֣�";
string window_name5 = "ͼ�ģ��Ҷȶ�ֵ��";
string window_name6 = "ͼ�����ҶȻ�";
string window_name7 = "ֱ��ͼ���⻯���Ч��";
void detectAndDisplay(Mat frame);

int main(int argc, char** argv){
	Mat image0, image1, thresholded, biner, biner1,image2;//opencv�����ͼ���������
	//Mat frame_slice_face_total, frame_slice_face;
	//image = imread(argv[1]);//���������ȡͼƬ,����ͨ����ûɶ��Ҫ������
	image1 = imread("lyf.jpg");//���������ȡͼƬ
	//image0 = image1;
	//cvtColor(image1, biner, CV_BGR2GRAY);//�ҶȻ�
	//imshow(window_name6, biner);
	//equalizeHist(biner, biner1);//ǰin��out
	//imshow(window_name7, biner1);

	//threshold(biner, thresholded, 100, 255, cv::THRESH_BINARY);//��ֵ��
	//imshow(window_name5, thresholded);
	//imwrite("mei.jpg", thresholded);


	if (argc != 2 || !image1.data){
		printf("[error] û��ͼƬ\n");
		return -1;
	}

	if (!face_cascade.load(face_cascade_name)){
		printf("[error] �޷����ؼ����������ļ���\n");
		return -1;
	}

	detectAndDisplay(image1);//��ȡ������ʾ������������Ӧ��
	waitKey(0);
}

void detectAndDisplay(Mat frame){
	std::vector<Rect> faces,conters;//�������������࣬�����ű�ʾ�����RECT��ʵ���࣬���嶨����faces������һ��RECT���顣 ���ö���������С
	Mat frame_gray;
	Mat frame_slice(frame.rows, frame.cols, frame.type(), Scalar(180, 120, 50));//���ʱ���������С
	Mat frame_slice_gray, frame_slice_binerary, frame_slice_binerary_slice;
	Mat frame_slice_binerary_slice_color(frame.rows, frame.cols, frame.type(), Scalar(180, 120, 50));//���ʱ���������С
	cvtColor(frame, frame_gray, CV_BGR2GRAY);//��Ҫ���лҶȻ��������Ѵ����mat����ҶȻ������frame_gray����
	//equalizeHist(frame_gray, frame_gray);//��֪��Ϊʲôֱ��ͼ���⻯����Ե���ֽ���ǿ������������ֱܷ����ǲ���������һ���֣��������ʦ��ѯ����

	face_cascade.detectMultiScale(frame_gray, faces, 1.1, 2, 0 | CV_HAAR_SCALE_IMAGE, Size(30, 30));//�ڵ��ÿ����������ȡ������
	//face_cascade.detectMultiScale(frame_gray, faces, 1.1, 2, 0 | CV_HAAR_DO_CANNY_PRUNING, Size(30, 30));����һ��canny��Ե���

	//conters = faces;
	//����������������
	for (int i = 0; i < faces.size(); i++)
	{
		Point2d center(faces[i].x + faces[i].width*0.55, faces[i].y + faces[i].height*0.43);//����λ��,faces[i]��ָ�ĵڼ���������ÿһ��������Ҫ�㶮��Ѫ�Ľ�ѵ��
		ellipse(frame, center, Size(faces[i].width*0.52, faces[i].height*0.65), 0, 0, 360, Scalar(255, 0, 0), 4, 8, 0);//ʹ����Բ
		//rectangle(frame, faces[i].x + faces[i].width*0.58, faces[i].y + faces[i].height*0.43, Scalar(255, 0, 0));//ʹ�þ���

		//���������������(ʹ����Բ)
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
		//�о�һ������������
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
	//�ҶȻ��Լ���ֵ��
	cvtColor(frame_slice, frame_slice_gray, CV_BGR2GRAY);//�ҶȻ�
	threshold(frame_slice_gray, frame_slice_binerary, 130, 255, cv::THRESH_BINARY);//��ֵ�����޷���Ч�и������벱�Ӳ��ֵ�������Ϊ����и��̫low����Ҫ��Ե��ȡ�㷨��ʵ�֣���ֵ��Ҫ����Ӧ(�������ó�����Ӧ)
	//���������ʵ���и�Ҷ�ͼ
	bitwise_and(frame_slice_binerary, frame_slice_gray, frame_slice_binerary_slice);//���ܵõ�ԭ�Ҷ�ͼλ�õ�ֵ��160��255��õ���ֵ��
	//���ö�ֵ���Ľ�����õ�ԭ��ɫͼ����������ɫ��
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

	//��ʾ
	imshow(window_name1, frame);
	imshow(window_name2, frame_slice);
	imshow(window_name6, frame_slice_gray);
	imshow(window_name5, frame_slice_binerary);
	imshow(window_name3, frame_slice_binerary_slice);
	imshow(window_name4, frame_slice_binerary_slice_color);

}