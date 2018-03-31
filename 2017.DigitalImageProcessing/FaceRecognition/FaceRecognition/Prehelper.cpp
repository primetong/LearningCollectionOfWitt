//preprocessing helper  
//����װ�ز������ļ���������ͼƬ��read_img�������������detectAndDraw��������ԭͼ�л���������ͼ��CutImg������ȡ(DetectandExtract)
// �Լ�ѵ����ʶ��Recognization()����ִ����������ȷ�ʣ���󷵻ؽ�ģ���Ptr<FaceRecognizer> model
//@ Author : witt
#include "Prehelper.h"  
#include "BrowseDir.h"  
#include "StatDir.h"  

#include <opencv2/core/core.hpp>  
#include <opencv2/highgui/highgui.hpp>  
#include <cv.h>  
using namespace cv;

//����haar�����������ѿ�������Ŀ¼����˲���Ҫ�ټӾ���·��
string cascadeName = "haarcascade_frontalface_alt.xml";
string nestedCascadeName = "haarcascade_eye_tree_eyeglasses.xml";

//0. ������ȡ����ͼƬ�����ָ������������
//����ȡ�������أ���������ͼƬ�Ƚ��м��������detectAndDraw��������ԭͼ�л���������ͼ��CutImg������ȡ(DetectandExtract)
//���Ҫ��ӡÿͼ�������ʱ�䣨detection time�����Խ�detectAndDraw������ȡ����printf�������ʱ���ע��
void detectFaceAndCut(char* dir, int K){
	bool tryflip = false;
	CascadeClassifier cascade, nestedCascade;
	double scale = 1.0;
	if (!cascade.load(cascadeName) || !nestedCascade.load(nestedCascadeName))
	{
		cerr << "ERROR: Could not load classifier cascade or nestedCascade" << endl;//�����ָ�������ȥ���cascadeName��������opencv�汾·������  
		return;
	}

	//  printf("select the mode of detection: \n1: from picture\t 2: from camera\n");  
	//  scanf("%d",&mode);  
			/************************************************************************/
			/*                      detect face and save                            */
			/************************************************************************/
	int i, j;
	cout << "������Ⲣ�ָ���С���" << endl;
	string cur_dir;
	char id[3];
	for (i = 1; i <= K; i++)
	{
		cur_dir = dir;
		//char*itoa(int value,char*string,int radix);���������͵�����ת��Ϊ�ַ�����
		//          int value ��ת����������char *string ת���󴢴���ַ����飬int radix ת������������2, 8, 10, 16 ���Ƶ�
		_itoa(i, id, 10);
		cur_dir.append(id);
		vector<pair<char*, Mat>> imgs = read_img(cur_dir);
		for (j = 0; j<imgs.size(); j++)
		{
			IplImage* res = DetectandExtract(imgs[j].second, cascade, nestedCascade, scale, tryflip);
			if (res)
				cvSaveImage(imgs[j].first, res);
		}
	}
}

//1. Ϊѵ������Ԥ���� תΪ�Ҷȡ����롢��һ�� ��
//תΪ�ҶȺͶ����Ǻ�����ѵ��ʱFaceRecognizer�ࣨEigenfaces��Fisherfaces��LBPH����Ҫ��
//��һ���Ƿ�ֹ���մ�����Ӱ��
void loadResizeAndTogray(char* dir, int K, vector<Mat> &images, vector<int> &labels,
	vector<Mat> &testimages, vector<int> &testlabels, String train_or_predict)
{
	IplImage* standard = cvLoadImage("D:\\OpenCV\\OpenCVLearning\\FaceRecognition\\FaceRecognition\\standard_size\\75x75.jpg", CV_LOAD_IMAGE_GRAYSCALE);
	string cur_dir;
	char id[3];
	int i, j;
	for (int i = 1; i <= K; i++)
	{
		cur_dir = dir;
		_itoa(i, id, 10);
		cur_dir.append(id);
		cur_dir.append("\\" + train_or_predict + "\\");
		const char* dd = cur_dir.c_str();
		CStatDir statdir;
		if (!statdir.SetInitDir(dd))
		{
			puts("Dir not exist");
			return;
		}
		cout << "���ڵ� " << i << " ����װ�ش�" + train_or_predict + "��ͼƬ���ݣ��ܹ��� " << endl;
		vector<char*>file_vec = statdir.BeginBrowseFilenames("*.*");
		for (j = 0; j<file_vec.size(); j++)
		{
			IplImage* cur_img = cvLoadImage(file_vec[j], CV_LOAD_IMAGE_GRAYSCALE);
			cvResize(cur_img, standard, CV_INTER_AREA);
			Mat cur_mat = cvarrToMat(standard, true), des_mat;
			cv::normalize(cur_mat, des_mat, 0, 255, NORM_MINMAX, CV_8UC1);
			cvSaveImage(file_vec[j], cvCloneImage(&(IplImage)des_mat));

			if (train_or_predict == "train")
			{
				images.push_back(des_mat);
				labels.push_back(i);
			}
			else if (train_or_predict == "predict")
			{
				testimages.push_back(des_mat);
				testlabels.push_back(i);
			}

		}
		cout << file_vec.size() << " �š�" << endl;
		//cout << "resize's images.size(): " << images.size() << endl;
		//cout << "resize's labels.size(): " << labels.size() << endl;
		//cout << "resize's testimages.size(): " << testimages.size() << endl;
		//cout << "resize's testlabels.size(): " << testlabels.size() << endl;
	}
}

//2. ѵ����ʶ��
//����vector<Mat> images, testimages;	vector<int> labels, testlabels; ���Կ�ʼѵ���ˣ�Ĭ�ϲ���FaceRecognizer���LBPH��ģ��
//Recognization()����ִ����������ȷ�ʣ���󷵻ؽ�ģ���Ptr<FaceRecognizer> model
Ptr<FaceRecognizer> Recognition(vector<Mat> images, vector<int> labels,
	vector<Mat> testimages, vector<int> testlabels)
{
	//Ptr<FaceRecognizer> model = createEigenFaceRecognizer(10);//10 Principal components  ������eigenface�㷨

	//�ҵ����캯����ԭ�ͽ��Ͳ����ĺ��壺
	//CV_EXPORTS_W Ptr<FaceRecognizer> createLBPHFaceRecognizer(int radius=1, int neighbors=8,  
	//                                                int grid_x = 8, int grid_y = 8, double threshold = DBL_MAX);
	//	int radius = 1 ���������ص㵽��Χ���ص�ľ��룬�������ؾ���Ϊ1���Դ�����
	//	int neighbors = 8 ��ѡȡ����Χ���ص�ĸ���
	//	int grid_x = 8 ����һ��ͼƬ��x����ֳ�8��
	//	int grid_y = 8 ����һ��ͼƬ��y����ֳ�8��
	//	double threshold = DBL_MAX  ��LBP�����������ƶȵ���ֵ��ֻ������ͼƬ�����ƶ�С����ֵ�ſ���Ϊʶ����Ч��������ֵ�򷵻� - 1
	Ptr<FaceRecognizer> model = createLBPHFaceRecognizer(1, 8, 3, 3, 50);	//����LBPH����ʶ����Ķ��󲢳�ʼ��
	model->train(images, labels);
	cout << "trainѵ������" << endl;
	int i, acc = 0, predict_l;
	for (i = 0; i<testimages.size(); i++)
	{
		predict_l = model->predict(testimages[i]);
		if (predict_l != testlabels[i])
		{
			cout << "Ԥ��������һ��Ԥ�����: �� " << i + 1 << " ������, Ԥ���ǵ� " <<
				predict_l << " ��, ����ʵ���� " << testlabels[i] << endl;
			imshow("error pic", testimages[i]);
			waitKey();
		}
		else
			acc++;
	}
	cout << "�ڸ����ݼ��µ�׼ȷ��Ϊ: " << acc*1.0 / testimages.size() << endl;
	//cout << "Recognition's images.size(): " << images.size() << endl;
	//cout << "Recognition's labels.size(): " << labels.size() << endl;
	//cout << "Recognition's testimages.size(): " << testimages.size() << endl;
	//cout << "Recognition's testlabels.size(): " << testlabels.size() << endl;
	return model;
}

void normalizeone(const char* dir, IplImage* standard)
{
	CStatDir statdir;
	if (!statdir.SetInitDir(dir))
	{
		puts("Dir not exist");
		return;
	}
	vector<char*>file_vec = statdir.BeginBrowseFilenames("*.*");
	int i;
	for (i = 0; i<file_vec.size(); i++)
	{
		IplImage* cur_img = cvLoadImage(file_vec[i], CV_LOAD_IMAGE_GRAYSCALE);
		//IplImage*cur_gray = cvCreateImage(cvGetSize(cur_img),cur_img->depth,1);  
		cvResize(cur_img, standard, CV_INTER_AREA);
		//cvCvtColor(standard,cur_gray,CV_RGB2GRAY);  
		//      cvNamedWindow("cur_img",CV_WINDOW_AUTOSIZE);  
		//      cvNamedWindow("standard",CV_WINDOW_AUTOSIZE);  
		//      cvShowImage("cur_img",cur_img);  
		//      cvShowImage("standard",standard);  
		//      cvWaitKey();  
		cvSaveImage(file_vec[i], cur_img);
	}
}

void CutImg(IplImage* src, CvRect rect, IplImage* res)
{
	CvSize imgsize;
	imgsize.height = rect.height;
	imgsize.width = rect.width;
	cvSetImageROI(src, rect);
	cvCopy(src, res);
	cvResetImageROI(res);
}

int read_img(const string& dir, vector<Mat> &images)
{
	CStatDir statdir;
	if (!statdir.SetInitDir(dir.c_str()))
	{
		cout << "Direct " << dir << "  not exist!" << endl;
		return 0;
	}
	int cls_id = dir[dir.length() - 1] - '0';
	vector<char*>file_vec = statdir.BeginBrowseFilenames("*.*");
	int i, s = file_vec.size();
	for (i = 0; i<s; i++)
	{
		Mat graymat = imread(file_vec[i], 0);
		//graymat.reshape(1,1);//flatten to one row  
		images.push_back(graymat);
	}
	return s;
}

vector<pair<char*, Mat>>  read_img(const string& dir)
{
	CStatDir statdir;
	pair<char*, Mat> pfi;
	vector<pair<char*, Mat>> Vp;
	if (!statdir.SetInitDir(dir.c_str()))
	{
		cout << "Direct " << dir << "  not exist!" << endl;
		return Vp;
	}
	int cls_id = dir[dir.length() - 1] - '0';
	vector<char*>file_vec = statdir.BeginBrowseFilenames("*.*");
	int i, s = file_vec.size();
	for (i = 0; i<s; i++)
	{
		pfi.first = file_vec[i];
		pfi.second = imread(file_vec[i]);
		Vp.push_back(pfi);
	}
	return Vp;
}

vector<Rect> detectAndDraw(Mat& img, CascadeClassifier& cascade,
	CascadeClassifier& nestedCascade,
	double scale, bool tryflip, bool draw)
{
	int i = 0;
	double t = 0;
	vector<Rect> faces, faces2;
	const static Scalar colors[] = { CV_RGB(0, 0, 255),
		CV_RGB(0, 128, 255),
		CV_RGB(0, 255, 255),
		CV_RGB(0, 255, 0),
		CV_RGB(255, 128, 0),
		CV_RGB(255, 255, 0),
		CV_RGB(255, 0, 0),
		CV_RGB(255, 0, 255) };
	Mat gray, smallImg(cvRound(img.rows / scale), cvRound(img.cols / scale), CV_8UC1);

	cvtColor(img, gray, CV_BGR2GRAY);
	resize(gray, smallImg, smallImg.size(), 0, 0, INTER_LINEAR);
	equalizeHist(smallImg, smallImg);

	t = (double)cvGetTickCount();
	cascade.detectMultiScale(smallImg, faces,
		1.1, 2, 0
		| CV_HAAR_FIND_BIGGEST_OBJECT
		//|CV_HAAR_DO_ROUGH_SEARCH  
		//|CV_HAAR_SCALE_IMAGE  
		,
		Size(30, 30));
	if (tryflip)
	{
		flip(smallImg, smallImg, 1);
		cascade.detectMultiScale(smallImg, faces2,
			1.1, 2, 0
			| CV_HAAR_FIND_BIGGEST_OBJECT
			//|CV_HAAR_DO_ROUGH_SEARCH  
			//|CV_HAAR_SCALE_IMAGE  
			,
			Size(30, 30));
		for (vector<Rect>::const_iterator r = faces2.begin(); r != faces2.end(); r++)
		{
			faces.push_back(Rect(smallImg.cols - r->x - r->width, r->y, r->width, r->height));
		}
	}
	t = (double)cvGetTickCount() - t;
	//printf("�����������ʱ�� = %g ms\n", t / ((double)cvGetTickFrequency()*1000.));
	if (draw)
	{
		for (vector<Rect>::const_iterator r = faces.begin(); r != faces.end(); r++, i++)
		{
			Mat smallImgROI;
			vector<Rect> nestedObjects;
			Point center;
			Scalar color = colors[i % 8];
			int radius;

			double aspect_ratio = (double)r->width / r->height;
			rectangle(img, cvPoint(cvRound(r->x*scale), cvRound(r->y*scale)),
				cvPoint(cvRound((r->x + r->width - 1)*scale), cvRound((r->y + r->height - 1)*scale)),
				color, 3, 8, 0);
			if (nestedCascade.empty())
				continue;
			smallImgROI = smallImg(*r);
			nestedCascade.detectMultiScale(smallImgROI, nestedObjects,
				1.1, 2, 0
				| CV_HAAR_FIND_BIGGEST_OBJECT
				//|CV_HAAR_DO_ROUGH_SEARCH  
				//|CV_HAAR_DO_CANNY_PRUNING  
				//|CV_HAAR_SCALE_IMAGE  
				,
				Size(30, 30));
			//draw eyes  
			//         for( vector<Rect>::const_iterator nr = nestedObjects.begin(); nr != nestedObjects.end(); nr++ )  
			//         {  
			//             center.x = cvRound((r->x + nr->x + nr->width*0.5)*scale);  
			//             center.y = cvRound((r->y + nr->y + nr->height*0.5)*scale);  
			//             radius = cvRound((nr->width + nr->height)*0.25*scale);  
			//             circle( img, center, radius, color, 3, 8, 0 );  
			//         }  
		}
		cv::imshow("result", img);
	}
	return faces;
}

IplImage* DetectandExtract(Mat& img, CascadeClassifier& cascade,
	CascadeClassifier& nestedCascade,
	double scale, bool tryflip)
{
	vector<Rect> Rvec = detectAndDraw(img, cascade, nestedCascade, scale, tryflip, 0);
	int i, maxxsize = 0, id = -1, area;
	for (i = 0; i<Rvec.size(); i++)
	{
		area = Rvec[i].width*Rvec[i].height;
		if (maxxsize<area)
		{
			maxxsize = area;
			id = i;
		}
	}
	IplImage* transimg = cvCloneImage(&(IplImage)img);
	if (id != -1)
	{
		CvSize imgsize;
		imgsize.height = Rvec[id].height;
		imgsize.width = Rvec[id].width;
		IplImage* res = cvCreateImage(imgsize, transimg->depth, transimg->nChannels);
		CutImg(transimg, Rvec[id], res);

		return res;
	}
	return NULL;
}