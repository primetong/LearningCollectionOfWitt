//preprocessing helper  
//负责装载并返回文件夹内所有图片（read_img），检测人脸（detectAndDraw并可以在原图中画出），截图（CutImg），提取(DetectandExtract)
// 以及训练和识别，Recognization()输出分错的样本和正确率，最后返回建模结果Ptr<FaceRecognizer> model
//@ Author : witt
#include "Prehelper.h"  
#include "BrowseDir.h"  
#include "StatDir.h"  

#include <opencv2/core/core.hpp>  
#include <opencv2/highgui/highgui.hpp>  
#include <cv.h>  
using namespace cv;

//这里haar特征级联器已拷至工程目录，因此不需要再加绝对路径
string cascadeName = "haarcascade_frontalface_alt.xml";
string nestedCascadeName = "haarcascade_eye_tree_eyeglasses.xml";

//0. 处理爬取到的图片集，分割出人脸并保存
//将爬取（或下载）到的网络图片先进行检测人脸（detectAndDraw并可以在原图中画出），截图（CutImg），提取(DetectandExtract)
//如果要打印每图人脸检测时间（detection time）可以进detectAndDraw函数中取消掉printf人脸检测时间的注释
void detectFaceAndCut(char* dir, int K){
	bool tryflip = false;
	CascadeClassifier cascade, nestedCascade;
	double scale = 1.0;
	if (!cascade.load(cascadeName) || !nestedCascade.load(nestedCascadeName))
	{
		cerr << "ERROR: Could not load classifier cascade or nestedCascade" << endl;//若出现该问题请去检查cascadeName，可能是opencv版本路径问题  
		return;
	}

	//  printf("select the mode of detection: \n1: from picture\t 2: from camera\n");  
	//  scanf("%d",&mode);  
			/************************************************************************/
			/*                      detect face and save                            */
			/************************************************************************/
	int i, j;
	cout << "人脸检测并分割保存中……" << endl;
	string cur_dir;
	char id[3];
	for (i = 1; i <= K; i++)
	{
		cur_dir = dir;
		//char*itoa(int value,char*string,int radix);将任意类型的数字转换为字符串。
		//          int value 被转换的整数，char *string 转换后储存的字符数组，int radix 转换进制数，如2, 8, 10, 16 进制等
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

//1. 为训练数据预处理（ 转为灰度、对齐、归一化 ）
//转为灰度和对齐是后面做训练时FaceRecognizer类（Eigenfaces，Fisherfaces，LBPH）的要求；
//归一化是防止光照带来的影响
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
		cout << "正在第 " << i << " 类中装载待" + train_or_predict + "的图片数据，总共有 " << endl;
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
		cout << file_vec.size() << " 张。" << endl;
		//cout << "resize's images.size(): " << images.size() << endl;
		//cout << "resize's labels.size(): " << labels.size() << endl;
		//cout << "resize's testimages.size(): " << testimages.size() << endl;
		//cout << "resize's testlabels.size(): " << testlabels.size() << endl;
	}
}

//2. 训练与识别
//有了vector<Mat> images, testimages;	vector<int> labels, testlabels; 可以开始训练了，默认采用FaceRecognizer类的LBPH建模。
//Recognization()输出分错的样本和正确率，最后返回建模结果Ptr<FaceRecognizer> model
Ptr<FaceRecognizer> Recognition(vector<Mat> images, vector<int> labels,
	vector<Mat> testimages, vector<int> testlabels)
{
	//Ptr<FaceRecognizer> model = createEigenFaceRecognizer(10);//10 Principal components  特征：eigenface算法

	//找到构造函数的原型解释参数的含义：
	//CV_EXPORTS_W Ptr<FaceRecognizer> createLBPHFaceRecognizer(int radius=1, int neighbors=8,  
	//                                                int grid_x = 8, int grid_y = 8, double threshold = DBL_MAX);
	//	int radius = 1 ：中心像素点到周围像素点的距离，相邻像素距离为1，以此类推
	//	int neighbors = 8 ：选取的周围像素点的个数
	//	int grid_x = 8 ：将一张图片在x方向分成8块
	//	int grid_y = 8 ：将一张图片在y方向分成8块
	//	double threshold = DBL_MAX  ：LBP特征向量相似度的阈值，只有两张图片的相似度小于阈值才可认为识别有效，大于阈值则返回 - 1
	Ptr<FaceRecognizer> model = createLBPHFaceRecognizer(1, 8, 3, 3, 50);	//构造LBPH人脸识别类的对象并初始化
	model->train(images, labels);
	cout << "train训练结束" << endl;
	int i, acc = 0, predict_l;
	for (i = 0; i<testimages.size(); i++)
	{
		predict_l = model->predict(testimages[i]);
		if (predict_l != testlabels[i])
		{
			cout << "预测结果产生一个预测错误: 第 " << i + 1 << " 个样本, 预测是第 " <<
				predict_l << " 类, 但是实际是 " << testlabels[i] << endl;
			imshow("error pic", testimages[i]);
			waitKey();
		}
		else
			acc++;
	}
	cout << "在该数据集下的准确率为: " << acc*1.0 / testimages.size() << endl;
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
	//printf("本次人脸检测时间 = %g ms\n", t / ((double)cvGetTickFrequency()*1000.));
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