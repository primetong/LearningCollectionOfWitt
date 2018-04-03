//-*- ������� -*-  
//-*- author : witt -*-

#include <opencv.hpp> 
using namespace std;
using namespace cv;

CascadeClassifier faceDetection;		//ʵ������������������

int main(){
	vector<Rect> faces;

	bool xmlfound = faceDetection.load("haarcascade_frontalface_alt2.xml");
	if (!xmlfound){
		printf("û���ҵ��ü�������������������·��\n");
		return -1;
	}

	Mat img = imread("Lenna.jpg");
	Mat imgGray;
	if (img.empty())
		printf("����Ŀ¼��û���ҵ���ͼƬ!");

	cvtColor(img, imgGray, CV_RGB2GRAY);	//תΪ�Ҷ�ͼ
	//equalizeHist(imgGray, imgGray);   //ֱ��ͼ���⻯

	faceDetection.detectMultiScale(imgGray,     //const Mat& image,�����ͼƬ��һ��Ϊ�Ҷ�ͼ��ӿ����ٶȣ�
		faces,    //CV_OUT vector<Rect>& objects,���������ľ��ο������飻
		1.1,    //double scaleFactor = 1.1,��ʾ��ǰ��������̵�ɨ���У��������ڵı���ϵ����Ĭ��Ϊ1.1��ÿ������������������10%;
		3,    /*int minNeighbors = 3,��ʾ���ɼ��Ŀ������ھ��ε���С����(Ĭ��Ϊ3��)��
				�����ɼ��Ŀ���С���εĸ�����С�� min_neighbors - 1 ���ᱻ�ų���
				���min_neighbors Ϊ 0, ���������κβ����ͷ������еı����ѡ���ο�
				�����趨ֵһ�������û��Զ���Լ��������ϳ����ϣ�*/
		0,    /*int flags = 0,Ҫôʹ��Ĭ��ֵ��Ҫôʹ��CV_HAAR_DO_CANNY_PRUNING���������Ϊ
				CV_HAAR_DO_CANNY_PRUNING����ô��������ʹ��Canny��Ե������ų���Ե�������ٵ�����
				�����Щ����ͨ��������������������*/
		Size(0, 0)    //minSize��maxSize�������Ƶõ���Ŀ������ķ�Χ��
		);
	if (faces.size() > 0)
	{
		for (int i = 0; i < faces.size(); i++)
		{
			rectangle(img, Point(faces[i].x, faces[i].y), 
				Point(faces[i].x + faces[i].width, faces[i].y + faces[i].height),
				Scalar(0, 0, 255), 2, 8);	/*�������λ��,rectangle(Mat& img, Point pt1,Point pt2,
											const Scalar& color, int thickness=1, int lineType=8, int shift=0)
											imgͼ��pt1���ε�һ�����㡣pt2���ζԽ����ϵ���һ�����㡣
											color������ɫ (RGB) �����ȣ��Ҷ�ͼ�� ��(grayscale image����
											thickness��ɾ��ε������Ĵ�ϸ�̶ȡ�ȡ��ֵʱ���� CV_FILLED���������������ɫ�ʵľ��Ρ�
											line_type���������ͣ���cvLine��������
											shift������С����λ����*/
		}
	}

	imshow("���������", img);
	waitKey(0);

	return 0;
}

/*
��������㷨�Ŀɿ��Ժܴ�̶��������ڷ���������ƣ���2001�꣬
Viola��Jones��λ��ţ�����˾���ġ�Rapid Object Detection using a Boosted Cascade of Simple Features��
�͡�Robust Real - Time Face Detection����2������AdaBoost�㷨�Ļ����ϣ�ʹ��Haar - likeС�������ͻ���ͼ��������������⣬
������������ʹ�����С�������ģ���������������������������Ч������������AdaBoostѵ������ǿ���������м�����
�����˵���������ʷ����̱�ʽ��һ���ˣ�Ҳ��˵�ʱ���������㷨����ΪViola - Jones�������
�ֹ���һ��ʱ�䣬Rainer Lienhart��Jochen Maydt��λ��ţ������������������չ��3����
�����γ���OpenCV���ڵ�Haar����������OpenCV2.0���������˻���LBP�����������������ĳЩ�����LBP������Haar���ĸ�Ϊ����
*/

/*
�ڽ���ʶ��ʱ����ͨ�������ľ��бȽ����Ե�haar���������Σ�������ͼ����ģʽʶ��ķ���ѵ������������
�������Ǹ������ģ�ÿ�����Դ����ͬ��ʶ���ʱ���������һ���ľ������������ĺ�ѡ���壬
��ÿһ�����ӷ������������haar�������ɣ��ɻ���ͼ�����õ�����������λ�ã�����ˮƽ�ġ���ֱ�ġ���б�ģ�
����ÿ��������һ����ֵ��������ֵ֧��ÿ���ӷ�������һ���ܵ���ֵ��ʶ�������ʱ��ͬ���������ͼ��Ϊ�������haar������׼����
Ȼ�������ѵ����ʱ��������Ĵ���ͬ����С�Ĵ��ڱ�������ͼ���Ժ��𽥷Ŵ󴰿ڣ�ͬ���������������壻ÿ�������ƶ���һ��λ�ã�
������ô����ڵ�haar��������Ȩ�����������haar��������ֵ�ȽϴӶ�ѡ��������ҷ�ֵ֧���ۼ�һ�����ķ�ֵ֧����Ӧ������ֵ�Ƚϣ�
���ڸ���ֵ�ſ���ͨ��������һ��ɸѡ����ͨ�����������м���ʱ��˵����������Դ���ʱ�ʶ��
*/
